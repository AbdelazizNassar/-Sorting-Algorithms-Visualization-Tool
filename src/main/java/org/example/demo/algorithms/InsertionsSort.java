package org.example.demo.algorithms;

import org.example.demo.VisualizationCallback;

public class InsertionsSort {
    private final VisualizationCallback callback;

    public InsertionsSort(VisualizationCallback callback) {
        this.callback = callback;
    }

    public void insertionSortVisualization(int[] arr) {
        new Thread(() -> {
            try {
                for (int i = 1; i < arr.length; i++) {
                    int key = arr[i];
                    int j = i - 1;

                    while (j >= 0 && arr[j] > key) {
                        arr[j + 1] = arr[j];
                        callback.update(arr, j, j + 1, "Shift element " + arr[j + 1] + " to the right");
                        j--;
                        Thread.sleep(800);
                    }

                    arr[j + 1] = key;
                    callback.update(arr, j + 1, -1, "Insert " + key + " at position " + (j + 1));
                    Thread.sleep(800);
                }
                callback.update(arr, -1, -1, "Insertion Sort complete!");
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }).start();
    }
}
