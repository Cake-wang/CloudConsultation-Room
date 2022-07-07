package com.aries.template.xiaoyu.uvc;

import android.app.Activity;
import android.graphics.SurfaceTexture;
import android.hardware.usb.UsbDevice;
import android.log.L;
import android.opengl.GLES20;
import android.os.Handler;
import android.text.TextUtils;
import android.widget.Toast;

import com.ainemo.sdk.otf.NemoSDK;
import com.aries.template.R;
import com.serenegiant.usb.DeviceFilter;
import com.serenegiant.usb.IFrameCallback;
import com.serenegiant.usb.USBMonitor;
import com.serenegiant.usb.UVCCamera;

import java.util.List;

import vulture.module.call.nativemedia.NativeDataSourceManager;

/**
 * UVC外接摄像头业务, 不需要的可以拿掉
 */
public class UVCCameraPresenter {
    private static final String TAG = "UVCCameraPresenter";
    private Activity mContext;

    private final Object mSync = new Object();
    private USBMonitor mUSBMonitor;
    private UVCCamera mUVCCamera;
    private SurfaceTexture mSurfaceTexture;

    /** 已经正在播放中 */
    private boolean proceccing;

    private boolean isUvcCamera;
    private int currentCamera = 0; // 0 前置 1 后置 2 UVC

    private Handler handler = new Handler();

    public UVCCameraPresenter(Activity context) {
        this.mContext = context;
        // fallback to YUV mode
        mUSBMonitor = new USBMonitor(context, mOnDeviceConnectListener);
        isUvcCamera = hasUvcCamera();
        updateCameraInfo(isUvcCamera, isUvcCamera ? 2 : 0);
        initSurfaceTexture();
    }

    private void initSurfaceTexture() {
        int[] texture = new int[1];

        GLES20.glGenTextures(1, texture, 0);

        GLES20.glClearColor(0.5f, 0.5f, 0.5f, 1.0f);

        mSurfaceTexture = new SurfaceTexture(texture[0]);
    }

    private final USBMonitor.OnDeviceConnectListener mOnDeviceConnectListener = new USBMonitor.OnDeviceConnectListener() {

        @Override
        public void onAttach(final UsbDevice device) {
            L.i(TAG, "onAttach:");
            Toast.makeText(mContext, "USB_DEVICE_ATTACHED", Toast.LENGTH_SHORT).show();
            synchronized (mSync) {
                onDialogResult(true);
            }
        }

        @Override
        public void onConnect(final UsbDevice device, final USBMonitor.UsbControlBlock ctrlBlock, final boolean createNew) {
            L.i(TAG, "onConnect:");
            NemoSDK.getInstance().releaseCamera();
            updateCameraInfo(true, 2);
            releaseCamera();

            NemoSDK.getInstance().switchCamera(2);

            handler.postDelayed(() -> {
                final UVCCamera camera = new UVCCamera();
                try {
                    camera.open(ctrlBlock);

                    L.i(TAG, "supportedSize:" + camera.getSupportedSize() + ",ctrlBlock=" + ctrlBlock);
                    camera.setPreviewSize(UVCCamera.DEFAULT_PREVIEW_WIDTH, UVCCamera.DEFAULT_PREVIEW_HEIGHT, UVCCamera.PIXEL_FORMAT_YUV420SP);
                } catch (final IllegalArgumentException e) {
                    // fallback to YUV mode
                    L.i(TAG, e.getMessage());
                    L.i(TAG, "IllegalArgumentException setPreviewSize:" + camera.getSupportedSize());
                    try {
                        camera.setPreviewSize(UVCCamera.DEFAULT_PREVIEW_WIDTH, UVCCamera.DEFAULT_PREVIEW_HEIGHT, UVCCamera.DEFAULT_PREVIEW_MODE);
                    } catch (final IllegalArgumentException e1) {
//                            camera.destroy();
                        return;
                    }
                }
                frameCount = 0;
                if (mSurfaceTexture != null) {
                    L.i(TAG, "setPreviewTexture success");
                    camera.setPreviewTexture(mSurfaceTexture);
                    camera.setFrameCallback(mIFrameCallback, UVCCamera.PIXEL_FORMAT_YUV420SP);
                    camera.startPreview();
                } else {
                    L.i(TAG, "mSurfaceTexture == null, cancel setPreviewTexture");
                }
                synchronized (mSync) {
                    mUVCCamera = camera;
                }
            }, 0);
        }

        @Override
        public void onDisconnect(final UsbDevice device, final USBMonitor.UsbControlBlock ctrlBlock) {
            handler.postDelayed(() -> releaseCamera(), 0);
        }

        @Override
        public void onDettach(final UsbDevice device) {
            Toast.makeText(mContext, "USB_DEVICE_DETACHED", Toast.LENGTH_SHORT).show();
            updateCameraInfo(false, 1);
            NemoSDK.getInstance().switchCamera(1);
            NemoSDK.getInstance().requestCamera();
        }

        @Override
        public void onCancel(final UsbDevice device) {
        }
    };

