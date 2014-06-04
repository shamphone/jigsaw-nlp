/**
 * 
 */
package net.phoenix.nlp.sentence;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;

/**
 * 简单断句算法：
 * 1. 以句号、感叹号、问号结尾的句子；
 * 2. 处理：“开始和”结尾的对话。
 * TODO：
 * 处理省略号。
 * @author lixf
 * 
 */
public class SimpleDetector implements Detector {
	private char[] none = { '\r', '\n' }; // 无类型
	private char[] declarative = { '。', '。' }; // 陈述句
	private char[] interrogative = { '？', '?', '？' }; // 疑问句
	private char[] imperative = {};
	private char[] exclamatory = { '!', '！', '！' }; // 感叹句;
	private char[] initialQuote = {'"', '“','＂'};  
	private char[] finalQuote = {'"', '＂','”'};  
	private char[] colon = {':','：',':'};  
	

	public SimpleDetector() {

	}

	@Override
	public Iterator<Sentence> detect(Reader source) throws IOException {
		BufferedReader reader = new BufferedReader(source);
		return new ResultIterator(reader);

	}

	/**
	 * 解析句子
	 * @param offset 当前句子在整个文本中的偏移
	 * @param paragraph 当前要处理的段落
	 * @return
	 */
	private List<Sentence> parse(int offset, String paragraph) {
		List<Sentence> result = new ArrayList<Sentence>();
		if (paragraph == null || paragraph.trim().length() == 0)
			return result;
		int start = 0;
		int end = 0;
		boolean inQuote = false;
		for (int i = 0; i < paragraph.length(); i++) {
			char ch = paragraph.charAt(i);
			end++;
			if (in(ch, none)) {
				SimpleSentence sentence = new SimpleSentence(paragraph, offset,
						start, end, Sentence.Type.None);
				result.add(sentence);
				start = end;
			} else if (in(ch, declarative)) {
				SimpleSentence sentence = new SimpleSentence(paragraph, offset,
						start, end, Sentence.Type.Declarative);
				result.add(sentence);
				start = end;
			} else if (in(ch, interrogative)) {
				SimpleSentence sentence = new SimpleSentence(paragraph, offset,
						start, end, Sentence.Type.Interrogative);
				result.add(sentence);
				start = end;
			} else if (in(ch, exclamatory)) {
				SimpleSentence sentence = new SimpleSentence(paragraph, offset,
						start, end, Sentence.Type.Exclamatory);
				result.add(sentence);
				start = end;
			} else if (in(ch, imperative)) {
				SimpleSentence sentence = new SimpleSentence(paragraph, offset,
						start, end, Sentence.Type.Exclamatory);
				result.add(sentence);
				start = end;
			} else if(in(ch, colon) &&  i < (paragraph.length()-2)){ 
				//处理   他说：“俺们那嘎都是活雷锋”的:"部分， 断句'他说’和‘俺们那嘎都是活雷锋’；
				char next = paragraph.charAt(i+1);
				if(in(next, initialQuote)) {
					//将 他说 分离出来
					SimpleSentence sentence = new SimpleSentence(paragraph, offset,
							start, end, Sentence.Type.Exclamatory);
					result.add(sentence);
					start = end +1;
					i++;
					end++;
					inQuote = true;
				}				
			} else if(in(ch, finalQuote) && inQuote){
				inQuote = false;
				if(end-start ==1) //如果是 。“ 的情况，到。已经成句，忽略掉”。
					start++;				
			}
		}
		if (start < paragraph.length()-1) {
			SimpleSentence sentence = new SimpleSentence(paragraph, offset,
					start, paragraph.length(), Sentence.Type.None);
			result.add(sentence);
		}
		return result;
	}

	/**
	 * 判断ch是否在数组 array中。由于这里的数组都很小，所以直接比较，不使用Array.binarySearch。
	 * 
	 * @param target
	 * @param array
	 * @return
	 */
	private boolean in(char target, char[] array) {
		for (Character ch : array) {
			if (ch.equals(target))
				return true;
		}
		return false;
	}

	/**
	 * 将分析结果的List转成Iterator;
	 * @author lixf
	 *
	 */
	class ResultIterator implements Iterator<Sentence> {
		private Iterator<Sentence> buffer;
		private BufferedReader reader;
		private int offset;

		public ResultIterator(BufferedReader reader) {
			this.reader = reader;
			this.offset = 0;
		}

		@Override
		public boolean hasNext() {
			if (buffer == null || !buffer.hasNext())
				readMore();
			if (buffer == null)
				return false;
			return buffer.hasNext();
		}

		@Override
		public Sentence next() {
			if (buffer == null || !buffer.hasNext())
				readMore();
			if (buffer == null)
				throw new NoSuchElementException();
			return buffer.next();
		}

		private void readMore() {
			try {
				String line = reader.readLine();
				while(line!=null && line.trim().length()== 0){
					offset += line.length() + 1; // 考虑到分行符；
					line = reader.readLine();
				}
				if (line != null) {
					this.buffer = parse(offset, line).iterator();
					offset += line.length() + 1; // 考虑到分行符；
				} else
					this.buffer = null;
			} catch (IOException e) {
				throw new NoSuchElementException();
			}

		}

		@Override
		public void remove() {
			throw new UnsupportedOperationException("remove");

		}
	};

}
