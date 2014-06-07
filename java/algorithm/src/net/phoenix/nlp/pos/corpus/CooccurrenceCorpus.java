package net.phoenix.nlp.pos.corpus;

/**
 * 查找两个词与词之间的频率
 * @author lixf
 *
 */
public interface CooccurrenceCorpus {

	/**
	 * 查找两个词与词之间的频率
	 * 
	 * @param from
	 * @param to
	 * @return
	 */
	public int getCooccurrenceFrequency(String from, String to);

}