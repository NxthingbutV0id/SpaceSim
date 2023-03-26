package main.customUtils;

public class Vec2 {
    private double x, y;

    public Vec2(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public double getMagnitude() {
        return Math.sqrt(x*x + y*y);
    }

    public double getAngleRadians() {
        return Math.acos(x/getMagnitude());
    }

    public double getAngleDegrees() {
        return getAngleRadians() * (180.0/Math.PI);
    }

    public double[] getComponents() {
        return new double[]{x, y};
    }

    public void setXY(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public void setXY(double[] xy){
        x = xy[0];
        y = xy[1];
    }

    public Vec2 add(Vec2 vec) {
        return new Vec2(x + vec.getX(), y + vec.getY());
    }

    public void incrementBy(Vec2 vec) {
        this.x += vec.getX();
        this.y += vec.getY();
    }

    public Vec2 sub(Vec2 vec) {
        return new Vec2(x - vec.getX(), y - vec.getY());
    }

    public Vec2 multiply(double scalar) {
        return new Vec2(x * scalar, y * scalar);
    }

    public double getDistance(Vec2 vec) {
        return (((vec.getX() - x) * (vec.getX() - x)) + ((vec.getY() - y) * (vec.getY() - y)));
    }

    public Vec2 divide(double scalar) {
        return new Vec2(x / scalar, y / scalar);
    }

    public double dotProduct(Vec2 vec) {
        return (x * vec.getX()) + (y * vec.getY());
    }

    public double radiansBetween(Vec2 vec) {
        return Math.acos(dotProduct(vec)/(getMagnitude() * vec.getMagnitude()));
    }

    public Vec2 normalize() {
        return new Vec2(x/getMagnitude(), y/getMagnitude());
    }

    @Override
    public String toString() {
        return "(" + x + ", " + y + ")";
    }
}
