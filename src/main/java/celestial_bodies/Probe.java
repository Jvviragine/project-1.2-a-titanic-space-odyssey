package celestial_bodies;
import physics.vectors.Vector;
public class Probe {
    private Vector initialState;

    public Probe(String name, Vector initialState, double mass){
        this.initialState = initialState;
    }
}
