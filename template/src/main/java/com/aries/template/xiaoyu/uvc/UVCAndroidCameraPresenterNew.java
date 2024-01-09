package com.aries.template.xiaoyu.uvc;

import android.app.Activity;
import android.hardware.usb.UsbDevice;
import android.view.SurfaceHolder;

import com.herohan.uvcapp.CameraHelper;
import com.herohan.uvcapp.ICameraHelper;
import com.serenegiant.usb.Size;
import com.serenegiant.widget.AspectRatioSurfaceView;

import androidx.annotation.NonNull;

public class UVCAndroidCameraPresenterNew {


    public ICameraHelper mCameraHelper;
    private AspectRatioSurfaceView mCameraViewMain;
    private ICameraHelper.StateCallback mStateListener;
    private Activity mContext;

    public UVCAndroidCameraPresenterNew(Activity context) {
        this.mContext = context;
        //UVC摄像头状态回调
        mStateListener = new ICameraHelper.StateCallback() {
            //插入UVC设备
            @Override
            public void onAttach(UsbDevice device) {
                //设置为当前设备（如果没有权限，会显示授权对话框）
                mCameraHelper.selectDevice(device);
            }

            //打开UVC设备成功（也就是已经获取到UVC设备的权限）
            @Override
            public void onDeviceOpen(UsbDevice device, boolean isFirstOpen) {
                //打开UVC摄像头
                mCameraHelper.openCamera();
            }

            //打开摄像头成功
            @Override
            public void onCameraOpen(UsbDevice device) {
                //开始预览
                mCameraHelper.startPreview();

                //获取预览使用的Size（包括帧格式、宽度、高度、FPS）
                Size size = mCameraHelper.getPreviewSize();
                if (size != null) {
                    int width = size.width;
                    int height = size.height;
                    //需要自适应摄像头分辨率的话，设置新的宽高比
                    mCameraViewMain.setAspectRatio(width, height);
                }

                //添加预览Surface
                mCameraHelper.addSurface(mCameraViewMain.getHolder().getSurface(), false);
            }

            //关闭摄像头成功
            @Override
            public void onCameraClose(UsbDevice device) {
                if (mCameraHelper != null) {
                    //移除预览Surface
                    mCameraHelper.removeSurface(mCameraViewMain.getHolder().getSurface());
                }
            }

            //关闭UVC设备成功
            @Override
            public void onDeviceClose(UsbDevice device) {
            }

            //断开UVC设备
            @Override
            public void onDetach(UsbDevice device) {
            }

            //用户没有授予访问UVC设备的权限
            @Override
            public void onCancel(UsbDevice device) {
            }

        };

        //设置SurfaceView的Surface监听回调
        mCameraViewMain.getHolder().addCallback(new SurfaceHolder.Callback() {

            //创建了新的Surface
            @Override
            public void surfaceCreated(@NonNull SurfaceHolder holder) {
                if (mCameraHelper != null) {
                    //添加预览Surface
                    mCameraHelper.addSurface(holder.getSurface(), false);
                }
            }

            //Surface发生了改变
            @Override
            public void surfaceChanged(@NonNull SurfaceHolder holder, int format, int width, int height) {
            }

            //销毁了原来的Surface
            @Override
            public void surfaceDestroyed(@NonNull SurfaceHolder holder) {
                if (mCameraHelper != null) {
                    //移除预览Surface
                    mCameraHelper.removeSurface(holder.getSurface());
                }
            }
        });

        mCameraHelper = new CameraHelper();
        //设置UVC摄像头状态回调
        mCameraHelper.setStateCallback(mStateListener);
    }






}
