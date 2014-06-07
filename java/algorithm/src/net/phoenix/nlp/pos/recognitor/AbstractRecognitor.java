/**
 * 
 */
package net.phoenix.nlp.pos.recognitor;

import java.util.ArrayList;
import java.util.List;

import net.phoenix.nlp.Term;
import net.phoenix.nlp.corpus.CorpusRepository;
import net.phoenix.nlp.pos.AbstractProcessor;
import net.phoenix.nlp.pos.POSTerm;
import net.phoenix.nlp.pos.Recognitor;
import net.phoenix.nlp.pos.TermEdge;
import net.phoenix.nlp.pos.TermGraph;
import net.phoenix.nlp.pos.TermNatures;
import net.phoenix.nlp.pos.TermPath;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jgrapht.alg.BellmanFordShortestPath;

/**
 * Recognitor基类，提供recogintor中将使用的各种公用方法；
 * 
 * @author lixf
 * 
 */
public abstract class AbstractRecognitor extends AbstractProcessor implements Recognitor {
	protected Log log = LogFactory.getLog(this.getClass());
	
	public AbstractRecognitor(CorpusRepository dictionary) {
		super(dictionary);
	}
	

	/**
	 * 从图中识别出新词来
	 * 
	 * @param pathes
	 *            上一个阶段已经识别好的路径。本阶段可以重新生成，也可以对这些路径进行排序，也可以进行优化。
	 * @param graph
	 * @return
	 */
	@Override
	public List<TermPath> process(TermGraph graph) {
		this.recognize(graph);
		ArrayList<TermPath> pathes = new ArrayList<TermPath>();
		pathes.add(this.findShortestPath(graph));
		return pathes;
	}

	/**
	 * 如果只返回一条路径，子类仅需覆盖这个方法的实现来识别未登录词
	 * 
	 * @param graph
	 */
	protected void recognize(TermGraph graph) {
	}


	/**
	 * 获取graph中的最短路径；
	 * @param graph
	 * @return
	 */
	protected TermPath findShortestPath(TermGraph graph) {
		List<TermEdge> edges = (BellmanFordShortestPath.findPathBetween(graph, graph.getStartVertex(), graph.getEndVertex()));
		return graph.createPath(edges);
	}

	protected Term createMergedTerm(TermGraph graph, TermPath found, TermNatures natures, boolean removeExisting) {

		// 建立新节点；
		POSTerm term = found.toTerm(natures);
		// 将原来连接到初始节点的节点，都连接到新节点上；
		for (TermEdge edge : graph.incomingEdgesOf(found.getStartVertex())) {
			POSTerm source = graph.getEdgeSource(edge);
			TermEdge leading = graph.addEdge(source, term);
			if (leading != null) {
				leading.setWeight(edge.getWeight());
			}
		}

		for (TermEdge edge : graph.outgoingEdgesOf(found.getEndVertex())) {
			POSTerm target = graph.getEdgeTarget(edge);
			TermEdge following = graph.addEdge(term, target);
			if (following != null) {
				following.setWeight(edge.getWeight());
			}
		}

		// 删除已有节点之间的边；
		if (removeExisting) {
			graph.removeAllEdges(found.getEdgeList());
		}
		return term;
	}

	protected POSTerm createMergedTerm(TermGraph graph, List<POSTerm> found, TermNatures natures, boolean removeExisting) {
		int from = found.get(0).getStartOffset();
		int to = found.get(found.size() - 1).getEndOffset();
		StringBuffer name = new StringBuffer();
		for (Term term : found) {
			name.append(term.getName());
		}

		// 建立新节点；
		POSTerm term = graph.addTerm(from, to, name.toString(), natures);
		// 将原来连接到初始节点的节点，都连接到新节点上；
		for (TermEdge edge : graph.incomingEdgesOf(found.get(0))) {
			POSTerm source = graph.getEdgeSource(edge);
			TermEdge leading = graph.addEdge(source, term);
			if (leading != null) {
				leading.setWeight(edge.getWeight());
			}
		}

		for (TermEdge edge : graph.outgoingEdgesOf(found.get(found.size() - 1))) {
			POSTerm target = graph.getEdgeTarget(edge);
			TermEdge following = graph.addEdge(term, target);
			if (following != null) {
				following.setWeight(edge.getWeight());
			}
		}

		// 删除已有节点之间的边；
		if (removeExisting) {
			POSTerm previous = null;
			for (POSTerm inner : found) {
				if (previous == null)
					previous = inner;
				else {
					graph.removeEdge(previous, inner);
					previous = inner;
				}
			}
		}
		return term;
	}
	
	/**
	 * 将所有的边的分数都修改为指定分数；
	 * @param graph
	 * @param score
	 */
	protected void resetEdgesScore(TermGraph graph, double score) {
		for(TermEdge edge : graph.edgeSet()){
			graph.setEdgeWeight(edge, score);
		}
	}
	

}
