package com.aries.template.xiaoyu.uvc;

import android.app.Activity;
import android.graphics.SurfaceTexture;
import android.hardware.usb.UsbDevice;
import android.opengl.GLES20;
import android.os.Handler;
import android.os.HandlerThread;
import android.text.TextUtils;

import com.ainemo.sdk.otf.NemoSDK;
import com.ainemo.sdk.otf.Orientation;
import com.aries.template.R;
import com.serenegiant.usb.DeviceFilter;
import com.serenegiant.usb.IFrameCallback;
import com.serenegiant.usb.Size;
import com.serenegiant.usb.USBMonitor;
import com.serenegiant.usb.UVCCamera;
import com.serenegiant.usb.UVCParam;
import com.serenegiant.utils.UVCUtils;

import java.util.ArrayList;
import java.util.List;

import vulture.module.call.nativemedia.NativeDataSourceManager;

/**
 *
 * UVC Camera 外界设想头控制器
 * 和小鱼SDK 深度融合，实现小鱼视频接收数据和格式化数据
 * 本类的核心业务是将摄像头打开，并启动摄像头数据录入，转存在小鱼SDK里面
 */
public class UVCAndroidCameraPresenter {
    private static final String TAG = "UVCCameraPresenter";
    // 上下文
    private Activity mContext;
    // 同步对象
    private final Object mSync = new Object();
    // USB 控制器，专门控制USB 设备
    private USBMonitor mUSBMonitor;
    // Camera 控制器，设定摄像头读取数据
    private UVCCamera mUVCCamera;
    // 视频结果显示器
    private SurfaceTexture mSurfaceTexture;
    // 类型必须是 NV21 否则 显示错误
    private int DefaultMode = UVCCamera.PIXEL_FORMAT_NV21;
    // 当前是否Uvc camera
    private boolean isUvcCamera;
    // 线程处理，增加性能
    private Handler handler = new Handler();
    // 视频统一参数
    private Size size;

    private HandlerThread mListenerHandlerThread;
    private Handler mListenerHandler;

    public UVCAndroidCameraPresenter(Activity context) {
        this.mContext = context;
        // fallback to YUV mode
//        mUSBMonitor = new USBMonitor(context, mOnDeviceConnectListener);
//        mUSBMonitor.register();
        mListenerHandlerThread = new HandlerThread("CameraConnection#" + hashCode());
        mListenerHandlerThread.start();
        mListenerHandler = new Handler(mListenerHandlerThread.getLooper());

        mUSBMonitor = new USBMonitor(
                UVCUtils.getApplication(),
                mOnDeviceConnectListener,
                mListenerHandler);

        // 如果有USB摄像头，就用他
        isUvcCamera = hasUvcCamera();
        // 初始化 mSurfaceTexture
        initSurfaceTexture();
        // 创建 size
        size = new Size(UVCCamera.DEFAULT_PREVIEW_FRAME_FORMAT,1280,720,30, new ArrayList(30));
    }

    /**
     * 设定 mSurfaceTexture 参数
     */
    private void initSurfaceTexture() {
        int[] texture = new int[1];
        GLES20.glGenTextures(1, texture, 0);
        GLES20.glClearColor(0.5f, 0.5f, 0.5f, 1.0f);
        mSurfaceTexture = new SurfaceTexture(texture[0]);
    }

    UVCCamera camera ;

