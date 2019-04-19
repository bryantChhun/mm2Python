package mm2python.DataStructures;

import java.util.Objects;

/**
 * Object that stores meta data for every image
 *  not all data is required for construction
 */
public class MetaDataStore {

    private final int time, stage, z, channel;

    private final int xRange, yRange, bitDepth;

    private final String prefix, windowname;

    private final String channel_name;

    public MetaDataStore(String prefix_, String windowname_, int time_, int stage_,
                  int z_, int channel_, int x_dim_, int y_dim_, int bytesPerPixel, String channel_name_) {
        prefix = prefix_;
        windowname = windowname_;
        time = time_;
        stage = stage_;
        z = z_;
        channel = channel_;
        xRange = x_dim_;
        yRange = y_dim_;
        bitDepth = bytesPerPixel;
        channel_name = channel_name_;
    }

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
                stage == that.stage &&
                z == that.z &&
                channel == that.channel &&
                Objects.equals(prefix, that.prefix) &&
                Objects.equals(windowname, that.windowname);
    }

    /**
     * Must override hashCode for proper hash map population
     * @return hash for this object, based purely on the 6 constructor parameters:
     *  time, x-y position, z-position, channel, filename prefix, windowname
     */
    @Override
    public int hashCode() {
        return Objects.hash(time, stage, z, channel, prefix, windowname);
    }



}

