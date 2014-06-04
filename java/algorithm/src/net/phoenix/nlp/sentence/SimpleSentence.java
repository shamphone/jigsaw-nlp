/**
 * 
 */
package net.phoenix.nlp.sentence;

/**
 * Sentence的一个简单实现；
 * @author lixf
 * 
 */
public class SimpleSentence implements Sentence {
	//该句子所在的原字符串；
	private String source;
	//该句子在source中的起始位置；
	private int start;
	//该句子在source中的结束位置；
	private int end;
	//句子类型；
	private Type type;
	//source在全部的字符串中的偏移；
	private int offset;
	public SimpleSentence(String source, int offset, int start, int end, Type type) {
		this.source = source;
		this.start = start;
		this.end = end;
		this.type = type;
		this.offset = offset;
	}

	@Override
	public int length() {
		return this.end - this.start;
	}

	@Override
	public char charAt(int index) {
		return this.source.charAt(index - this.start- this.offset);
	}

	@Override
	public CharSequence subSequence(int start, int end) {
		return this.source.subSequence(start + this.start, end + this.end);
	}

	@Override
	public int getStartOffset() {
		return this.start + this.offset;
	}

	@Override
	public int getEndOffset() {
		return this.end + this.offset;
	}

	@Override
	public Type getType() {
		return this.type;
	}
	
	public String toString(){
		return this.source.substring(this.start, this.end);
	}

}
