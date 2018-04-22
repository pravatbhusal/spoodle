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
public class BusinessCategoryAdapter extends RecyclerView.Adapter<BusinessCategoryAdapter.BusinessCategoryHolder> {
    private Context mContext;
    private ArrayList<BusinessCategory> mBusinessCategoryList;
    private onItemClickListener mClickListener;
    private onItemLongClickListener mLongClickListener;

    //interface to receive the category clicked
    public interface onItemClickListener {
        void onItemClick(int position);
    }

    //interface to receive the category held
    public interface onItemLongClickListener {
        void onItemLongClick(int position);
    }

    //set the category clicked
    public void setOnItemClickListener(onItemClickListener listener) {
        mClickListener = listener;
    }

    //set the category held
    public void setOnItemLongClickListener(onItemLongClickListener listener) {
        mLongClickListener = listener;
    }

    public BusinessCategoryAdapter(Context context, ArrayList<BusinessCategory> BusinessCategoryList) {
        mContext = context;
        mBusinessCategoryList = BusinessCategoryList;
    }

    //get the layout view from the business_category.xml file
    @Override
    public BusinessCategoryHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.business_category, parent, false);
        return new BusinessCategoryHolder(view);
    }

    //set the business category text to the business category
    @Override
    public void onBindViewHolder(BusinessCategoryHolder holder, int position) {
        BusinessCategory currentBusinessCategory = mBusinessCategoryList.get(position);
        String categoryText = currentBusinessCategory.getCategoryText();
        holder.mCategoryText.setText(categoryText);
    }

    //get number of items in this adapter
    @Override
    public int getItemCount() {
        return mBusinessCategoryList.size();
    }

    //holder of the views within the business_category.xml
    public class BusinessCategoryHolder extends RecyclerView.ViewHolder {
        public TextView mCategoryText;

        public BusinessCategoryHolder(View itemView) {
            super(itemView);
            mCategoryText = itemView.findViewById(R.id.categoryText);
            //asynchronous onclick listener when clicking a business category
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(mClickListener != null) {
                        int position = getAdapterPosition();
                        if(position != RecyclerView.NO_POSITION) {
                            mClickListener.onItemClick(position);
                        }
                    }
                }
            });
            //asynchronous onlongclick listener when holding a business category
            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    if(mLongClickListener != null) {
                        int position = getAdapterPosition();
                        if(position != RecyclerView.NO_POSITION) {
                            mLongClickListener.onItemLongClick(position);
                        }
                    }
                    return true;
                }
            });
        }
    }
}
