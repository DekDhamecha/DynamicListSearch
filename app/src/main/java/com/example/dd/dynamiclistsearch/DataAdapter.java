package com.example.dd.dynamiclistsearch;

import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.Typeface;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.TextAppearanceSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class DataAdapter extends RecyclerView.Adapter<DataAdapter.DataHolder> implements Filterable {

    private List<DataModel> dataList;
    private List<DataModel> dataListUnFilter;

    private RecyclerListener mRecyclerListener;
    private SearchFilter mSearchFilter;

    private String mSearchTerm;

    DataAdapter(List<DataModel> dataList) {
        this.dataList = dataList;
        this.dataListUnFilter = new ArrayList<>();
    }

    void setRecyclerListener(@NonNull RecyclerListener mRecyclerListener) {
        this.mRecyclerListener = mRecyclerListener;
    }

    @NonNull
    @Override
    public DataHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.data_model, viewGroup, false);
        return new DataHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull DataHolder dataHolder, int i) {
        DataModel data = dataList.get(i);

        dataHolder.tvSubtitle.setText(data.getSubtitle());

        String title = data.getTitle();

        if (TextUtils.isEmpty(mSearchTerm)) {
            dataHolder.tvTitle.setText(title);
        } else {
            int startPos = title.toLowerCase().indexOf(mSearchTerm.toLowerCase());
            int endPos = startPos + mSearchTerm.length();

            if (startPos != -1) {
                Spannable spannable = new SpannableString(title);
                ColorStateList blueColor = new ColorStateList(new int[][]{new int[]{}}, new int[]{Color.BLUE});
                TextAppearanceSpan highlightText = new TextAppearanceSpan(null, Typeface.NORMAL, -1, blueColor, null);
                spannable.setSpan(highlightText, startPos, endPos, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                dataHolder.tvTitle.setText(spannable);
            } else {
                dataHolder.tvTitle.setText(title);
            }
        }
    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }

    // Filterable method
    @Override
    public Filter getFilter() {

        if (mSearchFilter == null) {
            dataListUnFilter.clear();
            dataListUnFilter.addAll(this.dataList);
            mSearchFilter = new SearchFilter(dataListUnFilter);
        }
        return mSearchFilter;
    }
    

    /**
     * Custom Filter class to overtake List Items issue
     * Delete Items while searching that are revised on refresh search
     */
    class SearchFilter extends Filter {

        private List<DataModel> listToFilter;

        SearchFilter(List<DataModel> listToFilter) {
            this.listToFilter = listToFilter;
        }

        // Update Base Search List on Item Removed while searching
        void updateList(DataModel dataModel) {
            listToFilter.remove(dataModel);
        }

        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            String searchString = constraint.toString().toLowerCase().trim();
            mSearchTerm = searchString;

            FilterResults results = new FilterResults();

            if (TextUtils.isEmpty(searchString)) {
                results.values = listToFilter;
            } else {
                List<DataModel> filteredList = new ArrayList<>();

                for (DataModel dm : listToFilter) {
                    if (dm.getTitle().toLowerCase().contains(searchString)) {
                        filteredList.add(dm);
                    }
                }
                results.values = filteredList;
            }
            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            //noinspection unchecked
            dataList = (List<DataModel>) results.values;
            notifyDataSetChanged();
        }
    }

    /**
     * ViewHolder class of DataModel
     */
    class DataHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private TextView tvTitle, tvSubtitle;
        private ImageButton btnDelete;

        DataHolder(@NonNull View itemView) {
            super(itemView);
            tvTitle = itemView.findViewById(R.id.tvTitle);
            tvSubtitle = itemView.findViewById(R.id.tvSubTitle);
            btnDelete = itemView.findViewById(R.id.btnDelete);

            btnDelete.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {

            final int pos = getAdapterPosition();
            DataModel dataModel = dataList.get(pos);

            mRecyclerListener.onItemClick(v, pos, dataModel);

            // Remove item from Adapter List and notify
            dataList.remove(pos);
            notifyItemRemoved(pos);

            // Remove Object from Original Data from SearchFilter Class
            if (mSearchFilter != null) {
                mSearchFilter.updateList(dataModel);
            }
        }
    }


    public interface RecyclerListener {
        void onItemClick(View view, int position, DataModel dataModel);
    }
}
