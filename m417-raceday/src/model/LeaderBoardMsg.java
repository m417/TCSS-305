/*
* TCSS 305 – Autumn 2018 - Assignment 5 – Race Day
*/
package model;

/**
 * The leaderboard message.
 * 
 * @author Matthew Chan
 * @version December 2018
 */
public class LeaderBoardMsg extends AbstractMessage {

    /** Contains all racer ids. */
    private final int[] myRacerIDs;
    
    /**
     * Constructs a leaderboard message.
     * 
     * @param theTime timestamp of message
     * @param theRacerIDs all IDs of racers
     */
    public LeaderBoardMsg(final int theTime, final int[] theRacerIDs) {
        super(theTime);
        myRacerIDs = theRacerIDs.clone();
    }
    
    /**
     * Returns all racer ids.
     * 
     * @return all racer ids
     */
    public int[] getRacerIDs() {
        return myRacerIDs.clone();
    }
    
    @Override
    public String toString() {
        final StringBuilder builder = new StringBuilder(32);
        builder.append("$L");
        builder.append(SEPARATOR);
        builder.append(getTime());
        for (final int racerID : myRacerIDs) {
            builder.append(SEPARATOR);
            builder.append(racerID);
        }
        return builder.toString();
    }

}
