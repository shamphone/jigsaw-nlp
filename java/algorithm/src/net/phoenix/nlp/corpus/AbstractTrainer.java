/**
 * 
 */
package net.phoenix.nlp.corpus;


/**
 * 
 * 提供一个空的实现，实现者仅需实现必要方法即可。 
 * @author lixf
 *
 */
public abstract class AbstractTrainer implements Trainer{

	@Override
	public void onFileBegin() {
	
	}

	@Override
	public void onFileEnd() {
	
	}

	@Override
	public void onSentenceBegin() {
	
	}

	@Override
	public void onPOS(String term, String nature) {
	
	}

	@Override
	public void onSentenceEnd() {
	}

}
