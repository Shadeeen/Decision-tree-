package application;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import java.io.File;
import java.util.*;

public class Main extends Application {

	private File selectedFile;
	private String selectedLabel;
	

	@Override
	public void start(Stage primaryStage) {
		Interface pane = new Interface();
		Scene scene = new Scene(pane.firstScreen());
		Stage stage = new Stage();
		stage.setScene(scene);
		stage.show();
		stage.setMaximized(true);

		pane.getChooseFileButton().setOnAction(e -> {
			FileChooser fileChooser = new FileChooser();
			fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("CSV Files", "*.csv"));
			selectedFile = fileChooser.showOpenDialog(primaryStage);
			if (selectedFile != null) {
				try {
					List<String> columns = DataLoader.loadColumnsFromFile(selectedFile);
					pane.getLabelSelector().getItems().setAll(columns);
					pane.getLabelSelector().setDisable(false);
				} catch (Exception ex) {
					pane.getTreeOutput().setText("Error: " + ex.getMessage());
				}
			}
		});

		pane.getLabelSelector().setOnAction(e -> {
			selectedLabel = pane.getLabelSelector().getValue();
			pane.getBuildTreeButton().setDisable(selectedLabel == null);
		});

		pane.getBuildTreeButton().setOnAction(e -> {
			if (selectedFile != null && selectedLabel != null) {
				try {
					
					Random random = new Random();
					int maxDepth = random.nextInt(20) + 5; 
					int minSamplesSplit = random.nextInt(20) + 5;
					List<Map<String, String>> data = DataLoader.loadData(selectedFile.getAbsolutePath());
					Map<String, List<Map<String, String>>> split = DataLoader.splitData(data, 0.6);
					List<Map<String, String>> trainData = split.get("train");
					List<Map<String, String>> testData = split.get("test");

					List<String> attributes = new ArrayList<>(trainData.get(0).keySet());
					attributes.remove(selectedLabel);

					ID3TreeBuilder treeBuilder = new ID3TreeBuilder(maxDepth, minSamplesSplit);
					Node tree = treeBuilder.buildTree(trainData, attributes, selectedLabel);

					ID3TreePrinter.printTree(tree, pane.getTreeOutput());

//					pane.getTreeOutput().appendText("\n\n\n Pre-Pruning Parameters:\n" + "Max Depth: " + maxDepth + "\n"
//							+ "Min Samples Split: " + minSamplesSplit);
//					pane.getTreeOutput().appendText("\n\n Performing 5-Fold Cross-Validation...\n");
//					double crossValidationAccuracy = crossValidate(trainData, selectedLabel, 5);
//					pane.getTreeOutput().appendText(
//							"Cross-Validation Accuracy: " + String.format("%.2f", crossValidationAccuracy) + "%\n");

					Metrics metrics = evaluateMatricsTree(tree, testData, selectedLabel);
					pane.getTreeOutput().appendText(
							"\n Test Set Accuracy: " + String.format("%.2f", metrics.getAccuracy()) + "%\n");
					pane.getTreeOutput()
							.appendText(" Precision: " + String.format("%.2f", metrics.getPrecision()) + "\n");
					pane.getTreeOutput().appendText(" Recall: " + String.format("%.2f", metrics.getRecall()) + "\n");
					pane.getTreeOutput().appendText(" F-score: " + String.format("%.2f", metrics.getFScore()) + "\n");

//					if (metrics.getAccuracy() < crossValidationAccuracy - 5) {
//						pane.getTreeOutput().appendText(" Warning: The model might be overfitting.\n");
//					}
				} catch (Exception ex) {
					pane.getTreeOutput().setText(" Error: " + ex.getMessage());
				}
			}

			primaryStage.setScene(new Scene(pane.secondScreen(), 800, 600));
			primaryStage.show();
		});

	}

	


	private Metrics evaluateMatricsTree(Node tree, List<Map<String, String>> testData, String labelColumn) {
		int truePositives = 0;
		int falsePositives = 0;
		int falseNegatives = 0;
		int trueNegatives = 0;

		Set<String> uniqueLabels = new HashSet<>();
		for (Map<String, String> instance : testData) {
			uniqueLabels.add(instance.get(labelColumn));
		}

		Iterator<String> labelIterator = uniqueLabels.iterator();
		String positiveLabel = labelIterator.next();

		for (Map<String, String> instance : testData) {
			String actual = instance.get(labelColumn);
			String predicted = classify(tree, instance);

			if (actual.equals(predicted)) {
				if (actual.equals(positiveLabel)) {
					truePositives++;
				} else {
					trueNegatives++;
				}
			} else {
				if (predicted.equals(positiveLabel)) {
					falsePositives++;
				} else {
					falseNegatives++;
				}
			}
		}

		double accuracy = (double) (truePositives + trueNegatives) / testData.size() * 100;
		double precision = (double) truePositives / (truePositives + falsePositives);
		double recall = (double) truePositives / (truePositives + falseNegatives);
		double fScore = 2 * (precision * recall) / (precision + recall);

		return new Metrics(accuracy, precision, recall, fScore);
	}
	


	private String classify(Node tree, Map<String, String> instance) {
		if (tree.isLeaf()) {
			return tree.getLabel();
		}
		String attributeValue = instance.get(tree.getAttribute());
		Node child = tree.getChildren().get(attributeValue);
		if (child == null) {
			return "Unknown";
		}
		return classify(child, instance);
	}

	public static void main(String[] args) {
		launch(args);
	}
	
//	private double crossValidate(List<Map<String, String>> data, String labelColumn, int k) {
//	Collections.shuffle(data, new Random());
//	int foldSize = data.size() / k;
//	double totalAccuracy = 0.0;
//
//	for (int i = 0; i < k; i++) {
//
//		List<Map<String, String>> validationFold = new ArrayList<>(
//				data.subList(i * foldSize, Math.min((i + 1) * foldSize, data.size())));
//		List<Map<String, String>> trainingFold = new ArrayList<>(data);
//		trainingFold.removeAll(validationFold);
//
//		List<String> attributes = new ArrayList<>(trainingFold.get(0).keySet());
//		attributes.remove(labelColumn);
//
//		ID3TreeBuilder treeBuilder = new ID3TreeBuilder(maxDepth, minSamplesSplit);
//		Node tree = treeBuilder.buildTree(trainingFold, attributes, labelColumn);
//
//		double accuracy = evaluateTree(tree, validationFold, labelColumn);
//		totalAccuracy += accuracy;
//	}
//
//	return totalAccuracy / k;
//}
//
//private double evaluateTree(Node tree, List<Map<String, String>> testData, String labelColumn) {
//	int correct = 0;
//
//	for (Map<String, String> instance : testData) {
//		String actual = instance.get(labelColumn);
//		String predicted = classify(tree, instance);
//		if (actual.equals(predicted)) {
//			correct++;
//		}
//	}
//
//	return (double) correct / testData.size() * 100;
//}

}
