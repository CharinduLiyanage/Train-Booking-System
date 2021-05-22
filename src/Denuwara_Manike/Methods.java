package Denuwara_Manike;

import com.mongodb.MongoClient;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import org.bson.Document;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.text.DecimalFormat;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.*;

/**
 * This Class contains all the Methods to Run the code.
 */
public class Methods {

    private static final Image imageAvailable = new Image(Methods.class.getResourceAsStream("seatT2.png"));
    private static final Image imageTaken = new Image(Methods.class.getResourceAsStream("seatA1.png"));
    private static final Image imageBlank = new Image(Methods.class.getResourceAsStream("seatN1.png"));
    private static final DecimalFormat df2 = new DecimalFormat("#.##");

    /**
     * Method to launch the Main Menu if MongoDB didn't execute automatically.
     * @param args System Arguments.
     */
    protected static void launchingMainMenu(String[] args) {
        Scanner sc = new Scanner(System.in);
        System.out.print("\n\nSelect an option from the Menu.\n" +
                "\tC :     To CONTINUE the Program (Run MongoDB MANUALLY beforeHand) \n" +
                "\tQ :     To Quit the program\n" +
                "\n\nEnter option letter to proceed :   ");
        String text = formattingWhitespace(sc.nextLine());
        if (text.equalsIgnoreCase("c")) {
            // Reading the Database from Train Booking to get the Waiting room.
            TrainStation.setWaitingRoom();
            // Running the program.
            GUI.main(args);
        } else if (text.equalsIgnoreCase("Q")){
            System.out.println("Exiting program.");
        } else {
            System.out.println("Error - Invalid Input.\n");
            launchingMainMenu(args);
        }
    }

    /**
     * Main Console Menu of the Program.
     */
    public static void mainMenu(){
        // Creating a scanner to get console inputs.
        Scanner sc = new Scanner(System.in);

        // Getting user selection for from the main menu.
        System.out.print("\n\nSelect an option from the Menu.\n" +
                "\tA : \tTo ADD a passenger to Train Queue\n" +
                "\tV : \tTo VIEW the Train Queue\n" +
                "\tD : \tTo DELETE passenger from Train Queue\n" +
                "\tS : \tTo SAVE Train Queue\n" +
                "\tL : \tTo LOAD Train Queue\n" +
                "\tR : \tTo RUN simulation\n" +
                "\tQ : \tTo QUIT the Program\n" +
                "\n\nEnter option letter to proceed :    ");
        String text = formattingWhitespace(sc.nextLine());

        // If user selected to add Passengers to the TrainQueue.
        if (text.equalsIgnoreCase("A")) {
            System.out.println("Adding passenger...");
            // Setting default train before Launching the GUI.
            GUI.choiceBoxTrains.setValue(GUI.trains[0]);
            launchingGUI("addTQ", TrainStation.getWaitingRoom(), TrainStation.getTrainQueue(), 0, 0);
        }
        // If user selected to view the Train Queue.
        else if (text.equalsIgnoreCase("V")) {
            System.out.println("Viewing Train Queue...");
            // Setting default train before Launching the GUI.
            GUI.choiceBoxTrains.setValue(GUI.trains[0]);
            launchingGUI("viewTQ", TrainStation.getWaitingRoom(), TrainStation.getTrainQueue(), 0, 0);
        }
        // If the user selected to Delete a passenger form thr Train Queue.
        else if (text.equalsIgnoreCase("D")) {
            System.out.println("Deleting passenger...");
            deletePassenger(TrainStation.getWaitingRoom(), TrainStation.getTrainQueue());
            mainMenu();
        }
        // If the user selected to save the Train Queue.
        else if (text.equalsIgnoreCase("S")) {
            System.out.println("Saving Train Queue...");
            saveTQ(TrainStation.getTrainQueue());
            mainMenu();
        }
        // If the user selected tho load the saved Train Queue.
        else if (text.equalsIgnoreCase("L")) {
            System.out.println("Loading Train Queue...");
            TrainStation.setTrainQueue(readTQ());
            mainMenu();
        }
        // If the User selected to run the simulation.
        else if (text.equalsIgnoreCase("R")) {
            System.out.println("Running Simulation...");
            runningSimulation();
        }
        // If the user selected to Quit the program.
        else if (text.equalsIgnoreCase("Q")) {
            quitingProgram(true);
        }
        // When the user input is invalid.
        else {
            System.out.println("Invalid Input.");
            mainMenu();
        }
    }

