/**
 * 
 */
package net.phoenix.nlp.pos.chmm.term;

import net.phoenix.nlp.pos.chmm.POSTerm;
import net.phoenix.nlp.pos.chmm.TermEdge;

import org.jgrapht.EdgeFactory;

/**
 * @author lixf
 * 
 */
public class TermEdgeFactory implements EdgeFactory<POSTerm, TermEdge> {
	public TermEdgeFactory() {
	}

	@Override
	public TermEdge createEdge(POSTerm sourceVertex, POSTerm targetVertex) {
		return new DefaultTermEdge();
	}

}
