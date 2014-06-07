/**
 * 
 */
package net.phoenix.nlp.pos.term;

import java.util.Collection;
import java.util.List;

import net.phoenix.nlp.Term;
import net.phoenix.nlp.pos.POSTerm;
import net.phoenix.nlp.pos.TermEdge;
import net.phoenix.nlp.pos.TermGraph;
import net.phoenix.nlp.pos.TermNatures;
import net.phoenix.nlp.pos.TermPath;

import org.jgrapht.Graph;
import org.jgrapht.GraphPath;
import org.jgrapht.WeightedGraph;
import org.jgrapht.graph.SimpleDirectedWeightedGraph;

/**
 * Term图。 节点为可能的词，边为词与词之间变化的概率。
 * 
 * @author lixf
 * 
 */
public class DefaultTermGraph extends SimpleDirectedWeightedGraph<POSTerm, TermEdge> implements TermGraph {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2647818172800427794L;
	private String source;
	/**
	 * 起始节点
	 */
	private POSTerm start;
	/**
	 * 终止节点；
	 */
	private POSTerm end;

	public DefaultTermGraph(String source) {
		super(new TermEdgeFactory());
		this.source = source;
		this.start = null;
		this.end = null;
	}

	/**
	 * @see Graph#getEdgeWeight(Object)
	 */
	@Override
	public double getEdgeWeight(TermEdge e) {
		return e.getWeight();
	}

	/**
	 * @see WeightedGraph#setEdgeWeight(Object, double)
	 */
	@Override
	public void setEdgeWeight(TermEdge e, double weight) {
		e.setWeight(weight);
	}

	@Override
	public String getSource() {
		return source;
	}

	/**
	 * 清除在开始和结束节点之间的边 (不能同时包括start和end);
	 * 
	 * @param term
	 */
	@Override
	public void cleanEdgesBetween(int start, int end) {
		// ArrayList<Term> removal = new ArrayList<Term>();
		// for (Term term : edgeSet()) {
		// if (getEdgeSource(term) >= start && getEdgeTarget(term) <= end)
		// removal.add(term);
		// }
		// removeAllEdges(removal);
	}

	// public String toString2() {
	// Collection<Term> edgeSet = this.edgeSet();
	// List<String> renderedEdges = new ArrayList<String>();
	//
	// StringBuffer sb = new StringBuffer();
	// for (Term e : edgeSet) {
	// if ((e.getClass() != DefaultEdge.class) && (e.getClass() !=
	// DefaultWeightedEdge.class)) {
	// sb.append(e);
	// }
	// renderedEdges.add(sb.toString());
	// sb.setLength(0);
	// }
	//
	// return renderedEdges.toString();
	// }
	@Override
	public String toString() {
		Collection<POSTerm> vertexSet = this.vertexSet();
		Collection<TermEdge> edgeSet = this.edgeSet();
		StringBuffer sb = new StringBuffer();
		sb.append(vertexSet);
		for (TermEdge e : edgeSet) {
			sb.append("(");
			sb.append(getEdgeSource(e));
			sb.append(",");
			sb.append(getEdgeTarget(e));
			sb.append(":");
			sb.append(this.getEdgeWeight(e));
			sb.append("), ");
		}

		return sb.toString();
	}

	@Override
	public boolean addVertex(POSTerm term) {
		if (this.start == null || term.getStartOffset() < this.start.getStartOffset())
			this.start = term;
		if (this.end == null || term.getEndOffset() > this.end.getEndOffset())
			this.end = term;
		
		return super.addVertex(term);
	}

	@Override
	public POSTerm addTerm(int from, int to, String name, TermNatures natures) {
		DefaultTerm term = new DefaultTerm(name, from, to, natures);
		this.addVertex(term);
		if (this.start == null || from < this.start.getStartOffset())
			this.start = term;
		if (this.end == null || to > this.end.getEndOffset())
			this.end = term;
		return term;
	}

	@Override
	public POSTerm getStartVertex() {
		return this.start;
	}

	@Override
	public POSTerm getEndVertex() {
		return this.end;
	}

	@Override
	public Term addTerm(int from, char ch, TermNatures natures) {
		return this.addTerm(from, from + 1, String.valueOf(ch), natures);
	}

	@Override
	public TermPath createPath(POSTerm start) {
		return new DefaultTermPath(this, start);
	}

	@Override
	public TermPath createPath(GraphPath<POSTerm, TermEdge> path) {
		TermPath termPath = this.createPath(path.getStartVertex());
		for (TermEdge edge : path.getEdgeList())
			termPath.extend(edge);
		return termPath;

	}

	@Override
	public TermPath createPath(List<TermEdge> edges) {
		TermPath termPath = this.createPath(this.getEdgeSource(edges.get(0)));
		for (TermEdge edge : edges)
			termPath.extend(edge);
		return termPath;
	}

}
