package net.phoenix.nlp;

import java.util.List;

import net.phoenix.nlp.pos.POSTerm;


/**
 * 分词器。将给定的片段切分成词的列表。
 * @author lixf
 *
 */
public interface Tokenizer {

	/**
	 * 分词操作
	 * @param temp
	 * @return
	 */
	public List<POSTerm> tokenize(String temp);
}