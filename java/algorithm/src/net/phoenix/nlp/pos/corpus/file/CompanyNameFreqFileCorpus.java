/**
 * 
 */
package net.phoenix.nlp.pos.corpus.file;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import net.phoenix.nlp.corpus.FileCorpus;
import net.phoenix.nlp.pos.CharNode;
import net.phoenix.nlp.pos.corpus.CharDFACorpus;
import net.phoenix.nlp.pos.corpus.CompanyTermAttribute;

/**
 * 这个表中列举各个词在机构名称前、后出现的概率，但是这些词都不是机构名称
 * @author lixf
 * 
 */
public class CompanyNameFreqFileCorpus extends FileCorpus  {
	private CharDFACorpus dfaCorpus;

	public CompanyNameFreqFileCorpus(CharDFACorpus charTree) throws IOException {
		//super(folder, "company.location.data");
		this.dfaCorpus = charTree;
	}

	/**
	 * 加载机构名称属性表；这个表中列举各个词在机构名称前、后出现的概率，但是这些词都不是机构名称。
	 * 
	 * @throws IOException
	 */
	public void load(File namePath) throws IOException {
		BufferedReader br = null;
		try {
			// br = new BufferedReader(new FileReader("company/company.data"));
			br = new BufferedReader(new FileReader(namePath));
			String temp = null;
			String[] strs = null;
			CompanyTermAttribute cna = null;

			int p = 0;
			int b = 0;
			int m = 0;
			int e = 0;
			int s = 0;
			int allFreq = 0;

			while ((temp = br.readLine()) != null) {
				strs = temp.split("\t");
				p = Integer.parseInt(strs[1]);
				b = Integer.parseInt(strs[2]);
				m = Integer.parseInt(strs[3]);
				e = Integer.parseInt(strs[4]);
				s = Integer.parseInt(strs[5]);
				allFreq = Integer.parseInt(strs[6]);
				cna = new CompanyTermAttribute(p, b, m, e, s, allFreq);
				this.makeupNewWords(strs[0], cna);
			}
		} finally {
			if (br != null)
				br.close();
		}
	}

	/**
	 * 为DFA字典补录文字；
	 */
	private void makeupNewWords(String name, CompanyTermAttribute attr) {
		CharNode node = this.dfaCorpus.getNode(name);
		if (node != null)
			node.getTermNatures().setAttribute(CompanyTermAttribute.ATTRIBUTE,
					attr);
		//else
		//	log.info("unrecorded word:" + name);
	}

}
