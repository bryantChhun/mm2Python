package org.mm2python.DataStructures.Maps;

import org.mm2python.DataStructures.Builders.MDSParamObject;
import org.mm2python.DataStructures.Builders.MDSParameters;
import org.mm2python.DataStructures.MetaDataStore;
import org.mm2python.UI.reporter;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

/***
 * Methods to place and retrieve MetaDataStores in a HashMap
 *  Backed by a concurrent HashMap because all data writes are threaded
 *
 *  @author bryant.chhun
 */
public class MDSMap {

    private static final ConcurrentHashMap<MetaDataStore, MetaDataStore> allData
            = new ConcurrentHashMap<>(100, 0.75f, 30);

    public static void clearMap() {
        allData.clear();
    }

    public static void clearData() {
        if(!allData.isEmpty()){
            allData.clear();
        }
    }

    public static int getSize() {
        return allData.size();
    }

    public static boolean isEmpty() {
        return allData.isEmpty();
    }

    /***
     * Implementation of Data record using Concurrent HashMap
     *  image coordinates are stored in a MetaDataStore object.
     *  This MDS object is hashed on unique z, p, t, c constructor params
     *  Additional metadata is passed into constructor, but not used for hash
     *  Behavior is like a set, but allows retrieval of the object
     * @param m : MetaDataStore object
     */
    public void putMDS(MetaDataStore m) {
        try {
            allData.put(m, m);
        } catch(NullPointerException e) {
            System.out.println("null MDS parameter" + e.toString());
            reporter.set_report_area(true, true, true, "null MDS parameter "+e.toString());
        }
    }

    /***
     * return MDS value matching MDS key
     *  returned MDS contains additional metadata from acquisition time
     *  MDS must match based on hashed values
     * @param m : MDS is hashed only on Z, P, T, C
     * @return : an MDS
     */
    public MetaDataStore getMDS(MetaDataStore m) {
        return allData.getOrDefault(m, null);
    }

    /**
     * method to retrieve any subset of data that matches the supplied parameters
     *  Time complexity is O(n*m)
     *      where n is number of elements in concurrentHashMap
     *      where m is max number of params to check against (currently m=11)
     *
     * @param mp : MDSParameter object
     * @return : an arraylist of all MDS that match the parameters
     */
    public ArrayList<MetaDataStore> getMDSByParams(MDSParameters mp) {
        List<Boolean> boolList = new ArrayList<>();
        ArrayList<MDSParamObject> params = mp.getParams();

        // iterate through concurrent HashMap allData
        Iterator<MetaDataStore> itr = allData.keySet().iterator();
        ArrayList<MetaDataStore> mds = new ArrayList<>();
        while(itr.hasNext()) {
            //temp contains full metadata
            MetaDataStore temp = allData.get(itr.next());

            // populate a list of Boolean upon matching parameters
            // iterates for only exactly the defined parameters in MDSParameters
            for(MDSParamObject s : params) {
                switch(s.getLabel()){
                    case "TIME":
                        if(temp.getTime() == s.getInt()) {boolList.add(true);}
                        else {boolList.add(false);}
                        break;
                    case "POSITION":
                        if(temp.getPosition() == s.getInt()) {boolList.add(true);}
                        else {boolList.add(false);}
                        break;
                    case "Z":
                        if(temp.getZ() == s.getInt()) {boolList.add(true);}
                        else {boolList.add(false);}
                        break;
                    case "CHANNEL":
                        if(temp.getChannel() == s.getInt()) {boolList.add(true);}
                        else {boolList.add(false);}
                        break;
                    case "CHANNELNAME":
                        if(temp.getChannelName().equals(s.getStr())) {boolList.add(true);}
                        else {boolList.add(false);}
                        break;
                }
            }
            // if all parameters match, add this MDS to result
            if(areAllTrue(boolList)) { mds.add(temp);}
            boolList.clear();
        }
        return mds;
    }


    /**
     * Same as above but returns an ArrayList of Strings (filenames)
     *
     * @param mp : MDSParameter object
     * @return : an arraylist of all STRING that match the parameters
     */
    public ArrayList<String> getFilenamesByParams(MDSParameters mp) {
        List<Boolean> boolList = new ArrayList<>();
        ArrayList<MDSParamObject> params = mp.getParams();

        // iterate through concurrent HashMap allData
        Iterator<MetaDataStore> itr = allData.keySet().iterator();
        ArrayList<String> mds = new ArrayList<>();
        while(itr.hasNext()) {
            //temp contains full metadata
            MetaDataStore temp = allData.get(itr.next());

            // populate a list of Boolean upon matching parameters
            // iterates for only exactly the defined parameters in MDSParameters
            for(MDSParamObject s : params) {
                switch(s.getLabel()){
                    case "TIME":
                        if(temp.getTime() == s.getInt()) {boolList.add(true);}
                        else {boolList.add(false);}
                        break;
                    case "POSITION":
                        if(temp.getPosition() == s.getInt()) {boolList.add(true);}
                        else {boolList.add(false);}
                        break;
                    case "Z":
                        if(temp.getZ() == s.getInt()) {boolList.add(true);}
                        else {boolList.add(false);}
                        break;
                    case "CHANNEL":
                        if(temp.getChannel() == s.getInt()) {boolList.add(true);}
                        else {boolList.add(false);}
                        break;
                    case "CHANNELNAME":
                        if(temp.getChannelName().equals(s.getStr())) {boolList.add(true);}
                        else {boolList.add(false);}
                        break;
                }
            }
            // if all parameters match, add this MDS to result
            if(areAllTrue(boolList)) { mds.add(temp.getFilepath());}
            boolList.clear();
        }
        return mds;
    }

    private boolean areAllTrue(List<Boolean> array){
        for(boolean b : array) if(!b) return false;
        return true;
    }

}
