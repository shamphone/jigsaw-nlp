package net.phoenix.nlp.pos.recognitor;

import java.io.IOException;
import java.util.List;

import net.phoenix.nlp.Nature;
import net.phoenix.nlp.corpus.CorpusRepository;
import net.phoenix.nlp.pos.POSTerm;
import net.phoenix.nlp.pos.TermGraph;
import net.phoenix.nlp.pos.corpus.NatureCooccurrenceCorpus;
import net.phoenix.nlp.pos.corpus.file.NatureCooccurrenceFileCorpus;

import org.jgrapht.EdgeFactory;
import org.jgrapht.alg.BellmanFordShortestPath;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleDirectedWeightedGraph;

/**
 * 词性标注工具类
 * 
 * @author ansj
 * 
 */
public class NatureRecognitor extends AbstractRecognitor {
	private NatureCooccurrenceCorpus cooccurrence;
	public NatureRecognitor(CorpusRepository dictionary) throws IOException {		
		super(dictionary);
		this.cooccurrence = dictionary.getCorpus(NatureCooccurrenceFileCorpus.class);
		
	}

	/**
	 * 进行最佳词性查找,引用赋值.所以不需要有返回值
	 */
	@Override
	protected void recognize(TermGraph graph) {
		NatureTermGraph natureGraph = new NatureTermGraph();

		NatureTerm start = null;
		NatureTerm end = null;
		for (POSTerm term : graph.vertexSet()){
			boolean hasNature = false; //该Term是否有nature；
			for (Nature nature : term.getTermNatures().natures()) {
				NatureTerm item = new NatureTerm(term, nature, term.getTermNatures().getFrequency(nature));
				if (graph.getStartVertex().equals(term)) {
					item.score = 0;
					start = item;
				} else if (graph.getEndVertex().equals(term)) {
					end = item;
				}
				natureGraph.addVertex(item);
				hasNature = true;
			}
			if(!hasNature)
				natureGraph.addVertex(new NatureTerm(term, Nature.valueOf("n"), 0)); //默认的设置为名词
		}
		for (NatureTerm from : natureGraph.vertexSet())
			for (NatureTerm to : natureGraph.vertexSet()) {
				if (graph.getEdge(from.term, to.term) != null) {
					natureGraph.addEdge(from, to);
				}
			}

		
		List<NatureTermEdge> edges = (BellmanFordShortestPath.findPathBetween(natureGraph, start, end));
		for (NatureTermEdge edge : edges) {
			NatureTerm nt = natureGraph.getEdgeTarget(edge);
			nt.term.setNature(nt.nature);
		}
	
	}

	/**
	 * 关于这个term的词性
	 * 
	 * @author ansj
	 * 
	 */
	class NatureTerm {
		public POSTerm term;
		public Nature nature;
		public double score = Double.NaN;
		public double selfScore = 1;

		NatureTerm(POSTerm term, Nature nature, int freq) {
			this.term = term;
			this.nature = nature;
			this.selfScore = freq + 1;
			this.score = Double.NaN;
		}
		
		public String toString(){
			return term.getName()+"/"+nature.toInt();
		}

	}

	class NatureTermEdge extends DefaultWeightedEdge {
		/**
		 * 
		 */
		private static final long serialVersionUID = -1233335717530439856L;
		public double weight = 0;

		@Override
		public double getWeight() {
			return weight;
		}
	}

	class NatureTermEdgeFactory implements EdgeFactory<NatureTerm, NatureTermEdge> {

		@Override
		public NatureTermEdge createEdge(NatureTerm from, NatureTerm to) {
			NatureTermEdge edge = new NatureTermEdge();
			double coorrence = cooccurrence.getOccurrenctFrequency(from.nature, to.nature);
			if (coorrence == 0) {
				coorrence = Math.log(from.selfScore + to.selfScore);
			}
			// double score = from.score + Math.log((from.selfScore +
			// to.selfScore) * coorrence) + Math.log(to.selfScore);
			double score = from.score + Math.log((from.selfScore + to.selfScore) * coorrence) + to.selfScore;
			edge.weight = score;
			if (Double.isNaN(to.score) || to.score < score)
				to.score = score;
			return edge;
		}
	}

	class NatureTermGraph extends SimpleDirectedWeightedGraph<NatureTerm, NatureTermEdge> {

		private static final long serialVersionUID = -6094581477691079359L;

		public NatureTermGraph() {
			super(new NatureTermEdgeFactory());
		}

	}
}
