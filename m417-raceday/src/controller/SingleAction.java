/*
* TCSS 305 – Autumn 2018
* Assignment 5 – Race Day
*/
package controller;

import java.awt.event.ActionEvent;
import javax.swing.ImageIcon;

/**
 * Defines an action when button clicked on.
 * 
 * @author Matthew Chan
 * @version December 2018
 */
public class SingleAction extends AbstractButtonAction {
    
    /**
     * A generated serial version UID for object Serialization.
     * http://docs.oracle.com/javase/7/docs/api/java/io/Serializable.html
     */
    private static final long serialVersionUID = 2588301426352355832L;
    
    /** Action to take when clicked. */
    private final Runnable myAction;
    
    /**
     * Constructs the action for click.
     * 
     * @param theText text of the button
     * @param theIcon icon of the button
     * @param theAction action to take
     */
    public SingleAction(final String theText,
                       final ImageIcon theIcon,
                       final Runnable theAction) {
        super(theText);
        setIcon(theIcon, theText);
        myAction = theAction;
    }
    
    @Override
    public void actionPerformed(final ActionEvent theEvent) {
        myAction.run();
    }
    
}