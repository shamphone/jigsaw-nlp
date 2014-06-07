/**
 * 
 */
package net.phoenix.nlp.pos.corpus.file;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import net.phoenix.nlp.corpus.FileCorpus;
import net.phoenix.nlp.pos.corpus.CooccurrenceCorpus;

import org.apache.commons.collections.map.MultiKeyMap;

/**
 * 词与词之间的共现关系。
 * @author lixf
 *
 */
public class CooccurrenceFileCorpus extends FileCorpus implements CooccurrenceCorpus{
	private MultiKeyMap bigramTables; //词之间的共现关系；
	
	public CooccurrenceFileCorpus() {
		//super(folder, "term.cooccurrence.data");
	}

	@Override
	public void load(File bigramFilePath) throws IOException {
		if(this.bigramTables == null)
			this.bigramTables = new MultiKeyMap();
		BufferedReader reader = new BufferedReader(new FileReader(bigramFilePath));
		String row = reader.readLine().trim();
		while (row != null) {
			row = row.trim();
			if (row.length() > 0) {
				String[] items = row.split("\\s");
				int freq = Integer.parseInt(items[2]);
				bigramTables.put(items[0], items[1], freq);
			}
			row = reader.readLine();
		}
		reader.close();
	}

	/* (non-Javadoc)
	 * @see net.phoenix.nlp.pos.corpus.file.CooccurrenceCorpus#getCooccurrenceFrequency(java.lang.String, java.lang.String)
	 */
	@Override
	public int getCooccurrenceFrequency(String from, String to) {
		Integer freq = (Integer) bigramTables.get(from, to);
		if (freq == null)
			return 0;
		return freq;
	}
	
}
