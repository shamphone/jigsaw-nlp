/**
 * 
 */
package net.phoenix.nlp.pos.chmm;


/**
 * 初分词。 根据输入的sentence来生成graph， 产生N条最短路径。 后续使用这些路径来做未登录词的识别
 * 
 * @author lixf
 * 
 */
public interface Segmenter {

	/**
	 * 
	 * @param graph
	 *            初始的TermGraph
	 * @param N最短路径
	 * @return N条最短路径。
	 */
	public TermGraph segment(String source);
}