    /**
     * Method that launches GUI for Adding Passengers and View Train Queue.
     * @param sceneName The name of the scene.
     * @param waitingRoom WaitingRoom from the TrainStation.
     * @param trainQueue TrainQueue From the TrainStation.
     * @param train Train Number.
     * @param tab Tab which is open in the View Train Queue scene.
     */
    protected static void launchingGUI(String sceneName, Passenger[][] waitingRoom, PassengerQueue trainQueue, int train, int tab) {
        GUI.sceneName = sceneName;
        GUI.gridPane.getChildren().clear();

        GridPane.setConstraints(GUI.space2, 8, 8);
        GridPane.setConstraints(GUI.menuButton, 9, 8);

        // If  Adding to the Train Queue scene is selected.
        if (sceneName.equalsIgnoreCase("addTQ")) {
            GUI.scene.setRoot(GUI.gridPane);
            GUI.titleLabel.setText("A D D   P A S S E N G E R S...");

            GUI.headingLabel.setText("Waiting Room");
            GUI.headingLabel2.setText("Train Queue");

            GridPane.setConstraints(GUI.choiceBoxTrains, 9, 2, 2,1);
            GridPane.setConstraints(GUI.dateLabel, 9, 3, 2,1);

            // Creating the number that should be selected.
            GUI.maxQueueSelect = creatingMaxWQSelect(train, TrainStation.getWaitingRoom());
            GUI.maxSelectLabel.setText("No to be added \t:\t " + GUI.maxQueueSelect);

            GUI.selectCountLabel.setText("No selected \t:\t " + 0);

            // Updating The Waiting Room Table.
            GUI.tableViewWaitingRoom.getItems().clear();

            // Setting Column Sizes.
            GUI.seatNumberColumnWR.setPrefWidth(57);
            GUI.passengerNameColumnWR.setPrefWidth(120);
            GUI.passengerNICColumnWR.setPrefWidth(110);

            for (Passenger passenger : TrainStation.getWaitingRoom()[train]) {
                if (passenger != null) {
                    GUI.tableViewWaitingRoom.getItems().add(passenger);
                }
            }
            GUI.tableViewWaitingRoom.getSortOrder().add(GUI.seatNumberColumnWR);
            GUI.tableViewWaitingRoom.setMinSize(287, 390 );
            GridPane.setConstraints(GUI.tableViewWaitingRoom, 0, 2, 3, 6);

            // Updating The Train Queue Table.
            GUI.tableViewTrainQueue.getItems().clear();

            // Setting Column Sizes.
            GUI.seatNumberColumnTQ.setPrefWidth(57);
            GUI.passengerNameColumnTQ.setPrefWidth(120);
            GUI.passengerNICColumnTQ.setPrefWidth(110);

            for (Passenger passenger : TrainStation.getTrainQueue().getQueueArray(train)) {
                GUI.tableViewTrainQueue.getItems().add(passenger);
            }
            GUI.tableViewTrainQueue.getSortOrder().add(GUI.seatNumberColumnTQ);
            GUI.tableViewTrainQueue.setMinSize(287, 390 );
            GridPane.setConstraints(GUI.tableViewTrainQueue, 4, 2, 3, 6);

            // Adding all elements to the layout.
            GUI.gridPane.getChildren().addAll(GUI.tableViewWaitingRoom, GUI.tableViewTrainQueue, GUI.choiceBoxTrains, GUI.titleLabel, GUI.headingLabel, GUI.headingLabel2, GUI.buttonAddToTQ, GUI.space, GUI.space2, GUI.dateLabel, GUI.menuButton, GUI.maxSelectLabel, GUI.selectCountLabel, GUI.space3);

        }
        // If View Train Queue scene is selected.
        else if (sceneName.equalsIgnoreCase("viewTQ")) {
            GUI.scene.setRoot(GUI.mainTabPane);
            GUI.gridPaneTab.getChildren().clear();

            // Setting Tabs.
            GUI.mainTabPane.getTabs().get(0).setContent(null);
            GUI.mainTabPane.getTabs().get(1).setContent(null);
            GUI.mainTabPane.getTabs().get(2).setContent(null);

            GUI.mainTabPane.getTabs().get(0).setText("Seat View");
            GUI.mainTabPane.getTabs().get(1).setText("Train Queue Passenger Details");
            GUI.mainTabPane.getTabs().get(2).setText("Waiting Room Passenger Details");

            GridPane.setConstraints(GUI.choiceBoxTrains, 9, 1, 2,1);
            GridPane.setConstraints(GUI.dateLabel, 9, 2, 2,1);

            // If the "Seat View" tab is selected.
            if (tab == 0) {
                GUI.titleLabel.setText("S E A T   V I E W...");


                GridPane.setConstraints(GUI.space2, 8, 7);
                GridPane.setConstraints(GUI.menuButton, 9, 7);

                // Updating the Train seat labels.
                for (int passenger = 0; passenger < waitingRoom[train].length; passenger++) {
                    // If Passenger is in the waiting room.
                    if (waitingRoom[train][passenger] != null) {
                        GUI.seatLabels[passenger].setGraphic(new ImageView(imageAvailable));
                    }
                    // If the seat has been not booked.
                    else {
                        GUI.seatLabels[passenger].setGraphic(new ImageView(imageBlank));
                    }

                    // Removing Previously added Tooltips if available.
                    if (GUI.seatLabels[passenger].getTooltip() != null) {
                        GUI.seatLabels[passenger].setTooltip(null);
                    }

                    // Adding updated label to the GUI.
                    GUI.gridPaneTab.getChildren().add(GUI.seatLabels[passenger]);
                }

                // Adding the passengers that are in the trainQueue.
                for (int passenger = 0; passenger < trainQueue.getQueueLength(train); passenger++) {
                    // Getting the seat number.
                    int seatNumber = seatNumberConverter(trainQueue.getQueueArray(train)[passenger].getSeatNumber());

                    // Updating the label.
                    GUI.seatLabels[seatNumber].setGraphic(new ImageView(imageTaken));
                    Tooltip tooltip = new Tooltip();
                    tooltip.setText("Passenger Name \t:   " + trainQueue.getQueueArray(train)[passenger].getPassengerName() + "\nPassenger NIC \t:   " + trainQueue.getQueueArray(train)[passenger].getPassengerNIC());

                    // Adding updated label to the GUI.
                    GUI.seatLabels[seatNumber].setTooltip(tooltip);
                }

                // Adding other elements to the layout.
                GUI.gridPaneTab.getChildren().addAll(GUI.choiceBoxTrains, GUI.titleLabel, GUI.space, GUI.space2, GUI.dateLabel, GUI.menuButton, GUI.labelBoarded, GUI.labelEmpty, GUI.labelUnbooked, GUI.labelInstructions);
            }
            // If the "Train Queue Passenger Details" tab is selected.
            else if (tab == 1) {
                GUI.titleLabel.setText("V I E W   T R A I N   Q U E U E...");

                // Setting the TrainQueue table.
                GUI.tableViewTrainQueue.getItems().clear();

                // Setting Column Sizes.
                GUI.seatNumberColumnTQ.setPrefWidth(94);
                GUI.passengerNameColumnTQ.setPrefWidth(290);
                GUI.passengerNICColumnTQ.setPrefWidth(270);

                for (Passenger passenger : TrainStation.getTrainQueue().getQueueArray(train)) {
                    GUI.tableViewTrainQueue.getItems().add(passenger);
                }
                GUI.tableViewTrainQueue.getSortOrder().add(GUI.seatNumberColumnTQ);
                GUI.tableViewTrainQueue.setMinSize(89*6+10*7+50, 70*6+10*6);
                GridPane.setConstraints(GUI.tableViewTrainQueue, 0, 1, 7, 7);

                // Adding all elements to the layout.
                GUI.gridPaneTab.getChildren().addAll(GUI.tableViewTrainQueue, GUI.titleLabel, GUI.choiceBoxTrains, GUI.menuButton, GUI.dateLabel, GUI.space2);

            }
            // If the "Waiting Room Passenger Details" tab is selected.
            else if ((tab == 2)) {
                GUI.titleLabel.setText("V I E W   W A I T I N G   R O O M...");

                // Setting the Waiting Room table.
                GUI.tableViewWaitingRoom.getItems().clear();

                // Setting Column Sizes.
                GUI.seatNumberColumnWR.setPrefWidth(94);
                GUI.passengerNameColumnWR.setPrefWidth(290);
                GUI.passengerNICColumnWR.setPrefWidth(270);

                for (Passenger passenger : TrainStation.getWaitingRoom()[train]) {
                    if (passenger != null) {
                        GUI.tableViewWaitingRoom.getItems().add(passenger);
                    }
                }
                GUI.tableViewWaitingRoom.getSortOrder().add(GUI.seatNumberColumnWR);
                GUI.tableViewWaitingRoom.setMinSize(89*6+10*7+50, 70*6+10*6);
                GridPane.setConstraints(GUI.tableViewWaitingRoom, 0, 1, 7, 7);

                // Adding all elements to the layout.
                GUI.gridPaneTab.getChildren().addAll(GUI.tableViewWaitingRoom, GUI.titleLabel, GUI.choiceBoxTrains, GUI.menuButton, GUI.dateLabel, GUI.space2);
            }

            // Adding the grid to the selected tab.
            GUI.mainTabPane.getTabs().get(tab).setContent(GUI.gridPaneTab);
            GUI.mainTabPane.getSelectionModel().select(tab);
        }
        // Making the GUI visible.
        GUI.window.setIconified(false);
    }

