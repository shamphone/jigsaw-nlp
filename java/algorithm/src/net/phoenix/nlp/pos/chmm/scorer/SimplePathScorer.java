/**
 * 
 */
package net.phoenix.nlp.pos.chmm.scorer;

import net.phoenix.nlp.pos.chmm.PathScorer;
import net.phoenix.nlp.pos.chmm.TermPath;

/**
 * @author lixf
 *
 */
public class SimplePathScorer implements PathScorer{

	@Override
	public double score(TermPath path) {		
		return path.getEdgeList().size();
	}


}
