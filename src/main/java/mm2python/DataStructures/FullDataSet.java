package mm2python.DataStructures;

import mm2python.UI.reporter;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.concurrent.ConcurrentHashMap;

public class FullDataSet {

    private static final ConcurrentHashMap<MetaDataStore, MetaDataStore> allData
            = new ConcurrentHashMap<>(100, 0.75f, 30);

    public ConcurrentHashMap<MetaDataStore, MetaDataStore> getAllData() {
        return allData;
    }

    /***
     * Implementation of Data record using Concurrent HashMap
     *  image coordinates are stored in a MetaDataStore object.
     *  This MDS object is hashed on unique z, p, t, c constructor params
     *  Additional metadata is passed into constructor, but not used for hash
     *  Behavior is like a set, but allows retrieval of the object
     * @param m : MetaDataStore object
     */
    public void putDataToSet(MetaDataStore m) {
        try {
            allData.put(m, m);
        } catch(NullPointerException e) {
            reporter.set_report_area(false, false, "null MDS parameter "+e.toString());
        }
    }

    /***
     * return MDS value matching MDS key
     *  returned MDS contains additional metadata from acquisition time
     * @param m : MDS is hashed only on Z, P, T, C
     * @return : an MDS
     */
    public MetaDataStore getDataFromSet(MetaDataStore m) {
        return allData.getOrDefault(m, null);
    }

    /***
     * return all MDS that contain matching C
     * @param c : channel index
     * @return : list of MDS
     */
    public ArrayList<MetaDataStore> getDataFromSetByC(int c) {
        Iterator<MetaDataStore> itr = allData.keySet().iterator();
        ArrayList<MetaDataStore> mds = new ArrayList<>();
        while(itr.hasNext()) {
            MetaDataStore temp = allData.get(itr.next());
            if(temp.getChannel() == c) {
                mds.add(temp);
            }
        }
        return mds;
    }

    /***
     * return all MDS that contain matching P and C
     * @param p : position index
     * @param c : channel index
     * @return : list of MDS
     */
    public ArrayList<MetaDataStore> getDataFromSetByP(int p, int c) {
        Iterator<MetaDataStore> itr = allData.keySet().iterator();
        ArrayList<MetaDataStore> mds = new ArrayList<>();
        while(itr.hasNext()) {
            MetaDataStore temp = allData.get(itr.next());
            if(temp.getP() == p && temp.getChannel() == c) {
                mds.add(temp);
            }
        }
        return mds;
    }

    /***
     * return all MDS that contain matching Z and C
     * @param t : single time point
     * @param c : channel index
     * @return : list of MDS
     */
    public ArrayList<MetaDataStore> getDataFromSetByT(int t, int c) {
        Iterator<MetaDataStore> itr = allData.keySet().iterator();
        ArrayList<MetaDataStore> mds = new ArrayList<>();
        while(itr.hasNext()) {
            MetaDataStore temp = allData.get(itr.next());
            if(temp.getTime() == t && temp.getChannel() == c) {
                mds.add(temp);
            }
        }
        return mds;
    }

}
