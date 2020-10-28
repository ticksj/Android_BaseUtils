package com.sj.android_base_utils.utils_file;

import com.sj.android_base_utils.utils_record.LogUtils;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by SJ on 2020/10/28.
 */
public class FileUtils {

    private void studyFile(String pathname) {
        /*构造器*/
        //File(String pathname)
        //File(String parent,String child)
        //File(File parent,String child)
        File file = new File(pathname);
        /*文件操作功能*/
        try {
            //创建文件，没有就创建-ture，有就不创建-false
            boolean newFile = file.createNewFile();
        } catch (IOException e) {
            //路径异常时报错
            e.printStackTrace();
        }
        //创建文件夹，没有就创建-ture，有就不创建-false
        boolean mkdir = file.mkdir();
        boolean mkdirs = file.mkdirs();//创建本身以及父级
        //更换路径，可以实现重命名与剪切功能
        boolean renameTo = file.renameTo(new File("text.txt"));
        //删除 文件夹必须为空才能删除
        boolean delete = file.delete();


        /*判断功能*/
        boolean directory = file.isDirectory(); // 是否文件夹
        boolean file1 = file.isFile();  // 是否文件
        boolean exists = file.exists(); // 是否存在
        boolean canRead = file.canRead();   // 是否可读  设置是否可读 与系统有关，windows系统认为所有文件都可读
        boolean canWrite = file.canWrite(); // 是否可写
        boolean hidden = file.isHidden();   // 是否隐藏

        /*获取功能*/
        String absolutePath = file.getAbsolutePath();      // 获取绝对路径
        String path = file.getPath();      // 获取构造路径
        String name = file.getName();      // 获取文件名
        long length = file.length();       // 获取长度
        long lastModified = file.lastModified();       // 获取最后修改时间
        String[] list = file.list();       // 获取目录下所有文件或文件夹名称数组
        File[] files = file.listFiles();       // 获取目录下所有文件或文件夹File数组
        File[] filterFiles = file.listFiles(new FilenameFilter() { // 获取目录下过滤后所有文件或文件夹File数组
            @Override
            public boolean accept(File dir, String name) {
                //true 表示通过过滤
                return false;
            }
        });

    }

    public static boolean deleteFile(File file) {
        if (file == null || !file.exists()) {
            log(operationResult(FILE_NULL));
            return false;
        }
        if (file.isFile()) {
            return file.delete();
        }
        if (file.isDirectory()) {
            for (File f : file.listFiles()) {
                return deleteFile(f);
            }
        }
        log(operationResult(FILE_UNKNOWN));
        return false;
    }

    public static List<File> getFileList(File file) {
        List<File> files = new ArrayList<>();
        if (file == null || !file.exists()) {
            log(operationResult(FILE_NULL));
            return files;
        }
        if (file.isFile()) {
            files.add(file);
            return files;
        }
        if (file.isDirectory()) {
            for (File f : file.listFiles()) {
                files.addAll(getFileList(f));
            }
        }

        log(operationResult(FILE_UNKNOWN));
        return files;
    }

    public static List<File> getFileList(File file, String endWith) {
        if (endWith == null || endWith == "") {
            return getFileList(file);
        }
        List<File> files = new ArrayList<>();
        if (file == null || !file.exists()) {
            log(operationResult(FILE_NULL));
            return files;
        }
        if (file.isFile()) {
            if (file.getName().endsWith(endWith)) {
                files.add(file);
            }
            return files;
        }
        if (file.isDirectory()) {
            for (File f : file.listFiles()) {
                files.addAll(getFileList(f, endWith));
            }
        }
        return files;
    }

    private static String operationResult(int result) {
        switch (result) {
            case FILE_NULL:
                return "文件不存在";
            case FILE_TYPE_ERROR:
                return "文件类型错误";
        }
        return "未知错误";
    }

    private static void log(String msg) {
        LogUtils.e("FileUtils", msg);
    }

    private static final int FILE_NULL = 0;
    private static final int FILE_TYPE_ERROR = 1;
    private static final int FILE_UNKNOWN = -1;
}
