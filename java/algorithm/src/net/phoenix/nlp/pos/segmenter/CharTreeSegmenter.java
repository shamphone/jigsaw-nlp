/**
 * 
 */
package net.phoenix.nlp.pos.segmenter;

import net.phoenix.nlp.Nature;
import net.phoenix.nlp.corpus.CorpusRepository;
import net.phoenix.nlp.pos.AbstractProcessor;
import net.phoenix.nlp.pos.CharNode;
import net.phoenix.nlp.pos.CharNode.State;
import net.phoenix.nlp.pos.POSTerm;
import net.phoenix.nlp.pos.Segmenter;
import net.phoenix.nlp.pos.TermEdge;
import net.phoenix.nlp.pos.TermGraph;
import net.phoenix.nlp.pos.corpus.CharDFACorpus;
import net.phoenix.nlp.pos.corpus.T2SCorpus;
import net.phoenix.nlp.pos.corpus.file.CharDFAFileCorpus;
import net.phoenix.nlp.pos.corpus.file.T2SFileCorpus;
import net.phoenix.nlp.pos.term.DefaultTermGraph;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * 基于CharTree的分词；
 * @author lixf
 *
 */
public class CharTreeSegmenter extends AbstractProcessor implements Segmenter {

	/**
	 * 这个就是(int)'ａ'
	 */
	private static final int MIN_LOWER = 65345;
	/**
	 * 这个就是(int)'ｚ'
	 */
	private static final int MAX_LOWER = 65370;
	/**
	 * 差距进行转译需要的
	 */
	private static final int LOWER_GAP = 65248;
	/**
	 * 这个就是(int)'Ａ'
	 */
	private static final int MIN_UPPER = 65313;
	/**
	 * 这个就是(int)'Ｚ'
	 */
	private static final int MAX_UPPER = 65338;
	/**
	 * 差距进行转译需要的
	 */
	private static final int UPPER_GAP = 65216;
	/**
	 * 这个就是(int)'A'
	 */
	private static final int MIN_UPPER_E = 65;
	/**
	 * 这个就是(int)'Z'
	 */
	private static final int MAX_UPPER_E = 90;
	/**
	 * 差距进行转译需要的
	 */
	private static final int UPPER_GAP_E = -32;
	/**
	 * 这个就是(int)'0'
	 */
	private static final int MIN_UPPER_N = 65296;
	/**
	 * 这个就是(int)'９'
	 */
	private static final int MAX_UPPER_N = 65305;
	/**
	 * 差距进行转译需要的
	 */
	private static final int UPPER_GAP_N = 65248;

	private static final String E = "末##末";
	private static final String B = "始##始";

	private CharDFACorpus natures;
	private T2SCorpus  t2s;
	private static final Log log = LogFactory.getLog(CharTreeSegmenter.class);
	

	public CharTreeSegmenter(CorpusRepository dictionary) {
		super(dictionary);
		natures = dictionary.getCorpus(CharDFAFileCorpus.class);
		t2s = dictionary.getCorpus(T2SFileCorpus.class);
	}
	

	@Override
	public TermGraph segment(String source) {
		TermGraph graph = new DefaultTermGraph(source);
		this.buildVertexes(graph);
		this.buildEdges(graph);
		return graph;
	}

