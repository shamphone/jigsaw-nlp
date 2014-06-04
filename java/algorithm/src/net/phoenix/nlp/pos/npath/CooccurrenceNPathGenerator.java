/**
 * 
 */
package net.phoenix.nlp.pos.npath;

import java.util.ArrayList;
import java.util.List;

import net.phoenix.nlp.pos.Dictionary;
import net.phoenix.nlp.pos.Term;
import net.phoenix.nlp.pos.TermEdge;
import net.phoenix.nlp.pos.TermGraph;
import net.phoenix.nlp.pos.dictionary.CooccurrenceDictionary;

/**
 * @author lixf
 *
 */
public class CooccurrenceNPathGenerator extends AbstractNPathGenerator{
	// 平滑参数
	private static final double dSmoothingPara = 0.1;
	// 一个参数
	private static final int MAX_FREQUENCE = 2079997;// 7528283+329805;
	// ﻿Two linked Words frequency
	private static final double dTemp = (double) 1 / MAX_FREQUENCE;
	/**
	 * 单词共现字典
	 * 
	 * @param cooccurrence
	 */
	private CooccurrenceDictionary cooccurrence;	
	public CooccurrenceNPathGenerator(Dictionary dictionary) {
		super(dictionary);
		this.cooccurrence = this.dictionary.getDictionary(CooccurrenceDictionary.class);
	}
	

	/**
	 * 
	 * @param gp
	 */
	@Override
	protected void scoreEdges(TermGraph gp) {	
		this.scoreOutgoingEdges(gp, gp.getStartVertex(), new ArrayList<Term>());		
	}
	/**
	 * 为term所有外向边打分。使用Viterbi算法
	 * @param gp
	 * @param term
	 * @param processed
	 */
	private void scoreOutgoingEdges(TermGraph gp, Term term, List<Term> processed){
		if(processed.contains(term))
			return;
		double score = Double.NaN;
		//从进入的链接中取最小值作为 起始值； 
		for(TermEdge edge : gp.incomingEdgesOf(term)){
			if(Double.isNaN(score) || gp.getEdgeWeight(edge)< score)
				score = gp.getEdgeWeight(edge);
		}
		if(Double.isNaN(score))
			score = 0;
		processed.add(term);
		for(TermEdge edge : gp.outgoingEdgesOf(term)){
			Term next = gp.getEdgeTarget(edge);
			double current =  scoreCooccurrence(term, next)+score;
			double old = gp.getEdgeWeight(edge);
			if(Double.isNaN(old) || current< old)
				gp.setEdgeWeight(edge, current);
		}
		
		for(TermEdge edge : gp.outgoingEdgesOf(term)){
			Term next = gp.getEdgeTarget(edge);
			this.scoreOutgoingEdges(gp, next, processed);
		}
		
	}
	/**
	 * 从一个词的词性到另一个词的词性的分数
	 * 
	 * @param form
	 *            前面的词
	 * @param to
	 *            后面的词
	 * @return 这两个词之间连接的分数
	 */
	public double scoreCooccurrence(Term from, Term to) {
		double frequency = from.getTermNatures().getSumFrequency() + 1;
		int nTwoWordsFreq = this.cooccurrence.getCooccurrenceFrequency(from.getName(), to.getName());
		double value = -Math.log(dSmoothingPara * frequency / (MAX_FREQUENCE + 80000) + (1 - dSmoothingPara) * ((1 - dTemp) * nTwoWordsFreq / frequency + dTemp));

		if (value < 0)
			value += frequency;

		if (value < 0) {
			value += frequency;
		}
		return value;

	}
}
