/**
 * 
 */
package net.phoenix.nlp.pos.chmm.recognitor;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import net.phoenix.nlp.Nature;
import net.phoenix.nlp.Term;
import net.phoenix.nlp.corpus.CorpusRepository;
import net.phoenix.nlp.pos.chmm.POSTerm;
import net.phoenix.nlp.pos.chmm.TermEdge;
import net.phoenix.nlp.pos.chmm.TermGraph;
import net.phoenix.nlp.pos.chmm.TermPath;
import net.phoenix.nlp.pos.chmm.corpus.CompanyNameLengthCorpus;
import net.phoenix.nlp.pos.chmm.corpus.CompanyTermAttribute;
import net.phoenix.nlp.pos.chmm.corpus.file.CompanyNameLengthFileCorpus;

/**
 * @author lixf
 * 
 */
public class CompanyRecognitor extends AbstractRecognitor {
	
	private CompanyNameLengthCorpus lengthFreq ;
	
	public CompanyRecognitor(CorpusRepository dictionary) throws IOException {
		super(dictionary);
		this.lengthFreq = dictionary.getCorpus(CompanyNameLengthFileCorpus.class);
	}
	
	@Override
	protected void recognize(TermGraph graph) {
		List<POSTerm> currentVertexes = new ArrayList<POSTerm>(graph.vertexSet());
		for (POSTerm first : currentVertexes) {
			CompanyTermAttribute nature = (CompanyTermAttribute) first.getTermNatures().getAttribute(CompanyTermAttribute.ATTRIBUTE);
			if (nature != null && nature.getIdfBegin() < -0.005 && nature.getBegin() > 1000) {
				TermPath candidate = graph.createPath(first); // 存放正在遍历的名字组合；
				this.findNames(graph, candidate);
			}

		}
	}

	/**
	 * 通过遍历来发现所有可能的机构名称组合
	 * 
	 * @param graph
	 *            句图
	 * @param partName
	 *            当前已经发现的名字片段或者名字
	 * @param names
	 *            用来存放发现的结果；
	 */
	private void findNames(TermGraph graph, TermPath partName) {
		POSTerm current = partName.getEndVertex();
		for (TermEdge edge : graph.outgoingEdgesOf(current)) {
			POSTerm next = graph.getEdgeTarget(edge);
			CompanyTermAttribute nature = (CompanyTermAttribute) next.getTermNatures().getAttribute(CompanyTermAttribute.ATTRIBUTE);
			if (nature != null) {
				if (nature.getIdfEnd() < -0.005 && nature.getEnd() > 200) {
					partName.extendTo(next);
					this.createTerm(graph, partName);
				} else if (nature.getIdfMiddle() < -0.005 && nature.getMiddle() > 50) {
					partName.extendTo(next);
					findNames(graph, partName);
					partName.removeEnd();
				}
			}
		}
	}

	/**
	 * 创建一个Company的Term.
	 * 
	 * @param graph
	 * @param partName
	 */
	private void createTerm(TermGraph graph, TermPath partName) {
		if (partName.getVertexCount() == 0)
			return;
		if (partName.getName().trim().length() < 2)
			return;
		double score = 0;
		List<POSTerm> terms = partName.getVertextList();
		CompanyTermAttribute nature = (CompanyTermAttribute) terms.get(0).getTermNatures().getAttribute(CompanyTermAttribute.ATTRIBUTE);
		score += nature.getIdfBegin();
		for (int i = 1; i < terms.size() - 1; i++) {
			nature = (CompanyTermAttribute) terms.get(i).getTermNatures().getAttribute(CompanyTermAttribute.ATTRIBUTE);
			score += nature.getIdfMiddle();
		}
		nature = (CompanyTermAttribute) terms.get(terms.size() - 1).getTermNatures().getAttribute(CompanyTermAttribute.ATTRIBUTE);
		score += nature.getIdfEnd();

		String name = partName.getName();
		int length = name.length() > 50 ? 50 : name.length();
		score *= -Math.log(1 - lengthFreq.getFrequency(length));

		score /= 2.0;

		POSTerm current = partName.toTerm(this.createTermNatures(Nature.OrginizationName));

		// 将原来指向name第一个词的起始词，都建立指向新的name节点的连接。
		for (TermEdge edge : graph.incomingEdgesOf(partName.getStartVertex())) {
			POSTerm leading = graph.getEdgeSource(edge);
			TermEdge newEdge = graph.addEdge(leading, current);
			// 使用Viterbi算法计算前驱权重
			newEdge.setWeight(score + this.calculateLeadingScore(graph, current, leading));
		}

		// 将原来name最后一个词的所有外向连接，都建立指向新的name节点的外向连接。
		for (TermEdge edge : graph.outgoingEdgesOf(partName.getEndVertex())) {
			POSTerm following = graph.getEdgeTarget(edge);
			// 如果名字最后一个词和后面的词结合紧密，则不建立连接。否则建立连接。
			// if(following.equals(graph.getEndVertex()) ||
			// this.cooccurrence.getCooccurrenceFrequency(partName.getEndVertex(),
			// following) < 3){
			TermEdge newEdge = graph.addEdge(current, following);
			// 使用Viterbi算法计算后缀权重
			newEdge.setWeight(score + this.calculateFollowingScore(graph, current, following));

			// }
		}

		// return name;
	}

	/**
	 * 计算后缀词的分数
	 * 
	 * @param term
	 * @return
	 */
	private double calculateFollowingScore(TermGraph graph, Term term, POSTerm to) {
		// 后缀分数
		double score = 0;
		if (to.getTermNatures().isNature(Nature.End)) {
			CompanyTermAttribute nature = (CompanyTermAttribute) to.getTermNatures().getAttribute(CompanyTermAttribute.ATTRIBUTE);
			if (nature != null)
				score = nature.getIdfFollowing();
		}
		return score;
	}

	/**
	 * 计算前导词的分数
	 * 
	 * @param term
	 * @return
	 */
	private double calculateLeadingScore(TermGraph graph, Term term, POSTerm from) {
		double score = 0;
		if (from.getTermNatures().isNature(Nature.Begin)) {
			CompanyTermAttribute nature = (CompanyTermAttribute) from.getTermNatures().getAttribute(CompanyTermAttribute.ATTRIBUTE);
			if (nature != null)
				score = nature.getIdfLeading();

		}
		return score;
	}

	
	
	
}
