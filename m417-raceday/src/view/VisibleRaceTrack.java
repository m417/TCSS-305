/*
* TCSS 305 – Autumn 2018 - Assignment 5 – Race Day
*/
package view;

import java.awt.Point;
import java.awt.Shape;
import java.awt.geom.CubicCurve2D;
import java.awt.geom.Line2D;
import java.awt.geom.PathIterator;
import java.awt.geom.Point2D;
import java.awt.geom.RoundRectangle2D;

/**
 * This class is a RoundedRectangle2D.Double that automatically shapes itself to look 
 * similar to a race track, with semi-circular curves on the left and right 
 * and two straitaways connecting them.
 * 
 * This class also parameterizes the rounded rectangle based on the given distance value, 
 * which can then be used to determine positions on the track using the 
 * getPointAtDistance(double) method.
 * 
 * The distance is arbitrary and is not related to the actual mathematical distance 
 * around the race track. 
 * The distance is used purely to determine relative positions of racers on the track.
 * 
 * @author Matthew Chan
 * @version December 2018
 */
public class VisibleRaceTrack extends RoundRectangle2D.Double {
    
    /**
     * A generated serial version UID for object Serialization.
     * http://docs.oracle.com/javase/7/docs/api/java/io/Serializable.html
     */
    private static final long serialVersionUID = 4632988310264095209L;

    /** The number of rectangle sides. */
    private static final double RECT_SIDES = 4.0;
    
    /** The points on straight sides. */
    private double myPointsOnStraights;
    
    /** The points on curved sides. */
    private double myPointsOnTurns;
    
    /** The first turn segment. */
    private CubicCurve2D.Double myFirstTurn;
    
    /** The second turn segment. */
    private CubicCurve2D.Double mySecondTurn;
    
    /** The third turn segment. */
    private CubicCurve2D.Double myThirdTurn;
    
    /** The fourth turn segment. */
    private CubicCurve2D.Double myFourthTurn;
    
    /** The top stright line of the track. */
    private Line2D.Double myTopStraight;
    
    /** The bottom stright line of the track. */
    private Line2D.Double myBottomStraight;
    
    /** The track path iterator. */
    private PathIterator myPathIterator;
    
    /** The last point. */
    private Point myLastPoint;
    
    /** The track length. */
    private final int myLength;

    /**
     * Constructs a VisibleRaceTrack.
     * 
     * @param theX the x value of the track
     * @param theY the y value of the track
     * @param theWidth the track width
     * @param theHeight the track height
     * @param theLength the track length
     */
    public VisibleRaceTrack(final int theX, final int theY, final int theWidth, 
                            final int theHeight, final int theLength) {
        super(theX, theY, theWidth, theHeight, theHeight, theHeight);
        this.myLength = theLength;
        if (theWidth < theHeight) {
            throw new IllegalArgumentException("Racetracks width must be longer than height");
        }
        this.calculateCurvesAndStraights();
        this.calculateDivisions();
    }

    /**
     * Returns the arbitrary distance of one lap around the race track.
     * 
     * @return the arbitrary distance of one lap around the race track
     */
    public int getTrackDistance() {
        return this.myLength;
    }

    /**
     * Helper method to calculate divisions.
     */
    private void calculateDivisions() {
        double straightToTurnPercentage =
                        (this.getWidth() - this.getHeight()) / this.getWidth();
        final double almostCircular = 0.3;
        final double smallAdjustment = 0.01;
        final double mediumAdjustment = 0.05;
        final double largeAdjustment = 0.1;
        if (straightToTurnPercentage > almostCircular) {
            straightToTurnPercentage -= largeAdjustment;
        } else if (straightToTurnPercentage > largeAdjustment) {
            straightToTurnPercentage -= mediumAdjustment;
        } else if (straightToTurnPercentage > 0.0) {
            straightToTurnPercentage -= smallAdjustment;
        }
        this.myPointsOnTurns = ((double) this.myLength
                                - (double) this.myLength * straightToTurnPercentage)
                               / RECT_SIDES;
        this.myPointsOnStraights = (double) this.myLength * straightToTurnPercentage / 2.0;
    }

    /**
     * Helper method to calculate curves and straight lines.
     */
    private void calculateCurvesAndStraights() {
        this.myPathIterator = this.getPathIterator(null);
        this.myLastPoint = new Point(0, 0);
        this.myTopStraight =
                        new Line2D.Double(this.getX() + this.getWidth()
                                          - this.getHeight() / 2.0, this.getY(),
                                          this.getX() + this.getHeight() / 2.0, this.getY());
        this.myBottomStraight =
                        new Line2D.Double(this.getX() + this.getHeight() / 2.0,
                                          this.getY() + this.getHeight(),
                                          this.getX() + this.getWidth() - this.getHeight()
                                                                          / 2.0,
                                          this.getY() + this.getHeight());
        this.mySecondTurn = this.advanceIteratorToNextCubicCurve();
        this.myThirdTurn = this.advanceIteratorToNextCubicCurve();
        this.myFourthTurn = this.advanceIteratorToNextCubicCurve();
        this.myFirstTurn = this.advanceIteratorToNextCubicCurve();
    }

