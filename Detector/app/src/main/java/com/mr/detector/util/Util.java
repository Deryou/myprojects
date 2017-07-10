package com.mr.detector.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Environment;
import android.text.TextUtils;
import android.text.format.Time;
import android.util.Log;
import android.widget.Toast;

import com.mr.detector.gloable.AppConstants;
import com.mr.detector.gloable.DetectorApplication;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Created by MR on 2017/4/28.
 */

public class Util {
    private static final String TAG = "Util";
    public static float dataInfo[];

    /**
     * 获取发送给BMT的配置信息
     *
     * @param machineNum 机器号
     * @return
     */
    public static String[] getSendData(int machineNum) {
//        final String[] data = {"ST01NMRend", "ST01S123456789end", "ST01I"
//                + AppConstants.SERVER_IP + "end", "ST01P12355end"};
        String[] data = new String[4];
        data[0] = "N" + getHotspotName() + "end";
        data[1] = "S" + getHotspotPwd() + "end";
        data[2] = "I" + AppConstants.HOTSPOT_IP + "end";
        data[3] = "P" + AppConstants.HOTSPOT_PORT + "end";
        for (int i = 0; i < data.length; i++) {
            if (machineNum >= 0 && machineNum < 10) {
                data[i] = "ST0" + machineNum + data[i];
            } else if (machineNum > 9) {
                data[i] = "ST" + machineNum + data[i];
            }
            Log.e(TAG, "getSendData:测试编码数据 " + data[i]);
        }
        return data;
    }

    public static float[] getTempArray(String data) {
        //ST01  TA22.74C  TB22.74C  TC22.56C  TD22.64C  TE22.41C  TH68H  DL001C  end
        dataInfo = new float[7];
        dataInfo[AppConstants.EQUIP_TA] = Float.valueOf(data.substring(6, 11));
        dataInfo[AppConstants.EQUIP_TB] = Float.valueOf(data.substring(14, 19));
        dataInfo[AppConstants.EQUIP_TC] = Float.valueOf(data.substring(22, 27));
        dataInfo[AppConstants.EQUIP_TD] = Float.valueOf(data.substring(30, 35));
        dataInfo[AppConstants.EQUIP_TE] = Float.valueOf(data.substring(38, 43));
        dataInfo[AppConstants.EQUIP_TH] = Float.valueOf(data.substring(46, 48));
        dataInfo[AppConstants.EQUIP_TO] = Float.valueOf(data.substring(51, 54));
        Log.e(TAG, "getTempArray: " + dataInfo.toString() + "之前的数据" + data);
        return dataInfo;
    }

    /***************************************************热点开启工具***************************************************/

