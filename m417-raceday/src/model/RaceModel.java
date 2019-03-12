/*
 * TCSS 305 – Autumn 2018 - Assignment 5 – Race Day
 */

package model;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Scanner;
import java.util.concurrent.ExecutionException;
import javax.swing.JOptionPane;
import javax.swing.SwingWorker;

/**
 * The RaceDay GUI model.
 * 
 * @author Matthew Chan
 * @version December 2018
 */
public class RaceModel implements PropertyChangeEnabledRaceControls {

    /** Separator to split up race messages. */
    private static final String SEPARATOR = ":";

    /** Error title to output when race file is wrong. */
    private static final String ERROR_TITLE = "Error!";
    
    /** Error message to output when race file is wrong. */
    private static final String ERROR_MSG = "Error Loading File.";
    
    /** The prefix of line crossing messages. */ 
    private static final String C_MSG_PREFIX = "$C";
    
    /** The prefix of leaderboard messages. */ 
    private static final String L_MSG_PREFIX = "$L";
    
    /** The prefix of telemetry messages. */ 
    private static final String T_MSG_PREFIX = "$T";

    /** Minimum number of parts of a message. */
    private static final int MIN_LENGTH = 3;

    /** The expected length of telemetry message. */
    private static final int T_MSG_LENGTH = 5;

    /** The number of lines needed to display one "Still loading" message. */
    private static final int SL_AMOUNT = 500000;

    /** Support on firing changes to GUI. */
    private final PropertyChangeSupport myPcs;
    
    /** Number of racers. */
    private int myNumOfRacers;

    /** Current time of race. */
    private int myTime;

    /** Contains all info from header. */
    private RaceInfo myRaceInfo;

    /** Current leaderboard. */
    private LeaderBoardMsg myCurrentLeader;

    /** Lists if racer ID should be disabled or not. */
    private final Map<Integer, Boolean> myTogglePart;

    /** Current list of messages. */
    private List<Message> myCurrentMsg;

    /** List of messages organized by timestamp. */
    private final List<List<Message>> myMsgs;

    /** List of header patterns to adhere to. */
    private final List<String> myHeaderList;

    /**
     * Creates the functions for the race.
     */
    public RaceModel() {
        super();
        myPcs = new PropertyChangeSupport(this);
        myHeaderList = new ArrayList<>();
        myMsgs = new ArrayList<>();
        myTogglePart = new HashMap<>();
        myRaceInfo = null;
        myTime = 0;

        addRegex();
    }

    /**
     * Create a list of patterns to follow for header.
     */
    private void addRegex() {
        myHeaderList.add("#RACE:(.+)");
        myHeaderList.add("#TRACK:(.+)");
        myHeaderList.add("#WIDTH:(\\d+)");
        myHeaderList.add("#HEIGHT:(\\d+)");
        myHeaderList.add("#DISTANCE:(\\d+)");
        myHeaderList.add("#TIME:(\\d+)");
        myHeaderList.add("#PARTICIPANTS:(\\d+)");
    }

    @Override
    public void advance() {
        advance(1);
    }

    @Override
    public void advance(final int theMillisecond) {
        final int old = myTime;
        final int newTime = myTime + theMillisecond;
        if (newTime > myRaceInfo.getTime()) {
            myTime = myRaceInfo.getTime();
            myPcs.firePropertyChange(PROPERTY_REPEAT, false, true);
            if (myTime == myRaceInfo.getTime()) {
                myPcs.firePropertyChange(PROPERTY_RACE_FINISHED, false, true);
            }
        } else {
            myTime = newTime;
        }
        myPcs.firePropertyChange(PROPERTY_TIME, old, myTime);
        printAll(old, myTime);
    }

    @Override
    public void moveTo(final int theMillisecond) {
        if (theMillisecond < 0) {
            throw new IllegalArgumentException("Time cannot be negative.");
        }
        final int old = myTime;
        myTime = theMillisecond;
        myPcs.firePropertyChange(PROPERTY_TIME, old, myTime);
        printLeader(old, theMillisecond);
    }

    @Override
    public void restartRace() {
        moveTo(0);
        myPcs.firePropertyChange(PROPERTY_BACK_TO_START, false, true);
    }
    
