package com.shoppers_era.Adapters;

import android.content.Context;
import android.graphics.Paint;
import com.shoppers_era.Model.Selection;
import android.support.v7.widget.RecyclerView;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;
import com.shoppers_era.R;
import com.squareup.picasso.Picasso;
import java.util.List;

public class Cat_Image_Adapter extends RecyclerView.Adapter<Cat_Image_Adapter.MyViewHolder> {

    private List<Selection> images;
    private Context mContext;
    private MyViewHolder myViewHolder;
    private ClickListener listener;

    public Cat_Image_Adapter() {
        //do nothing
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        private ImageView fav;
        public TextView price;
        private TextView price_original;
        private TextView cat_name_text;
        public ImageView cat_image;
        private CheckBox imageView;
        private TextView count_all;


        public MyViewHolder(View view) {
            super(view);

            cat_image = (ImageView) view.findViewById(R.id.thumbnail);
            imageView = (CheckBox) view.findViewById(R.id.tick);

            cat_image.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onImgClick(v,getAdapterPosition());
                }
            });

            fav = (ImageView) view.findViewById(R.id.fav_add);

            fav.setTag(getAdapterPosition());

            count_all = (TextView) view.findViewById(R.id.count_all);

            fav.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onFavClick(v,getAdapterPosition(),count_all);
                }
            });

            price = (TextView) view.findViewById(R.id.price);
            price_original =(TextView) view.findViewById(R.id.price_original);
            cat_name_text = (TextView) view.findViewById(R.id.cat_name_text);
        }
    }


    public Cat_Image_Adapter(Context mContext,List<Selection> images,ClickListener listener) {
        this.mContext = mContext;
        this.images = images;
        this.listener=listener;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.gallery_thumbnail, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {

        myViewHolder = holder;

        Selection selection = images.get(position);

        final int pos = position;

        holder.cat_image.setTag(position);

        holder.imageView.setTag(images.get(position));
        holder.imageView.setChecked(images.get(position).getSelect());

        holder.count_all.setText(selection.getCount_all());

        holder.imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                CheckBox cb = (CheckBox) v;
                Selection contact = (Selection) cb.getTag();

                contact.setSelect(cb.isChecked());
                images.get(pos).setSelect(cb.isChecked());
                if (cb.isChecked()){
                    holder.cat_image.setBackgroundResource(R.drawable.black_border);
                }else {
                    holder.cat_image.setBackgroundResource(R.color.white);
                }
            }
        });

        if (selection.getLiked()){
            holder.fav.setImageResource(R.drawable.heart_re);
        }else {
            holder.fav.setImageResource(R.drawable.heart_outline);
        }

        Picasso.with(mContext).load(selection.getImg_url()).placeholder(R.drawable.phimg).noFade().into(holder.cat_image);

        holder.price.setText("₹"+selection.getPrice());

        holder.price_original.setText("₹"+selection.getActual_price());

        if(selection.getActual_price().equals("0.00")){
            holder.price_original.setVisibility(View.GONE);
        }

        holder.cat_name_text.setText(selection.getImage_name());

        holder.price_original.setPaintFlags(holder.price_original.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
    }

    @Override
    public int getItemCount() {
        return images.size();
    }

    public void selectAll(){

        for (int i=0;i<images.size();i++){
            myViewHolder.imageView.setTag(i+1);
            images.get(i).setSelect(true);
        }

        notifyDataSetChanged();
    }

    public void deselectAll(){

        for (int i=0;i<images.size();i++){
            myViewHolder.imageView.setTag(i+1);
            images.get(i).setSelect(false);

        }

    notifyDataSetChanged();
    }

    public interface ClickListener {
        void onFavClick(View v,int pos,View v2);
        void onClick(View view, int position);
        void onImgClick(View view,int position);
        void onLongClick(View view, int position);
    }

    public static class RecyclerTouchListener implements RecyclerView.OnItemTouchListener {

        private GestureDetector gestureDetector;
        private Cat_Image_Adapter.ClickListener clickListener;

        public RecyclerTouchListener(Context context, final RecyclerView recyclerView, final Cat_Image_Adapter.ClickListener clickListener) {
            this.clickListener = clickListener;
            gestureDetector = new GestureDetector(context, new GestureDetector.SimpleOnGestureListener() {
                @Override
                public boolean onSingleTapUp(MotionEvent e) {
                    return true;
                }

                @Override
                public void onLongPress(MotionEvent e) {
                    View child = recyclerView.findChildViewUnder(e.getX(), e.getY());
                    if (child != null && clickListener != null) {
                        clickListener.onLongClick(child, recyclerView.getChildPosition(child));
                    }
                }
            });
        }



        @Override
        public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {

            View child = rv.findChildViewUnder(e.getX(), e.getY());
            if (child != null && clickListener != null && gestureDetector.onTouchEvent(e)) {
                clickListener.onClick(child, rv.getChildPosition(child));
            }

            return false;
        }

        @Override
        public void onTouchEvent(RecyclerView rv, MotionEvent e){

        }

        @Override
        public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {

        }
    }

}
