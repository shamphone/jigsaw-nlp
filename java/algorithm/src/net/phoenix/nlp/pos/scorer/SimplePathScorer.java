/**
 * 
 */
package net.phoenix.nlp.pos.scorer;

import net.phoenix.nlp.pos.PathScorer;
import net.phoenix.nlp.pos.TermPath;

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
