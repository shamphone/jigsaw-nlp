/**
 * 
 */
package net.phoenix.nlp.pos.corpus.file;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Vector;

import net.phoenix.nlp.corpus.Corpus;
import net.phoenix.nlp.corpus.CorpusRepository;
import net.phoenix.nlp.corpus.CorpusRepositoryWrapper;
import net.phoenix.nlp.corpus.FileCorpus;
import net.phoenix.nlp.corpus.RootCorpusRepository;

/**
 * POS所需要的语料库。这里提供硬编码的实现。如果使用springframework,可以通过bean配置文件来设置。
 * @author lixf
 *
 */
public class POSFileCorpusRepository extends  CorpusRepositoryWrapper{
	private List<FileCorpus> corpuses;
	/**
	 * 如果有多个语料库，使用这个来构建；
	 * @param parent
	 */
	public POSFileCorpusRepository(CorpusRepository parent) {
		super(parent);
		this.corpuses = new Vector<FileCorpus>();
	}
	/**
	 * 默认的构建；
	 */
	public POSFileCorpusRepository() {
		this(new RootCorpusRepository());
	}
	
	/**
	 * 从指定的目录中加载POS所需要的所有语料库
	 * @param folder
	 * @throws IOException
	 */
	public void load(File folder) throws IOException{
		FileCorpus corpus = new NatureFreqFileCorpus();
		corpus.load(new File(folder, "nature.freq.data"));
		this.corpuses.add(corpus);
		
		corpus = new NatureCooccurrenceFileCorpus();
		corpus.load(new File(folder, "nature.cooccurrence.data"));
		this.corpuses.add(corpus);
		
		corpus = new T2SFileCorpus();
		corpus.load(new File(folder,"t2s.data"));
		this.corpuses.add(corpus);
		
		corpus = new CharsetFileCorpus();
		corpus.load(new File(folder,"chars.data"));
		this.corpuses.add(corpus);
		
		CharDFAFileCorpus dfa = new CharDFAFileCorpus();
		dfa.load(new File(folder,"term.natures.data"));
		this.corpuses.add(dfa);
		
		corpus = new CompanyNameFreqFileCorpus(dfa);
		corpus.load(new File(folder,"company.location.data"));
		this.corpuses.add(corpus);
		
		corpus = new CompanyNameLengthFileCorpus();
		corpus.load(new File(folder,"company.length.data"));
		this.corpuses.add(corpus);
		
		corpus = new CooccurrenceFileCorpus();
		corpus.load(new File(folder,"term.cooccurrence.data"));
		this.corpuses.add(corpus);
		
		corpus = new PersonNameBoundaryFileCorpus(dfa);
		corpus.load(new File(folder,"person.boundary.data"));
		this.corpuses.add(corpus);
		
		corpus = new PersonNameFreqFileCorpus(dfa);	
		corpus.load(new File(folder,"person.freq.data"));
		this.corpuses.add(corpus);
	}
	
	@Override
	public <T extends Corpus> T getLocalCorpus(Class<T> clazz) {
		for(Corpus corpus : this.corpuses){
			if(clazz.isInstance(corpus))
				return clazz.cast(corpus);
		}
		return null;
	}

}
