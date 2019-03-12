/*
* TCSS 305 – Autumn 2018 - Assignment 5 – Race Day
*/
package model;

/**
 * The abstract message for telemetry, leaderboard, and line crossing messages.
 * 
 * @author Matthew Chan
 * @version December 2018
 */
public abstract class AbstractMessage implements Message {
    
    /** Messages formatting separator. */
    public static final String SEPARATOR = ":";
    
    /** The timestamp of the message. */
    private final int myTime;
    
    /**
     * Constructs an abstract message.
     * 
     * @param theTime timestamp of message
     */
    public AbstractMessage(final int theTime) {
        myTime = theTime;
    }

    @Override
    public int getTime() {
        return myTime;
    }

}
