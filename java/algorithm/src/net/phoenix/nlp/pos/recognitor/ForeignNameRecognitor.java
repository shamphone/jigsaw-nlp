/**
 * 
 */
package net.phoenix.nlp.pos.recognitor;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import net.phoenix.nlp.Nature;
import net.phoenix.nlp.Term;
import net.phoenix.nlp.corpus.CorpusRepository;
import net.phoenix.nlp.pos.POSTerm;
import net.phoenix.nlp.pos.TermEdge;
import net.phoenix.nlp.pos.TermGraph;
import net.phoenix.nlp.pos.TermNatures;
import net.phoenix.nlp.pos.TermPath;
import net.phoenix.nlp.pos.corpus.CharsetCorpus;
import net.phoenix.nlp.pos.corpus.CooccurrenceCorpus;
import net.phoenix.nlp.pos.corpus.file.CharsetFileCorpus;
import net.phoenix.nlp.pos.corpus.file.CooccurrenceFileCorpus;

/**
 * @author lixf
 * 
 */
public class ForeignNameRecognitor extends NameRecognitor {
	
	private char[] candidates;
	private CooccurrenceCorpus cooccurrence;
	
	public ForeignNameRecognitor(CorpusRepository dictionary) throws IOException {
		super(dictionary);
		CharsetCorpus chars = dictionary.getCorpus(CharsetFileCorpus.class);
		this.candidates = chars.getChars("person");
		Arrays.sort(this.candidates);
		this.cooccurrence = dictionary.getCorpus(CooccurrenceFileCorpus.class);
	}


	/**
	 * 从TermGraph中识别出名次
	 * 
	 * @param graph
	 * @return
	 */
	protected void recognize(TermGraph graph) {
		this.resetEdgesScore(graph, 0.0);
		// 从每一个点边开始检查其成词的可能性;
		// 系统将生成一些新的节点，这样可以避免同时修改一个集合的问题。
		List<POSTerm> currentVertexes = new ArrayList<POSTerm>(graph.vertexSet());
		for (POSTerm start : currentVertexes) {
			// 这个词是否可以作为长度为length的名字的开头词；
			if (this.canBeNameStartTerm(start)) {
				TermPath candidate = graph.createPath(start);
				this.findNames(graph, candidate);
			}

		}
	}

	/**
	 * 这个词是否可以作为长度为length的名字的开始（即 姓）；
	 * 
	 * @param term
	 * @param length
	 * @return
	 */
	private boolean canBeNameStartTerm(POSTerm term) {
		return this.isFName(term);
	}

	/**
	 * 这个词是否可以出现在长度为length名字的第pos个位置上。位置从0开始算；
	 * 
	 * @param term
	 * @param length
	 * @param pos
	 * @return
	 */
	private boolean canAppearInName(POSTerm term, int pos) {
		//PersonTermAttribute attr = (PersonTermAttribute) term.getTermNatures().getAttribute(PersonTermAttribute.ATTRIBUTE);
		return this.isFName(term);

	}

	/**
	 * 通过遍历来发现长度为length的词。
	 * 
	 * @param graph
	 *            句图
	 * @param partName
	 *            当前已经发现的名字片段或者名字
	 * @param length
	 *            待发现的名字长度；
	 * @param names
	 *            用来存放发现的结果；
	 */
	private void findNames(TermGraph graph, TermPath partName) {
		int pos = partName.getVertexCount();
		POSTerm current = partName.getEndVertex();
		for (TermEdge edge : graph.outgoingEdgesOf(current)) {
			// 对于长度为N的名字，往前走一步，到N+1，检查下路径是否可以组成名字；
			// 判断新增加的这条边目标节点是否可以作为名字的第N个字符；
			POSTerm next = graph.getEdgeTarget(edge);
			if (this.canAppearInName(next, pos)) {
				partName.extendTo(next);
				// 继续发现人名；
				this.findNames(graph, partName);
				partName.removeEnd();

			} else {
				this.checkNamePath(graph, partName);
			}
		}

	}

	/**
	 * 检查存放在这个list中的name是否可以构成名字；如果可以构成名字，则建立名字节点，并对边打分。 
	 * 名字边打分的算法同中国人名。区别是名字频率计算直接用名字长度来。 
	 * 这个算法和asj算法不一样。
	 * 以后要改进为每个字在外国人名中出现的频率来计算名字频率，类似中国人名。 
	 * 
	 * 
	 * @param path
	 */
	private Term checkNamePath(TermGraph graph, TermPath partName) {
		int length = partName.getName().length();
		if (length < 2)
			return null;
		
		double score = 0;
		// 平滑处理
		score -= Math.log(length);
		// 分为为前置和后置edge；
		score /= 2.0;

		POSTerm name = partName.toTerm(this.createTermNatures(Nature.PersonName));

		// 将原来指向name第一个词的起始词，都建立指向新的name节点的连接。
		for (TermEdge edge : graph.incomingEdgesOf(partName.getStartVertex())) {
			POSTerm leading = graph.getEdgeSource(edge);
			TermEdge newEdge = graph.addEdge(leading, name);
			// 使用Viterbi算法计算前驱权重
			newEdge.setWeight(score - Math.log(this.calcLeadingFrequency(graph, name, leading)));
		}

		// 将原来name最后一个词的所有外向连接，都建立指向新的name节点的外向连接。
		for (TermEdge edge : graph.outgoingEdgesOf(partName.getEndVertex())) {
			POSTerm following = graph.getEdgeTarget(edge);
			// 如果名字最后一个词和后面的词结合紧密，则不建立连接。否则建立连接。
			if (following.equals(graph.getEndVertex()) || this.cooccurrence.getCooccurrenceFrequency(partName.getEndVertex().getName(), following.getName()) < 3) {
				TermEdge newEdge = graph.addEdge(name, following);
				// 使用Viterbi算法计算后缀权重
				newEdge.setWeight(score - Math.log(this.calcFollowingFrequency(graph, name, following)));

			}
		}

		return name;
	}

	private boolean isFName(POSTerm term) {
		TermNatures termNatures = term.getTermNatures();
		String name = term.getName();
		//if (termNatures.isNature(Nature.PersonName) || termNatures.isNature(Nature.NULL) || name.length() == 1)
		//	return isFName(name);		
		//return false;
		return isFName(name);
	}

	public boolean isFName(String name) {
		for (int i = 0; i < name.length(); i++) {
			if (Arrays.binarySearch(this.candidates, name.charAt(i)) < 0) {
				return false;
			}
		}
		return true;
	}

}
