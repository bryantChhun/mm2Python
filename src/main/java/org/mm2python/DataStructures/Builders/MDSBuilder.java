package org.mm2python.DataStructures.Builders;

import org.mm2python.DataStructures.MetaDataStore;

/**
 * Builder to create MetaDataStores (MDS)
 */
public class MDSBuilder extends MDSBuilderBase {

    public MDSBuilder(){
    }

    /**
     * Implemented from base class
     *
     * @return : MetaDataStore object
     */
    @Override
    public MetaDataStore buildMDS() {
        return new MetaDataStore(z, pos, time, channel,
                xRange, yRange, bitDepth,
                channel_name, prefix, windowname,
                filepath, buffer_position);
    }

    /**
     * to prevent accidental construction of MDSParams from MDS
     * @return : none
     * @throws IllegalAccessException :
     */
    @Override
    public MDSParameters buildMDSParams() throws IllegalAccessException{
        throw new IllegalAccessException("buildMDSParams not allowed from ParamBuilder");
    }

}

