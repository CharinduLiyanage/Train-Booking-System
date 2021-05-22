package Denuwara_Manike;

import javafx.application.Application;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.Side;
import javafx.scene.Scene;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;

import java.time.LocalDate;

/**
 * This Class Creates the Graphical User Interface of the program.
 */
public class GUI extends Application {
    protected static final String[] trains = {"Colombo to Badulla", "Badulla to Colombo"};

    protected static String sceneName;
    protected static int maxQueueSelect;

    protected static Stage window;
    protected static Scene scene;
    protected static GridPane gridPane;
    protected static ChoiceBox<String> choiceBoxTrains;
    protected static Label titleLabel;
    protected static Label headingLabel, headingLabel2;
    protected static Button buttonAddToTQ;
    protected static Label space, space2, space3;
    protected static Label dateLabel;
    protected static Button menuButton;
    protected static Label maxSelectLabel;
    protected static Label selectCountLabel;
    protected static Label[] seatLabels = new Label[42];
    protected static Label labelEmpty, labelBoarded, labelUnbooked, labelInstructions;
    protected static TableView<Passenger> tableViewWaitingRoom;
    protected static TableView<Passenger> tableViewTrainQueue;
    protected static TableColumn<Passenger,String > seatNumberColumnTQ, passengerNameColumnTQ, passengerNICColumnTQ;
    protected static TableColumn<Passenger, String> seatNumberColumnWR, passengerNameColumnWR, passengerNICColumnWR;

    protected static TabPane mainTabPane;
    protected static TabPane secondQueueTabPane;
    protected static GridPane gridPaneTab;
    protected static TableView<SimulationPassenger> tableViewTrainQueueSim;
    protected static Label maxWaitLabel, minWaitLabel, avgWaitLabel, queueLengthLabel;
    protected static Tab queue1Tab, queue2Tab;
    protected static Tab simulation1StatTab, simulation2StatTab, overallStatTab;

    protected static BarChart<String,Number> barChartTime;
    protected static BarChart<String,Number> barChartLength;

    int selectedTrain = 0;


