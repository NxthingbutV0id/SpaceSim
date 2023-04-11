/* Class: Vec2
 * Author: Christian Torres
 * Created: 2023/3/13
 * Modified:
 *
 * Purpose: My own version of a vector with the necessary methods
 *
 * Attributes: -x: double
 *             -y: double
 *
 * Methods: +Vec2(double, double): this
 *          +Vec2(double, Vec2): this
 *          +getX(): double
 *          +getY(): double
 *          +getMagnitude(): double
 *          +getAngleRadians(): double
 *          +getAngleDegrees(): double
 *          +getComponents(): double[]
 *          +setXY(double, double): void
 *          +setXY(double[]): void
 *          +add(Vec2): Vec2
 *          +sub(Vec2): Vec2
 *          +multiply(double): Vec2
 *          +divide(double): Vec2
 *          +getDistance(Vec2): double
 *          +dotProduct(Vec2): double
 *          +radiansBetween(Vec2): double
 *          +normalize(): Vec2
 *          +toString(): String
 */
package main.customUtils;

public class Vec2 {
    private double x, y;

    public Vec2(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public Vec2(double magnitude, Vec2 direction) {
        x = magnitude * Math.sin(direction.getAngleRadians());
        y = magnitude * Math.cos(direction.getAngleRadians());
    }

    public Vec2() {
        x = 0;
        y = 0;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public void setX(double x) {
        this.x = x;
    }

    public void setY(double y) {
        this.y = y;
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

    public void set(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public void set(double[] xy){
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

    public void selfMultiply(double scalar) {x *= scalar; y *= scalar;}

    public Vec2 divide(double scalar) {
        return new Vec2(x / scalar, y / scalar);
    }

    public double distance(Vec2 vec) {
        return Math.sqrt((Math.abs(vec.x - x) * Math.abs(vec.x - x)) + (Math.abs(vec.y - y) * Math.abs(vec.y - y)));
    }

    public double dotProduct(Vec2 vec) {
        return (x * vec.getX()) + (y * vec.getY());
    }

    public double radiansBetween(Vec2 vec) {
        return Math.acos(dotProduct(vec)/(getMagnitude() * vec.getMagnitude()));
    }

    public Vec2 normalize() {
        if (getMagnitude() != 0) {
            return new Vec2(x / getMagnitude(), y / getMagnitude());
        } else {
            return new Vec2();
        }
    }

    public void selfNormalize() {
        double currentMag = getMagnitude();
        if (currentMag != 0) {
            x /= currentMag;
            y /= currentMag;
        }
    }

    public Vec2 copy() {
        return new Vec2(x, y);
    }

    public void set(Vec2 other) {
        x = other.x;
        y = other.y;
    }

    @Override
    public String toString() {
        return "(" + x + ", " + y + ")";
    }
}
