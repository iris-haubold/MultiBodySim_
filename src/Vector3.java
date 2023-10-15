import java.awt.*;

// This class represents vectors in a 3D vector space.
public class Vector3 {

    private double x;
    private double y;
    private double z;

    public Vector3(double x, double y, double z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public Vector3() {
        this.x = 0;
        this.y = 0;
        this.z = 0;
    }


    // Returns the sum of this vector and vector 'v'.
    public Vector3 plus(Vector3 v) {
        return new Vector3(this.x + v.x, this.y + v.y, this.z + v.z);
    }

    public void voidPlus(Vector3 v) {
        this.x += v.x;
        this.y += v.y;
        this.z += v.z;
    }

    // Returns the product of this vector and 'd' as a new vector.
    public Vector3 times(double d) {
        double newX = this.x * d;
        double newY = this.y * d;
        double newZ = this.z * d;

        return new Vector3(newX, newY, newZ);
    }

    // Multiplies this vector with scalar d
    public void voidTimes(double d) {
        this.x *= d;
        this.y *= d;
        this.z *= d;
    }

    // Returns the sum of this vector and -1*v.
    public Vector3 minus(Vector3 v) {
        return new Vector3(this.x - v.x, this.y - v.y, this.z - v.z);
    }

    // Returns the Euclidean distance of this vector
    // to the specified vector 'v'.
    public double distanceTo(Vector3 v) {
        return (this.minus(v)).length(); // ||this - v||
    }

    // Returns the scalar product of this vector and v
    public double scalarProduct(Vector3 v) {
        return this.x * v.x + this.y * v.y + this.z * v.z; // ⟨this|v⟩
    }

    // Returns the length (norm) of this vector.
    public double length() {
        return Math.sqrt(this.scalarProduct(this)); // ✓⟨this|this⟩ = ||this||
    }

    // Normalizes this vector: changes the length of this vector such that it becomes 1.
    // The direction and orientation of the vector is not affected.
    public void normalize() {
        this.voidTimes(1.0 / this.length());
    }

    // Draws a filled circle with a specified radius centered at the (x,y) coordinates of this vector
    // in the existing StdDraw canvas. The z-coordinate is not used.
    public void drawAsDot(double radius, Color color) {
        StdDraw.setPenColor(color);
        StdDraw.filledCircle(this.x, this.y, radius);
    }

    // Returns the coordinates of this vector in brackets as a string
    // in the form "[x,y,z]", e.g., "[1.48E11,0.0,0.0]".
    public String toString() {
        return "[" + this.x + "," + this.y + "," + this.z + "]";
    }

    //this = position, edge cases go towards the outside
    public int octreeIndex(double length, Vector3 cubeCorner) {
        double a = length / 2;
        boolean lowerX = (this.x - cubeCorner.x) < a;
        boolean lowerY = (this.y - cubeCorner.y) < a;
        boolean lowerZ = (this.z - cubeCorner.z) < a;

        return lowerX ?
                (lowerY ?
                        (lowerZ ? 2 : 0)
                        : (lowerZ ? 6 : 4))
                : (lowerY ?
                (lowerZ ? 3 : 1)
                : lowerZ ? 7 : 5);
    }

    public boolean outside(int scale) {
        boolean xOut = x > scale || x < 0;
        boolean yOut = y > scale || y < 0;
        boolean zOut = z > scale || z < 0;

        return xOut || yOut || zOut;
    }

    public Vector3 nextCentre(Octree oct, int depth, int index) {
        double x = 0, y = 0, z = 0;
        if (depth != 0) {
            double a = oct.getLength() / Math.pow(2, depth);
            boolean upperX, upperY, upperZ;
            upperX = index % 2 != 0;
            upperY = index >= 4;
            upperZ = index % 4 <= 1;

            x += upperX ? a / 2 : -a / 2;
            y += upperY ? a / 2 : -a / 2;
            z += upperZ ? a / 2 : -a / 2;
        }

        return this.plus(new Vector3(x, y, z));
    }


}