    public boolean hasUvcCamera() {
        return (getUsbDevice() != null);
    }

    private void updateCameraInfo(boolean isUVC, int cameraID) {
        isUvcCamera = isUVC;
        currentCamera = cameraID;
    }

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

    private int frameCount = 0;

    private final IFrameCallback mIFrameCallback = frame -> {
        frame.clear();
        int len = frame.capacity();
        int captureWidth = UVCCamera.DEFAULT_PREVIEW_WIDTH;
        int captureHeight = UVCCamera.DEFAULT_PREVIEW_HEIGHT;
        byte[] yuv = new byte[len];
        frame.get(yuv);
        String localSourceId = NemoSDK.getInstance().getDataSourceId();
        if (!TextUtils.isEmpty(localSourceId)) {
            NativeDataSourceManager.putVideoData(localSourceId, yuv, yuv.length,
                    captureWidth, captureHeight, 0, false);
        }
        if (frameCount % 50 == 0 && frameCount < 500) {
            L.i(TAG, "putVideoData: " + yuv.length);
        }
        frameCount++;
        NativeDataSourceManager.putVideoData("LocalPreviewID", yuv, yuv.length,
                captureWidth, captureHeight, 0, false);
    };

    public void onDialogResult(boolean canceled) {
        if (canceled && mUSBMonitor != null) {
            UsbDevice usbdevice = getUsbDevice();
            if (usbdevice != null) {
                if (11785 == usbdevice.getVendorId() && 48 == usbdevice.getProductId()) {
                } else {
                    mUSBMonitor.requestPermission(usbdevice);
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

    public void switchCamera() {
        switch (currentCamera) {
            case 0:
                releaseCamera();
                updateCameraInfo(false, 1);
                NemoSDK.getInstance().switchCamera(1);
                break;
            case 1:
                NemoSDK.getInstance().releaseCamera();
                updateCameraInfo(true, 2);
                NemoSDK.getInstance().switchCamera(2);
                onDialogResult(true);
                break;
            case 2:
                updateCameraInfo(false, 0);
                releaseCamera();
                NemoSDK.getInstance().switchCamera(0);
//                    NemoSDK.getInstance().requestCamera();
                break;
            default:
                break;
        }
    }

    public synchronized void releaseCamera() {
        if (mUVCCamera != null) {
            try {
                mUVCCamera.close();
                mUVCCamera.destroy();
            } catch (final Exception e) {
                //
                e.printStackTrace();
            }
            mUVCCamera = null;
        }
        if (mSurfaceTexture != null) {
            mSurfaceTexture.release();
        }
    }

    /**
     * 初始化注册
     * mUSBMonitor USB
     * mUVCCamera 摄像头
     * onDialogResult 权限
     */
    public void onInit(){
        if (mUSBMonitor != null) {
            mUSBMonitor.register();
        }
        synchronized (mSync) {
            if (mUVCCamera != null) {
                mUVCCamera.setFrameCallback(mIFrameCallback, UVCCamera.PIXEL_FORMAT_YUV420SP);
            }
        }
        onDialogResult(true);
    }

    /**
     * 启动摄像头
     */
    public void onStart(){
        if (mUVCCamera!=null
                && proceccing!=true){
            mUVCCamera.startPreview();
            proceccing = true;
        }
    }

    /**
     * 一气呵成的装载
     * 可能会有问题，会崩溃
     */
    public void onStartAndRegister() {
        if (mUSBMonitor != null) {
            mUSBMonitor.register();
        }
        synchronized (mSync) {
            if (mUVCCamera != null) {
                mUVCCamera.setFrameCallback(mIFrameCallback, UVCCamera.PIXEL_FORMAT_YUV420SP);
                mUVCCamera.startPreview();
            }
        }
        onDialogResult(true);
    }

    private synchronized void releaseUsbMonitor() {
        if (mUSBMonitor != null) {
            mUSBMonitor.destroy();
            mUSBMonitor = null;
        }
    }

    public void onDestroy() {
        releaseCamera();
        releaseUsbMonitor();
        mSurfaceTexture.release();
    }

    public boolean isUvcCamera() {
        return isUvcCamera;
    }

    public void onStop() {
        proceccing = false;
        releaseCamera();
        synchronized (mSync) {
            if (mUSBMonitor != null) {
                mUSBMonitor.unregister();
            }
        }
    }

}
