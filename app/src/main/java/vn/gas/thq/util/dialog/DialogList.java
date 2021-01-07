package vn.gas.thq.util.dialog;

import android.app.Activity;
import android.os.Handler;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Objects;

import vn.gas.thq.util.AppConstants;
import vn.hongha.ga.R;

public class DialogList {
    public AlertDialog alertDialog = null;

    private void dismissAlertDialog() {
        if (alertDialog != null) {
            try {
                alertDialog.dismiss();
            } catch (Exception e) {
            }
        }
    }

    public void displayNotSelect() {
        if (adapter != null) {
            adapter.displayNotSelect();
        }
    }

    DialogListAdapter adapter;

    public void show(Activity context
            , ArrayList<DialogListModel> list
            , String title
            , String hint
            , DialogListCallback callback) {
        if (list == null) return;
        //dismissAlertDialog();

        if (alertDialog != null && adapter != null) {
            adapter.refresh(list);
            alertDialog.show();
            return;
        }

        if (title != null) {
            title = title.replace("*", "");
        }

        // init inflater
        LayoutInflater inflater = context.getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_list_base, null);
        // init AlertDialog
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(context, R.style.AlertDialogNoBG);
        dialogBuilder.setView(dialogView);

        // find view
        TextView tvTitle = dialogView.findViewById(R.id.tvTitle);
        RecyclerView recyclerView = dialogView.findViewById(R.id.rv);
        tvTitle.setText(title);
        dialogView.findViewById(R.id.ivMappingClose).setOnClickListener(v -> {
            dismissAlertDialog();
        });
        // setup the RecyclerView
        LinearLayoutManager layoutManager = new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);
        adapter = new DialogListAdapter(context, list, (item) -> {
            dismissAlertDialog();
            // nếu chọn "Không chọn"
            if (context.getString(R.string.not_select).equals(item.name)) {
                item.id = AppConstants.NOT_SELECT;
            }
            callback.onCallback(item);
        });
        recyclerView.setAdapter(adapter);
        // search layout
        RelativeLayout rlSearch = dialogView.findViewById(R.id.rlSearch);
        EditText etSearch = dialogView.findViewById(R.id.etSearch);
        ImageView ivClear = dialogView.findViewById(R.id.ivSearch);
        if (!TextUtils.isEmpty(hint)) {
            etSearch.setHint(hint);
        }
        ivClear.setOnClickListener(v -> {
            etSearch.setText("");
        });
        etSearch.addTextChangedListener(new TextWatcher() {

            @Override
            public void afterTextChanged(Editable s) {
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence query, int start, int before, int count) {
                // filter recycler view when query submitted
                adapter.getFilter().filter(query.toString());
                ivClear.setVisibility(query.toString().length() > 0 ? View.VISIBLE : View.GONE);
            }
        });
        // check scrollable
        Handler handler = new Handler();
        handler.postDelayed(() -> {
            try {
                if (isRecyclerScrollable(recyclerView)) {
                    rlSearch.setVisibility(View.VISIBLE);
                } else {
                    rlSearch.setVisibility(View.GONE);
                }
            } catch (Exception e) {
            }
        }, 150);
        //show
        alertDialog = dialogBuilder.create();
        Objects.requireNonNull(alertDialog.getWindow()).getAttributes().windowAnimations = R.style.DialogAnimation;
        alertDialog.setCanceledOnTouchOutside(true);
        alertDialog.show();
    }


    /**
     * Check if RecyclerView scrollable
     *
     * @param recyclerView
     * @return
     */
    boolean isRecyclerScrollable(RecyclerView recyclerView) {
        LinearLayoutManager layoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
        RecyclerView.Adapter adapter = recyclerView.getAdapter();
        if (layoutManager == null || adapter == null) return false;

        return layoutManager.findLastCompletelyVisibleItemPosition() < adapter.getItemCount() - 1;
    }
}
