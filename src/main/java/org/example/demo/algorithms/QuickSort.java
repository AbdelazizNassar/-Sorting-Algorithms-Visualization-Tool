package org.example.demo.algorithms;

import org.example.demo.VisualizationCallback;

public class QuickSort {
    private final VisualizationCallback callback;

    public QuickSort(VisualizationCallback callback) {
        this.callback = callback;
    }

    public void quickSortVisualization(int[] arr,int animationDelay) {
        new Thread(() -> {
            try {
                quickSortHelper(arr, 0, arr.length - 1, animationDelay);
                callback.update(arr, -1, -1, "Quick Sort complete!");
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }).start();
    }

    private void quickSortHelper(int[] arr, int low, int high,int animationDelay) throws InterruptedException {
        if (low < high) {
            int pi = partition(arr, low, high,animationDelay);
            callback.update(arr, pi, -1, "Pivot placed at index " + pi);
            Thread.sleep(animationDelay);

            quickSortHelper(arr, low, pi - 1,animationDelay);
            quickSortHelper(arr, pi + 1, high,animationDelay);
        }
    }

    private int partition(int[] arr, int low, int high, int animationDelay) throws InterruptedException {
        int pivot = arr[high];
        int i = low - 1;

        for (int j = low; j < high; j++) {
            callback.update(arr, j, high, "Comparing " + arr[j] + " with pivot " + pivot);
            Thread.sleep(animationDelay);
            if (arr[j] < pivot) {
                i++;
                int temp = arr[i];
                arr[i] = arr[j];
                arr[j] = temp;
                callback.update(arr, i, j, "Swapped " + arr[i] + " and " + arr[j]);
                Thread.sleep(animationDelay);
            }
        }

        int temp = arr[i + 1];
        arr[i + 1] = arr[high];
        arr[high] = temp;
        callback.update(arr, i + 1, high, "Swapped pivot " + arr[i + 1] + " with " + pivot);
        Thread.sleep(animationDelay);
        return i + 1;
    }
}