    /**
     * Helper method that prints out only the most recent leaderboard message.
     * 
     * @param theStart the earlier timestamp
     * @param theEnd the later timestamp
     */
    private void printLeader(final int theStart, final int theEnd) {
        int start = 0;
        int end = 0;
        if (theStart > theEnd) {
            start = theEnd;
            end = theStart;
        } else {
            start = theStart;
            end = theEnd;
        }

        boolean skip = false;
        for (int i = end; i >= 0; i--) {
            for (final Message msg : myMsgs.get(i)) {
                if (msg instanceof LeaderBoardMsg) {
                    if (msg.getTime() != myCurrentLeader.getTime()) {
                        myCurrentLeader = (LeaderBoardMsg) msg;
                        final List<Message> list = new ArrayList<>();
                        list.add(myCurrentLeader);
                        myPcs.firePropertyChange(PROPERTY_MESSAGE, null, list);
                    }
                    skip = true;
                    break;
                }
            }
            if (skip) {
                break;
            }
        }
        printAll(start, end);
    }

    /**
     * Prints output of messages from specific timestamp intervals.
     * 
     * @param theBegin starting millisecond
     * @param theEnd ending millisecond
     */
    private void printAll(final int theBegin, final int theEnd) {
        final List<Message> oldMsg = myCurrentMsg;
        myCurrentMsg = new ArrayList<>();
        int newBegin = theBegin;
        if (theBegin != 0) {
            newBegin++;
        }
        for (int i = newBegin; i <= theEnd; i++) {
            for (final Message msg : myMsgs.get(i)) {
                if (!(msg instanceof TelemetryMsg) 
                                || myTogglePart.get(((TelemetryMsg) msg).getRacerID())) {
                    myCurrentMsg.add(msg);
                }
            }
        }
        myPcs.firePropertyChange(PROPERTY_MESSAGE, oldMsg, myCurrentMsg);
    }

    @Override
    public void toggleParticipant(final int theParticpantID, final boolean theToggle) {
        if (myTogglePart.containsKey(theParticpantID)) {
            myTogglePart.put(theParticpantID, theToggle);
        }
    }

    @Override
    public void loadRace(final File theRaceFile) throws IOException {
        final BackgroundThread backThread = new BackgroundThread(theRaceFile);
        backThread.execute();
    }

    /**
     * Loads the race information.
     * 
     * @param theHeader input of header messages
     * @throws IOException if race file format is invalid
     */
    private void loadHeader(final Scanner theHeader) throws IOException {
        String oldLine = "";
        String newLine = "";

        if (theHeader.hasNextLine()) {
            newLine = theHeader.nextLine();
        }
        final Queue<String> header = new LinkedList<>();
        for (final String regex : myHeaderList) {
            lineValidation(theHeader.hasNextLine(), newLine, regex);
            header.add(newLine.split(SEPARATOR)[1]);
            oldLine = newLine;
            newLine = theHeader.nextLine();
        }
        myNumOfRacers = Integer.parseInt(oldLine.replaceAll("\\D+", ""));

        final List<RaceParticipant> racers = new ArrayList<>();
        for (int i = 0; i < myNumOfRacers; i++) {
            lineValidation(theHeader.hasNextLine(), newLine, "#(\\d+):(.+):(-*)(\\d*)(.\\d?)");
            final String[] racerInfo = newLine.split(SEPARATOR);
            final int racerID = Integer.parseInt(racerInfo[0].substring(1));
            racers.add(new RaceParticipant(racerID, racerInfo[1],
                                           Double.parseDouble(racerInfo[2])));
            myTogglePart.put(racerID, true);
            if (i < myNumOfRacers - 1) {
                newLine = theHeader.nextLine();
            }
        }

        myRaceInfo = new RaceInfo(header.poll(), header.poll(),
                                  Integer.parseInt(header.poll()),
                                  Integer.parseInt(header.poll()),
                                  Integer.parseInt(header.poll()),
                                  Integer.parseInt(header.poll()),
                                  Integer.parseInt(header.poll()), racers);
    }

