/*
* TCSS 305 – Autumn 2018 - Assignment 5 – Race Day
*/
package controller;

import static model.PropertyChangeEnabledRaceControls.PROPERTY_TIME;
import static model.PropertyChangeEnabledRaceControls.PROPERTY_HEADER;
import static model.PropertyChangeEnabledRaceControls.PROPERTY_DONE_LOADING;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import javax.swing.JSlider;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import model.RaceInfo;
import model.RaceModel;

/**
 * Slider that allows users to change the timestamp of the race.
 * 
 * @author Matthew Chan
 * @version December 2018
 */
public class Slider extends JSlider implements PropertyChangeListener, ChangeListener {

    /**
     * A generated serial version UID for object Serialization.
     * http://docs.oracle.com/javase/7/docs/api/java/io/Serializable.html
     */
    private static final long serialVersionUID = 6605560790889299806L;
    
    /** The number of milliseconds in a second. */
    private static final int MILLIS_PER_SEC = 1000;

    /** The number of seconds in a minute. */
    private static final int SEC_PER_MIN = 60;
    
    /** Minor tick of slider. */
    private static final int MINOR_TICK = 10;
    
    /** The race model that control the race. */
    private final RaceModel myRace;
    
    /**
     * Constructs a slider.
     * 
     * @param theRace the race model
     */
    public Slider(final RaceModel theRace) {
        super();
        myRace = theRace;
        setValue(0);
        setEnabled(false);
        addChangeListener(this);
    }

    @Override
    public void propertyChange(final PropertyChangeEvent theEvent) {
        if (PROPERTY_TIME.equals(theEvent.getPropertyName())) {
            final int time = (Integer) theEvent.getNewValue();
            setValue(time / MILLIS_PER_SEC);
        } else if (PROPERTY_HEADER.equals(theEvent.getPropertyName())) {
            final int maxValue = ((RaceInfo) theEvent.getNewValue()).getTime();
            setMinorTickSpacing(MINOR_TICK);
            setMajorTickSpacing(SEC_PER_MIN);
            setPaintTicks(true);
            setMaximum(maxValue / MILLIS_PER_SEC);
        } else if (PROPERTY_DONE_LOADING.equals(theEvent.getPropertyName())) {
            setEnabled(true);
        }
    }

    @Override
    public void stateChanged(final ChangeEvent theEvent) {
        final JSlider slider = (JSlider) theEvent.getSource();
        if (slider.getValueIsAdjusting()) {
            myRace.moveTo(slider.getValue() * MILLIS_PER_SEC);
        }
    }

}
