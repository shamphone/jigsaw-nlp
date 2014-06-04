/**
 * 
 */
package net.phoenix.nlp.pos.corpus;

/**
 * words.dic语料库训练
 * 这个实现类非线程安全。不要跨线程使用。 
 * @author lixf
 *
 */
public class WordsTrainer extends AbstractTrainer{

	
	@Override
	public void onFileBegin() {
	}

	@Override
	public void onFileEnd() {
		// TODO Auto-generated method stub
		super.onFileEnd();
	}

	@Override
	public void onSentenceBegin() {
		// TODO Auto-generated method stub
		super.onSentenceBegin();
	}

	@Override
	public void onPOS(String term, String nature) {
		// TODO Auto-generated method stub
		super.onPOS(term, nature);
	}

	@Override
	public void onSentenceEnd() {
		// TODO Auto-generated method stub
		super.onSentenceEnd();
	}

}
