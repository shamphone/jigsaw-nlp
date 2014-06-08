package net.phoenix.nlp.pos.chmm.corpus;

/**
 * 按照长度计算各个公司名字出现的频率
 * @author lixf
 *
 */
public interface CompanyNameLengthCorpus {

	/**
	 * 长度1~50之间的机构名字出现的概率比例；
	 * @param length
	 * @return
	 */
	public double getFrequency(int length);

}