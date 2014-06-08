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
package net.phoenix.nlp.pos.chmm.recognitor;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import net.phoenix.nlp.Nature;
import net.phoenix.nlp.corpus.CorpusRepository;
import net.phoenix.nlp.pos.chmm.POSTerm;
import net.phoenix.nlp.pos.chmm.TermEdge;
import net.phoenix.nlp.pos.chmm.TermGraph;
import net.phoenix.nlp.pos.chmm.TermPath;
import net.phoenix.nlp.pos.chmm.corpus.CharsetCorpus;
import net.phoenix.nlp.pos.chmm.corpus.file.CharsetFileCorpus;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Jigsaw server 识别如 1987年11月1日3点50分12秒的全部或者部分片段，其中数字可以是汉字大写、阿拉伯数字。
 * 
 * @author <a href="shamphone@gmail.com">Li XiongFeng</a>
 * @date 2013-2-3
 * @version 1.0.0
 */
public class DateTimeRecognitor extends AbstractRecognitor {

	private static Log log = LogFactory.getLog(DateTimeRecognitor.class);

	enum Status {
		Number, Date, Qualifier, Other
	};

	private char[] numbers;
	private char[] qualifiers;

	public DateTimeRecognitor(CorpusRepository dictionary) throws IOException {
		super(dictionary);
		CharsetCorpus chars = dictionary.getCorpus(CharsetFileCorpus.class);
		this.numbers = chars.getChars("datetime.number");
		Arrays.sort(this.numbers);
		this.qualifiers = chars.getChars("datetime.qualifier");
		Arrays.sort(this.qualifiers);
		log.info("DateTimeRecognitor ready.");
	}



	@Override
	public void recognize(TermGraph graph) {
		this.resetEdgesScore(graph, 0.0);
		// 从每一个点边开始检查其成词的可能性;
		// 系统将生成一些新的节点，这样可以避免同时修改一个集合的问题。
		List<POSTerm> currentVertexes = new ArrayList<POSTerm>(graph.vertexSet());
		for (POSTerm start : currentVertexes) {
			// 这个词是否可以作为长度为length的名字的开头词；
			Status status = this.parseStatus(start);
			if (status != Status.Other) {
				TermPath candidate = graph.createPath(start);
				this.findDateTime(graph, candidate, status);
			}

		}

	}

	private Status parseStatus(POSTerm term) {
		if (term.getTermNatures().isNature(Nature.Number))
			return Status.Number;
		String name = term.getName();
		name = name.trim();
		if (name.length() == 0)
			return Status.Other;
		// 只有一个字符的；
		if (name.length() == 1) {
			if (Arrays.binarySearch(this.qualifiers, name.charAt(0)) >= 0)
				return Status.Qualifier;
			else if (Arrays.binarySearch(this.numbers, name.charAt(0)) >= 0)
				return Status.Number;
			else
				return Status.Other;
		}

		// 多个字符，检查是全数字，或者数字+ 年/月/日的形式
		boolean isNumber = true;
		int pos = 0;
		// 所有字符都是数字；
		while (isNumber && pos < name.length() - 1) {
			isNumber = (Arrays.binarySearch(this.numbers, name.charAt(pos)) >= 0);
			pos++;
		}
		if (isNumber) {
			if (Arrays.binarySearch(this.qualifiers, name.charAt(pos)) >= 0)
				return Status.Date;
			else if (Arrays.binarySearch(this.numbers, name.charAt(pos)) >= 0)
				return Status.Number;
			else
				return Status.Other;
		}

		return Status.Other;
	}

	/**
	 * 通过遍历来发现长度为length的词。
	 * 
	 * @param graph
	 *            句图
	 * @param partName
	 *            当前已经发现的名字片段或者名字
	 * @param length
	 *            待发现的名字长度；
	 * @param names
	 *            用来存放发现的结果；
	 */
	private void findDateTime(TermGraph graph, TermPath partName, Status currentStatus) {
		// int pos = partName.getVertexCount();
		POSTerm current = partName.getEndVertex();
		for (TermEdge edge : graph.outgoingEdgesOf(current)) {
			// 对于长度为N的名字，往前走一步，到N+1，检查下路径是否可以组成名字；
			// 判断新增加的这条边目标节点是否可以作为名字的第N个字符；
			POSTerm next = graph.getEdgeTarget(edge);
			Status nextStatus = this.parseStatus(next);
			boolean canContinue = false;
			switch (currentStatus) {
			case Number:
				canContinue = (nextStatus.equals(Status.Qualifier) || nextStatus.equals(Status.Date));
				break;
			case Qualifier:
			case Date:
				canContinue = nextStatus.equals(Status.Number) || nextStatus.equals(Status.Date);
				break;
			default:
				canContinue = false;
			}
			if (canContinue) {
				partName.extend(edge);
				this.findDateTime(graph, partName, nextStatus);
				partName.removeEnd();
			} else {
				this.checkDateTime(graph, partName);
			}
		}

	}

	/**
	 * 将found的数字放到number中。
	 * 
	 * @param graph
	 * @param found
	 * @param numbers
	 */
	private void checkDateTime(TermGraph graph, TermPath partName) {
		if (partName.getVertexCount() == 1) {
			return;
		}
		this.createMergedTerm(graph, partName, this.createTermNatures(Nature.Time), false);
		// log.info(term + " found!");
	}

}
