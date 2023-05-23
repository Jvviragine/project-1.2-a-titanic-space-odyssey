package solar_system_data;
import celestial_bodies.CelestialBody;
import physics.vectors.StateVector;
import physics.vectors.Vector;

/**
 * Stores the Data about the Celestial Bodies
 * All the Data about the Celestial Bodies will be Stored here
 * In case the Data Change, or a new Celestial Body is Added/Removed, the Data only changes in this Class
 * Will Store Instances of the Celestial Body Class
 */
public class PlanetaryData {

    // Sun
    private static Vector sunInitialPosition = new Vector(new double[]{0, 0, 0});
    private static Vector sunInitialVelocity = new Vector(new double[]{0, 0, 0});
    private static StateVector sunInitialStateVector = new StateVector(new Vector[]{sunInitialPosition, sunInitialVelocity});
    private static double sunMass = 1.9885*Math.pow(10, 30);
    private static CelestialBody sun = new CelestialBody("Sun", sunInitialStateVector, sunMass);

    // Mercury
    private static Vector mercuryInitialPosition = new Vector(new double[]{7833268.43923962, 44885949.3703908, 2867693.20054382});
    private static Vector mercuryInitialVelocity = new Vector(new double[]{-57.4967480139828, 11.52095127176, 6.21695374334136});
    private static StateVector mercuryInitialStateVector = new StateVector(new Vector[]{mercuryInitialPosition, mercuryInitialVelocity});
    private static double mercuryMass = 3.302*Math.pow(10, 23);
    private static CelestialBody mercury = new CelestialBody("Mercury", mercuryInitialStateVector, mercuryMass);

    // Venus
    private static Vector venusInitialPosition = new Vector(new double[]{-28216773.9426889, 103994008.541512, 3012326.64296788});
    private static Vector venusInitialVelocity = new Vector(new double[]{-34.0236737066136, -8.96521274688838, 1.84061735279188});
    private static StateVector venusInitialStateVector = new StateVector(new Vector[]{venusInitialPosition, venusInitialVelocity});
    private static double venusMass = 48.685 * Math.pow(10, 23);
    private static CelestialBody venus = new CelestialBody("Venus", venusInitialStateVector, venusMass);

    // Earth
    private static Vector earthInitialPosition = new Vector(new double[]{148186906.893642, -27823158.5715694, 33746.8987977113});
    private static Vector earthInitialVelocity = new Vector(new double[]{5.05251577575409, -29.3926687625899, 0.00170974277401292});
    private static StateVector earthInitialStateVector = new StateVector(new Vector[]{earthInitialPosition, earthInitialVelocity});
    private static double earthMass = 5.97219 * Math.pow(10, 24);
    private static CelestialBody earth = new CelestialBody("Earth", earthInitialStateVector, earthMass);

    // Moon
    private static Vector moonInitialPosition = new Vector(new double[]{-148458048.395164, -27524868.1841142, 70233.6499287411});
    private static Vector moonInitialVelocity = new Vector(new double[]{4.34032634654904, -30.0480834180741, -0.0116103535014229});
    private static StateVector moonInitialStateVector = new StateVector(new Vector[]{moonInitialPosition, moonInitialVelocity});
    private static double moonMass = 7.349 * Math.pow(10, 22);
    private static CelestialBody moon = new CelestialBody("Moon", moonInitialStateVector, moonMass);

    // Mars
    private static Vector marsInitialPosition = new Vector(new double[]{-159116303.422552, 189235671.561057, 7870476.08522969});
    private static Vector marsInitialVelocity = new Vector(new double[]{-17.6954469224752, -13.4635253412947, 0.152331928200531});
    private static StateVector marsInitialStateVector = new StateVector(new Vector[]{marsInitialPosition, marsInitialVelocity});
    private static double marsMass = 6.4171 * Math.pow(10, 23);
    private static CelestialBody mars = new CelestialBody("Mars", marsInitialStateVector, marsMass);

