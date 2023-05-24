package physics.functions;

import physics.vectors.StateVector;
import physics.vectors.Vector;

public class TestODEFunction implements Function{


    /**
     * Simple Function y'=y , y(0) = 1, and y = e^t
     * @param y
     * @param t
     * @return
     */
    @Override
    public StateVector applyFunction(StateVector y, double t) {

        double e = Math.exp(1);
        // Tests to make sure that there is only 1 Vector and that this vector is 1D
        if (y.getNumberOfVectors() == 1 && y.getNumberOfDimensions() == 1) {
            double value = Math.pow(e, t);
            Vector vector = new Vector(new double[]{value});
            StateVector resultStateVector = new StateVector(new Vector[]{vector});
            return resultStateVector;
        }
        else {
            return y;
        }
    }

    public void resetState(StateVector[] stateVectors){
        //Do nothing
    }

    public static void main(String[] args) {

        // Testing the Evaluation of the Function y = e^t
        Function f = new TestODEFunction();
        Vector v = new Vector(new double[]{0});
        StateVector sv = new StateVector(new Vector[]{v});
        System.out.println(f.applyFunction(sv, 1).getVector(0).get(0));
    }
}
