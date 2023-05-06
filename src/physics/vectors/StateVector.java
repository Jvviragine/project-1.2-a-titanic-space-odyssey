package physics.vectors;

/**
 * Stores the State Vector of the Differential Equation  of Interest
 *
 * For the Solar Syste, the State Vector will hold 2 3D Vectors (Position and Velocity)
 */
public class StateVector {

    private Vector[] stateVector;
    private int numberOfVectors;
    private int numberOfDimensions;

    /**
     * Constructor
     * @param vectors array containing the vectors that will be contained in the State Vector
     */
    public StateVector(Vector[] vectors) {
        if (vectors.length > 0) {
            numberOfVectors = vectors.length;
            numberOfDimensions = vectors[0].getDimension();

        }
        else {
            System.out.println("You have not provided any Vectors to make your State Vector");
        }
    }

    /**
     * Getter for the State Vector
     * @return the state vector in the format of an Array of Vectors
     */
    public Vector[] getStateVector() {
        return stateVector;
    }

    /**
     * Getter for the Number of Vectors in the State Vector
     * @return int containg the number of Vectors in the State Vector
     */
    public int getNumberOfVectors() {
        return numberOfVectors;
    }

    /**
     * Getter for the number of Dimensions that our State Vector Represents
     * It is simply the size of the vectors contained within the State Vector
     * @return
     */
    public int getNumberOfDimensions() {
        return numberOfDimensions;
    }

}
