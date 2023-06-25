package landing;

import physics.vectors.StateVector;
import physics.vectors.Vector;
import stochastic_wind_simulation.WindModel;

import java.util.ArrayList;

public class FeedbackController {

    //Gravitational pull on Titan in m and km. If using km remember to convert at the begging if using meter convert at the end!!!

    //private double G = -1.352 * Math.pow(10,-3); // km/s² = N/kg
    public static final double G = -1.352; // m/s² = N/kg
    private Vector target = new Vector(new double[]{0,0});
    private double h = 0.2;

    private LanderState landerState;

    private ArrayList<LanderState> landingStates = new ArrayList<>();

    public FeedbackController(LanderState state){
        this.landerState = state;
    }

    public ArrayList<LanderState> getLanding(){

        LandingFunction f = new LandingFunction();
        ArrayList<LanderState> descents = new ArrayList<>();
        descents.add(landerState);
        double theta = landerState.getThetaPos();
        double u = landerState.getU();

        WindModel wind = new WindModel();


            while (Math.abs(landerState.getPos().get(0)) > 0.1 || landerState.getPos().get(1) != 0) {

                Vector windVector = wind.getWindSpeed(landerState.getPos().get(1));

                theta = f.calculateTheta(landerState);
                u = f.calculateU(landerState, theta);
                if(Math.abs(u)>Math.abs(10*G)) u=10*G;
                System.out.println(u);
                LanderState current = landerState;

                current = f.LanderStep(current, h);
                if(Math.signum(current.getPos().get(0)) == -Math.signum(landerState.getPos().get(0))) {
                    landerState = new LanderState(landerState.getState(), u, landerState.getTorque());
                    landerState.setVel(new Vector(new double[]{-landerState.getPos().get(0)/h, landerState.getVel().get(1), landerState.getThetaVel()}));
                }
                if(current.getPos().get(1) < 0 && current.getPos().get(0) == 0){
                    landerState = new LanderState(landerState.getState(), u, landerState.getTorque());
                    landerState.setVel(new Vector(new double[]{landerState.getVel().get(0), -landerState.getPos().get(1)/h, landerState.getThetaVel()}));
                }else if(current.getPos().get(1) < 0){
                    landerState.setVel(new Vector(new double[]{landerState.getVel().get(0), 0, landerState.getThetaVel()}));
                }
                if(landerState.getPos().get(0) == 0){
                    landerState = new LanderState(landerState.getState(), u, landerState.getTorque());
                    landerState.setVel(new Vector(new double[]{0, landerState.getVel().get(1), landerState.getThetaVel()}));
                }
                if(landerState.getPos().get(1) == 0){
                    landerState = new LanderState(landerState.getState(), u, landerState.getTorque());
                    landerState.setVel(new Vector(new double[]{ landerState.getVel().get(0), 0, landerState.getThetaVel()}));
                }

                landerState.setVel(new Vector(new double[]{landerState.getVel().get(0) - windVector.get(1), landerState.getVel().get(1), landerState.getThetaVel()}));
                landerState = f.LanderStep(landerState, h);
                System.out.println(landerState.getStateToString());

                descents.add(landerState);
            }

        //calculate the vector pointing to the target to get theta and calculate acceleration
        return descents;


    }

    public double[][] getPath(){
        ArrayList<LanderState> states = getLanding();
        double[][] path = new double[states.size()][2];
        for(int i = 0;i<states.size();i++){
            path[i][0] = states.get(i).getPos().get(0);
            path[i][1] = states.get(i).getPos().get(1);
        }
        return path;
    }



    public static void main(String[] args) {
        StateVector s = new StateVector(new Vector[]{new Vector(new double[]{-100, 50,0}), new Vector(new double[]{0,0,0})});
        LanderState l = new LanderState(s, 10*G, 0);
        FeedbackController controller = new FeedbackController(l);
//        double[][] path = controller.getPath();
//        for(int i = 0; i<path.length;i++){
//            System.out.println(path[i][0]+" "+ path[i][1]);
//        }
        ArrayList<LanderState> descents = controller.getLanding();
        for(int i = 0; i<descents.size();i++){
            System.out.println(descents.get(i).getStateToString());
        }

    }


}
