package main.engine;

import java.awt.event.KeyEvent;

public class Container implements Runnable {
    private Window window;
    private Renderer renderer;
    private Input input;
    private final int width, height;
    private final float scale;
    private final String title;
    private AbstractScene scene;

    public Container(int width, int height, float scale, String title, AbstractScene scene) {
        this.width = width;
        this.height = height;
        this.scale = scale;
        this.title = title;
        this.scene = scene;
    }

    public void start() {
        window = new Window(this);
        renderer = new Renderer(this);
        Thread thread = new Thread(this);
        input = new Input(this);
        thread.run();
    }

    public void stop() {

    }

    public void run() {
        boolean render, running = true;
        double firstTime, lastTime, passedTime, unprocessedTime = 0;
        lastTime = System.nanoTime() / 1e9;

        double frameTime = 0;
        int fps, frames = 0;

        while (running) {
            render = false;

            firstTime = System.nanoTime() / 1e9;
            passedTime = firstTime - lastTime;
            lastTime = firstTime;

            unprocessedTime += passedTime;
            frameTime += unprocessedTime;

            double UPDATE_TIME = 1.0 / 60.0;
            while (unprocessedTime >= UPDATE_TIME) {
                unprocessedTime -= UPDATE_TIME;
                render = true;
                //TODO: update here
                scene.update(this, UPDATE_TIME);
                input.update();
                if (frameTime >= 1) {
                    frameTime = 0;
                    fps = frames;
                    frames = 0;
                    System.out.println("FPS: " + fps);
                }
            }

            if (render) {
                //TODO: render here
                scene.render(this, renderer);
                renderer.clear();
                window.update();
                frames++;
            } else {
                try {
                    Thread.sleep(1);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

            if (input.isKey(KeyEvent.VK_ESCAPE)) {
                running = false;
            }
        }

        dispose();
    }

    private void dispose() {

    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public float getScale() {
        return scale;
    }

    public String getTitle() {
        return title;
    }

    public Window getWindow() {
        return window;
    }

    public Input getInput() {
        return input;
    }
}
