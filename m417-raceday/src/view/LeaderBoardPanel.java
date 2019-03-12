/*
* TCSS 305 – Autumn 2018 - Assignment 5 – Race Day
*/
package view;

import static model.PropertyChangeEnabledRaceControls.PROPERTY_HEADER;
import static model.PropertyChangeEnabledRaceControls.PROPERTY_MESSAGE;
import static model.PropertyChangeEnabledRaceControls.PROPERTY_BACK_TO_START;
import static view.RaceView.PROPERTY_DESELECT;
import static view.RaceView.PROPERTY_SELECT;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;
import model.ColorMap;
import model.LeaderBoardMsg;
import model.Message;
import model.RaceInfo;
import model.RaceParticipant;

/**
 * Creates a leaderboard panel for viewers to see who is first.
 * 
 * @author Matthew Chan
 * @version December 2018
 */
public class LeaderBoardPanel extends JPanel implements PropertyChangeListener {

    /**
     * A generated serial version UID for object Serialization.
     * http://docs.oracle.com/javase/7/docs/api/java/io/Serializable.html
     */
    private static final long serialVersionUID = 2724170349689540827L;

    /** Constant of the dimension of the board size. */
    private static final Dimension BOARD_SIZE = new Dimension(300, 400);
    
    /** Constant of the dimension of the panel size. */
    private static final Dimension PANEL_SIZE = new Dimension(255, 30);
    
    /** Padding for leaderboard panels. */
    private static final int PADDING = 6;
    
    /** Support on firing changes to GUI. */
    private final PropertyChangeSupport myPcs;
    
    /** Collection of Leaderboard panels sorted by racer ID. */
    private final Map<Integer, JPanel> myRacerBoard;
    
    /** The initial leaderboard. */
    private final List<JPanel> myInitialLeaderBoard;
    
    /**
     * Sets up the leaderboard panel.
     * 
     * @param thePcs support on firing changes to GUI
     */
    public LeaderBoardPanel(final PropertyChangeSupport thePcs) {
        super();
        setPreferredSize(BOARD_SIZE);
        setLayout(new FlowLayout(FlowLayout.CENTER, 0, 0));
        myPcs = thePcs;
        myRacerBoard = new HashMap<>();
        myInitialLeaderBoard = new ArrayList<>();
        setUpComponents();
    }

    /**
     * Sets up border of leaderboard panel.
     */
    private void setUpComponents() {
        final TitledBorder title;
        title = BorderFactory.createTitledBorder("LEADER BOARD");
        title.setTitleJustification(TitledBorder.CENTER);
        setBorder(title);
        addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(final MouseEvent theEvent) {
                if (theEvent.getButton() == MouseEvent.BUTTON1) {
                    myPcs.firePropertyChange(PROPERTY_DESELECT, false, true);
                }
            }
        });
    }
    
    @Override
    public void propertyChange(final PropertyChangeEvent theEvent) {
        if (PROPERTY_HEADER.equals(theEvent.getPropertyName())) {
            myInitialLeaderBoard.clear();
            final RaceInfo info = (RaceInfo) theEvent.getNewValue();
            removeAll();
            for (final RaceParticipant r : info.getRacers()) {
                final JPanel racerPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 0));
                final JLabel label = new JLabel(r.getID() + ": " + r.getName(),
                                               SwingConstants.CENTER);
                label.setPreferredSize(PANEL_SIZE);
//                label.setBorder(BorderFactory.
//                               createBevelBorder(BevelBorder.RAISED));
                //label.setBorder(BorderFactory.createBevelBorder(BevelBorder.RAISED));
                label.setBorder(BorderFactory.
                                createEtchedBorder(EtchedBorder.LOWERED, Color.BLACK , null));
                label.setOpaque(true);
                label.setIcon(new ColorSwatch(ColorMap.getColor(r.getID())));
                //label.setBackground(r.getColor());
                racerPanel.add(label);
                racerPanel.setBorder(BorderFactory.createEmptyBorder(PADDING, 0, 0, 0));
                racerPanel.addMouseListener(new MouseAdapter() {
                    @Override
                    public void mousePressed(final MouseEvent theEvent) {
                        if (theEvent.getButton() == MouseEvent.BUTTON1) {
                            myPcs.firePropertyChange(PROPERTY_SELECT, null, r.getID());
                        }
                    }
                });
                add(racerPanel);
                myRacerBoard.put(r.getID(), racerPanel);
                myInitialLeaderBoard.add(racerPanel);
            }
            repaint();
            revalidate();
        } else if (PROPERTY_MESSAGE.equals(theEvent.getPropertyName())) {
            @SuppressWarnings("unchecked")
            final List<Message> list = (List<Message>) theEvent.getNewValue();
            for (final Message msg : list) {
                if (msg instanceof LeaderBoardMsg) {
                    removeAll();
                    final LeaderBoardMsg lbMsg = (LeaderBoardMsg) msg;
                    for (final int id : lbMsg.getRacerIDs()) {
                        add(myRacerBoard.get(id));
                    }
                }
            }
            repaint();
            revalidate();
        } else if (PROPERTY_BACK_TO_START.equals(theEvent.getPropertyName())) {
            removeAll();
            for (int i = 0; i < myInitialLeaderBoard.size(); i++) {
                add(myInitialLeaderBoard.get(i));
            }
            repaint();
            revalidate();
        }
    }
    
}
