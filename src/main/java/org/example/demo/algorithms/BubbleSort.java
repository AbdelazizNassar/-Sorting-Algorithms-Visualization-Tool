package org.example.demo.algorithms;

import org.example.demo.VisualizationCallback;

import java.util.Arrays;

public class BubbleSort {
    private final VisualizationCallback callback;

    public BubbleSort(VisualizationCallback callback) {
        this.callback = callback;
    }
    public void bubbleSortVisualization(int[] arr,int animationDelay) {
        new Thread(() -> {
            try {
                for (int i = 0; i < arr.length - 1; i++) {
                    boolean swapped = false;

                    for (int j = 0; j < arr.length - i - 1; j++) {
                        if (arr[j] > arr[j + 1]) {
                            int temp = arr[j];
                            arr[j] = arr[j + 1];
                            arr[j + 1] = temp;
                            swapped = true;

                            callback.update(arr, j, j + 1, "Swapped " + arr[j] + " and " + arr[j + 1]);
                            Thread.sleep(animationDelay);
                        }
                    }

                    if (!swapped) {
                        callback.update(arr, -1, -1, "No swaps needed. Sorting complete!");
                        break;
                    }

                    callback.update(arr, -1, -1, "Pass " + (i + 1) + " complete: " + Arrays.toString(arr));
                    Thread.sleep(animationDelay);
                }
                callback.update(arr, -1, -1, "Bubble Sort complete!");
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }).start();
    }

}
