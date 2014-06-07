/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package net.phoenix.nlp.pos.corpus.file;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import net.phoenix.nlp.Nature;
import net.phoenix.nlp.corpus.FileCorpus;
import net.phoenix.nlp.pos.BasicCharNode;
import net.phoenix.nlp.pos.BasicTermNatures;
import net.phoenix.nlp.pos.CharNode;
import net.phoenix.nlp.pos.TermNatures;
import net.phoenix.nlp.pos.CharNode.State;
import net.phoenix.nlp.pos.corpus.CharDFACorpus;

/**
 *  字符成词的状态自动机。
 *  GB2312收录了6763个汉字
 *  GBK收录了21003个汉字
 *  GB18030-2000收录了27533个汉字
 *  GB18030-2005收录了70244个汉字
 *  Unicode 5.0收录了70217个汉字
 * @author <a href="shamphone@gmail.com">Li XiongFeng</a>
 * @date 2013-2-7
 * @version 1.0.0
 */
public class CharDFAFileCorpus extends FileCorpus implements CharDFACorpus{
	private CharNode[] words;
	private static final int CHAR_COUNT = 70000; //汉字常用字UTF-8的最高编码；
	
	public CharDFAFileCorpus() {
		//super(folder, "term.natures.data");
	}


	/**
	 * 对于base,check,natrue,status的加载 0.代表这个字不在词典中 1.继续 2.是个词但是还可以继续 3.停止已经是个词了
	 * 
	 * @throws IOException
	 */
	@Override
	public void load(File DFAFile) throws IOException {
		BufferedReader reader = null;
		this.words = new CharNode[CHAR_COUNT];
		try {
			reader = new BufferedReader(new FileReader(DFAFile));
			this.readDFAFile(reader);
		} finally {
			if (reader != null)
				reader.close();
		}

	}


	/**
	 * 0: num, 编号，为收录的每个字/词建立编号。编号不是连续的，和UTF-8有关，需进一步研究；
	 * 1：words,词，可以是数字、英文字符、中文字符、符号 
	 * 2： base 
	 * 3: check; 
	 * 4: status,状态，应该是类型，1.为不成词.处于过度阶段. 2.成词，也可能是词语的一部分. 3.词语结束；4：英文字符；5：数字 
	 * 5: 词性，格式为：{词性1=频率,词性2=频率,....}
	 */
	private void readDFAFile(BufferedReader reader) throws IOException {
		String temp;
		while ((temp = reader.readLine()) != null) {
			String[] strs = temp.split("	");
			// int index = Integer.parseInt(strs[0]);
			String word = strs[1].trim();
			// int base = Integer.parseInt(strs[2]);
			// int check = Integer.parseInt(strs[3]);
			int status = Byte.parseByte(strs[4]);
			String nature = strs[5].trim();
			TermNatures natures = null;
			if (nature != null && nature.length() > 0 && !nature.equalsIgnoreCase("null"))
				natures = this.parse(nature);

			BasicCharNode node = this.createCharNode(word, State.valueOf(status));
			node.setTermNatures(natures);
		}

	}

	public TermNatures parse(String natureStr) {
		natureStr = natureStr.substring(1, natureStr.length() - 1);
		String[] split = natureStr.split(",");
		String[] strs = null;
		Integer frequency = null;
		TermNatures natures = new BasicTermNatures();
		for (int i = 0; i < split.length; i++) {
			strs = split[i].split("=");
			frequency = Integer.parseInt(strs[1]);
			if (strs[0].trim().length() > 0)
				natures.addNature(Nature.valueOf(strs[0].trim()), frequency);
		}
		return natures;
	}

	private BasicCharNode createCharNode(String word, State status) {
		if (word.length() == 0 || word == null)
			throw new IllegalArgumentException("word should not be empty.");
		if (word.length() == 1) {
			char ch = word.charAt(0);
			CharNode node = this.words[ch];
			if (node == null) {
				node = new BasicCharNode(ch, status);
				this.words[ch] = node;
			}
			return (BasicCharNode) node;
		} else {
			BasicCharNode parent = this.createCharNode(word.substring(0, word.length() - 1), State.PART_OF_WORD);
			char last = word.charAt(word.length() - 1);
			CharNode node = parent.get(last);
			if (node == null) {
				node = new BasicCharNode(last, status);
				parent.put(last, node);
			}
			return (BasicCharNode) node;
		}

	}

	/* (non-Javadoc)
	 * @see net.phoenix.nlp.pos.corpus.file.CharDFACorpus#addNewWord(java.lang.String, java.lang.String)
	 */
	@Override
	public CharNode addNewWord(String word, String natureName) {
		CharNode node = this.createCharNode(word, State.WORD_AND_PART);
		if (node == null)
			throw new IllegalArgumentException("unknown word:" + word);
		node.getTermNatures().addNature(Nature.valueOf(natureName));
		return node;
	}

	/* (non-Javadoc)
	 * @see net.phoenix.nlp.pos.corpus.file.CharDFACorpus#getNode(char)
	 */
	@Override
	public CharNode getNode(char ch) {
		return this.words[ch];
	}

	/* (non-Javadoc)
	 * @see net.phoenix.nlp.pos.corpus.file.CharDFACorpus#getNode(java.lang.String)
	 */
	@Override
	public CharNode getNode(String word) {
		if (word.length() == 0 || word == null)
			throw new IllegalArgumentException("word should not be empty.");
		if (word.length() == 1) {
			char ch = word.charAt(0);
			return this.words[ch];
		} else {
			CharNode parent = this.getNode(word.substring(0, word.length() - 1));
			if (parent == null)
				return null;
			char last = word.charAt(word.length() - 1);
			return parent.get(last);
		}
	}

}
