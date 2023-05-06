package physics.solvers;

import physics.vectors.Vector;

public class EulerSolver implements Solver{
    Vector y0;
    Vector fty;
    double h;

    /**
     * Constructor for an Euler solver
     * @param y0 the initial state vector on which to perform Euler's method
     * @param fty the derivate f(t,y) of the current state vector
     * @param h the step size
     */
    public EulerSolver(Vector y0, Vector fty, double h){
        this.y0 = y0;
        this.fty = fty;
        this.h = h;
    }

    /**
     * Applies Euler's method to the current state vector
     *      yn+1 = yn + hf(t,y)
     * @return the next state vector at state yn+1
     */
    public Vector solve() {
        //h * f(t,y)
        Vector hfty = fty.multiply(h);

        //yn + hf(t,y)
        Vector y1 = y0.add(hfty);

        this.y0 = y1; //set y0 for next round
        return y1;
    }

}
