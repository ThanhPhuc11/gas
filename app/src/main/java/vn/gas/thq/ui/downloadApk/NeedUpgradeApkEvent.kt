package vn.gas.thq.ui.downloadApk

class NeedUpgradeApkEvent {
    companion object {
        var MOVE_LOGIN_SCREEN = 0;
        var SHOW_POPUP = 1;
    }

    var mType = 0;
    var url = "";
    // 0 dua ve man hinh login
    // 1 hien thi dialog nang cap phien ban

    constructor(type: Int, path: String?) {
        this.mType = type
        if (path != null) {
            this.url = path
        }
    }
}