/*
* TCSS 305 – Autumn 2018 - Assignment 5 – Race Day
*/
package controller;

import static model.PropertyChangeEnabledRaceControls.PROPERTY_HEADER;
import static model.PropertyChangeEnabledRaceControls.PROPERTY_REPEAT;
import static model.PropertyChangeEnabledRaceControls.PROPERTY_TIME;
import static model.PropertyChangeEnabledRaceControls.PROPERTY_MESSAGE;
import static model.PropertyChangeEnabledRaceControls.PROPERTY_RACE_FINISHED;
import static model.PropertyChangeEnabledRaceControls.PROPERTY_LOADING;
import static model.PropertyChangeEnabledRaceControls.PROPERTY_DONE_LOADING;
import static model.PropertyChangeEnabledRaceControls.PROPERTY_ERROR;

import java.awt.BorderLayout;
import java.awt.Cursor;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.AdjustmentEvent;
import java.awt.event.AdjustmentListener;
import java.awt.event.KeyEvent;
import java.awt.event.WindowEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.swing.Action;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JToolBar;
import javax.swing.Timer;
import model.RaceModel;
import view.RaceView;

/**
 * The RaceDay GUI Controller.
 * 
 * @author Matthew Chan
 * @version December 2018
 */
public class RaceController extends JFrame implements PropertyChangeListener {

    /**
     * A generated serial version UID for object Serialization.
     * http://docs.oracle.com/javase/7/docs/api/java/io/Serializable.html
     */
    private static final long serialVersionUID = 1158480402300347544L;

    /** Constant of what padding of borders are. */
    private static final int PADDING = 10;
    
    /** Constant of normal speed. */
    private static final int REGULAR_MODE = 1;
    
    /** Constant of four times normal speed. */
    private static final int FAST_MODE = 4;
    
    /** Frequency on how often timer should fire in milliseconds. */
    private static final int TIMER_FREQUENCY = 29;
    
    /** The default system cursor. */
    private static final Cursor DEFAULT_CURSOR = new Cursor(Cursor.DEFAULT_CURSOR);
    
    /** The wait system cursor. */
    
    private static final Cursor WAIT_CURSOR = new Cursor(Cursor.WAIT_CURSOR);
    
    /** ImageIcon of restart. */
    private final ImageIcon myRestartIcon = new ImageIcon("./images/ic_restart.png");
    
    /** ImageIcon of play. */
    private final ImageIcon myPlayIcon = new ImageIcon("./images/ic_play.png");
    
    /** ImageIcon of pause. */
    private final ImageIcon myPauseIcon = new ImageIcon("./images/ic_pause.png");
    
    /** ImageIcon of one times speed. */
    private final ImageIcon myTimesOneIcon = new ImageIcon("./images/ic_one_times.png");
    
    /** ImageIcon of four times speed. */
    private final ImageIcon myTimesFourIcon = new ImageIcon("./images/ic_four_times.png");
    
    /** ImageIcon of one loop. */
    private final ImageIcon myRepeatOffIcon = new ImageIcon("./images/ic_repeat.png");
    
    /** ImageIcon of infinite loops. */
    private final ImageIcon myRepeatOnIcon = new ImageIcon("./images/ic_repeat_color.png");
    
    /** ImageIcon of clear. */
    private final ImageIcon myClearIcon = new ImageIcon("./images/ic_clear.png");
    
    /** ImageIcon for race application. */
    private final ImageIcon myAppIcon = new ImageIcon("./images/ic_walk.png");
    
    /** File opener to browse directory. */
    private final JFileChooser myFileChooser;
    
    /** Functions used for the race. */
    private final RaceModel myRace;   
    
    /** The timer to control how often to advance the Time object. */ 
    private final Timer myTimer;
    
    /** Current multiplier for timer. */
    private int myMultiplier;
    
    /** Tab of outputs and race participant selectors. */
    private final TabbedPane myTabbedPane;
    
    /** Slider that manipulates and follow timer. */
    private final Slider mySlider;
    
    /** List of all the actions for the buttons. */
    private List<Action> myActions;
    
    /** The race track view. */
    private RaceView myRaceView;
    
    /**
     * Constructs the icons and actions of the GUI.
     */
    public RaceController() {
        super("Race Day!");
        setLayout(new BorderLayout());
        myRace = new RaceModel();
        mySlider = new Slider(myRace);
        myTabbedPane = new TabbedPane(myRace);    
        myTimer = new Timer(TIMER_FREQUENCY, this::handleTimer);
        myMultiplier = REGULAR_MODE;
        myFileChooser = new JFileChooser(".");
        myRaceView = new RaceView(myRace);
        initActions();
    }

