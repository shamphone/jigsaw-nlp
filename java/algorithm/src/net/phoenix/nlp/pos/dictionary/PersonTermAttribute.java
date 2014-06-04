package net.phoenix.nlp.pos.dictionary;

import net.phoenix.nlp.pos.TermAttribute;

/**
 * 人名标注pojo类
 * 
 * @author ansj
 * 
 */
public class PersonTermAttribute implements TermAttribute {

	public static String ATTRIBUTE = "person";
	// public int B = -1;//0 姓氏
	// public int C = -1;//1 双名的首字
	// public int D = -1;//2 双名的末字
	// public int E = -1;//3 单名
	// public int N = -1; //4任意字
	// public int L = -1;//11 人名的下文
	// public int M = -1;//12 两个中国人名之间的成分
	// public int m = -1;//44 可拆分的姓名
	// String[] parretn = {"BC", "BCD", "BCDE", "BCDEN"}
	// double[] factory = {"BC", "BCD", "BCDE", "BCDEN"}

	public static final PersonTermAttribute NULL = new PersonTermAttribute();
	/*
	 * 含3个数组： 第1个数组，含两个整数，由两个词组成的名字，作为名字第一个字符（姓）的概率，作为名字第二个字符（名）的概率；
	 * 第2个数组，含三个整数，由三个词组成的名字，作为名字第一个字符（姓）的概率，作为名字第二个字符（名）的概率，作为名字第三个字符（名）的概率；
	 * 第3个数组
	 * ，含四个整数，由四个词组成的名字，作为名字第一个字符（姓）的概率，作为名字第二个字符（名）的概率，作为名字第三个字符（名）的概率，作为名字第四个字符
	 * （名）的概率；
	 */
	private int[][] locationFrequency = null;

	//中间词的频率
	private int splitFrequency;
	// 12 作为名字前缀的频率
	private int leadingFrequency;
	// 作为名字后缀的频率
	private int followingFrequency;
	//作为名字边界的频率（前缀或者后缀）
	private int boundaryFrequency;

	// 是否有可能是名字的第一个字
	private boolean canBeFirstLetter;
	
	public PersonTermAttribute(){
		splitFrequency = 0;
		leadingFrequency= 0;
		followingFrequency = 0;
		boundaryFrequency = 0;
		canBeFirstLetter = false;		
		
	}

	/**
	 * 设置该Term作为名字边界的概率（名字前缀，后缀，中分？）。
	 * 
	 * @param index,边界类型， 11： 后缀； 12：前缀；44：中间词
	 * @param freq
	 */
	public void addBoundaryFrequency(int index, int freq) {
		switch (index) {
		case 11:
			this.followingFrequency += freq;
			this.boundaryFrequency += freq;
			break;
		case 12:
			this.followingFrequency += freq;
			this.leadingFrequency+= freq;
			this.boundaryFrequency += freq;
			break;
		case 44:
			this.splitFrequency += freq;
			this.boundaryFrequency += freq;
			break;
		}
	}

	/**
	 * 得道某一个位置的词频
	 * 
	 * @param length
	 *            名字的长度，
	 * @param loc
	 *            在该长度名字中的位置。
	 * @return 这个词在length长度的名字中，处于第loc个位置的概率
	 */
	public int getLocationFrequency(int length, int loc) {
		if (locationFrequency == null)
			return 0;
		if (length > 3)
			length = 3;
		if (loc > 4)
			loc = 4;
		try {
			return locationFrequency[length][loc];
		} catch (Exception ex) {
			System.out.println(length + "," + loc);
			return 0;
		}
	}

	/**
	 * 词频记录表
	 * 
	 * @param ints
	 */
	public void setLocationFrequency(int[][] ints) {
		for (int i = 0; i < ints.length; i++) {
			if (ints[i][0] > 0) {
				this.canBeFirstLetter = (true);
			}
		}
		locationFrequency = ints;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("begin=" + leadingFrequency);
		sb.append(",");
		sb.append("end=" + followingFrequency);
		sb.append(",");
		sb.append("split=" + splitFrequency);
		return sb.toString();
	}

	/**
	 * @return the begin
	 */
	public int getLeadingFrequency() {
		return leadingFrequency;
	}

	/**
	 * @return the end
	 */
	public int getFollowingFrequency() {
		return followingFrequency;
	}

	/**
	 * @return the allFreq
	 */
	public int getBoundaryFrequency() {
		return boundaryFrequency;
	}

	/**
	 * @param allFreq the allFreq to set
	 */
	public void setSumFrequency(int allFreq) {
		this.boundaryFrequency = allFreq;
	}

	/**
	 * @return the canBeFirstLetter
	 */
	public boolean canBeFirstLetter() {
		return canBeFirstLetter;
	}

	/**
	 * @param canBeFirstLetter the canBeFirstLetter to set
	 */
	public void setCanBeFirstLetter(boolean canBeFirstLetter) {
		this.canBeFirstLetter = canBeFirstLetter;
	}

}
