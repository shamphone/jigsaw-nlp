/**
 * 
 */
package net.phoenix.nlp.pos;

import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.util.Iterator;

import net.phoenix.nlp.sentence.Detector;
import net.phoenix.nlp.sentence.Sentence;
import net.phoenix.nlp.sentence.SimpleDetector;

import org.junit.Test;

/**
 * @author lixf
 *
 */
public class TestSentence {
	@Test
	public void testText() throws IOException {
		Detector detector = new SimpleDetector();
		Reader paragraph = new FileReader("D:\\github\\jigsaw-nlp\\java\\algorithm\\test\\sentences.txt");
		Iterator<Sentence> sentences = detector.detect(paragraph);
		while(sentences.hasNext()){
			Sentence sentence = sentences.next();
			System.out.println(sentence.getStartOffset()+"-"+sentence.getEndOffset()+":"+ sentence.toString());
		}
	}
}
