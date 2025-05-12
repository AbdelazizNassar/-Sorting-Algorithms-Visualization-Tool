package org.example.demo;
@FunctionalInterface
public interface VisualizationCallback {
    void update(int[] arr, int highlight1, int highlight2, String message);
}