    /**
     * Method to launch the GUI for the Simulation results.
     * @param sceneName The name of the scene.
     * @param trainQueuesList Results of the simulation.
     * @param tab Tab in the scene that's open.
     * @param subTab Sub Tab in the tab thats open.
     * @param train Train number.
     */
    protected static void launchingSimulationGUI(String sceneName, LinkedList<SimulationPassengerQueue[]> trainQueuesList, int tab, int subTab, int train) {
        GUI.scene.setRoot(GUI.mainTabPane);
        GUI.sceneName = sceneName;
        GUI.gridPaneTab.getChildren().clear();

        // Setting all the Tabs.
        GUI.mainTabPane.getTabs().get(0).setContent(null);
        GUI.mainTabPane.getTabs().get(1).setContent(null);
        GUI.secondQueueTabPane.getTabs().get(0).setContent(null);
        GUI.secondQueueTabPane.getTabs().get(1).setContent(null);
        GUI.mainTabPane.getTabs().get(2).setContent(null);

        GUI.mainTabPane.getTabs().get(0).setText("One Queue");
        GUI.mainTabPane.getTabs().get(1).setText("Two Queues");
        GUI.mainTabPane.getTabs().get(2).setText("Overall Stats");
        GUI.mainTabPane.getTabs().get(1).setContent(GUI.secondQueueTabPane);



        GridPane.setConstraints(GUI.choiceBoxTrains, 9, 1, 2,1);
        GridPane.setConstraints(GUI.dateLabel, 9, 2, 2,1);

        GridPane.setConstraints(GUI.space2, 8, 9);
        GridPane.setConstraints(GUI.menuButton, 9, 9);

        // If "One Queue" tab or "Two Queues" tab selected.
        if (tab == 0 || tab == 1) {
            // Setting the Title accordingly.
            if (tab == 0) {
                GUI.titleLabel.setText("Simulation for One Queue...");
            } else {
                GUI.titleLabel.setText("Simulation for Two Queues...");
            }

            GridPane.setConstraints(GUI.space3, 9, 3, 2,1);

            // Selecting the relevant Train Queue.
            SimulationPassengerQueue[] trainQueues = trainQueuesList.get(tab);

            // Populating the Table with simulation results.
            GUI.tableViewTrainQueueSim.getItems().clear();
            for (SimulationPassenger passenger : trainQueues[subTab].getQueueArraySim(train)) {
                if (passenger != null) {
                    GUI.tableViewTrainQueueSim.getItems().add(passenger);
                }
            }

            // Setting the Stats labels---------------------------------------------------------------------------------
            // Maximum waiting time label.
            GUI.maxWaitLabel.setText("Max Wait \t\t: " + trainQueues[subTab].getMaxStayInQueue(train));
            // Minimum waiting time label.
            GUI.minWaitLabel.setText("Min Wait \t\t: " + trainQueues[subTab].getMinStayInQueue(train));

            // Average Waiting time label.
            // If the Queue has been used.
            if (!trainQueues[tab].isEmpty(train)) {
                GUI.avgWaitLabel.setText("Average Wait \t: " + df2.format(trainQueues[subTab].getTotalWaitingTime(train) / (double) trainQueues[0].getQueueLength(train)));
            }
            // If the Queue has not been used.
            else {
                GUI.avgWaitLabel.setText("Average Wait \t: " + df2.format((double) 0));
            }

            // Queue Length label.
            GUI.queueLengthLabel.setText("Queue Length \t: " + trainQueues[subTab].getQueueLength(train));

            // Adding all elements to the layout.
            GUI.gridPaneTab.getChildren().addAll(GUI.tableViewTrainQueueSim, GUI.titleLabel, GUI.choiceBoxTrains, GUI.dateLabel, GUI.menuButton, GUI.space2, GUI.maxWaitLabel, GUI.minWaitLabel, GUI.avgWaitLabel, GUI.queueLengthLabel, GUI.space3);

            // Adding the grid pane to the correct tab.
            if (tab == 0) {
                GUI.mainTabPane.getTabs().get(tab).setContent(GUI.gridPaneTab);
            } else {
                GUI.secondQueueTabPane.getTabs().get(subTab).setContent(GUI.gridPaneTab);
                // Making the selected tab stay open.
                GUI.secondQueueTabPane.getSelectionModel().select(subTab);
            }

            // Making the selected tab stay open.
            GUI.mainTabPane.getSelectionModel().select(tab);

        }
        // If "Overall Stats" tab selected.
        else if (tab == 2) {
            // Setting Title and Headings.
            GUI.titleLabel.setText("Overall Statistics of Two simulations...");
            GUI.headingLabel.setText("Total Time Waiting Comparison");
            GUI.headingLabel2.setText("Queue Length Comparison");

            // Calculating Simulation 2 results.
            int sim2MaxWait = Math.max(trainQueuesList.get(1)[0].getTotalWaitingTime(train), trainQueuesList.get(1)[1].getTotalWaitingTime(train));
            int sim2MinQueueLength = Math.min(trainQueuesList.get(1)[0].getQueueLength(train), trainQueuesList.get(1)[1].getQueueLength(train));

            // Calculating Simulation 2 minimum Average.
            double sim2MinAvg = 0.0;
            // If both queues are used.
            if (!trainQueuesList.get(1)[0].isEmpty(train) && !trainQueuesList.get(1)[1].isEmpty(train)) {
                sim2MinAvg = Math.min(trainQueuesList.get(1)[0].getTotalWaitingTime(train) / (double) trainQueuesList.get(1)[0].getQueueLength(train), trainQueuesList.get(1)[1].getTotalWaitingTime(train) / (double) trainQueuesList.get(1)[1].getQueueLength(train));
            }
            // If Only 1st Queue is used.
            else if (!trainQueuesList.get(1)[0].isEmpty(train) && trainQueuesList.get(1)[1].isEmpty(train)) {
                sim2MinAvg =trainQueuesList.get(1)[0].getTotalWaitingTime(train) / (double) trainQueuesList.get(1)[0].getQueueLength(train);
            }
            // If Only 2nd Queue is used.
            else if (trainQueuesList.get(1)[0].isEmpty(train) && !trainQueuesList.get(1)[1].isEmpty(train)) {
                sim2MinAvg =trainQueuesList.get(1)[1].getTotalWaitingTime(train) / (double) trainQueuesList.get(1)[1].getQueueLength(train);
            }


            // Adding simulation 1 data to the waiting time graph.
            GUI.barChartTime.getData().get(0).getData().clear();
            GUI.barChartTime.getData().get(0).getData().add(new XYChart.Data("Maximum", trainQueuesList.get(0)[0].getTotalWaitingTime(train)));
            GUI.barChartTime.getData().get(0).getData().add(new XYChart.Data("Average", (double) trainQueuesList.get(0)[0].getTotalWaitingTime(train) / trainQueuesList.get(0)[0].getQueueLength(train)));

            // Adding simulation 2 data to the waiting time graph.
            GUI.barChartTime.getData().get(1).getData().clear();
            GUI.barChartTime.getData().get(1).getData().add(new XYChart.Data("Maximum", sim2MaxWait));
            GUI.barChartTime.getData().get(1).getData().add(new XYChart.Data("Average", sim2MinAvg));

            // Adding simulation 1 data to the Queue length graph.
            GUI.barChartLength.getData().get(0).getData().clear();
            GUI.barChartLength.getData().get(0).getData().add(new XYChart.Data("Minimum", trainQueuesList.get(0)[0].getQueueLength(train)));

            // Adding simulation 2 data to the Queue length graph.
            GUI.barChartLength.getData().get(1).getData().clear();
            GUI.barChartLength.getData().get(1).getData().add(new XYChart.Data("Minimum", sim2MinQueueLength));

            // Adding all elements to the layout.
            GUI.gridPaneTab.getChildren().addAll(GUI.titleLabel, GUI.headingLabel, GUI.headingLabel2, GUI.choiceBoxTrains, GUI.dateLabel, GUI.menuButton, GUI.barChartLength, GUI.barChartTime, GUI.space2, GUI.space);

            // Adding the Grid to the selected tab.
            GUI.mainTabPane.getTabs().get(tab).setContent(GUI.gridPaneTab);
            // making the selected tab stay open.
            GUI.mainTabPane.getSelectionModel().select(tab);
        }
        // Making the GUI visible.
        GUI.window.setIconified(false);
    }

