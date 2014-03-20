package cn.yasir.knn;

import cn.yasir.knn.KnnModel.KNN;


public class HeapSort {

	

	public static void buildMaxHeapify(KNN[] data) {
		int startIndex = getParentIndex(data.length - 1);

		for (int i = startIndex; i >= 0; i--) {
			maxHeapify(data, data.length, i);
		}
	}

	private static void maxHeapify(KNN[] data, int heapSize, int index) {
		int left = getChildLeftIndex(index);
		int right = getChildRightIndex(index);
		int largest = index;
		if (left < heapSize && data[index].distance < data[left].distance) {
			largest = left;
		}
		if (right < heapSize && data[largest].distance < data[right].distance) {
			largest = right;
		}
		if (largest != index) {
			KNN temp = data[index];
			data[index] = data[largest];
			data[largest] = temp;
			maxHeapify(data, heapSize, largest);
		}
	}

	public static void heapSort(KNN[] data) {
		for (int i = data.length - 1; i > 0; i--) {
			KNN temp = data[0];
			data[0] = data[i];
			data[i] = temp;
			maxHeapify(data, i, 0);
		}
	}
	
	private static int getParentIndex(int current) {
		return (current - 1) >> 1;
	}

	private static int getChildLeftIndex(int current) {
		return (current << 1) + 1;
	}

	private static int getChildRightIndex(int current) {
		return (current << 1) + 2;
	}

}
