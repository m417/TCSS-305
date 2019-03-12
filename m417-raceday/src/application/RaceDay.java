/*
* TCSS 305 – Autumn 2018 - Assignment 5 – Race Day
*/
package application;

import controller.RaceController;
import java.awt.EventQueue;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

/**
 * Runs the RaceDay Program.
 * 
 * @author Matthew Chan
 * @version December 2018
 */
public final class RaceDay {

    /**
     * Private constructor, to prevent instantiation of this class.
     */
    private RaceDay() {
        throw new IllegalStateException();
    }
    
    /**
     * Starts the GUI with metal look and feel.
     * 
     * @param theArgs Command line arguments
     */
    public static void main(final String[] theArgs) {
        try {
            UIManager.setLookAndFeel("javax.swing.plaf.metal.MetalLookAndFeel");
        } catch (final UnsupportedLookAndFeelException ex) {
            ex.printStackTrace();
        } catch (final IllegalAccessException ex) {
            ex.printStackTrace();
        } catch (final InstantiationException ex) {
            ex.printStackTrace();
        } catch (final ClassNotFoundException ex) {
            ex.printStackTrace();
        }
        UIManager.put("swing.boldMetal", Boolean.FALSE);
        EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                new RaceController().createAndShowGUI();  
            }
        });
    }
}
