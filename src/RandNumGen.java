import java.util.Random;
import java.awt.*;

public class RandNumGen {
    public static Body generateRndBody(double scale) {

        Random random = new Random();

        String name = randomString();
        double mass = (Math.random() * 1e8);

        double radius = Math.random() * 2 + 1;
        double genX = random.nextDouble() * scale;
        double genY = random.nextDouble() * scale;
        double genZ = random.nextDouble() * scale;
        Vector3 position = new Vector3(genX, genY, genZ);
        Vector3 currentMovement = new Vector3(0, 0, 0);
        Color color = randomColor();

        Body body = new Body(name, mass, radius, position, currentMovement, color);
        return body;
    }

    public static String randomString() {
        int start = 'a';
        int end = 'z';
        Random random = new Random();

        StringBuilder builder = new StringBuilder(15);
        for (int i = 0; i < builder.capacity(); i++) {
            char letter = (char) (start + (random.nextDouble() * (end - start + 1)));
            builder.append(letter);
        }
        return builder.toString();
    }

    public static Color randomColor() {
        Random random = new Random();
        float r = random.nextFloat();
        float g = random.nextFloat();
        float b = random.nextFloat();

        return new Color(r, g, b);
    }
}
