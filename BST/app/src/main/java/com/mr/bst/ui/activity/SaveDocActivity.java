package com.mr.bst.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.mr.bst.R;
import com.mr.bst.gloable.AppConstant;
import com.mr.bst.util.Util;
import com.sdsmdg.tastytoast.TastyToast;

import org.apache.poi.hwpf.HWPFDocument;
import org.apache.poi.hwpf.usermodel.Range;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;

public class SaveDocActivity extends BaseActivity {

    @BindView(R.id.form_company)
    EditText mFormCompany;
    @BindView(R.id.form_address)
    EditText mFormAddress;
    @BindView(R.id.form_equip_name)
    EditText mFormEquipName;
    @BindView(R.id.form_factory_serial_num)
    EditText mFormFactorySerialNum;
    @BindView(R.id.form_equip_number)
    EditText mFormEquipNumber;
    @BindView(R.id.form_manufacturer)
    EditText mFormManufacturer;
    @BindView(R.id.form_equip_type)
    EditText mFormEquipType;
    @BindView(R.id.form_correct_degree)
    EditText mFormCorrectDegree;
    @BindView(R.id.form_test_pot)
    EditText mFormTestPot;
    @BindView(R.id.form_according)
    EditText mFormAccording;
    @BindView(R.id.form_unique_num)
    EditText mFormUniqueNum;
    @BindView(R.id.form_accuracy_degree)
    EditText mFormAccuracyDegree;
    @BindView(R.id.form_temp)
    EditText mFormTemp;
    @BindView(R.id.form_humdity)
    EditText mFormHumdity;
    @BindView(R.id.form_other)
    EditText mFormOther;
    @BindView(R.id.form_test_result)
    EditText mFormTestResult;
    @BindView(R.id.form_period_data)
    EditText mFormPeriodData;
    @BindView(R.id.form_calibrate_result)
    EditText mFormCalibrateResult;
    @BindView(R.id.form_calibrate_data)
    EditText mFormCalibrateData;
    @BindView(R.id.form_cnas)
    EditText mFormCnas;
    @BindView(R.id.form_test_person)
    EditText mFormTestPerson;
    @BindView(R.id.form_check_person)
    EditText mFormCheckPerson;
    @BindView(R.id.wg_yes)
    RadioButton mWgYes;
    @BindView(R.id.wg_no)
    RadioButton mWgNo;
    @BindView(R.id.cyd_yes)
    RadioButton mCydYes;
    @BindView(R.id.cyd_no)
    RadioButton mCydNo;
    @BindView(R.id.qcbj_yes)
    RadioButton mQcbjYes;
    @BindView(R.id.qcbj_no)
    RadioButton mQcbjNo;
    @BindView(R.id.gfbj_yes)
    RadioButton mGfbjYes;
    @BindView(R.id.gfbj_no)
    RadioButton mGfbjNo;
    @BindView(R.id.pqbj_yes)
    RadioButton mPqbjYes;
    @BindView(R.id.pqbj_no)
    RadioButton mPqbjNo;
    @BindView(R.id.bdbj_yes)
    RadioButton mBdbjYes;
    @BindView(R.id.bdbj_no)
    RadioButton mBdbjNo;
    private AlertDialog mDialog;
    private LinearLayout mDialogLayout;
    private EditText mInputData;
    private Button mDialogConfirm, mDialogCancel;
    private TextView mDialogTitle, mDialogDisplay;
    private Map<String, String> dataMap;
    private Map<String, String> map;
    private Gson mGson;

