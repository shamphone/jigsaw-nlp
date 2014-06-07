/**
 * 
 */
package net.phoenix.nlp.keyword;

import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import net.phoenix.nlp.Term;
import net.phoenix.nlp.Tokenizer;
import net.phoenix.nlp.sentence.Detector;
import net.phoenix.nlp.sentence.Sentence;
 
/**
 * 基于TF/IDF的关键字抽取算法
 * 
 * @author lixf
 * 
 */
public class TFIDFKeywordExtractor implements KeywordExtractor {
	private Tokenizer tokenizer;
	private Detector detector;

	public TFIDFKeywordExtractor(Tokenizer tokenizer, Detector detector) {
		this.tokenizer = tokenizer;
		this.detector = detector;
	}

	@Override
	public List<String> extract(Reader reader, int count) throws IOException {
		Map<String, Integer> keywords = this.calcFrequency(reader);
		return this.extractTopN(keywords, count);
	}

	/**
	 * 判断一个term是否可以作为关键字。这里仅考虑名词作为关键字
	 * @param term
	 * @return
	 */
	private boolean canBeKeyword(Term term) {
		return term.getNature().typeOf("n") && term.getName().length()>1;
	}

	/**
	 * 计算每个关键字的出现频率。
	 * 
	 * @param reader
	 * @return
	 * @throws IOException
	 */
	private Map<String, Integer> calcFrequency(Reader reader)
			throws IOException {
		Map<String, Integer> keywords = new HashMap<String, Integer>();
		Iterator<Sentence> sentences = this.detector.detect(reader);
		while (sentences.hasNext()) {
			Sentence sentence = sentences.next();
			for (Term term : this.tokenizer.tokenize(sentence.toString())) {
				String keyword = term.getName();
				if (this.canBeKeyword(term)) {
					if (keywords.containsKey(keyword)) {
						int freq = keywords.get(keyword);
						keywords.put(keyword, freq + 1);
					} else {
						keywords.put(keyword, 1);
					}
				}
			}
		}
		return keywords;
	}

	/**
	 * 获取从map中获取Top N 个关键字
	 * @param keywords
	 * @param count
	 * @return
	 */
	private List<String> extractTopN(Map<String, Integer> keywords, int count) {
		if (count > keywords.size())
			count = keywords.size();
		List<Map.Entry<String, Integer>> list = new ArrayList<Map.Entry<String, Integer>>(
				keywords.entrySet());
		Collections.sort(list, new Comparator<Map.Entry<String, Integer>>() {

			@Override
			public int compare(Entry<String, Integer> o1,
					Entry<String, Integer> o2) {
				return o2.getValue() - o1.getValue();
			}
		});
		List<String> result = new ArrayList<String>();
		for (Map.Entry<String, Integer> entry : list.subList(0, count)) {
			result.add(entry.getKey());
		}
		return result;
	}

}
