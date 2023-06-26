package physics.controllers;

import physics.vectors.StateVector;
import physics.vectors.Vector;
import stochastic_wind_simulation.WindModel;

import java.util.ArrayList;

public class FeedbackController implements Controller{

    //Gravitational pull on Titan in m and km. If using km remember to convert at the begging if using meter convert at the end!!!

    //private double G = -1.352 * Math.pow(10,-3); // km/s² = N/kg
    public static final double G = -1.352; // m/s² = N/kg
    private Vector target = new Vector(new double[]{0,0});
    private double h = 0.1;

    private LanderState landerState;

    private ArrayList<LanderState> landingStates = new ArrayList<>();

    public FeedbackController(LanderState state){
        this.landerState = state;
    }

    /**
     * Method handling the landing of the module by analyzing its state at each step and applying needed changes for a safe descent.
     * @return
     */
    public ArrayList<LanderState> getLanding(){

        LandingFunction f = new LandingFunction();
        ArrayList<LanderState> descents = new ArrayList<>();
        descents.add(landerState);
        double theta = landerState.getThetaPos();
        double u = landerState.getU();
        double torque = landerState.getTorque();
        double acceptableTorque = (180/Math.PI)*h;
        double ground = 2;

        WindModel wind = new WindModel();


            while (Math.abs(landerState.getPos().get(0)) != 0 || landerState.getPos().get(1) != 0) {

                //generating a random wind force at present height and moving our module with the wind
                Vector windVector = wind.getWindSpeed(landerState.getPos().get(1));
                landerState.setVel(new Vector(new double[]{landerState.getVel().get(0) - windVector.get(0), landerState.getVel().get(1), landerState.getThetaVel()}));

//                theta = f.calculateTheta(landerState);
//                landerState.setThetaPos(theta);
//                u = f.calculateU(landerState, theta);
//                if (Math.abs(u) > Math.abs(10 * G)) u = 10 * G;
//                landerState.setU(u);

                LanderState current = new LanderState(landerState.getState(),landerState.getU(),landerState.getTorque());
                current = f.LanderStep(current, h);

                //checking whether any of the coordinates go over their axis or we are already at 0 at x
                if (Math.signum(current.getPos().get(0)) == -Math.signum(landerState.getPos().get(0))) {
                    landerState = new LanderState(landerState.getState(), u, landerState.getTorque());
                    landerState.setVel(new Vector(new double[]{-landerState.getPos().get(0)/h, landerState.getVel().get(1), landerState.getThetaVel()}));
                }
                if (current.getPos().get(1) < ground && landerState.getPos().get(0) == 0) {
                    landerState = new LanderState(landerState.getState(), u, landerState.getTorque());
                    landerState.setVel(new Vector(new double[]{landerState.getVel().get(0), -landerState.getPos().get(1) / h, landerState.getThetaVel()}));
                }else if (current.getPos().get(1) < ground) {
                    landerState.setVel(new Vector(new double[]{landerState.getVel().get(0), 0, landerState.getThetaVel()}));
                    theta = landerState.getThetaPos();
                }
                if (landerState.getPos().get(0) == 0){
                    landerState = new LanderState(landerState.getState(), u, landerState.getTorque());
                    landerState.setVel(new Vector(new double[]{0, landerState.getVel().get(1), landerState.getThetaVel()}));
                }

                landerState = f.LanderStep(landerState, h);
                landerState.setThetaPos(theta);


                //adding the state to our list of descents
                descents.add(landerState);

            }

            //stopping the module completely
            if(landerState.getPos().get(0) == 0 && landerState.getPos().get(1) == 0){
                landerState = new LanderState(landerState.getState(), u, landerState.getTorque());
                landerState.setVel(new Vector(new double[]{0, 0, landerState.getThetaVel()}));
            }
            descents.add(landerState);

        //adding the state to our list of descents
          return descents;


    }


    //method returning a path represented by x, y coordinates in an array
    public double[][] getPath(){
        ArrayList<LanderState> states = getLanding();
        double[][] path = new double[states.size()][2];
        for(int i = 0;i<states.size();i++){
            path[i][0] = states.get(i).getPos().get(0);
            path[i][1] = states.get(i).getPos().get(1);
        }
        return path;
    }

    public Vector getDirectionVector(Vector v, Vector u){
        Vector r = new Vector(new double[v.getDimension()]);
        for(int i = 0;i<v.getDimension();i++){
            r.set(i, u.get(i) - v.get(i));
        }
        return r;
    }



    public static void main(String[] args) {
        StateVector s = new StateVector(new Vector[]{new Vector(new double[]{550, 30, 0}), new Vector(new double[]{
                0,0, 0})});
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


    @Override
    public void startControl() {

    }
}
