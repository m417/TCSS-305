/*
 * THIS FILE CONTAINS SOLUTION CODE. THIS FILE SHOULD NOT BE DISTRIBUTED!!! IF YOU ARE A
 * STUDENT, YOU SHOULD NOT BE LOOKING AT THIS FILE AND SHOULD DELETE IT IMMEDIATELY.
 * 
 * TCSS 305 - PowerPaint
 */

package view;

import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Graphics2D;
import javax.swing.Icon;

/**
 * This class represents a swatch of color.
 * 
 * Code adapted from http://www.codebeach.com/2007/06/creating-dynamic-icons-in-java.html- a
 * tutorial on creating custom dynamic icons.
 * 
 * @author Alan Fowler
 * @version Autumn 2015
 */
public class ColorSwatch implements Icon {

    /**
     * The height of the icon.
     */
    private static final int HEIGHT = 14;

    /**
     * The width of the icon.
     */
    private static final int WIDTH = 14;

    /**
     * The color of the icon.
     */
    private Color myColor;

    /**
     * Constructor initializing fields.
     * 
     * @param theColor The color of the icon.
     */
    public ColorSwatch(final Color theColor) {
        myColor = theColor;
    }

    /**
     * Changes the color of the icon.
     * 
     * @param theColor The color to change the icon to.
     */
    public void setColor(final Color theColor) {
        myColor = theColor;
    }

    @Override
    public int getIconHeight() {
        return HEIGHT;
    }

    @Override
    public int getIconWidth() {
        return WIDTH;
    }

    @Override
    public void paintIcon(final Component theComponent, final Graphics theGraphics,
                          final int theX, final int theY) {
        final Graphics2D g2d = (Graphics2D) theGraphics;
        g2d.setPaint(myColor);
        g2d.fillOval(theX, theY, WIDTH, HEIGHT);

        g2d.setPaint(Color.BLACK);
        g2d.drawOval(theX, theY, WIDTH, HEIGHT);
    }
}
