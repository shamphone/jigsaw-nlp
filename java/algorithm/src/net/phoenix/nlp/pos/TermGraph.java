/**
 * 
 */
package net.phoenix.nlp.pos;

import java.util.List;

import net.phoenix.nlp.pos.TermNatures;

import org.jgrapht.DirectedGraph;
import org.jgrapht.GraphPath;
import org.jgrapht.WeightedGraph;

/**
 * @author lixf
 *
 */
public interface TermGraph extends WeightedGraph<Term, TermEdge>, DirectedGraph<Term, TermEdge> {
	
	/**
	 * 获得起始节点；
	 * @return
	 */
	public Term getStartVertex();
	
	/**
	 * 获得结束节点
	 * @return
	 */
	public Term getEndVertex();
	
	
	/**
	 * the source string represent by this graph;
	 * @return
	 */
	public String getSource();
	
	/**
	 * 删除start和end节点之间的所有边
	 * @param start
	 * @param end
	 */
	public void cleanEdgesBetween(int start, int end);
	
	
	/**
	 * 添加一条边
	 * @param from
	 * @param to
	 * @param name
	 * @param natures
	 * @return
	 */
	public Term addTerm(int from, int to, String name, TermNatures natures);
	
	/**
	 * 添加一条边
	 * @param from
	 * @param ch
	 * @param natures
	 * @return
	 */
	public Term addTerm(int from, char ch, TermNatures natures);
	
	
	/**
	 * 使用现有的定点创建一条路径
	 * @param edges
	 * @return
	 */
	public TermPath createPath(Term start);
	/**
	 * 创建当前path的一个副本
	 * @param edges
	 * @return
	 */
	public TermPath createPath(GraphPath<Term, TermEdge> path);	
	
	/**
	 * 创建当前path的一个副本
	 * @param edges
	 * @return
	 */
	public TermPath createPath(List<TermEdge> edges);	

}
