package net.phoenix.nlp.pos;

import java.util.Set;

import net.phoenix.nlp.Nature;


/**
 * 
 * 每一个词都拥有一个的可能词性集合，以及该词的属性集合。
 * 
 * @author ansj
 * 
 */
public interface TermNatures {

	/**
	 * 遍历所有的Nature;
	 * @return
	 */
	public Set<Nature> natures();

	/**
	 * @return the sumFrequency
	 */
	public int getSumFrequency();
	
	/**
	 * If this term is of given nature; 
	 * @param nature
	 * @return
	 */
	public boolean isNature(String nature);
	/**
	 * 获取某个指定属性；
	 * @param name
	 * @return
	 */
	public TermAttribute getAttribute(String name);
	
	/**
	 * 设置属性
	 * @param name
	 * @param attribute
	 */
	public void setAttribute(String name, TermAttribute attribute);
	
	/**
	 * 判断是否具有某个属性
	 * @param name
	 * @return
	 */
	public boolean hasAttribute(String name);

	/**
	 * 获取指定Nature的Frequency;
	 * @param nature
	 * @return
	 */
	int getFrequency(Nature nature);
	
	/**
	 * 添加一个属性，默认frequency为1；
	 * @param nature
	 */
	public void addNature(Nature nature);
	
	/**
	 * 添加一个属性，设置其frequency；
	 * @param nature
	 */
	public void addNature(Nature nature, int freq);
	
}
