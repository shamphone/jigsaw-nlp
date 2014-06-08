/**
 * 
 */
package net.phoenix.nlp.pos.chmm.term;

import net.phoenix.nlp.Nature;
import net.phoenix.nlp.pos.chmm.POSTerm;
import net.phoenix.nlp.pos.chmm.TermNatures;

/**
 * @author lixf
 * 
 */
public class DefaultTerm  implements POSTerm {
	
	//private static final long serialVersionUID = 7598424649248112742L;
	// public static final Term NULL = new DefaultTerm("NULL", 0,
	// TermNatures.NULL);
	// 当前词
	private String name;
	// 当前词的起始位置
	// private int offe;
	// 词性列表
	private TermNatures termNatures = null;

	//private Map<String, Double> scores;

	// 本身这个term的词性.需要在词性识别之后才会有值,默认是空
	private Nature nature;
	
	private int start; //在源字符串中的起始位置；
	private int end; //在源字符串中的结束位置；


	public DefaultTerm(String name, int start, int end,  TermNatures termNatures) {
		this.name = name;
		this.start = start;
		this.end = end;
		this.termNatures = termNatures;

	}
	
	public DefaultTerm(char name, int start,  TermNatures termNatures) {
		this.name = String.valueOf(name);
		this.start = start;
		this.end = start + 1;
		this.termNatures = termNatures;

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see net.phoenix.nlp.pos.domain.Term#getName()
	 */
	@Override
	public String getName() {
		return name;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see net.phoenix.nlp.pos.domain.Term#setName(java.lang.String)
	 */
	public void setName(String name) {
		this.name = name;
	}



	/*
	 * (non-Javadoc)
	 * 
	 * @see net.phoenix.nlp.pos.domain.Term#getTermNatures()
	 */
	@Override
	public TermNatures getTermNatures() {
		return termNatures;
	}



	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * net.phoenix.nlp.pos.domain.Term#setNature(net.phoenix.nlp.pos.domain.
	 * Nature)
	 */
	@Override
	public void setNature(Nature nature) {
		this.nature = nature;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see net.phoenix.nlp.pos.domain.Term#getNatrue()
	 */
	@Override
	public Nature getNature() {
		return nature;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see net.phoenix.nlp.pos.domain.Term#toString()
	 */
	@Override
	public String toString() {
		if (nature != null && !"null".equals(nature.toString())) {
			return this.name + "/" + nature.toString();
		} else {
			return this.name;
		}
	}


	protected void setTermNatures(TermNatures en) {
		this.termNatures = en;
	}

	@Override
	public int getStartOffset() {
		return this.start;
	}

	@Override
	public int getEndOffset() {
		return this.end;
	}

	@Override
	public void setNature(String nature) {
		this.setNature(Nature.valueOf(nature));
		
	}

}
