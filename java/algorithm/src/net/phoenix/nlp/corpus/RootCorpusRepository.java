/**
 * 
 */
package net.phoenix.nlp.corpus;

/**
 * 提供一个空的repository作为语料库的根
 * @author lixf
 *
 */
public class RootCorpusRepository implements CorpusRepository{

	@Override
	public <T extends Corpus> T getCorpus(Class<T> clazz) {
		return null;
	}

}
