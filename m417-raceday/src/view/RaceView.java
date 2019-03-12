/*
* TCSS 305 – Autumn 2018 - Assignment 5 – Race Day
*/
package view;

import static model.PropertyChangeEnabledRaceControls.PROPERTY_TIME;
import static model.PropertyChangeEnabledRaceControls.PROPERTY_HEADER;
import static model.PropertyChangeEnabledRaceControls.PROPERTY_MESSAGE;
import static model.PropertyChangeEnabledRaceControls.PROPERTY_BACK_TO_START;

import java.awt.BorderLayout;
import java.beans.PropertyChangeSupport;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import model.RaceModel;

/**
 * Creates a viewable simulation of the Race Day application.
 *
 * @author Matthew Chan
 * @version December 2018
 */
public class RaceView extends JFrame {
    
    /** Property of ID. */
    protected static final String PROPERTY_SELECT = "SELECT";
    
    /** Property of ID. */
    protected static final String PROPERTY_DESELECT = "DESELECT";
    
    /**
     * A generated serial version UID for object Serialization.
     * http://docs.oracle.com/javase/7/docs/api/java/io/Serializable.html
     */
    private static final long serialVersionUID = 8207622770210005943L;
    
    /** Support on firing changes to GUI. */
    private final PropertyChangeSupport myPcs;
    
    /**
     * Creates the components to display for the RaceView.
     * 
     * @param theRace the race model
     */
    public RaceView(final RaceModel theRace) {
        super("Race View");
        setLayout(new BorderLayout());
        myPcs = new PropertyChangeSupport(this);
        setUpComponents(theRace);
    }
    
    /**
     * Sets up the components.
     * 
     * @param theRace the race model
     */
    private void setUpComponents(final RaceModel theRace) {
        final TrackPanel track = new TrackPanel(myPcs);
        theRace.addPropertyChangeListener(PROPERTY_TIME, track);
        theRace.addPropertyChangeListener(PROPERTY_HEADER, track);
        theRace.addPropertyChangeListener(PROPERTY_MESSAGE, track);
        theRace.addPropertyChangeListener(PROPERTY_BACK_TO_START, track);
        
        final LeaderBoardPanel leaderBoard = new LeaderBoardPanel(myPcs);
        theRace.addPropertyChangeListener(PROPERTY_TIME, leaderBoard);
        theRace.addPropertyChangeListener(PROPERTY_HEADER, leaderBoard);
        theRace.addPropertyChangeListener(PROPERTY_MESSAGE, leaderBoard);
        theRace.addPropertyChangeListener(PROPERTY_BACK_TO_START, leaderBoard);
        
        final StatusBar statusBar = new StatusBar();
        theRace.addPropertyChangeListener(PROPERTY_TIME, statusBar);
        theRace.addPropertyChangeListener(PROPERTY_HEADER, statusBar);
        theRace.addPropertyChangeListener(PROPERTY_MESSAGE, statusBar);
        theRace.addPropertyChangeListener(PROPERTY_BACK_TO_START, statusBar);
        myPcs.addPropertyChangeListener(PROPERTY_SELECT, statusBar);
        myPcs.addPropertyChangeListener(PROPERTY_DESELECT, statusBar);
        
        add(track, BorderLayout.WEST);
        add(leaderBoard, BorderLayout.EAST);
        add(statusBar, BorderLayout.SOUTH);
    }

    /**
     * Creates and show the RaceView.
     */
    public void createAndShowGUI() {
        setIconImage(new ImageIcon("./images/ic_walk.png").getImage());
        pack();
        setResizable(false);
        setVisible(true);
    }

}
