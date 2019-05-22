package mm2python.mmDataHandler;

/**
 *
 * @author bryant.chhun
 */
public interface DataPathInterface {

    // methods to retrieve from queue
    //  these call PathQueue
    //  return a single string
    // get last file by channel_name
    public String getLastFileByChannelName(String channelName) throws IllegalAccessException;

    // get last file by channel
    public String getLastFileByChannelIndex(int channelIndex) throws IllegalAccessException;

    // get last file by z
    public String getLastFileByZ(int z) throws IllegalAccessException;

    // get last file by p
    public String getLastFileByPosition(int pos) throws IllegalAccessException;

    // get last file by t
    public String getLastFileByTime(int time) throws IllegalAccessException;

    // get first file by channel_name
    public String getFirstFileByChannelName(String channelName) throws IllegalAccessException;

    // get first file by channel
    public String getFirstFileByChannelIndex(int channelIndex) throws IllegalAccessException;

    // get first file by z
    public String getFirstFileByZ(int z) throws IllegalAccessException;

    // get first file by p
    public String getFirstFileByPosition(int pos) throws IllegalAccessException;

    // get first file by t
    public String getFirstFileByTime(int time) throws IllegalAccessException;


}
