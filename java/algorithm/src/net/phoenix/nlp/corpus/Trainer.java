/**
 * 
 */
package net.phoenix.nlp.corpus;


/**
 * 语料库训练程序
 * 
 * @author lixf
 * 
 */
public interface Trainer {
	
	/**
	 * 处理开始
	 */
	public void onFileBegin();
	/**
	 * 处理结束
	 */
	public void onFileEnd();
	
	/**
	 * 
	 * 句子开始
	 */
	public void onSentenceBegin();
	
	/**
	 * 处理每个term和属性；
	 * @param term
	 * @param nature
	 */
	public void onPOS(String term, String nature);
	
	/**
	 * 一个句子处理介绍
	 */
	public void onSentenceEnd();


}
