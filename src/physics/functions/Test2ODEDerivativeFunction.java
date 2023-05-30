package physics.functions;

import physics.vectors.StateVector;
import physics.vectors.Vector;

public class Test2ODEDerivativeFunction implements Function{
    /**
     * Test Function for the ODE y'= Cos(t)*Sqrt(y + 1), y(pi) = 0, y'=f(t,y)
     * @param y
     * @param t
     * @return
     */
    @Override
    public StateVector applyFunction(StateVector y, double t) {
        // Tests to make sure that there is only 1 Vector and that this vector is 1D
        double valueAtY = y.getVector(0).get(0); // This is a 1D Derivative with 1 State Vector
        double newValueAtY = t*valueAtY + 2*t + valueAtY + 2; // Applies the Formula for the Derivative

        Vector v = new Vector(new double[]{newValueAtY});
        StateVector sv = new StateVector(new Vector[]{v});

        return sv;
    }

    public void resetState(StateVector[] stateVectors){
        //Do nothing
    }

    public static void main(String[] args) {

        // Testing the Evaluation of the Derivative of the Function y'= Cos(t)*Sqrt(y + 1)
        Function dydt = new Test2ODEDerivativeFunction();
        Vector v = new Vector(new double[]{Math.PI});
        StateVector sv = new StateVector(new Vector[]{v});
        System.out.println(dydt.applyFunction(sv, 10).getVector(0).get(0));
    }
}
