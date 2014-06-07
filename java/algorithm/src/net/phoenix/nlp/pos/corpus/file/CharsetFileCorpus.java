/**
 * 
 */
package net.phoenix.nlp.pos.corpus.file;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Properties;

import net.phoenix.nlp.corpus.FileCorpus;
import net.phoenix.nlp.pos.corpus.CharsetCorpus;

/**
 * 字符串集合的设置，默认提供日期、时间、数字、常用名中使用的字符。
 * 默认从chars.data文件中加载数据。
 * @author lixf
 *
 */
public class CharsetFileCorpus extends FileCorpus implements CharsetCorpus{
	private Properties chars;
	
	public CharsetFileCorpus() {
		//super(folder, "chars.data");		
	}

	@Override
	public void load(File file) throws IOException {
		if(chars==null)
			chars = new Properties();
		
		Properties properties = new Properties();
		properties.load(new InputStreamReader(new FileInputStream(file),"UTF-8"));
		this.chars.putAll(properties);
	}
	
	/* (non-Javadoc)
	 * @see net.phoenix.nlp.pos.corpus.file.CharsetCorpus#getChars(java.lang.String)
	 */
	@Override
	public char[] getChars(String name) {
		StringBuffer buffer  = new StringBuffer("");
		for(Object keyObj : this.chars.keySet()){
			String key = keyObj.toString();
			if(key.startsWith(name))
				buffer.append(this.chars.get(keyObj));
		}
		return buffer.toString().toCharArray();
	}

}
