package vn.gas.thq.ui.downloadApk;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import vn.hongha.ga.R;

public class ApkDialog extends Dialog {
    private Activity activity;
    public TextView tvDes;
    public Button btnUpgrade;

    public String contentDes, contentLink;

    public ApkDialog(Activity context, String des, String link) {
       // super(context, android.R.style.Theme_Light_NoTitleBar_Fullscreen);
        super(context);
        this.activity = context;
        contentDes = des;
        contentLink = link;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_download_apk);
        setCancelable(false);

        tvDes = findViewById(R.id.tvDes);
        btnUpgrade = findViewById(R.id.btnUpgrade);
    //    tvDes.setText(contentDes);

        btnUpgrade.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
                try {
                    DownloadApkAsyntask downloadApkAsyntask = new DownloadApkAsyntask();
                    downloadApkAsyntask.setContext(activity);
                    downloadApkAsyntask.execute(contentLink);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
