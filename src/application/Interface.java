package application;

import javafx.animation.ScaleTransition;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.util.Duration;

public class Interface {

	private TextArea treeOutput = new TextArea();
	private ComboBox<String> labelSelector = new ComboBox<>();
	private Button buildTreeButton;
	private Button chooseFileButton;
	VBox vb = new VBox(20);

	public Interface() {

		ImageView fileImg = new ImageView("excel.png");
		fileImg.setFitWidth(60);
		fileImg.setFitHeight(60);
		chooseFileButton = new Button("Choose CSV File", fileImg);
		chooseFileButton.setStyle(
				"-fx-border-color: transparent; -fx-background-color: transparent;-fx-font-weight: bold;-fx-font-family: 'Times New Roman';-fx-font-size: 45px;");
		scale(chooseFileButton);
		treeOutput.setStyle(
				"-fx-fill-color:#06402b;-fx-background-color:#06402b ;-fx-text-fill: #18392B;-fx-font-weight: bold;-fx-font-family: 'Times New Roman';-fx-control-inner-background: #f4fdf4");
	
		treeOutput.setEditable(false);
		labelSelector.setDisable(true);
		labelSelector.setStyle(
				"-fx-font-size: 20px;-fx-background-color: #fff;-fx-border-color: #ccc;-fx-border-width: 1px;-fx-border-radius: 8px;-fx-background-radius: 8px;-fx-pref-width: 300px;-fx-pref-height: 30px;"
						+ "-fx-font-weight: bold;-fx-font-family: 'Times New Roman';-fx-background-color:#c8f7c8;");

		ImageView treeImg = new ImageView("decision-tree.png");
		treeImg.setFitWidth(60);
		treeImg.setFitHeight(60);
		buildTreeButton = new Button("Build Tree", treeImg);
		buildTreeButton.setStyle(
				"-fx-border-color: transparent; -fx-background-color: transparent;-fx-font-weight: bold;-fx-font-family: 'Times New Roman';-fx-font-size: 45px;");
		buildTreeButton.setDisable(true);
		scale(buildTreeButton);
		vb.getChildren().addAll(chooseFileButton, labelSelector, buildTreeButton);

		
	}

	public BorderPane firstScreen() {
		BorderPane bp = new BorderPane();
		bp.setCenter(vb);
		bp.setStyle("-fx-background-color:#e9fce9");
		vb.setAlignment(Pos.CENTER);
		return bp;

	}
	
	public BorderPane secondScreen() {
		 BorderPane bp = new BorderPane();
		 bp.setMargin(treeOutput, new Insets(20, 20, 20, 20));
			bp.setCenter(treeOutput);
		return bp;
		 
		
	}

	public TextArea getTreeOutput() {
		return treeOutput;
	}

	public void setTreeOutput(TextArea treeOutput) {
		this.treeOutput = treeOutput;
	}

	

	public ComboBox<String> getLabelSelector() {
		return labelSelector;
	}

	public void setLabelSelector(ComboBox<String> labelSelector) {
		this.labelSelector = labelSelector;
	}

	public Button getBuildTreeButton() {
		return buildTreeButton;
	}

	public void setBuildTreeButton(Button buildTreeButton) {
		this.buildTreeButton = buildTreeButton;
	}

	public Button getChooseFileButton() {
		return chooseFileButton;
	}

	public void setChooseFileButton(Button chooseFileButton) {
		this.chooseFileButton = chooseFileButton;
	}

	private void scale(Button button) {
		ScaleTransition scaleUp = new ScaleTransition(Duration.millis(200), button);
		scaleUp.setToX(1.1);
		scaleUp.setToY(1.1);
		ScaleTransition scaleDown = new ScaleTransition(Duration.millis(200), button);
		scaleDown.setToX(1.0);
		scaleDown.setToY(1.0);
		button.setOnMouseEntered(e -> scaleUp.play());
		button.setOnMouseExited(e -> scaleDown.play());
	}

}
