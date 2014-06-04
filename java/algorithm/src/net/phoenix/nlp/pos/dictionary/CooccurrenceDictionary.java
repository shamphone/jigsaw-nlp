/**
 * 
 */
package net.phoenix.nlp.pos.dictionary;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import net.phoenix.nlp.pos.Dictionary;

import org.apache.commons.collections.map.MultiKeyMap;

/**
 * @author lixf
 *
 */
public class CooccurrenceDictionary extends FileBaseDictionary{
	private MultiKeyMap bigramTables; //词之间的共现关系；
	
	public CooccurrenceDictionary(Dictionary dictionary)
			throws IOException {
		super(dictionary, "term.cooccurrence.data");
	}

	@Override
	protected void load(File bigramFilePath) throws IOException {
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

	/**
	 * 查找两个词与词之间的频率
	 * 
	 * @param from
	 * @param to
	 * @return
	 */
	public int getCooccurrenceFrequency(String from, String to) {
		Integer freq = (Integer) bigramTables.get(from, to);
		if (freq == null)
			return 0;
		return freq;
	}
	
}
