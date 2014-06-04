/**
 * 
 */
package net.phoenix.nlp.summarization;

import java.util.List;

import net.phoenix.nlp.pos.Term;
import net.phoenix.nlp.sentence.Sentence;

/**
 * 为计算summarization而对sentence做的封装.
 * 
 * @author lixf
 * 
 */
public class SentenceWrapper implements Sentence {
	private Sentence sentence;
	private double score;
	private List<Term> terms;

	public SentenceWrapper(Sentence sentence) {
		this.sentence = sentence;
		this.score = 0;
	}

	public double getScore() {
		return score;
	}

	public void setScore(double score) {
		this.score = score;
	}

	public List<Term> getTerms() {
		return terms;
	}

	public void setTerms(List<Term> terms) {
		this.terms = terms;
	}

	public Sentence getSentence() {
		return sentence;
	}

	@Override
	public int getStartOffset() {
		return sentence.getStartOffset();
	}

	@Override
	public int getEndOffset() {
		return sentence.getEndOffset();
	}

	@Override
	public Type getType() {
		return sentence.getType();
	}

	@Override
	public int length() {
		return sentence.length();
	}

	@Override
	public char charAt(int index) {
		return sentence.charAt(index);
	}

	@Override
	public CharSequence subSequence(int start, int end) {
		return sentence.subSequence(start, end);
	}

	@Override
	public String toString() {
		return sentence.toString();
	}

}
