package com.sj.android_base_utils.utils_media;

import android.media.AudioFormat;
import android.media.AudioRecord;
import android.media.MediaRecorder;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;

public class AudioRecordManager {
    private static final String TAG = "AudioRecordManager";
    private final int AUDIO_SOURCE = MediaRecorder.AudioSource.MIC;//音频采集的输入源
    private final int sampleRateInHz = 8000;//采样率 目前44100Hz是唯一可以保证兼容所有Android手机的采样率
    private final int channelConfig = AudioFormat.CHANNEL_IN_MONO;//通道数配置
    private final int audioFormat = AudioFormat.ENCODING_PCM_16BIT;//数据位宽 保证兼容所有Android手机
    private int bufferSize;//音频缓冲区的大小
    private boolean isRecording = false;//录制状态
    private AudioRecord mRecorder;
    private DataOutputStream dos;
    private Thread recordThread;
    private Thread timeThread;
    private static AudioRecordManager mInstance;
    private RecordListener recordListener;
    private String fPath;

    public AudioRecordManager() {
        bufferSize = AudioRecord.getMinBufferSize(sampleRateInHz, channelConfig, audioFormat);
    }

    /**
     * 获取单例引用
     *
     * @return
     */
    public static AudioRecordManager getInstance() {
        if (mInstance == null) {
            synchronized (AudioRecordManager.class) {
                if (mInstance == null) {
                    mInstance = new AudioRecordManager();
                }
            }
        }
        return mInstance;
    }

    /**
     * 启动录音
     *
     * @param path
     */
    public void startRecord(String path) {
        try {
            setPath(path);
            startThread();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 停止录音
     */
    public void stopRecord() {
        isRecording = false;
    }

    /**
     * 保存文件
     *
     * @param fPath
     * @throws Exception
     */
    private void setPath(String fPath) throws Exception {
        this.fPath = fPath;
        File file = new File(fPath +"Audio.pcm");
        if (file.exists()) {
            file.delete();
        }
        file.createNewFile();
        dos = new DataOutputStream(new FileOutputStream(file, true));
    }

    /**
     * 获取录音状态
     *
     * @return
     */
    public boolean isRecording() {
        return isRecording;
    }

    /**
     * 启动录音线程
     */
    private void startThread() {
        destroyThread();
        isRecording = true;
        recordThread = new Thread(new RecordRunnable());
        recordThread.start();
        timeThread = new Thread(new TimeCount());
        timeThread.start();
    }

    /**
     * 销毁线程方法
     */
    private void destroyThread() {
        try {
            if (null != recordThread && Thread.State.RUNNABLE == recordThread.getState()) {
                try {
                    Thread.sleep(500);
                    recordThread.interrupt();
                } catch (Exception e) {
                    recordThread = null;
                }
            }
            if (null != timeThread && Thread.State.RUNNABLE == timeThread.getState()) {
                try {
                    Thread.sleep(500);
                    timeThread.interrupt();
                } catch (Exception e) {
                    timeThread = null;
                }
            }
            recordThread = null;
            timeThread = null;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            recordThread = null;
            timeThread = null;
        }
    }

    /**
     * 录音监听
     */
    interface RecordListener {
        void recordCount(int count);

        void showMsg(String msg);

        void startRecord();

        void stopRecord();
    }

    public void setRecordListener(RecordListener recordListener) {
        this.recordListener = recordListener;
    }

    private final int COUNTING = 0;
    private final int START_RECORD = 1;
    private final int STOP_RECORD = 2;

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case COUNTING:
                    if (recordListener != null) {
                        recordListener.recordCount(msg.arg1);
                    }
                    break;
                case START_RECORD:
                    if (recordListener != null) {
                        recordListener.startRecord();
                    }
                    break;
                case STOP_RECORD:
                    if (recordListener != null) {
                        recordListener.stopRecord();
                        if (!TextUtils.isEmpty(((String) msg.obj))) {
                            recordListener.showMsg((String) msg.obj);
                        }
                    }
                    stopRecord();
                    destroyThread();
                    break;
            }
        }
    };
    /**
     * 录音线程
     */
    class RecordRunnable implements Runnable {

        @Override
        public void run() {
            try {
                handler.sendEmptyMessage(START_RECORD);
                android.os.Process.setThreadPriority(android.os.Process.THREAD_PRIORITY_URGENT_AUDIO);
                int bytesRecord;
                byte[] tempBuffer = new byte[bufferSize];
                mRecorder = new AudioRecord(
                        AUDIO_SOURCE,
                        sampleRateInHz,
                        channelConfig,
                        audioFormat,
                        bufferSize * 2);
                mRecorder.startRecording();
                while (isRecording) {
                    if (null != mRecorder) {
                        bytesRecord = mRecorder.read(tempBuffer, 0, bufferSize);
                        if (bytesRecord == AudioRecord.ERROR_INVALID_OPERATION || bytesRecord == AudioRecord.ERROR_BAD_VALUE) {
                            continue;
                        }
                        if (bytesRecord != 0 && bytesRecord != -1) {
                            //在此可以对录制音频的数据进行二次处理 比如变声，压缩，降噪，增益等操作
                            //我们这里直接将pcm音频原数据写入文件 这里可以直接发送至服务器 对方采用AudioTrack进行播放原数据
                            dos.write(tempBuffer, 0, bytesRecord);
                        } else {
                            break;
                        }
                    }
                }
                mRecorder.stop();
                dos.flush();
                dos.close();
                Message msg = Message.obtain();
                msg.what = STOP_RECORD;
                msg.obj = "录制结束";
                handler.sendMessage(msg);
            } catch (Exception e) {
                Message msg = Message.obtain();
                msg.what = STOP_RECORD;
                msg.obj = e.getMessage();
                handler.sendMessage(msg);
            }
        }
    }


    /**
     * 计时线程
     */
    class TimeCount implements Runnable {
        private int count = 0;

        @Override
        public void run() {
            while (isRecording) {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                count++;
                Message message = Message.obtain();
                message.what = COUNTING;
                message.arg1 = count;
                handler.sendMessage(message);
            }
        }
    }
}