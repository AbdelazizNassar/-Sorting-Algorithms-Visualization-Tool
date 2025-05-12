package org.example.demo;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import org.example.demo.algorithms.*;

import java.io.IOException;
import java.util.Arrays;


public class HelloApplication extends Application {
    private static final int WIDTH = 1280;
    private static final int HEIGHT = 720;
    private static final int MAX_ARRAY_SIZE = 30;
    private static final int MIN_VALUE = 1;
    private static final int MAX_VALUE = 20;
    private static final int BAR_WIDTH = 40;
    private static final int SPACING = 5;

    private int[] array;
    private Pane visualizationPane;
    private TextArea stepsArea;
    private ComboBox<String> algorithmComboBox;
    private TextField inputField;
    private Button sortButton;
    private Button resetButton;
    private Button randomButton;

    @Override
    public void start(Stage primaryStage) {
        // Create UI components
        visualizationPane = new Pane();
        visualizationPane.setPrefSize(WIDTH, 300);
        visualizationPane.setStyle("-fx-background-color: #f0f0f0;");

        stepsArea = new TextArea();
        stepsArea.setEditable(false);
        stepsArea.setPrefSize(WIDTH, 150);
        stepsArea.setStyle("-fx-font-family: monospace;");

        algorithmComboBox = new ComboBox<>();
        algorithmComboBox.getItems().addAll(
                "Insertion Sort",
                "Merge Sort",
                "Bubble Sort",
                "Heap Sort",
                "Selection Sort",
                "Quick Sort",
                "Radix Sort"
        );
        algorithmComboBox.setValue("Insertion Sort");

        inputField = new TextField();
        inputField.setPromptText("Enter 15-30 numbers between 1-20, comma separated (e.g., 5,2,9,1,5,6)");
        inputField.setPrefWidth(400);

        sortButton = new Button("Sort");
        sortButton.setOnAction(e -> startSorting());

        resetButton = new Button("Reset");
        resetButton.setOnAction(e -> resetVisualization());

        randomButton = new Button("Random");
        randomButton.setOnAction(e -> generateRandomArray());

        // Layout
        HBox inputBox = new HBox(10,
                new Label("Algorithm:"), algorithmComboBox,
                new Label("Input:"), inputField
        );
        inputBox.setAlignment(Pos.CENTER_LEFT);

        HBox buttonBox = new HBox(10, sortButton, resetButton, randomButton);
        buttonBox.setAlignment(Pos.CENTER);

        VBox root = new VBox(15,
                new Label("Sorting Algorithm Visualizer"),
                inputBox,
                buttonBox,
                visualizationPane,
                new Label("Sorting Steps:"),
                stepsArea
        );
        root.setPadding(new Insets(15));
        root.setAlignment(Pos.TOP_CENTER);

        Scene scene = new Scene(root, WIDTH, HEIGHT);
        primaryStage.setTitle("Sorting Algorithm Visualizer");
        primaryStage.setScene(scene);
        primaryStage.show();

        // Initial setup
        generateRandomArray();
    }

    private void generateRandomArray() {
        int size = 15 + (int)(Math.random() * 16);
        array = new int[size];
        for (int i = 0; i < size; i++) {
            array[i] = MIN_VALUE + (int)(Math.random() * (MAX_VALUE - MIN_VALUE + 1));
        }
        inputField.setText(Arrays.toString(array).replaceAll("[\\[\\]]", ""));
        resetVisualization();
    }

    private void resetVisualization() {
        String input = inputField.getText().trim();
        if (input.isEmpty()) {
            generateRandomArray();
            return;
        }

        try {
            array = Arrays.stream(input.split(","))
                    .map(String::trim)
                    .mapToInt(Integer::parseInt)
                    .toArray();

            if (array.length < 15 || array.length > MAX_ARRAY_SIZE) {
                throw new IllegalArgumentException("Array size must be between 3 and " + MAX_ARRAY_SIZE);
            }

            for (int num : array) {
                if (num < MIN_VALUE || num > MAX_VALUE) {
                    throw new IllegalArgumentException("Numbers must be between " + MIN_VALUE + " and " + MAX_VALUE);
                }
            }

            drawArray(array, -1, -1);
            stepsArea.clear();
            stepsArea.appendText("Initial array: " + Arrays.toString(array) + "\n");

        } catch (Exception e) {
            showAlert("Invalid input: " + e.getMessage() + "\nUsing random array instead.");
            generateRandomArray();
        }
    }

    private void drawArray(int[] arr, int highlight1, int highlight2) {
        visualizationPane.getChildren().clear();

        double maxValue = Arrays.stream(arr).max().orElse(1);
        double scale = 250 / maxValue;

        double startX = (WIDTH - (arr.length * (BAR_WIDTH + SPACING))) / 2;

        for (int i = 0; i < arr.length; i++) {
            Rectangle bar = new Rectangle(
                    startX + i * (BAR_WIDTH + SPACING),
                    280 - arr[i] * scale,
                    BAR_WIDTH,
                    arr[i] * scale
            );

            if (i == highlight1 || i == highlight2) {
                bar.setFill(Color.RED);
            } else {
                bar.setFill(Color.STEELBLUE);
            }

            bar.setStroke(Color.BLACK);

            Label label = new Label(String.valueOf(arr[i]));
            label.setLayoutX(startX + i * (BAR_WIDTH + SPACING) + BAR_WIDTH/2 - 10);
            label.setLayoutY(285);

            visualizationPane.getChildren().addAll(bar, label);
        }
    }
    private void updateVisualization(int[] arr, int highlight1, int highlight2, String message) {
        javafx.application.Platform.runLater(() -> {
            drawArray(arr, highlight1, highlight2);
            stepsArea.appendText(message + "\n");
        });
    }

    private void startSorting() {
        String algorithm = algorithmComboBox.getValue();
        stepsArea.clear();
        stepsArea.appendText("Starting " + algorithm + "\n");
        stepsArea.appendText("Original array: " + Arrays.toString(array) + "\n");

        int[] arrCopy = Arrays.copyOf(array, array.length);

        switch (algorithm) {
            case "Insertion Sort":
                InsertionsSort insertionSort = new InsertionsSort(this::updateVisualization);
                insertionSort.insertionSortVisualization(arrCopy);
                break;
            case "Merge Sort":
                MergeSort mergeSort = new MergeSort(this::updateVisualization);
                mergeSort.mergeSortVisualization(arrCopy);
                break;
            case "Bubble Sort":
                BubbleSort bubbleSort = new BubbleSort(this::updateVisualization);
                bubbleSort.bubbleSortVisualization(arrCopy);
                break;
            case "Heap Sort":
                HeapSort heapSort = new HeapSort(this::updateVisualization);
                heapSort.heapSortVisualization(arrCopy);
                break;
            case "Selection Sort":
                SelectionSort selectionSort = new SelectionSort(this::updateVisualization);
                selectionSort.selectionSortVisualization(arrCopy);
                break;
            case "Quick Sort":
                QuickSort quickSort = new QuickSort(this::updateVisualization);
                quickSort.quickSortVisualization(arrCopy);
                break;
            case "Radix Sort":
                RadixSort radixSort = new RadixSort(this::updateVisualization);
                radixSort.radixSortVisualization(arrCopy);
                break;
        }
    }

    private void showAlert(String message) {
        javafx.application.Platform.runLater(() -> {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Warning");
            alert.setHeaderText(null);
            alert.setContentText(message);
            alert.showAndWait();
        });
    }


    public static void main(String[] args) {
        launch();
    }
}