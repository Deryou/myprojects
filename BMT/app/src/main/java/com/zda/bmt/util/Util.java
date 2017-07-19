package com.zda.bmt.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Environment;
import android.text.TextUtils;
import android.text.format.Time;
import android.util.Log;
import android.widget.Toast;

import com.zda.bmt.gloable.AppConstant;
import com.zda.bmt.gloable.BMTApp;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by MR on 2017/4/28.
 */

public class Util {
    private static final String TAG = "Util";
    public static float dataInfo[];
    public static float dataOne;

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
        data[0] = "SN" + getHotspotName() + "end";
        data[1] = "SS" + getHotspotPwd() + "end";
        data[2] = "SI" + AppConstant.HOTSPOT_IP + "end";
        data[3] = "SP" + AppConstant.HOTSPOT_PORT + "end";
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


    /**
     * 获取数组平均值，并把结果放到最后一个位置上
     *
     * @param data
     * @return
     */
    public static float getAveDelOne(float[] data) {
        float sum = 0;
        for (int i = 0; i < data.length - 1; i++) {
            sum += data[i];
        }
        return Math.round((sum / (data.length - 1)) * 1000) / 1000f;
    }

    /**
     * 获取一个存储float数据的集合的平均值
     * @param data
     * @return
     */
    public static float getListAve(List<Float> data) {
        float sum = 0;
        for (int i = 0; i < data.size(); i++) {
            sum += data.get(i);
        }
        return Math.round((sum / (data.size())) * 1000) / 1000f;
    }


    /***************************************************热点开启工具***************************************************/

