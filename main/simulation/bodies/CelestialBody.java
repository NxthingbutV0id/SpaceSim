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

public class CelestialBody {
    protected String name;
    protected double mass;
    protected double radius;
    protected Vec2 position;
    protected Vec2 velocity;
    protected Paint planetColor;
    protected double surfaceTemp;
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
        Vec2 currentPos = position.copy();
        path.add(currentPos);
        if (path.size() > 10000) {
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

    public void drawBodyPath(GraphicsContext gc, double scale, double screenWidth, double screenHeight, Vec2 relative) {
        double xPrev, yPrev, xCurr, yCurr, relX, relY, offsetX, offsetY;
        boolean isOffScreen, isOffScreenX, isOffScreenY, bothSamePoint;
        relX = relative.getX();
        relY = relative.getY();

        gc.setStroke(planetColor);
        gc.setLineWidth(4);

        offsetX = screenWidth/2;
        offsetY = screenHeight/2;



        for (int i = 1; i < path.size(); ++i) {
            Vec2 pos1 = path.get(i - 1);
            Vec2 pos2 = path.get(i);
            bothSamePoint = pos1.getX() == pos2.getX() && pos1.getY() == pos2.getY();
            if (!bothSamePoint) {
                xPrev = ((pos1.getX() + offsetX / scale) - relX) * scale;
                yPrev = ((pos1.getY() + offsetY / scale) - relY) * scale;
                xCurr = ((pos2.getX() + offsetX / scale) - relX) * scale;
                yCurr = ((pos2.getY() + offsetY / scale) - relY) * scale;

                isOffScreenX = xPrev > screenWidth && xPrev < 0;
                isOffScreenY = yPrev > screenHeight && yPrev < 0;

                isOffScreen = isOffScreenX && isOffScreenY;

                if (!isOffScreen) {
                    gc.strokeLine(xPrev, yPrev, xCurr, yCurr);
                }
            }
        }
    }

    public void printStats() {
        logger.debug("Body stats debug\nName: {}\nMass: {}\nRadius: {}\nPosition: {}\nVelocity: {}\n",
                name, mass, radius, position, velocity
        );
    }
}