    /**
     * Adds the actions to a list.
     */
    private void initActions() {
        myActions = new ArrayList<>();
        
        myActions.add(new SingleAction("Restart", myRestartIcon
                                      , () -> restartRace()));
        final ToggleAction play = new ToggleAction("Play", "Pause", myPlayIcon, myPauseIcon
                                         , () -> stopTimer(), () -> startTimer());
        myActions.add(play);
        
        myActions.add(new ToggleAction("Times One", "Times Four"
                                       , myTimesOneIcon, myTimesFourIcon
                                       , () -> myMultiplier = REGULAR_MODE
                                       , () -> myMultiplier = FAST_MODE));
        
        final RepeatAction repeat = 
                        new RepeatAction("Single Race", "Loop Race", myRepeatOffIcon
                                               , myRepeatOnIcon, () -> restartRace());
        myActions.add(repeat);
        
        myActions.add(new SingleAction("Clear", myClearIcon
                                      , () -> myTabbedPane.clearOutput()));
        
        myRace.addPropertyChangeListener(PROPERTY_RACE_FINISHED, play);
        myRace.addPropertyChangeListener(PROPERTY_REPEAT, repeat);
    }
    
    /**
     * Advances timer by the frequency and multiplier.
     * 
     * @param theEvent the fired event
     */
    private void handleTimer(final ActionEvent theEvent) { //NOPMD
        myRace.advance(TIMER_FREQUENCY * myMultiplier);
    }
    
    /**
     * Helper method to start the timer and disable the slider.
     */
    private void startTimer() {
        myTimer.start();
        mySlider.setEnabled(false);
    }
    
    /**
     * Helper method to stop the timer and enable the slider.
     */
    private void stopTimer() {
        myTimer.stop();
        mySlider.setEnabled(true);
    }
    
    /**
     * Helper method to restart the race.
     */
    private void restartRace() {
        myRace.restartRace();
    }
    
    /**
     * Enables/Disables all the buttons.
     * 
     * @param theBool enables/disables the buttons
     */
    private void changeActionState(final boolean theBool) {
        for (final Action action : myActions) {
            action.setEnabled(theBool);
        }
    }

    /**
     * Creates the main panel.
     * 
     * @return the main panel
     */
    private JPanel createMainPanel() { 
        final JPanel sliderTimer = new JPanel(new BorderLayout());

        myRace.addPropertyChangeListener(PROPERTY_HEADER, mySlider);
        myRace.addPropertyChangeListener(PROPERTY_TIME, mySlider);
        myRace.addPropertyChangeListener(PROPERTY_DONE_LOADING, mySlider);
        sliderTimer.add(mySlider);
        
        final TimerPanel time = new TimerPanel();
        myRace.addPropertyChangeListener(PROPERTY_TIME, time);
        sliderTimer.add(time, BorderLayout.EAST);
        sliderTimer.setBorder(BorderFactory.createEmptyBorder(PADDING,
                                                             PADDING,
                                                             PADDING + PADDING,
                                                             PADDING));
        
        myRace.addPropertyChangeListener(PROPERTY_HEADER, myTabbedPane);
        myRace.addPropertyChangeListener(PROPERTY_MESSAGE, myTabbedPane);
        myRace.addPropertyChangeListener(PROPERTY_LOADING, myTabbedPane);
        myRace.addPropertyChangeListener(PROPERTY_DONE_LOADING, myTabbedPane);
        
        final JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.add(sliderTimer, BorderLayout.NORTH);
        mainPanel.add(myTabbedPane, BorderLayout.SOUTH);
        mainPanel.setBorder(BorderFactory.createEmptyBorder(PADDING,
                                                       PADDING,
                                                       PADDING,
                                                       PADDING));
        
        return mainPanel;
    }
    
    /**
     * Creates the toolbar.
     * 
     * @return the toolbar
     */
    private JToolBar createToolBar() {
        final JToolBar toolbar = new JToolBar();
        for (final Action action : myActions) {
            final JButton button = new JButton(action);
            button.setHideActionText(true);
            toolbar.add(button);
        }
        return toolbar;
    }
    
    /**
     * Creates the file menu.
     * 
     * @return the file menu
     */
    private JMenu createFileMenu() {
        final JMenu menu = new JMenu("File");
        menu.setMnemonic(KeyEvent.VK_F);
        final JMenuItem loadFile = new JMenuItem("Load File...");
        loadFile.addActionListener(theEvent -> loadFile());
        final JMenuItem raceView = new JMenuItem("Show Race View...");
        raceView.addActionListener(theEvent -> showRaceView());
        final JMenuItem exit = new JMenuItem("Exit");
        exit.setIcon(scaleIcon(myAppIcon));
        exit.addActionListener(theEvent -> {
            dispatchEvent(new WindowEvent(this, WindowEvent.WINDOW_CLOSING));
        });
        menu.add(loadFile);
        menu.add(raceView);
        menu.addSeparator();
        menu.add(exit);
        return menu;
    }
    