    /**
     * USB 监听器
     */
    private final USBMonitor.OnDeviceConnectListener mOnDeviceConnectListener = new USBMonitor.OnDeviceConnectListener() {
        @Override
        public void onAttach(final UsbDevice device) {
            // 插入 USB
            // 这个方法会多次被调用
//            L.i(TAG, "onAttach:");
            synchronized (mSync) {
                onDialogResult(true);
            }
        }

        @Override
        public void onDeviceOpen(UsbDevice device, USBMonitor.UsbControlBlock ctrlBlock, boolean createNew) {
            // USB 摄像头打开
//            L.i(TAG, "onConnect:");
            NemoSDK.getInstance().releaseCamera();
            releaseCamera();
            handler.postDelayed(() -> {
               camera = new UVCCamera(new UVCParam());
//                final UVCCamera camera = new UVCCamera(new UVCParam(size,0));
                try {
                    camera.open(ctrlBlock);
//                    L.i(TAG, "supportedSize:" + camera.getSupportedSize() + ",ctrlBlock=" + ctrlBlock);
                    camera.setPreviewSize(size);
                } catch (final IllegalArgumentException e) {
                    // fallback to YUV mode
//                    L.i(TAG, e.getMessage());
//                    L.i(TAG, "IllegalArgumentException setPreviewSize:" + camera.getSupportedSize());
                    try {
                        camera.setPreviewSize(size);
                    } catch (final IllegalArgumentException e1) {
//                            camera.destroy();
                        return;
                    }
                }
                if (mSurfaceTexture != null) {
//                    L.i(TAG, "setPreviewTexture success");
                    camera.setPreviewTexture(mSurfaceTexture);
                    camera.setFrameCallback(mIFrameCallback, DefaultMode);
                    camera.startPreview();
                } else {
//                    L.i(TAG, "mSurfaceTexture == null, cancel setPreviewTexture");
                }
                synchronized (mSync) {
                    mUVCCamera = camera;
                }
            }, 0);
        }

        @Override
        public void onDeviceClose(UsbDevice usbDevice, USBMonitor.UsbControlBlock usbControlBlock) {
            // 关闭 UVC
            // 如果USB 被拔掉了，则启动 前置摄像头
//            try{
//                // maybe throw java.lang.IllegalStateException: already released
//                camera.setFrameCallback(null,DefaultMode);
//            }
//            catch(Exception e) {
//                e.printStackTrace();
//            }


            useFrontCamera();
        }

        @Override
        public void onDetach(UsbDevice usbDevice) {
            // 断开 UVC
            handler.postDelayed(() -> releaseCamera(), 0);
        }

        @Override
        public void onCancel(final UsbDevice device) {
        }
    };

    /**
     * 是否有USB Camera
     */
    public boolean hasUvcCamera() {
        return (getUsbDevice() != null);
    }

    /**
     * 获取 USB 设备
     */
    private UsbDevice getUsbDevice() {
        final List<DeviceFilter> filter = DeviceFilter.getDeviceFilters(mContext, R.xml.device_filter);
        if (filter != null && filter.size() > 0 && mUSBMonitor != null) {
            List<UsbDevice> devices = mUSBMonitor.getDeviceList(filter.get(0));
            if (devices != null && devices.size() > 0) {
                return devices.get(0);
            }
        }
        return null;
    }

    /**
     * 小鱼的监听返回
     */
    private final IFrameCallback mIFrameCallback = frame -> {
//        Log.d(TAG, "mIFrameCallback: work");
        frame.clear();
        int len = frame.capacity();
        int captureWidth =size.width; //视频宽
        int captureHeight =size.height; //视频高
        byte[] yuv = new byte[len];
        frame.get(yuv);
        String localSourceId = NemoSDK.getInstance().getDataSourceId();
        if (!TextUtils.isEmpty(localSourceId)) {
//            Log.d(TAG, "mIFrameCallbackkkkkk: work1");
            NativeDataSourceManager.putVideoData(localSourceId, yuv, yuv.length,
                    captureWidth, captureHeight, 0, false);
        }else {
//            Log.d(TAG, "mIFrameCallbackkkkkk: work2");
            NativeDataSourceManager.putVideoData("LocalPreviewID", yuv, yuv.length,
                    captureWidth, captureHeight, 0, false);
        }
    };

    /**
     * 获取USB权限
     * 有很大的可能会在这里崩溃，注意操作
     * 里面的processConnect 如果过多次数的执行，会崩溃
     */
    public void onDialogResult(boolean canceled) {
        // 加固代码
        try{
            if (canceled && mUSBMonitor != null) {
                UsbDevice usbdevice = getUsbDevice();
                if (usbdevice != null) {
                    if (11785 == usbdevice.getVendorId() && 48 == usbdevice.getProductId()) {
                    } else {
                        mUSBMonitor.requestPermission(usbdevice);
                    }
                }
            }
        }catch (Exception e){
//            Log.e(TAG, "onDialogResult: 崩溃了，请重新尝试");
        }
    }

