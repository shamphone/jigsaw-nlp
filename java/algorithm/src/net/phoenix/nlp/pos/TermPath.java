/**
 * 
 */
package net.phoenix.nlp.pos;

import java.util.List;

import net.phoenix.nlp.Term;

import org.jgrapht.GraphPath;

/**
 * @author lixf
 * 
 */
public interface TermPath extends GraphPath<POSTerm, TermEdge> {
	/**
	 * extend the path with edge term;
	 * 
	 * @param term
	 */
	public void extend(TermEdge term);
	/**
	 * extend the path with edge term;
	 * 
	 * @param term
	 */
	public void extendTo(POSTerm term);
	
	/**
	 * 删除最后一个节点,将倒数第二个节点变成最后一个节点；
	 */
	public TermEdge removeEnd();
	
	/**
	 * 指定的term是否包含在这个路径中；
	 * @param term
	 * @return
	 */
	public boolean contains(Term term);

	/**
	 * 获取path的名字，即组成它的各个Term的名字的组合
	 * 
	 * @return
	 */
	public String getName();

	/**
	 * 设置是否确认
	 * @param confirmed
	 */
	public void setConfirmed(boolean confirmed);

	public boolean isConfirmed();

	public void setNature(String nature);

	public String getNature();

	/**
	 * 设置权重
	 * @param weight
	 */
	public void setWeight(double weight);
	
	
	/**
	 * 获取顶点个数
	 * @return
	 */
	public int getVertexCount();
	
	/**
	 * 获取第index个定点
	 * @param index
	 * @return
	 */
	public POSTerm  getVertex(int index);
	
	/**
	 * 获取所有节点
	 * @param index
	 * @return
	 */
	public List<POSTerm>  getVertextList();
	
	/**
	 * 转换成一个Term
	 * @return
	 */
	public POSTerm toTerm(TermNatures natures);
	/**
	 * 转换成一个TermGraph
	 * @return
	 */
	public TermGraph toTermGraph();
	
	/**
	 * 获取所在的图。
	 * @return
	 */
	public TermGraph getTermGraph();

}
