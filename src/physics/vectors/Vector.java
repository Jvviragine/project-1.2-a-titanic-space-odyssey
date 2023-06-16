package physics.vectors;

public class Vector {

    private double[]state;
    private int dimension;

    public static void main(String[] args){
        double[] s = {1,2,3};
        double[] t = {1,2,3};
        Vector u = new Vector(s);
        Vector v = u.copyOf();
        Vector w = u.subtract(v);
        u = v.add(u);

        for(int i=0; i <u.getDimension();i++){
            System.out.println("U position " + i + ":" + u.state[i]);
            System.out.println("V position " + i + ":" + v.state[i]);
            System.out.println("W position " + i + ":" + w.state[i]);
        }
    }

    /**
     * Constructor
     * @param initialState (double) number entries in the vector
     */
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

    public Vector setAll(double value){
        for(int i = 0; i<state.length; i++){
            set(i, value);
        }
        return this;
    }

    /**
     * Computes the sum of two vectors
     * @param v the vector to be added
     * @return the resultant vector when adding v
     */
    public Vector add(Vector v){
        if(v.getDimension() == this.getDimension()){

        // Creates a Copy of the Vector in which the Function is called upon
        double[] sumOfValues = new double[v.getDimension()];

        // Iterates over the entries of Both Vectors, adding them together
        for (int i = 0; i < v.getDimension(); i++) {
            sumOfValues[i] = this.get(i) + v.get(i);
        }

        return new Vector(sumOfValues);

        }
        else{
            throw new IllegalArgumentException("Vectors must have the same dimensions to be added.");

        }
    }

    /**
     * Computes the difference between two vectors
     * @param v the vector to be subtracted
     * @return the resultant vector when subtracting v
     */
    public Vector subtract(Vector v){
        if(v.getDimension() == this.getDimension()){

            // Creates a Copy of the Vector in which the Function is called upon
            double[] differenceOfValues = new double[v.getDimension()];

            // Iterates over the entries of Both Vectors, adding them together
            for (int i = 0; i < v.getDimension(); i++) {
                differenceOfValues[i] = this.get(i) - v.get(i);
            }

            return new Vector(differenceOfValues);
        }
        else{
            throw new IllegalArgumentException("Vectors must have the same dimensions to be subtracted.");
        }
    }

    /**
     * Computes the product of a scalar multiplication
     * @param scalar a double that scales the vector
     * @return the scalar product of the vector and the scalar
     */
    public Vector multiply(double scalar){

        // Creates a Copy of the Vector in which the Function is called upon
        double[] scalledValues = new double[this.getDimension()];
        // Iterates over the entries of Both Vectors, adding them together
        for (int i = 0; i < this.getDimension(); i++) {
            scalledValues[i] = this.get(i) * scalar;
        }

        return new Vector(scalledValues);
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

    /**
     * Computes the dot product of two vectors
     * @param v the vector with which to do the dot product
     * @return dot product of this and v
     */
    public double dotProduct(Vector v){
        double product = 0;
        for(int i = 0; i < this.getDimension(); i++){
            product += this.get(i) * v.get(i);
        }
        return product;
    }

    /**
     * Makes a copy of a vector
     * @return vector copy
     */
    public Vector copyOf(){
        int dimension = this.getDimension();
        double[] u = new double[dimension];
        for(int i = 0; i < dimension; i++){
            u[i] = this.get(i);
        }
        return new Vector(u);
    }

    /**
     * Checks whether two Vectors are equivalent
     * @param v Vector to make comparison with
     * @return true if all elements are equivalent, otherwise false
     */
    public boolean isEqual(Vector v){
        for(int i = 0; i < v.getDimension(); i++){
            if(!(v.get(i) == this.get(i)))return false;
        }
        return true;
    }

    public String toString(){
        String r = "";
        for(int i = 0;i<state.length;i++){
            r += state[i] +", ";
        }
        return r;
    }


}


