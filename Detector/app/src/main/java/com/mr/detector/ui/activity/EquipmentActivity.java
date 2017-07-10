package com.mr.detector.ui.activity;

import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RadioButton;

import com.github.mikephil.charting.data.Entry;
import com.mr.detector.R;
import com.mr.detector.adapter.ClientAdapter;
import com.mr.detector.bean.BottomTabData;
import com.mr.detector.bean.ReportData;
import com.mr.detector.callback.DataResultCallback;
import com.mr.detector.callback.EquipmentCallback;
import com.mr.detector.callback.HumidtyCallback;
import com.mr.detector.callback.OxygenCallback;
import com.mr.detector.callback.ServerCallback;
import com.mr.detector.callback.TemperatureCallback;
import com.mr.detector.gloable.AppConstants;
import com.mr.detector.ui.fragment.BaseFragment;
import com.mr.detector.ui.fragment.HumidityFragment;
import com.mr.detector.ui.fragment.OxygenFragment;
import com.mr.detector.ui.fragment.TemperatureFragment;
import com.mr.detector.util.BackPressedUtil;
import com.mr.detector.util.TcpClient;
import com.mr.detector.util.TcpServer;
import com.mr.detector.util.Util;
import com.mr.detector.widget.MsgDialog;

import org.apache.poi.hwpf.HWPFDocument;
import org.apache.poi.hwpf.usermodel.Range;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 设备数据处理页
 */
