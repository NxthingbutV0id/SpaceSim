/* Class: Constants
 * Author: Christian Torres
 * Created: 2023/3/22
 * Modified:
 *
 * Purpose: To hold useful constants for the rest of the program
 *
 * Attributes: +_GRAVITATIONAL_CONSTANT: double
 *             +_Radius: Class
 *             +_Mass: Class
 *             +_Distance: Class
 *
 * Methods:
 */
package main.constants;

public class Constants {
    public static final double GRAVITATIONAL_CONSTANT = 6.67430e-11;

    public static class Radius {
        public static final double SOL = 6.957e8;
        public static final double JUPITER = 6.995e7;
        public static final double EARTH = 6.371009e6;
        public static final double MOON = 1.7374e6;
    }

    public static class Mass {
        public static final double SOL = 1.99847e30;
        public static final double JUPITER = 1.89813e27;
        public static final double EARTH = 5.9722e24;
        public static final double MOON = 7.3459e22;
    }

    public static class Distance {
        public static final double AU = 1.495978707e11;
    }
}
