/*
 * TCSS 305 – Autumn 2018 - Assignment 5 – Race Day
 */

package view;

import static model.PropertyChangeEnabledRaceControls.PROPERTY_TIME;
import static model.PropertyChangeEnabledRaceControls.PROPERTY_HEADER;
import static model.PropertyChangeEnabledRaceControls.PROPERTY_MESSAGE;
import static model.PropertyChangeEnabledRaceControls.PROPERTY_BACK_TO_START;
import static view.RaceView.PROPERTY_SELECT;
import static view.RaceView.PROPERTY_DESELECT;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;

import model.ColorMap;
import model.Message;
import model.RaceInfo;
import model.RaceParticipant;
import model.TelemetryMsg;

/**
 * Status bar of the race's time and selected racer.
 * 
 * @author Matthew Chan
 * @version December 2018
 */
public class StatusBar extends JPanel implements PropertyChangeListener {

    /**
     * A generated serial version UID for object Serialization.
     * http://docs.oracle.com/javase/7/docs/api/java/io/Serializable.html
     */
    private static final long serialVersionUID = -40377662266456910L;

    /** The separator for formatted. */
    private static final String SEPARATOR = ":";

    /** Title of participant status. */
    private static final String SELECT = "<html><b><i> Select a racer to display "
                    + "his/her information.</i></b></html>";

    /** Constant of how much to pad from the border. */
    private static final int PADDING = 10;

    /** The number of milliseconds in a second. */
    private static final int MILLIS_PER_SEC = 1000;

    /** The number of seconds in a minute. */
    private static final int SEC_PER_MIN = 60;

    /** The number of minute in a hour. */
    private static final int MIN_PER_HOUR = 60;
    
    /** The maximum ArrayList size. */
    private static final int MAX_LIST_SIZE = 500;

    /** A formatter to require at least 2 digits, leading 0s. */
    private static final DecimalFormat TWO_DIGIT_FORMAT = new DecimalFormat("00");

    /** A formatter to require at least 3 digits, leading 0s. */
    private static final DecimalFormat THREE_DIGIT_FORMAT = new DecimalFormat("000");

    /** Constant of the status bar dimension size. */
    private static final Dimension BAR_SIZE = new Dimension(800, 25);

    /** The font used for the JLabels. */
    private static final Font FONT = new Font("Apple Casual", Font.PLAIN, 12);

    /** The font used for the JLabels. */
    private static final Color COLOR = new Color(210, 210, 210);

    /** Current time of the race. */
    private final JLabel myClock;

    /** The participant text JLabel. */
    private final JLabel myParticipant;
    
    /** Information about participant. */
    private final JLabel myParticipantInfo;

    /** Current ID of chosen racer. */
    private int myRacerID;

    /** Current timestamp. */
    private int myTime;
    
    /** See if any racer is selected. */
    private boolean myIsRacerSelected;

    /** All the current racers. */
    private final Map<Integer, RaceParticipant> myRacers;

    /** The current timestamp telemetry messages. */
    private final List<TelemetryMsg> myTelemetryMsg;

    /**
     * Constructs the status bar of the view.
     */
    public StatusBar() {
        super();
        myRacers = new HashMap<>();
        myParticipant = new JLabel("Participant:");
        myParticipantInfo = new JLabel(SELECT);
        myClock = new JLabel(formatTime(0));
        myTelemetryMsg = new ArrayList<>();
        myRacerID = -1;
        myTime = 0;
        myIsRacerSelected = false;

        setupComponents();
    }

    /**
     * Sets up and layout components.
     */
    private void setupComponents() {
        setLayout(new BorderLayout());
        setPreferredSize(BAR_SIZE);
        setBackground(COLOR);
        setBorder(BorderFactory.createEmptyBorder(0, PADDING, 0, PADDING));
        
        myClock.setFont(FONT);
        myClock.setBorder(BorderFactory.createEmptyBorder(PADDING, PADDING, PADDING, PADDING));
        myParticipant.setFont(FONT);
        myParticipantInfo.setFont(FONT);
        myParticipant.setBorder(BorderFactory.createEmptyBorder(PADDING, PADDING, 
                                                                PADDING, PADDING));
        myParticipantInfo.setBorder(BorderFactory.createEmptyBorder(PADDING, 0, PADDING, 0));

        add(myParticipant, BorderLayout.WEST);
        add(myParticipantInfo, BorderLayout.CENTER);
        add(myClock, BorderLayout.EAST);
    }

