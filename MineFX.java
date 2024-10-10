package mines;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class MineFX extends Application {

	private BorderPane rootPane;
	private GridPane gridPane;
	// The Mines class that handles the game logic
	private Mines mines;
	private StackPane MyStackPane;
	private MenuBar menuBar;
	// Size of each cell in the grid
	private static final int BOX_SIZE = 70;

	@Override
	public void start(Stage primaryStage) {
		// Initialize the root pane
		rootPane = new BorderPane();

		// Initialize the Mines object with desired dimensions
		int height = 10;
		int width = 10;
		int numMines = 10;
		mines = new Mines(height, width, numMines);

		// Initialize the StackPane
		MyStackPane = new StackPane();

		// Set up the controls
		setupControls();
		startGame(height, width, numMines);

		// Set up the scene and stage
		Scene scene = new Scene(rootPane);
		primaryStage.setScene(scene);
		primaryStage.setTitle("Minesweeper");

		// Calculate the total width and height of the grid plus the menu bar
		double totalWidth = width * BOX_SIZE - menuBar.getPrefHeight();
		double totalHeight = height * BOX_SIZE + menuBar.getPrefHeight();

		// Set these values as the minimum width and height of the Stage
		primaryStage.setMinWidth(totalWidth);
		primaryStage.setMinHeight(totalHeight);
		primaryStage.setMaxWidth(totalWidth);
		primaryStage.setMaxHeight(totalHeight);

		// Add a listener to resize buttons when the window size changes
		scene.widthProperty().addListener((observable, oldValue, newValue) -> updateButtonSizes());
		scene.heightProperty().addListener((observable, oldValue, newValue) -> updateButtonSizes());

		primaryStage.show();

	}

	// Method to update button sizes based on current scene dimensions
	private void updateButtonSizes() {
		// Ensure mines is initialized
		if (mines != null) {
			// Divide by current width and height of grid
			double buttonWidth = rootPane.getWidth() / mines.getWidth();
			double buttonHeight = (rootPane.getHeight() - menuBar.getHeight()) / mines.getHeight();
			// Ensure buttons are square
			buttonWidth = Math.min(buttonWidth, buttonHeight);
			buttonHeight = buttonWidth;
			for (Node node : gridPane.getChildren()) {
				if (node instanceof CellButton) {
					((CellButton) node).setMinSize(buttonWidth, buttonHeight);
				}
			}
		}
	}

	// Method to set up the menu bar and controls
	private void setupControls() {

		VBox VPane = new VBox(10);
		VPane.setPadding(new Insets(0));
		VPane.setStyle("-fx-alignment: center-left;");

		// Create a MenuBar
		this.menuBar = new MenuBar();
		menuBar.setPrefHeight(20);

		// Create a Menu
		Menu gameMenu = new Menu("Game");
		Menu difficultyMenu = new Menu("Difficulty");

		// Create MenuItems
		MenuItem easy = new MenuItem("Easy");
		MenuItem medium = new MenuItem("Medium");
		MenuItem hard = new MenuItem("Hard");
		MenuItem restartGame = new MenuItem("Restart Game");

		// Difficulty Settings
		easy.setOnAction(e -> startGame(10, 10, 10));
		medium.setOnAction(e -> startGame(15, 15, 20));
		hard.setOnAction(e -> startGame(20, 20, 30));

		// Restart Game
		restartGame.setOnAction(e -> startGame(mines.getHeight(), mines.getWidth(), mines.getNumMines()));

		// Add MenuItems to the Menu
		difficultyMenu.getItems().addAll(easy, medium, hard);
		gameMenu.getItems().addAll(restartGame);

		// Add Menu to the MenuBar
		menuBar.getMenus().addAll(gameMenu, difficultyMenu);

		// Add MenuBar to the VBox
		VPane.getChildren().add(menuBar);

		// Add the setup pane to the left side of the BorderPane
		rootPane.setTop(VPane);
	}

	private void startGame(int height, int width, int numMines) {
		// Clear the previous grid
		MyStackPane.getChildren().clear();

		// Initialize the Mines object with the desired dimensions and number of mines
		mines = new Mines(height, width, numMines);

		// Create the game grid
		createGameGrid(height, width);

		// Add the gridPane to the center of the BorderPane
		rootPane.setCenter(MyStackPane);

		// Update button sizes based on current scene dimensions
		updateButtonSizes();
	}

	// Create the game grid based on the height and width of the Mines object
	public void createGameGrid(int height, int width) {
		gridPane = new GridPane();
		// Set the padding and alignment of the grid
		for (int row = 0; row < height; row++) {
			for (int col = 0; col < width; col++) {
				// Create a new CellButton for each cell in the grid
				CellButton cellButton = new CellButton(row, col);
				cellButton.setOnMouseClicked(event -> handleCellClick(event, cellButton));
				gridPane.add(cellButton, col, row);
			}
		}

		// Clear the previous grid and add the new one
		MyStackPane.getChildren().clear();
		MyStackPane.getChildren().add(gridPane);
	}

	// Update the grid display based on the current state of the Mines object
	private void updateGridDisplay() {
		for (Node node : gridPane.getChildren()) {
			if (node instanceof CellButton) {
				CellButton cellButton = (CellButton) node;
				int row = cellButton.getRow();
				int col = cellButton.getCol();
				cellButton.setText(mines.get(row, col)); // Display the cell's current state

			}

		}
	}

	// Custom Button class to store the row and column of the cell it represents
	public class CellButton extends Button {
		private int row;
		private int col;

		public CellButton(int row, int col) {
			this.row = row;
			this.col = col;
			this.setStyle("-fx-font-size: 20;");

		}

		public int getRow() {
			return row;
		}

		public int getCol() {
			return col;
		}
	}

	// Handle the mouse click event on a cell button
	private void handleCellClick(MouseEvent event, CellButton cellButton) {
		int row = cellButton.getRow();
		int col = cellButton.getCol();

		if (event.getButton() == MouseButton.PRIMARY) {
			// Handle left-click: open the cell
			boolean result = mines.open(row, col);
			if (!result) {
				// If a mine was clicked, reveal all mines
				mines.setShowAll(true);
			}
		} else if (event.getButton() == MouseButton.SECONDARY) {
			// Handle right-click: toggle flag
			mines.toggleFlag(row, col);
		}

		// Update the grid display to reflect the game state
		updateGridDisplay();
	}

	public static void main(String[] args) {
		launch(args);
	}
}
