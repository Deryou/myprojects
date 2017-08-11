package com.zda.bmt.ui.actiivty;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.zda.bmt.R;
import com.zda.bmt.adapter.ReportListAdapter;
import com.zda.bmt.bean.ReportFile;
import com.zda.bmt.gloable.AppConstant;
import com.zda.bmt.util.Util;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import me.leefeng.promptlibrary.PromptButton;
import me.leefeng.promptlibrary.PromptButtonListener;
import me.leefeng.promptlibrary.PromptDialog;

/**
 * 开启热点
 */
public class ReportFileActivity extends BaseActivity{
    private static final String TAG = "ReportFileActivity";

    @BindView(R.id.report_recycler_view)
    RecyclerView mRecyclerView;
    @BindView(R.id.file_cancel)
    LinearLayout mFileCancel;
    @BindView(R.id.file_delete)
    LinearLayout mFileDelete;
    @BindView(R.id.file_unchecked)
    LinearLayout mFileUnchecked;
    @BindView(R.id.file_checked)
    LinearLayout mFileChecked;
    private List<ReportFile> mFilelist;
    private ReportFile mReportFile;
    private ReportListAdapter mAdapter;
    private File[] files;
    private LinearLayout layout_menu;
    private PromptDialog mPromptDialog;
    private boolean isOPfile = false;

    public static void startActivity(Context context) {
        context.startActivity(new Intent(context,ReportFileActivity.class));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        layout_menu = (LinearLayout) findViewById(R.id.file_operate_layout);
        mPromptDialog = new PromptDialog(this);
        mPromptDialog.getDefaultBuilder().touchAble(true).round(3).loadingDuration(3000);
        mFilelist = new ArrayList<>();
        mAdapter = new ReportListAdapter(mFilelist);
        mRecyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setAdapter(mAdapter);
        mAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                if (!isOPfile) {
                    String filePath = mFilelist.get(position).getFilePath();
                    if (filePath != null) {
                        File file = new File(filePath);
                        Uri path = Uri.fromFile(file);
                        Intent intent = new Intent(Intent.ACTION_VIEW);
                        intent.setDataAndType(path, "text/plain");
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
//            startActivity(Intent.createChooser(intent,"请选择打开程序"));
                    }
                } else {
                    mAdapter.setSelected(position);
                }
            }
        });
        mAdapter.setOnItemLongClickListener(new BaseQuickAdapter.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(BaseQuickAdapter adapter, View view, int position) {
                mAdapter.setSelectMode(true);
                mAdapter.setSelected(position);
                isOPfile = true;
                layout_menu.setVisibility(View.VISIBLE);
                return true;
            }
        });
        updateList();
    }

    private void updateList() {
//        File file = this.getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS);
        mFilelist.clear();
        File file = new File(Util.getSDPath(this)+ AppConstant.REPORT_FOLDER);
        files = file.listFiles();
        if (files != null) {
            for (int i = 0; i < files.length; i++) {
                mReportFile = new ReportFile(files[i].getName(),Util.FormetFileSize(files[i].length()), Util.getFileLastModifiedTime(files[i]),files[i].getPath());
                mFilelist.add(mReportFile);
            }
        } else {
            Toast.makeText(this,"文件为空",Toast.LENGTH_SHORT).show();
        }
    }

    @OnClick({R.id.file_cancel, R.id.file_delete, R.id.file_unchecked, R.id.file_checked})
    public void click(View view) {
        switch (view.getId()) {
            case R.id.file_cancel:
                mAdapter.setSelectMode(false);
                isOPfile = false;
                layout_menu.setVisibility(View.GONE);
                break;
            case R.id.file_delete:
                //按钮的定义
                final PromptButton confirm = new PromptButton("确定", new PromptButtonListener() {
                    @Override
                    public void onClick(PromptButton button) {
                        for (int i = 0; i < mFilelist.size(); i++) {
                            if (mAdapter.getSelected(i) == true) {
                                String filePath = mFilelist.get(i).getFilePath();
                                File file = new File(filePath);
                                if (file.isFile()) {
                                    file.delete();
                                }
                            }
                        }
                        updateList();
                        mAdapter.setSelectMode(false);
                        isOPfile = false;
                        layout_menu.setVisibility(View.GONE);
                    }
                });
                confirm.setTextColor(Color.parseColor("#DAA520"));
                confirm.setFocusBacColor(Color.parseColor("#326EB4"));
                mPromptDialog.showWarnAlert("你确定要删除文件？", new PromptButton("取消", new
                        PromptButtonListener() {
                            @Override
                            public void onClick(PromptButton button) {
                                mPromptDialog.dismiss();
                            }
                        }), confirm);
                break;
            case R.id.file_unchecked:
                for (int i = 0; i < mFilelist.size(); i++) {
                    mAdapter.setSelected(i);
                }
                break;
            case R.id.file_checked:
                for (int i = 0; i < mFilelist.size(); i++) {
                    if (mAdapter.getSelected(i) == false) {
                        mAdapter.setSelected(i);
                    }
                }
                break;
        }
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_report_file;
    }

    @Override
    public String setToolbarTitle() {
        return null;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_file_menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.file_share:
                shareFile();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void shareFile() {
        ArrayList<Uri> imageUris = new ArrayList<>();
        boolean selected = false;
        for (int i = 0; i < mFilelist.size(); i++) {
            if (mAdapter.getSelected(i) == true) {
                selected = true;
            }
        }
        if (selected) {
            for (int i = 0; i < mFilelist.size(); i++) {
                if (mAdapter.getSelected(i) == true) {
                    String filePath = mFilelist.get(i).getFilePath();
                    File file = new File(filePath);
                    if (file.isFile()) {
                        imageUris.add(Uri.fromFile(file));
                    }
                }
            }
            Intent shareIntent = new Intent();
            shareIntent.setAction(Intent.ACTION_SEND_MULTIPLE);
            shareIntent.setType("*/*");
            shareIntent.putParcelableArrayListExtra(Intent.EXTRA_STREAM, imageUris);
            startActivityForResult(Intent.createChooser(shareIntent, "请选择打开工具"), 0);
        } else {
            Toast.makeText(this,"请长按记录选择后再分享",Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        //根据上面发送过去的请求码来区别
        switch (requestCode) {
            case 0:
                Toast.makeText(this,"分享完成",Toast.LENGTH_SHORT).show();
                break;
            default:
                break;
        }
    }

    @Override
    public void onBackPressed() {
        if (mAdapter.getSelectMode()) {
            mAdapter.setSelectMode(false);
            isOPfile = false;
            layout_menu.setVisibility(View.GONE);
        } else {
            super.onBackPressed();
        }
    }
}