public class EquipmentActivity extends BaseActivity implements ServerCallback, DataResultCallback {
    private static final String TAG = "EquipmentActivity";
    //坐标点数据集合
    public static List<List<Entry>> mEntries;
    public static int TIME_GAP = 4;
    //时间计数器
    public static int timeCount = 0;
    //用于判断是否开始
    public static boolean isStart = false;
    public static ReportData mReportData;
    public List<Entry> mEntryItem;
    public float[] tempData;
    //当前的fragment
    protected Fragment currentFragment = null;
    @BindView(R.id.top_area)
    LinearLayout mTopArea;
    @BindView(R.id.time_gap)
    EditText mTimeGap;
    @BindView(R.id.type_32)
    RadioButton mType32;
    @BindView(R.id.type_36)
    RadioButton mType36;
    @BindView(R.id.btn_start)
    Button mStart;
    @BindView(R.id.home_container)
    FrameLayout mContainer;
    @BindView(R.id.bottom_tab)
    TabLayout mBottomTab;
    //检测类型，默认32℃
    private String tempType = "32_";
    //保存文件数据
    private AlertDialog mSaveReport;
    private EditText mFileName;
    //回调
    private EquipmentCallback mEquipmentCallback;
    private TemperatureCallback mTemperatureCallback;
    private HumidtyCallback mHumidtyCallback;
    private OxygenCallback mOxygenCallback;
    private BaseFragment mBaseFragment;
    //所有的fragment集合
    private Fragment[] mFragments;
    private AlertDialog mEquipmentDialog;
    private LinearLayout mEquipLayout;
    private RecyclerView mEquipRecycler;
    private ClientAdapter adapter;
    private List<TcpClient> mClientList;
    //存放客户端接收的数据
    private HashMap<String, List<List<Entry>>> receivedData;
    //当前选中的客户端
    private TcpClient currentClient;
    private TcpServer mServer;
    private Map<String, String> map;
    //退出提示dialog
    private MsgDialog mMsgDialog;

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            //所有数据都更新
            mTemperatureCallback.setData(tempData, tempType);
            mHumidtyCallback.setData(tempData, tempType);
            mOxygenCallback.setData(tempData, tempType);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initFragmentData();
        initView();
    }

    private void initFragmentData() {
        mFragments = BottomTabData.getFragments();
        mEquipmentCallback = (EquipmentCallback) mFragments[AppConstants.EQUIPMENTFRAGMENT];
        mTemperatureCallback = (TemperatureCallback) mFragments[AppConstants.TEMPERATUREFRAGMENT];
        mBaseFragment = (BaseFragment) mFragments[AppConstants.TEMPERATUREFRAGMENT];
        mBaseFragment.setDataResultCallback(this);
        mHumidtyCallback = (HumidtyCallback) mFragments[AppConstants.HUMIDITYFRAGMENT];
        mOxygenCallback = (OxygenCallback) mFragments[AppConstants.OXYGENFRAGMENT];
        mReportData = new ReportData();
    }

    private void initView() {
        mBottomTab.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                onTabItemSelected(tab.getPosition());

                //改变Tab状态
                for (int i = 0; i < mBottomTab.getTabCount(); i++) {
                    if (i == tab.getPosition()) {
                        mBottomTab.getTabAt(i).setIcon(getResources().getDrawable(BottomTabData
                                .mTabResPressed[i]));
                    } else {
                        mBottomTab.getTabAt(i).setIcon(getResources().getDrawable(BottomTabData
                                .mTabRes[i]));
                    }
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
        mBottomTab.addTab(mBottomTab.newTab().setIcon(getResources().getDrawable(R.mipmap
                .icon_tab_form_select)).setText(BottomTabData.mTableTitle[0]));
        mBottomTab.addTab(mBottomTab.newTab().setIcon(getResources().getDrawable(R.mipmap
                .icon_tab_temp_normal)).setText(BottomTabData.mTableTitle[1]));
        mBottomTab.addTab(mBottomTab.newTab().setIcon(getResources().getDrawable(R.mipmap
                .icon_tab_humdity_normal)).setText(BottomTabData.mTableTitle[2]));
        mBottomTab.addTab(mBottomTab.newTab().setIcon(getResources().getDrawable(R.mipmap
                .icon_tab_oxygen_normal)).setText(BottomTabData.mTableTitle[3]));
        initEquipmentDialog();

        mType32.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    mTemperatureCallback.tempStateChanged("32");
                }
            }
        });
        mType36.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    mTemperatureCallback.tempStateChanged("36");
                }
            }
        });
    }

    private void initEquipmentDialog() {
        //初始化设备列表数据
        mClientList = new ArrayList<>();
        mEquipLayout = (LinearLayout) getLayoutInflater().inflate(R.layout.select_equipment, null);
        mEquipRecycler = (RecyclerView) mEquipLayout.findViewById(R.id
                .equipment_select_recycler_view);
        mEquipRecycler.setLayoutManager(new LinearLayoutManager(this));
        mEquipRecycler.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration
                .HORIZONTAL));
        adapter = new ClientAdapter(this, mClientList);
        mEquipRecycler.setAdapter(adapter);

        //初始化保存文件数据
        mFileName = (EditText) getLayoutInflater().inflate(R.layout.dialog_with_edittext, null);
    }

    @Override
    protected void onStart() {
        super.onStart();
        initData();
    }

    @Override
    protected void onDestroy() {
        //关闭连接
        if (mServer != null) {
            mServer.disconnect();
        }
        if (mEntries != null) {
            mEntries = null;
        }
        if (mEntryItem != null) {
            mEntryItem = null;
        }
        if (mClientList != null) {
            mClientList = null;
        }
        super.onDestroy();
    }

    private void onTabItemSelected(int position) {

        switch (position) {
            case 0:
                currentFragment = mFragments[0];
                mTopArea.setVisibility(View.GONE);
                break;
            case 1:
                currentFragment = mFragments[1];
                mTopArea.setVisibility(View.VISIBLE);
                break;
            case 2:
                currentFragment = mFragments[2];
                mTopArea.setVisibility(View.VISIBLE);
                break;
            case 3:
                currentFragment = mFragments[3];
                mTopArea.setVisibility(View.VISIBLE);
                break;
            default:
                break;
        }
        if (currentFragment != null) {
            for (Fragment fragment : mFragments) {
                getSupportFragmentManager().beginTransaction().hide(fragment).commit();
            }
            if (!currentFragment.isAdded()) {
                getSupportFragmentManager().beginTransaction().add(R.id.home_container,
                        currentFragment).commit();
            }
            getSupportFragmentManager().beginTransaction().show(currentFragment).commit();
        }
    }

    private void initData() {
        //初始化数据存储容器
        mEntries = new ArrayList<>();
        for (int i = 0; i < 7; i++) {
            mEntryItem = new ArrayList<>();
            mEntries.add(mEntryItem);
        }

        receivedData = new HashMap<>();
        //设置监听端口
        mServer = TcpServer.getInstance(AppConstants.HOTSPOT_PORT);
        //设置连接回调
        mServer.setServerCallback(this);
        //开始监听
        mServer.connect();
    }

    @Override
    public void initToolbar() {
        super.initToolbar();
        mCustomTitle.setText("选择设备");
        mCustomTitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mEquipmentDialog == null) {
                    mEquipmentDialog = new AlertDialog.Builder(EquipmentActivity.this)
                            .setTitle("选择设备")
                            .setView(mEquipLayout)
                            .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                }
                            }).setNegativeButton("取消", null)
                            .create();
                }
                mEquipmentDialog.show();
            }
        });
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_equipment, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.data_clear:
                if (currentFragment instanceof TemperatureFragment) {
                    mTemperatureCallback.clearData();
                } else if (currentFragment instanceof HumidityFragment) {
                    mHumidtyCallback.clearData();
                } else if (currentFragment instanceof OxygenFragment) {
                    mOxygenCallback.clearData();
                }
                break;
            case R.id.create_report:
                final String dataTime = Util.getNowTime();
                mFileName.setText(dataTime);
                if (mSaveReport == null) {
                    mSaveReport = new AlertDialog.Builder(this)
                            .setTitle("请输入保存文件名称")
                            .setView(mFileName)
                            .setPositiveButton("保存", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    //在设备fragment中具体实现，由于四个页面都要可以生成报告，所以废弃
//                                    mFormCreateCallback.createForm(mFileName.getText().toString()
//                                            , dataTime);
                                    try {
                                        InputStream inputStream = getAssets().open("report.doc");
                                        String demoPath = Util.getDemoDocPath(EquipmentActivity
                                                .this)
                                                + "temp" + "Temporary_file.doc";
                                        String reportPath = Util.getSDPaht(EquipmentActivity.this)
                                                + AppConstants.REPORT_FOLDER + mFileName.getText
                                                ().toString() + ".doc";
                                        File demoFile = new File(demoPath);
                                        File reportFile = new File(reportPath);
                                        if (Util.writeToFile(demoFile, inputStream,
                                                false)) {
                                            writeDataToDoc(demoFile, reportFile);
                                            toast("保存成功");
                                        } else {
                                            toast("文件写入失败");
                                        }
                                        mTemperatureCallback.dataClear();
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    }
                                }
                            })
                            .setNegativeButton("取消", null)
                            .create();
                }
                mSaveReport.show();
                break;
            case R.id.check_report:
                ReportFileActivity.startActivity(this);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * 保存数据到word文档中
     *
     * @param file       模板文件
     * @param reportFile 最终需要的word文件
     */
    private void writeDataToDoc(File file, File reportFile) {
        map = new HashMap<>();
        //把表单数据填充
        map.putAll(mEquipmentCallback.getEqumentReport());
        Map<String, List<Map<String, String>>> dataTemp = mTemperatureCallback.getTempReport();
        List<Map<String, String>> mapList = null;
        Map<String, String> tempMap;
        int tempSize;
        String equipType = null;
        float[] aveArray;
        float[] tempArray;
        float sSum = 0;
        float rSum = 0;
        int datasize = 1;
        int showsize = 1;


        if (!(dataTemp != null && dataTemp.size() > 0)) {
            dataTemp = new HashMap<>();
            List<Map<String, String>> temp_32_list = new ArrayList<>();
            List<Map<String, String>> temp_36_list = new ArrayList<>();
            for (int i = 0; i < 6; i++) {
                temp_32_list.add(new HashMap<String, String>());
                temp_36_list.add(new HashMap<String, String>());
            }
            dataTemp.put(AppConstants.TYPE_32, temp_32_list);
            dataTemp.put(AppConstants.TYPE_36, temp_36_list);
            int equipNum = 65;
            for (int i = 0; i < 6; i++) {
                for (int j = 1; j < 16; j++) {
                    if (i < 5) {
                        if (j < 10) {
                            dataTemp.get(AppConstants.TYPE_32).get(i).put("T" + (char) (equipNum
                                    + i) +
                                    AppConstants.TYPE_32 +
                                    "0" + j, "");
                            dataTemp.get(AppConstants.TYPE_36).get(i).put("T" + (char) (equipNum
                                    + i) +
                                    AppConstants.TYPE_36 +
                                    "0" + j, "");
                        } else {
                            dataTemp.get(AppConstants.TYPE_32).get(i).put("T" + (char) (equipNum
                                    + i) +
                                    AppConstants.TYPE_32 + j, "");
                            dataTemp.get(AppConstants.TYPE_36).get(i).put("T" + (char) (equipNum
                                    + i) +
                                    AppConstants.TYPE_36 + j, "");
                        }
                    } else {
                        if (j < 10) {
                            dataTemp.get(AppConstants.TYPE_32).get(i).put("ST" + AppConstants
                                    .TYPE_32 + "0" + j, "");
                            dataTemp.get(AppConstants.TYPE_36).get(i).put("ST" + AppConstants
                                    .TYPE_36 + "0" + j, "");
                        } else {
                            dataTemp.get(AppConstants.TYPE_32).get(i).put("ST" + AppConstants
                                    .TYPE_32 + j, "");
                            dataTemp.get(AppConstants.TYPE_36).get(i).put("ST" + AppConstants
                                    .TYPE_36 + j, "");
                        }
                    }
                }
            }
            map.put("ST" + AppConstants.TYPE_32 + "AVE", "");
            map.put("ST" + AppConstants.TYPE_36 + "AVE", "");
            map.put("TA" + AppConstants.TYPE_32 + "AVE", "");
            map.put("TA" + AppConstants.TYPE_36 + "AVE", "");
            map.put("TB" + AppConstants.TYPE_32 + "AVE", "");
            map.put("TB" + AppConstants.TYPE_36 + "AVE", "");
            map.put("TC" + AppConstants.TYPE_32 + "AVE", "");
            map.put("TC" + AppConstants.TYPE_36 + "AVE", "");
            map.put("TD" + AppConstants.TYPE_32 + "AVE", "");
            map.put("TD" + AppConstants.TYPE_36 + "AVE", "");
            map.put("TE" + AppConstants.TYPE_32 + "AVE", "");
            map.put("TE" + AppConstants.TYPE_36 + "AVE", "");
        }

        for (Map.Entry<String, List<Map<String, String>>> entry : dataTemp.entrySet()) {
            if (entry.getKey().equals(AppConstants.TYPE_32)) {
                mapList = entry.getValue();
            } else if (entry.getKey().equals(AppConstants.TYPE_36)) {
                mapList = entry.getValue();
            }
            if (mapList != null) {
                aveArray = new float[mapList.size()];
                float tempMax = 0;
                float aveMax = 0;
                for (int i = 0; i < mapList.size(); i++) {
                    tempMap = mapList.get(i);
                    tempArray = new float[15];
                    int count = 0;
                    float sum = 0;
                    for (Map.Entry<String, String> tempEntry : tempMap.entrySet()) {
                        if (!tempEntry.getValue().isEmpty()) {
                            tempArray[count] = Float.parseFloat(tempEntry.getValue());
                            //得到
                            if (i == 0 && tempMax < tempArray[count]) {
                                tempMax = tempArray[count];
                            }
                            count++;
                        }
                    }
                    for (int j = 0; j < tempArray.length; j++) {
                        sum += tempArray[j];
                    }
                    //把得到的平均值放入数组
                    aveArray[i] = count == 0 ? sum : sum / count;
                    //求数组中的最大值
                    if (aveMax < aveArray[i]) {
                        aveMax = aveArray[i];
                    }
                    //得到平均数中的最大值
                    tempSize = tempMap.size();
                    if (tempSize < 16) {
                        for (int j = tempSize + 1; j < 16; j++) {
                            switch (i) {
                                case 0:
                                    equipType = "TA";
                                    break;
                                case 1:
                                    equipType = "TB";
                                    break;
                                case 2:
                                    equipType = "TC";
                                    break;
                                case 3:
                                    equipType = "TD";
                                    break;
                                case 4:
                                    equipType = "TE";
                                    break;
                                case 5:
                                    equipType = "ST";
                                    break;
                            }
                            if (j < 10) {
                                tempMap.put(equipType + entry.getKey() + "0" + j, "");
                            } else {
                                tempMap.put(equipType + entry.getKey() + j, "");
                            }
                        }
                    }
                    map.putAll(tempMap);
                }
                map.put(entry.getKey() + "PC", String.valueOf(aveArray[5] - aveArray[0]));
                map.put(entry.getKey() + "BD", String.valueOf(tempMax - aveArray[0]));
                map.put(entry.getKey() + "JY", String.valueOf(aveMax - aveArray[0]));
                if (entry.getKey().equals(AppConstants.TYPE_32)) {
                    map.put("PKC", String.valueOf(aveArray[0] - 32));
                } else if (entry.getKey().equals(AppConstants.TYPE_36)){
                    map.put("PKC", String.valueOf(aveArray[0] - 36));
                    if (tempMax == 0) {
                        map.put("PYX_M_V", "");
                        map.put("CTL", "");
                    } else {
                        map.put("PYX_M_V", String.valueOf(tempMax));
                        map.put("CTL", String.valueOf(tempMax - 36));
                    }
                }

                for (int j = 0; j < 6; j++) {
                    switch (j) {
                        case 0:
                            if (aveArray[j] == 0.0) {
                                map.put("TA" + entry.getKey() + "AVE", "");
                            } else {
                                map.put("TA" + entry.getKey() + "AVE", String.valueOf
                                        (aveArray[j]));
                            }
                            break;
                        case 1:
                            if (aveArray[j] == 0.0) {
                                map.put("TB" + entry.getKey() + "AVE", "");
                            } else {
                                map.put("TB" + entry.getKey() + "AVE", String.valueOf
                                        (aveArray[j]));
                            }
                            break;
                        case 2:
                            if (aveArray[j] == 0.0) {
                                map.put("TC" + entry.getKey() + "AVE", "");
                            } else {
                                map.put("TC" + entry.getKey() + "AVE", String.valueOf
                                        (aveArray[j]));
                            }
                            break;
                        case 3:
                            if (aveArray[j] == 0.0) {
                                map.put("TD" + entry.getKey() + "AVE", "");
                            } else {
                                map.put("TD" + entry.getKey() + "AVE", String.valueOf
                                        (aveArray[j]));
                            }

                            break;
                        case 4:
                            if (aveArray[j] == 0.0) {
                                map.put("TE" + entry.getKey() + "AVE", "");
                            } else {
                                map.put("TE" + entry.getKey() + "AVE", String.valueOf
                                        (aveArray[j]));
                            }
                            break;
                        case 5:
                            if (aveArray[j] == 0.0) {
                                map.put("ST" + entry.getKey() + "AVE", "");
                            } else {
                                map.put("ST" + entry.getKey() + "AVE", String.valueOf
                                        (aveArray[j]));
                            }
                            break;
                        default:
                            break;
                    }
                }
            }
        }

        //湿度数据
        tempMap = mHumidtyCallback.getHumData();
        showsize = 0;
        datasize = 0;
        if (!(tempMap != null && tempMap.size() > 0)) {
            tempMap = new HashMap<>();
            for (int i = 1; i < 4; i++) {
                tempMap.put("SH0" + i, "");
                tempMap.put("TH0" + i, "");
            }
            tempMap.put("SH_AVE", "");
            tempMap.put("TH_AVE", "");
        }
        sSum = 0;
        rSum = 0;
        float aveShum = 0;
        float aveThum = 0;
        if (tempMap.size() < AppConstants.HUMIDITY_NUM * 2 + 1) {
            for (int i = 1; i <= AppConstants.HUMIDITY_NUM; i++) {
                if (!tempMap.containsKey("SH0" + i)) {
                    tempMap.put("SH0" + i, "");
                } else {
                    if (!tempMap.get("SH0" + i).isEmpty()) {
                        sSum += Float.parseFloat(tempMap.get("SH0" + i));
                        showsize++;
                    }
                }
                if (!tempMap.containsKey("TH0" + i)) {
                    tempMap.put("TH0" + i, "");
                } else {
                    if (!tempMap.get("TH0" + i).isEmpty()) {
                        rSum += Float.parseFloat(tempMap.get("TH0" + i));
                        datasize++;
                    }
                }
            }
        }
        aveShum = showsize == 0 ? sSum : sSum / showsize;
        tempMap.put("SH_AVE", String.valueOf(aveShum));
        aveThum = datasize == 0 ? rSum : rSum / datasize;
        tempMap.put("TH_AVE", String.valueOf(aveThum));
        tempMap.put("HUM_ERR", String.valueOf(aveShum - aveThum));
        map.putAll(tempMap);

        //氧浓度数据
        tempMap = mOxygenCallback.getOxygendata();
        showsize = 0;
        datasize = 0;
        if (!(tempMap != null && tempMap.size() > 0)) {
            tempMap = new HashMap<>();
            for (int i = 1; i < 4; i++) {
                tempMap.put("SO0" + i, "");
                tempMap.put("TO0" + i, "");
            }
            tempMap.put("SO_AVE", "");
            tempMap.put("TO_AVE", "");
        }
        sSum = 0;
        rSum = 0;
        if (tempMap.size() < AppConstants.OXYGEN_NUM * 2) {
            for (int i = 1; i <= AppConstants.OXYGEN_NUM; i++) {
                if (!tempMap.containsKey("SO0" + i)) {
                    tempMap.put("SO0" + i, "");
                } else {
                    if (!tempMap.get("SO0" + i).isEmpty()) {
                        sSum += Float.parseFloat(tempMap.get("SO0" + i));
                        showsize++;
                    }
                }
                if (!tempMap.containsKey("TO0" + i)) {
                    tempMap.put("TO0" + i, "");
                } else {
                    if (!tempMap.get("TO0" + i).isEmpty()) {
                        rSum += Float.parseFloat(tempMap.get("TO0" + i));
                        datasize++;
                    }
                }
            }
        }

        tempMap.put("SO_AVE", String.valueOf(showsize == 0 ? sSum : sSum / showsize));
        tempMap.put("TO_AVE", String.valueOf(datasize == 0 ? rSum : rSum / datasize));
        map.putAll(tempMap);
        try {
            FileInputStream in = new FileInputStream(file);
            HWPFDocument hdt = new HWPFDocument(in);
            Range range = hdt.getRange();
            for (Map.Entry<String, String> entry : map.entrySet()) {
                range.replaceText(entry.getKey(), entry.getValue() == null ? "空" : entry.getValue
                        ());
            }
            ByteArrayOutputStream ostream = new ByteArrayOutputStream();
            FileOutputStream out = new FileOutputStream(reportFile);
            hdt.write(ostream);
            out.write(ostream.toByteArray());
            out.close();
            ostream.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    @Override
    protected String setToolbarTitle() {
        return "";
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_equipment;
    }

    /**
     * 有新客户端连接后的回调方法
     *
     * @param client
     */
    @Override
    public void ClientConnected(TcpClient client) {
        mClientList.add(client);
        receivedData.put(client.mConnectId, mEntries);
        if (currentClient == null) {
            currentClient = client;
        }
        refreshClientList();
    }

    private void refreshClientList() {
        adapter.notifyDataSetChanged();
    }

    /**
     * 数据接收处理
     *
     * @param client 客户端
     * @param data   接收的数据
     */
    @Override
    public void OnDataReceived(TcpClient client, byte[] data) {
        String sendData = new String(data);
        tempData = Util.getTempArray(sendData);
        mEntries.get(AppConstants.EQUIP_TA).add(new Entry(timeCount, tempData[AppConstants
                .EQUIP_TA]));
        mEntries.get(AppConstants.EQUIP_TB).add(new Entry(timeCount, tempData[AppConstants
                .EQUIP_TB]));
        mEntries.get(AppConstants.EQUIP_TC).add(new Entry(timeCount, tempData[AppConstants
                .EQUIP_TC]));
        mEntries.get(AppConstants.EQUIP_TD).add(new Entry(timeCount, tempData[AppConstants
                .EQUIP_TD]));
        mEntries.get(AppConstants.EQUIP_TE).add(new Entry(timeCount, tempData[AppConstants
                .EQUIP_TE]));
        mEntries.get(AppConstants.EQUIP_TH).add(new Entry(timeCount, tempData[AppConstants
                .EQUIP_TH]));
        mEntries.get(AppConstants.EQUIP_TO).add(new Entry(timeCount, tempData[AppConstants
                .EQUIP_TO]));
        //坐标X轴值
        timeCount += TIME_GAP;
        if ((currentClient != null) && currentClient.mConnectId.equalsIgnoreCase(client
                .mConnectId)) {
            mHandler.sendEmptyMessage(0);
        }
    }

    @Override
    public void onBackPressed() {
        if (BackPressedUtil.handleFragmentBack(this, BackPressedUtil.MODE_NOTUSE_VIEWPAGER)) {
            mMsgDialog = new MsgDialog(getContext());
            mMsgDialog.setTitle("提示");
            mMsgDialog.setMessage("数据采集中，确定要退出？");
            mMsgDialog.setOnEnsureClickListener("确定", new MsgDialog.onEnsureClickListener() {
                @Override
                public void onEnsureClick() {
                    //TODO 数据保存到word处理
                    finish();
                    mMsgDialog.dismiss();
                }
            });
            mMsgDialog.setOnCancelClickListener("取消", new MsgDialog.onCancelClickListener() {
                @Override
                public void onCancelClick() {
                    mMsgDialog.dismiss();
                }
            });
            mMsgDialog.show();
        } else {
            super.onBackPressed();
        }
    }

    /**
     * 设置控件的状态
     *
     * @param state
     */
    public void setEditState(boolean state) {
        mTimeGap.setEnabled(state);
        mType32.setEnabled(state);
        mType36.setEnabled(state);
        mStart.setEnabled(state);
    }

    @OnClick(R.id.btn_start)
    public void click() {
        isStart = true;
        toast("开始数据收集");
        TIME_GAP = Integer.parseInt(mTimeGap.getText().toString());
        if (TIME_GAP < 4) {
            mTimeGap.setText("4");
            TIME_GAP = 4;
        }
        if (mType32.isChecked()) {
            tempType = AppConstants.TYPE_32;
        } else if (mType36.isChecked()) {
            tempType = AppConstants.TYPE_36;
        }
        setEditState(!isStart);
    }

    @Override
    public void onDataRecEnd() {
        setEditState(true);
    }
}
