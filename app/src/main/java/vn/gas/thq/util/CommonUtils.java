package vn.gas.thq.util;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.AssetManager;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.provider.OpenableColumns;
import android.provider.Settings;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import vn.hongha.ga.R;

public final class CommonUtils {

    private static final String TAG = "CommonUtils";
    public static final int min_length_phone = 10;
    public static final int max_length_phone = 11;

    private CommonUtils() {
        // This utility class is not publicly instantiable
    }

    public static ProgressDialog showLoadingDialog(Context context) {
        try {
            ProgressDialog progressDialog = new ProgressDialog(context, R.style.NewDialog);
            progressDialog.show();
            if (progressDialog.getWindow() != null) {
                progressDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            }
            progressDialog.setContentView(R.layout.progress_dialog);
            progressDialog.setIndeterminate(true);
            progressDialog.setCancelable(false);
            progressDialog.setCanceledOnTouchOutside(false);
            return progressDialog;
        } catch (Exception e) {
        }
        return null;
    }

    @SuppressLint("all")
    public static String getDeviceId(Context context) {
        return Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);
    }

    public static boolean isEmailValid(String email) {
        Pattern pattern;
        Matcher matcher;
        final String EMAIL_PATTERN = "^[a-zA-Z][a-zA-Z0-9_.]*@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$";
        pattern = Pattern.compile(EMAIL_PATTERN);
        matcher = pattern.matcher(email);
        return matcher.matches();
    }

    public static boolean isPhoneNumberValid(String phoneNumber) {
        Pattern pattern;
        Matcher matcher;
        final String PHONE_NUMBER_PATTERN =
                "(84|0)([0-9]{9,14})";
        pattern = Pattern.compile(PHONE_NUMBER_PATTERN);
        matcher = pattern.matcher(phoneNumber);
        return matcher.matches();
    }

    public static boolean isPhoneNumberValidate(String phoneNumber, phoneValidateCallback validateCallback) {
        final String PHONE_NUMBER_PATTERN = "(84|0)([0-9]{0,14})";
        Pattern pattern = Pattern.compile(PHONE_NUMBER_PATTERN);
        Matcher matcher = pattern.matcher(phoneNumber);
        if (!matcher.matches()) {
            validateCallback.formatValidate();
            return false;
        }
        if (((phoneNumber.length() > 0 && phoneNumber.length() < min_length_phone)) || phoneNumber.length() > max_length_phone) {
            validateCallback.lengthValidate();
            return false;
        }
        return true;
    }

    public static boolean isLicensePlatesNumber(String placeNumber) {
        final String LicensePlates_PATTERN = "([a-zA-Z0-9]{0,16})";
        Pattern pattern = Pattern.compile(LicensePlates_PATTERN);
        Matcher matcher = pattern.matcher(placeNumber);
        return matcher.matches();
    }

    public interface phoneValidateCallback {
        void lengthValidate();

        void formatValidate();
    }

    public static String loadJSONFromAsset(Context context, String jsonFileName)
            throws IOException {
        AssetManager manager = context.getAssets();
        InputStream is = manager.open(jsonFileName);

        int size = is.available();
        byte[] buffer = new byte[size];
        is.read(buffer);
        is.close();

        return new String(buffer, "UTF-8");
    }

//    public static String getTimeStamp() {
//        return new SimpleDateFormat(AppConstants.TIMESTAMP_FORMAT, Locale.US).format(new Date());
//    }

