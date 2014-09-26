package cn.yasir.knn;

import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import cn.yasir.knn.bean.KNN;
import cn.yasir.knn.bean.Message;
import cn.yasir.util.WordCount;

public class KnnModel {

	public boolean DEBUG = false;
	long start = 0;

	private List<KNN> metric = new LinkedList<KNN>();

	private static int k = 7;  //找最近的几个邻居
	
	private static int p = 3;  //超过几个属性匹配才录入比较


	/**
	 * 
	 * @param passedMsgs
	 * @param unPasseddMsgs
	 * @param properties
	 */
	public KnnModel(List<Message> passedMsgs,List<Message> unPasseddMsgs,List<String> properties) {
		start = System.currentTimeMillis();
		initKnnModel(metric, properties, passedMsgs, true);
		initKnnModel(metric, properties, unPasseddMsgs, false);
	}

	public boolean judge(String content) {
		
		KNN[] data = new KNN[metric.size()];
		buildData(data, content, metric);
		HeapSort.buildMaxHeapify(data);
		HeapSort.heapSort(data);
		return findKNeibor(data);
	}

	private void initKnnModel(List<KNN> metric, List<String> properties,
			List<Message> msgs, boolean result) {
		for (Message msg : msgs) {
			List<Map<String, Integer>> valueMaps = new LinkedList<Map<String, Integer>>();
			Map<String, Integer> wordCountMap = new HashMap<String, Integer>();
			WordCount.chineseCharacterWordCount(wordCountMap, msg.getContent());
			int hit = 0;
			for (String property : properties) {
				Map<String, Integer> valueMap = new HashMap<String, Integer>();
				if (wordCountMap.get(property) == null) {
					valueMap.put(property, 0);
				} else {
					hit ++;
					valueMap.put(property, wordCountMap.get(property));
				}
				valueMaps.add(valueMap);
			}
			if(hit > p){
				metric.add(new KNN(msg.getId(), msg.getContent(), valueMaps, result));
			}
		}
	}

	private void buildData(KNN[] data, String content, List<KNN> metric) {
		Map<String, Integer> wordCountMap = new HashMap<String, Integer>();
		WordCount.chineseCharacterWordCount(wordCountMap, content);
		for (int i = 0; i < metric.size(); i++) {
			KNN knn = metric.get(i);
			int distance = 0;
			int hit = 0;
			for (Map<String, Integer> valueMap : knn.valueMaps) {
				for (Iterator<Entry<String, Integer>> it = valueMap.entrySet().iterator(); it.hasNext();) {
					Entry<String, Integer> entry = (Entry<String, Integer>) it.next();
					String property = entry.getKey();
					int value = entry.getValue();
					Integer x = wordCountMap.get(property);
					if (x == null){
						x = 0;
					}else{
						hit ++;
						x = x.intValue();
					}
					distance += (value - x) * (value - x);
				}
			}
			if(hit > p){
				knn.distance = distance;
			}else{
				knn.distance = Integer.MAX_VALUE;
			}
			data[i] = knn;
		}
	}

	private boolean findKNeibor(KNN[] data) {
		// TODO Auto-generated method stub
		int right = 0, wrong = 0;
		int hit = 0;
		for (int i = 0; i < k && i < data.length; i++) {
			KNN knn = data[i];
			if(knn.distance < Integer.MAX_VALUE){
				hit ++;
				if (knn.result) {
					right++;
				} else {
					wrong++;
				}
				if (DEBUG) {
					List<Map<String,Integer>> hitMaps = new LinkedList<Map<String,Integer>>();
					for(Map<String,Integer> map : knn.valueMaps){
						for(String key:map.keySet()){
							if(map.get(key) != 0){
								hitMaps.add(map);
							}
						}
					}
					System.out.println("msgId:" + knn.msgId + " " + "distance:" + knn.distance + " result:" + knn.result + " hit:"
							+ hitMaps + " \ncontent:" + knn.content + "\n");
				}
			}
		}
		if(hit == k){
			return right > wrong ? true : false;
		}else{
			return false; //邻居过少，不予判断
		}
	}

}