    public static void startActivity(Context context) {
        Intent intent = new Intent(context, SaveDocActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        dataMap = new HashMap<>();
        initDialog();
    }

    private void initDialog() {
        mDialogLayout = (LinearLayout) getLayoutInflater().inflate(R.layout.dialog_single_input,
                null);
        mDialogTitle = (TextView) mDialogLayout.findViewById(R.id.dialog_title);
        mDialogDisplay = (TextView) mDialogLayout.findViewById(R.id.dialog_display);
        mInputData = (EditText) mDialogLayout.findViewById(R.id.dialog_input);
        mDialogConfirm = (Button) mDialogLayout.findViewById(R.id.dialog_confirm);
        mDialogCancel = (Button) mDialogLayout.findViewById(R.id.dialog_cancel);
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_save_doc;
    }

    @Override
    public String setToolbarTitle() {
        return "数据保存";
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_save_doc, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.save_doc:
                String dataTime = Util.getNowTime();
                mDialogTitle.setText("请输入文件名称");
                mDialogDisplay.setText("  ");
                mInputData.setText(dataTime);
                if (mDialog == null) {
                    mDialog = new AlertDialog.Builder(this)
                            .setView(mDialogLayout)
                            .create();
                    mDialogConfirm.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            try {
                                InputStream inputStream = getAssets().open("BST-BII.doc");
                                String demoPath = Util.getDemoDocPath(SaveDocActivity.this)
                                        + "/Temporary_file.doc";
                                String reportPath = Util.getSDPath(SaveDocActivity.this) +
                                        AppConstant.REPORT_FOLDER + mInputData.getText().toString()
                                        + ".doc";
                                File demoFile = new File(demoPath);
                                File reportFile = new File(reportPath);
                                if (Util.writeToFile(demoFile, inputStream, false)) {
                                    writeDataToDoc(demoFile, reportFile);
                                    TastyToast.makeText(getApplicationContext(), "保存成功！",
                                            TastyToast.LENGTH_SHORT, TastyToast.SUCCESS);
                                } else {
                                    TastyToast.makeText(getApplicationContext(), "文件写入失败！",
                                            TastyToast.LENGTH_SHORT, TastyToast.ERROR);
                                }
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            mDialog.dismiss();
                        }
                    });
                    mDialogCancel.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            mDialog.dismiss();
                        }
                    });
                }
                mDialog.show();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void writeDataToDoc(File file, File reportFile) {
        saveDataInfo();
        map = new HashMap<>();
        mGson = new Gson();
        Type type = new TypeToken<Map<String, String>>() {
        }.getType();
        map = getDataMap(Util.getSavedDocData(AppConstant.TP_DATA), type);
        dataMap.putAll(map == null ? Util.getEmptyTPData() : map);
        map = getDataMap(Util.getSavedDocData(AppConstant.SK_DATA), type);
        dataMap.putAll(map == null ? Util.getEmptySKData() : map);
        map = getDataMap(Util.getSavedDocData(AppConstant.LIGHT_DATA), type);
        dataMap.putAll(map == null ? Util.getEmptyLightData() : map);
        map = getDataMap(Util.getSavedDocData(AppConstant.FLOW_DATA), type);
        dataMap.putAll(map == null ? Util.getEmptyFlowData() : map);
        try {
            FileInputStream in = new FileInputStream(file);
            HWPFDocument hdt = new HWPFDocument(in);
            Range range = hdt.getRange();
            for (Map.Entry<String, String> entry : dataMap.entrySet()) {
                range.replaceText(entry.getKey(), entry.getValue() == null ? "" : entry.getValue());
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

    public Map<String, String> getDataMap(String data, Type type) {
        return mGson.fromJson(data, type);
    }

    public void saveDataInfo() {
        dataMap.put("$WYXH$", mFormUniqueNum.getText().toString() == null ? "" : mFormUniqueNum
                .getText().toString());
        dataMap.put("$WTDW$", mFormCompany.getText().toString() == null ? "" : mFormCompany
                .getText().toString());
        dataMap.put("$DZ$", mFormAddress.getText().toString() == null ? "" : mFormAddress
                .getText().toString());
        dataMap.put("$QJMC$", mFormEquipName.getText().toString() == null ? "" : mFormEquipName
                .getText().toString());
        dataMap.put("$ZZC$", mFormManufacturer.getText().toString() == null ? "" :
                mFormManufacturer.getText().toString());
        dataMap.put("$CCBH$", mFormFactorySerialNum.getText().toString() == null ? "" :
                mFormFactorySerialNum.getText().toString());
        dataMap.put("$SBBH$", mFormEquipNumber.getText().toString() == null ? "" :
                mFormEquipNumber.getText().toString());
        dataMap.put("$XHGG$", mFormEquipType.getText().toString() == null ? "" : mFormEquipType
                .getText().toString());
        dataMap.put("$ZQU$", mFormCorrectDegree.getText().toString() == null ? "" :
                mFormCorrectDegree.getText().toString());
        dataMap.put("$ZQD$", mFormAccuracyDegree.getText().toString() == null ? "" :
                mFormAccuracyDegree
                        .getText().toString());
        dataMap.put("$CSDD$", mFormTestPot.getText().toString() == null ? "" : mFormTestPot
                .getText().toString());
        dataMap.put("$YJ$", mFormAccording.getText().toString() == null ? "" : mFormAccording
                .getText().toString());
        dataMap.put("$JYRQ$", Util.getData());
        dataMap.put("$WD$", mFormTemp.getText().toString() == null ? "" : mFormTemp
                .getText().toString());
        dataMap.put("$SD$", mFormHumdity.getText().toString() == null ? "" : mFormHumdity
                .getText().toString());
        dataMap.put("$QT$", mFormOther.getText().toString() == null ? "" : mFormOther
                .getText().toString());
        dataMap.put("$JDJL$", mFormTestResult.getText().toString() == null ? "" : mFormTestResult
                .getText().toString());
        dataMap.put("$YXQ$", mFormPeriodData.getText().toString() == null ? "" : mFormPeriodData
                .getText().toString());
        dataMap.put("$YXQ$", mFormPeriodData.getText().toString() == null ? "" : mFormPeriodData
                .getText().toString());
        dataMap.put("$JZJL$", mFormCalibrateResult.getText().toString() == null ? "" :
                mFormCalibrateResult
                        .getText().toString());
        dataMap.put("$JZZQ$", mFormCalibrateData.getText().toString() == null ? "" :
                mFormCalibrateData.getText().toString());
        dataMap.put("$CNAS$", mFormCnas.getText().toString() == null ? "" :
                mFormCnas.getText().toString());
        dataMap.put("$JJRY$", mFormTestPerson.getText().toString() == null ? "" :
                mFormTestPerson.getText().toString());
        dataMap.put("$HYRY$", mFormCheckPerson.getText().toString() == null ? "" :
                mFormCheckPerson.getText().toString());

        if (mWgYes.isChecked()) {
            dataMap.put("$WG$","符合");
        } else {
            dataMap.put("$WG$", "不符合");
        }
        if (mCydYes.isChecked()) {
            dataMap.put("$DHJ$", "符合");
        } else {
            dataMap.put("$DHJ$", "不符合");
        }
        if (mQcbjYes.isChecked()) {
            dataMap.put("$FWOP$", "符合");
        } else {
            dataMap.put("$FWOP$", "不符合");
        }
        if (mGfbjYes.isChecked()) {
            dataMap.put("$SIPQ$", "符合");
        } else {
            dataMap.put("$SIPQ$$", "不符合");
        }
        if (mPqbjYes.isChecked()) {
            dataMap.put("$SPQ$", "符合");
        } else {
            dataMap.put("$SPQ$", "不符合");
        }
        if (mBdbjYes.isChecked()) {
            dataMap.put("$SFBD$", "符合");
        } else {
            dataMap.put("$SFBD$", "不符合");
        }
    }
}
