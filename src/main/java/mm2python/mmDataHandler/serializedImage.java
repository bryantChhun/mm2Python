/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mm2python.mmDataHandler;

import java.io.ByteArrayOutputStream;
import java.io.ObjectOutputStream;
import java.nio.ByteBuffer;
import java.util.ArrayDeque;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import org.micromanager.Studio;
import org.micromanager.data.Coords;
import org.micromanager.data.DataManager;
import org.micromanager.data.Datastore;
import org.micromanager.data.Image;
import org.micromanager.display.DisplayManager;

/**
 *
 * @author bryant.chhun
 */


/**
 * Class that provides access to Micromanager images
 * @author bryant.chhun
 */
public class serializedImage {

    private Studio mm;
    private DisplayManager mmDisplayManager;
    private DataManager mmDataManager;
    private Datastore tempStore;
    Coords.CoordsBuilder builder;
    
    List<Datastore> stores;
    ArrayDeque<Datastore> storesArray;
    ArrayDeque<Object> storeImages = new ArrayDeque<>();
  
    private ObjectOutputStream oos;
    private ByteArrayOutputStream bos;
    private byte[] data;
    private Byte[] Data;
    ArrayDeque<byte[]> storeOfByteArrays = new ArrayDeque<>();
    ArrayDeque<ByteBuffer> storeOfByteBuffers = new ArrayDeque<>();
    ArrayDeque<Byte[]> storeOfByteObjects = new ArrayDeque<>();
    
    
    public serializedImage(Studio mm_) {
        mm = mm_;
        mmDisplayManager = mm.getDisplayManager();
        mmDataManager = mm.getDataManager();
        builder = mm.data().getCoordsBuilder();
        bos = new ByteArrayOutputStream();
        try {
            oos = new ObjectOutputStream(bos);
        } catch (Exception ex) {
            System.out.println(ex);
        }
    }
    
    public void startSerialConnection() {
        bos = new ByteArrayOutputStream();
        try {
            oos = new ObjectOutputStream(bos);
        } catch (Exception ex) {
            System.out.println(ex);
        }
    }
    
    private Image returnRandomImage(Datastore store) {
        int randomNum = ThreadLocalRandom.current().nextInt(0, 20);
        //Coords coords = builder.time(randomNum).build();
        Coords coords = store.getMaxIndices().copy().channel(0).time(randomNum).build();
        Image tempImg = store.getImage(coords);
        if(tempImg == null) {System.out.println("temp img is null");}
        return tempImg;
    }
    
    /**
     * uses display manager's "managed" feature to find new datastores
     *  new datastores must be registered with "manage" method to be found.
     * @return Object reference to raw pixels (copied)
     */
    public Object getCurrentImage() {
        stores = mmDisplayManager.getManagedDatastores();
        tempStore = stores.get(0);
        Image tempImg = tempStore.getAnyImage();
        Object imagecopy1 = tempImg.getRawPixelsCopy();
        return imagecopy1;
    }
    
    public ByteBuffer getCurrentImageByteBuffer() throws Exception {
        stores = mmDisplayManager.getManagedDatastores();
        tempStore = stores.get(0);
        Image tempImg_ = returnRandomImage(tempStore);
        bos.reset();
        oos.writeObject(tempImg_.getRawPixels());
        oos.flush();
        data = bos.toByteArray();
        ByteBuffer buf = ByteBuffer.wrap(data);
        return buf;
    }
    
    //================================================================
    //====================== ArrayDeque popuation methods ============
   
    /**
     * An arrayDeque of arbitrary # Image objects from one datastore
     * @return ArrayDeque of images objects
     */
    public ArrayDeque<Object> getArrayOfOneStore() {
        stores = mmDisplayManager.getManagedDatastores();
        //System.out.println("size of store = "+stores.size());
        for(int i = 0; i < 1000; i++){
            tempStore = stores.get(0);
            //Image tempImg_ = tempStore.getAnyImage();
            Image tempImg_ = returnRandomImage(tempStore);
            storeImages.add(tempImg_.getRawPixelsCopy());
            //storeImages.add(tempImg_.getRawPixels());
            //System.out.println("size of storeImage arraydeque = "+storeImages.size());
        }
        return storeImages;
    }
    
    /**
     * ArrayDeque that serializes data before writing to deque
     * This saves the potential overhead cost of serializing at each python call.
     * @return 
     */
    public ArrayDeque<byte[]> getSerializedByteArrays() {
        stores = mmDisplayManager.getManagedDatastores();
        for(int i = 0; i < 100; i++){
            tempStore = stores.get(0);
            Image tempImg_ = returnRandomImage(tempStore);
            bos.reset();
            try {
                oos.writeObject(tempImg_.getRawPixelsCopy());
                oos.flush();
            } catch (Exception ex) {
                System.out.println(ex);
            }
            data = bos.toByteArray();
            
            storeOfByteArrays.add(data);
        }
        return storeOfByteArrays;
    }
    
    /**
     * ArrayDeque that serializes data before writing to deque
     * ArrayDeque stores bytebuffers?
     * @return 
     */

    public ArrayDeque<ByteBuffer> getSerializedByteBuffer() throws Exception {
        stores = mmDisplayManager.getManagedDatastores();
        for(int i = 0; i < 1000; i++){
            tempStore = stores.get(0);
            Image tempImg_ = returnRandomImage(tempStore);
            bos.reset();
            oos.writeObject(tempImg_.getRawPixelsCopy());
            oos.flush();
            data = bos.toByteArray();
            ByteBuffer buf = ByteBuffer.wrap(data);
            storeOfByteBuffers.add(buf);
        }
        return storeOfByteBuffers;
    }
    
}
