/*
* TCSS 305 – Autumn 2018 - Assignment 5 – Race Day
*/
package view;

import static model.PropertyChangeEnabledRaceControls.PROPERTY_HEADER;
import static model.PropertyChangeEnabledRaceControls.PROPERTY_MESSAGE;
import static model.PropertyChangeEnabledRaceControls.PROPERTY_BACK_TO_START;
import static view.RaceView.PROPERTY_SELECT;
import static view.RaceView.PROPERTY_DESELECT;

import controller.TimerPanel;
import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Point2D;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.TitledBorder;
import model.LineCrossingMsg;
import model.Message;
import model.RaceInfo;
import model.RaceParticipant;
import model.TelemetryMsg;
//import track.VisibleRaceTrack;

/**
 * Creates the Race Track to view in Race View.
 *
 * @author Matthew Chan
 * @version December 2018
 */
public class TrackPanel extends JPanel implements PropertyChangeListener {
    
    /**
     * A generated serial version UID for object Serialization.
     * http://docs.oracle.com/javase/7/docs/api/java/io/Serializable.html
     */
    private static final long serialVersionUID = -3452614450308801907L;
    
    /** The size of the Race Track Panel. */
    //private static final Dimension PANEL_SIZE = new Dimension(500, 400);
    private static final Dimension PANEL_SIZE = new Dimension(750, 600);
    
    /** The location offset of the track. */
    private static final int OFF_SET = 30;

    /** The stroke width in pixels. */
    private static final int STROKE_WIDTH = 25;

    /** The size of participants moving around the track. */
    private static final int OVAL_SIZE = 20;
    
    /** Support on firing changes to GUI. */
    private final PropertyChangeSupport myPcs;
    
    /** The visible track. */
    private VisibleRaceTrack myTrack;
    
    /** The width ratio of track. */
    private int myWidth;
    
    /** The height ratio of track. */
    private int myHeight;
    
    /** The distance of track. */
    private int myDistance;
    
    /** A JLabel to show the race winner. */
    private final JLabel myWinner;
    
    /** Colors representing racers by their ID. */
    private Map<Integer, Color> myColors;
    
    /** Circles representing racers by their ID. */
    private Map<Integer, Ellipse2D> myRacersCircle;
    
    /** All the current racers. */
    private Map<Integer, RaceParticipant> myRacers;
    
    /** The current timestamp telemetry messages. */
    private final List<LineCrossingMsg> myLineCrossingMsg;
    
    /** All the racer IDs in their initial order. */
    private final List<Integer> myOrderedIDs;
    
    /**
     * Creates the race track panel.
     * 
     * @param thePcs support on firing changes to GUI
     */
    public TrackPanel(final PropertyChangeSupport thePcs) {
        super();
        myPcs = thePcs;
        myColors = new HashMap<>();
        myRacers = new HashMap<>();
        myRacersCircle = new HashMap<>();
        myLineCrossingMsg = new ArrayList<>();
        myOrderedIDs = new ArrayList<>();
        myWinner = new JLabel();
        setUpComponents();
    }

