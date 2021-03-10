package vn.gas.thq.ui.downloadApk;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Environment;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import vn.hongha.ga.R;

public class DownloadApkAsyntask extends AsyncTask<String, Integer, Void> {
    private Activity activity;
    private ProgressDialog progress;
    File folder = null;
    String apkPath = "";

    private void dismissProgressDialog() {
        try {
            if (activity != null && !activity.isFinishing()) {
                if (progress != null && progress.isShowing()) {
                    progress.dismiss();
                }
            }
        } catch (Exception e) {
        }
    }

    private void showProgressDialog() {
        try {
            if (progress == null) {
                progress = new ProgressDialog(activity);
                progress.setCancelable(false);
                progress.setMessage(activity.getResources().getString(R.string.loading));
            }
            progress.setIndeterminate(true);
            progress.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
            if (!progress.isShowing()) {
                progress.show();
                progress.setCancelable(false);
            }
        } catch (Exception e) {
        }
    }

    public void setContext(Activity contextf) {
        activity = contextf;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        showProgressDialog();
    }

    public boolean createDirectory(Context context, String directoryPath) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q)
            folder = new File(context.getExternalFilesDir(null), directoryPath);
        else
            folder = new File(Environment.getExternalStorageState(), directoryPath);
        if (!folder.exists()) {
            return folder.mkdirs();
        }

        return true;
    }

    @Override
    protected Void doInBackground(String... arg0) {
        FileOutputStream fos = null;
        try {
            URL url = new URL(arg0[0]);
            HttpURLConnection c = (HttpURLConnection) url.openConnection();
            c.setRequestMethod("GET");
            c.connect();

            File outputFile = null;
            try {
                boolean rs = createDirectory(activity, "apknangcap");
                if (!rs) {
                    Toast.makeText(activity, "Lỗi tạo thư mục lưu trữ!", Toast.LENGTH_LONG).show();
                    return null;
                }
                System.out.println(rs);
                outputFile = new File(folder, "gas_hotfix.apk");
                apkPath = outputFile.getAbsolutePath();
            } catch (Exception e) {
                e.printStackTrace();
            }
            fos = new FileOutputStream(outputFile);
            int responseCode = c.getResponseCode(); //can call this instead of con.connect()
            // this will be useful to display download percentage
            // might be -1: server did not report the length
            int fileLength = c.getContentLength();

            InputStream is = c.getInputStream();

            long total = 0;
            int count;

            byte data[] = new byte[4096];
            while ((count = is.read(data)) != -1) {
                // allow canceling with back button
                if (isCancelled()) {
                    is.close();
                    return null;
                }
                total += count;
                // publishing the progress....
                if (fileLength > 0) // only if total length is known
                    publishProgress((int) (total * 100 / fileLength));
                fos.write(data, 0, count);
            }
            is.close();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (fos != null) {
                try {
                    fos.flush();
                    fos.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }

    @Override
    protected void onProgressUpdate(Integer... values) {
        super.onProgressUpdate(values);
        progress.setIndeterminate(false);
        progress.setMax(100);
        progress.setProgress(values[0]);
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);
        dismissProgressDialog();
        try {
            //  String urlinstall = "/mnt/sdcard/Download/gas_hotfix.apk";
            ApkInstaller.installApplication(activity, apkPath);
           /* if (Build.VERSION.SDK_INT < Build.VERSION_CODES.N) {
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setDataAndType(Uri.fromFile(new File(urlinstall)),
                        "application/vnd.android.package-archive");
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                activity.startActivity(intent);
            } else {
                Log.d("", "fileApk path = " + urlinstall);
                File fileApk = new File(urlinstall);
                Uri apkUri = FileProvider.getUriForFile(activity, activity.getApplicationContext().getPackageName() + ".provider", fileApk);
                Intent intent = new Intent(Intent.ACTION_INSTALL_PACKAGE);
                intent.setDataAndType(apkUri,
                        "application/vnd.android.package-archive");
                intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                activity.startActivity(intent);
            }*/
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}