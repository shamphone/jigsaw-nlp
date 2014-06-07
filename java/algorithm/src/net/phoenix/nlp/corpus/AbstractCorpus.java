/**
 * 
 */
package net.phoenix.nlp.corpus;

/**
 * 提供语料库处理时的一些基础操作
 * @author lixf
 *
 */
public abstract class AbstractCorpus implements Corpus{
	
	/**
	 * 判断字符串是否为空
	 * 
	 * @param cs
	 * @return
	 */
	public static boolean isBlank(CharSequence cs) {
		int strLen;
		if (cs == null || (strLen = cs.length()) == 0) {
			return true;
		}
		for (int i = 0; i < strLen; i++) {
			if (Character.isWhitespace(cs.charAt(i)) == false) {
				return false;
			}
		}
		return true;
	}
}
