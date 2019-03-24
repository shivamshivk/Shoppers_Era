package com.shoppers_era.Adapters;

import android.content.Context;
import android.graphics.Typeface;
import com.shoppers_era.Model.Diary;
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
import android.support.v7.widget.RecyclerView;

public class Diary_Adapter extends RecyclerView.Adapter<Diary_Adapter.MyViewHolder> {

    private List<Diary> favs;
    private Context mContext;
    private ClickListener listener;
    private MyViewHolder viewHolder;

    public class MyViewHolder extends RecyclerView.ViewHolder {

        public TextView price;
        private TextView cat_name_text;
        public ImageView cat_image;
        private CheckBox imageView;

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

            price = (TextView) view.findViewById(R.id.price);
            cat_name_text = (TextView) view.findViewById(R.id.cat_name_text);

        }
    }

    public Diary_Adapter(Context mContext,List<Diary> favs,ClickListener listener) {
        this.mContext = mContext;
        this.favs = favs;
        this.listener = listener;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.diary_layout, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {

        Diary diary = favs.get(position);

        viewHolder = holder;

        final int pos = position;

        holder.cat_image.setTag(position);

        holder.imageView.setTag(favs.get(position));
        holder.imageView.setChecked(diary.getSelect());

        holder.imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                CheckBox cb = (CheckBox) v;
                Diary contact = (Diary) cb.getTag();

                contact.setSelect(cb.isChecked());
                favs.get(pos).setSelect(cb.isChecked());
                if (cb.isChecked()){
                    holder.cat_image.setBackgroundResource(R.drawable.black_border);
                }else {
                    holder.cat_image.setBackgroundResource(R.color.white);
                }
            }
        });


        Picasso.with(mContext).load(diary.getImg_url()).placeholder(R.drawable.phimg).noFade().into(holder.cat_image);

        holder.price.setText(diary.getContent2());
        holder.cat_name_text.setText(diary.getContent1());


        Typeface tf = Typeface.createFromAsset(mContext.getAssets(), "fonts/arial.ttf");
        holder.price.setTypeface(tf);
        holder.cat_name_text.setTypeface(tf);

}


    @Override
    public int getItemCount() {
        return favs.size();
    }

    public interface ClickListener {
        void onFavClick(View v,int pos,View v2);
        void onClick(View view, int position);
        void onImgClick(View view,int position);
        void onLongClick(View view, int position);
    }

    public static class RecyclerTouchListener implements RecyclerView.OnItemTouchListener {

        private GestureDetector gestureDetector;
        private Diary_Adapter.ClickListener clickListener;

        public RecyclerTouchListener(Context context, final RecyclerView recyclerView, final Diary_Adapter.ClickListener clickListener) {
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

    public void selectAll(){

        for (int i=0;i<favs.size();i++){
            viewHolder.imageView.setTag(i+1);
            favs.get(i).setSelect(true);
        }
        notifyDataSetChanged();
    }

    public void deselectAll(){

        for (int i=0;i<favs.size();i++){
            viewHolder.imageView.setTag(i+1);
            favs.get(i).setSelect(false);
        }

        notifyDataSetChanged();
    }


}