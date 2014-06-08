package net.phoenix.nlp.pos.chmm.corpus;
/**
 * 字符串集合的设置，默认提供日期、时间、数字、常用名中使用的字符。
 * 默认从chars.data文件中加载数据。
 * @author lixf
 *
 */
public interface CharsetCorpus {

	/**
	 * 根据集合名称获取该集合中有的全部字符
	 * @param name
	 * @return
	 */
	public char[] getChars(String name);

}