    /**
     * 保存热点信息
     *
     * @param hotspotName ssid
     * @param hotspotPwd  密码
     */
    public static void saveHotspotData(String hotspotName, String hotspotPwd) {
        SharedPreferences sharedPreferences = DetectorApplication.getAppContext()
                .getSharedPreferences(AppConstants.LOCAL_DATA, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(AppConstants.HOTSPOT_NAME, hotspotName);
        editor.putString(AppConstants.HOTSPOT_PWD, hotspotPwd);
        editor.commit();
    }

    /**
     * 获取保存的ssid
     *
     * @return
     */
    public static String getHotspotName() {
        SharedPreferences sharedPreferences = DetectorApplication.getAppContext()
                .getSharedPreferences(AppConstants.LOCAL_DATA, Context.MODE_PRIVATE);
        return sharedPreferences.getString(AppConstants.HOTSPOT_NAME, "MR");
    }

    /***************************************************文件操作工具***************************************************/

    /**
     * 获取保存的密码
     *
     * @return
     */
    public static String getHotspotPwd() {
        SharedPreferences sharedPreferences = DetectorApplication.getAppContext()
                .getSharedPreferences(AppConstants.LOCAL_DATA, Context.MODE_PRIVATE);
        return sharedPreferences.getString(AppConstants.HOTSPOT_PWD, "123456789");
    }

    /**
     * 保存文件到私有路径，卸载或清除数据时消失
     *
     * @param data
     * @param type
     * @param fileName
     * @param context
     * @return
     */
    public static boolean saveFormData(byte[] data, String type, String fileName, Context context) {
        BufferedOutputStream bos = null;
        if (isSDCardMounted()) {
            File file = context.getExternalFilesDir(type);
            try {
                bos = new BufferedOutputStream(new FileOutputStream(new File(file, fileName)));
                bos.write(data);
                bos.flush();
                return true;
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    /**
     * 判断当前SD卡是否挂载
     *
     * @return
     */
    public static boolean isSDCardMounted() {
        return Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED);
    }

    /**
     * 获取当前时间
     *
     * @return 返回字符串时间
     */
    public static String getNowTime() {
        Time time = new Time();
        time.setToNow();
        return "" + time.year + time.month + time.monthDay + time.hour + time.minute + time.second;
    }

    /**
     * 读取文件的最后修改时间的方法
     */
    public static String getFileLastModifiedTime(File f) {
        Calendar cal = Calendar.getInstance();
        long time = f.lastModified();
        SimpleDateFormat formatter = new
                SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        cal.setTimeInMillis(time);
        return formatter.format(cal.getTime());
    }

    /**
     * 获取文件大小，格式化输出
     *
     * @param fileS
     * @return
     */
    public static String FormetFileSize(long fileS) {
        DecimalFormat df = new DecimalFormat("#.00");
        String fileSizeString = "";
        String wrongSize = "0B";
        if (fileS == 0) {
            return wrongSize;
        }
        if (fileS < 1024) {
            fileSizeString = df.format((double) fileS) + "B";
        } else if (fileS < 1048576) {
            fileSizeString = df.format((double) fileS / 1024) + "KB";
        } else if (fileS < 1073741824) {
            fileSizeString = df.format((double) fileS / 1048576) + "MB";
        } else {
            fileSizeString = df.format((double) fileS / 1073741824) + "GB";
        }
        return fileSizeString;
    }

    /**
     * 获取外置内存卡的路径
     * @return
     */
    public static String getSDPaht(Context context) {
        File sdDir = null;
        boolean sdCardExist = Environment.getExternalStorageState().equals(Environment
                .MEDIA_MOUNTED);
        if (sdCardExist) {
            sdDir = Environment.getExternalStorageDirectory();
        } else {
            Toast.makeText(context,"未检测到外置存储",Toast.LENGTH_SHORT).show();
        }
        return sdDir.toString();
    }

    /**
     * 获取doc样板文件路径
     * @param context
     * @return
     */
    public static String getDemoDocPath(Context context) {
        File sdDir = null;
        boolean sdCardExist = Environment.getExternalStorageState().equals(Environment
                .MEDIA_MOUNTED);
        if (sdCardExist) {
            sdDir = context.getExternalFilesDir(null);
        } else {
            Toast.makeText(context,"未检测到外置存储",Toast.LENGTH_SHORT).show();
        }
        return sdDir.toString();
    }

    /**
     * 把数据写入文件
     * @param file
     * @param inputStream
     * @param append
     * @return
     */
    public static boolean writeToFile(File file, InputStream inputStream,boolean append) {
        OutputStream outputStream = null;
        try {
            makeDirs(file.getAbsolutePath());
            outputStream = new FileOutputStream(file,append);
            byte data[] = new byte[1024];
            int length = -1;
            while ((length = inputStream.read(data)) != -1) {
                outputStream.write(data,0,length);
            }
            outputStream.flush();
            return true;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            if (outputStream != null) {
                try {
                    outputStream.close();
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return false;
    }

    /**
     * 创建文件夹
     * @param filePath
     * @return
     */
    public static boolean makeDirs(String filePath) {
        String folderName = getFolderName(filePath);
        if (TextUtils.isEmpty(folderName)) {
            return false;
        }
        File folder = new File(folderName);
        return (folder.exists()&&folder.isDirectory())?true:folder.mkdirs();
    }

    /**
     * 获取文件路径
     * @param filePath
     * @return
     */
    public static String getFolderName(String filePath) {
        if (TextUtils.isEmpty(filePath)) {
            return filePath;
        }
        int filePosition = filePath.lastIndexOf(File.separator);
        return (filePosition == -1) ? "" : filePath.substring(0, filePosition);
    }

    /**********************************word文档操作部分***********************************************/
//
//    public static void writeToDoc(File file, File reportFile,Map<String, String> map) {
//        try {
//            FileInputStream in = new FileInputStream(file);
//            XWPFDocument xwp = new XWPFDocument(in);
//            List<XWPFTable> tabList = xwp.getTables();
//            for (int i = 0; i<tabList.size();i++) {
//                switch (i) {
//                    case 0:
//                        XWPFTable table = tabList.get(i);
//                        break;
//                    case 1:
//                        break;
//                    case 2:
//                        break;
//                }
//            }
//        } catch (FileNotFoundException e) {
//            e.printStackTrace();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }

//    }
}
