package com.hazz.aipick.utils;

import android.content.Context;
import android.content.res.AssetManager;
import android.text.TextUtils;
import android.util.Log;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class AssetsUtil {
    public static byte[] getAssertsFileInBytes(Context context, String assetsFileName) {
        InputStream is;
        try {
            is = context.getResources().getAssets().open(assetsFileName);
            int ch = 0;
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            while ((ch = is.read()) != -1) {
                out.write(ch);
            }
            out.close();
            is.close();
            byte[] buff = out.toByteArray();
            return buff;

        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    /**
     * @param assetsPath 注意一定不是"/" 如果读取assets根目录下的东西 传入""即可
     * @return
     * @title: getAllAssetsList
     * @description: TODO
     * @return: String[]
     */
    public static String[] getAllAssetsList(Context context, String assetsPath) {
        AssetManager asserter = context.getAssets();
        String[] fileNames = null;
        try {
            fileNames = asserter.list(assetsPath);
            for (String fileName : fileNames) {
                Log.d("assetUtil", fileName);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return fileNames;
    }

    /**
     * 从assets目录中复制整个文件夹内容
     *
     * @param context    Context 使用CopyFiles类的Activity
     * @param assetsPath String 原文件路径 如：/aa , 如果读取assets根目录下的东西 请传入""
     * @param newPath    String 复制后路径 如：xx:/bb/cc
     */
    public static boolean copyFilesFassets(Context context, String assetsPath, String newPath) {

        try {
            String fileNames[] = context.getAssets().list(assetsPath);//获取assets目录下的所有文件及目录名
            if (fileNames.length > 0) {
                File file = new File(newPath);
                file.mkdirs();
                for (String fileName : fileNames) {
                    if (!TextUtils.isEmpty(assetsPath)) {
                        assetsPath += "/";
                    }
                    copyFilesFassets(context, assetsPath + fileName, newPath + "/" + fileName);
                }
            } else {//如果是文件
                InputStream is = context.getAssets().open(assetsPath);
                FileOutputStream fos = new FileOutputStream(new File(newPath));
                byte[] buffer = new byte[1024];
                int byteCount = 0;
                while ((byteCount = is.read(buffer)) != -1) {//循环从输入流读取 buffer字节        
                    fos.write(buffer, 0, byteCount);//将读取的输入流写入到输出流
                }
                fos.flush();//刷新缓冲区
                is.close();
                fos.close();
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}