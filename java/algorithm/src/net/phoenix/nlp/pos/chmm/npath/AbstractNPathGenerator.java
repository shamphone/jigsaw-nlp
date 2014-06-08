/**
 * 
 */
package net.phoenix.nlp.pos.chmm.npath;

import java.util.ArrayList;
import java.util.List;

import net.phoenix.nlp.corpus.CorpusRepository;
import net.phoenix.nlp.pos.chmm.AbstractProcessor;
import net.phoenix.nlp.pos.chmm.NPathGenerator;
import net.phoenix.nlp.pos.chmm.POSTerm;
import net.phoenix.nlp.pos.chmm.TermEdge;
import net.phoenix.nlp.pos.chmm.TermGraph;
import net.phoenix.nlp.pos.chmm.TermPath;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jgrapht.GraphPath;
import org.jgrapht.alg.KShortestPaths;

/**
 * @author lixf
 * 
 */
public class AbstractNPathGenerator extends AbstractProcessor implements NPathGenerator {
	private static final Log log = LogFactory.getLog(AbstractNPathGenerator.class);
	private int pathCount;

	public AbstractNPathGenerator(CorpusRepository dictionary) {
		super(dictionary);
		this.pathCount = 8;
	}

	@Override
	public List<TermPath> process(TermGraph graph) {
		this.scoreEdges(graph);
		return this.findNPath(graph, pathCount);
	}

	/**
	 * 寻找N条最短的路径。
	 * 
	 * @param graph
	 * @param count
	 *            最短路径条数；
	 * @return
	 */
	protected List<TermPath> findNPath(TermGraph graph, int count) {
		KShortestPaths<POSTerm, TermEdge> alg = new KShortestPaths<POSTerm, TermEdge>(graph, graph.getStartVertex(), count);
		List<GraphPath<POSTerm, TermEdge>> pathes = alg.getPaths(graph.getEndVertex());
		List<TermPath> result = new ArrayList<TermPath>();
		if(pathes==null) {
			log.error("Error in finding path for :"+ graph.getSource());
			return result;
		}
		for (GraphPath<POSTerm, TermEdge> path : pathes) {
			//log.info(path);
			result.add(graph.createPath(path));
		}
		return result;
	}

	/**
	 * 给边打分；
	 * 
	 * @param gp
	 */
	protected void scoreEdges(TermGraph gp) {
		for (TermEdge edge : gp.edgeSet()) {
			gp.setEdgeWeight(edge, 1.0);
		}
	}

}
