/**
 * 
 */
package net.phoenix.nlp.pos.chmm;

import java.util.HashMap;
import java.util.Map;

/**
 * 节点树上的节点。具体参见CharTreeDictionary注释说明
 * @author lixf
 * 
 */
public class BasicCharNode implements CharNode {
	private char ch; //这个节点对应的字符；
	private State state; //节点状态；
	private Map<Character, CharNode> children; //子节点；
	private TermNatures termNatures; //节点的所有可能词性；

	public BasicCharNode(char ch, int state) {
		this(ch, State.valueOf(state));
	}

	public BasicCharNode(char ch, State state) {
		this.ch = ch;
		this.state = state;
		this.children = new HashMap<Character, CharNode>();
		this.termNatures = new BasicTermNatures();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see net.phoenix.nlp.pos.CharNode#getChar()
	 */
	@Override
	public char getChar() {
		return this.ch;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see net.phoenix.nlp.pos.CharNode#getState()
	 */
	@Override
	public State getState() {
		return this.state;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see net.phoenix.nlp.pos.CharNode#getTermNatures()
	 */
	@Override
	public TermNatures getTermNatures() {
		return this.termNatures;
	}

	public void setTermNatures(TermNatures natures) {
		if (natures != null)
			this.termNatures = natures;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see net.phoenix.nlp.pos.CharNode#get(char)
	 */
	@Override
	public CharNode get(char ch) {
		return this.children.get(ch);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see net.phoenix.nlp.pos.CharNode#get(char)
	 */
	public void put(char ch, CharNode node) {
		this.children.put(ch, node);
	}

}
