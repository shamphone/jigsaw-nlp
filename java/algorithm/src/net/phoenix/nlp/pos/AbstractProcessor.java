/**
 * 
 */
package net.phoenix.nlp.pos;

import net.phoenix.nlp.pos.dictionary.BasicTermNatures;

/**
 * @author lixf
 * 
 */
public class AbstractProcessor {
	protected transient Dictionary dictionary;
	private static final int BLANK_LENATH = 0;

	public AbstractProcessor(final Dictionary dictionary) {
		this.dictionary = dictionary;
	}

	public TermNatures createTermNatures(final String natureName) {
		final TermNatures natures = new BasicTermNatures();
		natures.addNature(Nature.valueOf(natureName));
		return natures;
	}

	public TermNatures createTermNatures(final String natureName, final int freq) {
		final TermNatures natures = new BasicTermNatures();
		natures.addNature(Nature.valueOf(natureName), freq);
		return natures;
	}
	

  /**
   * 判断字符串是否为空
   *
   * @param target
   * @return
   */
  public boolean isBlank(final CharSequence target) {
	  if(target == null)
		  return true;
      final int strLen = target.length();
      if (BLANK_LENATH == strLen) {
          return true;
      }
      for (int i = 0; i < strLen; i++) {
          if (!Character.isWhitespace(target.charAt(i))) {
              return false;
          }
      }
      return true;
  }		
}