    /**
     * Method for making the simulation report text file.
     * @param trainQueuesList Results of the simulation.
     * @throws IOException if the path given is wrong.
     */
    protected static void simulationReport(LinkedList<SimulationPassengerQueue[]> trainQueuesList) throws IOException {
        // Opening the File.
        File file = new File("src/Denuwara_Manike/simulationReport.txt");
        FileOutputStream fileOutputStream = new FileOutputStream(file);
        OutputStreamWriter outputStreamWriter = new OutputStreamWriter(fileOutputStream);

        StringBuilder text = new StringBuilder();

        // Creating the Overall Summary.
        text.append("OVERALL SUMMARY\n\n");

        for (int train = 0; train < 2; train++) {
            int sim2MaxWait = Math.max(trainQueuesList.get(1)[0].getTotalWaitingTime(train), trainQueuesList.get(1)[1].getTotalWaitingTime(train));
            int sim2MinQueueLength = Math.min(trainQueuesList.get(1)[0].getQueueLength(train), trainQueuesList.get(1)[1].getQueueLength(train));

            // Calculating Simulation 2 minimum Average.
            double sim2MinAvg = 0.0;
            // If both queues are used.
            if (!trainQueuesList.get(1)[0].isEmpty(train) && !trainQueuesList.get(1)[1].isEmpty(train)) {
                sim2MinAvg = Math.min(trainQueuesList.get(1)[0].getTotalWaitingTime(train) / (double) trainQueuesList.get(1)[0].getQueueLength(train), trainQueuesList.get(1)[1].getTotalWaitingTime(train) / (double) trainQueuesList.get(1)[1].getQueueLength(train));
            }
            // If Only 1st Queue is used.
            else if (!trainQueuesList.get(1)[0].isEmpty(train) && trainQueuesList.get(1)[1].isEmpty(train)) {
                sim2MinAvg =trainQueuesList.get(1)[0].getTotalWaitingTime(train) / (double) trainQueuesList.get(1)[0].getQueueLength(train);
            }
            // If Only 2nd Queue is used.
            else if (trainQueuesList.get(1)[0].isEmpty(train) && !trainQueuesList.get(1)[1].isEmpty(train)) {
                sim2MinAvg =trainQueuesList.get(1)[1].getTotalWaitingTime(train) / (double) trainQueuesList.get(1)[1].getQueueLength(train);
            }


            text.append("Train : ").append(GUI.trains[train]).append("\n\n")
                    .append("\tMaximum Total Waiting Time (s) : \n")
                    .append("\t\tSimulation 1 : ").append(trainQueuesList.get(0)[0].getTotalWaitingTime(train)).append("\n")
                    .append("\t\tSimulation 2 : ").append(sim2MaxWait).append("\n\n")
                    .append("\tMinimum Average Waiting Time (s) : \n")
                    .append("\t\tSimulation 1 : ").append(df2.format((double) trainQueuesList.get(0)[0].getTotalWaitingTime(train)/ trainQueuesList.get(0)[0].getQueueLength(train)))
                    .append("\n").append("\t\tSimulation 2 : ").append(df2.format(sim2MinAvg)).append("\n\n")
                    .append("\tMaximum Queue Length : \n")
                    .append("\t\tSimulation 1 : ").append(trainQueuesList.get(0)[0].getQueueLength(train)).append("\n")
                    .append("\t\tSimulation 2 : ").append(sim2MinQueueLength).append("\n\n\n");
        }

        // Creating the Train Queues
        text.append("\nTRAIN QUEUES\n\n");

        for (int simulation = 0; simulation < 2; simulation++) {
            text.append("Simulation : ").append(simulation + 1).append("\n\n");
            for (int train = 0; train < 2; train++) {
                text.append("\tTrain : ").append(GUI.trains[train]).append("\n\n");
                for (int trainQueue = 0; trainQueue < trainQueuesList.get(simulation).length; trainQueue++) {
                    text.append("\t\tTrain Queue : ").append(trainQueue + 1).append("\n\n");
                    text.append("\t\t\t|-------------------------------------------------------------------------------------------------------|\n");
                    text.append("\t\t\t| group  |  Seat  |").append(repeatSpace(14)).append("Name").append(repeatSpace(15)).append("|").append(repeatSpace(7)).append("NIC").append(repeatSpace(8)).append("| Length |  time   |  totalTime  |\n");
                    text.append("\t\t\t|-------------------------------------------------------------------------------------------------------|\n");

                    for (SimulationPassenger sp : trainQueuesList.get(simulation)[trainQueue].getQueueArray(train)) {
                        text.append("\t\t\t|   ").append(sp.getGroupNumber()).append(repeatSpace(5 - String.valueOf(sp.getGroupNumber()).length())).append("|").append("   ").append(sp.getSeatNumber()).append("   |").append("   ").append(sp.getPassengerName()).append(repeatSpace(30 - sp.getPassengerName().length())).append("|").append("   ").append(sp.getPassengerNIC()).append(repeatSpace(15 - sp.getPassengerNIC().length())).append("|").append("   ").append(sp.getQueueLength()).append("    |").append("   ").append(sp.getSecondsInQueue()).append("s").append(repeatSpace(5 - String.valueOf(sp.getSecondsInQueue()).length())).append("|").append("     ").append(sp.getWaitingTime()).append("s").append(repeatSpace(7 - String.valueOf(sp.getWaitingTime()).length())).append("|\n");
                    }
                    text.append("\t\t\t|-------------------------------------------------------------------------------------------------------|\n\n");
                }
            }
            text.append("\n");
        }

        // Deleting Redundant lines at the edn of the file.
        text.delete(text.length()-2, text.length());

        // Writing the String to the file.
        outputStreamWriter.write(text.toString());
        outputStreamWriter.flush();

        // Closing the Opened Files.
        outputStreamWriter.close();
        fileOutputStream.close();
    }

