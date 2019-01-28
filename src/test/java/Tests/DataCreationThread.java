package Tests;

public class DataCreationThread implements Runnable {

    private String filename;

    DataCreationThread(String filename) {
        this.filename = filename;
    }

    @Override
    public void run() {

        // write memory mapped image
        try {
            System.out.println("FILENAME = " + filename);
            //memMapImage out = new memMapImage(temp_img, temp_coord, filename, prefix, window_name, channel_names);
            //out.writeToMemMap();
        } catch (NullPointerException ex) {
            System.out.println("null ptr exception in datastoreEvents Thread");
        }
    }

}
