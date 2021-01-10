package vn.gas.thq.datasourse.prefs;

import vn.gas.thq.model.TokenModel;
import vn.gas.thq.model.UserModel;

public interface PreferencesHelper {
    TokenModel getTokenModel();

    void setTokenModel(TokenModel tokenModel);

    UserModel getUserModel();

    void setUserModel(UserModel userModel);
}
