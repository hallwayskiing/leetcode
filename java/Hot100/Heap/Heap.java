package Hot100.Heap;

public class Heap {
    private void minHeapify(int[] arr, int i, int heapSize) {
        int left = 2 * i + 1;
        int right = 2 * i + 2;
        int smallest = i;

        if (left < heapSize && arr[left] < arr[smallest]) {
            smallest = left;
        }
        if (right < heapSize && arr[right] < arr[smallest]) {
            smallest = right;
        }

        if (smallest != i) {
            swap(arr,i,smallest);
            minHeapify(arr, smallest, heapSize);
        }
    }

    // 建最小堆
    public void buildMinHeap(int[] arr) {
        int n = arr.length;
        for (int i = n / 2 - 1; i >= 0; i--) {
            minHeapify(arr, i, n);
        }
    }

    private void swap(int[]arr,int i,int j){
        int temp=arr[i];
        arr[i]=arr[j];
        arr[j]=temp;
    }

    // 堆排序（升序）
    public void heapSort(int[] arr) {
        int n=arr.length;
        // 不断取出堆顶最小值，放到数组末尾
        for (int i = n - 1; i > 0; i--) {
            int temp = arr[0];
            arr[0] = arr[i];
            arr[i] = temp;

            minHeapify(arr, 0, i);
        }
    }

    // 测试
    public static void main(String[] args) {
        int[] arr = {3, 9, 2, 1, 4, 5};
        Heap heap = new Heap();

        heap.buildMinHeap(arr);
        for (int num : arr) {
            System.out.print(num + " ");
        }

        // 输出： [9, 5, 4, 3, 2, 1]
        System.out.println();
        heap.heapSort(arr);
        for (int num : arr) {
            System.out.print(num + " ");
        }
    }
}
