/*
* TCSS 305 – Autumn 2018 - Assignment 5 – Race Day
*/
package controller;

import static model.PropertyChangeEnabledRaceControls.PROPERTY_REPEAT;

import java.awt.event.ActionEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import javax.swing.Action;
import javax.swing.ImageIcon;

/**
 * Defines the RepeatAction.
 * 
 * @author Matthew Chan
 * @version December 2018
 */
public class RepeatAction extends AbstractButtonAction implements PropertyChangeListener {
    
    /**
     * A generated serial version UID for object Serialization.
     * http://docs.oracle.com/javase/7/docs/api/java/io/Serializable.html
     */
    private static final long serialVersionUID = -1290763853594660489L;
    
    /** Text for initial and toggle of action. */
    private final String myFirstText;
    
    /** Text for secondary toggle of action. */
    private final String mySecondText;
    
    /** Icon for initial and toggle of action. */
    private final ImageIcon myFirstIcon;
    
    /** Icon for secondary toggle of action. */
    private final ImageIcon mySecondIcon;
    
    /** Action to take. */
    private final Runnable myAction;
    
    /** Flag on which to toggle to. */
    private boolean myFlag;
    
    /**
     * Constructs the action for repeat toggle.
     * 
     * @param theFirstText initial text of button
     * @param theSecondText toggled text of button
     * @param theFirstIcon initial icon of button
     * @param theSecondIcon toggled icon of button
     * @param theAction the action to take
     */
    public RepeatAction(final String theFirstText, final String theSecondText, 
                        final ImageIcon theFirstIcon, final ImageIcon theSecondIcon, 
                        final Runnable theAction) {
        super(theFirstText);
        setIcon(theFirstIcon, theFirstText);
        myFlag = true;
        myFirstText = theFirstText;
        mySecondText = theSecondText;
        myFirstIcon = theFirstIcon;
        mySecondIcon = theSecondIcon;
        myAction = theAction;
    }

    @Override
    public void actionPerformed(final ActionEvent theEvent) {
        if (!myFlag) {
            putValue(Action.NAME, myFirstText);
            setIcon(myFirstIcon, myFirstText);
        } else {
            putValue(Action.NAME, mySecondText);
            setIcon(mySecondIcon, mySecondText);
        }
        myFlag = !myFlag;
    }
    
    @Override
    public void propertyChange(final PropertyChangeEvent theEvent) {
        if (PROPERTY_REPEAT.equals(theEvent.getPropertyName()) && !myFlag) {
            myAction.run();
        }
    }

}
