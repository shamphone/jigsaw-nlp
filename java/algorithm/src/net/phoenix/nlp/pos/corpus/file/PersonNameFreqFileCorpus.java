/**
 * 
 */
package net.phoenix.nlp.pos.corpus.file;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import net.phoenix.nlp.Nature;
import net.phoenix.nlp.corpus.FileCorpus;
import net.phoenix.nlp.pos.CharNode;
import net.phoenix.nlp.pos.TermNatures;
import net.phoenix.nlp.pos.corpus.CharDFACorpus;
import net.phoenix.nlp.pos.corpus.PersonNameFreqCorpus;
import net.phoenix.nlp.pos.corpus.PersonTermAttribute;

/**
 * @author lixf
 * 
 */
public class PersonNameFreqFileCorpus extends FileCorpus implements PersonNameFreqCorpus {
	// private Map<String, PersonTermAttribute> pnMap = null;
	private CharDFACorpus charTree;

	public PersonNameFreqFileCorpus(CharDFACorpus charTree) throws IOException {
		//super(dictionary, "person.freq.data");
		this.charTree = charTree;

	}

	/**
	 * 
	 * 读取字典中人名常用字频率表。字典按照如下方式组织： 第一个是词；
	 * 第1个数组，含两个整数，由两个词组成的名字，作为名字第一个字符（姓）的概率，作为名字第二个字符（名）的概率；
	 * 第2个数组，含三个整数，由三个词组成的名字，作为名字第一个字符（姓）的概率，作为名字第二个字符（名）的概率，作为名字第三个字符（名）的概率；
	 * 第3个数组，含四个整数，由四个词组成的名字，作为名字第一个字符（姓）的概率，作为名字第二个字符（名）的概率，作为名字第三个字符（名）的概率，
	 * 作为名字第四个字符（名）的概率； 如： 涛:1 486,0 107 2342,0 0 0 7 李:2724 31,14157 251 27,13
	 * 4 18 2
	 * 
	 * @return
	 * @throws IOException
	 */
	@Override
	public void load(File nameFrequencyFile) throws IOException {
		BufferedReader reader = null;
		try {
			reader = new BufferedReader(new FileReader(nameFrequencyFile));
			String line = reader.readLine();
			while (line != null) {
				String[] entry = line.split("\\:");
				String key = entry[0].trim();
				String[] rows = entry[1].trim().split("\\,");
				int[][] values = new int[rows.length][];
				for (int i = 0; i < rows.length; i++) {
					String[] columns = rows[i].trim().split("\\s+");
					values[i] = new int[columns.length];
					for (int j = 0; j < columns.length; j++)
						values[i][j] = Integer.parseInt(columns[j]);
				}
				this.setLocationFrequency(key, values);
				line = reader.readLine();
			}
		} finally {
			reader.close();
		}
	}

	/**
	 * 为DFA字典补录文字；
	 */
	private void setLocationFrequency(String name, int[][] values) {
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
		attr.setLocationFrequency(values);
	}
}
