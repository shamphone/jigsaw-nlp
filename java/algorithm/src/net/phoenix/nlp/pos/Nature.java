package net.phoenix.nlp.pos;

import java.util.HashMap;
import java.util.Map;

/**
 * 
 * 
 * @author lixf
 * 
 */
public class Nature {
	public static final String NULL = "null";
	public static final String Number = "m";
	public static final String Qualifier = "mg"; //数量词
	public static final String English = "en";
	public static final String Begin = "始##始";
	public static final String End = "末##末";
	public static final String USER_DEFINE = "userDefine";
	public static final String PersonName = "nr";
	public static final String OrginizationName = "nt";
	public static final String NW = "nw";
	public static final String Time = "t";
	
	private static Map<String, Nature> nameMap = new HashMap<String, Nature>();
	private static Map<Integer, Nature> indexMap  = new HashMap<Integer, Nature>();
	public static Nature register(String name, int index){
		if(nameMap.containsKey(name))
			return nameMap.get(name);
			//throw new IllegalArgumentException("nature already exists for name:" + name);
		if(indexMap.containsKey(index))
			return indexMap.get(index);
			//throw new IllegalArgumentException("nature already exists for index:" + index);
		Nature nature = new Nature(name, index);
		nameMap.put(name, nature);
		indexMap.put(index, nature);
		return nature;
	}
	public static int count(){
		return nameMap.size();
	}

	private int index;
	private String name;
	
	public Nature(String name, int index){
		this.name = name;
		this.index = index;
	}
	
	public static Nature valueOf(String name){
		Nature nature = nameMap.get(name);
		if(nature == null)
			throw new IllegalArgumentException("Unknow nature name : "+ name);
		return nature;
	}

	public static Nature valueOf(int index){
		Nature nature = indexMap.get(index);
		if(nature == null)
			throw new IllegalArgumentException("Unknow nature index : "+ index);
		return nature;
	}

	public boolean typeOf(Nature parent){
		return this.name.startsWith(parent.name);
	}

	
	public boolean typeOf(String parent){
		return this.name.startsWith(parent);
	}


	/**
	 * @return the natureStr
	 */
	public String toString(){
		return this.name;
	}
	
	public int toInt(){
		return this.index;
	}
	
	public boolean equals(Object obj){
		if(obj==null)
			return false;
		Nature another = (Nature)obj;
		return another.index == index;
	}


}