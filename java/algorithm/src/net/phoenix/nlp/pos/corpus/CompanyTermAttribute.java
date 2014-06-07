package net.phoenix.nlp.pos.corpus;

import net.phoenix.nlp.pos.TermAttribute;

/**
 * 
 * @author lixf
 *
 */
public class CompanyTermAttribute implements TermAttribute {
	public static String ATTRIBUTE = "company";
	
//	private static final double ALL = 52399292;
//	private static double BEGIN = 3186 / ALL;
//	private static double END = 2800 / ALL;

	// 前缀
	private int leading;
	// b首字词
	private int begin;
	// 中间部分
	private int middle;
	// 结尾部分
	private int end;
	// 后缀
	private int following;
	// 所有词频
	private int sum;

	// 前缀idf
	private double idfLeading;
	// b首字词idf
	private double idfBegin;
	// 中间部分idf
	private double idfMiddle;
	// 结尾部分idf
	private double idfEnd;
	// 后缀idf
	private double idfFollowing;
	// 所有词频idf
	private double idfSum;

	public CompanyTermAttribute(int p, int b, int m, int e, int s, int allFreq) {
		allFreq++;
		leading = p;
		this.begin = b;
		this.middle = m;
		this.end = e;
		this.following = s;
		this.sum = allFreq;
		double all = p + b + m + e + s + allFreq + 1;
		this.idfLeading = (p / all) * Math.log(1 - this.getLeading() / all);
		this.idfBegin = (b / all) * Math.log(1 - this.getBegin() / all);
		this.idfMiddle = (m / all) * Math.log(1 - this.getMiddle() / all);
		this.idfEnd = (e / all) * Math.log(1 - this.getEnd() / all);
		this.idfFollowing = (s / all) * Math.log(1 - this.getFollowing() / all);

	}

	@Override
	public String toString() {
		return "p" + ":" + getLeading() + "\t" + "b" + ":" + getBegin() + "\t" + "m" + ":" + getMiddle() + "\t" + "e" + ":" + getEnd() + "\t" + "s" + ":" + getFollowing();
	}

	/**
	 * @return the leading
	 */
	public int getLeading() {
		return leading;
	}

	/**
	 * @return the begin
	 */
	public int getBegin() {
		return begin;
	}

	/**
	 * @return the middle
	 */
	public int getMiddle() {
		return middle;
	}

	/**
	 * @return the end
	 */
	public int getEnd() {
		return end;
	}

	/**
	 * @return the following
	 */
	public int getFollowing() {
		return following;
	}

	/**
	 * @return the sum
	 */
	public int getSum() {
		return sum;
	}

	/**
	 * @return the idfLeading
	 */
	public double getIdfLeading() {
		return idfLeading;
	}

	/**
	 * @return the idfBegin
	 */
	public double getIdfBegin() {
		return idfBegin;
	}

	/**
	 * @return the idfMiddle
	 */
	public double getIdfMiddle() {
		return idfMiddle;
	}

	/**
	 * @return the idfEnd
	 */
	public double getIdfEnd() {
		return idfEnd;
	}

	/**
	 * @return the idfFollowing
	 */
	public double getIdfFollowing() {
		return idfFollowing;
	}

	/**
	 * @return the idfSum
	 */
	public double getIdfSum() {
		return idfSum;
	}

}
