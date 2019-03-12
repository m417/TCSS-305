/*
* TCSS 305 – Autumn 2018 - Assignment 5 – Race Day
*/
package controller;

import static model.PropertyChangeEnabledRaceControls.PROPERTY_TIME;

import java.awt.BorderLayout;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.text.DecimalFormat;
import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 * The Timer Panel which shows the race time.
 * 
 * @author Charles Bryan
 * @version Autumn 2015
 */
public class TimerPanel extends JPanel implements PropertyChangeListener {
    
    /**
     * A generated serial version UID for object Serialization.
     * http://docs.oracle.com/javase/7/docs/api/java/io/Serializable.html
     */
    private static final long serialVersionUID = -7291834519178571214L;
    
    /** The separator for formatted. */
    private static final String SEPARATOR = ":";
    
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
    
    /** Padding for the border around the label. */
    private static final int PADDING = 5;
    
    /** A label to display time. */
    private final JLabel myLabel;
    
    /**
     * Constructs a time panel. 
     */
    public TimerPanel() {
        super();
        myLabel = new JLabel(formatTime(0));
        setupComponents();
    }
    
    /**
     * Sets and layout components. 
     */
    private void setupComponents() {
        setLayout(new BorderLayout());
        myLabel.setBorder(BorderFactory.createCompoundBorder(
                                 BorderFactory.createEtchedBorder(), 
                                 BorderFactory.createEmptyBorder(PADDING, 
                                                                 PADDING, 
                                                                 PADDING, 
                                                                 PADDING)));
        add(myLabel, BorderLayout.CENTER);
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
        if (PROPERTY_TIME.equals(theEvent.getPropertyName())) {
            myLabel.setText(formatTime((Integer) theEvent.getNewValue()));
        }
    }

}
