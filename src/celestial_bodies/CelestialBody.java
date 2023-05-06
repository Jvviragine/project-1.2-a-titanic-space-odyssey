package celestial_bodies;
import physics.vectors.Vector;
import java.util.ArrayList;

public class CelestialBody {
    private String name;
    private Vector initialState;
    private double mass;

    private Vector currentState;

    public static ArrayList<CelestialBody> bodies;

    public CelestialBody(String name, Vector initialState, double mass){
        this.name = name;
        this.initialState = initialState;
        this.mass = mass;
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

}
