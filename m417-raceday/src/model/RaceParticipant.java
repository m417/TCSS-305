/*
* TCSS 305 – Autumn 2018 - Assignment 5 – Race Day
*/
package model;

import java.awt.Color;
import java.util.Random;

/**
 * Creates a race participant.
 * 
 * @author Matthew Chan
 * @version December 2018
 */
public class RaceParticipant {

    /** Random chooser for RGB numbers. */
    private static final Random RAND = new Random();
    
    /** ID of racer. */
    private final int myID;
    
    /** Name of racer. */
    private final String myName;
    
    /** Starting distance of racer. */
    private final double myDistance;
    
    /** The color of the racer. */
    private Color myColor;
    
    /**
     * Constructs a race participant.
     * 
     * @param theID the racer id
     * @param theName the racer name
     * @param theDistance the racer starting distance
     */
    public RaceParticipant(final int theID, final String theName, final double theDistance) {
        myID = theID;
        myName = theName;
        myDistance = theDistance;
        myColor = ColorMap.getColor(myID);
        //generateColor();
    }

    /**
     * Returns the racer id.
     * 
     * @return the racer id
     */
    public int getID() {
        return myID;
    }
    
    /**
     * Returns the racer name.
     * 
     * @return the racer name
     */
    public String getName() {
        return myName;
    }
    
    /**
     * Returns the racer starting distance.
     * 
     * @return the racer starting distance
     */
    public double getDistance() {
        return myDistance;
    }
    
    /**
     * Returns the racer color.
     * 
     * @return the racer color
     */
    public Color getColor() {
        return myColor;
    }
    
    /**
     * Sets the color of the racer.
     */
    private void generateColor() {
        final float h = RAND.nextFloat();
        final float s = 0.45f;
        final float b = 1.0f;

        myColor = Color.getHSBColor(h, s, b);
    }
}
