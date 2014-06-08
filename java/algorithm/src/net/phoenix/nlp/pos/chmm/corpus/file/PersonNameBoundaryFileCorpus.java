/**
 * 
 */
package net.phoenix.nlp.pos.chmm.corpus.file;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import net.phoenix.nlp.Nature;
import net.phoenix.nlp.corpus.FileCorpus;
import net.phoenix.nlp.pos.chmm.CharNode;
import net.phoenix.nlp.pos.chmm.TermNatures;
import net.phoenix.nlp.pos.chmm.corpus.CharDFACorpus;
import net.phoenix.nlp.pos.chmm.corpus.PersonTermAttribute;

/**
 * 一个词作为名字前缀、后缀出现的概率
 * @author lixf
 * 
 */
public class PersonNameBoundaryFileCorpus extends FileCorpus {
	private CharDFACorpus charTree;

	public PersonNameBoundaryFileCorpus(CharDFACorpus charTree) throws IOException {
		//super(dictionary, "person.boundary.data");
		this.charTree = charTree;
	}

	@Override
	public void load(File boundaryFrequencyFile) throws IOException {
		BufferedReader br = null;
		try {
			br = new BufferedReader(new FileReader(boundaryFrequencyFile));
			String temp = null;
			while ((temp = br.readLine()) != null) {
				String[] strs = temp.split("\t");
				this.addBoundaryFrequency(strs[0], Integer.parseInt(strs[1]),
						Integer.parseInt(strs[2]));
			}
		} finally {
			if (br != null)
				br.close();
		}
	}

	/**
	 * 为DFA字典补录文字；
	 */
	private void addBoundaryFrequency(String name, int position, int frequency) {
		CharNode node = this.charTree.getNode(name);
		if (node == null)
			node = this.charTree.addNewWord(name, Nature.PersonName);
		TermNatures natures = node.getTermNatures();
		PersonTermAttribute attr = (PersonTermAttribute) natures
				.getAttribute(PersonTermAttribute.ATTRIBUTE);
		if (attr == null) {
			attr = new PersonTermAttribute();
			natures.setAttribute(PersonTermAttribute.ATTRIBUTE, attr);
		}
		attr.addBoundaryFrequency(position, frequency);
	}

}
