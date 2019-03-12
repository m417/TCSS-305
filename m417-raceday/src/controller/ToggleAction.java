/*
* TCSS 305 – Autumn 2018 - Assignment 5 – Race Day
*/
package controller;

import static model.PropertyChangeEnabledRaceControls.PROPERTY_RACE_FINISHED;

import java.awt.event.ActionEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import javax.swing.Action;
import javax.swing.ImageIcon;

/**
 * Defines actions that toggle between two different states.
 * 
 * @author Matthew Chan
 * @version December 2018
 */
public class ToggleAction extends AbstractButtonAction implements PropertyChangeListener {

    /**
     * A generated serial version UID for object Serialization.
     * http://docs.oracle.com/javase/7/docs/api/java/io/Serializable.html
     */
    private static final long serialVersionUID = 7769623929056473731L;

    /** Text for initial and toggle of action. */
    private final String myFirstText;
    
    /** Text for secondary toggle of action. */
    private final String mySecondText;
    
    /** Icon for initial and toggle of action. */
    private final ImageIcon myFirstIcon;
    
    /** Icon for secondary toggle of action. */
    private final ImageIcon mySecondIcon;
    
    /** Action to take for first toggle. */
    private final Runnable myFirstAction;
    
    /** Action to take for second toggle. */
    private final Runnable mySecondAction;
    
    /** Flag on which to toggle to. */
    private boolean myFlag;
    
    /**
     * Constructs the action for toggle.
     * 
     * @param theFirstText initial text of button
     * @param theSecondText toggled text of button
     * @param theFirstIcon initial icon of button
     * @param theSecondIcon toggled icon of button
     * @param theFirstAction the first action
     * @param theSecondAction the second action
     */
    public ToggleAction(final String theFirstText,
                 final String theSecondText, 
                 final ImageIcon theFirstIcon, 
                 final ImageIcon theSecondIcon,
                 final Runnable theFirstAction,
                 final Runnable theSecondAction) {
        super(theFirstText);
        setIcon(theFirstIcon, theFirstText);
        myFlag = true;
        myFirstText = theFirstText;
        mySecondText = theSecondText;
        myFirstIcon = theFirstIcon;
        mySecondIcon = theSecondIcon;
        myFirstAction = theFirstAction;
        mySecondAction = theSecondAction;
    }
    
    @Override
    public void actionPerformed(final ActionEvent theEvent) {
        if (!myFlag) {
            changeAction(myFirstAction, myFirstText, myFirstIcon);
        } else {
            changeAction(mySecondAction, mySecondText, mySecondIcon);
        }
    } 
    
    /**
     * Helper method that toggles the button and its behavior.
     * 
     * @param theAction action to take
     * @param theText text to display
     * @param theIcon icon to display
     */
    private void changeAction(final Runnable theAction, final String theText,
                              final ImageIcon theIcon) {
        theAction.run();
        putValue(Action.NAME, theText);
        setIcon(theIcon, theText);
        myFlag = !myFlag;
    }
    
    @Override
    public void propertyChange(final PropertyChangeEvent theEvent) {
        if (PROPERTY_RACE_FINISHED.equals(theEvent.getPropertyName())) {
            changeAction(myFirstAction, myFirstText, myFirstIcon);
        }
    }
}
