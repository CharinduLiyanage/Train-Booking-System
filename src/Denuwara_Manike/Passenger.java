package Denuwara_Manike;

import java.util.Random;

/**
 * This class creates Passenger Objects.
 */
public class Passenger {
    private String passengerName;
    private String passengerNIC;
    private int secondsInQueue;
    private String seatNumber;

    /**
     * A Constructor for Passenger Objects, creates secondsInQueue for each Passenger Randomly.
     * @param passengerName A String containing a name of the Passenger.
     * @param passengerNIC A String containing the Passenger's NIC.
     * @param seatNumber A String containing a Passenger's seat number.
     */
    public Passenger(String passengerName, String passengerNIC, String seatNumber) {
        setPassengerName(passengerName);
        setPassengerNIC(passengerNIC);
        setSeatNumber(seatNumber);
        setRandomSecondsInQueue();
    }

    /**
     * A Constructor for Passenger Objects.
     * @param passengerName A String containing a name of the Passenger.
     * @param passengerNIC A String containing the Passengers NIC.
     * @param seatNumber A String containing a Passengers seat number.
     * @param secondsInQueue An Integer containing the Passengers seconds in Queue.
     */
    public Passenger(String passengerName, String passengerNIC, String seatNumber, int secondsInQueue){
        setPassengerName(passengerName);
        setPassengerNIC(passengerNIC);
        setSeatNumber(seatNumber);
        setSecondsInQueue(secondsInQueue);
    }

    /**
     * Method for setting a random seconds in Queue for a Passenger.
     */
    private void setRandomSecondsInQueue() {
        // Setting a random.
        Random random = new Random();

        // Initializing seconds in queue.
        int secondsInQueue = 0;
        // Adding 3 dices.
        for (int dice = 0; dice < 3; dice++) {
            // Getting a dice value 1 - 6.
            secondsInQueue += (random.nextInt(6) + 1);
        }

        // Returning calculated seconds in queue.
        this.secondsInQueue = secondsInQueue;
    }

    // Getters and Setters----------------------------------------------------------------------------------------------

    public String getPassengerName() {
        return passengerName;
    }

    public void setPassengerName(String passengerName) {
        this.passengerName = passengerName;
    }

    public String getPassengerNIC() {
        return passengerNIC;
    }

    public void setPassengerNIC(String passengerNIC) {
        this.passengerNIC = passengerNIC;
    }

    public int getSecondsInQueue() {
        return secondsInQueue;
    }

    public void setSecondsInQueue(int secondsInQueue) {
        this.secondsInQueue = secondsInQueue;
    }

    public String getSeatNumber() {
        return seatNumber;
    }

    public void setSeatNumber(String seatNumber) {
        this.seatNumber = seatNumber;
    }
}
