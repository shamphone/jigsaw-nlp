/**
 * 
 */
package net.phoenix.nlp.corpus;

/**
 * 级联语料库，用于支持多级语料库设置
 * @author lixf
 *
 */
public abstract class CorpusRepositoryWrapper implements CorpusRepository{
	private CorpusRepository parent;
	public CorpusRepositoryWrapper(CorpusRepository parent){
		this.parent = parent;
	}

	@Override
	public <T extends Corpus> T getCorpus(Class<T> clazz) {
		T corpus = this.getLocalCorpus(clazz);
		if(corpus == null)
			corpus =this.parent.getCorpus(clazz);
		return corpus;
	}
	
	public abstract <T extends Corpus> T getLocalCorpus(Class<T> clazz);

}
