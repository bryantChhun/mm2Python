package org.mm2python.DataStructures.Builders;

import org.mm2python.DataStructures.MetaDataStore;

import java.util.ArrayList;

/**
 * build an arraylist of parameters (String)
 *  parameters are simply T, P, Z, C, C_name
 * used when querying MDSMap or MDSQueue for MetaDataStores
 */
public class MDSParamBuilder extends MDSBuilderBase {

    private ArrayList<MDSParamObject> params;

    public MDSParamBuilder() {
        params = new ArrayList<>();
    }

    public MDSParameters buildMDSParams() {

        if(time!=null) {
            params.add(new MDSParamObject("TIME", time));
        }
        if(pos!=null) {
            params.add(new MDSParamObject("POSITION", pos));
        }
        if(z!=null) {
            params.add(new MDSParamObject("Z", z));
        }
        if(channel!=null) {
            params.add(new MDSParamObject("CHANNEL", channel));
        }

        if(channel_name!=null) {
            params.add(new MDSParamObject("CHANNELNAME", channel_name));
        }

        if(xRange!=null) {
            params.add(new MDSParamObject("XRANGE", xRange));
        }
        if(yRange!=null) {
            params.add(new MDSParamObject("YRANGE", yRange));
        }
        if(bitDepth!=null) {
            params.add(new MDSParamObject("BITDEPTH", bitDepth));
        }

        if(prefix!=null) {
            params.add(new MDSParamObject("PREFIX", prefix));
        }
        if(windowname!=null) {
            params.add(new MDSParamObject("WINDOWNAME", windowname));
        }
        if(filepath!=null) {
            params.add(new MDSParamObject("FILEPATH", filepath));
        }

        return new MDSParameters(params);
    }

    public MetaDataStore buildMDS() throws IllegalAccessException{
        throw new IllegalAccessException("buildMDS not allowed from ParamBuilder");
    }

}
