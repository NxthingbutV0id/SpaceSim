package main.engine;

import java.awt.image.*;
import java.util.Arrays;

public class Renderer {
    private int pW, pH;
    private int[] p;

    public Renderer(Container c) {
        pW = c.getWidth();
        pH = c.getHeight();

        p = ((DataBufferInt)c.getWindow().getImage().getRaster().getDataBuffer()).getData();
    }

    public void clear() {
        Arrays.fill(p, 0);
    }

    public void setPixel(int x, int y, int value) {
        if ((x < 0 || x >= pW || y < 0 || y >= pH) || value == 0xffff00ff) {
            return;
        }

        p[x + y * pW] = value;
    }

    public void drawRect(int x, int y, int width, int height, int color) {
        for (int i = 0; i < width; ++i) {
            for (int j = 0; j < height; ++j) {
                setPixel(x + i, y + j, color);
            }
        }
    }

    public 
}
