package Denuwara_Manike;

/**
 * This class creates SimulationPassenger objects which is an extension of Passenger objects.
 * SimulationPassenger objects are used for the simulation.
 */
public class SimulationPassenger extends Passenger {
    private int waitingTime;
    private int queueLength;
    private int groupNumber;

    /**
     * Constructor for simulationPassenger objects.
     * @param passenger Passenger that is added to the simulation.
     * @param queueLength The Queue Length of the passengers group.
     * @param groupNumber The group number of the passenger.
     */
    public SimulationPassenger(Passenger passenger, int queueLength, int groupNumber) {
        super(passenger.getPassengerName(), passenger.getPassengerNIC(), passenger.getSeatNumber(), passenger.getSecondsInQueue());
        setQueueLength(queueLength);
        setGroupNumber(groupNumber);
    }

    // Getters and Setters----------------------------------------------------------------------------------------------

    public int getWaitingTime() {
        return waitingTime;
    }

    public void setWaitingTime(int waitingTime) {
        this.waitingTime = waitingTime;
    }

    public int getQueueLength() {
        return queueLength;
    }

    public void setQueueLength(int queueLength) {
        this.queueLength = queueLength;
    }

    public int getGroupNumber() {
        return groupNumber;
    }

    public void setGroupNumber(int groupNumber) {
        this.groupNumber = groupNumber;
    }
}
