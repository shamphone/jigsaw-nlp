/**
 * 
 */
package net.phoenix.nlp.pos.term;

import net.phoenix.nlp.pos.POSTerm;
import net.phoenix.nlp.pos.TermEdge;

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
