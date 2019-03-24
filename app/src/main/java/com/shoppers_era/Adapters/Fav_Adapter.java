package com.shoppers_era.Adapters;


import android.content.Context;
import android.graphics.Paint;
import android.graphics.Typeface;
import com.shoppers_era.Model.Fav;
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

public class Fav_Adapter extends RecyclerView.Adapter<Fav_Adapter.MyViewHolder> {

    private List<Fav> favs;
    private Context mContext;
    private ClickListener clickListener;
    private MyViewHolder myViewHolder;

    public class MyViewHolder extends RecyclerView.ViewHolder {

        public ImageView cat_image;
        public TextView price;
        private TextView price_original;
        private CheckBox imageView;
        private ImageView fav;
        private TextView count_all;
        private TextView cat_name_text;

        public MyViewHolder(View view) {
            super(view);
            cat_image = (ImageView) view.findViewById(R.id.thumbnail);
            cat_image.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    clickListener.onImgClick(v,getAdapterPosition());
                }
            });
            price = (TextView) view.findViewById(R.id.price);
            price_original =(TextView) view.findViewById(R.id.price_original);
            imageView = (CheckBox) view.findViewById(R.id.tick);
            fav = (ImageView) view.findViewById(R.id.fav_add);
            count_all = (TextView) view.findViewById(R.id.count_all);

            fav.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    clickListener.onFavClick(v,getAdapterPosition(),count_all);
                }
            });

            cat_name_text = (TextView) view.findViewById(R.id.cat_name_text);

        }
    }

    public Fav_Adapter(Context mContext,List<Fav> favs,ClickListener clickListener) {
        this.mContext = mContext;
        this.favs = favs;
        this.clickListener = clickListener;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fav_layout, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {

        myViewHolder = holder;

        final Fav fav = favs.get(position);

        final int pos = position;

        holder.cat_image.setTag(position);

        holder.imageView.setTag(favs.get(position));
        holder.imageView.setChecked(fav.getSelect());

        holder.imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                CheckBox cb = (CheckBox) v;
                Fav contact = (Fav) cb.getTag();

                contact.setSelect(cb.isChecked());
                favs.get(pos).setSelect(cb.isChecked());
                if (cb.isChecked()){
                    holder.cat_image.setBackgroundResource(R.drawable.black_border);
                }else {
                    holder.cat_image.setBackgroundResource(R.color.white);
                }
            }
        });

        Picasso.with(mContext).load(fav.getImg_url()).placeholder(R.drawable.phimg).into(holder.cat_image);

        holder.price.setText("₹"+fav.getSelling_price());
        holder.price_original.setText("₹"+fav.getActual_price());

        holder.price_original.setPaintFlags(holder.price_original.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);

        holder.cat_name_text.setText(fav.getImage_name());
        holder.count_all.setText(fav.getCount_all());

        if (fav.getLiked())
            holder.fav.setImageResource(R.drawable.heart_re);

        holder.count_all.setText(fav.getCount_all());

        if(fav.getActual_price().equals("0.00")){
            holder.price_original.setVisibility(View.GONE);
        }

        Typeface tf = Typeface.createFromAsset(mContext.getAssets(), "fonts/arial.ttf");

        holder.price.setTypeface(tf);
        holder.price_original.setTypeface(tf);
    }

    public void selectAll(){

        for (int i=0;i<favs.size();i++){
            myViewHolder.imageView.setTag(i+1);
            favs.get(i).setSelect(true);
        }
        notifyDataSetChanged();
    }

    public void deselectAll(){

        for (int i=0;i<favs.size();i++){
            myViewHolder.imageView.setTag(i+1);
            favs.get(i).setSelect(false);
        }

        notifyDataSetChanged();
    }


    @Override
    public int getItemCount() {
        return favs.size();
    }

    public interface ClickListener {
        void onClick(View view, int position);

        void onLongClick(View view, int position);

        void onImgClick(View view,int position);

        void onFavClick(View v,int pos,View v2);

    }

    public static class RecyclerTouchListener implements RecyclerView.OnItemTouchListener {

        private GestureDetector gestureDetector;
        private Fav_Adapter.ClickListener clickListener;

        public RecyclerTouchListener(Context context, final RecyclerView recyclerView, final Fav_Adapter.ClickListener clickListener) {
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
