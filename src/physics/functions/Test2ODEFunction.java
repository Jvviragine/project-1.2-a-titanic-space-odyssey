package physics.functions;

import physics.vectors.StateVector;
import physics.vectors.Vector;

public class Test2ODEFunction implements Function{


    /**
     * Simple Function y'=y , y(0) = 1, and y = e^t
     * @param y
     * @param t
     * @return
     */
    @Override
    public StateVector applyFunction(StateVector y, double t) {

        double valueAtY = y.getVector(0).get(0);
        double newValueAtY = -2 + 2*Math.pow(Math.E, (0.5*Math.pow(t, 2) + t -4)); // Applies the Formula for the Derivative

        Vector v = new Vector(new double[]{newValueAtY});
        StateVector sv = new StateVector(new Vector[]{v});

        return sv;
    }

    public void resetState(StateVector[] stateVectors){
        //Do nothing
    }

    public static void main(String[] args) {

        // Testing the Evaluation of the Function
        Function f = new Test2ODEFunction();
        Vector v = new Vector(new double[]{0});
        StateVector sv = new StateVector(new Vector[]{v});
        System.out.println(f.applyFunction(sv, Math.PI).getVector(0).get(0));
    }
}
