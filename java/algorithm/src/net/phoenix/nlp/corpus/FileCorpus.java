/**
 * 
 */
package net.phoenix.nlp.corpus;

import java.io.File;
import java.io.IOException;

/**
 * 基于文件的语料库；
 * @author lixf
 * 
 */
public abstract class FileCorpus extends AbstractCorpus {
	
	/**
	 * 子类覆盖这个方法实现加载；
	 * @param file
	 * @throws IOException
	 */
	public abstract void load(File file) throws IOException;

}
