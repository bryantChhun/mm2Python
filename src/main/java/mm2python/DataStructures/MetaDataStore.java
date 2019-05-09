package mm2python.DataStructures;

import java.util.Objects;

/**
 * Object that stores meta data for every image
 *  not all data is required for construction
 */
public class MetaDataStore {

    private final int time, pos, z, channel;

    private final int xRange, yRange, bitDepth;

    private final String prefix, windowname, channel_name;

    private final String filepath;

    public MetaDataStore(int z_, int pos_, int time_, int channel_,
                         int x_dim_, int y_dim_, int bytesPerPixel,
                         String channel_name_, String prefix_, String windowname_,
                         String filepath_) {
        time = time_;
        pos = pos_;
        z = z_;
        channel = channel_;
        xRange = x_dim_;
        yRange = y_dim_;
        bitDepth = bytesPerPixel;
        channel_name = channel_name_;
        prefix = prefix_;
        windowname = windowname_;
        filepath = filepath_;

    }

    // overload: z, p, t, c, x-dim, y-dim, bytes
    public MetaDataStore(int z_, int pos_, int time_, int channel_,
                         int x_dim_, int y_dim_, int bytesPerPixel_,
                         String filepath_)
    {
        this(z_, pos_, time_, channel_,
                x_dim_, y_dim_, bytesPerPixel_,
                null, null, null, filepath_);
    }

    // overload: z, c
    public MetaDataStore(int z_, int channel_, String filepath_)
    {
        this(z_, 0, 0, channel_,
                0, 0, 0,
                null, null, null, filepath_);
    }

    // overload: z, p, c
    public MetaDataStore(int z_, int pos_, int channel_, String filepath_)
    {
        this(z_, pos_, 0, channel_,
                0, 0, 0,
                null, null, null, filepath_);
    }

    // overload: z, p, t, c
    public MetaDataStore(int z_, int pos_, int time_, int channel_, String filepath_)
    {
        this(z_, pos_, time_, channel_,
                0, 0, 0,
                null, null, null, filepath_);
    }

    public int getZ() { return z;}

    public int getP() { return pos;}

    public int getTime() { return time;}

    public int getChannel() { return channel;}

    public int getxRange() {
        return xRange;
    }

    public int getyRange() {
        return yRange;
    }

    public int getBitDepth() {
        return bitDepth;
    }

    public String getChannelName() {
        return channel_name;
    }

    public String getPrefix() { return prefix;}

    public String getWindowName() { return windowname;}

    public String getFilepath() { return filepath;}


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
                channel == that.channel; //&&
//                Objects.equals(prefix, that.prefix) &&
//                Objects.equals(windowname, that.windowname);
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

