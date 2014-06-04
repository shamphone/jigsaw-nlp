/**
 * 
 */
package net.phoenix.nlp.pos.recognitor;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import net.phoenix.nlp.pos.Dictionary;
import net.phoenix.nlp.pos.Nature;
import net.phoenix.nlp.pos.Term;
import net.phoenix.nlp.pos.TermEdge;
import net.phoenix.nlp.pos.TermGraph;
import net.phoenix.nlp.pos.TermNatures;
import net.phoenix.nlp.pos.dictionary.CharsDictionary;
import net.phoenix.nlp.pos.dictionary.PersonTermAttribute;

/**
 * @author lixf
 * 
 */
public class SimpleForeignNameRecognitor extends NameRecognitor {
	private char[] candidates;
	
	public SimpleForeignNameRecognitor(Dictionary dictionary) throws IOException{
		super(dictionary);
		CharsDictionary chars = dictionary.getDictionary(CharsDictionary.class);
		this.candidates = chars.getChars("person");
	}
	
	
	
	@Override
	public void recognize(TermGraph graph) {
		List<Term> currentVertexes = new ArrayList<Term>(graph.vertexSet());
		for (Term term : currentVertexes) {
			// 如果名字的开始是人名的前缀,或者后缀.那么忽略
			PersonTermAttribute attr = (PersonTermAttribute) term.getTermNatures().getAttribute(PersonTermAttribute.ATTRIBUTE);
			if (attr!=null && attr.getFollowingFrequency() <= 10 && this.isFName(term)) {
				List<Term> partName = new ArrayList<Term>();
				partName.add(term);
				this.findNames(graph, partName);
			}
		}
	}

	

	/**
	 * 通过遍历来发现名字。
	 * 
	 * @param graph
	 *            句图
	 * @param partName
	 *            当前已经发现的名字片段或者名字
	 */
	private void findNames(TermGraph graph, List<Term> partName) {
		int pos = partName.size();
		Term current = partName.get(pos - 1);
		for (TermEdge edge : graph.outgoingEdgesOf(current)) {
			Term next = graph.getEdgeTarget(edge);
			if (this.isFName(next)) {
					partName.add(next);
					findNames(graph, partName);
					partName.remove(next);
			} else if (partName.size() > 1) {
				this.createMergedTerm(graph, partName, createTermNatures(Nature.PersonName), false);
			}
		}

	}

	
	private boolean isFName(Term term){
		TermNatures termNatures = term.getTermNatures();
		String name = term.getName();
		if (termNatures.isNature(Nature.PersonName) || termNatures.isNature(Nature.NULL)||name.length()==1) 
			return isFName(name);
		return false;
	}

	public boolean isFName(String name) {		
		for (int i = 0; i < name.length(); i++) {
			if (Arrays.binarySearch(this.candidates,name.charAt(i))<0) {
				return false;
			}
		}
		return true;
	}

	
}
