package main.simulation.bodies;

import main.customUtils.*;

public class Terrestrial extends CelestialBody {
    private double albedo;
    private double emissivity;
    private boolean hasAtmosphere;
    private int atmosphereLayers;
    private double surfaceHeatCapacity;

    public Terrestrial(String name, double mass, double radius, Vec2 position, Vec2 velocity) {
        super(name, mass, radius, position, velocity);
    }

    public void setInitTemp(double temp) {
        surfaceTemp = temp;
    }

    public void setAlbedo(double albedo) {
        this.albedo = albedo;
    }

    public void setEmissivity(double emissivity) {
        this.emissivity = emissivity;
    }

    public void setTemp(Star star, double deltaT) {
        //TODO: cite sources
        double T4 = surfaceTemp * surfaceTemp * surfaceTemp * surfaceTemp;

        double dist = position.distance(star.position);
        double starFlux = star.getLuminosity()/(4 * Math.PI * (dist * dist));
        double powerRec = starFlux * Math.PI * (star.radius * star.radius);

        double powerStar = powerRec * (1 - albedo);
        double powerAtm = emissivityFunc()*Constants.STEFAN_BOLTZMANN*T4*4*Math.PI*(radius*radius);

        double powerRadiated = Constants.STEFAN_BOLTZMANN * T4 * 4 * Math.PI * (radius * radius);
        double powerAbsorbed = powerStar + powerAtm;

        surfaceTemp += ((powerRadiated - powerAbsorbed) * deltaT) / surfaceHeatCapacity;
    }

    private double emissivityFunc() {
        double top = (2*atmosphereLayers - 2) - (atmosphereLayers - 2) * emissivity;
        double bottom = (2*atmosphereLayers) - (atmosphereLayers - 1) * emissivity;
        return top/bottom;
    }
}
