package Denuwara_Manike;


import java.io.IOException;
import java.util.LinkedList;
import java.util.logging.Level;
import java.util.logging.Logger;

public class TrainStation {
    private static Passenger[][] waitingRoom;
    private static PassengerQueue trainQueue = new PassengerQueue();
    private static LinkedList<SimulationPassengerQueue[]> simulationTrainQueues = new LinkedList<>();


    public static void main(String[] args) {

        // Disabling MongoDb logs on console.
        Logger mongoLogger = Logger.getLogger( "org.mongodb.driver" );
        mongoLogger.setLevel(Level.SEVERE);

        
        // Prompt Text when the Program is launching
        System.out.println("Welcome to the Denuwara Manike Train Queue System\n".toUpperCase() +
                "-------------------------------------------------");


        try {
            // Executing MongoDB.
            Runtime.getRuntime().exec("C:\\Program Files\\MongoDB\\Server\\4.2\\bin\\mongod.exe");
            Runtime.getRuntime().exec("C:\\Program Files\\MongoDB\\Server\\4.2\\bin\\mongo.exe");


            // Reading the Database from Train Booking to get the Waiting room.
            setWaitingRoom();
            // Running the program.
            GUI.main(args);

        } catch (IOException e) {
            System.out.println("Error - MongoDB did not Execute Automatically.");
            Methods.launchingMainMenu(args);
        }
    }

    /**
     * Method to Read the Database from Train Booking and Set the waiting room.
     */
    public static void setWaitingRoom() {
        // Setting the waiting room.
        TrainStation.waitingRoom = Methods.readingDB();
    }

    // Getters and Setters----------------------------------------------------------------------------------------------
    public static Passenger[][] getWaitingRoom() {
        return waitingRoom;
    }

    public static PassengerQueue getTrainQueue() {
        return trainQueue;
    }

    public static void setTrainQueue(PassengerQueue trainQueue) {
        TrainStation.trainQueue = trainQueue;
    }

    public static LinkedList<SimulationPassengerQueue[]> getSimulationTrainQueues() {
        return simulationTrainQueues;
    }

    public static void addToSimulationTrainQueues(SimulationPassengerQueue[] simulationTrainQueues) {
        TrainStation.simulationTrainQueues.add(simulationTrainQueues);
    }

    public static void clearSimulationTrainQueues() {
        TrainStation.simulationTrainQueues.clear();
    }
}
