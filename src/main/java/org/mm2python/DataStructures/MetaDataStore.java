package org.mm2python.DataStructures;

import java.util.Objects;

/**
 * Object that stores meta data for every image
 *  uses a MDSBuilder to construct.
 *
 * Is Hashed on ONLY Z, Position, Time, and Channel
 *  Any stores with duplicate coordinates will be overwritten in hashmaps.
 */
public class MetaDataStore {

    private Integer time, pos, z, channel;

    private Integer xRange, yRange, bitDepth;

    private String prefix, windowname, channel_name;

    private String filepath;

    private Integer buffer_position;

    private Object image;

    // package private constructor
    public MetaDataStore(Integer z_, Integer pos_, Integer time_, Integer channel_,
                         Integer x_dim_, Integer y_dim_, Integer bitDepth_,
                         String channel_name_, String prefix_, String windowname_,
                         String filepath_, Integer buffer_position_, Object image_)
    {
        // positivity check
        if(
                (z_!=null && z_<0) ||
                (pos_!=null && pos_<0) ||
                (time_!=null && time_<0) ||
                (channel_!=null && channel_<0) ||
                (x_dim_!=null && x_dim_<0) ||
                (y_dim_!=null && y_dim_<0) ||
                (bitDepth_!=null && bitDepth_ <0) ||
                (buffer_position_!=null && buffer_position_ < 0)
                )
        {
            throw new IllegalArgumentException("MDS parameter must be positive and not null");
        }

        // if not null, assign value
        if(time_!=null) {this.time = time_;}
        if(pos_!=null) {this.pos = pos_;}
        if(z_!=null) {this.z = z_;}
        if(channel_!=null) {this.channel = channel_;}
        if(x_dim_!=null) {this.xRange = x_dim_;}
        if(y_dim_!=null) {this.yRange = y_dim_;}
        if(bitDepth_!=null) {this.bitDepth = bitDepth_;}
        if(channel_name_!=null) {this.channel_name = channel_name_;}
        if(prefix_!=null) {this.prefix = prefix_;}
        if(windowname_!=null) {this.windowname = windowname_;}
        if(filepath_!=null) {this.filepath = filepath_;}
        if(buffer_position_!=null) {this.buffer_position = buffer_position_;}
        if(image_!=null) {this.image = image_;}
    }

    public Integer getZ() { return this.z;}

    public Integer getPosition() { return this.pos;}

    public Integer getTime() { return this.time;}

    public Integer getChannel() { return this.channel;}

    public Integer getxRange() {
        return this.xRange;
    }

    public Integer getyRange() {
        return this.yRange;
    }

    public Integer getBitDepth() {
        return this.bitDepth;
    }

    public String getChannelName() {
        return this.channel_name;
    }

    public String getPrefix() { return this.prefix;}

    public String getWindowName() { return this.windowname;}

    public String getFilepath() { return this.filepath;}

    public Integer getBufferPosition() { return this.buffer_position;}

    public Object getImage() {return this.image;}


    /**
     * Must override equals for proper hash map population
     * @param o target MetaDataStore object
     * @return True if each field is equal
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MetaDataStore that = (MetaDataStore) o;
        return time == that.time &&
                pos == that.pos &&
                z == that.z &&
                channel == that.channel;
    }

    /**
     * Must override hashCode for proper hash map population
     * @return hash for this object, based purely on the 6 constructor parameters:
     *  time, x-y position, z-position, channel, filename prefix, windowname
     */
    @Override
    public int hashCode() {
        return Objects.hash(z, pos, time, channel);
    }


}

