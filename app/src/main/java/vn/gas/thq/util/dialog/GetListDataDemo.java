package vn.gas.thq.util.dialog;

import android.content.Context;

import java.util.ArrayList;

import vn.hongha.ga.R;

public class GetListDataDemo {

    public static ArrayList<DialogListModel> getListSex(Context mContext) {
        ArrayList<DialogListModel> modelArrayList = new ArrayList<>();
//        modelArrayList.add(new DialogListModel("1", mContext.getString(R.string.boy)));
//        modelArrayList.add(new DialogListModel("2", mContext.getString(R.string.girl)));
//        modelArrayList.add(new DialogListModel("3", mContext.getString(R.string.other)));
        return modelArrayList;
    }

    public static ArrayList<DialogListModel> getListRequestType(Context mContext) {
        ArrayList<DialogListModel> modelArrayList = new ArrayList<>();
        modelArrayList.add(new DialogListModel("1", mContext.getString(R.string.type_xk)));
        modelArrayList.add(new DialogListModel("2", mContext.getString(R.string.type_ban_le)));
        modelArrayList.add(new DialogListModel("3", mContext.getString(R.string.type_ban_le_tdl)));
        return modelArrayList;
    }

    public static ArrayList<DialogListModel> getListStatus(Context mContext) {
        ArrayList<DialogListModel> modelArrayList = new ArrayList<>();
        modelArrayList.add(new DialogListModel("-2", mContext.getString(R.string.all)));
        modelArrayList.add(new DialogListModel("NEW", mContext.getString(R.string.new_status)));
        modelArrayList.add(new DialogListModel("APPROVED", mContext.getString(R.string.approved_status)));
        modelArrayList.add(new DialogListModel("REJECTED", mContext.getString(R.string.reject_status)));
        modelArrayList.add(new DialogListModel("CANCELLED", mContext.getString(R.string.cancel_status)));
        return modelArrayList;
    }

    public static ArrayList<DialogListModel> getListLinkAccount(Context mContext) {
        ArrayList<DialogListModel> modelArrayList = new ArrayList<>();
        modelArrayList.add(new DialogListModel("E_WALLET", "Ví điện tử"));
        return modelArrayList;
    }

    public static ArrayList<DialogListModel> getContractStatus(Context mContext) {
        ArrayList<DialogListModel> modelArrayList = new ArrayList<>();
//        modelArrayList.add(new DialogListModel("1", mContext.getString(R.string.chua_hoat_dong)));
//        modelArrayList.add(new DialogListModel("2", mContext.getString(R.string.hoat_dong)));
//        modelArrayList.add(new DialogListModel("3", mContext.getString(R.string.huy)));
//        modelArrayList.add(new DialogListModel("4", mContext.getString(R.string.cham_dut)));
        return modelArrayList;
    }

    public static ArrayList<DialogListModel> getTramdoan() {
        ArrayList<DialogListModel> modelArrayList = new ArrayList<>();
      /*  modelArrayList.add(new DialogListModel("Pháp Vân - Cầu Gié"));
        modelArrayList.add(new DialogListModel("Xala - Nguyễn Xiển"));
        modelArrayList.add(new DialogListModel("Mỹ Đình - Cầu Giấy"));
        modelArrayList.add(new DialogListModel("Hồ Tây - Hàng Bài"));*/

        return modelArrayList;
    }

    public static ArrayList<DialogListModel> getDocumentType() {
        ArrayList<DialogListModel> modelArrayList = new ArrayList<>();
        /*modelArrayList.add(new DialogListModel("CMND"));
        modelArrayList.add(new DialogListModel("Hợp đồng"));
        modelArrayList.add(new DialogListModel("Bằng lái xe"));
        modelArrayList.add(new DialogListModel("Mã nv"));*/

        return modelArrayList;
    }

    public static ArrayList<DialogListModel> getTransferReason() {
        ArrayList<DialogListModel> modelArrayList = new ArrayList<>();
   /*     modelArrayList.add(new DialogListModel("Khách hàng đề nghị"));
        modelArrayList.add(new DialogListModel("Chủ xe đề nghị"));
        modelArrayList.add(new DialogListModel("Lý do khác"));*/

        return modelArrayList;
    }

    public static ArrayList<DialogListModel> getMonth() {
        ArrayList<DialogListModel> modelArrayList = new ArrayList<>();
    /*    modelArrayList.add(new DialogListModel("Tháng 1"));
        modelArrayList.add(new DialogListModel("Tháng 2"));
        modelArrayList.add(new DialogListModel("Tháng 3"));
        modelArrayList.add(new DialogListModel("Tháng 4"));
        modelArrayList.add(new DialogListModel("Tháng 5"));
        modelArrayList.add(new DialogListModel("Tháng 6"));
        modelArrayList.add(new DialogListModel("Tháng 7"));
        modelArrayList.add(new DialogListModel("Tháng 8"));
        modelArrayList.add(new DialogListModel("Tháng 9"));
        modelArrayList.add(new DialogListModel("Tháng 10"));
        modelArrayList.add(new DialogListModel("Tháng 11"));
        modelArrayList.add(new DialogListModel("Tháng 12"));*/

        return modelArrayList;
    }
}
