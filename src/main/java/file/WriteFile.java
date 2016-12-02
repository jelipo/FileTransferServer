package file;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.RandomAccessFile;
import java.util.RandomAccess;

/**
 * Created by 10441 on 2016/12/2.
 */
public class WriteFile {

    public static void writeByte(File file,byte[] data,long start){
        try {
            RandomAccessFile randomAccessFile=new RandomAccessFile(file.getAbsoluteFile(),"rw");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }
}
