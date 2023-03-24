package main.engine;

public interface AbstractScene {
    void update(Container container, double deltaT);
    void render(Container container, Renderer renderer);
}
