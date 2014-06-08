/**
 * 
 */
package net.phoenix.nlp.pos;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;

import net.phoenix.nlp.Tokenizer;
import net.phoenix.nlp.keyword.KeywordExtractor;
import net.phoenix.nlp.keyword.TFIDFKeywordExtractor;
import net.phoenix.nlp.pos.chmm.HMMTokenizer;
import net.phoenix.nlp.sentence.Detector;
import net.phoenix.nlp.sentence.SimpleDetector;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

/**
 * @author lixf
 *
 */
@RunWith(JUnit4.class)
public class TestKeywords  {
	@Test
	public void testText() throws IOException {
		File folder = new File("D:\\github\\jigsaw-nlp\\data\\pos");
		Tokenizer tokenizer = new HMMTokenizer(folder);
		Detector detector = new SimpleDetector();
		Reader paragraph = new FileReader("D:\\github\\jigsaw-nlp\\java\\algorithm\\test\\news2.txt");
		KeywordExtractor extractor = new TFIDFKeywordExtractor(tokenizer, detector);
		System.out.println();
		for(String  keyword: extractor.extract(paragraph, 8)){
			System.out.print(keyword+" ");
		}
		System.out.println();
	}
}
