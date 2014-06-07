/**
 * 
 */
package net.phoenix.nlp.pos.corpus.file;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import net.phoenix.nlp.Nature;
import net.phoenix.nlp.corpus.FileCorpus;
import net.phoenix.nlp.pos.corpus.NatureFreqCorpus;

/**
 * Nature出现的频率
 * @author lixf
 *
 */
public class NatureFreqFileCorpus  extends FileCorpus implements NatureFreqCorpus{
	/**
	 * 词性的字符串对照索引位的hashmap
	 */
	private Map<Nature, Integer> natureFrequency;
	
	public NatureFreqFileCorpus() throws IOException{
		//super(file, "nature.freq.data");
	}
	
	@Override
	public int getSize() {
		return this.natureFrequency.size();		
	}
	
	@Override
	public int getFrequency(Nature nature){
		return this.natureFrequency.get(nature);
	}

	@Override
	public void load(File natureFrequencyFile) throws IOException {
		if(this.natureFrequency == null)
			natureFrequency = new HashMap<Nature, Integer>();
		String split = "\t";
		// 加载词对照性表
		//nature/nature.map
		BufferedReader reader = new BufferedReader(new FileReader(natureFrequencyFile));
		String temp = null;
		String[] strs = null;
		int index = 0;
		int freq = 0;
		while ((temp = reader.readLine()) != null) {
			strs = temp.split(split);
			if (strs.length != 4)
				continue;

			index = Integer.parseInt(strs[0]);
			//p1 = Integer.parseInt(strs[1]);
			freq = Integer.parseInt(strs[3]);
			Nature nature = Nature.register(strs[2], index);
			this.natureFrequency.put(nature, freq);
			
		}
		reader.close();		

		
	}
}