    /**
     * 保存热点信息
     *
     * @param hotspotName ssid
     * @param hotspotPwd  密码
     */
    public static void saveHotspotData(String hotspotName, String hotspotPwd) {
        SharedPreferences sharedPreferences = BMTApp.getAppContext()
                .getSharedPreferences(AppConstant.LOCAL_DATA, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(AppConstant.HOTSPOT_NAME, hotspotName);
        editor.putString(AppConstant.HOTSPOT_PWD, hotspotPwd);
        editor.apply();
    }

    public static void saveEquipNum(String equipNum) {
        SharedPreferences sharedPreferences = BMTApp.getAppContext()
                .getSharedPreferences(AppConstant.LOCAL_DATA, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        int num = Integer.parseInt(equipNum);
        if (num > 0 && num < 10) {
            editor.putString(AppConstant.EQUIPMENT_NUMBER, "0" + equipNum);
        } else {
            editor.putString(AppConstant.EQUIPMENT_NUMBER, equipNum);
        }
        editor.apply();
    }

    /**
     * 保存文档数据
     *
     * @param key
     * @param data
     */
    public static void saveToDocData(String key, String data) {
        SharedPreferences sharedPreferences = BMTApp.getAppContext().getSharedPreferences
                (AppConstant.DOC_DATA, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(key, data);
        editor.apply();
    }

    /**
     * 根据键值获取相应的数据
     *
     * @param key 键值
     * @return 返回本地保存的数据，若无返回null
     */
    public static String getSavedDocData(String key) {
        SharedPreferences sharedPreferences = BMTApp.getAppContext().getSharedPreferences
                (AppConstant.DOC_DATA, Context.MODE_PRIVATE);
        return sharedPreferences.getString(key, null);
    }

    /**
     * 清理测试完后保存的数据
     */
    public static void clearDocData() {
        SharedPreferences sharedPreferences = BMTApp.getAppContext().getSharedPreferences
                (AppConstant.DOC_DATA, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.apply();
    }

    /**
     * 获取保存的ssid
     *
     * @return
     */
    public static String getHotspotName() {
        SharedPreferences sharedPreferences = BMTApp.getAppContext()
                .getSharedPreferences(AppConstant.LOCAL_DATA, Context.MODE_PRIVATE);
        return sharedPreferences.getString(AppConstant.HOTSPOT_NAME, "MR-BST");
    }

    /**
     * 获取保存的密码
     *
     * @return
     */
    public static String getHotspotPwd() {
        SharedPreferences sharedPreferences = BMTApp.getAppContext()
                .getSharedPreferences(AppConstant.LOCAL_DATA, Context.MODE_PRIVATE);
        return sharedPreferences.getString(AppConstant.HOTSPOT_PWD, "123456789");
    }

    /**
     * 获取设备号
     *
     * @return
     */
    public static String getEquipNum() {
        SharedPreferences sharedPreferences = BMTApp.getAppContext()
                .getSharedPreferences(AppConstant.LOCAL_DATA, Context.MODE_PRIVATE);
        return sharedPreferences.getString(AppConstant.EQUIPMENT_NUMBER, null);
    }

    /***************************************************文件操作工具***************************************************/
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
        return "" + time.year +  (time.month + 1) + time.monthDay + time.hour + time.minute + time.second;
    }

    /**
     * 获取当前日期
     *
     * @return
     */
    public static String getData() {
        Time time = new Time();
        time.setToNow();
        return time.year + "-" + (time.month + 1) + "-" + time.monthDay;
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
     *
     * @return
     */
    public static String getSDPath(Context context) {
        File sdDir = null;
        boolean sdCardExist = Environment.getExternalStorageState().equals(Environment
                .MEDIA_MOUNTED);
        if (sdCardExist) {
            sdDir = Environment.getExternalStorageDirectory();
        } else {
            Toast.makeText(context, "未检测到外置存储", Toast.LENGTH_SHORT).show();
        }
        return sdDir.toString();
    }

    /**
     * 获取doc样板文件路径
     *
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
            Toast.makeText(context, "未检测到外置存储", Toast.LENGTH_SHORT).show();
        }
        return sdDir.toString();
    }

    /**
     * 把数据写入文件
     *
     * @param file
     * @param inputStream
     * @param append
     * @return
     */
    public static boolean writeToFile(File file, InputStream inputStream, boolean append) {
        OutputStream outputStream = null;
        try {
            makeDirs(file.getAbsolutePath());
            outputStream = new FileOutputStream(file, append);
            byte data[] = new byte[1024];
            int length = -1;
            while ((length = inputStream.read(data)) != -1) {
                outputStream.write(data, 0, length);
            }
            outputStream.flush();
            return true;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
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
     *
     * @param filePath
     * @return
     */
    public static boolean makeDirs(String filePath) {
        String folderName = getFolderName(filePath);
        if (TextUtils.isEmpty(folderName)) {
            return false;
        }
        File folder = new File(folderName);
        return (folder.exists() && folder.isDirectory()) ? true : folder.mkdirs();
    }

    /**
     * 获取文件路径
     *
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

    //*****************************工具方法*******************************
    public static boolean isNum(String data) {
        Pattern pattern = Pattern.compile("-?[0-9]+.?[0-9]+");
        Matcher matcher = pattern.matcher(data);
        if (!matcher.matches()) {
            return false;
        }
        return true;
    }

    public static Map<String, String> getEmptyLightData() {
        Map<String, String> map = new HashMap<>();
        for (int i = 1; i <= 6; i++) {
            map.put("$O_" + i + "_1$", "");
            map.put("$O_" + i + "_2$", "");
            map.put("$O_" + i + "_3$", "");
            map.put("$O_" + i + "_ave$", "");

            map.put("$C_" + i + "_1$", "");
            map.put("$C_" + i + "_2$", "");
            map.put("$C_" + i + "_3$", "");
            map.put("$C_" + i + "_ave$", "");
        }
        map.put("$CAVE_1$", "");
        map.put("$CAVE_2$", "");
        map.put("$CAVE_3$", "");
        map.put("$CAVE_ave$", "");
        map.put("$OAVE_1$", "");
        map.put("$OAVE_2$", "");
        map.put("$OAVE_3$", "");
        map.put("$OAVE_ave$", "");
        return map;
    }

    public static Map<String, String> getEmptyFlowData() {
        Map<String, String> map = new HashMap<>();
        for (int i = 1; i <= 7; i++) {
            map.put("$IA_1_" + i + "$", "");
            map.put("$IA_2_" + i + "$", "");
            map.put("$IA_3_" + i + "$", "");
            map.put("$IA_ave_" + i + "$", "");

            map.put("$DA_1_" + i + "$", "");
            map.put("$DA_2_" + i + "$", "");
            map.put("$DA_3_" + i + "$", "");
            map.put("$DA_ave_" + i + "$", "");
        }
        map.put("$IA_ave_ave$", "");
        map.put("$DA_ave_ave$", "");
        return map;
    }

    public static Map<String, String> getEmptyTPData() {
        Map<String, String> map = new HashMap<>();
        map.put("$PA_1$", "");
        map.put("$PA_2$", "");
        map.put("$PA_3$", "");
        map.put("$PA_ave$", "");
        map.put("$OBT$", "");
        map.put("$OAT$", "");
        map.put("$T_ave$", "");
        return map;
    }

    /**
     * 获取空振动数据，填入表格
     * @return
     */
    public static Map<String, String> getEmptySKData() {
        Map<String, String> map = new HashMap<>();
        map.put("$OBS$", "");
        map.put("$OAS$", "");
        map.put("$S_ave$", "");
        return map;
    }
}
