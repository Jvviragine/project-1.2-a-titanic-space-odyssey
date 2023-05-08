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
            this.stateVector = vectors;
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

    public Vector getVector(int index) {return stateVector[index];}

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

    /**
     * Computes the sum of two vectors
     * @param v the vector to be added
     * @return the resultant vector when adding v
     */
    public StateVector add(StateVector v){
        //Make new vector array for new StateVector
        Vector[] vectors = stateVector;

        //Iterate through each vector in the State Vector
        for(int i=0; i<stateVector.length; i++){
            stateVector[i] = stateVector[i].add(v.getStateVector()[i]);
        }

        //Return State Vector with modified values
        return new StateVector(vectors);
    }

    /**
     * Computes the difference between two vectors
     * @param v the vector to be subtracted
     * @return the resultant vector when subtracting v
     */
    public StateVector subtract(StateVector v){
        //Make new vector array for new StateVector
        Vector[] vectors = stateVector;

        //Iterate through each vector in the State Vector
        for(int i= 0; i<stateVector.length; i++){
            vectors[i] = stateVector[i].subtract(v.getStateVector()[i]);
        }
        //Return State Vector with modified values
        return new StateVector(vectors);
    }

    /**
     * Computes the product of a scalar multiplication
     * @param scalar a double that scales the state vector
     * @return the scalar product of the state vector and the scalar
     */
    public StateVector multiply(double scalar){
        //Make new vector array for new StateVector
        Vector[] vectors = stateVector;

        //Iterate through each vector in the State Vector
        for(int i=0; i<stateVector.length; i++){
            vectors[i] = stateVector[i].multiply(scalar);
        }

        //Return State Vector with modified values
        return new StateVector(vectors);
    }

}
