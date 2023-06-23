package stochastic_wind_simulation;

import physics.vectors.Vector;

/**
 * Simulation for the Wind in Titan
 * The Information regarding the Atmosphere of Titan was found in the following links:
 * https://solarsystem.nasa.gov/moons/saturn-moons/titan/in-depth/#otp_atmosphere
 * https://www.esa.int/Science_Exploration/Space_Science/Cassini-Huygens/First_measurement_of_Titan_s_winds_from_Huygens#:~:text=Winds%20on%20Titan%20are%20found,altitude%20of%20about%20120%20km.
 * https://www.nasa.gov/mission_pages/cassini/whycassini/cassinif-20070601-05.html
 *
 * Since we are given a Scenario where only the (x, y) Coordinates matter, the x coodinate will be the one where the wind will be acting
 * This is due to the fact that the Articles state that the Wind was basically just in relation from West to East
 */
public class WindModel {

    final int MAX_ALTITUDE = 120000; // Wind goes up to 120 Km from the Surface
    final double MAX_WIND_SPEED_120_KM = 120; // m/s
    final double MAX_WIND_SPEED_SEVEN_KM = 0.3; // m/s
    double maxWindForTesting; // Will be used for Testing Purposes

    /**
     * Constructor for the Real Simulation
     */
    public WindModel() {
        this.maxWindForTesting = MAX_WIND_SPEED_120_KM;
    }

    public Vector getWindSpeed(double heigth) { // Height Must be in Meters

        // Bellow 7km of Altitude, the Wind of Titan has a Max Speed of 0.3 m/s
        if (heigth <= 7000) {

        }
        // From 7km up until 60 Km, the Wind Increases a bit with height (though no Empirical Data was found stating by how much)
        else if (heigth <= 60000) {

        }
        // Here, Scientists do not have a lot of empirical data, however, with the Landing of the Huygens, it was found that strong winds can come randomly, though not frequent
        else if (heigth <= 120000) {

        }
        // Above 120 Km, the Atmosphere of Titan is very Rare (also due to its gravity)
        else {
            
        }





        return null;

    }
}
