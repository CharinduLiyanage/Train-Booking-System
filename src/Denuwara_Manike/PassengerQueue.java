package Denuwara_Manike;

/**
 * This class creates PassengerQueue Objects.
 */
public class PassengerQueue {
    private Passenger[][] queueArray = new Passenger[2][42];
    private final int first = 0;
    private int last;
    private int[] queueLength = {0, 0};

    /**
     * Method to add a passenger to the Queue.
     * @param nextPassenger Passenger that should be added to the Queue.
     * @param train The train that the passenger should be added.
     */
    public void add(Passenger nextPassenger, int train) {

        // Checking if the queue is full.
        if (!isFull(train)) {
            // Checking if the passenger adding is the first passenger.
            if (isEmpty(train)) {
                this.queueArray[train][first] = nextPassenger;
                this.last = 0;
            } else {
                // Making sure there are no null passengers to the lower indexes on the queue.
                for (int last = this.last; last < this.queueArray[train].length; last++) {
                    // Adding to the first null that is found.
                    if (this.queueArray[train][last] == null) {
                        this.queueArray[train][last] = nextPassenger;
                        this.last = last;
                        break;
                    }
                }
            }
            // Increasing the Queue length.
            this.queueLength[train]++;
        }
    }

    /**
     * Method to remove a passenger from the Queue.
     * @param removingPassenger Passenger that should be removed.
     * @param train The train that the Passenger should be removed from.
     */
    public void remove(Passenger removingPassenger, int train) {
        // Checking the queue for the passenger to be deleted.
        for (int passenger = 0; passenger < this.queueArray[train].length; passenger++) {
            // Finding the passenger.
            if (this.queueArray[train][passenger] == removingPassenger) {
                // Deleting the passenger.
                this.queueArray[train][passenger] = null;
                this.queueLength[train]--;
                if (passenger < this.last) {
                    this.last = passenger;
                }
                break;
            }
        }
    }

    /**
     * Method to check if the Queue is empty.
     * @param train The Train number.
     * @return
     */
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
     * Method to check if the Queue is full.
     * @param train The train number.
     * @return
     */
    public boolean isFull(int train) {

        // Getting the count of populated elements in the Queue.
        int count = getQueueLength(train);

        // Return true if the count is 42 (the length of the array) else return false.
        return count == 42;
    }

    /**
     * Method to get the length of the selected train Queue.
     * @param train The train selected.
     * @return
     */
    public int getQueueLength(int train) {
        // Returning the selected Queues length.
        return this.queueLength[train];
    }

    /**
     * Method to get an array of with only the passengers in the train Queue.
     * @param train The selected train number.
     * @return
     */
    public Passenger[] getQueueArray(int train) {
        // Creating a array with the length of only the population of the queue
        Passenger[] passengers = new Passenger[getQueueLength(train)];

        int index = 0;
        // Populating the created array.
        for (Passenger passenger : this.queueArray[train]) {
            if (passenger != null) {
                passengers[index] = passenger;
                index++;
            }
        }

        // returning the populated array.
        return passengers;
    }

    /**
     * Method to set the Queue length of the train Queue.
     * @param queueLength the length.
     * @param train the selected train.
     */
    public void setQueueLength(int queueLength, int train) {
        // Setting the Queue length to the given length.
        this.queueLength[train] = queueLength;
    }

    // Getters and Setters----------------------------------------------------------------------------------------------

    public void setLast(int last) {
        this.last = last;
    }

    public int getFirst() {
        return first;
    }

    public int getLast() {
        return last;
    }

}
