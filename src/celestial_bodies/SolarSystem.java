package celestial_bodies;

import physics.vectors.StateVector;

public class SolarSystem {
    private double[] masses;
    private StateVector[] stateVectors;

    private String[] names;

    public final double G = 6.6743*Math.pow(10,-20);

    public SolarSystem(StateVector[] stateVectors, double[] masses, String[] names){
        this.stateVectors = stateVectors;
        this.masses = masses;
        this.names = names;
    }

    public double[] getMasses(){
        return masses;
    }

    public void setStateVectors(StateVector [] stateVectors){
        this.stateVectors = stateVectors;
    }

    public StateVector[] getStateVectors(){
        return stateVectors;
    }

    public int getIndex(StateVector v){
        for(int i = 0; i < stateVectors.length; i++){
            if(v == stateVectors[i]){
                return i;
            }
        }
        return -1;
    }

    public String[] getBodyNames(){
        return names;
    }

    public int totalBodies(){
        return names.length;
    }
}