//    public static void showConfirmDiglog2Button(Activity activity, String title, String message, int btnLeftText, int btnRightText, ConfirmDialogCallback callback) {
//        if (activity != null && !activity.isFinishing()) {
//            AlertDialog.Builder builder = new AlertDialog.Builder(activity, R.style.AlertDialogNoBG);
//            LayoutInflater inflater = activity.getLayoutInflater();
//            View dialogView = inflater.inflate(R.layout.layout_dialog2button, null);
//            builder.setView(dialogView);
//            TextView tv_header_dialog = dialogView.findViewById(R.id.tv_header_dialog);
//            TextView tv_content_dialog = dialogView.findViewById(R.id.tv_content_dialog);
//
//            tv_header_dialog.setText("" + title);
//            tv_content_dialog.setText("" + message);
//
//            TextView tv_cancel = dialogView.findViewById(R.id.tv_dialog_cancel);
//            tv_cancel.setText(btnLeftText);
//            TextView tv_ok = dialogView.findViewById(R.id.tv_dialog_ok);
//            tv_ok.setText(btnRightText);
//
//            AlertDialog alertDialog = builder.create();
//            alertDialog.show();
//            tv_cancel.setOnClickListener((view) -> {
//                alertDialog.dismiss();
//                if (callback != null) {
//                    callback.ConfirmDialogCallback(AppConstants.NO);
//                }
//            });
//            tv_ok.setOnClickListener(view -> {
//                alertDialog.dismiss();
//                if (callback != null) {
//                    callback.ConfirmDialogCallback(AppConstants.YES);
//                }
//            });
//        }
//    }

    public static AlertDialog alertDialogTemp;

//    public static void showConfirmDiglog2Button(Activity activity, String title, String message, ConfirmDialogCallback callback) {
//        try {
//            alertDialogTemp.dismiss();
//            alertDialogTemp = null;
//        } catch (Exception e) {
//        }
//        if (activity != null && !activity.isFinishing()) {
//            AlertDialog.Builder builder = new AlertDialog.Builder(activity, R.style.AlertDialogNoBG);
//            LayoutInflater inflater = activity.getLayoutInflater();
//            View dialogView = inflater.inflate(R.layout.layout_dialog2button, null);
//            builder.setView(dialogView);
//            TextView tv_header_dialog = dialogView.findViewById(R.id.tv_header_dialog);
//            TextView tv_content_dialog = dialogView.findViewById(R.id.tv_content_dialog);
//
//            tv_header_dialog.setText("" + title);
//            tv_content_dialog.setText("" + message);
//
//            TextView tv_cancel = dialogView.findViewById(R.id.tv_dialog_cancel);
//            TextView tv_ok = dialogView.findViewById(R.id.tv_dialog_ok);
//
//            alertDialogTemp = builder.create();
//            alertDialogTemp.show();
//            tv_cancel.setOnClickListener((view) -> {
//                try {
//                    alertDialogTemp.dismiss();
//                } catch (Exception e) {
//                }
//                if (callback != null) {
//                    callback.ConfirmDialogCallback(AppConstants.NO);
//                }
//            });
//            tv_ok.setOnClickListener(view -> {
//                try {
//                    alertDialogTemp.dismiss();
//                } catch (Exception e) {
//                }
//                if (callback != null) {
//                    callback.ConfirmDialogCallback(AppConstants.YES);
//                }
//            });
//        }
//    }

//    public static void showDiglog1Button(Activity activity, String title, String message, ConfirmDialogCallback callback) {
//        if (activity != null && !activity.isFinishing()) {
//            AlertDialog.Builder builder = new AlertDialog.Builder(activity, R.style.AlertDialogNoBG);
//            LayoutInflater inflater = activity.getLayoutInflater();
//            View dialogView = inflater.inflate(R.layout.layout_dialog1button, null);
//            builder.setView(dialogView);
//            TextView tv_header_dialog = dialogView.findViewById(R.id.tv_header_dialog);
//            TextView tv_content_dialog = dialogView.findViewById(R.id.tv_content_dialog);
//
//            tv_header_dialog.setText("" + title);
//            tv_content_dialog.setText("" + message);
//
//            TextView tv_ok = dialogView.findViewById(R.id.tv_dialog_ok);
//
//            AlertDialog alertDialog = builder.create();
//            alertDialog.show();
//            tv_ok.setOnClickListener(view -> {
//                alertDialog.dismiss();
//                if (callback != null) {
//                    callback.ConfirmDialogCallback(AppConstants.YES);
//                }
//            });
//        }
//    }