    /**
     * Method for taking the initial user conditions before running the simulation and running it.
     */
    protected static void runningSimulation() {
        Scanner sc = new Scanner(System.in);

        try {
            // Getting the simulation type.
            System.out.print("Select the Simulation Method : \n" +
                    "\t0 : To Run the Simulation to the Current Waiting Room\n" +
                    "\t1 : To Run the Simulation for the Whole Waiting Room\n" +
                    "\n\t\t :  ");
            int situation = sc.nextInt();
            sc.nextLine();

            // Checking if the Simulation Type is valid.
            if (situation == 0 || situation == 1) {
                creatingSimulationStats(situation);
                simulationReport(TrainStation.getSimulationTrainQueues());
                launchingSimulationGUI("SimTQ", TrainStation.getSimulationTrainQueues(), 0, 0, 0);

            }
            // When the simulation type is invalid.
            else {
                System.out.println("Invalid Selection.");
                mainMenu();
            }
        }
        // When an Invalid character is added.
        catch (InputMismatchException e) {
            System.out.println("Invalid Input.");
            mainMenu();
        }
        // When an error is OCCured when writing the simulation report.
        catch ( IOException e) {
            System.out.println("ERROR - Making the Simulation Report.");
            mainMenu();
        }
    }

    /**
     * Method to make given number of spaces.
     * @param number Number of spaces.
     * @return The created whitespaces.
     */
    private static String repeatSpace(int number) {
        StringBuilder space = new StringBuilder();

        // Adding spaces.
        for (int i = 0; i < number; i++) {
            space.append(" ");
        }

        //returning spaces.
        return space.toString();
    }

