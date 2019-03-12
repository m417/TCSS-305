
package model;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Contains a color map for the View.
 * 
 * @author Charles Bryan
 * @version 1
 */
public final class ColorMap {

    private static final float ONE_EIGHTH = .125f;

    /** A random number generator to make random colors. */
    public static final Random GEN = new Random();

    /** The maximum number of a car id. */
    public static final int MAX_CAR_NUMBER = 100;

    /** The maximum value of a RGB. */
    public static final int MAX_RGB_VALUE = 256;

    /** The list of possible colors. */
    private static final List<Color> COLORS = new ArrayList<>();

    static {
        COLORS.add(Color.BLUE);
        COLORS.add(Color.YELLOW);
        COLORS.add(Color.RED);
        COLORS.add(Color.GREEN);
        COLORS.add(Color.PINK);
        COLORS.add(Color.MAGENTA);
        COLORS.add(Color.ORANGE);
        COLORS.add(Color.CYAN);
        COLORS.add(Color.WHITE);
        COLORS.add(Color.LIGHT_GRAY);
    }

    /**
     * Cannot instantiate.
     */
    private ColorMap() {
    }

    /**
     * Spreads the color spectrum over 100 values 0-100.
     *
     * @param theNumber the number to get a color for
     * @return The associated Color object with the number 0-100
     */
    public static Color getColor(final int theNumber) {

        final float percent = (theNumber % MAX_CAR_NUMBER) / (float) MAX_CAR_NUMBER;

        float r = 0.0f;
        float g = 0.0f;
        float b = 0.0f;

        if (percent <= ONE_EIGHTH) {
            b = 4.0f * percent + .5f;
        } else if (percent <= ONE_EIGHTH * 3.0f) {
            g = 4.0f * percent - .5f;
            b = 1.0f;
        } else if (percent <= ONE_EIGHTH * 5.0f) {
            r = 4.0f * percent - 1.5f;
            g = 1.0f;
            b = -4.0f * percent + 2.5f;
        } else if (percent <= ONE_EIGHTH * 7.0f) {
            r = 1.0f;
            g = -4.0f * percent + 3.5f;
        } else if (percent <= ONE_EIGHTH * 8.0f) {
            r = -4.0f * percent + 4.5f;
        } else {
            r = g = b = 1.0f;
        }
        return new Color((int) (r * (MAX_RGB_VALUE - 1)), (int) (g * (MAX_RGB_VALUE - 1)),
                         (int) (b * (MAX_RGB_VALUE - 1)));

    }

}
