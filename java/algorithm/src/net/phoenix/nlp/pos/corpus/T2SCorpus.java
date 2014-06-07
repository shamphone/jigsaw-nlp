package net.phoenix.nlp.pos.corpus;

/**
 * 简繁体转换字典
 * @author lixf
 *
 */
public interface T2SCorpus {

	/**
	 * 简繁体转换,
	 * 
	 * @param c
	 *            输入'孫'
	 * @return 输出'孙'
	 */
	public char toSimple(char c);

}