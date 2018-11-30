package mmDataHandler;

import java.util.Objects;

/**
 * Object that stores meta data for every image
 *  not all data is required for construction
 */
public class MetaDataStore {

    private final int time, stage, z, channel;

    public final int x_dim, y_dim, bitdepth;

    private final String prefix, windowname;

    public final String channel_name;

    public MetaDataStore(String prefix_, String windowname_, int time_, int stage_,
                  int z_, int channel_, int x_dim_, int y_dim_, int bytesPerPixel, String channel_name_) {
        prefix = prefix_;
        windowname = windowname_;
        time = time_;
        stage = stage_;
        z = z_;
        channel = channel_;
        x_dim = x_dim_;
        y_dim = y_dim_;
        bitdepth = bytesPerPixel;
        channel_name = channel_name_;
    }

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

    @Override
    public int hashCode() {
        return Objects.hash(time, stage, z, channel, prefix, windowname);
    }

}

