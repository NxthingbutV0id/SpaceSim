/* Class: Vec2
 * Author: Christian Torres
 * Date: 2023/3/13
 *
 * Purpose: This class represents a two-dimensional vector and provides various operations and
 *          methods to work with these vectors, such as addition, subtraction,
 *          multiplication, dot product, cross product, and normalization.
 *
 * Attributes:
 * -x: double
 * -y: double
 *
 * Methods:
 * +Vec2(double, double): Constructor
 * +Vec2(double, Vec2): Constructor
 * +Vec2(): Constructor
 * +getX(): double
 * +getY(): double
 * +setX(double): void
 * +setY(double): void
 * +getMagnitude(): double
 * +getAngleRadians(): double
 * +set(double, double): void
 * +add(Vec2): Vec2
 * +incrementBy(Vec2): void
 * +sub(Vec2): Vec2
 * +multiply(double): Vec2
 * +selfMultiply(double): void
 * +distance(Vec2): double
 * +dotProduct(Vec2): double
 * +crossProduct(Vec2): double
 * +selfNormalize(): void
 * +copy(): Vec2
 * +set(Vec2): void
 * +toString(): String
 */
package main.utils;

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

    public void set(double x, double y) {
        this.x = x;
        this.y = y;
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

    public double distance(Vec2 vec) {
        return Math.sqrt((Math.abs(vec.x - x) * Math.abs(vec.x - x)) + (Math.abs(vec.y - y) * Math.abs(vec.y - y)));
    }
    //Pro-tip: taking the dot product of yourself is the same as doing (x^2 + y^2)
    public double dotProduct(Vec2 vec) {
        return (x * vec.getX()) + (y * vec.getY());
    }

    public double crossProduct(Vec2 vec) {
        return ((x * vec.y) - (y * vec.x));
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
