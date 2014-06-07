/**
 * 
 */
package net.phoenix.nlp.pos.recognitor;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import net.phoenix.nlp.Nature;
import net.phoenix.nlp.corpus.CorpusRepository;
import net.phoenix.nlp.pos.POSTerm;
import net.phoenix.nlp.pos.TermEdge;
import net.phoenix.nlp.pos.TermGraph;
import net.phoenix.nlp.pos.TermPath;
import net.phoenix.nlp.pos.corpus.CooccurrenceCorpus;
import net.phoenix.nlp.pos.corpus.PersonTermAttribute;
import net.phoenix.nlp.pos.corpus.file.CooccurrenceFileCorpus;

/**
 * 亚洲（中文）名识别，针对2-4个字的名字识别。
 * 
 * @author lixf
 * 
 */
public class AsianNameRecognitor extends NameRecognitor {

	private static final double[] FACTORY = { 0.16271366224044456, 0.8060521860870434, 0.031234151672511947 };
	private static int NAME_LENGTH_MIN = 2;
	private static int NAME_LENGTH_MAX = 4;
	private CooccurrenceCorpus cooccurrence;

	public AsianNameRecognitor(CorpusRepository dictionary) throws IOException {
		super(dictionary);
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
		List<POSTerm> foundNames = new ArrayList<POSTerm>();
		// 从每一个点边开始检查其成词的可能性;
		// 系统将生成一些新的节点，这样可以避免同时修改一个集合的问题。
		List<POSTerm> currentVertexes = new ArrayList<POSTerm>(graph.vertexSet());
		for (POSTerm start : currentVertexes) {
			PersonTermAttribute attr = (PersonTermAttribute) start.getTermNatures().getAttribute(PersonTermAttribute.ATTRIBUTE);
			if (attr != null && attr.canBeFirstLetter()) {
				for (int length = NAME_LENGTH_MIN; length <= NAME_LENGTH_MAX; length++) {
					// 这个词是否可以作为长度为length的名字的开头词；
					if (this.canBeNameStartTerm(start, length)) {
						TermPath candidate = graph.createPath(start);
						this.findNames(graph, candidate, length, foundNames);
					}
				}
			}
		}
	}

	/**
	 * 这个（已登录）词是否可以作为长度为length的名字的开始（即 姓）； 在person前缀和后缀表中寻找这个term对应的作为前缀的频率。
	 * 
	 * @param term
	 * @param length
	 * @return
	 */
	private boolean canBeNameStartTerm(POSTerm term, int length) {
		PersonTermAttribute attr = (PersonTermAttribute) term.getTermNatures().getAttribute(PersonTermAttribute.ATTRIBUTE);
		return attr != null && attr.getLocationFrequency(length - NAME_LENGTH_MIN, 0) > 10;
	}

	/**
	 * 这个词是否可以出现在长度为length名字的第pos个位置上。位置从0开始算； 算法： 到person库中寻找这个词出现在指定位置上的频率。
	 * 
	 * @param term
	 * @param length
	 * @param pos
	 * @return
	 */
	private boolean canAppearInName(POSTerm term, int length, int pos) {
		PersonTermAttribute attr = (PersonTermAttribute) term.getTermNatures().getAttribute(PersonTermAttribute.ATTRIBUTE);
		return attr != null && attr.getLocationFrequency(length - NAME_LENGTH_MIN, pos) > 0;

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
	private void findNames(TermGraph graph, TermPath partName, int length, List<POSTerm> names) {
		int pos = partName.getVertexCount();
		// 超长，不理；
		if (pos > length)
			return;
		POSTerm current = partName.getEndVertex();
		for (TermEdge edge : graph.outgoingEdgesOf(current)) {
			// 对于长度为N的名字，往前走一步，到N+1，检查下路径是否可以组成名字；
			// 判断新增加的这条边目标节点是否可以作为名字的第N个字符；
			POSTerm next = graph.getEdgeTarget(edge);
			if (this.canAppearInName(next, length, pos)) {
				partName.extendTo(next);
				if (pos == length - 1) {
					// 达到指定长度，检查是否可以组成人名了；
					POSTerm candidate = this.checkNamePath(graph, partName);
					if (candidate != null)
						names.add(candidate);

				} else
					// 继续发现人名；
					this.findNames(graph, partName, length, names);
				partName.removeEnd();

			}
		}

	}

	/**
	 * 检查存放在这个list中的name是否可以构成名字；如果可以构成名字，则建立名字节点，并对边打分。
	 * 给新增名字的连接边打分的算法：（基于Viterbi算法） 类似 总裁 - 李想 - 的，其中李想是新识别的人名。 1. 人名（李想）概率
	 * 是：名字中各个字符 的 (ln(名字中各字符在训练文本中的出现总频率）- ln(名字出现在指定位置上的频率）)的总和。 2. 前缀，即 总裁-李想
	 * 这条边权重算法是：人名概率/2 - ln(总裁作为前缀出现的频率） 3. 后缀，即 李想-的 这条边权重算法是：人名概率/2 -
	 * ln(‘的’作为后缀出现的频率）
	 * 
	 * @param path
	 */
	private POSTerm checkNamePath(TermGraph graph, TermPath partName) {
		double score = 0;
		int maybeBoundary = 0;
		int nameLength = partName.getVertexCount();
		for (int i = 0; i < nameLength; i++) {
			POSTerm term = partName.getVertex(i);
			PersonTermAttribute pna = (PersonTermAttribute) term.getTermNatures().getAttribute(PersonTermAttribute.ATTRIBUTE);
			// 在这个长度的这个位置的词频,如果没有可能就跳出循环
			int freq = pna.getLocationFrequency(partName.getVertexCount() - NAME_LENGTH_MIN, i);
			score += Math.log(term.getTermNatures().getSumFrequency() + 1);
			score -= Math.log((freq));
			if (pna.getBoundaryFrequency() > 0) {
				// 是否有可能作为名字的前/后缀出现，如部长，司令等。
				maybeBoundary++;
			}
		}
		// score>0 该名字存在的可能性: sum(单词在指定位置出现的概率/单词出现的总概率)>1
		// maybeBoundary 不会作为边界词。
		if (score > 0 && maybeBoundary > 0)
			return null;

		// 平滑处理
		score -= Math.log(FACTORY[nameLength - NAME_LENGTH_MIN]);
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

	
	


}
