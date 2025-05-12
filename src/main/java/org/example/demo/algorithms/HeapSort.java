package org.example.demo.algorithms;

import org.example.demo.VisualizationCallback;

import java.util.Arrays;

public class HeapSort {
    private final VisualizationCallback callback;

    public HeapSort(VisualizationCallback callback) {
        this.callback = callback;
    }
    public void heapSortVisualization(int[] arr) {
        new Thread(() -> {
            try {
                // Build heap (rearrange array)
                for (int i = arr.length / 2 - 1; i >= 0; i--) {
                    heapify(arr, arr.length, i);
                    callback.update(arr, i, -1, "Heapify at index " + i);
                    Thread.sleep(1000);
                }

                callback.update(arr, -1, -1, "Max heap built: " + Arrays.toString(arr));
                Thread.sleep(1000);

                // One by one extract an element from heap
                for (int i = arr.length - 1; i > 0; i--) {
                    // Move current root to end
                    int temp = arr[0];
                    arr[0] = arr[i];
                    arr[i] = temp;

                    callback.update(arr, 0, i, "Swapped root " + temp + " with last element " + arr[0]);
                    Thread.sleep(1000);

                    // call max heapify on the reduced heap
                    heapify(arr, i, 0);
                    callback.update(arr, 0, -1, "Heapify reduced heap");
                    Thread.sleep(1000);
                }

                callback.update(arr, -1, -1, "Heap Sort complete!");
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }).start();
    }

    private void heapify(int[] arr, int n, int i) {
        int largest = i;
        int left = 2 * i + 1;
        int right = 2 * i + 2;

        if (left < n && arr[left] > arr[largest]) {
            largest = left;
        }

        if (right < n && arr[right] > arr[largest]) {
            largest = right;
        }

        if (largest != i) {
            int swap = arr[i];
            arr[i] = arr[largest];
            arr[largest] = swap;

            heapify(arr, n, largest);
        }
    }
}
