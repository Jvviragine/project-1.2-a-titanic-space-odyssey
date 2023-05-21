package physics.solvers;

import physics.functions.Function;
import physics.functions.TestODEDerivativeFunction;
import physics.vectors.Vector;
import physics.vectors.StateVector;

public class EulerSolver implements Solver{
    static double t;

    public EulerSolver(){

    }

    /**
     * Applies Euler's method to the current state vector
     *      yn+1 = yn + hf(t,y)
     * @param function the derivative function of the equation
     * @param initialCondition the position and velocity at t0
     * @param t0 the time from which the Euler steps are calculated
     * @param tf the time until which the Euler steps are calculated
     * @param stepSize the time interval at which evaluations of the derivative are made
     * @return the state vector at the end of the time period tf - t0
     */
    public StateVector solve(Function function, StateVector initialCondition, double t0, double tf, double stepSize) {

        StateVector currentState = initialCondition;

        //Solve Euler for the time period tf-t0
        for(double t=t0; t<tf; t+=stepSize){
            //Get derivative, f(t,y) of the function
            StateVector derivative = function.applyFunction(currentState,t);

            //h * f(t,y)
            StateVector hfty = derivative.multiply(stepSize);

            //yn + hf(t,y)
            StateVector y1 = currentState.add(hfty);

            //Set y1 to y0 for next iteration
            currentState = y1;

            //Update solver time
            this.t +=stepSize;
        }

        return currentState;

    }

    // Testing the Euler Solver for y'=y, and y(0)=1, for which we know the exact solution y=eˆt
    public static void main(String[] args) {

        // Defining and Getting the f' = f(t,y) = dy/dt
        Function dydt = new TestODEDerivativeFunction();

        // Defining the Initial Condition y(0)=1
        double t0 = 0, tf = 1;
        Vector y0 = new Vector(new double[]{1}); // Vector that represents y(o)=1
        StateVector svY0 = new StateVector(new Vector[]{y0}); // Embeds this Vector into a State Vector

        // Defines the step Size
        double h = 0.000001;

        // Calls the Euler Solver to Numerically solve y'=t with y(0)=1 and h=0.1
        Solver eulerSolver = new EulerSolver();
        StateVector svYf = eulerSolver.solve(dydt, svY0, t0, tf, h);

        // Prints wt at tf
        System.out.println(svYf.getVector(0).get(0));


    }

}
