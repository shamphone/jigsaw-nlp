package net.phoenix.nlp.pos.chmm;

/**
 * Jigsaw server
 * 
 * @author <a href="shamphone@gmail.com">Li XiongFeng</a>
 * @date 2013-1-27
 * @version 1.0.0
 */
public interface TermEdge {
	
	/**
	 * 获取该Term的最终得分；
	 * @return
	 */
	public double getWeight();

	/**
	 * 设置该Term的最终得分；
	 * @param score
	 */
	public void setWeight(double score);

	/**
	 * 获得某个分数，用于各具体的识别器中设置分数；
	 * @return
	 */
	public double getScore(String name);

	/**
	 * 设置某个分数，用户各具体识别器中的设置；
	 * @param score
	 */
	public void setScore(String name, double score);
}
