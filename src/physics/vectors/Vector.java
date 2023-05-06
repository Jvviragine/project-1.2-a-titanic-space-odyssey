package physics.vectors;

public class Vector {

    private double[]state;

    public Vector(double[] initialState){
        if (initialState.length > 0) {
            this.state = initialState;
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
        for(int i= 0; i<state.length;i++){
            state[i] += v.state[i];
        }
        return this;
    }

    /**
     * Computes the difference between two vectors
     * @param v the vector to be subtracted
     * @return the resultant vector when subtracting v
     */
    public Vector subtract(Vector v){
        for(int i= 0; i<state.length;i++){
            state[i] -= v.state[i];
        }
        return this;
    }

    /**
     * Computes the product of a scalar multiplication
     * @param scalar a double that scales the vector
     * @return the scalar product of the vector and the scalar
     */
    public Vector multiply(double scalar){
        for(int i=0;i<state.length;i++){
            state[i] *= scalar;
        }
        return this;
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