    /**
     * This formats a positive integer into minutes, seconds, and milliseconds. 00:00:000
     * 
     * @param theTime the time to be formatted
     * @return the formated string.
     */
    private String formatTime(final long theTime) {
        long time = theTime;
        final long milliseconds = time % MILLIS_PER_SEC;
        time /= MILLIS_PER_SEC;
        final long seconds = time % SEC_PER_MIN;
        time /= SEC_PER_MIN;
        final long min = time % MIN_PER_HOUR;
        time /= MIN_PER_HOUR;
        return "<html>Elapsed Time: <b><i>" + TWO_DIGIT_FORMAT.format(min) + SEPARATOR
               + TWO_DIGIT_FORMAT.format(seconds) + SEPARATOR
               + THREE_DIGIT_FORMAT.format(milliseconds) + "</i></b><html>";
    }

    @Override
    public void propertyChange(final PropertyChangeEvent theEvent) {
        if (PROPERTY_TIME.equals(theEvent.getPropertyName())) {
            myTime = (Integer) theEvent.getNewValue();
            myClock.setText(formatTime(myTime));
        } else if (PROPERTY_HEADER.equals(theEvent.getPropertyName())) {
            propertyHeader(theEvent);
        } else if (PROPERTY_MESSAGE.equals(theEvent.getPropertyName())) {
            propertyMessage(theEvent);
        } else if (PROPERTY_BACK_TO_START.equals(theEvent.getPropertyName())) {
            propertyBackToStart(theEvent);
        } else if (PROPERTY_SELECT.equals(theEvent.getPropertyName())) {
            myIsRacerSelected = true;
            myRacerID = (Integer) theEvent.getNewValue();
            for (final TelemetryMsg tMsg : myTelemetryMsg) {
                refresh(tMsg);
            }
        } else if (PROPERTY_DESELECT.equals(theEvent.getPropertyName())) {
            myIsRacerSelected = false;
            myParticipantInfo.setIcon(null);
            myParticipantInfo.setText(SELECT);
            setBackground(COLOR);
        } 
    }

    /**
     * Helper method of listening PROPERTY_HEADER change event.
     * 
     * @param theEvent the property change event
     */
    private void propertyHeader(final PropertyChangeEvent theEvent) {
        setBackground(COLOR);
        myClock.setText(formatTime(0));
        myParticipantInfo.setText(SELECT);
        setPreferredSize(BAR_SIZE);
        final RaceInfo info = (RaceInfo) theEvent.getNewValue();
        for (final RaceParticipant r : info.getRacers()) {
            myRacers.put(r.getID(), r);
            myTelemetryMsg.add(new TelemetryMsg(0, r.getID(), r.getDistance(), 0));
        }
    }
    
    /**
     * Helper method of listening PROPERTY_MESSAGE change event.
     * 
     * @param theEvent the property change event
     */
    private void propertyMessage(final PropertyChangeEvent theEvent) {
        if (myTelemetryMsg.size() > MAX_LIST_SIZE) {
            myTelemetryMsg.clear();
        }
        @SuppressWarnings("unchecked")
        final List<Message> list = (List<Message>) theEvent.getNewValue();
        for (final Message msg : list) {
            if (msg instanceof TelemetryMsg) {
                final TelemetryMsg tMsg = (TelemetryMsg) msg;
                myTelemetryMsg.add(tMsg);
                if (myIsRacerSelected) {
                    refresh(tMsg); 
                }
            }
        }
    }
    
    /**
     * Helper method of listening PROPERTY_BACK_TO_START change event.
     * 
     * @param theEvent the property change event
     */
    private void propertyBackToStart(final PropertyChangeEvent theEvent) {
        myTelemetryMsg.clear();
        for (final RaceParticipant r : myRacers.values()) {
            myTelemetryMsg.add(new TelemetryMsg(0, r.getID(), r.getDistance(), 0));
        }
        for (final TelemetryMsg tMsg : myTelemetryMsg) {
            refresh(tMsg);
        }
    }
    
    /**
     * Refresh the status bar.
     * 
     * @param theMsg the telemetry message to be displayed
     */
    private void refresh(final TelemetryMsg theMsg) {
        if (myIsRacerSelected) {
            if (theMsg.getRacerID() == myRacerID) {
                final StringBuilder sb = new StringBuilder();
                sb.append("#");
                sb.append(myRacerID);
                sb.append(" -------");
                sb.append(myRacers.get(myRacerID).getName());
                sb.append("------- Lap: ");
                sb.append(theMsg.getLap());
                sb.append(" - Distance: ");
                sb.append(theMsg.getDistance());
                myParticipantInfo.setIcon(new ColorSwatch(ColorMap.getColor(myRacerID)));
                myParticipantInfo.setText("<html><b><i>" + sb.toString() + "</i></b></html>");
                //setBackground(myRacers.get(myRacerID).getColor());
                revalidate();
            }
        } else {
            myParticipantInfo.setText(SELECT);
        }
    }
    
}
