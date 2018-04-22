package com.shadowsych.spoodle.assets;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.shadowsych.spoodle.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

//an adapter that manages the layout of the business cards
public class BusinessCardAdapter extends RecyclerView.Adapter<BusinessCardAdapter.BusinessCardViewHolder> {
    private Context mContext;
    private ArrayList<BusinessCard> mBusinessList;
    private onItemClickListener mListener;

    //interface to receive the item clicked
    public interface onItemClickListener {
        void onItemClick(int position);
    }

    //set the item clicked
    public void setOnItemClickListener(onItemClickListener listener) {
        mListener = listener;
    }

    public BusinessCardAdapter(Context context, ArrayList<BusinessCard> BusinessList) {
        mContext = context;
        mBusinessList = BusinessList;
    }

    //get the layout view from the business_card.xml file
    @Override
    public BusinessCardViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.business_card, parent, false);
        return new BusinessCardViewHolder(view);
    }

    //set the texts, images, etc. to the business card
    @Override
    public void onBindViewHolder(BusinessCardViewHolder holder, int position) {
        BusinessCard currentBusinessCard = mBusinessList.get(position);
        String businessImage = currentBusinessCard.getBusinessImage();
        String timeText = currentBusinessCard.getTimeText();
        String titleText = currentBusinessCard.getTitleText();

        Picasso.with(mContext).load(businessImage).fit().centerInside().into(holder.mBusinessImage);
        holder.mTimeText.setText(timeText);
        holder.mTitleText.setText(titleText);
        //if the business card is not promoted, then make the promotion star invisible
        if(!currentBusinessCard.getIsPromoted()) {
            holder.mPromotionImage.setVisibility(View.GONE);
        }
    }

    //get number of items in this adapter
    @Override
    public int getItemCount() {
        return mBusinessList.size();
    }

    //holder of the views within the business_card.xml
    public class BusinessCardViewHolder extends RecyclerView.ViewHolder {
        public ImageView mBusinessImage;
        public TextView mTimeText;
        public TextView mTitleText;
        public ImageView mPromotionImage;

        public BusinessCardViewHolder(View itemView) {
            super(itemView);
            mBusinessImage = itemView.findViewById(R.id.businessImage);
            mTimeText = itemView.findViewById(R.id.timeText);
            mTitleText = itemView.findViewById(R.id.titleText);
            mPromotionImage = itemView.findViewById(R.id.promotionImage);
            //asynchronous onclick listener when clicking a business card
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
