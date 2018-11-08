/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mmDataHandler;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.ObjectOutputStream;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.MappedByteBuffer;
import java.nio.ShortBuffer;
import java.nio.channels.FileChannel;
import Constants.constants;
import mmDataHandler.Exceptions.NoImageException;
import org.micromanager.data.Coords;
import org.micromanager.data.Image;

/**
 *
 * @author bryant.chhun
 */
public class memMapImage {
    private final Image temp_img;
    private final String filename;
    private final Coords coord;
    
    public memMapImage(Image temp_img_, Coords coord_, String filename_) throws NoImageException {
        temp_img = temp_img_;
        filename = filename_;
        coord = coord_;
        System.out.println("memMapImage constructor filename = "+filename);
    }
    
    public boolean writeToMemMap() {
        byte[] byteimg= null;
                
        File file = new File(filename);
        file.delete();
        System.out.println("writeToMemMap filename = "+filename);

        // write data as memmap to memmap file
        try (   FileChannel fileChannel = new RandomAccessFile(file, "rw").getChannel())
        {
            byteimg = convertToByte(temp_img);
            //byte[] byteimg = convertToByteViaOutputStream(temp_img);
            if(byteimg == null) {
                throw new NoImageException("image not converted to byte[]");
            }
            MappedByteBuffer buffer = fileChannel.map(FileChannel.MapMode.READ_WRITE, 0, byteimg.length);
            buffer.put(byteimg);
        } catch (Exception ex) {
            System.out.println(ex);
        }
        
        // write data record to class constants.
        // This is what Py4J uses to fetch the data from RAMdisk.
        try {
            constants.LBQ_data_queue.put(filename);
            //constants.writeCoordToHashMap(coord, filename);
            //constants.data_list.add(filename);
            if(byteimg != null) {constants.LBQ_metadata_queue.put(""+byteimg.length);}
            return true;
        } catch (NullPointerException ex) {
            System.out.println("null ptr exception in lbq data queue");
        } catch (InterruptedException ex) {
            System.out.println("interrupted exception: queue interrupted while waiting for put");
        } catch (Exception ex) {
            System.out.println(ex);
        }
        return false;
    }
    
    private byte[] convertToByte(Image tempImg_) {
        try
        {
            byte[] bytes;
            Object pixels = tempImg_.getRawPixels();
            if (pixels instanceof byte[]) {
                bytes = (byte[]) pixels;
            }
            else if (pixels instanceof short[]) {
                ShortBuffer shortPixels = ShortBuffer.wrap((short[]) pixels);
                ByteBuffer dest = ByteBuffer.allocate(2 * ((short[]) pixels).length).order(ByteOrder.nativeOrder());
                ShortBuffer shortDest = dest.asShortBuffer();
                shortDest.put(shortPixels);
                bytes = dest.array();
            }
            else {
                throw new UnsupportedOperationException("Unsupported pixel type");
            }
            return bytes;
            
        } catch (Exception ex) {
            System.out.println(ex);
        }
        return null;
    }
    
    private byte[] convertToByteViaOutputStream(Image tempImg_) {
        try (   ByteArrayOutputStream bos = new ByteArrayOutputStream();
                ObjectOutputStream oos = new ObjectOutputStream(bos);
                )
        {
            byte[] bytes;
            bos.reset();
            oos.writeObject(tempImg_.getRawPixels());
            oos.flush();
            bytes = bos.toByteArray();
            return bytes;
        } catch (Exception ex) {
            System.out.println(ex);
        }
        return null;
    }
}


    