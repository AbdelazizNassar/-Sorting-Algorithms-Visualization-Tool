package org.example.demo.algorithms;

import org.example.demo.VisualizationCallback;

import java.util.Arrays;

public class MergeSort {
    private final VisualizationCallback callback;

    public MergeSort(VisualizationCallback callback) {
        this.callback = callback;
    }
    public void mergeSortVisualization(int[] arr) {
        new Thread(() -> {
            try {
                int[] temp = new int[arr.length];
                mergeSortHelper(arr, temp, 0, arr.length - 1);
                callback.update(arr, -1, -1, "Merge Sort complete!");
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }).start();
    }

    private void mergeSortHelper(int[] arr, int[] temp, int left, int right) throws InterruptedException {
        if (left < right) {
            int mid = left + (right - left) / 2;
            mergeSortHelper(arr, temp, left, mid);
            mergeSortHelper(arr, temp, mid + 1, right);
            merge(arr, temp, left, mid, right);

            callback.update(arr, -1, -1, "Merged " + left + "-" + right + ": " + Arrays.toString(arr));
            Thread.sleep(1000);
        }
    }

    private void merge(int[] arr, int[] temp, int left, int mid, int right) {
        System.arraycopy(arr, left, temp, left, right - left + 1);

        int i = left;
        int j = mid + 1;
        int k = left;

        while (i <= mid && j <= right) {
            if (temp[i] <= temp[j]) {
                arr[k] = temp[i];
                i++;
            } else {
                arr[k] = temp[j];
                j++;
            }
            k++;
        }

        while (i <= mid) {
            arr[k] = temp[i];
            k++;
            i++;
        }
    }
}
