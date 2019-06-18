package mm2python.DataStructures.Queues;

import mm2python.DataStructures.Builders.MDSParamObject;
import mm2python.DataStructures.Builders.MDSParameters;
import mm2python.DataStructures.MetaDataStore;
import mm2python.UI.reporter;

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

    public void resetQueue() {
        mdsQueue.clear();
    }

    // methods to add/remove MetaDataStores from lbd
    public void putMDS(MetaDataStore mds_) {
        try {
            boolean result = mdsQueue.offer(mds_, 20, TimeUnit.MILLISECONDS);
            if(!result){
                reporter.set_report_area(true, true, true, "failure to offer MDS into MDSQueue: "+mds_.toString());
            }
        } catch (InterruptedException iex) {
            reporter.set_report_area(true, true, true,
                    "interrupted exception placing MDS in queue : "+ iex.toString());
        }
    }

    public boolean isMDSQueueEmpty() {
        return mdsQueue.isEmpty();
    }

    public MetaDataStore getFirstMDS() {
        return mdsQueue.pollFirst();
    }

    public MetaDataStore getLastMDS() {
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
        throw new InvalidParameterException("mdsQueue has no element matching that parameter");
    }

}
