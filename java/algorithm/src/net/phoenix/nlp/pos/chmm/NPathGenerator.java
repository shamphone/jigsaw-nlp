/**
 * 
 */
package net.phoenix.nlp.pos.chmm;

import java.util.List;


/**
 * @author lixf
 * 
 */
public interface NPathGenerator {

	/**
	 * 从graph中获取最佳的N条路径；
	 * @param graph
	 * @return
	 */
	public List<TermPath> process(TermGraph graph) ;
}
