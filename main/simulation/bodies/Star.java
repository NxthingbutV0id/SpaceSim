package main.simulation.bodies;

import main.customUtils.*;

public class Star extends CelestialBody {
    private double luminosity;

    public Star(String name, double mass, double radius, Vec2 position, Vec2 velocity, double surfaceTemp) {
        super(name, mass, radius, position, velocity);
        this.surfaceTemp = surfaceTemp;
    }

    private void setTempAndLuminosity() {
        surfaceTemp = Math.pow(Math.pow(mass/Constants.SOL_MASS.getValue(), 2.5),
                0.25) * Constants.SOL_TEMPERATURE.getValue();
        luminosity = (radius * radius) * (surfaceTemp * surfaceTemp * surfaceTemp * surfaceTemp); // L = R^2 * T^4
    }

    public double getBrightness(double distance) {
        return luminosity / (4 * Math.PI * distance * distance);
    }
}