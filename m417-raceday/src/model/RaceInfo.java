/*
* TCSS 305 – Autumn 2018 - Assignment 5 – Race Day
*/
package model;

import java.util.List;

/**
 * Information about the race.
 * 
 * @author Matthew Chan
 * @version December 2018
 */
public class RaceInfo {

    /** Name of track. */
    private final String myName;
    
    /** Track type. */
    private final String myTrack;
    
    /** Width of track. */
    private final int myWidth;
    
    /** Height of track. */
    private final int myHeight;
    
    /** Distance of race. */
    private final int myDistance;
    
    /** Time length of race. */
    private final int myTime;
    
    /** Number of race participants. */
    private final int myNumOfRacer;
    
    /** Total number of laps. */
    private int myNumOfLap;
    
    /** List of race participants and their info. */
    private final List<RaceParticipant> myRacers;
    
    /**
     * Creates the compilation of race info.
     * 
     * @param theName name of track
     * @param theTrack type of track
     * @param theWidth width of track
     * @param theHeight height of track
     * @param theDistance distance of race
     * @param theTime time length of race
     * @param theNumOfRacer number of participants
     * @param theRacers list of racers info
     */
    public RaceInfo(final String theName, final String theTrack,
                  final int theWidth, final int theHeight,
                  final int theDistance, final int theTime,
                  final int theNumOfRacer, final List<RaceParticipant> theRacers) {
        
        myName = theName;
        myTrack = theTrack;
        myWidth = theWidth;
        myHeight = theHeight;
        myDistance = theDistance;
        myTime = theTime;
        myNumOfRacer = theNumOfRacer;
        myRacers = theRacers;
        myNumOfLap = 0;
    }
    
    /**
     * Returns name of track.
     * 
     * @return name of track
     */
    public String getName() {
        return myName;
    }
    
    /**
     * Returns type of track.
     * 
     * @return type of track
     */
    public String getTrack() {
        return myTrack;
    }
    
    /**
     * Returns width of track.
     * 
     * @return width of track
     */
    public int getWidth() {
        return myWidth;
    }
    
    /**
     * Returns height of track.
     * 
     * @return height of track
     */
    public int getHeight() {
        return myHeight;
    }
    /**
     * Returns distance of race.
     * 
     * @return distance of race
     */
    public int getDistance() {
        return myDistance;
    }
    
    /**
     * Returns time length of race.
     * 
     * @return time length of race
     */
    public int getTime() {
        return myTime;
    }
    
    /**
     * Returns the number of participants.
     * 
     * @return the number of participants
     */
    public int getNumOfRacer() {
        return myNumOfRacer;
    }
    
    /**
     * Returns the total number of laps.
     * 
     * @return the total number of laps
     */
    public int getNumOfLap() {
        return myNumOfLap;
    }
    
    /**
     * Sets the total number of laps.
     * 
     * @param theNumOfLap the total number of laps
     */
    public void setNumOfLap(final int theNumOfLap) {
        myNumOfLap = theNumOfLap;
    }
    
    /**
     * Returns list of racers info.
     * 
     * @return list of racers info
     */
    public List<RaceParticipant> getRacers() {
        return myRacers;
    }

}