    public static void main(String[] args) {
        // Launching the JavaFX Application.
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {

        // Creating the window.
        window = primaryStage;
        window.setTitle("Denuwara Manike Train Queue");
        window.setMinHeight(700);
        window.setMinWidth(1000);

        // Creating the layout for the add view.
        gridPane = new GridPane();
        gridPane.setPadding(new Insets(5));
        gridPane.setVgap(10);
        gridPane.setHgap(10);
        gridPane.setAlignment(Pos.CENTER);

        // Creating the Title label.
        titleLabel = new Label();
        titleLabel.setStyle("-fx-font-family: 'Trebuchet MS', Helvetica, sans-serif; -fx-font-size: 24px;");
        titleLabel.setPadding(new Insets(10,0,20,0));
        GridPane.setConstraints(titleLabel, 0, 0, 6, 1);

        // Creating the Heading label 1.
        headingLabel = new Label();
        headingLabel.setStyle("-fx-font-family: 'Trebuchet MS', Helvetica, sans-serif; -fx-font-size: 18px;");
        headingLabel.setPadding(new Insets(5));
        GridPane.setConstraints(headingLabel, 0, 1, 5, 1);

        // Creating the Heading label 2.
        headingLabel2 = new Label();
        headingLabel2.setStyle("-fx-font-family: 'Trebuchet MS', Helvetica, sans-serif; -fx-font-size: 18px;");
        headingLabel2.setPadding(new Insets(5));
        GridPane.setConstraints(headingLabel2, 4, 1, 3, 1);


        // Creating Waiting room table----------------------------------------------------------------------------------
        tableViewWaitingRoom = new TableView<>();
        tableViewWaitingRoom.setMinSize(287, 390 );

        // Seat Number column
        seatNumberColumnWR = new TableColumn<>("Seat");
        seatNumberColumnWR.setResizable(false);
        seatNumberColumnWR.setStyle("-fx-alignment: CENTER;");
        seatNumberColumnWR.setCellValueFactory(new PropertyValueFactory<>("seatNumber"));

        // Passenger Name column
        passengerNameColumnWR = new TableColumn<>("Passenger Name");
        passengerNameColumnWR.setResizable(false);
        passengerNameColumnWR.setSortable(false);
        passengerNameColumnWR.setCellValueFactory(new PropertyValueFactory<>("passengerName"));

        // Passenger NIC column
        passengerNICColumnWR = new TableColumn<>("Passenger NIC");
        passengerNICColumnWR.setResizable(false);
        passengerNICColumnWR.setSortable(false);
        passengerNICColumnWR.setCellValueFactory(new PropertyValueFactory<>("passengerNIC"));

        // Setting waiting room table.

        tableViewWaitingRoom.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        tableViewWaitingRoom.getColumns().addAll(seatNumberColumnWR, passengerNameColumnWR, passengerNICColumnWR);
        tableViewWaitingRoom.getSortOrder().add(seatNumberColumnWR);
        tableViewWaitingRoom.setOnMouseReleased( e-> {
            // Updating the Number of selected label every time a passenger is selected.
            int noOfSelectedPassengers = tableViewWaitingRoom.getSelectionModel().getSelectedItems().size();
            selectCountLabel.setText("No selected \t:\t " + noOfSelectedPassengers);
        });


        // Creating Train Queue Table----------------------------------------------------------------------------------
        // Seat Number column
        seatNumberColumnTQ = new TableColumn<>("Seat");
        seatNumberColumnTQ.setMinWidth(seatNumberColumnWR.getMinWidth());
        seatNumberColumnTQ.setResizable(false);
        seatNumberColumnTQ.setStyle("-fx-alignment: CENTER;");
        seatNumberColumnTQ.setCellValueFactory(new PropertyValueFactory<>("seatNumber"));

        // Passenger Name column
        passengerNameColumnTQ = new TableColumn<>("Passenger Name");
        passengerNameColumnTQ.setMinWidth(passengerNameColumnWR.getMinWidth());
        passengerNameColumnTQ.setResizable(false);
        passengerNameColumnTQ.setSortable(false);
        passengerNameColumnTQ.setCellValueFactory(new PropertyValueFactory<>("passengerName"));

        // Passenger NIC column
        passengerNICColumnTQ = new TableColumn<>("Passenger NIC");
        passengerNICColumnTQ.setMinWidth(passengerNICColumnWR.getMinWidth());
        passengerNICColumnTQ.setResizable(false);
        passengerNICColumnTQ.setSortable(false);
        passengerNICColumnTQ.setCellValueFactory(new PropertyValueFactory<>("passengerNIC"));

        // Setting Train Queue Table.
        tableViewTrainQueue = new TableView<>();
        tableViewTrainQueue.setMinSize(tableViewWaitingRoom.getMinWidth(), tableViewWaitingRoom.getMinHeight());
        tableViewTrainQueue.getColumns().addAll(seatNumberColumnTQ, passengerNameColumnTQ, passengerNICColumnTQ);
        tableViewTrainQueue.getSortOrder().add(seatNumberColumnTQ);


        // Creating Button to add passengers to Train Queue.
        buttonAddToTQ = new Button("Add to Train Queue > > >");
        // Setting OnClickAction (Adding the Selected Passengers from Waiting Room to Train Queue).
        buttonAddToTQ.setOnAction(e -> {
            // Getting the all selected passengers from the waiting room.
            ObservableList<Passenger> selectedWaitingRoom;
            selectedWaitingRoom = tableViewWaitingRoom.getSelectionModel().getSelectedItems();

            // Making sure the button only works when there is passengers in the waiting room.
            if (!tableViewWaitingRoom.getItems().isEmpty()) {
                // Checking if user has selected any passenger.
                if (!selectedWaitingRoom.isEmpty()) {
                    // Checking if the selected amount is equal to the number that should be selected.
                    if (selectedWaitingRoom.size() == maxQueueSelect) {
                        for (Passenger passenger : selectedWaitingRoom) {
                            // Adding passengers to the Train Queue.
                            TrainStation.getTrainQueue().add(passenger, selectedTrain);
                            // Deleting passengers from the waiting Room.
                            TrainStation.getWaitingRoom()[selectedTrain][Methods.seatNumberConverter(passenger.getSeatNumber())] = null;
                        }
                        // Updating the GUI.
                        Methods.launchingGUI(sceneName, TrainStation.getWaitingRoom(), TrainStation.getTrainQueue(), selectedTrain, 0);
                    }
                    // If the passengers selected is not the correct amount.
                    else {
                        AlertBox.display("ERROR - Adding to Train Queue", "You can only select maximum of " + maxQueueSelect + " passenger/s.", "OK");
                    }
                }
                // If no passenger has been selected.
                else {
                    AlertBox.display("ERROR - None Selected", "You have to select a Passenger", "OK");
                }
            }
        });
        GridPane.setConstraints(buttonAddToTQ, 2, 8, 4, 1);

        // Creating Labels to space out the GUI-------------------------------------------------------------------------
        space = new Label();
        space.setMinSize(50, 70.0);
        GridPane.setConstraints(space, 3, 1);

        space2 = new Label();
        space2.setMinSize(50, 70.0);
        GridPane.setConstraints(space2, 8, 8);

        space3 = new Label();
        space3.setMinSize(50, 70.0);
        GridPane.setConstraints(space3, 9, 4);


        // Creating a Dropdown menu for selecting the date.
        choiceBoxTrains = new ChoiceBox<>();
        for (String train : trains) {
            choiceBoxTrains.getItems().add(train);
        }
        choiceBoxTrains.setValue(trains[0]);
        choiceBoxTrains.setStyle("-fx-font-family: 'Trebuchet MS', Helvetica, sans-serif; -fx-font-size: 12px;");
        choiceBoxTrains.setPrefWidth(160);
        choiceBoxTrains.setPadding(new Insets(5));
        choiceBoxTrains.getSelectionModel().selectedItemProperty().addListener( (v , oldValue, newValue) -> {
            // Setting the selected train number.
            for (int train = 0; train < trains.length; train++) {
                if (trains[train].equals(newValue)) {
                    selectedTrain = train;
                }
            }

            // Getting the tab that is selected used for simulation and view train queue scenes.
            int tab = mainTabPane.getSelectionModel().getSelectedIndex();
            // Updating the GUI accordingly.
            if (sceneName.equalsIgnoreCase("SimTQ")) {
                int subTab = secondQueueTabPane.getSelectionModel().getSelectedIndex();
                Methods.launchingSimulationGUI("SimTQ", TrainStation.getSimulationTrainQueues(), tab, subTab, selectedTrain);
            } else {
                Methods.launchingGUI(sceneName, TrainStation.getWaitingRoom(), TrainStation.getTrainQueue(), selectedTrain, tab);
            }
        });


        // Creating Date label.
        dateLabel = new Label("Date : " + LocalDate.now().toString());
        dateLabel.setStyle("-fx-border-color: gray; -fx-border-width: 1px; -fx-font-family: 'Trebuchet MS', Helvetica, sans-serif; -fx-font-size: 13px; -fx-border-radius: 5px;");
        dateLabel.setPrefWidth(160);
        dateLabel.setPadding(new Insets(5));

        // Creating select limit label from the waiting room.
        maxSelectLabel = new Label();
        maxSelectLabel.setStyle("-fx-border-color: gray; -fx-border-width: 1px; -fx-font-family: 'Trebuchet MS', Helvetica, sans-serif; -fx-font-size: 13px; -fx-border-radius: 5px;");
        maxSelectLabel.setPrefWidth(160);
        maxSelectLabel.setPadding(new Insets(5));
        GridPane.setConstraints(maxSelectLabel, 9, 5, 2, 1);

        // Creating the number of selected labels form the Train Queue.
        selectCountLabel = new Label();
        selectCountLabel.setStyle("-fx-border-color: gray; -fx-border-width: 1px; -fx-font-family: 'Trebuchet MS', Helvetica, sans-serif; -fx-font-size: 13px; -fx-border-radius: 5px;");
        selectCountLabel.setPrefWidth(160);
        selectCountLabel.setPadding(new Insets(5));
        GridPane.setConstraints(selectCountLabel, 9, 6, 2, 1);


        // Creating the button to go back to console menu from GUI.
        menuButton = new Button("Console Menu");
        menuButton.setPadding(new Insets(5));
        menuButton.setStyle("-fx-font-family: 'Trebuchet MS', Helvetica, sans-serif; -fx-font-size: 12px;");
        menuButton.setPrefWidth(160);
        menuButton.setPadding(new Insets(5));
        menuButton.setOnAction(e -> {
            window.setIconified(true);
            Methods.mainMenu();
        });
        GridPane.setConstraints(menuButton, 9, 8, 2, 1);

        // Creating seat labels for seat view.
        String[] letters = {"A", "B", "C", "D", "E", "F", "G"};
        for (String letter : letters) {
            for (int num = 1; num < 7; num++) {
                String seatNumberText = letter + num;
                int seatNumber = Methods.seatNumberConverter(seatNumberText);
                seatLabels[seatNumber] = new Label(seatNumberText);
                seatLabels[seatNumber].setPadding(new Insets(10));
                seatLabels[seatNumber].setContentDisplay(ContentDisplay.RIGHT);
                seatLabels[seatNumber].setPrefWidth(89.0);
                seatLabels[seatNumber].setPrefHeight(70.0);
                seatLabels[seatNumber].setStyle("-fx-background-color: lightgray; -fx-color: black;");
                int row = seatNumber / 6;
                int column = seatNumber % 6;
                if (column < 3) {
                    GridPane.setConstraints(seatLabels[seatNumber], column, row+1);
                } else {
                    GridPane.setConstraints(seatLabels[seatNumber], column+1, row+1);
                }
            }
        }

        // Getting Images from file.
        Image imageEmpty = new Image(getClass().getResourceAsStream("seatT2.png"));
        Image imageBoarded = new Image(getClass().getResourceAsStream("seatA1.png"));
        Image imageUnbooked = new Image(getClass().getResourceAsStream("seatN1.png"));

        // Creating Available example label.
        labelUnbooked = new Label("    Unbooked", new ImageView(imageUnbooked));
        labelUnbooked.setPrefWidth(160);
        labelUnbooked.setPadding(new Insets(10));
        labelUnbooked.setStyle("-fx-border-color: gray; -fx-border-width: 1px; -fx-font-family: 'Trebuchet MS', Helvetica, sans-serif; -fx-font-size: 13px; -fx-border-radius: 5px;");
        GridPane.setConstraints(labelUnbooked, 9, 3, 2, 1);

        // Creating Available example label.
        labelEmpty = new Label("    Empty", new ImageView(imageEmpty));
        labelEmpty.setPrefWidth(160);
        labelEmpty.setPadding(new Insets(10));
        labelEmpty.setStyle("-fx-border-color: gray; -fx-border-width: 1px; -fx-font-family: 'Trebuchet MS', Helvetica, sans-serif; -fx-font-size: 13px; -fx-border-radius: 5px;");
        GridPane.setConstraints(labelEmpty, 9, 4, 2, 1);

        // Creating Taken example Label.
        labelBoarded = new Label("    Boarded", new ImageView(imageBoarded));
        labelBoarded.setPrefWidth(160);
        labelBoarded.setPadding(new Insets(10));
        labelBoarded.setStyle("-fx-border-color: gray; -fx-border-width: 1px; -fx-font-family: 'Trebuchet MS', Helvetica, sans-serif; -fx-font-size: 13px; -fx-border-radius: 5px;");
        GridPane.setConstraints(labelBoarded, 9, 5, 2, 1);

        // Creating Instruction Label
        labelInstructions = new Label("* Hover Over Boarded Seats for Information.");
        labelInstructions.setPrefWidth(160);
        labelInstructions.setPadding(new Insets(10));
        labelInstructions.setWrapText(true);
        labelInstructions.setTextAlignment(TextAlignment.CENTER);
        labelInstructions.setStyle("-fx-font-family: 'Trebuchet MS', Helvetica, sans-serif; -fx-font-size: 13px; -fx-font-weight: bold");
        GridPane.setConstraints(labelInstructions, 9, 6, 2, 1);


        // Creating Simulation Result Table-----------------------------------------------------------------------------
        // Seat Group Number column
        TableColumn<SimulationPassenger, String> groupNumberColumnSim = new TableColumn<>("Group");
        groupNumberColumnSim.setPrefWidth(65);
        groupNumberColumnSim.setResizable(false);
        groupNumberColumnSim.setStyle("-fx-alignment: CENTER;");
        groupNumberColumnSim.setCellValueFactory(new PropertyValueFactory<>("groupNumber"));

        // Seat Number column
        TableColumn<SimulationPassenger, String> seatNumberColumnSim = new TableColumn<>("Seat");
        seatNumberColumnSim.setPrefWidth(65);
        seatNumberColumnSim.setResizable(false);
        seatNumberColumnSim.setStyle("-fx-alignment: CENTER;");
        seatNumberColumnSim.setCellValueFactory(new PropertyValueFactory<>("seatNumber"));

        // Passenger Name column
        TableColumn<SimulationPassenger, Double> passengerNameColumnSim = new TableColumn<>("Passenger Name");
        passengerNameColumnSim.setMinWidth(passengerNameColumnWR.getMinWidth());
        passengerNameColumnSim.setResizable(false);
        passengerNameColumnSim.setSortable(false);
        passengerNameColumnSim.setPrefWidth(140);
        passengerNameColumnSim.setCellValueFactory(new PropertyValueFactory<>("passengerName"));

        // Passenger NIC column
        TableColumn<SimulationPassenger, String> passengerNICColumnSim = new TableColumn<>("Passenger NIC");
        passengerNICColumnSim.setMinWidth(passengerNICColumnWR.getMinWidth());
        passengerNICColumnSim.setResizable(false);
        passengerNICColumnSim.setSortable(false);
        passengerNICColumnSim.setPrefWidth(105);
        passengerNICColumnSim.setCellValueFactory(new PropertyValueFactory<>("passengerNIC"));


        // Seat Queue Length column
        TableColumn<SimulationPassenger, String> queueLengthColumnSim = new TableColumn<>("Group size");
        queueLengthColumnSim.setResizable(false);
        queueLengthColumnSim.setSortable(false);
        queueLengthColumnSim.setPrefWidth(79);
        queueLengthColumnSim.setStyle("-fx-alignment: CENTER;");
        queueLengthColumnSim.setCellValueFactory(new PropertyValueFactory<>("queueLength"));

        // Seat Queue Length column
        TableColumn<SimulationPassenger, String> personalTimeColumnSim = new TableColumn<>("Personal wait");
        personalTimeColumnSim.setResizable(false);
        personalTimeColumnSim.setSortable(false);
        personalTimeColumnSim.setPrefWidth(100);
        personalTimeColumnSim.setStyle("-fx-alignment: CENTER;");
        personalTimeColumnSim.setCellValueFactory(new PropertyValueFactory<>("secondsInQueue"));

        // Seat Queue Length column
        TableColumn<SimulationPassenger, String> totalWaitingTimeColumnSim = new TableColumn<>("\u03A3 Waiting Time");
        totalWaitingTimeColumnSim.setResizable(false);
        totalWaitingTimeColumnSim.setSortable(false);
        totalWaitingTimeColumnSim.setPrefWidth(100);
        totalWaitingTimeColumnSim.setStyle("-fx-alignment: CENTER;");
        totalWaitingTimeColumnSim.setCellValueFactory(new PropertyValueFactory<>("waitingTime"));

        // Setting Simulation Result Table.
        tableViewTrainQueueSim = new TableView<>();
        tableViewTrainQueueSim.setMinSize(89*6+10*7+50, 70*6+10*6);
        tableViewTrainQueueSim.getColumns().addAll(groupNumberColumnSim, seatNumberColumnSim, passengerNameColumnSim, passengerNICColumnSim, queueLengthColumnSim, personalTimeColumnSim, totalWaitingTimeColumnSim);
        tableViewTrainQueueSim.getSortOrder().addAll(groupNumberColumnSim, seatNumberColumnSim);
        GridPane.setConstraints(tableViewTrainQueueSim, 0, 1, 8, 8);

        // Label to show the maximum waiting time for the simulation.
        maxWaitLabel = new Label();
        maxWaitLabel.setPrefWidth(160);
        maxWaitLabel.setPadding(new Insets(10));
        maxWaitLabel.setStyle("-fx-border-color: gray; -fx-border-width: 1px; -fx-font-family: 'Trebuchet MS', Helvetica, sans-serif; -fx-font-size: 13px; -fx-border-radius: 5px;");
        GridPane.setConstraints(maxWaitLabel, 9, 4, 2, 1);

        // Label to show the minimum waiting time for the simulation.
        minWaitLabel = new Label();
        minWaitLabel.setPrefWidth(160);
        minWaitLabel.setPadding(new Insets(10));
        minWaitLabel.setStyle("-fx-border-color: gray; -fx-border-width: 1px; -fx-font-family: 'Trebuchet MS', Helvetica, sans-serif; -fx-font-size: 13px; -fx-border-radius: 5px;");
        GridPane.setConstraints(minWaitLabel, 9, 5, 2, 1);

        // Label to show the average waiting time for the simulation.
        avgWaitLabel = new Label();
        avgWaitLabel.setPrefWidth(160);
        avgWaitLabel.setPadding(new Insets(10));
        avgWaitLabel.setStyle("-fx-border-color: gray; -fx-border-width: 1px; -fx-font-family: 'Trebuchet MS', Helvetica, sans-serif; -fx-font-size: 13px; -fx-border-radius: 5px;");
        GridPane.setConstraints(avgWaitLabel, 9, 6, 2, 1);

        // Label to shw the minimum queue length for the simulation.
        queueLengthLabel = new Label();
        queueLengthLabel.setPrefWidth(160);
        queueLengthLabel.setPadding(new Insets(10));
        queueLengthLabel.setStyle("-fx-border-color: gray; -fx-border-width: 1px; -fx-font-family: 'Trebuchet MS', Helvetica, sans-serif; -fx-font-size: 13px; -fx-border-radius: 5px;");
        GridPane.setConstraints(queueLengthLabel, 9, 7, 2, 1);

        // Creating TabPane Layout.
        mainTabPane = new TabPane();
        secondQueueTabPane = new TabPane();
        secondQueueTabPane.setSide(Side.LEFT);

        // Creating the basic tabs.
        simulation1StatTab = new Tab();
        simulation2StatTab = new Tab();
        overallStatTab = new Tab();

        // Creating the sub-tabs for the second tab in simulation.
        queue1Tab = new Tab("Queue 1");
        queue2Tab = new Tab("Queue 2");

        // Setting the TabPane.
        secondQueueTabPane.getTabs().addAll(queue1Tab, queue2Tab);
        secondQueueTabPane.setTabClosingPolicy(TabPane.TabClosingPolicy.UNAVAILABLE);
        mainTabPane.getTabs().addAll(simulation1StatTab, simulation2StatTab, overallStatTab);
        mainTabPane.setTabClosingPolicy(TabPane.TabClosingPolicy.UNAVAILABLE);

        // Creating the grid pane layout foe tabs as a layout.
        gridPaneTab = new GridPane();
        gridPaneTab.setPadding(new Insets(5));
        gridPaneTab.setVgap(10);
        gridPaneTab.setHgap(10);
        gridPaneTab.setAlignment(Pos.CENTER);

        // Updating the GUI when tabs user switches between tabs (Main TabPane).
        mainTabPane.getSelectionModel().selectedIndexProperty().addListener( (v, oldValue, newValue) -> {
            int tab = (int) newValue;
            if (sceneName.equalsIgnoreCase("SimTQ")) {
                Methods.launchingSimulationGUI(sceneName, TrainStation.getSimulationTrainQueues(), tab, 0, selectedTrain);
            } else {
                Methods.launchingGUI(sceneName, TrainStation.getWaitingRoom(), TrainStation.getTrainQueue(), selectedTrain, tab);
            }
        });

        // Updating the GUI when tabs user switches between sub-tabs Simulation Second tab (sub tabPane).
        secondQueueTabPane.getSelectionModel().selectedIndexProperty().addListener( (v, oldValue, newValue) -> {
            int subTab = (int) newValue;
            Methods.launchingSimulationGUI(sceneName, TrainStation.getSimulationTrainQueues(), 1, subTab, selectedTrain);
        });


        // Creating Chart of waiting time for the simulation------------------------------------------------------------
        // Creating axises.
        CategoryAxis xAxisTime = new CategoryAxis();
        NumberAxis yAxisTime = new NumberAxis();
        barChartTime = new BarChart<>(xAxisTime,yAxisTime);

        xAxisTime.setLabel("Simulation");
        yAxisTime.setLabel("Seconds");

        // adding Graph data.
        XYChart.Series Simulation1Time = new XYChart.Series();
        Simulation1Time.setName("Simulation 1");

        XYChart.Series simulation2Time = new XYChart.Series();
        simulation2Time.setName("Simulation 2");

        // Setting the Graph.
        barChartTime.getData().addAll(Simulation1Time, simulation2Time);
        barChartTime.setPrefSize(287, 390 );
        barChartTime.setStyle("-fx-border-color: gray; -fx-border-width: 1px;");
        GridPane.setConstraints(barChartTime, 0, 2, 3, 7);


        // Creating Chart of Queue Length for the simulation------------------------------------------------------------
        // Creating axises.
        CategoryAxis xAxisLength = new CategoryAxis();
        NumberAxis yAxisLength = new NumberAxis();
        barChartLength = new BarChart<>(xAxisLength,yAxisLength);

        xAxisLength.setLabel("Simulation");
        yAxisLength.setLabel("No of Passengers");

        // adding Graph data.
        XYChart.Series simulation1Length = new XYChart.Series();
        simulation1Length.setName("Simulation 1");

        XYChart.Series simulation2Length = new XYChart.Series();
        simulation2Length.setName("Simulation 2");

        // Setting the Graph.
        barChartLength.getData().addAll(simulation1Length, simulation2Length);
        barChartLength.setPrefSize(287, 390 );
        barChartLength.setStyle("-fx-border-color: gray; -fx-border-width: 1px;");
        GridPane.setConstraints(barChartLength, 4, 2, 3, 7);


        // Setting controlled closing to the GUI.
        window.setOnCloseRequest(e -> {
            e.consume();
            Methods.quitingProgram(false);
        });

        // Setting the Window
        scene = new Scene(gridPane, 1000, 700);
        window.setScene(scene);
        window.show();

        // Launching the console menu.
        window.setIconified(true);
        Methods.mainMenu();


    }
}
