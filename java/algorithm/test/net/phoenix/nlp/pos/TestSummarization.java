/**
 * 
 */
package net.phoenix.nlp.pos;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;

import net.phoenix.nlp.Tokenizer;
import net.phoenix.nlp.sentence.Detector;
import net.phoenix.nlp.sentence.Sentence;
import net.phoenix.nlp.sentence.SimpleDetector;
import net.phoenix.nlp.summarization.SentenceWrapper;
import net.phoenix.nlp.summarization.Summarization;
import net.phoenix.nlp.summarization.TextRankSummarization;

import org.junit.Test;

/**
 * @author lixf
 *
 */
public class TestSummarization {
	@Test
	public void testText() throws IOException {
		File folder = new File("D:\\github\\jigsaw-nlp\\data\\pos");
		Tokenizer tokenizer = new StandardTokenizer(folder);
		Detector detector = new SimpleDetector();
		Reader paragraph = new FileReader("D:\\github\\jigsaw-nlp\\java\\algorithm\\test\\news2.txt");
		Summarization summarization = new TextRankSummarization(tokenizer, detector);
		for(Sentence sentence : summarization.summarize(paragraph, 4)){
			double score = ((SentenceWrapper)sentence).getScore();
			System.out.println("["+ score+"]"+ sentence);
		}
	}
}
