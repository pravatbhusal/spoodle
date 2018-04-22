package com.shadowsych.spoodle.assets;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.shadowsych.spoodle.R;

import java.util.ArrayList;

//an adapter that manages the layout of the business category
public class BusinessCategoryItemAdapter extends RecyclerView.Adapter<BusinessCategoryItemAdapter.BusinessCategoryItemHolder> {
    private Context mContext;
    private ArrayList<BusinessCategoryItem> mBusinessCategoryItemsList;
    private onItemClickListener mListener;

    //interface to receive the item clicked
    public interface onItemClickListener {
        void onItemClick(int position);
    }

    //set the item clicked
    public void setOnItemClickListener(onItemClickListener listener) {
        mListener = listener;
    }

    public BusinessCategoryItemAdapter(Context context, ArrayList<BusinessCategoryItem> BusinessCategoryList) {
        mContext = context;
        mBusinessCategoryItemsList = BusinessCategoryList;
    }

    //get the layout view from the business_category_item.xml file
    @Override
    public BusinessCategoryItemHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.business_category_item, parent, false);
        return new BusinessCategoryItemHolder(view);
    }

    //set the category item and price text to the business category item
    @Override
    public void onBindViewHolder(BusinessCategoryItemHolder holder, int position) {
        BusinessCategoryItem currentBusinessCategoryItem = mBusinessCategoryItemsList.get(position);
        String categoryItemText = currentBusinessCategoryItem.getCategoryItem();
        String categoryItemPriceText = "$" + currentBusinessCategoryItem.getCategoryItemPrice();
        if(currentBusinessCategoryItem.getCategoryItemPrice() == 0) {
            categoryItemPriceText = "FREE";
        }
        holder.mCategoryItemText.setText(categoryItemText);
        holder.mCategoryItemPriceText.setText(categoryItemPriceText);
    }

    //get number of items in this adapter
    @Override
    public int getItemCount() {
        return mBusinessCategoryItemsList.size();
    }

    //holder of the views within the business_category_item.xml
    public class BusinessCategoryItemHolder extends RecyclerView.ViewHolder {
        public TextView mCategoryItemText;
        public TextView mCategoryItemPriceText;

        public BusinessCategoryItemHolder(View itemView) {
            super(itemView);
            mCategoryItemText = itemView.findViewById(R.id.categoryItemText);
            mCategoryItemPriceText = itemView.findViewById(R.id.categoryPriceItemText);
            //asynchronous onclick listener when clicking a business category item
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(mListener != null) {
                        int position = getAdapterPosition();
                        if(position != RecyclerView.NO_POSITION) {
                            mListener.onItemClick(position);
                        }
                    }
                }
            });
        }
    }
}
