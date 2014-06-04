/**
 * 
 */
package net.phoenix.nlp.sentence;

/**
 * 句子
 * 
 * @author lixf
 * 
 */
public interface Sentence extends CharSequence {
	enum Type {
		None(0), //无类型
		Declarative(1), // 陈述句
		Interrogative(2), // 疑问句
		Imperative(3), // 祈使句；
		Exclamatory(4) // 感叹句;
		;

		private int index;

		private Type(int index) {
			this.index = index;
		}

		public int toInt() {
			return this.index;
		}

		public static Type valueOf(int index) {
			if (index < 0 || index > 4)
				throw new IllegalArgumentException(
						"unknow sentence type for index " + index);
			for (Type type : Type.values())
				if (type.index == index)
					return type;
			return null;

		}
	}

	/**
	 * 在原文中的初始偏移
	 * 
	 * @return
	 */
	public int getStartOffset();

	/**
	 * 在原文中的结束偏移（不包含在这个Sentence中）。
	 * 
	 * @return
	 */
	public int getEndOffset();
	/**
	 * 获取句子的类型
	 * @return
	 */
	public Type getType();

}
