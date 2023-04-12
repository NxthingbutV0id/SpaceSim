/* Class: CelestialBody
 * Author: Christian Torres
 * Created: 2023/3/13
 * Modified:
 *
 * Purpose: The bodies that generate gravity
 *
 * Attributes: *TO BE FINALIZED*
 *
 * Methods: *TO BE FINALIZED*
 */
package main.simulation.bodies;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.*;
import main.customUtils.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.LinkedList;

public abstract class CelestialBody {
    protected String name;
    protected double mass;
    protected double radius;
    protected Vec2 position;
    protected Vec2 velocity;
    protected Paint planetColor;
    protected double surfaceTemp;
    protected double parentMass;
    private LinkedList<Vec2> path = new LinkedList<>();
    private Logger logger = LoggerFactory.getLogger(CelestialBody.class);

    public CelestialBody(String name, double mass, double radius, Vec2 position, Vec2 velocity) {
        this.name = name;
        this.mass = mass;
        this.radius = radius;
        this.position = position;
        this.velocity = velocity;
        path.add(new Vec2(position.getX(), position.getY()));
    }

    public double getMass() {return mass;}
    public Vec2 getPosition() {return position;}
    public Vec2 getVelocity() {return velocity;}
    public String getName() {return name;}
    public double getRadius() {return radius;}
    public double getTemperature() {return surfaceTemp;}
    public void setPlanetColor(Paint planetColor) {this.planetColor = planetColor;}
    public void setPosition(Vec2 position) {this.position = position;}
    public void setName(String name) {this.name = name;}

    public void addToPath() {
        Vec2 currentPos = new Vec2(position.getX(), position.getY());
        path.add(currentPos);
        if (path.size() > 1000) {
            path.remove(0);
        }
    }

    public Vec2 getScreenPosition(double scale, double screenWidth, double screenHeight, Vec2 relative) {
        double x, y, r, relX, relY;

        x = position.getX() * scale;
        y = position.getY() * scale;
        r = radius*scale;
        relX = relative.getX() * scale;
        relY = relative.getY() * scale;

        return new Vec2((((x - r) + screenWidth/2) - relX), (((y - r) + screenHeight/2) - relY));
    }

    public void drawBody(GraphicsContext g, double scale, double screenWidth, double screenHeight, Vec2 relative) {
        double x, y, r, relX, relY;

        x = position.getX() * scale;
        y = position.getY() * scale;
        r = radius*scale;
        relX = relative.getX() * scale;
        relY = relative.getY() * scale;

        g.setFill(planetColor);
        g.fillOval(
                ((x - r) + screenWidth/2) - relX,
                ((y - r) + screenHeight/2) - relY,
                2*r,
                2*r
        );
    }

    public void drawBodyText(GraphicsContext g, double scale, double screenWidth, double screenHeight, Vec2 relative) {
        double x, y, r, relX, relY;

        x = position.getX() * scale;
        y = position.getY() * scale;
        r = radius*scale;
        relX = relative.getX() * scale;
        relY = relative.getY() * scale;

        g.setFill(Color.WHITE);
        g.fillText(name, ((x - r) + screenWidth/2) - relX, ((y - r) + screenHeight/2) - relY);
    }

    public void drawBodyPath(
            GraphicsContext gc, double scale, double screenWidth, double screenHeight, Vec2 relative, Vec2 target) {
        double offsetX = screenWidth / 2;
        double offsetY = screenHeight / 2;

        gc.setStroke(planetColor);
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

    public void printStats() {
        logger.debug("Body stats debug\nName: {}\nMass: {}\nRadius: {}\nPosition: {}\nVelocity: {}\n",
                name, mass, radius, position, velocity
        );
    }

    public abstract void setTemp(Star star);
}
