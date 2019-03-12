/*
* TCSS 305 – Autumn 2018
* Assignment 5 – Race Day
*/
package controller;

import java.awt.Image;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.ImageIcon;

/**
 * Abstract class that defines all race actions.
 * 
 * @author Matthew Chan
 * @version December 2018
 */
public abstract class AbstractButtonAction extends AbstractAction {
    
    /**
     * A generated serial version UID for object Serialization.
     * http://docs.oracle.com/javase/7/docs/api/java/io/Serializable.html
     */
    private static final long serialVersionUID = 4657286966800235717L;

    /**
     * Constructs the action for AbstractAction.
     * 
     * @param theText text of the button
     */
    public AbstractButtonAction(final String theText) {
        super(theText);
        setEnabled(false);
    }
    
    /**
     * Sets the icons of the buttons.
     * 
     * @param theIcon icon for the button
     * @param theText text for the button
     */
    public void setIcon(final ImageIcon theIcon, final String theText) {
        final ImageIcon icon = (ImageIcon) theIcon;
        final Image largeImage =
            icon.getImage().getScaledInstance(24, 24, java.awt.Image.SCALE_SMOOTH);
        final ImageIcon largeIcon = new ImageIcon(largeImage);
        putValue(Action.LARGE_ICON_KEY, largeIcon);
        
        final Image smallImage =
            icon.getImage().getScaledInstance(12, -1, java.awt.Image.SCALE_SMOOTH);
        final ImageIcon smallIcon = new ImageIcon(smallImage);
        putValue(Action.SMALL_ICON, smallIcon);
        
        final char c = theText.charAt(0);
        putValue(Action.MNEMONIC_KEY, (int) c);
    }
    
}
