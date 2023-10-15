public class OctreeNode {

    private OctreeNode[] childNodes;
    private Body body;
    private int depth;
    private double mass; //mass of all the bodies currently in this node


    OctreeNode(Body body) {
        this.childNodes = new OctreeNode[8];
        this.body = body;
        this.depth = 0;
    }

    void setMass(double mass) {
        this.mass += mass;
    }

    //presumes body doesn't already exist in tree
    //if function starts with this node containing a body -> both this.body and newBody need to be moved
    //newBody != null
    void addBody(Body newBody, int addDepth, Octree oct) {
        if (this.body != null) {
            Body placeholder = this.body;
            this.mass -= this.body.getMass();
            this.body = null;
            this.addBody(placeholder, addDepth, oct);
        }

        if (this.childNodes[newIndex(oct, newBody)] == null) {
            this.mass += newBody.getMass();

            this.childNodes[newIndex(oct, newBody)] = new OctreeNode(newBody);
            this.childNodes[newIndex(oct, newBody)].mass += newBody.getMass();
            this.childNodes[newIndex(oct, newBody)].depth += addDepth;

        } else {
            this.mass += newBody.getMass();
            this.childNodes[newIndex(oct, newBody)].addBody(newBody, ++addDepth, oct);
        }
    }

    boolean contains(Octree oct, Body body) {
        boolean here = this.body != null && this.body.getName().equals(body.getName());

        if (here) {
            return true;
        } else if (this.childNodes[newIndex(oct, body)] == null) {
            return false;
        }
        return this.childNodes[newIndex(oct, body)].contains(oct, body);

    }

    //returns index of the new cube which the arg body gets sorted into.
    int newIndex(Octree oct, Body body) {
        Vector3 currentCorner = new Vector3(0, 0, 0);
        int depth = this.depth;
        int index = -1;

        for (int i = 0; i <= depth; i++) {
            index = body.octreeIndex(nCubeLength(oct, i), currentCorner);
            currentCorner = cubeCorner(oct, currentCorner, i + 1, index);
        }
        return index;
    }
    /*                 ______________
     *                /4     /5     /|
     *               /------/------/ |
     *              /______/______/ ||
     *              |0     |1     | /|
     *         _->  |      |      |/|| <-7
     *        6     |------|------| |/
     *              |2     |3     | /
     *              |______|______|/
     */

    //does not work with depth == 0
    Vector3 cubeCorner(Octree oct, Vector3 oldCorner, int depth, int index) {
        boolean upperX, upperY, upperZ;
        double addX, addY, addZ;
        double a = nCubeLength(oct, depth);

        upperX = index % 2 != 0;
        upperY = index >= 4;
        upperZ = index % 4 <= 1;

        addX = upperX ? a : 0;
        addY = upperY ? a : 0;
        addZ = upperZ ? a : 0;


        return oldCorner.plus(new Vector3(addX, addY, addZ));
    }

    //returns length of the depth's cube
    double nCubeLength(Octree oct, int depth) {
        return oct.getLength() / Math.pow(2, depth);
    }

    //returns a body's depth from this down (root), assumes tree contains body
    int nBodyDepth(Octree oct, Body body) {
        if (this.body != null) {
            boolean here = this.body.getName().equals(body.getName());
            return here ? 0 : 1 + this.childNodes[newIndex(oct, body)].nBodyDepth(oct, body);
        }


        return 1 + this.childNodes[newIndex(oct, body)].nBodyDepth(oct, body);
    }

    Vector3 cubeCentre(Octree oct, Vector3 corner, int depth) {
        double a = nCubeLength(oct, depth);
        return corner.plus(new Vector3(a / 2, a / 2, a / 2));
    }

    Vector3 nodeForce(Octree oct, Body body, Vector3 oldCorner, int depth) {
        Vector3 c, cC = cubeCentre(oct, oldCorner, depth);
        Vector3 force = new Vector3(0, 0, 0);
        double d, r;

        for (int i = 0; i < 8; i++) {
            c = cC.nextCentre(oct, depth, i);
            r = body.distanceTo(c);
            d = nCubeLength(oct, depth);

            if (d / r < oct.geT() && this.childNodes[i] != null) {
                force.voidPlus(singleNodeForce(body, c, this.childNodes[i].mass));
            } else {
                if (this.childNodes[i] != null) {
                    depth++;
                    force.voidPlus(this.childNodes[i].nodeForce(oct, body, cubeCorner(oct, oldCorner, depth, i), depth));
                }
            }
        }
        return force;
    }

    Vector3 singleNodeForce(Body body, Vector3 cubeCentre, double mass) {
        Vector3 direction = cubeCentre.minus(body.getPosition());
        double r = direction.length();
        direction.normalize();
        double force = Simulation.G * body.getMass() * mass / (r * r);
        Vector3 vForce = direction.times(force);
        //System.out.println("sNF on " + body.getName() + ": " + vForce.toString());
        return vForce;

    }

    int nullChildren() {
        int count = 0;
        for (OctreeNode child : this.childNodes) {
            count += (child == null)? 1 : 0;
        }
        return (count < 8)? count : 0;
    }

    int nEmptyNodes() {
        int count = this.nullChildren();
        for (int i = 0; i < 8; i++) {

            if (this.childNodes[i] != null) {
                count += this.childNodes[i].nEmptyNodes();
            }
        }
        return count;
    }

}