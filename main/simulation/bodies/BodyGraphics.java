package main.simulation.bodies;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import main.utils.Vec2;

public class BodyGraphics {
    private double x, y, r, relX, relY;
    private CelestialBody body;

    public BodyGraphics(CelestialBody body) {
        this.body = body;
    }

    public void drawBody(GraphicsContext g, double scale, double screenWidth, double screenHeight, Vec2 relative) {
        x = body.position.getX() * scale;
        y = body.position.getY() * scale;
        r = body.radius*scale;
        relX = relative.getX() * scale;
        relY = relative.getY() * scale;

        g.setFill(body.planetColor);
        g.fillOval(
                ((x - r) + screenWidth/2) - relX,
                ((y - r) + screenHeight/2) - relY,
                2*r,
                2*r
        );
    }

    public void drawBodyText(GraphicsContext g, double scale, double screenWidth, double screenHeight, Vec2 relative) {
        x = body.position.getX() * scale;
        y = body.position.getY() * scale;
        relX = relative.getX() * scale;
        relY = relative.getY() * scale;

        g.setFont(new Font("Impact", 20));

        g.setFill(Color.BLACK);
        g.fillText(body.name, ((x) + screenWidth/2) - relX - 2, ((y) + screenHeight/2) - relY - 2);
        g.setFill(Color.WHITE);
        g.fillText(body.name, ((x) + screenWidth/2) - relX, ((y) + screenHeight/2) - relY);
    }

}
