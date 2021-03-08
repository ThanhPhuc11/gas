package vn.gas.thq.datasourse.prefs;

import vn.gas.thq.model.NickPassModel;
import vn.gas.thq.model.TokenModel;
import vn.gas.thq.model.UserModel;

public interface PreferencesHelper {
    TokenModel getTokenModel();

    void setTokenModel(TokenModel tokenModel);

    UserModel getUserModel();

    void setUserModel(UserModel userModel);

    NickPassModel getNickPass();

    void setNickPass(NickPassModel user);

    boolean getRemember();

    void setRemember(boolean check);

    String getGiaNiemYet(String type);

    void setGiaNiemYet(String price, String type);
}
