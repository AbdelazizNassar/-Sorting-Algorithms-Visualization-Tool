package org.example.demo.algorithms;

import org.example.demo.VisualizationCallback;

import java.util.Arrays;

public class RadixSort {
    private final VisualizationCallback callback;

    public RadixSort(VisualizationCallback callback) {
        this.callback = callback;
    }

    public void radixSortVisualization(int[] arr,int animationDelay) {
        new Thread(() -> {
            try {
                int max = Arrays.stream(arr).max().orElse(0);
                for (int exp = 1; max / exp > 0; exp *= 10) {
                    countingSortByDigit(arr, exp,animationDelay);
                    callback.update(arr, -1, -1, "Sorted by digit place " + exp);
                    Thread.sleep(animationDelay);
                }
                callback.update(arr, -1, -1, "Radix Sort complete!");
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }).start();
    }

    private void countingSortByDigit(int[] arr, int exp,int animationDelay) throws InterruptedException {
        int n = arr.length;
        int[] output = new int[n];
        int[] count = new int[10];

        for (int i = 0; i < n; i++) {
            count[(arr[i] / exp) % 10]++;
            callback.update(arr, i, -1, "Counting digit of " + arr[i]);
            Thread.sleep(animationDelay);
        }

        for (int i = 1; i < 10; i++) {
            count[i] += count[i - 1];
        }

        for (int i = n - 1; i >= 0; i--) {
            output[count[(arr[i] / exp) % 10] - 1] = arr[i];
            count[(arr[i] / exp) % 10]--;
        }

        for (int i = 0; i < n; i++) {
            arr[i] = output[i];
            callback.update(arr, i, -1, "Placing " + arr[i] + " after sort by digit " + exp);
            Thread.sleep(animationDelay);
        }
    }

}
