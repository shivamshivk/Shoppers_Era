package com.shoppers_era.Adapters;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import com.shoppers_era.Model.Home;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.shoppers_era.R;

import java.util.List;

public class Home_Adapter extends RecyclerView.Adapter<Home_Adapter.MyViewHolder> {

    private List<Home> home;
    private Context mContext;
    private ClickListener listener;

    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView cat_text;
        TextView cat_no;
        TextView cat_desc;
        TextView tag_name;
        TextView tag_name_2;
        RecyclerView recyclerView;
        AdView imageView;
        RelativeLayout relativeLayout;

        public MyViewHolder(View view) {
            super(view);
            cat_text = (TextView) view.findViewById(R.id.cat_name);
            cat_no = (TextView) view.findViewById(R.id.cat_no);
            cat_desc = (TextView) view.findViewById(R.id.cat_desc);
            recyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);
            imageView = (AdView) view.findViewById(R.id.adView);
            relativeLayout = (RelativeLayout) view.findViewById(R.id.list_layout);
            tag_name = (TextView) view.findViewById(R.id.newly_added);
            tag_name_2 = (TextView) view.findViewById(R.id.newly_added_1);
        }
    }


    public Home_Adapter(Context mContext,List<Home> home,ClickListener listener) {
        this.mContext = mContext;
        this.home = home;
        this.listener = listener;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item_design, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {

        Home image = home.get(position);

        holder.cat_text.setText(image.getCat_name());
        holder.cat_no.setText(image.getCat_count()+" Designs");
        holder.cat_desc.setText(image.getCat_desc());
        holder.tag_name.setText(image.getTag_name());
        holder.tag_name_2.setText(image.getTag_name_2());

        try{
            holder.tag_name.setBackgroundColor(Color.parseColor(image.getTag_color()));
            holder.tag_name_2.setBackgroundColor(Color.parseColor(image.getTag_color_2()));
        }catch (IllegalStateException e){
            e.printStackTrace();
        }catch (NullPointerException e){
            e.printStackTrace();
        }catch (StringIndexOutOfBoundsException e){
            e.printStackTrace();
        }

        if (image.getTag_name().equals("Default") ||image.getTag_name().equals("")){
            holder.tag_name.setVisibility(View.GONE);
        }

        if (image.getTag_name_2().equals("Default")||image.getTag_name().equals("")){
            holder.tag_name_2.setVisibility(View.GONE);
        }

        Typeface tf = Typeface.createFromAsset(mContext.getAssets(), "fonts/arial.ttf");
        holder.cat_text.setTypeface(tf);
        holder.cat_no.setTypeface(tf);
        holder.cat_desc.setTypeface(tf);

        holder.imageView.setTag(position);

        holder.relativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onLayoutClick(v,position);
            }
        });

        if (position%3==0){
            holder.imageView.setVisibility(View.VISIBLE);
        }

        AdRequest adRequest = new AdRequest.Builder().build();
        holder.imageView.loadAd(adRequest);

        holder.recyclerView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onLayoutClick2(view,position);
            }
        });

        holder.imageView.setAdListener(new AdListener() {
            @Override
            public void onAdLoaded() {
                // Code to be executed when an ad finishes loading.
            }

            @Override
            public void onAdFailedToLoad(int errorCode) {
                // Code to be executed when an ad request fails.
            }

            @Override
            public void onAdOpened() {
                // Code to be executed when an ad opens an overlay that
                // covers the screen.
            }

            @Override
            public void onAdLeftApplication() {
                // Code to be executed when the user has left the app.
            }

            @Override
            public void onAdClosed() {
                // Code to be executed when when the user is about to return
                // to the app after tapping on an ad.
            }
        });

        Image_Adapter image_adapter = new Image_Adapter(mContext, image.getImages(), new Image_Adapter.ClickListener() {
            @Override
            public void onimgClck(View v, int posiiton) {
                listener.onLayoutClick2(v,position);
            }
        });

        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(mContext, 4);
        holder.recyclerView.setLayoutManager(mLayoutManager);
        holder.recyclerView.setItemAnimator(new DefaultItemAnimator());
        holder.recyclerView.setAdapter(image_adapter);

        holder.recyclerView.setNestedScrollingEnabled(false);
    }

    @Override
    public int getItemCount() {
        return home.size();
    }

    public interface ClickListener {
        void onClick(View view, int position);

        void onLongClick(View view, int position);

        void onLayoutClick(View view,int position);

        void onLayoutClick2(View view,int position);
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