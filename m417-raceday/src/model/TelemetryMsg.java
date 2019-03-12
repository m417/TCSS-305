/*
* TCSS 305 – Autumn 2018 - Assignment 5 – Race Day
*/
package model;

/**
 * The telemetry message.
 * 
 * @author Matthew Chan
 * @version December 2018
 */
public class TelemetryMsg extends AbstractMessage {

    /** Racer ID. */
    private final int myRacerID;
    
    /** Distance that the racer is currently at. */
    private final double myDistance;
    
    /** Lap number that the racer is currently on. */
    private final int myLap;
    
    /**
     * Constructs a telemetry message.
     * 
     * @param theTime the timestamp
     * @param theRacerID the racer's ID
     * @param theDistance the distance currently at
     * @param theLap the lap currently at
     */
    public TelemetryMsg(final int theTime, final int theRacerID,
                     final double theDistance, final int theLap) {
        super(theTime);
        myRacerID = theRacerID;
        myDistance = theDistance;
        myLap = theLap;
    }
    
    /**
     * Returns the racer ID.
     * 
     * @return the racer ID
     */
    public int getRacerID() {
        return myRacerID;
    }

    /**
     * Returns the current distance.
     * 
     * @return the current distance
     */
    public double getDistance() {
        return myDistance;
    }
    
    /**
     * Returns the current lap number.
     * 
     * @return the current lap number
     */
    public int getLap() {
        return myLap;
    }
    
    @Override
    public String toString() {
        final StringBuilder builder = new StringBuilder(32);
        builder.append("$T");
        builder.append(SEPARATOR);
        builder.append(getTime());
        builder.append(SEPARATOR);
        builder.append(myRacerID);
        builder.append(SEPARATOR);
        builder.append(myDistance);
        builder.append(SEPARATOR);
        builder.append(myLap);
        return builder.toString();
    }
}
