public class Simulation {

    public static final double G = 6.6743e-11; // gravitational constant
    private static final int stars = 1000;
    public static final int scale = 1000;

    public static void main(String[] args) {
        StdDraw.setCanvasSize(scale, scale);
        StdDraw.setXscale(0, scale);
        StdDraw.setYscale(0, scale);
        StdDraw.enableDoubleBuffering();
        StdDraw.clear(StdDraw.BLACK);

        int seconds = 0;

        Octree octree = new Octree(scale, 1);
        Body bodies[] = new Body[stars];
        for (int i = 0; i < stars; i++) {
            bodies[i] = RandNumGen.generateRndBody(octree.getLength());
        }

        while (true) {

            seconds++;
            octree.clear();

            for (int i = 0; i < bodies.length; i++) {
                if (bodies[i].inside(scale)) {
                    octree.addBody(bodies[i]);
                }
            }

            //System.out.println(seconds + " seconds | empty nodes: " + octree.getEmptyNodes());

            for (int i = 0; i < bodies.length; i++) {
                if (bodies[i].inside(scale)) {
                    octree.move(bodies[i]);
                }
            }

            //clear old positions (exclude the following line if you want to draw orbits).
            StdDraw.clear(StdDraw.BLACK);

            if (seconds % (60) == 0) {
                for (int i = 0; i < bodies.length; i++) {
                    if (bodies[i].inside(scale)) {
                        bodies[i].draw();
                    }
                }

                StdDraw.show();
            }
        }
    }
}
