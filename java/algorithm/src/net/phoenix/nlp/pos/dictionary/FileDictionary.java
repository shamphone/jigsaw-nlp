/**
 * 
 */
package net.phoenix.nlp.pos.dictionary;

import java.io.File;

import net.phoenix.nlp.pos.Dictionary;

/**
 * @author lixf
 *
 */
public class FileDictionary implements Dictionary{
	private File folder;
	public FileDictionary(File folder){
		this.folder = folder;
	}

	@Override
	public <T extends Dictionary> T getDictionary(Class<T> clazz) {
		if(clazz.isInstance(this))
			return clazz.cast(this);
		return null;
	}
	
	public File getFolder() {
		return this.folder;
	}
}
