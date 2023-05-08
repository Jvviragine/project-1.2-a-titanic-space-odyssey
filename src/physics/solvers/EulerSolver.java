package physics.solvers;

import physics.functions.Function;
import physics.vectors.Vector;
import physics.vectors.StateVector;

public class EulerSolver implements Solver{
    StateVector y0;
    StateVector fty;
    double h;

    static double t;

    /**
     * Constructor for an Euler solver
     * @param y0 the initial state vector on which to perform Euler's method
     * @param fty the derivative f(t,y) of the current state vector
     * @param h the step size
     * @param t0 the initial time
     */

    //y0, function f, h, t0
    public EulerSolver(StateVector y0, StateVector fty, double h, double t0){
        this.y0 = y0;
        this.fty = fty;
        this.h = h;
        this.t = t0;
    }

    /**
     * Applies Euler's method to the current state vector
     *      yn+1 = yn + hf(t,y)
     * @return the next state vector at state yn+1
     */
    public StateVector solve(Function function, StateVector initialCondition, double t0, double tf, double stepSize) {
        int numberOfVectors = y0.getNumberOfVectors();
        Vector[] yn = y0.getStateVector();
        Vector[] fty = this.fty.getStateVector();
        Vector[] newState = new Vector[2];

        //f.apply(time, state);

        //Solve Euler for each vector in the state vector
        for(int i=0;i<numberOfVectors;i++){
            //h * f(t,y)
            Vector hfty = fty[i].multiply(h);

            //yn + hf(t,y)
            Vector y1 = yn[i].add(hfty);

            //Add vector to vector list
            newState[i] = y1;
        }

        //New state vector produced by Euler step
        StateVector ynplus1 = new StateVector(newState);

        //Update time
        t+=h;

        return ynplus1;
    }


}