    /**
     * Method to creating simulation results.
     * @param situation The required user condition for waiting room.
     */
    protected static void creatingSimulationStats(int situation) {
        // Clearing previous simulation results.
        TrainStation.clearSimulationTrainQueues();

        // Getting the Waiting Room from the DataBase.
        Passenger[][] waitingRoomDB = Methods.readingDB();

        // Declaring a waiting Room to run the simulation on.
        Passenger[][] waitingRoom = new Passenger[2][42];

        // Running the simulation twice.
        for (int simulation = 1; simulation < 3; simulation++) {

            // Declaring and initializing a SimulationPassengerQueue[] to store simulation results.
            SimulationPassengerQueue[] trainQueues = new SimulationPassengerQueue[simulation];
            for (int i = 0; i < simulation; i++) {
                trainQueues[i] = new SimulationPassengerQueue();
            }

            // Initializing Waiting room for the simulation.
            for(int train = 0; train < waitingRoom.length; train++) {
                System.arraycopy(waitingRoomDB[train], 0, waitingRoom[train], 0, waitingRoomDB[train].length);
            }

            // Getting the Waiting room trainQueue.
            PassengerQueue trainQueue = TrainStation.getTrainQueue();

            for (int train = 0; train < 2; train++) {
                // Making the waiting room equal to the TrainStation.waitingRoom if its selected.
                if (situation == 0) {
                    for (int passenger = 0; passenger < trainQueue.getQueueLength(train); passenger++) {
                        String seatNumberTQ = trainQueue.getQueueArray(train)[passenger].getSeatNumber();
                        if (waitingRoom[train][seatNumberConverter(seatNumberTQ)] != null) {
                            if (waitingRoom[train][seatNumberConverter(seatNumberTQ)].getSeatNumber().equalsIgnoreCase(seatNumberTQ)) {
                                waitingRoom[train][seatNumberConverter(seatNumberTQ)] = null;
                            }
                        }
                    }
                }

                // Initializing the group number.
                int groupNumber = 0;
                // Running the simulation till the waiting room finishes.
                while (Methods.waitingRoomSize(train, waitingRoom) > 0) {
                    // Increasing the group number.
                    groupNumber++;

                    // Declaring random.
                    Random random = new Random();

                    // Getting number rof passengers for the current group.
                    int maxSelect = Methods.creatingMaxWQSelect(train, waitingRoom);

                    // Creating a array for the Group and Populating it.
                    SimulationPassenger[] passengersGroup = new SimulationPassenger[maxSelect];
                    for (int select = 0; select < maxSelect; select++) {
                        // Getting the current Waiting room without any null elements.
                        Passenger[] sortedWaitingRoom = Methods.sortingWaitingRoom(train, waitingRoom);

                        // Selecting a Passenger Randomly to the Group.
                        int selection;
                        do {
                            selection = random.nextInt(sortedWaitingRoom.length);
                        } while (sortedWaitingRoom[selection] == null);

                        // Adding the selected passenger to the group.
                        passengersGroup[select] = new SimulationPassenger(sortedWaitingRoom[selection], maxSelect, groupNumber);
                        // Deleteing the added passenger from the waiting room.
                        waitingRoom[train][Methods.seatNumberConverter(passengersGroup[select].getSeatNumber())] = null;
                    }

                    // Sorting the Group to according to the seat number (because passengers board according to the seat number).
                    SimulationPassenger temp;
                    for (int i = 0; i < passengersGroup.length; i++) {
                        for (int j = i + 1; j < passengersGroup.length; j++) {
                            if (passengersGroup[j].getSeatNumber().compareTo(passengersGroup[i].getSeatNumber()) < 0) {
                                temp = passengersGroup[i];
                                passengersGroup[i] = passengersGroup[j];
                                passengersGroup[j] = temp;
                            }
                        }
                    }

                    // Selecting the Queue 2 if the Queue 1 is longer than Queue 2
                    boolean isTQ2Selected = false;
                    if (simulation == 2) {
                        isTQ2Selected = trainQueues[0].getQueueLength(train) > trainQueues[1].getQueueLength(train);
                    }

                    // Initializing waiting time for a group.
                    int secondsWaited = 0;
                    // Adding every passenger in group to trainQueue.
                    for (int passenger = 0; passenger < maxSelect; passenger++) {
                        // Adding each passengers waiting time to the Queue waiting time.
                        secondsWaited += passengersGroup[passenger].getSecondsInQueue();
                        // Setting the waiting time in queue to each passenger.
                        passengersGroup[passenger].setWaitingTime(secondsWaited);

                        // Adding passengers to the Queue.
                        if (isTQ2Selected) {
                            trainQueues[1].add(passengersGroup[passenger], train);
                        } else {
                            trainQueues[0].add(passengersGroup[passenger], train);
                        }

                    }
                }
            }
            // Storing the simulation report.
            TrainStation.addToSimulationTrainQueues(trainQueues);
        }
    }

    /**
     * Method to Quit the program.
     * @param isFromConsole Boolean from where the quiting is executed from(true if from console, false if from GUI).
     */
    protected static void quitingProgram(boolean isFromConsole) {

        // If the user tried to terminate the program from the console.
        if (isFromConsole) {

            // Asking for Confirmation to terminate the program.
            Scanner sc = new Scanner(System.in);
            System.out.print("Press Y to Exit\n" +
                    "Press C to continue\n" +
                    "\t: ");
            String text = formattingWhitespace(sc.nextLine());

            // If user wants to terminate the program.
            if (text.equalsIgnoreCase("Y")) {
                System.out.println("Quiting Program...");
                System.exit(0);
            }
            // If user wants to continue the program.
            else if (text.equalsIgnoreCase("C")) {
                System.out.println("Continuing program");
                mainMenu();
            }
            // User entering an Invalid Input.
            else {
                System.out.println("Invalid Input");
                quitingProgram(true);
            }
        }
        // If the user tried to terminate the program from the GUI.
        else {
            // Asking for Confirmation to terminate the program.
            String result = ConfirmBox.display("Confirm Exit", "Are you sure you want to exit?", "Yes", "No");

            // Checking the confirmation
            if (result.equalsIgnoreCase("yes")) {
                System.out.println("Quiting Program...");
                System.exit(0);
            }
        }
    }

    /**
     * Method to delete a passenger from the trainQueue.
     * @param waitingRoom The Current Waiting room.
     * @param trainQueue The current trainQueue.
     */
    protected static void deletePassenger(Passenger[][] waitingRoom, PassengerQueue trainQueue) {
        // Setting scanner for console inputs.
        Scanner sc = new Scanner(System.in);

        // Taking user input for how to delete passenger.
        System.out.print("Press S to delete passenger using the seat number\n" +
                "Press N to delete a passenger using passenger name\n" +
                "\t: ");
        String text = formattingWhitespace(sc.nextLine());

        // Validating User Input for How to delete passenger.
        if (text.equalsIgnoreCase("s") || text.equalsIgnoreCase("n")) {

            // Taking User Input for train to delete customer from.
            System.out.println("Select the Train : ");
            for (int train = 0; train < GUI.trains.length; train++) {
                System.out.println("\t " + train + " : " + GUI.trains[train]);
            }
            System.out.print("Enter Train Number : ");
            try {
                int train = sc.nextInt();
                sc.nextLine();  // Consuming the newline, so the next sc.nextLine does not react to it.

                // Validating User Input for train to delete customer from.
                if (train == 0 || train == 1) {

                    // If customer select to delete customer using seat number.
                    if (text.equalsIgnoreCase("S")) {

                        // Taking seat number to delete customer from.
                        System.out.print("Enter Seat Number : ");
                        String seatNumberText = formattingWhitespace(convert(sc.nextLine()));

                        // Validating User Input for seat to delete customer from.
                        int seatNumber = seatNumberConverter(seatNumberText);
                        if (seatNumber < 42 && seatNumber > -1) {
                            // Checking a passenger array for the seat to delete passenger..
                            boolean isRemoved = false;
                            for (int seat = 0; seat < trainQueue.getQueueLength(train); seat++) {
                                // When the seat found.
                                if (trainQueue.getQueueArray(train)[seat].getSeatNumber().equalsIgnoreCase(seatNumberText)) {
                                    waitingRoom[train][seatNumber] = trainQueue.getQueueArray(train)[seat];
                                    trainQueue.remove(trainQueue.getQueueArray(train)[seat], train);
                                    isRemoved = true;
                                    System.out.println("Passenger Deleted from seat : " + seatNumber);
                                    break;
                                }
                            }
                            // When the loop didn't find the seat in the TrainQueue.
                            if (!isRemoved) {
                                System.out.println("Seat : " + seatNumberText + " is not in the Train Queue.");
                            }
                        }
                        // If the user input for the seat number is invalid.
                        else {
                            System.out.println("Invalid Seat Number.");
                        }
                    }
                    // If customer select to delete passenger using passenger name.
                    else if (text.equalsIgnoreCase("N")) {
                        // Taking user input of the passenger name to delete.
                        System.out.print("Enter Passenger Name : ");
                        String passengerName = formattingWhitespace(convert(sc.nextLine()));

                        // Checking a passenger array for the passenger name to delete passenger..
                        boolean isRemoved = false;
                        for (int seat = 0; seat < trainQueue.getQueueLength(train); seat++) {
                            // Deleting the every passenger with that name.
                            if (trainQueue.getQueueArray(train)[seat].getPassengerName().equalsIgnoreCase(passengerName)) {
                                String seatNumber = trainQueue.getQueueArray(train)[seat].getSeatNumber();
                                waitingRoom[train][seatNumberConverter(seatNumber)] = trainQueue.getQueueArray(train)[seat];
                                trainQueue.remove(trainQueue.getQueueArray(train)[seat], train);
                                isRemoved = true;
                                System.out.println("Passenger deleted : " + passengerName + " form seat : " + seatNumber);
                            }
                        }
                        // If there was no passenger with the user given passenger.
                        if (!isRemoved) {
                            System.out.println("Invalid Passenger Name.");
                        }
                    }
                }
                // When the user given Train is invalid.
                else {
                    System.out.println("Invalid Train Number.");
                }
            }
            // When the user enter a letter for a train instead of a number.
            catch (InputMismatchException e) {
                System.out.println("Invalid Input.");
            }
        }
        // When the user given delete Method is Invalid.
        else {
            System.out.println("Invalid Input.");
        }
    }

