/**
 * 
 */
package net.phoenix.nlp.pos;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.phoenix.nlp.Tokenizer;
import net.phoenix.nlp.pos.corpus.file.POSFileCorpusRepository;
import net.phoenix.nlp.pos.npath.CooccurrenceNPathGenerator;
import net.phoenix.nlp.pos.recognitor.AsianNameRecognitor;
import net.phoenix.nlp.pos.recognitor.CompanyRecognitor;
import net.phoenix.nlp.pos.recognitor.DateTimeRecognitor;
import net.phoenix.nlp.pos.recognitor.ForeignNameRecognitor;
import net.phoenix.nlp.pos.recognitor.NatureRecognitor;
import net.phoenix.nlp.pos.recognitor.NumberRecognitor;
import net.phoenix.nlp.pos.scorer.SimplePathScorer;
import net.phoenix.nlp.pos.segmenter.CharTreeSegmenter;


/**、
 * 标准的词性和分词工具。
 * 处理流程如下：
 * 1. 粗分词，切分成已登录词，
 * 2. 根据粗分词结果，形成 N条最短路径；
 * 2. 对N条最短路径中的进行识别： 1. 数字 2. 人名 3. 地名 4. 机构名 5. 时间/日期。每个识别可能会继续产生更多路径；
 * 3. 从识别最终结果中选取最佳的标注路径
 * 
 * @author lixf
 * 
 */
public class StandardTokenizer implements Tokenizer {
	private List<Recognitor> recognitors;
	private NPathGenerator npath;
	private Segmenter segmenter;
	private PathScorer scorer;
	

	/**
	 * 分词的类
	 * @throws IOException 
	 */
	public StandardTokenizer(File directory) throws IOException {
		POSFileCorpusRepository library = new POSFileCorpusRepository();
		library.load(directory);	
		this.scorer = new SimplePathScorer();
		this.segmenter = new CharTreeSegmenter(library);
		this.npath = new CooccurrenceNPathGenerator(library);
		this.recognitors = new ArrayList<Recognitor>();
		this.recognitors.add(new NumberRecognitor(library));
		this.recognitors.add(new AsianNameRecognitor(library));
		this.recognitors.add(new CompanyRecognitor(library));
		this.recognitors.add(new ForeignNameRecognitor(library));		
		this.recognitors.add(new DateTimeRecognitor(library));
		this.recognitors.add(new NatureRecognitor(library));
	}

	/**
	 * 分句；按照，。和？分隔；
	 * 
	 * @param sentence
	 * @return
	 */
	@Override
	public List<POSTerm> tokenize(String sentence) {
		//long startTime = System.currentTimeMillis();
		Pattern pattern = Pattern.compile("[，。？！　 ]");
		Matcher matcher = pattern.matcher(sentence);
		int start = 0;
		List<POSTerm> result = new ArrayList<POSTerm>();
		while (matcher.find(start)) {
			result.addAll(this.parse(sentence.substring(start, matcher.start() + 1)));
			start = matcher.start() + 1;
		}
		if (start < sentence.length() - 1) {
			result.addAll(this.parse(sentence.substring(start)));
		}
		//log.info("[" + (System.currentTimeMillis() - startTime) + "] :" + result);
		return result;
	}

	public List<POSTerm> parse(String temp) {
		if(temp.trim().length()==0)
			return new ArrayList<POSTerm>();
		// 初分词，生成 graph;
		TermGraph graph = this.segmenter.segment(temp.trim());
		List<TermPath> pathes = this.npath.process(graph);
		List<TermPath> generated = new ArrayList<TermPath>();
		for (Recognitor recognitor : this.recognitors) {
			generated.clear();
			for (TermPath path : pathes) {
				generated.addAll(recognitor.process(path.toTermGraph()));
			}
			pathes.clear();
			pathes.addAll(generated);

		}

		TermPath shortest = null;
		double score = Double.MAX_VALUE;
		for (TermPath path : pathes) {
			double currentScore = this.scorer.score(path);
			//log.info("candidate-[" + currentScore + "] :" + path);
			if (shortest == null || score > currentScore) {
				shortest = path;
				score = currentScore;

			}
		}
		//log.info("[" + score + "] :" + shortest);
		List<POSTerm> terms = new ArrayList<POSTerm>(shortest.getVertextList());
		terms.remove(terms.size() - 1);
		terms.remove(0);
		return terms;
	}

	public void addRecognitors(Recognitor recognitor) {
		this.recognitors.add(recognitor);
	}

	public void setSegmenter(Segmenter segmenter) {
		this.segmenter = segmenter;
	}

	public void setPathScorer(PathScorer scorer) {
		this.scorer = scorer;
	}

	public void setNPathGenerator(NPathGenerator generator) {
		this.npath = generator;
	}

}
