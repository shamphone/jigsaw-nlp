/**
 * 
 */
package net.phoenix.nlp.pos.corpus;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * @author lixf
 * 
 */
public class TrainingDriver {
	private static Map<String, Character> escapes = new HashMap<String, Character>();

	private static final Log log = LogFactory.getLog(TrainingDriver.class);
	private List<Trainer> listeners;
	private int count;

	public TrainingDriver() {
		this.listeners = new ArrayList<Trainer>();
		escapes.put("&nbsp;", ' ');
		escapes.put("&euro;", '€');
		escapes.put("&quot;", '"');
		escapes.put("&amp;", '&');
		escapes.put("&lt;", '<');
		escapes.put("&gt;", '>');
		this.count = 0;
	}

	/**
	 * 注册一个侦听器。
	 * 
	 * @param listener
	 */
	public void registerListener(Trainer listener) {
		this.listeners.add(listener);
	}

	/**
	 * 处理给定目录下所有的文件。
	 * 
	 * @param folder
	 * @throws IOException
	 */
	public void processFolder(File folder) throws IOException {

		File[] files = folder.listFiles(new FilenameFilter() {

			@Override
			public boolean accept(File dir, String name) {
				return name.toLowerCase().endsWith(".txt");
			}
		});
		for (File file : files)
			this.processFile(file);

		log.info(this.count + " sentences parsed. ");

	}

	/**
	 * 处理给定文件
	 * 
	 * @param folder
	 * @throws IOException
	 */
	public void processFile(File file) throws IOException {
		log.info("process file " + file.getPath() + " ...");
		this.fireOnFileBegin();
		BufferedReader reader = new BufferedReader(new InputStreamReader(
				new FileInputStream(file), "UTF-8"));
		String line = reader.readLine();
		int count = 0;
		while (line != null) {
			count++;
			line = line.trim();
			if (line.length() > 7) {
				this.processPOSSentence(count, line);
			}
			line = reader.readLine();
		}
		reader.close();
		this.fireOnFileEnd();
	}

	/**
	 * 处理带POS表标记的句子
	 * 
	 * @param num
	 * @param sentence
	 * @throws IOException
	 */
	public void processPOSSentence(int num, String sentence) throws IOException {
		this.count++;
		this.fireOnSentenceBegin();
		String[] items = sentence.split("\\s");
		for (String item : items) {
			if (item.length() == 0)
				continue;
			boolean ok = false;
			if (item.length() > 2) {
				int pos = item.indexOf('/');
				if (pos > 0) {
					String word = item.substring(0, pos);
					String nature = item.substring(pos + 1);
					this.fireOnPOS(word, nature);
					ok = true;
				}
			}
			if (!ok) {
				log.info("Error in processing '" + item + "' on sentence ["
						+ num + "] '" + sentence + "'");
			}
		}
		this.fireOnSentenceEnd();
	}

	/**
	 * 在句子处理之前触发
	 */
	private void fireOnSentenceBegin() {
		for (Trainer listener : this.listeners) {
			listener.onSentenceBegin();
		}

	}

	/**
	 * 在句子处理完成之后触发
	 */
	private void fireOnSentenceEnd() {
		for (Trainer listener : this.listeners) {
			listener.onSentenceEnd();
		}

	}

	/**
	 * 在句子处理之前触发
	 */
	private void fireOnFileBegin() {
		for (Trainer listener : this.listeners) {
			listener.onFileBegin();
		}

	}

	/**
	 * 在句子处理完成之后触发
	 */
	private void fireOnFileEnd() {
		for (Trainer listener : this.listeners) {
			listener.onFileEnd();
		}

	}

	/**
	 * 触发事件
	 * 
	 * @param word
	 * @param nature
	 */
	private void fireOnPOS(String word, String nature) {
		if (escapes.containsKey(word))
			word = String.valueOf(escapes.get(word));
		for (Trainer listener : this.listeners) {
			listener.onPOS(word, nature);
		}
	}

	public static void main(String[] args) throws Exception {
		File corpusFolder = new File("D:\\google\\jigsaw-nlp\\corpus\\cncorpus");
		TrainingDriver trainer = new TrainingDriver();
		trainer.processFolder(corpusFolder);
	}
}
