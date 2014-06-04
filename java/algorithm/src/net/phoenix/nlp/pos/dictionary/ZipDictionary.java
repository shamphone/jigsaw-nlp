/**
 * 
 */
package net.phoenix.nlp.pos.dictionary;

import java.io.IOException;
import java.io.InputStream;

import net.phoenix.nlp.pos.Dictionary;

/**
 * @author lixf
 *
 */
public abstract class ZipDictionary implements Dictionary{
	/**
	 * 从给定的流中加载数据；
	 * @param is
	 * @return
	 */
	public abstract void read(InputStream is) throws IOException;
	
	/**
	 * 获取已经读取的数据；
	 * @return
	 */
	public abstract Object getData();
}