    /**
     * Helper method to iterate the paths.
     * 
     * @return cubic parametric curve segment
     */
    private CubicCurve2D.Double advanceIteratorToNextCubicCurve() {
        this.myPathIterator.next();
        final int numberOfCoords = 6;
        final int three = 3;
        final int four = 4;
        final int five = 5;
        double[] coordArray = new double[numberOfCoords];
        while (this.myPathIterator.currentSegment(coordArray) != three) {
            this.myPathIterator.next();
            this.myLastPoint.setLocation(coordArray[0], coordArray[1]);
            coordArray = new double[numberOfCoords];
        }
        return new CubicCurve2D.Double(this.myLastPoint.getX(), this.myLastPoint.getY(),
                                       coordArray[0], coordArray[1], coordArray[2],
                                       coordArray[three], 
                                       coordArray[four], coordArray[five]);
    }

    /**
     * Returns a Point2D.Double at the given distance on the race track.
     * 
     * @param theDistance the distance value
     * @return a Point2D.Double at the given distance on the race track
     */
    public Point2D.Double getPointAtDistance(final double theDistance) {
        double correctedDistance = theDistance;
        while (correctedDistance < 0.0) {
            correctedDistance += (double) this.myLength;
        }
        while (correctedDistance > (double) this.myLength) {
            correctedDistance -= (double) this.myLength;
        }
        final double firstTurnDistance = this.myPointsOnTurns;
        final double secondTurnDistance = this.myPointsOnTurns * 2.0;
        final double firstStraightDistance = secondTurnDistance + this.myPointsOnStraights;
        final double thirdTurnDistance = this.myPointsOnTurns * 3.0 + this.myPointsOnStraights;
        final double fourthTurnDistance = this.myPointsOnTurns * 4.0 
                        + this.myPointsOnStraights;
        final double secondStraightDistance = fourthTurnDistance + this.myPointsOnStraights;
        Shape specificShape = new Line2D.Double(0.0, 0.0, 0.0, 0.0);
        double t = 0.0;
        if (correctedDistance < firstTurnDistance) {
            t = correctedDistance / firstTurnDistance;
            specificShape = this.myFirstTurn;
        } else if (correctedDistance < secondTurnDistance) {
            t = (correctedDistance - firstTurnDistance) / this.myPointsOnTurns;
            specificShape = this.mySecondTurn;
        } else if (correctedDistance < firstStraightDistance) {
            t = (correctedDistance - secondTurnDistance) / this.myPointsOnStraights;
            specificShape = this.myBottomStraight;
        } else if (correctedDistance < thirdTurnDistance) {
            t = (correctedDistance - firstStraightDistance) / this.myPointsOnTurns;
            specificShape = this.myThirdTurn;
        } else if (correctedDistance < fourthTurnDistance) {
            t = (correctedDistance - thirdTurnDistance) / this.myPointsOnTurns;
            specificShape = this.myFourthTurn;
        } else if (correctedDistance < secondStraightDistance) {
            t = (correctedDistance - fourthTurnDistance) / this.myPointsOnStraights;
            specificShape = this.myTopStraight;
        }
        return this.parametricSolver(specificShape, t);
    }

    /**
     * Helper method for getPointAtDistance.
     * 
     * @param theShape the shape type
     * @param theTValue the T value
     * @return a Point2D point
     */
    private Point2D.Double parametricSolver(final Shape theShape, final double theTValue) {
        final Point2D.Double returnPoint = new Point2D.Double(0.0, 0.0);
        if (theShape instanceof Line2D) {
            final Line2D.Double line = (Line2D.Double) theShape;
            returnPoint.x = line.getX1() + theTValue * (line.getX2() - line.getX1());
            returnPoint.y = line.getY1() + theTValue * (line.getY2() - line.getY1());
        } else {
            final CubicCurve2D.Double curve = (CubicCurve2D.Double) theShape;
            final double squared = 2.0;
            final double cubed = 3.0;
            returnPoint.x = Math.pow(1.0 - theTValue, cubed) * curve.x1
                + cubed * theTValue * Math.pow(1.0 - theTValue, squared) * curve.ctrlx1
                + cubed * Math.pow(theTValue, squared) * (1.0 - theTValue) * curve.ctrlx2
                + Math.pow(theTValue, cubed) * curve.x2;
            returnPoint.y = Math.pow(1.0 - theTValue, cubed) * curve.y1
                + cubed * theTValue * Math.pow(1.0 - theTValue, 2.0) * curve.ctrly1
                + cubed * Math.pow(theTValue, squared) * (1.0 - theTValue) * curve.ctrly2
                + Math.pow(theTValue, cubed) * curve.y2;
        }
        return returnPoint;
    }
    
}
