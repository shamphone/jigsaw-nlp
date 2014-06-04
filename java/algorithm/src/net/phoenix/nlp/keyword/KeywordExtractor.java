/**
 * 
 */
package net.phoenix.nlp.keyword;

import java.io.IOException;
import java.io.Reader;
import java.util.List;

/**
 * @author lixf
 * 
 */
public interface KeywordExtractor {
	public List<String> extract(Reader reader, int count) throws IOException;
}
