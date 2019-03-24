package com.shoppers_era.Adapters;


import android.content.Context;
import com.shoppers_era.Layout.SquareLayout;
import com.shoppers_era.Model.Selection;
import android.support.v7.widget.RecyclerView;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import com.bumptech.glide.Glide;
import com.shoppers_era.R;
import java.util.List;

public class Image_Adapter extends RecyclerView.Adapter<Image_Adapter.MyViewHolder> {

    private List<Selection> images;
    private Context mContext;
    private ClickListener clickListener;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public ImageView cat_image;
        public SquareLayout squareLayout;

        public MyViewHolder(View view) {
            super(view);
            cat_image = (ImageView) view.findViewById(R.id.thumbnail);
            squareLayout = (SquareLayout) view.findViewById(R.id.container);
        }
    }


    public Image_Adapter(Context mContext,List<Selection> images,ClickListener clickListener) {
        this.mContext = mContext;
        this.images = images;
        this.clickListener = clickListener;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.grid_view_design, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        String image = images.get(position).getImg_url();
        Glide.with(mContext).load(image).placeholder(R.drawable.phimg).into(holder.cat_image);

        holder.squareLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clickListener.onimgClck(view,position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return images.size();
    }

    public interface ClickListener {
        void onimgClck(View v,int posiiton);
    }

    public static class RecyclerTouchListener implements RecyclerView.OnItemTouchListener {

        private GestureDetector gestureDetector;
        private Home_Adapter.ClickListener clickListener;

        public RecyclerTouchListener(Context context, final RecyclerView recyclerView, final Home_Adapter.ClickListener clickListener) {
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