package vn.gas.thq.ui.downloadApk;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

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
        tvDes.setText(contentDes);

        btnUpgrade.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    /*boolean isNonPlayAppAllowed = Settings.Secure.getInt(activity.getContentResolver(), Settings.Secure.INSTALL_NON_MARKET_APPS) == 1;
                    System.out.println("@@@@@ isNonPlayAppAllowed "+isNonPlayAppAllowed);
                    if (!isNonPlayAppAllowed) {
                        activity.startActivity(new Intent(android.provider.Settings.ACTION_SECURITY_SETTINGS));
                        return;
                    }*/
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                        if (!activity.getPackageManager().canRequestPackageInstalls()) {
                            Toast.makeText(activity, "Vui lòng cho phép cài đặt ứng dụng từ nguồn này", Toast.LENGTH_LONG).show();
                            activity.startActivityForResult(new Intent(Settings.ACTION_MANAGE_UNKNOWN_APP_SOURCES).setData(Uri.parse(String.format("package:%s", activity.getPackageName()))), 1234);
                            return;
                        } else {
                            //callInstallProcess();
                        }
                    } else {
                        //callInstallProcess();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
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