    // Jupiter
    private static Vector jupiterInitialPosition = new Vector(new double[]{692722875.928222, 258560760.813524, -16570817.7105996});
    private static Vector jupiterInitialVelocity = new Vector(new double[]{-4.71443059866156, 12.8555096964427, 0.0522118126939208});
    private static StateVector jupiterInitialStateVector = new StateVector(new Vector[]{jupiterInitialPosition, jupiterInitialVelocity});
    private static double jupiterMass = 189818722 * Math.pow(10, 19);
    private static CelestialBody jupiter = new CelestialBody("Jupiter", jupiterInitialStateVector, jupiterMass);

    // Saturn
    private static Vector saturnInitialPosition = new Vector(new double[]{1253801723.95465, -760453007.810989, -36697431.1565206});
    private static Vector saturnInitialVelocity = new Vector(new double[]{4.46781341335014, 8.23989540475628, -0.320745376969732});
    private static StateVector saturnInitialStateVector = new StateVector(new Vector[]{saturnInitialPosition, saturnInitialVelocity});
    private static double saturnMass = 5.6834 * Math.pow(10, 26);
    private static CelestialBody saturn = new CelestialBody("Saturn", saturnInitialStateVector, saturnMass);

    // Titan
    private static Vector titanInitialPosition = new Vector(new double[]{1254501624.95946, -761340299.067828, -36309613.8378104});
    private static Vector titanInitialVelocity = new Vector(new double[]{8.99593229549645, 11.1085713608453, -2.25130986174761});
    private static StateVector titanInitialStateVector = new StateVector(new Vector[]{titanInitialPosition, titanInitialVelocity});
    private static double titanMass = 13455.3 * Math.pow(10, 19);
    private static CelestialBody titan = new CelestialBody("Titan", titanInitialStateVector, titanMass);

    // Neptune
    private static Vector neptuneInitialPosition = new Vector(new double[]{4454487339.09447, -397895128.763904, -94464151.3421107});
    private static Vector neptuneInitialVelocity = new Vector(new double[]{0.447991656952326, 5.44610697514907, -0.122638125365954});
    private static StateVector neptuneInitialStateVector = new StateVector(new Vector[]{neptuneInitialPosition, neptuneInitialVelocity});
    private static double neptuneMass = 102.409 * Math.pow(10, 24);
    private static CelestialBody neptune = new CelestialBody("Neptune", neptuneInitialStateVector, neptuneMass);

    // Uranus
    private static Vector uranusInitialPosition = new Vector(new double[]{1958732435.99338, 2191808553.21893, -17235283.8321992});
    private static Vector uranusInitialVelocity = new Vector(new double[]{-5.12766216337626, 4.22055347264457, 0.0821190336403063});
    private static StateVector uranusInitialStateVector = new StateVector(new Vector[]{uranusInitialPosition, uranusInitialVelocity});
    private static double uranusMass = 86.813 * Math.pow(10, 24);
    private static CelestialBody uranus = new CelestialBody("Uranus", uranusInitialStateVector, uranusMass);

    // List of Celestial Bodies - It will be Passed to the SolarSystem class to start the simulation
    private static CelestialBody[] celestialBodies = new CelestialBody[]{sun, mercury, venus, earth, moon, mars, jupiter, saturn, titan, neptune, uranus};

    // Getters
    public static CelestialBody[] getCelestialBodies() {
        return celestialBodies;
    }

    public static StateVector[] getCelestialBodiesStateVector() {
        StateVector[] stateVectors = new StateVector[celestialBodies.length];

        for (int i = 0; i < celestialBodies.length; i++) {
            stateVectors[i] = celestialBodies[i].getInitialState();
        }
        return stateVectors;
    }

    public static double[] getCelestialBodiesMasses() {
        double[] masses = new double[celestialBodies.length];

        for (int i = 0; i < celestialBodies.length; i++) {
            masses[i] = celestialBodies[i].getMass();
        }
        return masses;
    }

    public static String[] getCelestialBodyNames(){
        String[] names = new String[celestialBodies.length];

        for(int i = 0; i < celestialBodies.length; i++){
            names[i] = celestialBodies[i].getName();
        }
        return names;
    }

}
