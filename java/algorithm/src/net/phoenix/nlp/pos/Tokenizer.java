package net.phoenix.nlp.pos;

import java.util.List;


/**
 * interface for Analysis
 * @author lixf
 *
 */
public interface Tokenizer {

	/**
	 * 
	 * @param temp
	 * @return
	 */
	public List<Term> tokenize(String temp);
}