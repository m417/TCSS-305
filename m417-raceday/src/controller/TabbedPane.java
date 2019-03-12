/*
* TCSS 305 – Autumn 2018 - Assignment 5 – Race Day
*/
package controller;

import static model.PropertyChangeEnabledRaceControls.PROPERTY_HEADER;
import static model.PropertyChangeEnabledRaceControls.PROPERTY_LOADING;
import static model.PropertyChangeEnabledRaceControls.PROPERTY_MESSAGE;

import java.awt.GridLayout;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.swing.JCheckBox;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;
import model.Message;
import model.RaceInfo;
import model.RaceModel;
import model.RaceParticipant;

/**
 * A TabbedPane that contains data output and racer checkboxes.
 * 
 * @author Matthew Chan
 * @version December 2018
 */
public class TabbedPane extends JTabbedPane implements PropertyChangeListener, ItemListener {

    /**
     * A generated serial version UID for object Serialization.
     * http://docs.oracle.com/javase/7/docs/api/java/io/Serializable.html
     */
    private static final long serialVersionUID = -6011860724880403023L;

    /** The number of rows. */
    private static final int ROWS = 10;
    
    /** The number of width. */
    private static final int WIDTH = 50;
    
    /** The maximum number of lines of the textarea. */
    private static final int MAX_LINES = 1100000;
    
    /** The first tab containing race output. */
    private final JTextArea myTextArea;
    
    /** The second tab containing choices for racers. */
    private final JPanel myRacerPanel;
    
    /** Functions that manipulate the race. */
    private final RaceModel myRace;
    
    /** Check box that selects/deselects all other check boxes. */
    private JCheckBox mySelectAll;
    
    /** Check to see if the 'Select All' check box was actually clicked. */
    private boolean myIsClicked;
    
    /** The scrollpane for data output stream. */
    private JScrollPane myDataScroll;
    
    /** Collection of check box with its corresponding racer ID. */
    private final Map<JCheckBox, Integer> myCheckBox;
    
    /**
     * Constructs the TabbedPane.
     * 
     * @param theRace the race model
     */
    public TabbedPane(final RaceModel theRace) {
        super();
        myTextArea = new JTextArea(ROWS, WIDTH);
        myRacerPanel = new JPanel(new GridLayout(0, 1));
        myCheckBox = new HashMap<>();
        myRace = theRace;
        myIsClicked = true;
        setUpComponents();
    }

    /**
     * Sets up the look and functionality of the tabs.
     */
    private void setUpComponents() {
        myDataScroll = new JScrollPane(myTextArea,
                                       JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
                                       JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        myTextArea.setEditable(false);
        
        final JScrollPane racerScroll = 
                        new JScrollPane(myRacerPanel, 
                                        JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, 
                                        JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        racerScroll.setPreferredSize(getSize());
        addTab("Data Output Stream", myDataScroll);
        addTab("Race Participants", racerScroll);
        
        setEnabledAt(1, false);
    }
    
    /**
     * Clears all outputs in the textarea.
     */
    public void clearOutput() {
        myTextArea.setText("");
    }
    
    /**
     * Returns data output stream scrollpane.
     * 
     * @return data output stream scrollpane
     */
    public JScrollPane getdataScrollPane() {
        return myDataScroll;
    }
    
    /**
     * Creates check boxes.
     * 
     * @param theRacers list of racers
     */
    private void createCheckBoxes(final List<RaceParticipant> theRacers) {
        myRacerPanel.removeAll();
        mySelectAll = new JCheckBox("Select All");
        mySelectAll.setSelected(true);
        mySelectAll.addItemListener(this);
        myRacerPanel.add(mySelectAll);
        for (final RaceParticipant r : theRacers) {
            final JCheckBox checkBox = new JCheckBox(r.getName());
            checkBox.setSelected(true);
            checkBox.addItemListener(this);
            myCheckBox.put(checkBox, r.getID());
            myRacerPanel.add(checkBox);
        }
    }


    @SuppressWarnings("unchecked")
    @Override
    public void propertyChange(final PropertyChangeEvent theEvent) {
        if (PROPERTY_HEADER.equals(theEvent.getPropertyName())) {
            clearOutput();
            final RaceInfo info = (RaceInfo) theEvent.getNewValue();
            createCheckBoxes(info.getRacers());
            setEnabledAt(1, true);
        } else if (PROPERTY_MESSAGE.equals(theEvent.getPropertyName())) {
            if (myTextArea.getLineCount() > MAX_LINES) {
                clearOutput();
            }
            
            final List<Message> list = (List<Message>) theEvent.getNewValue();
            final StringBuilder sb = new StringBuilder();
            for (final Message msg : list) {
                sb.append(msg.toString());
                sb.append("\n");
            }
            myTextArea.append(sb.toString());
        } else if (PROPERTY_LOADING.equals(theEvent.getPropertyName())) {
            myTextArea.append((String) theEvent.getNewValue());
        }
    }

    @Override
    public void itemStateChanged(final ItemEvent theEvent) {
        final Object obj = theEvent.getItemSelectable();
        if (obj.equals(mySelectAll) && myIsClicked) {
            for (final JCheckBox box : myCheckBox.keySet()) {
                if (theEvent.getStateChange() == ItemEvent.DESELECTED) {
                    box.setSelected(false);
                } else {
                    box.setSelected(true);
                }
            }
        } else {
            for (final JCheckBox box : myCheckBox.keySet()) {
                if (obj.equals(box)) {
                    if (theEvent.getStateChange() == ItemEvent.DESELECTED) {
                        myIsClicked = false;
                        mySelectAll.setSelected(false);
                        myIsClicked = true;
                        myRace.toggleParticipant(myCheckBox.get(box), false);
                    } else {
                        myRace.toggleParticipant(myCheckBox.get(box), true);
                        mySelectAll.setSelected(true);
                    }
                }
            } 
        }
    }

}
