package mm2python.DataStructures.Builders;

import mm2python.DataStructures.MetaDataStore;

public class MDSBuilder extends MDSBuilderBase {

    public MDSBuilder(){
    }

    public MetaDataStore buildMDS() {
        return new MetaDataStore(z, pos, time, channel,
                xRange, yRange, bitDepth,
                channel_name, prefix, windowname,
                filepath);
    }

    public MDSParameters buildMDSParams() throws IllegalAccessException{
        throw new IllegalAccessException("buildMDSParams not allowed from ParamBuilder");
    }

}

