package net.phoenix.nlp.pos.chmm.corpus;

import net.phoenix.nlp.pos.chmm.CharNode;

/**
 *  字符成词的状态自动机。
 *  GB2312收录了6763个汉字
 *  GBK收录了21003个汉字
 *  GB18030-2000收录了27533个汉字
 *  GB18030-2005收录了70244个汉字
 *  Unicode 5.0收录了70217个汉字
 * @author <a href="shamphone@gmail.com">Li XiongFeng</a>
 * @date 2013-2-7
 * @version 1.0.0
 */
public interface CharDFACorpus {

	/**
	 * 添加一个新词到自动机中
	 * @param word
	 * @param natureName
	 * @return
	 */
	public CharNode addNewWord(String word, String natureName);

	/**
	 * 获取指定的词所在的状态
	 * @param ch
	 * @return
	 */
	public CharNode getNode(char ch);

	/**
	 * 获取指定的词所在的状态
	 * @param word
	 * @return
	 */
	public CharNode getNode(String word);

}