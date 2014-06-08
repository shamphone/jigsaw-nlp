/**
 * 
 */
package net.phoenix.nlp.pos.chmm;

import java.util.List;



/**
 * @author lixf
 *
 */
public interface Recognitor {

	/**
	 * 从图中识别出新词来
	 * @param pathes 上一个阶段已经识别好的路径。本阶段可以重新生成，也可以对这些路径进行排序，也可以进行优化。
	 * @param graph
	 * @return
	 */
	public List<TermPath> process(TermGraph graph) ;
}
