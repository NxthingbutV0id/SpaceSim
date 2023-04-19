package main.simulation.bodies;

import javafx.scene.canvas.GraphicsContext;
import main.utils.Constants;
import main.utils.Vec2;

import java.util.LinkedList;

import static java.lang.Math.*;

public class OrbitGraphics {
    private CelestialBody body;
    private LinkedList<Vec2> path = new LinkedList<>();

    public OrbitGraphics(CelestialBody body) {
        this.body = body;
        path.add(new Vec2(body.position.getX(), body.position.getY()));
    }

    public void addToPath() {
        Vec2 currentPos = new Vec2(body.position.getX(), body.position.getY());
        path.add(currentPos);
        if (path.size() > 1000) {
            path.remove(0);
        }
    }

    public void drawOrbit(GraphicsContext gc, double scale, double screenWidth, double screenHeight, Vec2 camera) {
        if (body.parent != null) {
            drawBodyConic(gc, scale, screenWidth, screenHeight, camera);
        } else {
            drawBodyPath(gc, scale, screenWidth, screenHeight, camera);
        }
    }

    private void drawBodyConic(GraphicsContext gc, double scale, double screenWidth, double screenHeight, Vec2 camera) {
        int res = 1000;
        double mu = Constants.GRAVITATIONAL_CONSTANT * body.parent.mass;
        Vec2 parentScreenPos = body.parent.getScreenPosition(scale, screenWidth, screenHeight, camera);
        Vec2 relativePos = body.position.sub(body.parent.position);
        Vec2 relativeVel = body.velocity.sub(body.parent.velocity);
        double r = body.position.distance(body.parent.position);
        double v = relativeVel.getMagnitude();
        double a = (mu * r)/(2 * mu - r * v * v);

        double vTheta = relativePos.crossProduct(relativeVel)/r;
        double e = sqrt(1 + ((r * vTheta * vTheta)/mu) * ((r * v * v)/mu - 2));

        double vRadial = relativePos.dotProduct(relativeVel)/r;

        double deltaTheta = sign(vTheta*vRadial) * acos((a * (1 - e * e) - r)/(e * r)) -
                atan2(relativePos.getY(), relativePos.getX());

        double rMax;
        if (body.parent.parent != null) {
            rMax = a* pow(body.parent.mass/body.parent.parent.mass, 2.5);
        } else {
            rMax = 1.36e7*body.parent.radius;
        }
        gc.setStroke(body.planetColor);
        gc.setLineWidth(4);
        gc.beginPath();
        double thetaMax = acos((a * (1 - e * e) - rMax)/(e * rMax));
        if (e >= 1) {
            for (double theta = -thetaMax; theta <= thetaMax; theta += (2 * PI) / res) {
                drawConic(gc, scale, parentScreenPos, a, e, deltaTheta, theta);
            }
        } else {
            for (double theta = 0; theta <= 2 * PI; theta += (2 * PI) / res) {
                drawConic(gc, scale, parentScreenPos, a, e, deltaTheta, theta);
            }
        }
        gc.stroke();
    }

    private void drawConic(GraphicsContext gc, double scale, Vec2 parentScreenPos,
            double a, double e, double deltaTheta, double theta) {
        double pathX = r(theta, a, e) * cos(theta + deltaTheta);
        double pathY = -r(theta, a, e) * sin(theta + deltaTheta);
        if (theta == 0) {
            gc.moveTo(pathX * scale + parentScreenPos.getX(), pathY * scale + parentScreenPos.getY());
        } else {
            gc.lineTo(pathX * scale + parentScreenPos.getX(), pathY * scale + parentScreenPos.getY());
        }
    }

    private double r(double theta, double a, double e) {
        return (a * (1 - e * e))/(1 + e * cos(theta));
    }

    private double sign(double x) {
        return x/abs(x);
    }

    private void drawBodyPath(GraphicsContext gc, double scale, double screenWidth, double screenHeight, Vec2 relative)
    {
        double offsetX = screenWidth / 2;
        double offsetY = screenHeight / 2;

        gc.setStroke(body.planetColor);
        double thickness = 4;

        for (int i = path.size() - 1; i > 1; --i) {
            gc.setLineWidth(thickness);
            Vec2 pos1 = path.get(i - 1);
            Vec2 pos2 = path.get(i);

            if (shouldSkipDrawing(pos1, pos2, screenWidth, screenHeight)) {
                continue;
            }

            double[] points = computeLinePoints(pos1, pos2, offsetX, offsetY, scale, relative);
            gc.strokeLine(points[0], points[1], points[2], points[3]);
            thickness *= 0.99;
        }
    }

    private boolean shouldSkipDrawing(Vec2 pos1, Vec2 pos2, double screenWidth, double screenHeight) {
        boolean bothSamePoint = pos1.getX() == pos2.getX() && pos1.getY() == pos2.getY();
        boolean isOffScreenX = pos1.getX() > screenWidth && pos1.getX() < 0;
        boolean isOffScreenY = pos1.getY() > screenHeight && pos1.getY() < 0;
        boolean isOffScreen = isOffScreenX && isOffScreenY;

        return bothSamePoint || isOffScreen;
    }

    private double[] computeLinePoints(
            Vec2 pos1, Vec2 pos2, double offsetX, double offsetY, double scale, Vec2 relative) {
        double relX = relative.getX();
        double relY = relative.getY();

        double xPrev = ((pos1.getX() + offsetX / scale) - relX) * scale;
        double yPrev = ((pos1.getY() + offsetY / scale) - relY) * scale;
        double xCurr = ((pos2.getX() + offsetX / scale) - relX) * scale;
        double yCurr = ((pos2.getY() + offsetY / scale) - relY) * scale;

        return new double[]{xPrev, yPrev, xCurr, yCurr};
    }
}
