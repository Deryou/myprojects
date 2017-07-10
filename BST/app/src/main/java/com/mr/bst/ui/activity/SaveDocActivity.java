package com.mr.bst.ui.activity;

import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.mr.bst.R;
import com.mr.bst.gloable.AppConstant;
import com.mr.bst.util.Util;
import com.sdsmdg.tastytoast.TastyToast;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
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

    private AlertDialog mDialog;
    private LinearLayout mDialogLayout;
    private EditText mInputData;
    private Button mDialogConfirm, mDialogCancel;
    private TextView mDialogTitle, mDialogDisplay;
    private Map<String, String> dataMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        dataMap = new HashMap<>();
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
        getMenuInflater().inflate(R.menu.menu_save_doc,menu);
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

    }

    public void saveDataInfo() {
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
        dataMap.put("$CSDD$", mFormTestPot.getText().toString() == null ? "" : mFormTestPot
                .getText().toString());
        dataMap.put("$YJ$", mFormAccording.getText().toString() == null ? "" : mFormAccording
                .getText().toString());
        dataMap.put("$JYRQ$", Util.getNowTime());
    }
}