    /**
     * Sets up the components of track panel.
     */
    private void setUpComponents() {
        final TitledBorder title;
        title = BorderFactory.createTitledBorder("Race Track");
        setBorder(title);
        
        setLayout(new BorderLayout());
        setPreferredSize(PANEL_SIZE);
        addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(final MouseEvent theEvent) {
                final List<Integer> temp = new ArrayList<>();
                temp.addAll(myOrderedIDs);
                Collections.reverse(temp);
                
                for (final int id : temp) {
                    final Ellipse2D c = myRacersCircle.get(id);
                    if ((theEvent.getButton() == MouseEvent.BUTTON1)
                        && c.contains(theEvent.getX(), theEvent.getY())) {
                        myPcs.firePropertyChange(PROPERTY_SELECT, null, id);
                        break;
                    } else if ((theEvent.getButton() == MouseEvent.BUTTON1) 
                                    && !c.contains(theEvent.getX(), theEvent.getY())) {
                        myPcs.firePropertyChange(PROPERTY_DESELECT, false, true);
                    }
                }
            }
        });
    }
    
    @Override
    public void paintComponent(final Graphics theGraphics) {
        super.paintComponent(theGraphics);
        final Graphics2D g2d = (Graphics2D) theGraphics;

        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                             RenderingHints.VALUE_ANTIALIAS_ON);

        if (myTrack != null) {
            g2d.setPaint(Color.DARK_GRAY);
            g2d.setStroke(new BasicStroke(STROKE_WIDTH));
            g2d.draw(myTrack);
        }
        
        for (final int id : myOrderedIDs) {
            g2d.setPaint(myColors.get(id));
            g2d.setStroke(new BasicStroke(1));
            g2d.fill(myRacersCircle.get(id));
        }
    }
    
    @Override
    public void propertyChange(final PropertyChangeEvent theEvent) {
        if (PROPERTY_HEADER.equals(theEvent.getPropertyName())) {
            final RaceInfo info = (RaceInfo) theEvent.getNewValue();
            myColors = new HashMap<>();
            myRacers = new HashMap<>();
            myRacersCircle = new HashMap<>();
            myHeight = info.getHeight();
            myWidth = info.getWidth();
            myDistance = info.getDistance();
            myLineCrossingMsg.clear();
            myOrderedIDs.clear();
            remove(myWinner);
            createTrack();
            for (final RaceParticipant r : info.getRacers()) {
                final Point2D.Double newDist = myTrack.getPointAtDistance(r.getDistance());
                final Ellipse2D circle = new Ellipse2D.Double(newDist.x - OVAL_SIZE / 2,
                                                                     newDist.y - OVAL_SIZE / 2,
                                                                     OVAL_SIZE, OVAL_SIZE);
                
                myColors.put(r.getID(), r.getColor());
                myRacers.put(r.getID(), r);
                myRacersCircle.put(r.getID(), circle);
                myOrderedIDs.add(r.getID());
            }
            repaint();
        } else if (PROPERTY_MESSAGE.equals(theEvent.getPropertyName())) {
            propertyMessage(theEvent);
        } else if (PROPERTY_BACK_TO_START.equals(theEvent.getPropertyName())) {
            remove(myWinner);
            //myColors = new HashMap<>();
            //myRacers = new HashMap<>();
            myRacersCircle = new HashMap<>();
            myLineCrossingMsg.clear();
            for (final RaceParticipant r : myRacers.values()) {
                final Point2D.Double newDist = myTrack.getPointAtDistance(r.getDistance());
                final Ellipse2D circle = new Ellipse2D.Double(newDist.x - OVAL_SIZE / 2,
                                                                     newDist.y - OVAL_SIZE / 2,
                                                                     OVAL_SIZE, OVAL_SIZE);
                
                //myColors.put(r.getID(), r.getColor());
                //myRacers.put(r.getID(), r);
                myRacersCircle.put(r.getID(), circle);
            }
            repaint();
        }
    }
    
    /**
     * Creates the race track.
     */
    private void createTrack() {
        final int width = PANEL_SIZE.width - OFF_SET * 2;
        final double height = ((double) myHeight / myWidth) * width;
        
        final int x = OFF_SET;
        final int y = (int) (PANEL_SIZE.height - height) / 2 + 2;
        
        myTrack = new VisibleRaceTrack(x, y, width, (int) height, myDistance);
    }
    
    /**
     * Helper method to display the winner of the race.
     * 
     * @param theMsg the line {@link Crossings} message
     */
    private void displayWinner(final LineCrossingMsg theMsg) {
        if (theMsg == myLineCrossingMsg.get(0)) {
            final int id = theMsg.getRacerID();
            final String name = myRacers.get(id).getName();
            final String usedTime = TimerPanel.formatTime(theMsg.getTime());
            final String text = "<html><b>" + id + ": " + name 
                            + " is the winner of the race!<br>Time taken: "
                            + usedTime + "</b></html>";
            
            myWinner.setForeground(Color.BLUE);
            myWinner.setText(text);
            myWinner.setHorizontalAlignment(JLabel.CENTER);
            add(myWinner, BorderLayout.CENTER);
        }
    }
    
    /**
     * Helper method of listening PROPERTY_MESSAGE change event.
     * 
     * @param theEvent the property change event
     */
    private void propertyMessage(final PropertyChangeEvent theEvent) {
        @SuppressWarnings("unchecked")
        final List<Message> list = (List<Message>) theEvent.getNewValue();
        for (final Message msg : list) {
            if (msg instanceof TelemetryMsg) {
                final TelemetryMsg tMsg = (TelemetryMsg) msg;
                final Point2D.Double newDist = myTrack.getPointAtDistance(
                                                                  tMsg.getDistance());
                myRacersCircle.get(tMsg.getRacerID()).setFrame(newDist.x - OVAL_SIZE / 2, 
                                                         newDist.y - OVAL_SIZE / 2,
                                                         OVAL_SIZE, OVAL_SIZE);
            } else if (msg instanceof LineCrossingMsg) {
                final LineCrossingMsg lcMsg = (LineCrossingMsg) msg;
                if (myLineCrossingMsg.isEmpty() && lcMsg.isFinish()) {
                    myLineCrossingMsg.add(lcMsg);
                    displayWinner(lcMsg);
                }
            }
        }
        repaint();
    }
    
}
