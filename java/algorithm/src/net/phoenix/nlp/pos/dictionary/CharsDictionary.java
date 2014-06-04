/**
 * 
 */
package net.phoenix.nlp.pos.dictionary;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Properties;

import net.phoenix.nlp.pos.Dictionary;

/**
 * 
 * @author lixf
 *
 */
public class CharsDictionary extends FileBaseDictionary{
	private Properties chars;
	public CharsDictionary(Dictionary dictionary)
			throws IOException {
		super(dictionary, "chars.data");		
	}

	@Override
	protected void load(File file) throws IOException {
		if(chars==null)
			chars = new Properties();
		
		Properties properties = new Properties();
		properties.load(new InputStreamReader(new FileInputStream(file),"UTF-8"));
		this.chars.putAll(properties);
	}
	
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
