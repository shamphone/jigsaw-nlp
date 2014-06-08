/**
 * 
 */
package net.phoenix.nlp.pos.chmm;


/**
 * @author lixf
 *
 */
public interface PathScorer{
	/**
	 * 为路径打分。
	 * @param path
	 * @return
	 */
	public double score(TermPath path);
}
