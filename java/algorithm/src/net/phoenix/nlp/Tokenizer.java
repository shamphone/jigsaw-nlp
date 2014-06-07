package net.phoenix.nlp;

import java.util.List;


/**
 * 分词器。将给定的片段切分成词的列表。
 * @author lixf
 *
 */
public interface Tokenizer {

	/**
	 * 分词操作,输入为短语、句子或者段落
	 * @param source 待分词的短语或者段落；
	 * @return POS列表
	 */
	public List<Term> tokenize(String source);
}