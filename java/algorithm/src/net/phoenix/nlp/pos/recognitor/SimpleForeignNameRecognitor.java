/**
 * 
 */
package net.phoenix.nlp.pos.recognitor;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import net.phoenix.nlp.Nature;
import net.phoenix.nlp.corpus.CorpusRepository;
import net.phoenix.nlp.pos.POSTerm;
import net.phoenix.nlp.pos.TermEdge;
import net.phoenix.nlp.pos.TermGraph;
import net.phoenix.nlp.pos.TermNatures;
import net.phoenix.nlp.pos.corpus.CharsetCorpus;
import net.phoenix.nlp.pos.corpus.PersonTermAttribute;
import net.phoenix.nlp.pos.corpus.file.CharsetFileCorpus;

/**
 * @author lixf
 * 
 */
public class SimpleForeignNameRecognitor extends NameRecognitor {
	private char[] candidates;
	
	public SimpleForeignNameRecognitor(CorpusRepository dictionary) throws IOException{
		super(dictionary);
		CharsetCorpus chars = dictionary.getCorpus(CharsetFileCorpus.class);
		this.candidates = chars.getChars("person");
	}
	
	
	
	@Override
	public void recognize(TermGraph graph) {
		List<POSTerm> currentVertexes = new ArrayList<POSTerm>(graph.vertexSet());
		for (POSTerm term : currentVertexes) {
			// 如果名字的开始是人名的前缀,或者后缀.那么忽略
			PersonTermAttribute attr = (PersonTermAttribute) term.getTermNatures().getAttribute(PersonTermAttribute.ATTRIBUTE);
			if (attr!=null && attr.getFollowingFrequency() <= 10 && this.isFName(term)) {
				List<POSTerm> partName = new ArrayList<POSTerm>();
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
	private void findNames(TermGraph graph, List<POSTerm> partName) {
		int pos = partName.size();
		POSTerm current = partName.get(pos - 1);
		for (TermEdge edge : graph.outgoingEdgesOf(current)) {
			POSTerm next = graph.getEdgeTarget(edge);
			if (this.isFName(next)) {
					partName.add(next);
					findNames(graph, partName);
					partName.remove(next);
			} else if (partName.size() > 1) {
				this.createMergedTerm(graph, partName, createTermNatures(Nature.PersonName), false);
			}
		}

	}

	
	private boolean isFName(POSTerm term){
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
