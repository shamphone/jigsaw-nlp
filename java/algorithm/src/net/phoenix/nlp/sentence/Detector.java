/**
 * 
 */
package net.phoenix.nlp.sentence;

import java.io.IOException;
import java.io.Reader;
import java.util.Iterator;

/**
 * 中文句子断句。 输入文本片段，输出句子。
 * @author lixf
 *
 */
public interface Detector {
	
	/**
	 * 断句
	 * @param paragraph
	 * @return
	 */
	public Iterator<Sentence> detect(Reader paragraph)  throws IOException ;
}
