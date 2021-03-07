package vn.gas.thq.datasourse.prefs;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;

import vn.gas.thq.model.NickPassModel;
import vn.gas.thq.model.TokenModel;
import vn.gas.thq.model.UserModel;
import vn.gas.thq.util.AppConstants;


public class AppPreferencesHelper implements PreferencesHelper {
    // key
    private static final String PREF_KEY_ACCESS_TOKEN = "PREF_KEY_ACCESS_TOKEN";
    private static final String LOGIN_BIOMETRIC = "LOGIN_BIOMETRIC";
    private static final String OLD_ACCOUNT = "OLD_ACCOUNT";
    private static final String OLD_PASSWORD = "OLD_PASSWORD";
    private static final String VEHICLE_TYPE = "VEHICLE_TYPE";
    private static final String VEHICLE_GROUP = "VEHICLE_GROUP";
    private static final String DOC_TYPE = "DOC_TYPE";
    private static final String CUSTOMER_TYPE = "CUSTOMER_TYPE";
    public static final String DATA_LICENSE_CHECKED = "DATA_LICENSE_CHECKED";
    public static final String CONTRACT_MODEL_SELECT = "CONTRACT_MODEL_SELECT";
    public static final String STATION_STAGE = "STATION_STAGE";
    private static final String PREF_KEY_USER = "PREF_KEY_USER";
    private static final String REMEMBER_USER = "REMEMBER_USER";
    private static final String NICK_PASS_USER = "NICK_PASS_USER";

    // SharedPreferences
    private final SharedPreferences mPrefs;
    private Context mContext;

    public AppPreferencesHelper(Context context) {
        this.mContext = context;
        mPrefs = context.getSharedPreferences(AppConstants.PREF_NAME, Context.MODE_PRIVATE);
    }

    @Override
    public TokenModel getTokenModel() {
        Gson gson = new Gson();
        String json = mPrefs.getString(PREF_KEY_ACCESS_TOKEN, "");
        TokenModel tokenModel = gson.fromJson(json, TokenModel.class);
        return tokenModel;
    }

    @Override
    public void setTokenModel(TokenModel tokenModel) {
        if (tokenModel != null) {
            Gson gson = new Gson();
            String json = gson.toJson(tokenModel);
            mPrefs.edit().putString(PREF_KEY_ACCESS_TOKEN, json).apply();
        } else {
            mPrefs.edit().putString(PREF_KEY_ACCESS_TOKEN, "").apply();
        }
    }

    @Override
    public UserModel getUserModel() {
        Gson gson = new Gson();
        String json = mPrefs.getString(PREF_KEY_USER, "");
        UserModel userModel = gson.fromJson(json, UserModel.class);
        return userModel;
    }

    @Override
    public void setUserModel(UserModel userModel) {
        if (userModel != null) {
            Gson gson = new Gson();
            String json = gson.toJson(userModel);
            mPrefs.edit().putString(PREF_KEY_USER, json).apply();
        } else {
            mPrefs.edit().putString(PREF_KEY_USER, "").apply();
        }
    }

    @Override
    public NickPassModel getNickPass() {
        Gson gson = new Gson();
        String json = mPrefs.getString(NICK_PASS_USER, "");
        NickPassModel nickPassModel = gson.fromJson(json, NickPassModel.class);
        return nickPassModel;
    }

    @Override
    public void setNickPass(NickPassModel user) {
        if (user != null) {
            Gson gson = new Gson();
            String json = gson.toJson(user);
            mPrefs.edit().putString(NICK_PASS_USER, json).apply();
        } else {
            mPrefs.edit().putString(NICK_PASS_USER, "").apply();
        }
    }

    @Override
    public boolean getRemember() {
        return mPrefs.getBoolean(REMEMBER_USER, false);
    }

    @Override
    public void setRemember(boolean check) {
        mPrefs.edit().putBoolean(REMEMBER_USER, check).apply();
    }

    @Override
    public String getGiaNiemYet(String type) {
        String gia = mPrefs.getString(type, "");
        return gia;
    }

    @Override
    public void setGiaNiemYet(String price, String type) {
        if (price != null) {
            mPrefs.edit().putString(type, price).apply();
        } else {
            mPrefs.edit().putString(type, "").apply();
        }
    }


    public void putBiometric(boolean putBiometric) {
        mPrefs.edit().putBoolean(LOGIN_BIOMETRIC, putBiometric).apply();
    }

    public boolean getBiometric() {
        return mPrefs.getBoolean(LOGIN_BIOMETRIC, false);
    }


//    public void setOldAccount(String mAcountName) {
//        if (mContext != null) {
//            try {
//                String acountName = SecurityCrypt.encrypt(mContext, mAcountName);
//                mPrefs.edit().putString(OLD_ACCOUNT, acountName).apply();
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//        }
//    }
//
//    public String getOldAccount() {
//        if (mContext != null) {
//            try {
//                return SecurityCrypt.decrypt(mContext, mPrefs.getString(OLD_ACCOUNT, ""));
//            } catch (Exception e) {
//            //    e.printStackTrace();
//                return "";
//            }
//        }
//        return "";
//    }
//
//
//    public void setOldPassword(String mpass) {
//        if (mContext != null) {
//            try {
//                String passEncrypt = SecurityCrypt.encrypt(mContext, mpass);
//                mPrefs.edit().putString(OLD_PASSWORD, passEncrypt).apply();
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//        }
//    }
//
//
//    public String getOldPassword() {
//        if (mContext != null) {
//            try {
//                return SecurityCrypt.decrypt(mContext, mPrefs.getString(OLD_PASSWORD, ""));
//            } catch (Exception e) {
//               // e.printStackTrace();
//                return "";
//            }
//        }
//        return "";
//    }

    public void saveString(String key, String value) {
        if (mContext != null) {
            try {
                mPrefs.edit().putString(key, value).apply();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public String getString(String key) {
        if (mContext != null) {
            try {
                return mPrefs.getString(key, "");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return "";
    }
}
