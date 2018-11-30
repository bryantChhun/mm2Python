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
import org.micromanager.Studio;
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
    private final String prefix;
    private final String window_name;
    private final String[] channel_names;
    
    public memMapImage(Image temp_img_, Coords coord_, String filename_, String prefix_, String window_name_, String[] channel_names_) {
        temp_img = temp_img_;
        filename = filename_;
        prefix = prefix_;
        coord = coord_;
        window_name = window_name_;
        channel_names = channel_names_;
        System.out.println("memMapImage constructor filename = "+filename);
    }
    
    public boolean writeToMemMap() throws NoImageException {
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
            MetaDataStore meta = new MetaDataStore(prefix, window_name,
                    coord.getTime(),
                    coord.getStagePosition(),
                    coord.getZ(),
                    coord.getChannel(),
                    temp_img.getWidth(),
                    temp_img.getHeight(),
                    temp_img.getBytesPerPixel(),
                    channel_names[coord.getChannel()]
                    );
            System.out.println("writing meta = "+meta.toString());
            System.out.println("writing filename = "+filename);
            System.out.println("writing chanName = "+channel_names[coord.getChannel()]);

            constants.putMetaStoreToFilenameMap(meta, filename);

            constants.putChanToMetaStoreMap(channel_names[coord.getChannel()], meta);

            constants.putChanToFilenameMap(channel_names[coord.getChannel()], filename);
            return true;

        } catch (NullPointerException ex) {
            System.out.println("null ptr exception in lbq data queue");
        } catch (Exception ex) {
            System.out.println(ex);
        }
        return false;
    }
    
    private byte[] convertToByte(Image tempImg_) throws UnsupportedOperationException {
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


    