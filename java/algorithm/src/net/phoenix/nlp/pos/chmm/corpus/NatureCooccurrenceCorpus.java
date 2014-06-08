package net.phoenix.nlp.pos.chmm.corpus;

import net.phoenix.nlp.Nature;

/**
 * 两个词性之间的共现关系频率
 * @author lixf
 *
 */
public interface NatureCooccurrenceCorpus {

	/**
	 * 获取两个词性之间的共现关系频率。
	 * @param from
	 * @param to
	 * @return
	 */
	public int getOccurrenctFrequency(Nature from, Nature to);

}