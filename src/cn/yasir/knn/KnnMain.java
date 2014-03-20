package cn.yasir.knn;

import java.io.File;
import java.util.LinkedList;
import java.util.List;

import cn.yasir.knn.data.Message;
import cn.yasir.util.ReadAll;
import cn.yasir.util.ReadByLine;

public class KnnMain {

	public static void main(String[] args) {
		knn();
	}

	/**
	 * 
	 * @param path
	 * @return
	 */
	private static List<Message> readDirFiles(String path) {
		List<Message> result = new LinkedList<Message>();
		File file = new File(path);
		for (File item : file.listFiles()) {
			Message msg = new Message();
			msg.setId(item.getName());
			msg.setContent(ReadAll.readAll(item.getAbsolutePath(), "utf-8"));
			result.add(msg);
		}
		return result;
	}

	private static void knn() {
		List<Message> inputMsgs = readDirFiles("data_sets/cookbook/");  //输入数据
		List<Message> passed = readDirFiles("data_sets/cookbook/");     //菜谱相关信息训练集
		List<Message> unPassed = readDirFiles("data_sets/unrelated/"); //不相关信息训练集

		List<String> properties = ReadByLine.readByLine("KnnProperties.txt","utf-8");
		KnnModel knnModel = new KnnModel(passed, unPassed, properties);
		knnModel.DEBUG = false;   
		int falseCount = 0;
		for (Message msg : inputMsgs) {
			boolean result = knnModel.judge(msg.getContent());
			System.out.println(result + " " + msg.getContent());
			if (!result)
				falseCount++;
		}
		System.out.println("Input " + inputMsgs.size() + " messages and the wrong judgement count is :" + falseCount);  //误判总数
	}

}
