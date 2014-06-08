/**
 * 
 */
package net.phoenix.nlp.pos.chmm.recognitor;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import net.phoenix.nlp.Nature;
import net.phoenix.nlp.Term;
import net.phoenix.nlp.corpus.CorpusRepository;
import net.phoenix.nlp.pos.chmm.POSTerm;
import net.phoenix.nlp.pos.chmm.TermEdge;
import net.phoenix.nlp.pos.chmm.TermGraph;
import net.phoenix.nlp.pos.chmm.TermNatures;
import net.phoenix.nlp.pos.chmm.corpus.CharsetCorpus;
import net.phoenix.nlp.pos.chmm.corpus.file.CharsetFileCorpus;

/**
 * @author lixf
 * 
 */
public class NumberRecognitor extends AbstractRecognitor {
	private char[] characters;

	public NumberRecognitor(CorpusRepository dictionary) throws IOException {
		super(dictionary);		
		CharsetCorpus chars = dictionary.getCorpus(CharsetFileCorpus.class);
		this.characters = chars.getChars("number");
		Arrays.sort(this.characters);
	}


	@Override
	public void recognize(TermGraph graph) {
		this.findNumbers(graph);
		this.findQuatifiers(graph);

	}

	/**
	 * 寻找所有的数字；
	 * 
	 * @param graph
	 * @return
	 */
	private List<POSTerm> findNumbers(TermGraph graph) {
		List<POSTerm> confirmed = new ArrayList<POSTerm>();
		List<POSTerm> parts = new ArrayList<POSTerm>();
		List<POSTerm> candidates = new ArrayList<POSTerm>(graph.vertexSet());
		while (candidates.size() > 0) {
			POSTerm term = candidates.remove(0);
			// 如果当前词是一个数字，而且这个数字还没有处理过（不在pathes中）。
			if (this.isNumber(term)) {
				// 清空缓存
				parts.clear();
				// 将当前词（数字）加入到路径中。
				parts.add(term);
				// 从当前位置开始，继续搜索数字。这里使用的是基于规则的方法。
				this.findNumbers(graph, parts, candidates, confirmed);
			}
		}
		return confirmed;
	}

	/**
	 * 扩充found的数字组合，继续寻找更多数字加入到found。如果下一个位置是数字结束符，则将发现的数字放到Numbers中。
	 * 
	 * @param graph
	 * @param parts
	 * @param confirmed
	 */
	private void findNumbers(TermGraph graph, List<POSTerm> parts, List<POSTerm> candidates, List<POSTerm> confirmed) {
		// The term next to current edge;
		POSTerm current = parts.get(parts.size() - 1);
		Collection<TermEdge> following = graph.outgoingEdgesOf(current);
		if (following.size() == 0) {
			// 如果后续没有节点了，这是最后一个节点；
			if (parts.size() > 1)
				this.createNumber(graph, parts, false, confirmed);
		} else
			// 检查所有的后续节点
			for (TermEdge edge : following) {
				POSTerm next = graph.getEdgeTarget(edge);
				candidates.remove(next);
				if (this.isNumber(next) || next.getName().equals(".")) {
					parts.add(next);
					this.findNumbers(graph, parts, candidates, confirmed);
					parts.remove(next);
					// }
					// else
					// if (this.isNumberFollowing(next.getTermNatures())) {
					// // 可以作为数字结尾的字符（量词），也加入到数字中；
					// found.add(next);
					// this.createNumberPath(graph, found, false, numbers);
					// found.remove(next);
				} else {
					// 非数字，结束；
					if (parts.size() > 1)
						this.createNumber(graph, parts, false, confirmed);
				}
			}
	}

	/**
	 * 将found的数字放到number中。
	 * 
	 * @param graph
	 * @param found
	 * @param numbers
	 */
	private void createNumber(TermGraph graph, List<POSTerm> found, boolean confirmed, List<POSTerm> numbers) {
		POSTerm term = this.createMergedTerm(graph, found, this.createTermNatures(Nature.Number), confirmed);
		numbers.add(term);
	}

	/**
	 * 是否数字
	 * 
	 * @param term
	 * @return
	 */
	private boolean isNumber(Term term) {
		String name = term.getName();
		for (int i = 0; i < name.length(); i++)
			if (Arrays.binarySearch(this.characters, name.charAt(i)) < 0)
				return false;
		return true;
	}

	/**
	 * 是否是数字的后缀，比如 xxx个， xxx斤等量词单位。
	 * 
	 * @param natures
	 * @return
	 */
	private boolean isNumberFollowing(TermNatures natures) {
		if (natures == null)
			return false;
		return natures.getFrequency(Nature.valueOf(29)) > 0;
	}

	/**
	 * 寻找数量词；
	 * 
	 * @param graph
	 * @param number
	 */
	private void findQuatifiers(TermGraph graph) {
		List<POSTerm> candidates = new ArrayList<POSTerm>(graph.vertexSet());
		for (POSTerm number : candidates) {
			if (this.isNumber(number)) {
				for (TermEdge outgoing : graph.outgoingEdgesOf(number)) {
					POSTerm candidate = graph.getEdgeTarget(outgoing);
					if (this.isNumberFollowing(candidate.getTermNatures())) {
						this.createQuatifiers(graph, number, candidate);
					}
				}
			}
		}
	}

	/**
	 * 将数量词添加到系统中。
	 * 
	 * @param graph
	 * @param number
	 * @param quatifier
	 * @return
	 */
	private Term createQuatifiers(TermGraph graph, POSTerm number, POSTerm quatifier) {
		List<POSTerm> terms = new ArrayList<POSTerm>();
		terms.add(number);
		terms.add(quatifier);
		return this.createMergedTerm(graph, terms, this.createTermNatures(Nature.Qualifier), false);

	}

}