    /**
     * Method to create the number of passengers that should be selected to add to Train Queue.
     * @param train The selected Train.
     * @param waitingRoom Current Waiting Room.
     * @return The selecting number.
     */
    protected static int creatingMaxWQSelect(int train, Passenger[][] waitingRoom) {
        // Declaring the number for selecting.
        int maxQueueSelect;

        // When the waiting room is higher than 5.
        if (waitingRoomSize(train, waitingRoom) > 5) {
            maxQueueSelect = new Random().nextInt(6) + 1;
        }
        // When the waiting room is empty
        else if (waitingRoomSize(train, waitingRoom) == 0){
            maxQueueSelect = 0;
        }
        // When the waaiting room is not empty and less than or equal to 5.
        else {
            maxQueueSelect = new Random().nextInt(waitingRoomSize(train, waitingRoom)) + 1;
        }

        // Returning the generated random number.
        return maxQueueSelect;
    }

    /**
     * Method that converts the seat number to the save position index of the waiting room array.
     * @param seatNumber Seat number that need to be converted.
     * @return The position index.
     */
    protected static int seatNumberConverter(String seatNumber) {
        int number = -1;
        // Initializing row names.
        String[] rows = {"A", "B", "C", "D", "E", "F", "G"};

        // Making sure the given seat number is valid.
        if (seatNumber.length() == 2) {
            for (int row = 0; row < rows.length; row++) {
                // Finding the Row of the given seat.
                if (seatNumber.substring(0, 1).equalsIgnoreCase(rows[row]) && "123456".contains(seatNumber.substring(1))) {
                    // Multiplying the row index by 6 and adding the column number.
                    number += row * 6 + Integer.parseInt(seatNumber.substring(1));
                }
            }
        }

        // Returning the seat Number.
        return number;
    }

    /**
     * The Method to read the waiting room from the CW1 save data.
     * @return Returns a Waiting Room.
     */
    protected static Passenger[][] readingDB() {
        // Creating a 2D Passenger array to store seats.
        Passenger[][] passengers = new Passenger[2][42];

        // Populating the passenger array.
        for (int train = 0; train < 2; train++){
            for (int passenger = 0; passenger < 42; passenger++) {
                passengers[train][passenger] = null;
            }
        }

        // Connecting to a Mongo Client.
        MongoClient mongoClient = new MongoClient("localHost", 27017);

        // Accessing the database.
        MongoDatabase mongoDatabase = mongoClient.getDatabase("TrainBooking");
        // Retrieving a collection.
        MongoCollection<Document> mongoCollection = mongoDatabase.getCollection("DenuwaraManike");

        // Getting all the Documents.
        FindIterable<Document> documents = mongoCollection.find();

        // Adding all documents data to seats 3D array.
        // Getting today's date.
        LocalDate today = LocalDate.now();
        // Getting system's timezone.
        ZoneId defaultZoneId = ZoneId.systemDefault();

        for (Document document : documents) {
            // Converting stored date to LocalDate.
            Instant instant = document.getDate("Date").toInstant();
            LocalDate loadedDate = instant.atZone(defaultZoneId).toLocalDate();

            // Comparing the stored date with today's date.
            long date = ChronoUnit.DAYS.between(today, loadedDate);

            // Adding to the Array if the date is equal with today's date..
            if (date == 0) {
                if (!document.getString("PassengerName").equalsIgnoreCase("-")) {
                    passengers[document.getInteger("Train")][document.getInteger("Seat")] = new Passenger(
                            document.getString("PassengerName"),
                            document.getString("PassengerNIC"),
                            document.getString("SeatNumber")
                    );
                }
            }
        }

        // Disconnecting form the Mongo client.
        mongoClient.close();

        // Returning the populated 2D array.
        return passengers;
    }

    /**
     * Method that count the number of populated elements in a given waitingRoom (2D Passenger array).
     * @param train The selected Train.
     * @param waitingRoom The waiting room that needed be processed.
     * @return The number of populated elements.
     */
    public static int waitingRoomSize(int train, Passenger[][] waitingRoom) {
        // Initializing the count.
        int count = 0;
        // Checking the array for passengers.
        for (int passenger = 0; passenger < waitingRoom[train].length; passenger++) {
            // When a passenger is found.
            if (waitingRoom[train][passenger] != null) {
                count++;
            }
        }

        // Returning the passenger count.
        return count;
    }

