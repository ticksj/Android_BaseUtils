package com.sj.android_base_utils.utils_media.utils;

import android.util.Log;

import java.io.FileInputStream;
import java.io.FileOutputStream;

/**
 * Created by SJ on 2018/5/7.
 */

public class FileUtils {

    /**
     * 将pcm转化为opus文件
     * @param inFileName
     * @param outFileName
     */
    public static void pcm2opus(String inFileName, String outFileName) {

        FileInputStream rawFileInputStream = null;
        FileOutputStream fileOutputStream = null;
        try {
            rawFileInputStream = new FileInputStream(inFileName);
            fileOutputStream = new FileOutputStream(outFileName);
            byte[] rawbyte = new byte[320];
            byte[] encoded = new byte[160];
            int readedtotal = 0;
            int size = 0;
            int encodedtotal = 0;
            OpusJni instance = OpusJni.getInstance();
            instance.Opusopen(8);
            while ((size = rawFileInputStream.read(rawbyte, 0, 320)) != -1) {
                readedtotal = readedtotal + size;
                short[] rawdata = ShortByteUtil.byteArray2ShortArray(rawbyte);
                int encodesize = instance.Opusencode(rawdata, 0, encoded, rawdata.length);
                fileOutputStream.write(encoded, 0, encodesize);
                encodedtotal = encodedtotal + encodesize;
            }
            fileOutputStream.close();
            rawFileInputStream.close();
        } catch (Exception e) {
            Log.e("TAG", "pcm2opus: "+e.getMessage() );
            e.printStackTrace();
        }

    }
    /**
     * 将pcm转化为spx文件
     * @param inFileName
     * @param outFileName
     */
    public static void pcm2spx(String inFileName, String outFileName) {

        FileInputStream rawFileInputStream = null;
        FileOutputStream fileOutputStream = null;
        try {
            rawFileInputStream = new FileInputStream(inFileName);
            fileOutputStream = new FileOutputStream(outFileName);
            byte[] rawbyte = new byte[320];
            byte[] encoded = new byte[160];
            //将原数据转换成spx压缩的文件，speex只能编码160字节的数据，需要使用一个循环
            int readedtotal = 0;
            int size = 0;
            int encodedtotal = 0;
            Speex speex = new Speex();
            speex.init();
            while ((size = rawFileInputStream.read(rawbyte, 0, 320)) != -1) {
                readedtotal = readedtotal + size;
                short[] rawdata = ShortByteUtil.byteArray2ShortArray(rawbyte);
                int encodesize = speex.encode(rawdata, 0, encoded, rawdata.length);
                fileOutputStream.write(encoded, 0, encodesize);
                encodedtotal = encodedtotal + encodesize;
            }
            fileOutputStream.close();
            rawFileInputStream.close();
        } catch (Exception e) {

        }

    }

    /**
     * 将opus文件转化为pcm文件
     * @param inFileName
     * @param outFileName
     */
    public static void opus2pcm(String inFileName, String outFileName) {
        FileInputStream inAccessFile = null;
        FileOutputStream fileOutputStream = null;
        try {
            inAccessFile = new FileInputStream(inFileName);
            fileOutputStream = new FileOutputStream(outFileName);
            byte[] inbyte = new byte[20];
            short[] decoded = new short[160];
            int readsize = 0;
            int readedtotal = 0;
            int decsize = 0;
            int decodetotal = 0;
            OpusJni instance = OpusJni.getInstance();
            instance.Opusopen(8);
            while ((readsize = inAccessFile.read(inbyte, 0, 20)) != -1) {
                readedtotal = readedtotal + readsize;
                decsize = instance.Opusdecode(inbyte, decoded, readsize);
                fileOutputStream.write(ShortByteUtil.shortArray2ByteArray(decoded), 0, decsize*2);
                decodetotal = decodetotal + decsize;
            }
            fileOutputStream.close();
            inAccessFile.close();
        } catch (Exception e) {

        }
    }








    /**
     * 将spx文件转化为pcm文件
     * @param inFileName
     * @param outFileName
     */
    public static void spx2pcm(String inFileName, String outFileName) {
        FileInputStream inAccessFile = null;
        FileOutputStream fileOutputStream = null;
        try {
            inAccessFile = new FileInputStream(inFileName);
            fileOutputStream = new FileOutputStream(outFileName);
            byte[] inbyte = new byte[20];
            short[] decoded = new short[160];
            int readsize = 0;
            int readedtotal = 0;
            int decsize = 0;
            int decodetotal = 0;
            Speex speex = new Speex();
            speex.init();
            while ((readsize = inAccessFile.read(inbyte, 0, 20)) != -1) {
                readedtotal = readedtotal + readsize;
                decsize = speex.decode(inbyte, decoded, readsize);
                fileOutputStream.write(ShortByteUtil.shortArray2ByteArray(decoded), 0, decsize*2);
                decodetotal = decodetotal + decsize;
            }
            fileOutputStream.close();
            inAccessFile.close();
        } catch (Exception e) {

        }
    }





}
