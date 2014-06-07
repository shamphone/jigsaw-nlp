package net.phoenix.nlp;

/**
 * 词，代表从一个片段中切分出来的词。
 * @author lixf
 *
 */

public interface Term {

	/**
	 * 获取Term的name。即source.subString(getStartOffset(), getEndOffset);
	 * @return
	 */
	public String getName();

	/**
	 * 在原文中的初始偏移
	 * @return
	 */
	public int getStartOffset();

	/**
	 * 在原文中的结束偏移（不包含在这个Term中）。
	 * @return
	 */
	public int getEndOffset();

	/**
	 * 获得这个词的词性.词性计算后才可生效
	 * 
	 * @return
	 */
	public Nature getNature();

}