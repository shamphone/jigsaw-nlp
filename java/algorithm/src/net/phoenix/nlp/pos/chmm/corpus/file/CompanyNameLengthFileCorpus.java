/**
 * 
 */
package net.phoenix.nlp.pos.chmm.corpus.file;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import net.phoenix.nlp.corpus.FileCorpus;
import net.phoenix.nlp.pos.chmm.corpus.CompanyNameLengthCorpus;

/**
 * 公司名字长度
 * @author lixf
 * 
 */
public class CompanyNameLengthFileCorpus extends FileCorpus implements CompanyNameLengthCorpus {
	private double freqOfLength[]; // 长度为1~50的机构名字出现的概率

	public CompanyNameLengthFileCorpus() throws IOException {
		//super(folder, "company.length.data");
	}

	/* (non-Javadoc)
	 * @see net.phoenix.nlp.pos.corpus.file.CompanyNameLengthCorpus#getFrequency(int)
	 */
	@Override
	public double getFrequency(int length) {
		return freqOfLength[length > 50 ? 50 : length];
	}

	/**
	 * 加载频率表。 频率表记录长度为N的公司名字的出现的概率，其中N=1..50。
	 * 
	 * @throws IOException
	 */
	public void load(File frequencyPath) throws IOException {
		BufferedReader reader = null;
		String temp = null;
		this.freqOfLength = new double[51];
		String[] strs = null;
		int index = 0;
		float fac = 0;
		try {
			reader = new BufferedReader(new FileReader(frequencyPath));
			while ((temp = reader.readLine()) != null) {
				if (isBlank(temp = temp.trim())) {
					continue;
				}
				strs = temp.split("\t");
				index = Integer.parseInt(strs[0]);
				fac = Float.parseFloat(strs[2]);
				if (index > 50) {
					index = 50;
				}
				freqOfLength[index] += fac;
			}

		} finally {
			reader.close();
		}
	}

}