    /**
     * Method to get a waiting room that has no unpopulated elements.
     * @param train Selected train number.
     * @param waitingRoom Current Waiting room.
     * @return Returns a populated Passenger array.
     */
    public static Passenger[] sortingWaitingRoom (int train, Passenger[][] waitingRoom) {
        // Declaring a Passenger array to the size of the passengers.
        Passenger[] passengers = new Passenger[waitingRoomSize(train, waitingRoom)];

        // Adding passengers form the given waiting room to the declared Passenger array.
        int index = 0;
        for (int passenger = 0; passenger < waitingRoom[train].length; passenger++) {
            if (waitingRoom[train][passenger] != null) {
                passengers[index] = waitingRoom[train][passenger];
                index++;
            }
        }

        // Returning the Populated Passenger array.
        return passengers;
    }

    /**
     * This makes a given String to Title case.
     * @param word Takes customer name as a String.
     * @return Returns Customer name in Title case.
     */
    private static String convert(String word) {

        char[] charArray = word.toCharArray();

        for (int letter = 0; letter < word.length(); letter++) {
            // Finding the first letter of a word.
            if (letter == 0 && charArray[letter] != ' ' || charArray[letter] != ' ' && charArray[letter - 1] == ' ') {
                // Checking if the first letter is lower case.
                if (charArray[letter] >= 'a' && charArray[letter] <= 'z') {
                    // Converting first letter to upper case
                    charArray[letter] = (char)(charArray[letter] - 'a' + 'A');
                }
            }
            // Checking if the letters other than the first letter are capital
            else if (charArray[letter] >= 'A' && charArray[letter] <= 'Z')
                // Converting uppercase other letters to lower case.
                charArray[letter] = (char)(charArray[letter] + 'a' - 'A');
        }
        // Returning a Title cased String.
        return new String(charArray);
    }

    /**
     * This Method removes excess spaces in a String.
     * @param string The String that need to be formatted.
     * @return Returns the Formatted String.
     */
    private static String formattingWhitespace(String string) {
        // Splitting the String.
        String[] stringSplits = string.split(" ");

        StringBuilder formattedString = new StringBuilder();
        for (int word = 0; word < stringSplits.length; word++) {
            // If the element is not an empty String.
            if (!stringSplits[word].isEmpty()) {
                // Checking if the word is not the last word of the sentence.
                if (word < stringSplits.length - 1) {
                    // Adding a space after the word.
                    formattedString.append(stringSplits[word]).append(" ");
                }
                // If last word of the sentence.
                else {
                    // Last word does not have a space after it.
                    formattedString.append(stringSplits[word]);
                }
            }
        }
        // Returning a formatted String.
        return formattedString.toString();
    }

    /**
     * Method to  save data from the Train Queue.
     * @param trainQueue Current Train Queue.
     */
    protected static void saveTQ(PassengerQueue trainQueue) {

        // Connecting to a Mongo Client.
        MongoClient mongoClient = new MongoClient("localHost", 27017);

        // Accessing the database.
        MongoDatabase mongoDatabase = mongoClient.getDatabase("TrainQueue");
        // Retrieving a collection.
        MongoCollection<Document> mongoCollection = mongoDatabase.getCollection("DenuwaraManike");

        // Deleting All existing documents.
        FindIterable<Document> findIterable = mongoCollection.find();
        for (Document document : findIterable) {
            mongoCollection.deleteOne(document);
        }


        // Adding Documents to the database.
        for (int train = 0; train < 2; train++) {
            for (int passenger = 0; passenger < trainQueue.getQueueLength(train); passenger++) {
                Document document = new Document();
                document.append("SeatNumber", trainQueue.getQueueArray(train)[passenger].getSeatNumber())
                        .append("PassengerName", convert(trainQueue.getQueueArray(train)[passenger].getPassengerName()))
                        .append("PassengerNIC", trainQueue.getQueueArray(train)[passenger].getPassengerNIC())
                        .append("SecondsInQueue", trainQueue.getQueueArray(train)[passenger].getSecondsInQueue())
                        .append("Train", train)
                        .append("Date", LocalDate.now());
                mongoCollection.insertOne(document);

            }
        }
        // Disconnecting form the Mongo client.
        mongoClient.close();
    }

    /**
     * Method to Method to Load the save data for the Train Queue.
     * @return The loaded Train Queue.
     */
    protected static PassengerQueue readTQ() {

        // Creating a PassengerQueue object to store trainQueue.
        PassengerQueue trainQueue = new PassengerQueue();

        // Connecting to a Mongo Client.
        MongoClient mongoClient = new MongoClient("localHost", 27017);

        // Accessing the database.
        MongoDatabase mongoDatabase = mongoClient.getDatabase("TrainQueue");
        // Retrieving a collection.
        MongoCollection<Document> mongoCollection = mongoDatabase.getCollection("DenuwaraManike");

        // Getting all the Documents.
        FindIterable<Document> documents = mongoCollection.find();

        // Adding all documents data to trainQueue PassengerQueue object.
        // Getting today's date.
        LocalDate today = LocalDate.now();
        // Getting system's timezone.
        ZoneId defaultZoneId = ZoneId.systemDefault();

        for (Document document : documents) {
            // Converting stored date to LocalDate.
            Instant instant = document.getDate("Date").toInstant();
            LocalDate loadedDate = instant.atZone(defaultZoneId).toLocalDate();

            // Comparing the stored date with today's date.
            long date = ChronoUnit.DAYS.between(today, loadedDate);

            // Adding to the trainQueue when the date is today's date.
            if (date == 0) {
                Passenger passenger = new Passenger(
                        document.getString("PassengerName"),
                        document.getString("PassengerNIC"),
                        document.getString("SeatNumber"));
                passenger.setSecondsInQueue(document.getInteger("SecondsInQueue"));
                trainQueue.add(passenger, document.getInteger("Train"));
            }
        }

        // Disconnecting form the Mongo client.
        mongoClient.close();

        TrainStation.setWaitingRoom();

        for (int train = 0; train < 2; train++) {
            for (int passenger = 0; passenger < trainQueue.getQueueLength(train); passenger++) {
                String seatNumberTQ = trainQueue.getQueueArray(train)[passenger].getSeatNumber();
                if (TrainStation.getWaitingRoom()[train][seatNumberConverter(seatNumberTQ)] != null) {
                    if (TrainStation.getWaitingRoom()[train][seatNumberConverter(seatNumberTQ)].getSeatNumber().equalsIgnoreCase(seatNumberTQ)) {
                        TrainStation.getWaitingRoom()[train][seatNumberConverter(seatNumberTQ)] = null;
                    }
                }
            }
        }

        // Returning the populated trainQueue.
        return trainQueue;

    }

}
