package physics.vectors;

/**
 * Stores the State Vector of the Differential Equation  of Interest
 *
 * For the Solar System, the State Vector will hold 2 3D Vectors (Position and Velocity)
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
            throw new IllegalArgumentException("You have not provided any Vectors to make your State Vector");
        }
    }

    /**
     * Getter for the State Vector
     * @return the state vector in the format of an Array of Vectors
     */
    public Vector[] getStateVector() {
        return stateVector;
    }

    public Vector getVector(int index) {
        return stateVector[index];
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

    /**
     * Computes the sum of two vectors
     * @param v the vector to be added
     * @return the resultant vector when adding v
     */
    public StateVector add(StateVector v){

        if(v.getNumberOfVectors()==this.getNumberOfVectors()){
            // Stores the Array of Vectors that will be used to create the Result State Vector
            Vector[] arrayOfVectors = new Vector[this.getNumberOfVectors()];

            // Iterate over the Vectors of the 2 State Vectors and Add them
            for (int vi = 0; vi < this.getNumberOfVectors(); vi++) {

                // Here we are going through each Vector within the State Vectors

                // Will store the Result of Adding the 2 Vectors from the ith position in the State Vector
                Vector resultantVector = this.getVector(vi).add(v.getVector(vi));
                arrayOfVectors[vi] = resultantVector;
            }

            return new StateVector(arrayOfVectors);
        }
        else throw new IllegalArgumentException("State Vectors must have the same number of Vectors to be added.");

    }

    /**
     * Computes the difference between two vectors
     * @param v the vector to be subtracted
     * @return the resultant vector when subtracting v
     */
    public StateVector subtract(StateVector v){

        if(v.getNumberOfVectors()==this.getNumberOfVectors()){
            // Stores the Array of Vectors that will be used to create the Result State Vector
            Vector[] arrayOfVectors = new Vector[this.getNumberOfVectors()];

            // Iterate over the Vectors of the 2 State Vectors and Add them
            for (int vi = 0; vi < this.getNumberOfVectors(); vi++) {

                // Here we are going through each Vector within the State Vectors

                // Will store the Result of Adding the 2 Vectors from the ith position in the State Vector
                Vector resultantVector = this.getVector(vi).subtract(v.getVector(vi));
                arrayOfVectors[vi] = resultantVector;
            }

            return new StateVector(arrayOfVectors);
        }
        else throw new IllegalArgumentException("State Vectors must have the same number of Vectors to be added.");
    }

    /**
     * Computes the product of a scalar multiplication
     * @param scalar a double that scales the state vector
     * @return the scalar product of the state vector and the scalar
     */
    public StateVector multiply(double scalar){
        // Stores the Array of Vectors that will be used to create the Result State Vector
        Vector[] arrayOfVectors = new Vector[this.getNumberOfVectors()];

        // Iterate over the Vectors of the 2 State Vectors and Add them
        for (int vi = 0; vi < this.getNumberOfVectors(); vi++) {

            // Here we are going through each Vector within the State Vectors

            // Will store the Result of Adding the 2 Vectors from the ith position in the State Vector
            Vector resultantVector = this.getVector(vi).multiply(scalar);
            arrayOfVectors[vi] = resultantVector;
        }

        return new StateVector(arrayOfVectors);
    }

    /**
     * Checks whether two StateVectors are equivalent
     * @param v StateVector to make comparison with
     * @return true if all elements are equivalent, otherwise false
     */
    public boolean isEqual(StateVector v){
        for(int i=0; i<v.getNumberOfVectors();i++){
            if(!(v.getVector(i).isEqual(this.getVector(i)))) return false;
        }
        return true;
    }

    public String toString(){
        String re = "";
        for(int i = 0; i<this.numberOfVectors;i++){
            for(int j = 0; j<this.numberOfDimensions;j++){
                re += this.getVector(i).get(j);
                re += ", ";
            }
            re += "\n";
        }
        return re;
    }

    public StateVector setStateVector(StateVector sv){
        Vector[] v = new Vector[sv.getNumberOfVectors()];
        for(int i =0; i< sv.getNumberOfVectors();i++) {
            v[i] = sv.getVector(i);
        }
        StateVector n = new StateVector(v);
        return n;
    }

    // Testing the Functionalities of the StateVector
    public static void main(String[] args) {

        // Creating Vectors
        Vector v1 = new Vector(new double[]{2, 4, 6});
        Vector v2 = new Vector(new double[]{1, 2, 3});

        // Setting up the State Vectors
        StateVector stateVector1 = new StateVector(new Vector[]{v1});
        StateVector stateVector2 = new StateVector(new Vector[]{v2});

        // Trying to Add V1 and V2
        System.out.println("Resultant State Vector of adding SV1 and SV2:");
        StateVector sumOfStateVectors = stateVector1.add(stateVector2);
        for (int i = 0; i < 3; i++) {
            System.out.println(sumOfStateVectors.getVector(0).get(i));
        }

        System.out.println("Checking the Value of SV1 after the addition (should not change!):");

        // Checking the values of State Vector 1 after the Adddition
        for (int i = 0; i < 3; i++) {
            System.out.println(stateVector1.getVector(0).get(i));
        }

        System.out.println("State Vector that Represents the SV1 multiplied by a Scalar of 10: ");

        // Trying to Multiply StateVector by a Scalar
        StateVector scaledStateVector1 = stateVector1.multiply(10);
        for (int i = 0; i < 3; i++) {
            System.out.println(scaledStateVector1.getVector(0).get(i));
        }

        System.out.println("Checking the Values of SV1 after the Multiplication(should not change!):");
        // Check if the Values of V1 have changed after the Multiplication
        for (int i = 0; i < 3; i++) {
            System.out.println(stateVector1.getVector(0).get(i));
        }

        // Testing the Subtract Method
        System.out.println("Resultant State vector of the Subtraction of SV2 and SV1");
        StateVector subtractedStateVector = stateVector2.subtract(stateVector1);
        for (int i = 0; i < 3; i++) {
            System.out.println(subtractedStateVector.getVector(0).get(i));
        }

    }

}
