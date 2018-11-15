package mmDataHandler;

import java.util.Objects;

public class MetaDataStore {

    private final int time, stage, z, channel;

    private final String prefix, windowname, mdaChannel;

    public MetaDataStore(String prefix_, String windowname_, int time_, int stage_,
                  int z_, int channel_, String mdaChannel_) {
        prefix = prefix_;
        windowname = windowname_;
        time = time_;
        stage = stage_;
        z = z_;
        channel = channel_;
        mdaChannel = mdaChannel_;
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
                Objects.equals(windowname, that.windowname) &&
                Objects.equals(mdaChannel, that.mdaChannel);
    }

    @Override
    public int hashCode() {

        return Objects.hash(time, stage, z, channel, prefix, windowname, mdaChannel);
    }

}

