package com.sj.android_base_utils.utils_network;

import android.app.DownloadManager;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Environment;
import android.util.Log;

import java.io.File;

public class DownloadUtil {
    private Context context;
    private String downloadUrl;
    private String downloadPath;
    private boolean allowNetworkMobile;
    private long downloadID;
    private DownloadManager downloadManager;

    public DownloadUtil(Context context, String downloadUrl, String downloadPath, boolean allowNetworkMobile) {
        this.context = context;
        this.downloadUrl = downloadUrl;
        this.downloadPath = downloadPath;
        this.allowNetworkMobile = allowNetworkMobile;
        initDownloadManager();
    }

    //1.下载配置
    private void initDownloadManager() {
        downloadManager = (DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE);
        DownloadManager.Request request = new DownloadManager.Request(Uri.parse(downloadUrl));

        /**设置用于下载时的网络状态*/
        if (allowNetworkMobile) {
            request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI | DownloadManager.Request.NETWORK_MOBILE);
        } else {
            request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI);
        }
        /**设置通知栏是否可见*/
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_HIDDEN);
        /**设置漫游状态下是否可以下载*/
        request.setAllowedOverRoaming(false);
        /**如果我们希望下载的文件可以被系统的Downloads应 用扫描到并管理，
         我们需要调用Request对象的setVisibleInDownloadsUi方法，传递参数true.*/
        request.setVisibleInDownloadsUi(false);
        /**设置文件保存路径*/
        request.setDestinationInExternalPublicDir(Environment.getExternalStorageDirectory()+ File.separator+"AA"+ File.separator,downloadPath);
        /**将下载请求放入队列， return下载任务的ID*/
        downloadID = downloadManager.enqueue(request);

    }

    //2.获取下载状态
    public void checkStatus(int downloadID) {
        DownloadManager.Query query = new DownloadManager.Query();
        //通过下载的id查找
        if (downloadID!=0) {
            query.setFilterById(downloadID);
            Cursor c = downloadManager.query(query);
            if (c.moveToFirst()) {
                int status = c.getInt(c.getColumnIndex(DownloadManager.COLUMN_STATUS));
                switch (status) {
                    //下载暂停
                    case DownloadManager.STATUS_PAUSED:
                        break;
                    //下载延迟
                    case DownloadManager.STATUS_PENDING:
                        break;
                    //正在下载
                    case DownloadManager.STATUS_RUNNING:
                        break;
                    //下载完成
                    case DownloadManager.STATUS_SUCCESSFUL:
                        break;
                    //下载失败
                    case DownloadManager.STATUS_FAILED:
                        break;
                }
            }
            c.close();
        }
    }


    // 查询下载进度
    public int query() {
        if (downloadID!=0) {
            DownloadManager.Query downloadQuery = new DownloadManager.Query();
            downloadQuery.setFilterById(downloadID);
            Cursor cursor = downloadManager.query(downloadQuery);
            if (cursor != null && cursor.moveToFirst()) {
                int fileName = cursor.getColumnIndex(DownloadManager.COLUMN_LOCAL_FILENAME);
                int fileUri = cursor.getColumnIndex(DownloadManager.COLUMN_URI);
                String fn = cursor.getString(fileName);
                String fu = cursor.getString(fileUri);

                int totalSizeBytesIndex = cursor.getColumnIndex(DownloadManager.COLUMN_TOTAL_SIZE_BYTES);
                int bytesDownloadSoFarIndex = cursor.getColumnIndex(DownloadManager.COLUMN_BYTES_DOWNLOADED_SO_FAR);

                // 下载的文件总大小
                int totalSizeBytes = cursor.getInt(totalSizeBytesIndex);

                // 截止目前已经下载的文件总大小
                int bytesDownloadSoFar = cursor.getInt(bytesDownloadSoFarIndex);

                Log.d(this.getClass().getName(),
                        "from " + fu + " 下载到本地 " + fn + " 文件总大小:" + totalSizeBytes + " 已经下载:" + bytesDownloadSoFar);
                cursor.close();
                return bytesDownloadSoFar*100/totalSizeBytes;
            }
        }
        return 0;
    }


}

