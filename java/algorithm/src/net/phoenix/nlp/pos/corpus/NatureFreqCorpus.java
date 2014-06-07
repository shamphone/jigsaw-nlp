package net.phoenix.nlp.pos.corpus;

import net.phoenix.nlp.Nature;

/**
 * 
 * @author lixf
 *
 */
public interface NatureFreqCorpus {

	/**
	 * 获取指定词性出现的频率
	 * @param nature
	 * @return
	 */
	public abstract int getFrequency(Nature nature);

	/**
	 * 获取总大小
	 * @return
	 */
	public abstract int getSize();

}