//    public static void showConfirmDiglog2Button(Activity activity, String title, String message, String leftText, String rightText, ConfirmDialogCallback callback) {
//        if (activity != null && !activity.isFinishing()) {
//            AlertDialog.Builder builder = new AlertDialog.Builder(activity, R.style.AlertDialogNoBG);
//            LayoutInflater inflater = activity.getLayoutInflater();
//            View dialogView = inflater.inflate(R.layout.layout_dialog2button, null);
//            builder.setView(dialogView);
//            TextView tv_header_dialog = dialogView.findViewById(R.id.tv_header_dialog);
//            TextView tv_content_dialog = dialogView.findViewById(R.id.tv_content_dialog);
//
//            tv_header_dialog.setText("" + title);
//            tv_content_dialog.setText("" + message);
//
//            TextView tv_cancel = dialogView.findViewById(R.id.tv_dialog_cancel);
//            TextView tv_ok = dialogView.findViewById(R.id.tv_dialog_ok);
//            if (!TextUtils.isEmpty(leftText)) {
//                tv_cancel.setText(leftText);
//            }
//            if (!TextUtils.isEmpty(rightText)) {
//                tv_ok.setText(rightText);
//            }
//            AlertDialog alertDialog = builder.create();
//            alertDialog.show();
//            tv_cancel.setOnClickListener((view) -> {
//                alertDialog.dismiss();
//                if (callback != null) {
//                    callback.ConfirmDialogCallback(AppConstants.NO);
//                }
//            });
//            tv_ok.setOnClickListener(view -> {
//                alertDialog.dismiss();
//                if (callback != null) {
//                    callback.ConfirmDialogCallback(AppConstants.YES);
//                }
//            });
//        }
//    }

//    public static void showConfirmOTPDiglog2Button(Activity activity, String title, String leftText, String rightText, ConfirmOTPCallback callback) {
//        if (activity != null && !activity.isFinishing()) {
//            AlertDialog.Builder builder = new AlertDialog.Builder(activity, R.style.AlertDialogNoBG);
//            LayoutInflater inflater = activity.getLayoutInflater();
//            View dialogView = inflater.inflate(R.layout.layout_otp_dialog2button, null);
//            builder.setView(dialogView);
//            TextView tv_header_dialog = dialogView.findViewById(R.id.tv_header_dialog);
//            EditText tv_content_dialog = dialogView.findViewById(R.id.tv_content_dialog);
//
//            tv_header_dialog.setText("" + title);
////            tv_content_dialog.setText("" + message);
//
//            TextView tv_cancel = dialogView.findViewById(R.id.tv_dialog_cancel);
//            TextView tv_ok = dialogView.findViewById(R.id.tv_dialog_ok);
//            if (!TextUtils.isEmpty(leftText)) {
//                tv_cancel.setText(leftText);
//            }
//            if (!TextUtils.isEmpty(rightText)) {
//                tv_ok.setText(rightText);
//            }
//            AlertDialog alertDialog = builder.create();
//            alertDialog.show();
//            tv_cancel.setOnClickListener((view) -> {
//                alertDialog.dismiss();
//                if (callback != null) {
//                    callback.ConfirmDialogCallback(AppConstants.NO, null);
//                }
//            });
//            tv_ok.setOnClickListener(view -> {
//                alertDialog.dismiss();
//                if (callback != null) {
//                    callback.ConfirmDialogCallback(AppConstants.YES, tv_content_dialog.getText().toString().trim());
//                }
//            });
//        }
//    }

    public static String priceWithoutDecimal(Double price) {
        DecimalFormat formatter = new DecimalFormat("###,###,###.##");
        String num = formatter.format(price);
        num = num.replace(".", ",");
        return num;
    }

    public static void saveString(String content, String fileName) {
        try {
            File myFile = new File("/sdcard/" + fileName);
            myFile.createNewFile();
            FileOutputStream fOut = new FileOutputStream(myFile);
            OutputStreamWriter myOutWriter =
                    new OutputStreamWriter(fOut);
            myOutWriter.append(content);
            myOutWriter.close();
            fOut.close();
        } catch (Exception e) {
        }
    }

    // check kích thước số lượng nhập vào theo dạng length 8.2
    // return true : OK
    // return false : Hiển thị message lỗi  ví dụ: Vui lòng không nhập khối lượng
    public static boolean validateDecimalNumber(String text) {
        // check xem có nhập -1 không
        try {
            Double num = Double.parseDouble(text);
            if (num < 0) {
                return false;
            }
        } catch (Exception e) {
            return false;
        }
        if (text.indexOf('.') > 0) { // nếu là số thập phân
            int integerPlaces = text.indexOf('.');
            int decimalPlaces = text.length() - integerPlaces - 1;
            // + 1 dấu chấm => 6+1
            if (integerPlaces >= 7) {
                return false;
            }
            if (decimalPlaces > 2) {
                return false;
            }
            return true;
        } else {
            // trường hợp không phải thập phân thì là 6 số
            if (text.length() > 6) {
                return false;
            }
            return true;
        }
    }

    /***
     * Xóa .0 đằng sau dấu chấm
     * ví dụ:
     * 123.0 stripTrailingZeros >> 123
     * 123.00 stripTrailingZeros >> 123
     * 121212 stripTrailingZeros >> 121212
     * 121212.02 stripTrailingZeros >> 121212.02
     * 12. stripTrailingZeros >> 12
     */
    public static String stripTrailingZero(String weight) {
        try {
            BigDecimal bd = new BigDecimal(weight);
            return bd.stripTrailingZeros().toPlainString();
        } catch (Exception e) {
            return weight;
        }
    }

    // Luồng thay đổi thông tin, một số trường bị disable ko cho sửa
    // nên thay đổi màu text về dạng disable để dễ phân biệt
