package Denuwara_Manike;

/**
 * This class creates a SimulationPassengerQueue object which is an extension of PassengerQueue objects.
 * SimulationPassengerQueue objects are used in the simulation.
 */
public class SimulationPassengerQueue extends PassengerQueue {

    private SimulationPassenger[][] queueArray = new SimulationPassenger[2][42];
    private int[] maxStayInQueue = {0, 0};
    private int[] minStayInQueue = {0, 0};
    private int totalWaitingTime[] = {0, 0};

    /**
     * Method to add SimulationPassengers to the queue.
     * @param nextPassenger The Simulation Passenger to be added.
     * @param train The train that the SimulationPassenger should be added.
     */
    public void add(SimulationPassenger nextPassenger, int train) {

        // Checking if the queue is full.
        if (!isFull(train)) {
            // Checking if the passenger adding is the first passenger.
            if (isEmpty(train)) {
                this.queueArray[train][getFirst()] = nextPassenger;
                setLast(0);
                setMaxStayInQueue(nextPassenger.getWaitingTime(), train);
                setMinStayInQueue(nextPassenger.getWaitingTime(), train);

            } else {
                // Making sure there are no null passengers to the lower indexes on the queue.
                for (int last = getLast(); last < this.queueArray[train].length; last++) {
                    if (this.queueArray[train][last] == null) {
                        this.queueArray[train][last] = nextPassenger;
                        setLast(last);
                        break;
                    }
                }
                // Updating the Maximum waiting time if needed.
                if (getMaxStayInQueue(train)< nextPassenger.getWaitingTime()) {
                    setMaxStayInQueue(nextPassenger.getWaitingTime(), train);
                }
                // Updated the minimum waiting time if needed.
                if (getMinStayInQueue(train) > nextPassenger.getWaitingTime()) {
                    setMinStayInQueue(nextPassenger.getWaitingTime(), train);
                }
            }
            // Increasing the Queue length.
            setQueueLength(getQueueLength(train)+1, train);
            // Increasing the total waiting time of the queue.
            this.totalWaitingTime[train] += nextPassenger.getSecondsInQueue();

        }
    }

    /**
     * Method to get the total waiting time for a selected train.
     * @param train The selected train.
     * @return Returns the total waiting time for the selected queue.
     */
    public int getTotalWaitingTime(int train) {
        // Returning the waiting time for the selected queue.
        return totalWaitingTime[train];
    }

    /**
     * Method to get the queue array of only the passengers in the queue.
     * @param train The selected train number.
     * @return Returns an array of passengers that are in the queue.
     */
    @Override
    public SimulationPassenger[] getQueueArray(int train) {
        // Creating a array with the length of only the population of the queue
        SimulationPassenger[] passengers = new SimulationPassenger[getQueueLength(train)];

        int index = 0;
        // Populating the created array.
        for (SimulationPassenger passenger : this.queueArray[train]) {
            if (passenger != null) {
                passengers[index] = passenger;
                index++;
            }
        }

        // returning the populated array.
        return passengers;
    }

    /**
     * Method to check if the queue is empty.
     * @param train The Train number.
     * @return Returns true if the queue is empty, false if its not.
     */
    @Override
    public boolean isEmpty(int train) {

        // Initialising a variable for the return.
        boolean answer = true;

        // Checking if any index of the queue is populated
        for (Passenger passenger : this.queueArray[train]) {
            // Making the variable true if populated.
            if (passenger != null) {
                answer = false;
                break;
            }
        }

        // Returning the variable.
        return answer;
    }

    /**
     * Method to Check if the queue is full.
     * @param train The train number.
     * @return Returns true if the queue is full, false if its not.
     */
    @Override
    public boolean isFull(int train) {

        // Getting the count of populated elements in the Queue.
        int count = getQueueLength(train);

        // Return true if the count is 42 (the length of the array) else return false.
        return count == 42;
    }

    /**
     * Method to get the maximum waiting time of a selected Queue.
     * @param train The selected Queue.
     * @return Returns the maximum wait time.
     */
    public int getMaxStayInQueue(int train) {
        // Returning the maximum waiting time of the selected train.
        return maxStayInQueue[train];
    }

    /**
     * Method to set the maximum waiting time for a queue.
     * @param maxStayInQueue The maximum waiting time.
     * @param train The queue to be updated.
     */
    public void setMaxStayInQueue(int maxStayInQueue, int train) {
        // Setting the given maximum waiting time to the given queue.
        this.maxStayInQueue[train] = maxStayInQueue;
    }

    /**
     * Method to get minimum waiting time of a selected train queue.
     * @param train The selected train.
     * @return Returns the minimum waiting time.
     */
    public int getMinStayInQueue(int train) {
        // Returning the minimum waiting time of the selected train.
        return minStayInQueue[train];
    }

    /**
     * Method to set minimum waiting time of a selected train queue.
     * @param minStayInQueue The minimum waiting time.
     * @param train The selected train.
     */
    public void setMinStayInQueue(int minStayInQueue, int train) {
        // Setting the given minimum waiting time to the given queue.
        this.minStayInQueue[train] = minStayInQueue;
    }

    /**
     * Method to get the queue of a selected train.
     * @param train The selected train.
     * @return Returns the trainQueue.
     */
    public SimulationPassenger[] getQueueArraySim(int train) {
        // Returning the queue for the selected train.
        return queueArray[train];
    }

}
