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
package net.phoenix.nlp.pos.term;

import java.util.HashMap;
import java.util.Map;

import net.phoenix.nlp.pos.TermEdge;

import org.jgrapht.graph.DefaultWeightedEdge;

/**
 * Jigsaw server
 * 
 * @author    <a href="shamphone@gmail.com">Li XiongFeng</a>
 * @date      2013-1-27
 * @version   1.0.0
 */
public class DefaultTermEdge  extends DefaultWeightedEdge implements TermEdge{
	/**
	 * 
	 */
	private static final long serialVersionUID = 731639439730465378L;
	private double weight;
	private Map<String, Double> scores;
	
	public DefaultTermEdge() {
		weight = 0;
		this.scores = new HashMap<String, Double>();
	}


	@Override
	public void setWeight(double score) {
		this.weight = score;
	}
	/*
	 * (non-Javadoc)
	 * 
	 * @see net.phoenix.nlp.pos.domain.Term#getScore()
	 */
	@Override
	public double getWeight() {
		return this.weight;
	}
	@Override
	public void setScore(String name, double score) {
		this.scores.put(name, score);
	}

	@Override
	public double getScore(String name) {
		if (this.scores.containsKey(name))
			return this.scores.get(name);
		return 0;

	}
}