//    public static void setDisableTextColor(TextInputEditText et) {
//        if (et != null) {
//            et.setTextColor(et.getContext().getResources().getColor(R.color.gray_888888));
//            et.setHintTextColor(et.getContext().getResources().getColor(R.color.gray_888888));
//        }
//    }


    public static File from(Context context, Uri uri) throws IOException {
        InputStream inputStream = context.getContentResolver().openInputStream(uri);
        String fileName = getFileName(context, uri);
        String[] splitName = splitFileName(fileName);
        File tempFile = File.createTempFile(splitName[0], splitName[1]);
        tempFile = rename(tempFile, fileName);
        tempFile.deleteOnExit();
        FileOutputStream out = null;
        try {
            out = new FileOutputStream(tempFile);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        if (inputStream != null) {
            copy(inputStream, out);
            inputStream.close();
        }

        if (out != null) {
            out.close();
        }
        return tempFile;
    }

    private static String[] splitFileName(String fileName) {
        String name = fileName;
        String extension = "";
        int i = fileName.lastIndexOf(".");
        if (i != -1) {
            name = fileName.substring(0, i);
            extension = fileName.substring(i);
        }

        return new String[]{name, extension};
    }

    private static String getFileName(Context context, Uri uri) {
        String result = null;
        if (uri.getScheme().equals("content")) {
            Cursor cursor = context.getContentResolver().query(uri, null, null, null, null);
            try {
                if (cursor != null && cursor.moveToFirst()) {
                    result = cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME));
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if (cursor != null) {
                    cursor.close();
                }
            }
        }
        if (result == null) {
            result = uri.getPath();
            int cut = result.lastIndexOf(File.separator);
            if (cut != -1) {
                result = result.substring(cut + 1);
            }
        }
        return result;
    }

    private static File rename(File file, String newName) {
        File newFile = new File(file.getParent(), newName);
        if (!newFile.equals(file)) {
            if (newFile.exists() && newFile.delete()) {
                Log.d("FileUtil", "Delete old " + newName + " file");
            }
            if (file.renameTo(newFile)) {
                Log.d("FileUtil", "Rename file to " + newName);
            }
        }
        return newFile;
    }

    private static final int EOF = -1;
    private static final int DEFAULT_BUFFER_SIZE = 1024 * 4;

    private static long copy(InputStream input, OutputStream output) throws IOException {
        long count = 0;
        int n;
        byte[] buffer = new byte[DEFAULT_BUFFER_SIZE];
        while (EOF != (n = input.read(buffer))) {
            output.write(buffer, 0, n);
            count += n;
        }
        return count;
    }
}
