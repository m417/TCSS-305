/*
* TCSS 305 – Autumn 2018 - Assignment 5 – Race Day
*/
package model;

import java.beans.PropertyChangeListener;

/**
 * Defines behaviors allowing PropertyChangeListeners to be added or removed from a 
 * RaceControls object. Implementing classes should inform PropertyChangeListeners
 * when methods defined in RaceControls mutate the state of the Race. 
 * 
 * Defines a set of Properties that may be listened too. Implementing class may further define
 * more Properties. 
 * 
 * @author Charles Bryan
 * @version Fall 2018
 */
public interface PropertyChangeEnabledRaceControls extends RaceControls {
   
    /*
     * Add your own constant Property values here. 
     */
    
    /** Property for change in time. */
    String PROPERTY_TIME = "TIME";
    
    /** Property for update in header info. */
    String PROPERTY_HEADER = "HEADER";
    
    /** Property for new message output. */
    String PROPERTY_MESSAGE = "MESSAGE";
    
    /** Property for loading race file. */
    String PROPERTY_LOADING = "LOADING";
    
    /** Property for finished race indication. */
    String PROPERTY_REPEAT = "REPEAT";
    
    /** Property for race that has started. */
    String PROPERTY_RACE_FINISHED = "FINISHED";
    
    /** Property for race that has finished loading. */
    String PROPERTY_DONE_LOADING = "DONE";
    
    /** Property for error while loading the race. */
    String PROPERTY_ERROR = "ERROR";
    
    /** Property for reset the race winner. */
    String PROPERTY_BACK_TO_START = "BACK";
   
    
    /**
     * Add a PropertyChangeListener to the listener list. The listener is registered for 
     * all properties. The same listener object may be added more than once, and will be 
     * called as many times as it is added. If listener is null, no exception is thrown and 
     * no action is taken.
     * 
     * @param theListener The PropertyChangeListener to be added
     */
    void addPropertyChangeListener(PropertyChangeListener theListener);
    
    
    /**
     * Add a PropertyChangeListener for a specific property. The listener will be invoked only 
     * when a call on firePropertyChange names that specific property. The same listener object
     * may be added more than once. For each property, the listener will be invoked the number 
     * of times it was added for that property. If propertyName or listener is null, no 
     * exception is thrown and no action is taken.
     * 
     * @param thePropertyName The name of the property to listen on.
     * @param theListener The PropertyChangeListener to be added
     */
    void addPropertyChangeListener(String thePropertyName, PropertyChangeListener theListener);

    /**
     * Remove a PropertyChangeListener from the listener list. This removes a 
     * PropertyChangeListener that was registered for all properties. If listener was added 
     * more than once to the same event source, it will be notified one less time after being 
     * removed. If listener is null, or was never added, no exception is thrown and no action 
     * is taken.
     * 
     * @param theListener The PropertyChangeListener to be removed
     */
    void removePropertyChangeListener(PropertyChangeListener theListener);
    
    /**
     * Remove a PropertyChangeListener for a specific property. If listener was added more than
     * once to the same event source for the specified property, it will be notified one less 
     * time after being removed. If propertyName is null, no exception is thrown and no action 
     * is taken. If listener is null, or was never added for the specified property, no 
     * exception is thrown and no action is taken.
     * 
     * @param thePropertyName The name of the property that was listened on.
     * @param theListener The PropertyChangeListener to be removed
     */
    void removePropertyChangeListener(String thePropertyName, 
                                      PropertyChangeListener theListener);
    
}