    /**
     * Loads messages and checks if they are valid.
     * 
     * @param theInput input of race messages
     * @throws IOException if input is in invalid format
     */
    private void loadMessages(final Scanner theInput) throws IOException {
        myMsgs.clear();
        final String anyDigit = "(\\d+)";
        final String twoDigit = "(\\d?\\d)";
        for (int i = 0; i <= myRaceInfo.getTime(); i++) {
            myMsgs.add(new ArrayList<>());
        }
        int count = 0;
        while (theInput.hasNextLine()) {
            final String message = theInput.nextLine();
            final String[] msgArr = message.split(SEPARATOR);
            if (msgArr.length < MIN_LENGTH || !msgArr[0].matches("\\$[LTC]")
                || !msgArr[1].matches(anyDigit)) {
                throw new IOException(ERROR_MSG);
            }
            final int time = Integer.parseInt(msgArr[1]);
            if (msgArr[0].equals(L_MSG_PREFIX)) {
                checkLength(msgArr.length - 2, myNumOfRacers);
                final int[] racerIDs = new int[myNumOfRacers];
                for (int i = 0; i < myNumOfRacers; i++) {
                    final String temp = msgArr[i + 2];
                    lineValidation(temp, twoDigit);
                    racerIDs[i] = Integer.parseInt(temp);
                }
                final LeaderBoardMsg leaderboard = new LeaderBoardMsg(time, racerIDs);
                if (count == 0) {
                    myCurrentLeader = leaderboard;
                }
                myMsgs.get(time).add(leaderboard);

            } else if (msgArr[0].equals(T_MSG_PREFIX)) {
                checkLength(msgArr.length, T_MSG_LENGTH);
                lineValidation(msgArr[2], twoDigit);
                lineValidation(msgArr[2 + 1], "(-?)(\\d+)(.\\d)?(\\d?)");
                lineValidation(msgArr[2 + 2], anyDigit);
                final TelemetryMsg telemetry =
                                new TelemetryMsg(time, Integer.parseInt(msgArr[2]),
                                                 Double.parseDouble(msgArr[2 + 1]),
                                                 Integer.parseInt(msgArr[2 + 2]));
                myMsgs.get(time).add(telemetry);
            } else {
                lineValidation(msgArr[2], twoDigit);
                lineValidation(msgArr[2 + 1], anyDigit);
                lineValidation(msgArr[2 + 2], "(true|false)");
                final LineCrossingMsg crossing =
                                new LineCrossingMsg(time, Integer.parseInt(msgArr[2]),
                                                    Integer.parseInt(msgArr[2 + 1]),
                                                    Boolean.parseBoolean(msgArr[2 + 2]));
                myMsgs.get(time).add(crossing);
            }
            showStillLoading(count);
            count++;
        }

    }

    /**
     * Helper method that fires "still loading" message every 500000 message loads.
     * 
     * @param theCount the index number of message
     */
    private void showStillLoading(final int theCount) {
        if (theCount % SL_AMOUNT == 0) {
            myPcs.firePropertyChange(PROPERTY_LOADING, "", "Load file: Still loading...\n");
        }
    }

    /**
     * Checks if the lengths match.
     * 
     * @param theFirst the first length number
     * @param theSecond the second length number
     * @throws IOException if lengths are different
     */
    private void checkLength(final int theFirst, final int theSecond) throws IOException {
        if (theFirst != theSecond) {
            throw new IOException(ERROR_MSG);
        }
    }

    /**
     * Helper method that finds the validity of a certain part of the file.
     * 
     * @param theNext if there is a next element
     * @param theLine the line to be checked
     * @param theRegex validation pattern
     * @throws IOException if the line format is invalid
     */
    private void lineValidation(final boolean theNext, final String theLine,
                                final String theRegex)
                    throws IOException {
        if (!theNext || !theLine.matches(theRegex)) {
            throw new IOException(ERROR_MSG);
        }
    }

    /**
     * Helper method that only compares if string has valid pattern.
     * 
     * @param theLine the line to be checked
     * @param theRegex validation pattern
     * @throws IOException if the line format is invalid
     */
    private void lineValidation(final String theLine, final String theRegex)
                    throws IOException {
        lineValidation(true, theLine, theRegex);
    }

    @Override
    public void addPropertyChangeListener(final PropertyChangeListener theListener) {
        myPcs.addPropertyChangeListener(theListener);
    }

