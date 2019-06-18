package mm2python.DataStructures.Builders;

import mm2python.DataStructures.MetaDataStore;

/**
 * Builder to create MetaDataStores (MDS)
 */
public class MDSBuilder extends MDSBuilderBase {

    public MDSBuilder(){
    }

    public MetaDataStore buildMDS() {
        return new MetaDataStore(z, pos, time, channel,
                xRange, yRange, bitDepth,
                channel_name, prefix, windowname,
                filepath);
    }

    /**
     * to prevent accidental construction of MDSParams from MDS
     * @return : none
     * @throws IllegalAccessException :
     */
    public MDSParameters buildMDSParams() throws IllegalAccessException{
        throw new IllegalAccessException("buildMDSParams not allowed from ParamBuilder");
    }

}

