package celestial_bodies;
import physics.vectors.Vector;
import physics.vectors.StateVector;

import javax.swing.*;
import java.util.ArrayList;

/**
 * Class to represent a Celestial Body within the Physics Simulation
 * This Class serves the Purposes needed for the Physics Simulation, being Independent from the GUI
 */
public class CelestialBody {
    private String name;
    private StateVector initialState;
    private double mass;
    //private Vector currentState;

    //public static ArrayList<CelestialBody> bodies;

    public CelestialBody(String name, StateVector initialState, double mass){
        this.name = name;
        this.initialState = initialState;
        this.mass = mass;
        //bodies.add(this);
    }

    public String getName(){
        return this.name;
    }

    public StateVector getInitialState(){
        return this.initialState;
    }

    public double getMass(){
        return this.mass;
    }

}
