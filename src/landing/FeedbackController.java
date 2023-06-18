package landing;

import physics.vectors.StateVector;
import physics.vectors.Vector;

import java.util.ArrayList;

public class FeedbackController {

    //Gravitational pull on Titan in m and km. If using km remember to convert at the begging if using meter convert at the end!!!

    //private double G = -1.352 * Math.pow(10,-3); // km/s² = N/kg
    private final double G = -1.352; // m/s² = N/kg
    private Vector target = new Vector(new double[]{0,0});

    private LanderState landerState;

    private ArrayList<LanderState> landingStates = new ArrayList<>();

    public FeedbackController(LanderState state){
        this.landerState = state;
    }

    public ArrayList<LanderState> getLanding(){

        LandingFunction f = new LandingFunction();
        ArrayList<LanderState> descents = new ArrayList<>();
        descents.add(landerState);

        //calculate the vector pointing to the target to get theta and calculate acceleration
        double theta = f.calculateTheta(landerState);
        double u = f.calculateU(landerState, theta);
        landerState.setThetaPos(theta);
        landerState = new LanderState(landerState.getState(),u,landerState.getTorque());
        landerState = f.LanderStep(landerState,1);
        descents.add(landerState);

         theta = f.calculateTheta(landerState);
         u = f.calculateU(landerState, theta);
        landerState.setThetaPos(theta);
        landerState = new LanderState(landerState.getState(),u,landerState.getTorque());
        landerState = f.LanderStep(landerState,1);
        descents.add(landerState);


        return descents;

    }


    public double dotProduct(Vector v1, Vector v2){
        double sum = 0;
        for(int i = 0; i<v1.getDimension();i++){
            sum += v1.get(i)*v2.get(i);
        }
        return sum;
    }


    public Vector getDirectionVector(Vector point, Vector target){
        Vector r = point.subtract(target);
        return r;
    }

    public Vector getNorm(Vector v, double norm){
        double magnitude = v.getMagnitude();
        for(int i=0; i<v.getDimension();i++){
            v.set(i, v.get(i)/magnitude);
        }
        v = v.multiply(norm);
        return v;
    }

    public static void main(String[] args) {
        StateVector s = new StateVector(new Vector[]{new Vector(new double[]{10,15,0}), new Vector(new double[]{0,0,0})});
        LanderState l = new LanderState(s, 0, 0);
        FeedbackController controller = new FeedbackController(l);
        ArrayList<LanderState> descents = controller.getLanding();
        for(int i = 0; i<descents.size();i++){
            System.out.println(descents.get(i).getStateToString());
        }

    }


}
