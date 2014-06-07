/**
 * 
 */
package net.phoenix.nlp.corpus;

/**
 * 语料库管理器，用来注册和获取所有的语料库；
 * @author lixf
 *
 */
public interface CorpusRepository {
	/**
	 * 根据字典类别获取所需要的字典；
	 * @param clazz
	 * @return
	 */
	public  <T extends Corpus> T getCorpus(Class<T> clazz);
}
