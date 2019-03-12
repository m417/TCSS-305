/*
* TCSS 305 – Autumn 2018 - Assignment 5 – Race Day
*/
package model;

/**
 * The line crossing message.
 * 
 * @author Matthew Chan
 * @version December 2018
 */
public class LineCrossingMsg extends AbstractMessage {

    /** Racer ID. */
    private final int myRacerID;
    
    /** Lap number that was crossed. */
    private final int myLap;
    
    /** Provides if race was finished. */
    private final boolean myIsFinish;
    
    /**
     * Constructs a line crossing message.
     * 
     * @param theTime The timestamp
     * @param theRacerID The racer's ID
     * @param theLap The lap that was crossed
     * @param theFinish If the race was finished
     */
    public LineCrossingMsg(final int theTime, final int theRacerID,
                        final int theLap, final boolean theFinish) {
        super(theTime);
        myRacerID = theRacerID;
        myLap = theLap;
        myIsFinish = theFinish;
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
     * Returns the lap number crossed.
     * 
     * @return the lap number crossed
     */
    public int getLap() {
        return myLap;
    }
    
    /**
     * Returns if the racer has finished the race or not.
     * 
     * @return if the racer has finished the race or not
     */
    public boolean isFinish() {
        return myIsFinish;
    }
    
    @Override
    public String toString() {
        final StringBuilder builder = new StringBuilder(32);
        builder.append("$C");
        builder.append(SEPARATOR);
        builder.append(getTime());
        builder.append(SEPARATOR);
        builder.append(myRacerID);
        builder.append(SEPARATOR);
        builder.append(myLap);
        builder.append(SEPARATOR);
        builder.append(myIsFinish);
        return builder.toString();
    }
    
}
