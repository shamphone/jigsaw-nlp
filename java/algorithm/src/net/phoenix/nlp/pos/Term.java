package net.phoenix.nlp.pos;

/**
 * 
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
	 * 获得这个term的所有词性
	 * 
	 * @return
	 */
	public TermNatures getTermNatures();

	/**
	 * 
	 * @param nature
	 */
	public void setNature(Nature nature);
	/**
	 * 
	 * 等同于setNature(Nature.valueOf(nature));
	 * @param nature
	 */
	public void setNature(String nature);

	/**
	 * 获得这个词的词性.词性计算后才可生效
	 * 
	 * @return
	 */
	public Nature getNature();



}