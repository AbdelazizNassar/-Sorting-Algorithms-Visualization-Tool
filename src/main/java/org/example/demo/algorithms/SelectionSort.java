package org.example.demo.algorithms;

import org.example.demo.VisualizationCallback;

public class SelectionSort {
    private final VisualizationCallback callback;

    public SelectionSort(VisualizationCallback callback) {
        this.callback = callback;
    }


    public void selectionSortVisualization(int[] arr,int animationDelay) {
        new Thread(() -> {
            try {
                for (int i = 0; i < arr.length - 1; i++) {
                    int minIndex = i;
                    for (int j = i + 1; j < arr.length; j++) {
                        if (arr[j] < arr[minIndex]) {
                            minIndex = j;
                        }
                        callback.update(arr, j, minIndex, "Comparing " + arr[j] + " with current min " + arr[minIndex]);
                        Thread.sleep(animationDelay);
                    }

                    if (minIndex != i) {
                        int temp = arr[i];
                        arr[i] = arr[minIndex];
                        arr[minIndex] = temp;
                        callback.update(arr, i, minIndex, "Swapped " + arr[i] + " and " + arr[minIndex]);
                        Thread.sleep(animationDelay);
                    }

                    callback.update(arr, -1, -1, "Position " + i + " fixed: " + arr[i]);
                    Thread.sleep(animationDelay);
                }
                callback.update(arr, -1, -1, "Selection Sort complete!");
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }).start();
    }
}
