package org.example.demo;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
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
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.util.Duration;
import org.example.demo.algorithms.*;

import java.io.IOException;
import java.util.Arrays;


public class HelloApplication extends Application {
    private static final int WIDTH = 1280;
    private static final int HEIGHT = 720;
    private static final int MAX_ARRAY_SIZE = 30;
    private static final int MIN_VALUE = 1;
    private static final int MAX_VALUE = 45;
    private static final int BAR_WIDTH = 40;
    private static final int SPACING = 5;

    private int[] array;
    private Pane visualizationPane;
    private TextArea stepsArea;
    private ComboBox<String> algorithmComboBox;
    private TextField inputField;
    private Button sortButton, resetButton, randomButton;
    private Slider speedSlider;
    private int animationSpeed = 500;

    @Override
    public void start(Stage primaryStage) {
        visualizationPane = new Pane();
        visualizationPane.setPrefSize(WIDTH, 300);
        visualizationPane.setStyle("-fx-background-color: #e3f2fd; -fx-border-color: #90caf9; -fx-border-radius: 10;");

        stepsArea = new TextArea();
        stepsArea.setEditable(false);
        stepsArea.setPrefSize(WIDTH, 150);
        stepsArea.setStyle("-fx-font-family: monospace; -fx-background-color: #fafafa;");

        algorithmComboBox = new ComboBox<>();
        algorithmComboBox.getItems().addAll("Insertion Sort", "Merge Sort", "Bubble Sort", "Heap Sort", "Selection Sort", "Quick Sort", "Radix Sort", "Counting Sort");
        algorithmComboBox.setValue("Insertion Sort");

        inputField = new TextField();
        inputField.setPromptText("Enter 10-20 numbers between 1-20, comma separated");
        inputField.setPrefWidth(400);

        sortButton = new Button("Sort");
        sortButton.setOnAction(e -> startSorting());

        resetButton = new Button("Reset");
        resetButton.setOnAction(e -> resetVisualization());

        randomButton = new Button("Random");
        randomButton.setOnAction(e -> generateRandomArray());

        speedSlider = new Slider(500, 1500, 300);
        speedSlider.setShowTickLabels(true);
        speedSlider.setShowTickMarks(true);
        speedSlider.setMajorTickUnit(250);
        speedSlider.valueProperty().addListener((obs, oldVal, newVal) -> animationSpeed = newVal.intValue());

        Label speedLabel = new Label("Speed (ms):");
        HBox speedBox = new HBox(10, speedLabel, speedSlider);
        speedBox.setAlignment(Pos.CENTER_LEFT);

        Label title = new Label("Sorting Algorithm Visualizer");
        title.setFont(Font.font("Arial", 24));

        HBox inputBox = new HBox(10, new Label("Algorithm:"), algorithmComboBox, new Label("Input:"), inputField);
        inputBox.setAlignment(Pos.CENTER_LEFT);

        HBox buttonBox = new HBox(10, sortButton, resetButton, randomButton);
        buttonBox.setAlignment(Pos.CENTER);

        VBox root = new VBox(15, title, inputBox, buttonBox, speedBox, visualizationPane, new Label("Sorting Steps:"), stepsArea);
        root.setPadding(new Insets(15));
        root.setStyle("-fx-background-color: #ffffff;");

        Scene scene = new Scene(root, WIDTH, HEIGHT);
        primaryStage.setTitle("Sorting Visualizer Tool");
        primaryStage.setScene(scene);
        primaryStage.show();

        generateRandomArray();
    }

    private void generateRandomArray() {
        int size = 10 + (int) (Math.random() * 11);
        array = new int[size];
        for (int i = 0; i < size; i++) {
            array[i] = MIN_VALUE + (int) (Math.random() * (MAX_VALUE - MIN_VALUE + 1));
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

            if (array.length < 10 || array.length > MAX_ARRAY_SIZE) {
                throw new IllegalArgumentException("Array size must be between 15 and " + MAX_ARRAY_SIZE);
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
            bar.setFill(i == highlight1 || i == highlight2 ? Color.RED : Color.STEELBLUE);
            bar.setStroke(Color.BLACK);

            Label label = new Label(String.valueOf(arr[i]));
            label.setLayoutX(startX + i * (BAR_WIDTH + SPACING) + BAR_WIDTH / 4.0);
            label.setLayoutY(290);

            visualizationPane.getChildren().addAll(bar, label);
        }
    }

    private void updateVisualization(int[] arr, int highlight1, int highlight2, String message) {
        Timeline timeline = new Timeline();
        timeline.getKeyFrames().add(new KeyFrame(Duration.millis(animationSpeed), e -> {
            drawArray(arr, highlight1, highlight2);
            stepsArea.appendText(message + "\n");
        }));
        timeline.play();
    }

    private void startSorting() {
        String algorithm = algorithmComboBox.getValue();
        stepsArea.clear();
        stepsArea.appendText("Starting " + algorithm + "\n");
        stepsArea.appendText("Original array: " + Arrays.toString(array) + "\n");

        int[] arrCopy = Arrays.copyOf(array, array.length);

        switch (algorithm) {
            case "Insertion Sort":
                new InsertionsSort(this::updateVisualization).insertionSortVisualization(arrCopy,animationSpeed);
                break;
            case "Merge Sort":
                new MergeSort(this::updateVisualization).mergeSortVisualization(arrCopy,animationSpeed);
                break;
            case "Bubble Sort":
                new BubbleSort(this::updateVisualization).bubbleSortVisualization(arrCopy,animationSpeed);
                break;
            case "Heap Sort":
                new HeapSort(this::updateVisualization).heapSortVisualization(arrCopy,animationSpeed);
                break;
            case "Selection Sort":
                new SelectionSort(this::updateVisualization).selectionSortVisualization(arrCopy,animationSpeed);
                break;
            case "Quick Sort":
                new QuickSort(this::updateVisualization).quickSortVisualization(arrCopy,animationSpeed);
                break;
            case "Radix Sort":
                new RadixSort(this::updateVisualization).radixSortVisualization(arrCopy,animationSpeed);
                break;
            case "Counting Sort":
                new CountingSort(this::updateVisualization).countingSortVisualization(arrCopy,animationSpeed);
                break;
        }
    }

    private void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Warning");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
