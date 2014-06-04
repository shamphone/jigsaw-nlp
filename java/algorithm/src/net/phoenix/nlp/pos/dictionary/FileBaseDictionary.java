/**
 * 
 */
package net.phoenix.nlp.pos.dictionary;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;

import net.phoenix.nlp.pos.Dictionary;
import net.phoenix.nlp.pos.DictionaryWrapper;

/**
 * @author lixf
 * 
 */
public abstract class FileBaseDictionary extends DictionaryWrapper {
	/**
	 * 初始化并加载词典
	 * @param dictionary
	 * @param postfix
	 * @throws IOException
	 */
	public FileBaseDictionary(Dictionary dictionary, final String postfix)
			throws IOException {
		super(dictionary);
		FileDictionary parent = dictionary.getDictionary(FileDictionary.class);
		File folder = parent.getFolder();
		File[] files = folder.listFiles(new FilenameFilter() {

			@Override
			public boolean accept(File dir, String name) {
				return name.equalsIgnoreCase(postfix)
						|| name.toLowerCase().endsWith(
								"." + postfix.toLowerCase());
			}
		});

		for (File file : files) {
			this.load(file);
		}

	}

	protected void load(File file) throws IOException {
		
	}

}
