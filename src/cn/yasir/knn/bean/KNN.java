package cn.yasir.knn.bean;

import java.util.List;
import java.util.Map;

public class KNN {
	
	public String msgId;
	public String content;
	public List<Map<String, Integer>> valueMaps;
	public int distance;
	public boolean result = false;

	public KNN(String msgId, String content,List<Map<String, Integer>> valueMaps, boolean result) {
		this.msgId = msgId;
		this.content = content;
		this.valueMaps = valueMaps;
		this.result = result;
	}

}
