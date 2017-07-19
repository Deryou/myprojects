package com.zda.bmt.widget;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.Button;
import android.widget.TableRow;
import android.widget.TextView;

import com.zda.bmt.R;

/**
 * Created by MR on 2017/6/28.
 */

public class MEditDialog extends Dialog{
    private Button ensure,cancel;
    private TextView title,titleRow1,titleRow2;
    private String titleMsg,titleRow1Msg,titleRow2Msg;
    //用于自定义确认与取消按钮
    private String ensureStr,cancelStr;
    private TableRow mTableRow1,mTableRow2;

    private onEnsureClickListener mEnsureClickListener;
    private onCancelClickListener mCancelClickListener;


    public MEditDialog(@NonNull Context context) {
        super(context, R.style.MEditDialog);
    }

    public void setOnEnsureClickListener(String ensureStr, onEnsureClickListener listener) {
        if (this.ensureStr != null) {
            this.ensureStr = ensureStr;
        }
        this.mEnsureClickListener = listener;
    }

    public void setOnCancelClickListener(String cancelStr, onCancelClickListener listener) {
        if (this.cancelStr != null) {
            this.cancelStr = cancelStr;
        }
        this.mCancelClickListener = listener;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.medit_dialog);
        setCanceledOnTouchOutside(false);
        initView();
        initData();
        initEvent();
    }

    private void initEvent() {
        ensure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mEnsureClickListener != null) {
                    mEnsureClickListener.onEnsureClick();
                }
            }
        });
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mCancelClickListener != null) {
                    mCancelClickListener.onCancelClick();
                }
            }
        });
    }

    private void initData() {
        if (titleMsg != null) {
            title.setText(titleMsg);
        }
        if (titleRow1Msg != null) {
            titleRow1.setText(titleRow1Msg);
        }
        if (titleRow2Msg != null) {
            titleRow2.setText(titleRow2Msg);
        }
        if (ensureStr!= null) {
            ensure.setText(ensureStr);
        }
        if (cancelStr != null) {
            cancel.setText(cancelStr);
        }
    }

    private void initView() {
        ensure = (Button) findViewById(R.id.dialog_confirm);
        cancel = (Button) findViewById(R.id.dialog_cancel);
        title = (TextView) findViewById(R.id.dialog_title);
        titleRow1 = (TextView) findViewById(R.id.title_row1);
        titleRow2 = (TextView) findViewById(R.id.title_row2);
        mTableRow1 = (TableRow) findViewById(R.id.table_row1);
        mTableRow2 = (TableRow) findViewById(R.id.table_row2);

    }

    public MEditDialog setTitle(String title) {
        titleMsg = title;
        return this;
    }

    public MEditDialog setTitleRow1(String titleRow1Msg) {
        this.titleRow1Msg = titleRow1Msg;
        return this;
    }

    public MEditDialog setTitleRow2(String titleRow2Msg) {
        this.titleRow2Msg = titleRow2Msg;
        return  this;
    }

    public String getTitleRow1Msg() {
        return titleRow1.getText().toString();
    }

    public String getTitleRow2Msg() {
        return titleRow2.getText().toString();
    }

    /**
     * 确认事件监听
     */
    public interface onEnsureClickListener {
        public void onEnsureClick();
    }

    /**
     * 取消事件监听
     */
    public interface onCancelClickListener{
        public void onCancelClick();
    }
}
