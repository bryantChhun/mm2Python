package org.mm2python.DataStructures.Queues;

import org.mm2python.DataStructures.Builders.MDSParamObject;
import org.mm2python.DataStructures.Builders.MDSParameters;
import org.mm2python.DataStructures.MetaDataStore;
import org.mm2python.UI.reporter;

import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.TimeUnit;

/**
 * Methods to place / retrieve MetaDataStores in a linkedBlockingQueue
 *
 */
public class MDSQueue {

    private static final LinkedBlockingDeque<MetaDataStore> mdsQueue = new LinkedBlockingDeque<>();

    public static void resetQueue() {
        mdsQueue.clear();
    }

    // methods to add/remove MetaDataStores from lbd
    public void putMDS(MetaDataStore mds_) {
        try {
            boolean result = mdsQueue.offer(mds_, 10, TimeUnit.MILLISECONDS);
            if(!result){
                reporter.set_report_area(true, true, true, "failure to offer MDS into MDSQueue: "+mds_.toString());
            }
        } catch (InterruptedException iex) {
            reporter.set_report_area(true, true, true,
                    "interrupted exception placing MDS in queue : "+ iex.toString());
        }
    }

    public boolean isQueueEmpty() {
        return mdsQueue.isEmpty();
    }

    public MetaDataStore getFirstMDS() {
        // should we use takeFirst()?  This will wait till mds available, while poll returns null
        // poll takes and removes
        return mdsQueue.pollFirst();
    }

    public MetaDataStore getLastMDS() {
        // should we use takeLast()?  This will wait till mds available, while poll returns null
        // poll takes and removes
        return mdsQueue.pollLast();
    }

    public MetaDataStore getFirstMDSByParam(MDSParameters mdsp) throws InvalidParameterException {
        return traverseQueue(mdsp, true);
    }

    public MetaDataStore getLastMDSByParam(MDSParameters mdsp) throws InvalidParameterException {
        return traverseQueue(mdsp, false);
    }

    public String getFirstFilenameByParam(MDSParameters mdsp) throws InvalidParameterException {
        MetaDataStore m = traverseQueue(mdsp, true);
        return m.getFilepath();
    }

    public String getLastFilenameByParam(MDSParameters mdsp) throws InvalidParameterException {
        MetaDataStore m = traverseQueue(mdsp, false);
        return m.getFilepath();
    }

    public void removeFirstMDSByParam(MDSParameters mdsp) throws InvalidParameterException {
        traverseQueueAndRemove(mdsp, true);
    }

    public void removeLastMDSByParam(MDSParameters mdsp) throws InvalidParameterException {
        traverseQueueAndRemove(mdsp, false);
    }

    /**
     * iterates forward or backwards through the mdsQueue to find the next MDS containing requested parameters
     * @param mdsp_ : MDSParameters object
     * @param forward : to traverse forward or backwards
     * @return : an MDS object
     * @throws InvalidParameterException : commented out for now
     */
    private static MetaDataStore traverseQueue(MDSParameters mdsp_, boolean forward) throws InvalidParameterException{
        Iterator<MetaDataStore> itr;
        if(forward) {
            itr = mdsQueue.iterator();
        } else {
            itr = mdsQueue.descendingIterator();
        }
        ArrayList<MDSParamObject> params = mdsp_.getParams();
        while(itr.hasNext()) {
            MetaDataStore temp = itr.next();
            for(MDSParamObject s : params) {
                switch(s.getLabel()){
                    case "TIME":
                        if(temp.getTime() == s.getInt()) {return temp;}
                        break;
                    case "POSITION":
                        if(temp.getPosition() == s.getInt()) {return temp;}
                        break;
                    case "Z":
                        if(temp.getZ() == s.getInt()) {return temp;}
                        break;
                    case "CHANNEL":
                        if(temp.getChannel() == s.getInt()) {return temp;}
                        break;
                    case "CHANNELNAME":
                        if(temp.getChannelName().equals(s.getStr())) {return temp;}
                        break;
                }
            }
        }
//        throw new InvalidParameterException("mdsQueue has no element matching that parameter");
        return null;
    }

    private static MetaDataStore traverseQueueAndRemove(MDSParameters mdsp_, boolean forward) throws InvalidParameterException{
        Iterator<MetaDataStore> itr;
        if(forward) {
            itr = mdsQueue.iterator();
        } else {
            itr = mdsQueue.descendingIterator();
        }
        ArrayList<MDSParamObject> params = mdsp_.getParams();
        while(itr.hasNext()) {
            MetaDataStore temp = itr.next();
            for(MDSParamObject s : params) {
                switch(s.getLabel()){
                    case "TIME":
                        if(temp.getTime() == s.getInt()) {mdsQueue.remove(temp);}
                        break;
                    case "POSITION":
                        if(temp.getPosition() == s.getInt()) {mdsQueue.remove(temp);}
                        break;
                    case "Z":
                        if(temp.getZ() == s.getInt()) {mdsQueue.remove(temp);}
                        break;
                    case "CHANNEL":
                        if(temp.getChannel() == s.getInt()) {mdsQueue.remove(temp);}
                        break;
                    case "CHANNELNAME":
                        if(temp.getChannelName().equals(s.getStr())) {mdsQueue.remove(temp);}
                        break;
                }
            }
        }
//        throw new InvalidParameterException("mdsQueue has no element matching that parameter");
        return null;
    }

}
