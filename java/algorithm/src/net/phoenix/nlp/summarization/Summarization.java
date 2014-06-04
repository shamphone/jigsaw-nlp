/**
 * 
 */
package net.phoenix.nlp.summarization;

import java.io.IOException;
import java.io.Reader;
import java.util.List;

import net.phoenix.nlp.sentence.Sentence;

/**
 * 生成文档摘要
 * @author lixf
 * 
 */
public interface Summarization {
	/**
	 * 生成 summarization
	 * 
	 * @param paragraph  原文
	 * @param size    摘要中句子的个数
	 * @return 摘要句子
	 * @throws IOException 读取源的错误
	 */
	public List<Sentence> summarize(Reader paragraph, int size)
			throws IOException;
}
