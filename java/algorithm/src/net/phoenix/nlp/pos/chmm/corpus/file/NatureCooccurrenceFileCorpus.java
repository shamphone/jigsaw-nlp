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
import net.phoenix.nlp.pos.chmm.corpus.NatureCooccurrenceCorpus;

/**
 * 词性之间的共现关系
 * @author lixf
 *
 */
public class NatureCooccurrenceFileCorpus extends FileCorpus implements NatureCooccurrenceCorpus{
	private int[][] natureCooccurrence;	
	public NatureCooccurrenceFileCorpus(){
		//super(folder, "nature.cooccurrence.data");
	}	
	/* (non-Javadoc)
	 * @see net.phoenix.nlp.pos.corpus.file.NatureCooccurrenceCorpus#getOccurrenctFrequency(net.phoenix.nlp.Nature, net.phoenix.nlp.Nature)
	 */
	@Override
	public int getOccurrenctFrequency(Nature from, Nature to) {
		if (from.toInt() < 0 || to.toInt() < 0) {
			return 0;
		}
		return natureCooccurrence[from.toInt()][to.toInt()];
	}
	@Override
	public void load(File natureCooccurrenceFile) throws IOException {
		if(this.natureCooccurrence == null) {
			// 加载词性关系
			//"nature/nature.table")
			int maxLength = Nature.count();
			natureCooccurrence = new int[maxLength + 1][maxLength + 1];
			
		}
		String seperator = "\t";
		String line = null;

		BufferedReader reader = new BufferedReader(new FileReader(natureCooccurrenceFile));
		int j = 0;
		while ((line = reader.readLine()) != null) {
			if (isBlank(line))
				continue;
			String[] strs = line.split(seperator);
			for (int i = 0; i < strs.length; i++) {
				natureCooccurrence[j][i] = Integer.parseInt(strs[i]);
			}
			j++;
		}
		reader.close();
		
	}
	
}
