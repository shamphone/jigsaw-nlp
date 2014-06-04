/**
 * 
 */
package net.phoenix.nlp.pos;

/**
 * 字典（加载器），用于加载指定字典。具体如何加载根据实现来确定接口。
 * @author lixf
 *
 */
public interface Dictionary {
	/**
	 * 获取所包含的其他字典；
	 * @param clazz
	 * @return
	 */
	public  <T extends Dictionary> T getDictionary(Class<T> clazz);
}
