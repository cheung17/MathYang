package com.math.yang.mathyang.view;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.math.yang.mathyang.R;

/**
 * Created by zhangtx on 2018/1/10.
 */

public class DownloadDialog extends Dialog implements View.OnClickListener {
    private ImageView ivClose;
    private Button ivSelectAll, ivConfirmDialog;
    private ListView listView;

    public DownloadDialog(@NonNull Context context) {
        super(context);
        new DownloadDialog(context, R.style.fulldialog);
    }

    public DownloadDialog(@NonNull Context context, int themeResId) {
        super(context, themeResId);
    }

    protected DownloadDialog(@NonNull Context context, boolean cancelable, @Nullable OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        Window window = getWindow();
        window.setWindowAnimations(R.style.dialog_anim);
        setContentView(R.layout.dialog_download);
        initViews();
    }

    @Override
    public void show() {
        super.show();
        Window window = getWindow();
        WindowManager.LayoutParams lp = window.getAttributes();
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.MATCH_PARENT;
        window.getDecorView().setPadding(0, 0, 0, 0);
        window.setAttributes(lp);
    }

    private void initViews() {
        findViewById(R.id.iv_close).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_close:
                DownloadDialog.this.dismiss();
                break;
        }
    }
}
