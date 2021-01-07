package vn.gas.thq.util.dialog;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import vn.gas.thq.util.AppConstants;
import vn.gas.thq.util.VNCharacterUtils2;
import vn.hongha.ga.R;

public class DialogListAdapter extends RecyclerView.Adapter<DialogListAdapter.MyViewHolder>
        implements Filterable {
    private Context context;
    private List<DialogListModel> contactList;
    private List<DialogListModel> contactListFiltered;
    private RvListAdapterListener listener;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView name;

        public MyViewHolder(View view) {
            super(view);
            name = view.findViewById(R.id.tv);
        }
    }

    // kiểm tra xem trong list đã có item Không chọn chưa
    private boolean hasNotSelect() {
        boolean isExisted = false;
        if (contactListFiltered != null && contactListFiltered.size() > 0) {
            for (int i = 0, n = contactListFiltered.size(); i < n; i++) {
                if (AppConstants.NOT_SELECT.equals(contactListFiltered.get(i).id)) {
                    isExisted = true;
                    break;
                }
            }
        }
        return isExisted;
    }

    // set dòng "Không chọn" vào đầu tiên của list
    public void displayNotSelect() {
        if (!hasNotSelect()) {
            DialogListModel dialogListModel = new DialogListModel(AppConstants.NOT_SELECT, context.getString(R.string.not_select));
            if (contactList != null && contactList.size() > 0 && !AppConstants.NOT_SELECT.equals(contactList.get(0).id)) {
                contactList.add(0, dialogListModel);
            }
            if (contactListFiltered != null && contactListFiltered.size() > 0 && !AppConstants.NOT_SELECT.equals(contactListFiltered.get(0).id)) {
               contactListFiltered.add(0, dialogListModel);
            }
            notifyDataSetChanged();
        }
    }

    public void refresh(List<DialogListModel> contactListNew) {
        // kiểm tra xem list cũ có chứa dòng "Không chọn" ko.
        boolean isExisted = hasNotSelect();
        //////////////////////
        this.contactList = contactListNew;
        this.contactListFiltered = contactListNew;
        // nếu ds cũ có "Không chọn" thì ds mới sẽ cũng phải có
        if (isExisted) {
            displayNotSelect();
        } else {
            notifyDataSetChanged();
        }
    }

    public DialogListAdapter(Context context, List<DialogListModel> contactList, RvListAdapterListener listener) {
        this.context = context;
        this.listener = listener;
        this.contactList = contactList;
        this.contactListFiltered = contactList;

        // sort theo alphabet
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.dialog_list_item_base, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        final DialogListModel contact = contactListFiltered.get(position);
        holder.name.setText(contact.name);

        holder.itemView.setOnClickListener(v -> {
            // send selected contact in callback
            for (int i = 0, n = contactList.size(); i < n; i++) {
                if (contact.id.equals(contactList.get(i).id)) {
                    listener.onContactSelected(contactListFiltered.get(position));
                    break;
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return contactListFiltered.size();
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                String charString = charSequence.toString();
                if (charString.isEmpty()) {
                    contactListFiltered = contactList;
                } else {
                    List<DialogListModel> filteredList = new ArrayList<>();
                    for (DialogListModel row : contactList) {

                        // name match condition. this might differ depending on your requirement
                        // here we are looking for name or phone number match

                        String check = "" + row.name;
                        if (VNCharacterUtils2.removeAccent(check.toLowerCase()).contains(VNCharacterUtils2.removeAccent(charString.toLowerCase()))) {
                            filteredList.add(row);
                        }
                    }
                    contactListFiltered = filteredList;
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = contactListFiltered;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                contactListFiltered = (ArrayList<DialogListModel>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }

    public interface RvListAdapterListener {
        void onContactSelected(DialogListModel contact);
    }
}