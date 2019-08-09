package mm2python.DataStructures.Builders;

import mm2python.DataStructures.MetaDataStore;

/**
 * Builder base
 *  Build parameters needed for either
 *      BuildMDS or
 *      BuildMDSParams (for searching MDS by parameters)
 */
abstract public class MDSBuilderBase {

    Integer time, pos, z, channel;

    Integer xRange, yRange, bitDepth;

    String prefix, windowname, channel_name;

    String filepath;

    Integer buffer_position;

    public MDSBuilderBase time(int t) {
        this.time = t;
        return this;
    }

    public MDSBuilderBase position(int pos) {
        this.pos = pos;
        return this;
    }

    public MDSBuilderBase z(int z_) {
        this.z = z_;
        return this;
    }

    public MDSBuilderBase channel(int c) {
        this.channel = c;
        return this;
    }

    public MDSBuilderBase xRange(int xr) {
        this.xRange = xr;
        return this;
    }

    public MDSBuilderBase yRange(int yr) {
        this.yRange = yr;
        return this;
    }

    public MDSBuilderBase bitDepth(int bd) {
        this.bitDepth = bd;
        return this;
    }

    public MDSBuilderBase prefix(String pfx) {
        this.prefix = pfx;
        return this;
    }

    public MDSBuilderBase windowname(String wname_) {
        this.windowname = wname_;
        return this;
    }

    public MDSBuilderBase channel_name(String channel_name) {
        this.channel_name = channel_name;
        return this;
    }

    public MDSBuilderBase filepath(String fp) {
        this.filepath = fp;
        return this;
    }

    public MDSBuilderBase buffer_position(Integer buf_pos) {
        this.buffer_position = buf_pos;
        return this;
    }

    public MetaDataStore buildMDS() throws IllegalAccessException {
        throw new IllegalAccessException("buildMDS not allowed from ParamBuilder");
    }

    public MDSParameters buildMDSParams() throws IllegalAccessException{
        throw new IllegalAccessException("buildMDSParams not allowed from MDSBuilder");
    }

}
