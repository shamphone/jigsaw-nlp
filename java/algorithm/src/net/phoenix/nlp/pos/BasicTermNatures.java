/**
 * 
 */
package net.phoenix.nlp.pos;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import net.phoenix.nlp.Nature;

/**
 * @author lixf
 *
 */
public class BasicTermNatures implements TermNatures{

	/**
	 * 关于这个term的所有词性
	 */
	private Map<Nature, Integer> termNatures = null;


	/**
	 * 扩展属性；
	 */
	private Map<String, TermAttribute> attributes;

	/**
	 * 构造方法.一个词对应一个TermNatures;
	 * 
	 * @param termNatures
	 */
	public BasicTermNatures() {

		this.termNatures = new HashMap<Nature, Integer>();
		this.attributes = new HashMap<String, TermAttribute>();
	}


	/**
	 * @return the sumFrequency
	 */
	public int getSumFrequency() {
		int sumFrequency = 0;
		for(Map.Entry<Nature, Integer> entry : this.termNatures.entrySet()) {
			sumFrequency+= entry.getValue();
		}
		return sumFrequency;
	}


	@Override
	public boolean isNature(String name) {
		return this.termNatures.containsKey(name);
	}
	
	@Override
	public boolean equals(Object obj){
		if(obj == null)
			return false;
		BasicTermNatures another = (BasicTermNatures)obj;
		return another.termNatures.equals(this.termNatures);
	}

	@Override
	public TermAttribute getAttribute(String name) {
		return this.attributes.get(name);
	}

	@Override
	public void setAttribute(String name, TermAttribute attribute) {
		this.attributes.put(name, attribute);
	}

	@Override
	public boolean hasAttribute(String name) {
		return this.attributes.containsKey(name);
	}
	
	@Override
	public int getFrequency(Nature nature) {
		if(this.termNatures.containsKey(nature))
			return this.termNatures.get(nature);
		return 0;
	}

	@Override
	public void addNature(Nature nature) {
		if(! this.termNatures.containsKey(nature)) {
			this.termNatures.put(nature, 1);
		}
	
	}

	@Override
	public void addNature(Nature nature, int freq) {
		this.termNatures.put(nature, freq);
	}


	@Override
	public Set<Nature> natures() {
		return this.termNatures.keySet();
	}	
}
