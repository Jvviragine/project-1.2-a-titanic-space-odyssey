package physics.vectors;

public class Vector {
    private double x1,x2,x3;
    private double v1,v2,v3;

    /**
     * Getters for the coordinates/velocities of the vector
     * @return double for corresponding coordinate/velocity
     */
    public double getX1(){
        return x1;
    }

    public double getX2(){
        return x2;
    }

    public double getX3(){
        return x3;
    }

    public double getV1(){
        return v1;
    }

    public double getV2(){
        return v2;
    }

    public double getV3(){
        return v3;
    }

    /**
     * Setters for the coordinates/velocities of the vector
     * @param x1 the input for the corresponding coordinate/velocity
     */
    public void setX1(double x1){
        this.x1 = x1;
    }

    public void setX2(double x2){
        this.x2 = x2;
    }

    public void setX3(double x3){
        this.x3 = x3;
    }

    public void setV1(double v1){
        this.v1 = v1;
    }

    public void setV2(double v2){
        this.v2 = v2;
    }

    public void setV3(double v3){
        this.v3 = v3;
    }

    /**
     * Computes the sum of two vectors
     * @param v the vector to be added
     * @return the resultant vector when adding v
     */
    public Vector add(Vector v){
        Vector u = new Vector();
        u.setX1(x1 + v.getX1());
        u.setX2(x2 + v.getX2());
        u.setX3(x3 + v.getX3());
        u.setV1(v1 + v.getV1());
        u.setV2(v2 + v.getV2());
        u.setV3(v3 + v.getV3());
        return v;
    }

    /**
     * Computes the difference between two vectors
     * @param v the vector to be subtracted
     * @return the resultant vector when subtracting v
     */
    public Vector subtract(Vector v){
        Vector u = new Vector();
        u.setX1(x1 - v.getX1());
        u.setX2(x2 - v.getX2());
        u.setX3(x3 - v.getX3());
        u.setV1(v1 - v.getV1());
        u.setV2(v2 - v.getV2());
        u.setV3(v3 - v.getV3());
        return v;
    }

    /**
     * Computes the product of a scalar multiplication
     * @param scalar a double that scales the vector
     * @return the scalar product of the vector and the scalar
     */
    public Vector multiply(double scalar){
        Vector v = new Vector();
        v.setX1(x1*scalar);
        v.setX2(x2*scalar);
        v.setX3(x3*scalar);
        v.setV1(v1*scalar);
        v.setV2(v2*scalar);
        v.setV3(v3*scalar);
        return v;
    }

    /**
     * Computes the magnitude of the position
     * @return the magnitude of the position sub-vector
     */
    public double positionMagnitude(){
        return Math.sqrt(Math.pow(x1, 2) + Math.pow(x2, 2) + Math.pow(x3, 2));
    }

    /**
     * Computes the magnitude of the velocity
     * @return the magnitude of the velocity sub-vector
     */
    public double velocityMagnitude(){
        return Math.sqrt(Math.pow(v1, 2) + Math.pow(v2, 2) + Math.pow(v3, 2));
    }

    /**
     * Computes the Euclidean distance between two vectors
     * @param v the vector to make distance comparison
     * @return double representing the Euclidean distance
     */
    public double distance(Vector v){
        return Math.sqrt(Math.pow(x1 - v.getX1(), 2) + Math.pow(x2 - v.getX2(), 2) + Math.pow(x3 - v.getX3(), 2));
    }

}