	protected void buildVertexes(TermGraph graph) {
		String sentence = graph.getSource();
		// 加起始边；
		POSTerm first = graph.addTerm(-1, 0, B, this.createTermNatures(Nature.Begin, 50610));
		first.setNature(Nature.Begin);
		int start = 0;
		int pos = 0;
		int sentenceLength = sentence.length();
		String part;
		if (sentenceLength > 0) {
			CharNode previous = this.natures.getNode(t2s.toSimple(sentence.charAt(pos)));
			pos = 0;
			while (pos < sentenceLength) {
				if (previous == null) {
					//未收录的字符或者标点
					log.info("unknown:'"+ sentence.charAt(pos)+"'");
					graph.addTerm(pos, sentence.charAt(pos), createTermNatures("w"));
					pos++;
					if (pos < sentenceLength)
						previous = this.natures.getNode(t2s.toSimple(sentence.charAt(pos)));
					else
						previous = null;
				} else
					switch (previous.getState()) {
					case NOT_WORD:
						//非收录字，每个字单独成词，分词之后， 将指针打向下一个位置即可；
						graph.addTerm(pos, previous.getChar(), createTermNatures(Nature.NULL));
						pos++;
						if (pos < sentenceLength)
							previous = this.natures.getNode(t2s.toSimple(sentence.charAt(pos)));
						else
							previous = null;
						break;
					case ENGLISH:
						//英文单词，向前寻找更多单词，同时将指针打向最后一个英语字符之后的位置；
						start = pos;
						while (previous != null && previous.getState() == State.ENGLISH) {
							pos++;
							if (pos < sentenceLength)
								previous = this.natures.getNode(t2s.toSimple(sentence.charAt(pos)));
							else
								previous = null;
						}
						part = normalizeEnglishCharacters(sentence, start, pos - start);
						graph.addTerm(start, pos, part, createTermNatures(Nature.English));
						break;
					case NUMBER:
						//数字字符，向前寻找更多单词，同时将指针打向最后一个数字字符之后的位置；
						start = pos;
						while (previous != null && previous.getState() == State.NUMBER) {
							pos++;
							if (pos < sentenceLength)
								previous = this.natures.getNode(t2s.toSimple(sentence.charAt(pos)));
							else
								previous = null;
						}
						part = this.normalizeNumberCharacters(sentence, start, pos - start);
						graph.addTerm(start, pos, part, createTermNatures(Nature.Number));
						break;
					default://如果是中文字符， 则开始分词。每个中文字符都可以作为单词的开始，寻找尽可能多的分词；
						start = pos;
						int end = pos;
						//从当前位置开始寻找所有可能的成词；
						CharNode node = previous;
						while (node != null) {
							switch (node.getState()) {
							//不成词；
							case PART_OF_WORD:
								end++;
								//如果碰到边界，或者只有一个字的， 不成词也得成词；
								if (end == sentenceLength || end == start + 1)
									graph.addTerm(start, end, sentence.substring(start, end), node.getTermNatures());
								if (end < sentenceLength)
									node = node.get(t2s.toSimple(sentence.charAt(end)));
								else
									node = null;
								break;
							case WORD_AND_PART:
								end++;
								graph.addTerm(start, end, sentence.substring(start, end), node.getTermNatures());
								if (end < sentenceLength)
									node = node.get(t2s.toSimple(sentence.charAt(end)));
								else
									node = null;
								break;
							case END_OF_WORD:
								end++;
								//成词了，不再继续；
								graph.addTerm(start, end, sentence.substring(start, end), node.getTermNatures());
								node = null;
								break;
							default:
								break;
							}
						}
						//从下一个位置开始继续寻找成词；
						pos++;
						if (pos < sentenceLength)
							previous = this.natures.getNode(t2s.toSimple(sentence.charAt(pos)));
						else
							previous = null;

					}
			}
		}

		// 加入终止边
		POSTerm last = graph.addTerm(sentence.length(), sentence.length() + 1, E, this.createTermNatures(Nature.End, 50610));
		last.setNature(Nature.valueOf(Nature.End));
	}

	/**
	 * 为所有的边打分。
	 * 
	 * @param gp
	 */
	private void buildEdges(TermGraph gp) {
		for (POSTerm start : gp.vertexSet()) {
			for (POSTerm end : gp.vertexSet()) {
				if (start.getEndOffset() == end.getStartOffset()) {
					TermEdge edge = gp.addEdge(start, end);
					gp.setEdgeWeight(edge, Double.NaN);
				}
			}
		}
	}

	/**
	 * 英文大小写、全角半角转换
	 * 
	 * @param temp
	 * @param start
	 * @param length
	 * @return
	 */
	private String normalizeEnglishCharacters(String temp, int start, int length) {
		char c = 0;
		StringBuilder sb = new StringBuilder();
		for (int i = start; i < start + length; i++) {
			c = temp.charAt(i);
			if (c >= MIN_LOWER && c <= MAX_LOWER) {
				sb.append((char) (c - LOWER_GAP));
			} else if (c >= MIN_UPPER && c <= MAX_UPPER) {
				sb.append((char) (c - UPPER_GAP));
			} else if (c >= MIN_UPPER_E && c <= MAX_UPPER_E) {
				sb.append((char) (c - UPPER_GAP_E));
			} else {
				sb.append(c);
			}
		}
		return sb.toString();
	}

	/**
	 * 数字全角半角转换
	 * 
	 * @param temp
	 * @param start
	 * @param end
	 * @return
	 */
	private String normalizeNumberCharacters(String temp, int start, int end) {
		char c = 0;
		StringBuilder sb = new StringBuilder();
		for (int i = start; i < start + end; i++) {
			c = temp.charAt(i);
			if (c >= MIN_UPPER_N && c <= MAX_UPPER_N) {
				sb.append((char) (c - UPPER_GAP_N));
			} else {
				sb.append(c);
			}
		}
		return sb.toString();
	}

}
