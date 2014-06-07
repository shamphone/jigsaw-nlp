/**
 * 
 */
package net.phoenix.nlp.summarization;

import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

import net.phoenix.nlp.Term;
import net.phoenix.nlp.Tokenizer;
import net.phoenix.nlp.sentence.Detector;
import net.phoenix.nlp.sentence.Sentence;

import org.ejml.ops.CommonOps;
import org.ejml.simple.SimpleMatrix;

/**
 * 基于TextRank的summarization实现，算法描述参见论文： <TextRank: Bringing Order into Texts>,
 * Rada Mihalcea and Paul Tarau
 * 
 * @author lixf
 * 
 */
public class TextRankSummarization implements Summarization {
	private Tokenizer tokenizer;
	private Detector detector;
	private double threshold;
	private double damp; 

	/**
	 * 
	 * @param tokenizer
	 *            设置分词算法；
	 * @param detector
	 *            设置断句算法；
	 */
	public TextRankSummarization(Tokenizer tokenizer, Detector detector) {
		this.tokenizer = tokenizer;
		this.detector = detector;
		this.threshold = 0.001;
		this.damp = 0.8;
	}

	@Override
	public List<Sentence> summarize(Reader paragraph, int size)
			throws IOException {
		List<Sentence> sentences = this.toList(this.detector.detect(paragraph));
		SimpleMatrix similarity = this.buildSimilarityMatrix(sentences);
//		System.out.println("similarity========================");
//		System.out.println(similarity);
		this.normalize(similarity);
//		System.out.println("normalize========================");
//		System.out.println(similarity);
		SimpleMatrix weights = this.buildWeightVector(similarity);
//		System.out.println("weights========================");
//		System.out.println(weights);
		return this.rankByWeight(sentences, weights, size);
	}

	private List<Sentence> rankByWeight(List<Sentence> sentences,
			SimpleMatrix weights, int size) {
		if(size> sentences.size())
			return sentences;
		
		int index = 0;
		for(Sentence sentence : sentences){
			((SentenceWrapper)sentence).setScore(weights.get(index));
			this.scoreSentence((SentenceWrapper)sentence);
			index ++;
		}
		//排序，获得权重最高的size个句子；
		Collections.sort(sentences, new Comparator<Sentence>(){
			@Override
			public int compare(Sentence o1, Sentence o2) {
				double sub =  ((SentenceWrapper)o1).getScore() - ((SentenceWrapper)o2).getScore();
				if(sub > 0)
					return -1;
				else 
					return 1;
			}});
		//对权重最高的size个句子，恢复它们的顺序；
		List<Sentence> result = new ArrayList<Sentence>(sentences.subList(0, size));
		Collections.sort(result,  new Comparator<Sentence>(){

			@Override
			public int compare(Sentence o1, Sentence o2) {
				return o1.getStartOffset() - o2.getStartOffset();
			}});
		return result;
	}
	
	/**
	 * 可以继承这个方法来给句子打分；
	 * @param sentence
	 */
	protected void scoreSentence(SentenceWrapper sentence){
		
	}

	/**
	 * 用textRank算法计算权重矩阵。
	 * 
	 * @param matrix
	 * @return
	 */
	protected SimpleMatrix buildWeightVector(SimpleMatrix matrix) {
		SimpleMatrix vector = new SimpleMatrix(matrix.numCols(), 1);
		vector.set(1);
		
		SimpleMatrix vecDamp = new SimpleMatrix(matrix.numCols(), 1);
		vecDamp.set(1- this.damp);
		
		double diff = 1;
		while(diff > this.threshold){
			SimpleMatrix next = matrix.mult(vector);
			//next = (1-damp)+damp * next;
			next = vecDamp.plus(this.damp, next);			
			diff = next.minus(vector).normF();
			vector = next;
//			System.out.println("weight==========");
//			System.out.println(vector);
		}
		return vector;
	}

	/**
	 * 建立相似度矩阵
	 * 
	 * @param sentences
	 * @return
	 */
	protected SimpleMatrix buildSimilarityMatrix(List<Sentence> sentences) {
		for (Sentence sentence : sentences) {
			List<Term> terms = this.tokenizer.tokenize(sentence.toString());
			((SentenceWrapper) sentence).setTerms(terms);
		}
		SimpleMatrix matrix = new SimpleMatrix(sentences.size(),
				sentences.size());
		matrix.set(0);
		for (int i = 0; i < sentences.size(); i++)
			for (int j = i + 1; j < sentences.size(); j++) {
				// 相似度+1，消除0值；
				double similarity = this.similarity(sentences.get(i),
						sentences.get(j)) + 1;
				matrix.set(i, j, similarity);
				matrix.set(j, i, similarity);
			}
		return matrix;
	}

	/**
	 * 将matrix归一化处理。
	 * 
	 * @param matrix
	 */
	protected void normalize(SimpleMatrix matrix) {
		SimpleMatrix one = new SimpleMatrix(matrix.numCols(), matrix.numRows());
		one.set(1);
		SimpleMatrix sum = matrix.mult(one);
		CommonOps.elementDiv(matrix.getMatrix(), sum.getMatrix());
		CommonOps.transpose(matrix.getMatrix());
	}

	/**
	 * 计算句子之间的相似度
	 * 
	 * @param first
	 * @param second
	 * @return
	 */
	protected double similarity(Sentence sentence1, Sentence sentence2) {
		List<Term> tokens1 = ((SentenceWrapper) sentence1).getTerms();
		List<Term> tokens2 = ((SentenceWrapper) sentence2).getTerms();
		if (tokens1.size() == 0 || tokens2.size() == 0)
			return 0;
		int count = 0;
		for (Term first : tokens1)
			for (Term second : tokens2) {
				if (first.getName().equalsIgnoreCase(second.getName()))
					count++;
			}
		return count / Math.log(tokens1.size()) + Math.log(tokens2.size());
	}

	/**
	 * 将iterator转为List。
	 * 
	 * @param sentences
	 * @return
	 */
	private List<Sentence> toList(Iterator<Sentence> sentences) {
		List<Sentence> list = new ArrayList<Sentence>();
		while (sentences.hasNext())
			list.add(new SentenceWrapper(sentences.next()));
		return list;
	}

}
