package com.mr.detector.widget;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.mr.detector.R;

/**
 * Created by MR on 2017/5/31.
 */
public class MsgDialog extends Dialog{

    //确定与取消按钮
    private Button ensure;
    private Button cancel;
    //消息标题与主体文本
    private TextView title;
    private TextView message;
    //设置title和消息主体，用于用户自定义
    private String titleStr;
    private String messageStr;
    //用于自定义确认取消按钮文本内容
    private String ensureStr,cancelStr;

    private onCancelClickListener mCancelClickListener;
    private onEnsureClickListener mEnsureClickListener;

    /**
     * 设置确定按钮的显示内容和监听
     * @param ensureStr 显示的文本内容
     * @param listener 确认监听事件
     */
    public void setOnEnsureClickListener(String ensureStr,onEnsureClickListener listener) {
        if (this.ensureStr != null) {
            this.ensureStr = ensureStr;
        }
        this.mEnsureClickListener = listener;
    }

    /**
     * 设置取消按钮的显示内容和监听
     * @param cancelStr 显示的文本内容
     * @param listener 取消监听事件
     */
    public void setOnCancelClickListener(String cancelStr, onCancelClickListener listener) {
        if (this.cancelStr != null) {
            this.cancelStr = cancelStr;
        }
        this.mCancelClickListener = listener;
    }

    public MsgDialog(Context context) {
        super(context,R.style.MsgDialog);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_show_mesg);
        //按空白处不能取消动画
        setCanceledOnTouchOutside(false);
        //初始化界面控件
        initView();
        //初始化界面数据
        initData();
        //初始化控件事件
        initEvent();
    }

    /**
     * 初始化界面的确定和取消事件监听
     */
    private void initEvent() {
        //设置确定监听
        ensure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mEnsureClickListener != null) {
                    mEnsureClickListener.onEnsureClick();
                }
            }
        });
        //设置取消监听
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mCancelClickListener != null) {
                    mCancelClickListener.onCancelClick();
                }
            }
        });
    }

    /**
     * 初始化界面数据
     */
    private void initData() {
        //设置用户自定义的title和message
        if (titleStr != null) {
            title.setText(titleStr);
        }
        if (messageStr != null) {
            message.setText(messageStr);
        }
        //设置按钮的文字
        if (ensureStr != null) {
            ensure.setText(ensureStr);
        }
        if (cancelStr != null) {
            cancel.setText(cancelStr);
        }
    }

    /**
     * 初始化界面控件
     */
    private void initView() {
        ensure = (Button) findViewById(R.id.dialog_confirm);
        cancel = (Button) findViewById(R.id.dialog_cancel);
        title = (TextView) findViewById(R.id.dialog_title);
        message = (TextView) findViewById(R.id.dialog_display);
    }

    public void setTitle(String title) {
        titleStr = title;
    }

    public void setMessage(String message) {
        messageStr = message;
    }

    /**
     * 确认事件监听
     */
    public interface onEnsureClickListener{
        public void onEnsureClick();
    }

    /**
     * 取消事件监听
     */
    public interface onCancelClickListener{
        public void onCancelClick();
    }
}
