package celestial_bodies;
import physics.vectors.Vector;

import javax.swing.*;
import java.util.ArrayList;

public class CelestialBody {
    private String name;
    private Vector initialState;
    private double mass;
    private Vector currentState;
    private JLabel celestialBodyLabel;

    public static ArrayList<CelestialBody> bodies;

    public CelestialBody(String name, Vector initialState, double mass, JLabel celestialBodyLabel){
        this.name = name;
        this.initialState = initialState;
        this.mass = mass;
        this.celestialBodyLabel = celestialBodyLabel;
        bodies.add(this);
    }

    public String getName(){
        return this.name;
    }

    public Vector getInitialState(){
        return this.initialState;
    }

    public double getMass(){
        return this.mass;
    }

    public void setCurrentState(Vector currentState){
        this.currentState = currentState;
    }

    public Vector getCurrentState(){
        return currentState;
    }

    public static ArrayList<CelestialBody> getBodies() {
        return bodies;
    }

    public JLabel getCelestialBodyLabel() {
        return celestialBodyLabel;
    }

    //updates the position of the celestial object's image according to the given vector
    public JLabel updateImagePosition(Vector vector) {
        //TODO : update the  position of the celestialBodyLabel
        return this.celestialBodyLabel;
    }

}
