/**
 * 
 */
package net.phoenix.nlp.pos.chmm;


/**
 * 用于索引TermNature的节点树的节点 
 * 
 * @author lixf
 * 
 */
public interface CharNode {
	enum State {
		// 0.代表这个字/词不在词典中, 不再继续；
		NOT_WORD(0),
		// 1. 不成词，但是可以继续。比如 中流砥(柱),走到 过 的时候，还没成词，继续好了。
		PART_OF_WORD(1),
		// 2.是个词但是还可以继续
		WORD_AND_PART(2),
		// 3.停止已经是个词了
		END_OF_WORD(3),
		// 4. 英语
		ENGLISH(4),
		// 5. 数字
		NUMBER(5);

		private int state;

		State(int state) {
			this.state = state;
		}

		public static State valueOf(int value) {
			for (State state : State.values()) {
				if (state.state == value)
					return state;
			}
			throw new IllegalArgumentException("Unknown state :" + value);
		}

		public boolean isChinese() {
			return state < 4;
		}

		@Override
		public String toString() {
			return String.valueOf(this.state);
		}

	};

	/**
	 * 这个节点对应的字符；
	 * 
	 * @return
	 */
	public char getChar();

	/**
	 * 从跟到当前节点的状态；
	 * 
	 * @return
	 */
	public State getState();

	/**
	 * 当前节点的TermNatures;
	 * 
	 * @return
	 */
	public TermNatures getTermNatures();

	/**
	 * 获取子节点；
	 * 
	 * @param ch
	 * @return
	 */
	public CharNode get(char ch);
}
