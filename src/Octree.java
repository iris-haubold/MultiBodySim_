public class Octree {

    private OctreeNode root;
    private double length;
    private double T;

    public double getLength() {
        return length;
    }

    public double geT() {
        return T;
    }

    public Octree(double length, double T) {
        this.length = length;
        this.T = T;
    }

    public Octree() {

    }

    //Adds a body to the tree if it doesn't already exist.
    //Returns true if body was added, otherwise false.
    public boolean addBody(Body body) {
        boolean change = false;

        if (body == null) {
            return false;
        }

        if (root == null) {
            root = new OctreeNode(body);
            root.setMass(body.getMass());
            change = true;
        } else if (!root.contains(this, body)) {
            root.addBody(body, 1, this);
            change = true;
        }

        return change;
    }

    //returns depth of a body
    //root != null && tree contains body
    public int bodyDepth(Body body) {
        return root.nBodyDepth(this, body);
    }

    //returns length of the arg body's current cube (taken from root)
    public double cubeLength(Body body) {
        return root.nCubeLength(this, bodyDepth(body));
    }

    public void forceOn(Body body) {
        if (root != null) {
            Vector3 force = root.nodeForce(this, body, new Vector3(0, 0, 0), 0);
            body.setForce(force);
            //System.out.println("forceOn " + body.getName() + ": " + force.toString() + "\n");
        }
    }

    public void move(Body body) {
        forceOn(body);
        body.move();

    }

    public void clear() {
        this.root = null;
    }

    public int getEmptyNodes() {
        return (root == null)? 1 : root.nEmptyNodes();
    }


}

