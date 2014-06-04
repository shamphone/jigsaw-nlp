/**
 * 
 */
package net.phoenix.nlp.pos;

/**
 * @author lixf
 *
 */
public class DictionaryWrapper implements Dictionary {
	protected Dictionary parent;
	public DictionaryWrapper(Dictionary parent) {
		this.parent = parent;
	}

	
	@Override
	public <T extends Dictionary> T getDictionary(Class<T> clazz) {
		if(clazz.isInstance(this))
			return clazz.cast(this);
		if(clazz.isInstance(parent))
			return clazz.cast(parent);
		if(parent instanceof DictionaryWrapper)
			return ((DictionaryWrapper)parent).getDictionary(clazz);
		return null;
	}
	/**
	 * 判断字符串是否为空
	 * 
	 * @param cs
	 * @return
	 */
	public static boolean isBlank(CharSequence cs) {
		int strLen;
		if (cs == null || (strLen = cs.length()) == 0) {
			return true;
		}
		for (int i = 0; i < strLen; i++) {
			if (Character.isWhitespace(cs.charAt(i)) == false) {
				return false;
			}
		}
		return true;
	}
}
