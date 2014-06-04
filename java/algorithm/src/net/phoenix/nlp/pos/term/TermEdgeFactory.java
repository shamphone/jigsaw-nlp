/**
 * 
 */
package net.phoenix.nlp.pos.term;

import net.phoenix.nlp.pos.Term;
import net.phoenix.nlp.pos.TermEdge;

import org.jgrapht.EdgeFactory;

/**
 * @author lixf
 * 
 */
public class TermEdgeFactory implements EdgeFactory<Term, TermEdge> {
	public TermEdgeFactory() {
	}

	@Override
	public TermEdge createEdge(Term sourceVertex, Term targetVertex) {
		return new DefaultTermEdge();
	}

}