    /**
     * 经常进行判断USB是否已经被拿到权限
     * 如果拿到权限了，就不做任何操作
     * 如果没有，就申请
     * 插入USB 可能会无法启动，这是由于 .processConnect(device); 没有执行造成的
     * todo 优化，第二次执行，发现有设备的时候，执行 .processConnect(device);
     */
    public void onDialogResult(){
        if (mUSBMonitor!=null){
            UsbDevice usbdevice = getUsbDevice();
            if (usbdevice != null) {
                if (11785 == usbdevice.getVendorId() && 48 == usbdevice.getProductId()) {
                } else {
                    try {
                        if (!mUSBMonitor.hasPermission(usbdevice)){
                            mUSBMonitor.requestPermission(usbdevice);
                        }
                    }catch (Exception e){e.printStackTrace();}
                }
            }
        }
    }

    public void requestCamera() {
        if (!isUvcCamera()) {
            NemoSDK.getInstance().requestCamera();
        } else {
            onDialogResult(true);
        }
    }

    /**
     * 切换摄像头为前置摄像头
     */
    public void useFrontCamera() {
        // todo 如果前置摄像头不启动，提示用户，并退回到首页
        try{
            NemoSDK.getInstance().releaseCamera();
            releaseCamera();
            // 设置小鱼摄像头方向
            // 设置摄像头方向
            NemoSDK.getInstance().setOrientation(Orientation.LANDSCAPE);
            // 如果没有，则启动前置
            NemoSDK.getInstance().switchCamera(0);
        }catch (Exception e){
            e.printStackTrace();
//            JTJKLogUtils.message(e.toString());
        }
    }

    /**
     * 释放摄像头
     */
    public synchronized void releaseCamera() {
//        NemoSDK.getInstance().releaseCamera();
        if (mUVCCamera != null) {
            try {
                // maybe throw java.lang.IllegalStateException: already released
                mUVCCamera.setFrameCallback(null,DefaultMode);
                mUVCCamera.close();
                mUVCCamera.destroy();
            } catch (final Exception e) {
                e.printStackTrace();
            }
            mUVCCamera = null;
        }
        if (mSurfaceTexture != null) {
            mSurfaceTexture.release();
        }
    }

    /**
     * 启动
     * 一气呵成的装载
     * onDialogResult 绝对不能启动，会崩溃的
     */
    public void onStart() {
        if (mUSBMonitor != null) {
            try{
                mUSBMonitor.hasPermission(null);
                mUSBMonitor.register();
            }catch (Exception e){
                e.printStackTrace();
            }
        }
        synchronized (mSync) {
            //如果有USB摄像头，则启动
            if (hasUvcCamera()){
                if (mUVCCamera != null) {
                    //  添加 UVC 流监听和流的解码类型
                    mUVCCamera.setFrameCallback(mIFrameCallback, DefaultMode);
                    mUVCCamera.startPreview();
                }
            }else{
                useFrontCamera();
            }
        }
//         绝对不能启动 onDialogResult
//        onDialogResult();
    }

    /**
     * 释放 mUSBMonitor
     */
    private synchronized void releaseUsbMonitor() {

            if (mUSBMonitor != null) {
//                try {
//                    Log.d("JTJK","releaseUsbMonitor b");
//                    // 由于 hasPermission 有崩溃，所以必须要在 destroy 之前，先判断
//                    mUSBMonitor.hasPermission(null);
//                    mUSBMonitor.destroy();
//                    Log.d("JTJK","releaseUsbMonitor e");
//                }catch (Exception e){
//                    e.printStackTrace();
//                }
                if (!mUSBMonitor.isRegistered()){
                    try{

                        mUSBMonitor.destroy();

                    }catch (Exception e){e.printStackTrace();};

                }
                mUSBMonitor = null;

                mListenerHandlerThread.quitSafely();
            }

    }

    /**
     * 删除本类的所有对象
     */
    public void onDestroy() {
        releaseCamera();
        releaseUsbMonitor();
        mSurfaceTexture.release();
        handler.removeCallbacksAndMessages(null);
    }

    public boolean isUvcCamera() {
        return isUvcCamera;
    }

    /**
     * 移除所有
     */
    public void onStop() {
        releaseCamera();
        synchronized (mSync) {
            if (mUSBMonitor != null) {
                mUSBMonitor.unregister();
            }
        }
    }

    /**
     * 注销 USB
     */
    public void USBUnregister(){
        synchronized (mSync) {
            try {
//                Log.d("JTJK","releaseUsbMonitor b");
                // 由于 hasPermission 有崩溃，所以必须要在 destroy 之前，先判断
                mUSBMonitor.hasPermission(null);
                mUSBMonitor.unregister();
//                Log.d("JTJK","releaseUsbMonitor e");
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }

}
