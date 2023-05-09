package physics.vectors;

public class Vector {

    private double[]state;
    private int dimension;
    public Vector(double[] initialState){
        if (initialState.length > 0) {
            this.state = initialState;
            this.dimension = initialState.length;
        }
        else {
            throw new IllegalArgumentException("An Empty Vector cannot be created! Vectors must have at least 1 Dimension.");
        }
    }

    /**
     * Getter for the coordinates/velocities of the vector
     * @return double for corresponding coordinate/velocity
     */
    public double get(int index){
        return state[index];
    }

    /**
     * Gets the dimension of the vector
     * @return dimension of vector (n x 1)
     */
    public int getDimension() {
        return state.length;
    }

    /**
     * Setters for the coordinates/velocities of the vector
     * @param index,value the value to place in the vector and its index
     */

    public void set(int index, double value){
        state[index] = value;
    }

    /**
     * Computes the sum of two vectors
     * @param v the vector to be added
     * @return the resultant vector when adding v
     */
    public Vector add(Vector v){
        //New vector to be returned
        Vector u = new Vector(state);

        //Loop through all coordinates in vector
        for(int i= 0; i<state.length;i++){
            u.state[i] += v.state[i];
        }

        //Return new vector
        return u;
    }

    /**
     * Computes the difference between two vectors
     * @param v the vector to be subtracted
     * @return the resultant vector when subtracting v
     */
    public Vector subtract(Vector v){
        //New vector to be returned
        Vector u = new Vector(state);

        //Loop through all coordinates in vector
        for(int i= 0; i<state.length;i++){
            u.state[i] -= v.state[i];
        }

        //Return new vector
        return u;
    }

    /**
     * Computes the product of a scalar multiplication
     * @param scalar a double that scales the vector
     * @return the scalar product of the vector and the scalar
     */
    public Vector multiply(double scalar){
        //New vector to be returned
        Vector u = new Vector(state);

        //Loop through all coordinates in vector
        for(int i=0;i<state.length;i++){
            u.state[i] *= scalar;
        }

        //Return new vector
        return u;
    }

    /**
     * Computes the magnitude of the vector
     * @return the magnitude of the vector
     */
    public double getMagnitude(){
        double sum=0;
        for(int i=0;i<state.length;i++){
            sum += Math.pow(state[i],2);
        }
        return Math.sqrt(sum);
    }

    /**
     * Computes the Euclidean distance between two vectors
     * @param v the vector to make distance comparison
     * @return double representing the Euclidean distance
     */
    public double distance(Vector v){
        double diffsquared = 0;
        for(int i=0;i<state.length;i++){
            diffsquared += Math.pow(state[i]-v.state[i],2);
        }
        return Math.sqrt(diffsquared);
    }

}
