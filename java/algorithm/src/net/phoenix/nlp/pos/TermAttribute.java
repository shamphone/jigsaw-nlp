/**
 * 
 */
package net.phoenix.nlp.pos;

/**
 * 
 * 描述一个Term的属性，用于各种识别算法中。
 * 注意TermAttribute和TermNature之间并没有直接的关系。比如一个作为动词的Term，他也可以是一个name的结束位置，从而具有NameAttribute属性。
 * @author lixf
 *
 */
public interface TermAttribute {

}
