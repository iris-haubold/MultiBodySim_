import java.awt.*;

// This class represents celestial bodies like stars, planets, asteroids, etc..
public class Body {

    private String name;
    private double mass;
    private double radius;
    private Vector3 position; // position of the center.
    private Vector3 currentMovement;
    private Vector3 currentForce = new Vector3(0, 0, 0);
    private Color color; // for drawing the body

    public Body(String name, double mass, double radius, //mass in kg, radius in m, mvmt in m/s
                Vector3 position, Vector3 currentMovement, Color color) {
        this.name = name;
        this.mass = mass;
        this.radius = radius;
        this.position = position;
        this.currentMovement = currentMovement;
        this.color = color;
    }

    public double getMass() {
        return this.mass;
    }

    public String getName() {
        return this.name;
    }

    public Vector3 getPosition() {
        return this.position;
    }

    // Returns the distance between this body and the specified 'body'.
    public double distanceTo(Body body) {
        return (this.position).distanceTo(body.position);
    }

    //returns the distance between this body and the specified vector
    public double distanceTo(Vector3 v) {
        return this.position.distanceTo(v);
    }

    public void setForce(Vector3 currentForce) {
        this.currentForce = currentForce;
    }

    // Moves this body to a new position, according to the current force exerted
    // on it, and updates the current movement accordingly.
    public void move() {
        move(currentForce);
    }

    // Moves this body to a new position, according to the specified force vector 'force' exerted
    // on it, and updates the current movement accordingly.
    // (Movement depends on the mass of this body, its current movement and the exerted force.)
    public void move(Vector3 force) {

        currentMovement = currentMovement.plus(force.times(1 / mass));
        position = position.plus(currentMovement);
    }

    // Returns a string with the information about this body including
    // name, mass, radius, position and current movement. Example:
    // "Earth, 5.972E24 kg, radius: 6371000.0 m, position: [1.48E11,0.0,0.0] m, movement: [0.0,29290.0,0.0] m/s."
    public String toString() {
        return this.name + ", " + this.mass + " kg, " + this.radius + " m, " +
                this.position.toString() + " m, " + this.currentMovement.toString() + " m/s.";
    }

    // Draws the body to the current StdDraw canvas as a dot using 'color' of this body.
    // The radius of the dot is in relation to the radius of the celestial body
    public void draw() {
        this.position.drawAsDot(this.radius, this.color);
    }

    public int octreeIndex(double length, Vector3 cubeCorner) {
        return this.position.octreeIndex(length, cubeCorner);
    }

    public boolean inside(int scale) {
        return !position.outside(scale);
    }

    public void printPositions(Body[] bodies) {
        for (int i = 0; i < bodies.length; i++) {
            System.out.println(bodies[i].getName() + "'s position: " + bodies[i].position.toString());
        }
        System.out.println();
    }


}
