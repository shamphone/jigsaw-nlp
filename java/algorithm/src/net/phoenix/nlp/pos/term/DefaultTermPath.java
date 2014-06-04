/**
 * 
 */
package net.phoenix.nlp.pos.term;

import java.util.AbstractList;
import java.util.ArrayList;
import java.util.List;

import net.phoenix.nlp.pos.Term;
import net.phoenix.nlp.pos.TermEdge;
import net.phoenix.nlp.pos.TermGraph;
import net.phoenix.nlp.pos.TermNatures;
import net.phoenix.nlp.pos.TermPath;

import org.jgrapht.Graph;

/**
 * @author lixf
 * 
 */
public class DefaultTermPath implements TermPath {
	private List<TermEdge> edges;
	private TermGraph graph;
	private double weight;
	private boolean confirmed;
	private String nature;
	private Term start;
	private Term end;

	protected DefaultTermPath(TermGraph graph, Term start) {
		this.graph = graph;
		this.edges = new ArrayList<TermEdge>();
		this.confirmed = false;
		this.start = start;
		this.end = start;
	}

	protected DefaultTermPath(TermGraph graph, TermPath path) {
		DefaultTermPath source = (DefaultTermPath) path;
		this.graph = graph;
		this.edges = new ArrayList<TermEdge>(source.edges);
		this.confirmed = source.confirmed;
		this.start = source.start;
		this.end = source.end;
	}

	@Override
	public Graph<Term, TermEdge> getGraph() {
		return this.graph;
	}

	@Override
	public Term getStartVertex() {
		return this.start;
	}

	@Override
	public Term getEndVertex() {
		return this.end;
	}

	@Override
	public List<TermEdge> getEdgeList() {
		return this.edges;
	}

	@Override
	public double getWeight() {
		return this.weight;
	}

	@Override
	public void setWeight(double weight) {
		this.weight = weight;
	}

	@Override
	public void extend(TermEdge edge) {
		if (!graph.getEdgeSource(edge).equals(end))
			throw new IllegalArgumentException("Could not extend path because new edge {" + graph.getEdgeSource(edge) + "-" + graph.getEdgeTarget(edge) + "} is not connected with the last vertext ["
					+ end + "]  of the path.");
		this.edges.add(edge);
		this.end = graph.getEdgeTarget(edge);

	}

	@Override
	public boolean isConfirmed() {
		return confirmed;
	}

	@Override
	public void setConfirmed(boolean confirmed) {
		this.confirmed = confirmed;
	}

	@Override
	public String getNature() {
		return nature;
	}

	@Override
	public void setNature(String nature) {
		this.nature = nature;
	}

	@Override
	public String getName() {
		StringBuffer buffer = new StringBuffer();
		TermEdge last = null;
		for (TermEdge edge : this.edges) {
			buffer.append(graph.getEdgeSource(edge).getName());
			last = edge;
		}
		if (last != null)
			buffer.append(graph.getEdgeTarget(last).getName());
		return buffer.toString();
	}

	@Override
	public boolean contains(Term term) {
		for (TermEdge edge : this.edges) {
			if (graph.getEdgeSource(edge).equals(term) || graph.getEdgeTarget(edge).equals(term))
				return true;
		}
		return false;
	}

	@Override
	public String toString() {
		StringBuffer buffer = new StringBuffer();
		buffer.append("weight:" + this.weight + ",nature:" + this.nature + ",confirmed:" + this.confirmed + this.edges);
		return buffer.toString();
	}

	@Override
	public void extendTo(Term term) {
		TermEdge edge = this.graph.getEdge(end, term);
		if (edge == null)
			throw new IllegalArgumentException("Could not extend path to term [" + term + "], it is not connected with last term [" + end + "].");
		this.edges.add(edge);
		this.end = term;

	}

	@Override
	public TermEdge removeEnd() {
		if (this.edges.size() == 0)
			throw new IndexOutOfBoundsException("Could not remove end from an empty path.");
		int pos = this.edges.size() - 1;
		TermEdge last = this.edges.get(pos);
		this.end = graph.getEdgeSource(last);
		this.edges.remove(pos);
		return last;
	}

	@Override
	public int getVertexCount() {
		return this.edges.size() + 1;
	}

	@Override
	public Term getVertex(int index) {
		if (index > this.edges.size())
			throw new IndexOutOfBoundsException("Could not get " + index + " vertext in path with size " + this.getVertexCount() + ".");
		if (index == this.edges.size())
			return this.end;
		if (index == 0)
			return this.start;
		if (index < 0)
			throw new IllegalArgumentException("index should greater than or equal 0");
		return graph.getEdgeSource(this.edges.get(index));
	}

	@Override
	public Term toTerm(TermNatures natures) {
		if (this.edges.size() == 0)
			return this.start;
		return graph.addTerm(this.start.getStartOffset(), this.end.getEndOffset(), this.getName(), natures);
	}

	@Override
	public List<Term> getVertextList() {
		return new EdgeList2VertextList();
	}

	@Override
	public TermGraph getTermGraph() {
		return this.graph;
	}

	class EdgeList2VertextList extends AbstractList<Term> {

		@Override
		public Term get(int index) {
			return DefaultTermPath.this.getVertex(index);
		}

		@Override
		public int size() {
			return DefaultTermPath.this.getVertexCount();
		}

	}

	@Override
	public TermGraph toTermGraph() {
		DefaultTermGraph newGraph = new DefaultTermGraph(this.graph.getSource());
		newGraph.addVertex(this.getStartVertex());
		for(TermEdge edge : this.edges){
			newGraph.addVertex(this.graph.getEdgeTarget(edge));
			newGraph.addEdge(this.graph.getEdgeSource(edge),this.graph.getEdgeTarget(edge), edge);
		}
		return newGraph;
	}

}
