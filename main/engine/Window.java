package main.engine;

import javax.swing.*;
import java.awt.*;
import java.awt.image.*;

public class Window {
    private final BufferedImage image;
    private final Canvas canvas;
    private final BufferStrategy bufferStrategy;
    private final Graphics graphics;

    public Window(Container container) {
        image = new BufferedImage(container.getWidth(), container.getHeight(), BufferedImage.TYPE_INT_RGB);
        canvas = new Canvas();
        Dimension screenSize = new Dimension(
                (int)(container.getWidth() * container.getScale()),
                (int)(container.getHeight() * container.getScale())
        );

        canvas.setPreferredSize(screenSize);
        canvas.setMaximumSize(screenSize);
        canvas.setMinimumSize(screenSize);

        JFrame frame = new JFrame(container.getTitle());
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout());
        frame.add(canvas, BorderLayout.CENTER);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setResizable(false);
        frame.setVisible(true);

        canvas.createBufferStrategy(2);
        bufferStrategy = canvas.getBufferStrategy();
        graphics = bufferStrategy.getDrawGraphics();
    }

    public void update() {
        graphics.drawImage(image, 0, 0, canvas.getWidth(), canvas.getHeight(), null);
        bufferStrategy.show();
    }

    public BufferedImage getImage() {
        return image;
    }

    public Canvas getCanvas() {
        return canvas;
    }
}
