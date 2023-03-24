package main.engine;

import java.awt.event.*;

public class Input implements KeyListener, MouseListener, MouseMotionListener, MouseWheelListener {
    private final Container container;

    private final int NUM_KEYS = 256;
    private final boolean[] keys = new boolean[NUM_KEYS];
    private final boolean[] lastKeys = new boolean[NUM_KEYS];

    private final int NUM_BUTTONS = 5;
    private final boolean[] buttons = new boolean[NUM_BUTTONS];
    private final boolean[] lastButtons = new boolean[NUM_BUTTONS];

    private int mouseX, mouseY, scroll;

    public Input(Container container) {
        this.container = container;
        mouseX = 0;
        mouseY = 0;
        scroll = 0;

        container.getWindow().getCanvas().addKeyListener(this);
        container.getWindow().getCanvas().addMouseMotionListener(this);
        container.getWindow().getCanvas().addMouseListener(this);
        container.getWindow().getCanvas().addMouseWheelListener(this);
    }

    public void update() {
        System.arraycopy(keys, 0, lastKeys, 0, NUM_KEYS);
        System.arraycopy(buttons, 0, lastButtons, 0, NUM_BUTTONS);
    }

    public boolean isKey(int keyCode) {return keys[keyCode];}
    public boolean isKeyUp(int keyCode) {return !keys[keyCode] && lastKeys[keyCode];}
    public boolean isKeyDown(int keyCode) {return keys[keyCode] && !lastKeys[keyCode];}
    public boolean isButton(int button) {return buttons[button];}
    public boolean isButtonUp(int button) {return !buttons[button] && lastButtons[button];}
    public boolean isButtonDown(int button) {return buttons[button] && !lastButtons[button];}

    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {
        keys[e.getKeyCode()] = true;
    }

    @Override
    public void keyReleased(KeyEvent e) {
        keys[e.getKeyCode()] = false;
    }

    @Override
    public void mouseClicked(MouseEvent e) {

    }

    @Override
    public void mousePressed(MouseEvent e) {
        buttons[e.getButton()] = true;
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        buttons[e.getButton()] = false;
    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }

    @Override
    public void mouseDragged(MouseEvent e) {
        mouseX = (int)(e.getX() / container.getScale());
        mouseY = (int)(e.getY() / container.getScale());
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        mouseX = (int)(e.getX() / container.getScale());
        mouseY = (int)(e.getY() / container.getScale());
    }

    @Override
    public void mouseWheelMoved(MouseWheelEvent e) {
        scroll = e.getWheelRotation();
    }

    public int getMouseX() {return mouseX;}
    public int getMouseY() {return mouseY;}
    public int getScroll() {return scroll;}
}
