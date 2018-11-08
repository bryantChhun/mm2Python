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
import Exceptions.NoImageException;
import org.micromanager.data.Coords;
import org.micromanager.data.Image;

/**
 *
 * @author bryant.chhun
 */
public class memMapImage {
    private final Image temp_img;
    private final Coords temp_coord;
    private final String prefix;
    private final String filename;

    
    public memMapImage(Image temp_img_, Coords temp_coord_, String prefix_, String filename_) throws NoImageException {
        temp_img = temp_img_;
        temp_coord = temp_coord_;
        prefix = prefix_;
        filename = filename_;
        byte[] byteimg= null;
                
        File file = new File(filename);
        file.delete();

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
        
        try {
            constants.LBQ_data_queue.put(filename);
            if(byteimg != null) {constants.LBQ_metadata_queue.put(""+byteimg.length);}
        } catch (NullPointerException ex) {
            System.out.println("null ptr exception in lbq data queue");
        } catch (InterruptedException ex) {
            System.out.println("interrupted exception: queue interrupted while waiting for put");
        } catch (Exception ex) {
            System.out.println(ex);
        }
        
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


    