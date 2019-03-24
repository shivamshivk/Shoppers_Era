package com.shoppers_era.Adapters;


import android.content.Context;

import com.shoppers_era.Model.Notification;
import android.support.v7.widget.RecyclerView;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.shoppers_era.R;
import com.squareup.picasso.Picasso;

import java.util.List;

public class Notification_Adapter extends RecyclerView.Adapter<Notification_Adapter.MyViewHolder> {

    private List<Notification> notifications;
    private Context mContext;

    public class MyViewHolder extends RecyclerView.ViewHolder {

        public ImageView cat_image;
        public TextView t1;
        public TextView t2;
        public TextView date;
        public TextView time;

        public MyViewHolder(View view) {
            super(view);
            cat_image = (ImageView) view.findViewById(R.id.img);
            t1 = (TextView) view.findViewById(R.id.t1);
            t2 = (TextView) view.findViewById(R.id.t2);
            date = (TextView) view.findViewById(R.id.date);
            time = (TextView) view.findViewById(R.id.time);
        }
    }


    public Notification_Adapter(Context mContext,List<Notification> notifications) {
        this.mContext = mContext;
        this.notifications = notifications;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.notification_layout, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        Notification notification = notifications.get(position);

        Picasso.with(mContext).load(notification.getImg_url()).placeholder(R.drawable.phimg).into(holder.cat_image);
        holder.t1.setText(notification.getTitle());
        holder.t2.setText(notification.getDesc());
        holder.date.setText(notification.getDate());
        holder.time.setText(notification.getTime());
   }

    @Override
    public int getItemCount() {
        return notifications.size();
    }

    public interface ClickListener {
        void onClick(View view, int position);

        void onLongClick(View view, int position);
    }

    public static class RecyclerTouchListener implements RecyclerView.OnItemTouchListener {

        private GestureDetector gestureDetector;
        private Notification_Adapter.ClickListener clickListener;

        public RecyclerTouchListener(Context context, final RecyclerView recyclerView, final Notification_Adapter.ClickListener clickListener) {
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