    @Override
    public void removePropertyChangeListener(final PropertyChangeListener theListener) {
        myPcs.removePropertyChangeListener(theListener);
    }

    @Override
    public void addPropertyChangeListener(final String thePropertyName,
                                          final PropertyChangeListener theListener) {
        myPcs.addPropertyChangeListener(thePropertyName, theListener);
    }

    @Override
    public void removePropertyChangeListener(final String thePropertyName,
                                             final PropertyChangeListener theListener) {
        myPcs.removePropertyChangeListener(thePropertyName, theListener);
    }

    /**
     * Returns the last line of a file.
     * 
     * @param theFile the input file
     * @return the last line of the file
     */
    private String readLastLine(final File theFile) {
        String lastLine = "";
        final int readByteA = 0xA;
        final int readByteB = 0xD;
        try {
            final RandomAccessFile fileHandler = new RandomAccessFile(theFile, "r");
            final long fileLength = fileHandler.length() - 1;
            final StringBuilder sb = new StringBuilder();

            for (long filePointer = fileLength; filePointer != -1; filePointer--) {
                fileHandler.seek(filePointer);
                final int readByte = fileHandler.readByte();

                if (readByte == readByteA) {
                    if (filePointer == fileLength) {
                        continue;
                    }
                    break;

                } else if (readByte == readByteB) {
                    if (filePointer == fileLength - 1) {
                        continue;
                    }
                    break;
                }

                sb.append((char) readByte);
            }
            fileHandler.close();
            lastLine = sb.reverse().toString();
        } catch (final IOException ex) {
            lastLine = ERROR_MSG;
        }
        return lastLine;
    }

    /**
     * Background thread for file loading.
     * 
     * @author Matthew Chan
     * @version December 2018
     */
    private final class BackgroundThread extends SwingWorker<Integer, Void> {
        
        /** The race file. */
        private final File myFile;

        /**
         * Constructs a BackGroundThread.
         * 
         * @param theFile the race file
         */
        private BackgroundThread(final File theFile) {
            super();
            myFile = theFile;
        }

        @Override
        protected Integer doInBackground() throws Exception {
            final Scanner input = new Scanner(myFile);
            loadHeader(input);
            if (getTotalLap(readLastLine(myFile)) <= 0) {
                throw new InterruptedException();
            }
            myRaceInfo.setNumOfLap(getTotalLap(readLastLine(myFile)));
            myPcs.firePropertyChange(PROPERTY_HEADER, null, myRaceInfo);
            String msg = "Load file: Start - This may take a while. Please be patient.\n";
            myPcs.firePropertyChange(PROPERTY_LOADING, null, msg);
            msg = "Load file: Race Information Loaded.\n";
            myPcs.firePropertyChange(PROPERTY_LOADING, null, msg);
            msg = "Load file: Loading telemetry information...\n";
            myPcs.firePropertyChange(PROPERTY_LOADING, null, msg);
            loadMessages(input);
            msg = "Load file: Complete!\n\n";
            myPcs.firePropertyChange(PROPERTY_LOADING, null, msg);
            final int old = myTime;
            myTime = 0;
            myPcs.firePropertyChange(PROPERTY_TIME, old, myTime);
            return 0;
        }

        @Override
        protected void done() {
            try {
                get();
                myPcs.firePropertyChange(PROPERTY_DONE_LOADING, null, 0);
            } catch (final InterruptedException | ExecutionException ex) {
                JOptionPane.showMessageDialog(null, ERROR_MSG, ERROR_TITLE,
                                              JOptionPane.ERROR_MESSAGE);
                myPcs.firePropertyChange(PROPERTY_ERROR, null, 0);
            }
        }
        
        /**
         * Returns the total number of laps in the race.
         * 
         * @param theLine the last line of the race file
         * @return the total number of laps in the race
         */
        private int getTotalLap(final String theLine) {
            int totalLap = 0;
            final String[] msgArr = theLine.split(SEPARATOR);
            if (msgArr[0].equals(T_MSG_PREFIX)) {
                totalLap = Integer.parseInt(msgArr[2 + 2]);
            } else if (msgArr[0].equals(C_MSG_PREFIX)) {
                totalLap = Integer.parseInt(msgArr[2 + 1]);
            }
            return totalLap;
        }
    }
}
