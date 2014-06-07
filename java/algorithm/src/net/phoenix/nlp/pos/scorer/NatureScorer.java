/**
 * 
 */
package net.phoenix.nlp.pos.scorer;

import net.phoenix.nlp.Term;
import net.phoenix.nlp.corpus.CorpusRepository;
import net.phoenix.nlp.pos.AbstractProcessor;
import net.phoenix.nlp.pos.PathScorer;
import net.phoenix.nlp.pos.TermEdge;
import net.phoenix.nlp.pos.TermPath;
import net.phoenix.nlp.pos.corpus.NatureCooccurrenceCorpus;
import net.phoenix.nlp.pos.corpus.file.NatureCooccurrenceFileCorpus;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * 根据词性语法对切分结果进行打分。 考虑如下因素： 1. 最佳的切分结果，形成的词应该较少。 2. 最佳切分结果形成的句法是最优的。
 * 
 * @author lixf
 * 
 */
public class NatureScorer extends AbstractProcessor implements PathScorer {

	private static Log log = LogFactory.getLog(NatureScorer.class);

	private NatureCooccurrenceCorpus natures;
	public NatureScorer(CorpusRepository dictionary) {
		super(dictionary);
		this.natures = dictionary.getCorpus(NatureCooccurrenceFileCorpus.class);
	}

	/**
	 * 进行最佳词性查找,引用赋值.所以不需要有返回值
	 */
	@Override
	public double score(TermPath path) {
		double score = 0;
		for (TermEdge edge : path.getEdgeList()) {
			Term from = path.getTermGraph().getEdgeSource(edge);
			Term to = path.getTermGraph().getEdgeTarget(edge);
			if (from.getNature() == null)
				log.error("No nature for [" + from + "] in " + path);
			if (to.getNature() == null)
				log.error("No nature from [" + to + "] in " + path);
			score += Math.log(natures.getOccurrenctFrequency(from.getNature(), to.getNature()) + 1.0);
			//log.info(from+"-"+to +":" +Math.log(natures.getOccurrenctFrequency(from.getNature(), to.getNature()) + 1.0));
		}
		score = (path.getVertexCount()) / score;
		//log.info("[" + score + "]" + path);
		return score;
	}

	
}
