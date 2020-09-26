package com.sj.android_base_utils.utils_media;

import android.media.AudioFormat;
import android.media.AudioManager;
import android.media.AudioTrack;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;

import com.sj.android_base_utils.utils_media.utils.FileUtils;

import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;

public class AudioTrackManager {
    private static final String TAG = "AudioTrackManager";
    private final int STREAM_TYPE = AudioManager.STREAM_MUSIC;//音频管理策略
    private final int sampleRateInHz = 8000;//采样率 目前44100Hz是唯一可以保证兼容所有Android手机的采样率
    private final int channelConfig = AudioFormat.CHANNEL_OUT_MONO;//通道数配置
    private final int audioFormat = AudioFormat.ENCODING_PCM_16BIT;//数据位宽 保证兼容所有Android手机
    private final int mode = AudioTrack.MODE_STREAM;//播放模式

    private AudioTrack audioTrack;
    private DataInputStream dis;
    private Thread recordThread;
    private Thread timeThread;
    private boolean isPlaying = false;
    private static AudioTrackManager mInstance;
    private int bufferSize;

    public AudioTrackManager() {
        bufferSize = AudioTrack.getMinBufferSize(sampleRateInHz, channelConfig, audioFormat);
    }

    /**
     * 获取单例引用
     *
     * @return
     */
    public static AudioTrackManager getInstance() {
        if (mInstance == null) {
            synchronized (AudioTrackManager.class) {
                if (mInstance == null) {
                    mInstance = new AudioTrackManager();
                }
            }
        }
        return mInstance;
    }

    /**
     * 启动播放
     *
     * @param path
     */
    public void startPlay(String path) {
        try {
            setPath(path);
            startThread();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void startPlay(String path,boolean isOpus ) {
        if (isOpus) {
            FileUtils.opus2pcm(path+"opus.opus",path+"opus.pcm");
            path = path+"opus.pcm";
        }else {
            FileUtils.spx2pcm(path+"spx.spx",path+"spx.pcm");
            path = path+"spx.pcm";
        }
        try {
            setPath(path);
            startThread();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * 停止播放
     */
    public void stopPlay() {
        isPlaying = false;
    }

    /**
     * 获取播放状态
     *
     * @return
     */
    public boolean isPlaying() {
        return isPlaying;
    }

    /**
     * 启动播放线程
     */
    private void startThread() {
        destroyThread();
        isPlaying = true;
        recordThread = new Thread(new PlayRunnable());
        recordThread.start();
        timeThread = new Thread(new TimeCount());
        timeThread.start();
    }

    /**
     * 播放文件
     *
     * @param path
     * @throws Exception
     */
    private void setPath(String path) throws Exception {
        File file = new File(path);
        dis = new DataInputStream(new FileInputStream(file));
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


    private PlayListener playListener;

    interface PlayListener {
        void startPlay();
        void stopPlay();
        void setCount(int count);
        void showMsg(String msg);
    }

    public void setPlayListener(PlayListener playListener) {
        this.playListener = playListener;
    }

    private final int COUNTING = 0;
    private final int START_PLAY = 1;
    private final int STOP_PLAY = 2;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case COUNTING:
                    if (playListener != null) {
                        playListener.setCount(msg.arg1);
                    }
                    break;
                case START_PLAY:
                    if (playListener != null) {
                        playListener.startPlay();
                        if (!TextUtils.isEmpty(((String) msg.obj))) {
                            playListener.showMsg((String) msg.obj);
                        }
                    }
                    break;
                case STOP_PLAY:
                    isPlaying = false;
                    if (playListener != null) {
                        playListener.stopPlay();
                        if (!TextUtils.isEmpty(((String) msg.obj))) {
                            playListener.showMsg((String) msg.obj);
                        }
                    }
                    stopPlay();
                    destroyThread();
                    break;
            }
        }
    };

    /**
     * 播放线程
     */
    class PlayRunnable implements Runnable {
        @Override
        public void run() {
            try {
                handler.sendEmptyMessage(START_PLAY);
                android.os.Process.setThreadPriority(android.os.Process.THREAD_PRIORITY_URGENT_AUDIO);
                audioTrack = new AudioTrack(
                        STREAM_TYPE,
                        sampleRateInHz,
                        channelConfig,
                        audioFormat,
                        bufferSize * 2,
                        mode
                );
                byte[] tempBuffer = new byte[bufferSize];
                int readCount = 0;
                while (isPlaying && dis.available() > 0) {
                    readCount = dis.read(tempBuffer);
                    if (readCount == AudioTrack.ERROR_INVALID_OPERATION || readCount == AudioTrack.ERROR_BAD_VALUE) {
                        continue;
                    }
                    if (readCount != 0 && readCount != -1) {
                        audioTrack.play();
                        audioTrack.write(tempBuffer, 0, readCount);
                    }
                }
                Log.e(TAG, "run: " + Thread.currentThread().getName());
                audioTrack.stop();
                dis.close();
                handler.sendEmptyMessage(STOP_PLAY);
            } catch (Exception e) {
                Message msg = Message.obtain();
                msg.what = STOP_PLAY;
                msg.obj = e.getMessage();
                handler.sendMessage(msg);
            }
        }

    }

    ;

    /**
     * 计时线程
     */
    class TimeCount implements Runnable {
        private int count = 0;
        @Override
        public void run() {
            while (isPlaying) {
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