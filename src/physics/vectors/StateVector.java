package physics.vectors;

/**
 * Stores the State Vector of the Differential Equation  of Interest
 *
 * In case it is an ODE in just One Dimension, it simply creates a 1D Vector
 *
 * For the Solar System, it will be a 6D Vector (Velocity and Position), each with the 3 Spatial Dimensions
 */
public class StateVector {

    /**
     * Stores the Vector itself as an Array, for O(1) indexing
     */
    private double[] vector;

    /**
     * Constructor for the State Vector
     * @param array that represents the Vector. Cannot be Empty!
     */
    public StateVector(double[] array) {
        if (array.length > 0) {
            vector = array;
        }
        else {
            System.out.println("Your Vector is Empty! Vector must have at least 1 Dimension");
        }
    }


}
