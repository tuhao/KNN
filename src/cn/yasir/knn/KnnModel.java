package cn.yasir.knn;

import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import cn.yasir.knn.data.Message;
import cn.yasir.util.WordCount;

public class KnnModel {

	public boolean DEBUG = true;

	private List<KNN> metric = new LinkedList<KNN>();

	private static int k = 7;    //取邻近k个

	public static class KNN {
		String msgId;
		List<Map<String, Integer>> valueMaps;
		boolean result = false;
		String content;
		public int distance;

		public KNN(String msgId, String content,
				List<Map<String, Integer>> valueMaps, boolean result) {
			this.msgId = msgId;
			this.content = content;
			this.valueMaps = valueMaps;
			this.result = result;
		}
	}


	/**
	 * 构建KnnModel
	 * @param passedMsgs    相关的信息列表
	 * @param unPasseddMsgs 不相关的信息列表
	 * @param properties    作为判断依据的属性列表
	 */
	public KnnModel(List<Message> passedMsgs,List<Message> unPasseddMsgs,List<String> properties) {
		initKnnModel(metric, properties, passedMsgs, true);
		initKnnModel(metric, properties, unPasseddMsgs, false);
	}

	/**
	 * 判断信息相关与否
	 * @param content
	 * @return true 相关;false 不相关
	 */
	public boolean judge(String content) {
		KNN[] data = new KNN[metric.size()];
		buildData(data, content, metric);
		HeapSort.buildMaxHeapify(data);
		HeapSort.heapSort(data);
		return findKNeibor(data);
	}

	/**
	 * 初始化knnModel
	 * @param metric
	 * @param properties
	 * @param msgs
	 * @param result
	 */
	private void initKnnModel(List<KNN> metric, List<String> properties,
			List<Message> msgs, boolean result) {
		for (Message msg : msgs) {
			List<Map<String, Integer>> valueMaps = new LinkedList<Map<String, Integer>>();
			Map<String, Integer> wordCountMap = new HashMap<String, Integer>();
			WordCount.chineseCharacterWordCount(wordCountMap, msg.getContent());
			for (String property : properties) {
				Map<String, Integer> valueMap = new HashMap<String, Integer>();
				if (wordCountMap.get(property) == null) {
					valueMap.put(property, 0);
				} else {
					valueMap.put(property, wordCountMap.get(property));
				}
				valueMaps.add(valueMap);
			}
			metric.add(new KNN(msg.getId(), msg.getContent(), valueMaps, result));
		}
	}

	/**
	 * 构建堆
	 * @param data    KNN堆
	 * @param content 
	 * @param metric
	 */
	private void buildData(KNN[] data, String content, List<KNN> metric) {
		Map<String, Integer> wordCountMap = new HashMap<String, Integer>();
		WordCount.chineseCharacterWordCount(wordCountMap, content);
		for (int i = 0; i < metric.size(); i++) {
			KNN knn = metric.get(i);
			int distance = 0;
			for (Map<String, Integer> valueMap : knn.valueMaps) {
				for (Iterator<Entry<String, Integer>> it = valueMap.entrySet()
						.iterator(); it.hasNext();) {
					Entry<String, Integer> entry = (Entry<String, Integer>) it
							.next();
					String property = entry.getKey();
					int value = entry.getValue();
					Integer x = wordCountMap.get(property);
					x = x == null ? 0 : x.intValue();
					distance += (value - x) * (value - x);
				}
			}
			knn.distance = distance;
			data[i] = knn;
		}
	}

	/**
	 * 合计相近K个邻居的值，取多者作为结果返回
	 * @param data
	 * @return
	 */
	private boolean findKNeibor(KNN[] data) {
		// TODO Auto-generated method stub
		int right = 0, wrong = 0;
		for (int i = 0; i < k && i < data.length; i++) {
			KNN knn = data[i];
			if (knn.result) {
				right++;
			} else {
				wrong++;
			}
			if (DEBUG) {
				System.out.println(knn.distance + " : " + knn.result + " "
						+ knn.msgId + " " + knn.content);
			}
		}
		return right > wrong ? true : false;
	}
	
}
