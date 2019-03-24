package com.shoppers_era.Adapters;

import android.content.Context;
import android.graphics.Paint;
import android.graphics.Typeface;
import customfonts.Zoom.TouchImageView;
import com.shoppers_era.Model.Selection;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.shoppers_era.R;
import com.squareup.picasso.Picasso;
import java.util.List;

public class Img_Adapter extends RecyclerView.Adapter<Img_Adapter.MyViewHolder> {

    private List<Selection> images;
    private Context mContext;
    private Clicklistener listener;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TouchImageView cat_image;
        private ImageView fav;
        public TextView price;
        private TextView price_original;
        private TextView cat_name_text;

        public MyViewHolder(View view) {
            super(view);
            cat_image = (TouchImageView) view.findViewById(R.id.image_preview);
            cat_image.setMaxZoom(4f);

            cat_image.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onImgClick(v,getAdapterPosition());
                }
            });

            fav = (ImageView) view.findViewById(R.id.fav_add);

            fav.setTag(getAdapterPosition());

            fav.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onFavClick(v,getAdapterPosition());
                }
            });

            price = (TextView) view.findViewById(R.id.price);
            price_original =(TextView) view.findViewById(R.id.price_original);
            cat_name_text = (TextView) view.findViewById(R.id.cat_name_text);
        }
    }


    public Img_Adapter(Context mContext,List<Selection> images,Clicklistener listener) {
        this.mContext = mContext;
        this.images = images;
        this.listener=listener;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.full_screen_img, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {

        Selection selection = images.get(position);

        final int pos = position;


        holder.cat_image.setTag(position);

        if (selection.getLiked()){
            holder.fav.setImageResource(R.drawable.heart_re);
        }else {
            holder.fav.setImageResource(R.drawable.heart_outline);
        }

        Picasso.with(mContext).load(selection.getImg_url()).placeholder(R.drawable.phimg).into(holder.cat_image);

        holder.price.setText("₹"+selection.getPrice());
        holder.price_original.setText("₹"+selection.getActual_price());
        holder.cat_name_text.setText(selection.getCat_name());

        holder.price_original.setPaintFlags(holder.price_original.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);

        if(selection.getActual_price().equals("0.00")){
            holder.price_original.setVisibility(View.GONE);
        }

        Typeface tf = Typeface.createFromAsset(mContext.getAssets(), "fonts/arial.ttf");
        holder.price.setTypeface(tf);
        holder.price_original.setTypeface(tf);
        holder.cat_name_text.setTypeface(tf);
    }

    public interface Clicklistener{
        void onFavClick(View v,int pos);

        void onImgClick(View v ,int pos);
    }


    @Override
    public int getItemCount() {
        return images.size();
    }


}
