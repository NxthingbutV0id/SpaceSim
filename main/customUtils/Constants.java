package main.customUtils;

public enum Constants {
    GRAVITATIONAL_CONSTANT(6.67428e-11),
    STEFAN_BOLTZMANN(5.67037419e-8),
    SOL_RADIUS(6.957e8),
    JUPITER_RADIUS(6.995e7),
    EARTH_RADIUS(6.371009e6),
    MOON_RADIUS(1.7374e6),
    SOL_MASS(1.988435e30),
    JUPITER_MASS(1.898e27),
    EARTH_MASS(5.97e24),
    MOON_MASS(7.3459e22),
    ASTRONOMICAL_UNIT(1.496e11),
    KILOMETER(1000),
    SOL_TEMPERATURE(5700);

    private double value;

    Constants(double value) {
        this.value = value;
    }

    public double getValue() {
        return value;
    }
}
