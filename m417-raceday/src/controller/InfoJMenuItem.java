/*
* TCSS 305 – Autumn 2018 - Assignment 5 – Race Day
*/
package controller;

import static model.PropertyChangeEnabledRaceControls.PROPERTY_HEADER;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.text.DecimalFormat;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import model.RaceInfo;

/**
 * A JMenuItem that displays race info.
 * 
 * @author Matthew Chan
 * @version December 2018
 */
public class InfoJMenuItem extends JMenuItem implements PropertyChangeListener {
    
    /**
     * A generated serial version UID for object Serialization.
     * http://docs.oracle.com/javase/7/docs/api/java/io/Serializable.html
     */
    private static final long serialVersionUID = -7565785640756825510L;
    
    /** The number of milliseconds in a second. */
    private static final int MILLIS_PER_SEC = 1000;
    
    /** The number of seconds in a minute. */
    private static final int SEC_PER_MIN = 60;
    
    /** The number of minute in a hour. */
    private static final int MIN_PER_HOUR = 60;
    
    /** A formatter to require at least 2 digits, leading 0s. */
    private static final DecimalFormat TWO_DIGIT_FORMAT = new DecimalFormat("00");
    
    /** A formatter to require at least 3 digits, leading 0s. */
    private static final DecimalFormat THREE_DIGIT_FORMAT = new DecimalFormat("000");
    
    /** The constant of new line. */
    private static final String NEW_LINE = "\n";
    
    /** The formatting separator. */
    private static final String SEPARATOR = ":";
    
    /** The message to display. */
    private String myMessage;
    
    /**
     * Constructs the info JMenuItem.
     */
    public InfoJMenuItem() {
        super("Race Info...");
        myMessage = "";
        addActionListener(theEvent -> {
            JOptionPane.showMessageDialog(this, myMessage, "Race Information",
                                          JOptionPane.INFORMATION_MESSAGE);
        });
        setEnabled(false);
    }
    
    /**
     * This formats a positive integer into minutes, seconds, and milliseconds. 
     * 00:00:000
     * 
     * @param theTime the time to be formatted
     * @return the formated string. 
     */
    public static String formatTime(final long theTime) {
        long time = theTime;
        final long milliseconds = time % MILLIS_PER_SEC;
        time /= MILLIS_PER_SEC;
        final long seconds = time % SEC_PER_MIN;
        time /= SEC_PER_MIN;
        final long min = time % MIN_PER_HOUR;
        time /= MIN_PER_HOUR;
        return TWO_DIGIT_FORMAT.format(min) + SEPARATOR
                        + TWO_DIGIT_FORMAT.format(seconds) 
                        + SEPARATOR + THREE_DIGIT_FORMAT.format(milliseconds);
    }

    @Override
    public void propertyChange(final PropertyChangeEvent theEvent) {
        if (PROPERTY_HEADER.equals(theEvent.getPropertyName())) {
            final RaceInfo info = (RaceInfo) theEvent.getNewValue();
            final StringBuilder sb = new StringBuilder();
            sb.append(info.getName());
            sb.append(NEW_LINE);
            sb.append("Track type: ");
            sb.append(info.getTrack());
            sb.append(NEW_LINE);
            sb.append("Total time: ");
            sb.append(formatTime(info.getTime()));
            sb.append(NEW_LINE);
            sb.append("Lap Distance: ");
            sb.append(info.getDistance());
            sb.append(NEW_LINE);
            sb.append("Total Lap: ");
            sb.append(info.getNumOfLap());
            myMessage = sb.toString();
            setEnabled(true);
        }
    }
    
}