    /**
     * Creates menu for controls.
     * 
     * @return menu of controls
     */
    private JMenu createControlsMenu() {
        final JMenu menu = new JMenu("Controls");
        menu.setMnemonic(KeyEvent.VK_C);
        for (final Action action : myActions) {
            menu.add(new JMenuItem(action));
        }
        return menu;
    }
    
    /**
     * Creates the help menu.
     * 
     * @return the help menu
     */
    private JMenu createHelpMenu() {
        final JMenu menu = new JMenu("Help");
        menu.setMnemonic(KeyEvent.VK_H);
        final JMenuItem about = new JMenuItem("About...");
        final String info = "Matthew Chan\nAutumn 2018\nTCSS 305 Assignment 5";
        
        about.addActionListener(theEvent -> {
            JOptionPane.showMessageDialog(this, info, "About", 
                                          JOptionPane.INFORMATION_MESSAGE, myAppIcon);
        });
        
        final InfoJMenuItem raceInfoMenu = new InfoJMenuItem();
        myRace.addPropertyChangeListener(PROPERTY_HEADER, raceInfoMenu);
        menu.add(raceInfoMenu);
        menu.add(about);
        return menu;
    }
    
    /**
     * Creates the menu bar.
     * 
     * @return the menu bar
     */
    private JMenuBar createMenuBar() {
        final JMenuBar menuBar = new JMenuBar();
         
        menuBar.add(createFileMenu());
        menuBar.add(createControlsMenu());
        menuBar.add(createHelpMenu());
        
        return menuBar;
    }
    
    /**
     * Loads the file and checks for invalid file format.
     * 
     * @throws IOException if the race file format is invalid
     */
    private void loadFile() {
        if (!myTimer.isRunning()) {
            final int choice = myFileChooser.showOpenDialog(null);
            if (choice == JFileChooser.APPROVE_OPTION) {     
                final File selectedFile = myFileChooser.getSelectedFile();
                setCursor(WAIT_CURSOR);
                try {
                    myRace.loadRace(selectedFile);
                } catch (final IOException ex) {
                    JOptionPane.showMessageDialog(this, "Error loading file.",
                                                  "Load Error!", JOptionPane.ERROR_MESSAGE);
                }
            }
        } else {
            JOptionPane.showMessageDialog(this, "Please stop the current race first.",
                                          "Error!", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    /**
     * Shows the race track view window.
     */
    private void showRaceView() {
        myRaceView.createAndShowGUI();
    }
    
    /**
     * Sets up the components of the GUI.
     */
    private void setUpComponents() {
        add(createToolBar(), BorderLayout.SOUTH);
        add(createMainPanel(), BorderLayout.CENTER);
        setJMenuBar(createMenuBar());
    }
    
    /**
     * Create and show the RaceController.
     */
    public void createAndShowGUI() {
        myRace.addPropertyChangeListener(PROPERTY_LOADING, this);
        myRace.addPropertyChangeListener(PROPERTY_DONE_LOADING, this);
        myRace.addPropertyChangeListener(PROPERTY_ERROR, this);
        setUpComponents();
        showRaceView();
        
        setIconImage(myAppIcon.getImage());
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        pack();
        setLocationRelativeTo(null);
        setResizable(false);
        setVisible(true);
    }
    
    /**
     * Scale the image down to 12x12 pixels.
     * https://docs.oracle.com/javase/8/docs/api/java/awt/Image.html
     * 
     * @param theIcon the icon to be scaled
     * @return the scaled icon
     */
    private ImageIcon scaleIcon(final ImageIcon theIcon) {
        ImageIcon icon = theIcon;
        final Image smallImage =
                        icon.getImage().getScaledInstance(12, -1, java.awt.Image.SCALE_SMOOTH);
        icon = new ImageIcon(smallImage);
        return icon;
    }

    @Override
    public void propertyChange(final PropertyChangeEvent theEvent) {
        if (PROPERTY_DONE_LOADING.equals(theEvent.getPropertyName())) {
            changeActionState(true);
            setCursor(DEFAULT_CURSOR);
            
            myTabbedPane.getdataScrollPane().getVerticalScrollBar().
                addAdjustmentListener(new AdjustmentListener() {
                    @Override
                    public void adjustmentValueChanged(final AdjustmentEvent theEvent) {
                        if (myTimer.isRunning() || mySlider.getValueIsAdjusting()) {
                            theEvent.getAdjustable().
                                  setValue(theEvent.getAdjustable().getMaximum());
                        }
                    }
                });
        } else if (PROPERTY_ERROR.equals(theEvent.getPropertyName())) {
            setCursor(DEFAULT_CURSOR);
        }
    }
}
