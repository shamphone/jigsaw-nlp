/**
 * 
 */
package net.phoenix.nlp.pos.dictionary;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;

import net.phoenix.nlp.pos.Dictionary;

/**
 * @author lixf
 * 
 */
public class T2SDictionary extends FileBaseDictionary {
	private char map[]; // 简繁体映射表；

	/**
	 * 
	 * @param mapFile
	 *            简繁体映射文件
	 * @throws IOException
	 */
	public T2SDictionary(Dictionary parent) throws IOException {
		super(parent, "t2s.data");
	}

	@Override
	protected void load(File mapFile) throws IOException {
		if (this.map == null) {
			this.map = new char[65535];
			Arrays.fill(map, (char) 0);
		}
		BufferedReader reader = null;
		try {
			reader = new BufferedReader(new FileReader(mapFile));
			String temp;
			while ((temp = reader.readLine()) != null) {
				temp = temp.trim();
				if (isBlank(temp)) {
					continue;
				}
				if (map[temp.charAt(0)] == 0) {
					map[temp.charAt(0)] = temp.charAt(2);
				}
			}
			reader.close();

		} finally {
			if (reader != null)
				reader.close();
		}
	}

	/**
	 * 简繁体转换,
	 * 
	 * @param c
	 *            输入'孫'
	 * @return 输出'孙'
	 */
	public char toSimple(char c) {
		char value = map[c];
		if (value == 0) {
			return c;
		}
		return value;
	}

}
