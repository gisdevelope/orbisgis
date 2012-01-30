
/**
 * @package org.orbisgis.view.events
 * @brief Event manage system
 */
package org.orbisgis.view.events;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;
/**
 * @brief manage the processing of propagation of events.
 * 
 * Ex :
 *
 * An eventListener is linked with at least 2 objects,
 * an EventSource and an EventTarget. The listener will be removed automatically
 * if the associated EventSource no longer exist. 
 * However the event target is hard linked with listener, then an event target
 * would remove listener.
 * 
 * To declare an new event :
 *   The class that own the event must implement EventSource and store in final static the EventName related to it
 *   The class that will create listeners and be called by them should create the listener by this way :
 *   code : EventHandler.create(Listener.class, targetInstance, "methodOfTarget","the property name of EventData to pass into the methodOfTarget as parameter");
 *   EventHandler will return a new listener, this listener must be pushed into the EventDispatcher :
 *   code :EventDispatcher.addListener(targetInstance,theNewListener, EventSourceClass.SOMETHING_EVENT, sourceInstance);
 *   The target object must free the listeners by using this method :
 *   code: EventDispatcher.removeListeners(targetInstance);
 */
public class EventDispatcher {
    //Link between the event name, event source and listener ID
    //Used by OnEvent
    private static HashMap<EventName,ListenerListManager> links = new HashMap<EventName,ListenerListManager>();
    //Active listener list
    private static HashMap<UUID,Listener> listeners = new  HashMap<UUID,Listener>();
    //Link between target and listeners
    //Used by target to no longer by called by listener, and guarentee 
    //that the target will be freed by the garbage collector
    private static HashMap<Integer,ArrayList<UUID>> targetToListener = new HashMap<Integer,ArrayList<UUID>>();
    /**
     * Called by an Event Source to fire attached listeners
     * @param evtName Event description
     * @param parameters Event specific data
     * @param watchedInstance The source of the event, usually "this"
     */
    public static void onEvent(EventData parameters) throws EventException {
       if(!(parameters.getSource() instanceof EventSource)) {
           throw new EventException("Event source must implement EventSourceInstance.");
       }
        //Compute all sub event (for an EventName of a/b/c it will fire a/b/c then a/b then a )
       EventName[] evtHierarchy=parameters.getEventName().getDecomposedEvent();
       for(EventName firedEvent : evtHierarchy) {
           //Search for this event in the hashTable
           ListenerListManager foundManager=links.get(firedEvent);
           if(foundManager!=null) {
               //Search the instance related listener list
               ListenerList llist = foundManager.get((EventSource)parameters.getSource());
               if(llist!=null) {
                   //For each UUID
                   for(EventSourceInstanceFinalizeWitness listenerInst : llist) {                  
                       Listener listener = listeners.get(listenerInst.getUniqueId());
                       if(listener!=null) {
                           try {
                                //Fire event
                                listener.onEvent(parameters);
                           }
                           catch (ListenerException exc){ //If the event throw an error then stop propagation if the listener want to
                               if(!exc.letContinueProcessing()) {
                                   throw new EventException(exc.getMessage());
                               }
                           }
                       } else {
                           //The listener has been removed by the target
                           //Remove the reference in the source list
                           llist.remove(listenerInst);
                           if(llist.isEmpty()) {
                               //That was the last listener attached to this source
                               foundManager.remove((EventSource)parameters.getSource());
                           }
                       }                       
                   }
               }
           }
       }
    }
    /**
     * Add a listener to an Object/Event
     * @param target The target associated with the listener !=watchedInstance
     * @param listener Object that inherit from Listener class
     * @param evtName The event description
     * @param watchedInstance Null or a specific instance, this object must not be strong referenced by the listener, otherwise listener could never be automatically removed.
     * @warning Do not forget to call the method  
     * EventDispatcher.removeListeners(target); 
     * to remove listeners linked with your action class, 
     * to let the garbage collector free your target instance.
     * @return The unique ID of the new event listener
     */
    public synchronized static UUID addListener(Object target,Listener listener,EventName evtName, EventSource watchedInstance) {
        //Compute a unique ID for the listener
        EventSourceInstanceFinalizeWitness evtLink = new EventSourceInstanceFinalizeWitness();
        //Retrieve the target listener list
        Integer targetHashCode = target.hashCode();
        ArrayList<UUID> thelist = targetToListener.get(targetHashCode);
        if(thelist==null) {
            thelist = new ArrayList<UUID>();
            targetToListener.put(targetHashCode, thelist);
        }
        //Add the listener in the target listener list
        thelist.add(evtLink.getUniqueId());
        //Add the listener in the flat list
        listeners.put(evtLink.getUniqueId(), listener);        
        ListenerListManager foundManager;
        //Feed HashMap, retrieve WeakHashMap
        if(!links.containsKey(evtName)) {
            foundManager = new ListenerListManager();
            links.put(evtName, foundManager);
        } else {
            foundManager=links.get(evtName);
        }
        //Feed WeakHashMap, retrieve ArrayList
        ListenerList llist;
        if(!foundManager.containsKey(watchedInstance)) {
            llist = new ListenerList();
            foundManager.put(watchedInstance, llist);
        } else {
            llist = foundManager.get(watchedInstance);
        }
        llist.add(evtLink);
        return evtLink.getUniqueId();
    }
    /**
     * When a target is no longer used, the listeners created by it must be removed.
     */
    public synchronized static void removeListeners(Object target) {
        //Retrieve all UUID linked with this target
        ArrayList<UUID> thelist = targetToListener.get(target.hashCode());
        if(thelist!=null) {
            for(UUID listenerId : thelist) {
                removeListener(listenerId);
            }
            targetToListener.remove(target.hashCode());
        }
    }
    /**
     * Remove a specific listener
     * @param listenerId 
     */
    public synchronized static void removeListener(UUID listenerId) {
        if(listeners.containsKey(listenerId)) {
            listeners.remove(listenerId);
        }
    }
}
