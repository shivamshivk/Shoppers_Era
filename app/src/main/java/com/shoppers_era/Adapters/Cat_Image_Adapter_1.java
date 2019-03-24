package com.shoppers_era.Adapters;

import android.content.Context;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.os.Build;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import com.shoppers_era.Model.Home_1;
import com.shoppers_era.Model.Selection;
import com.shoppers_era.R;
import com.squareup.picasso.Picasso;
import java.util.List;


public class Cat_Image_Adapter_1 extends  RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<Selection> images;
    private Context mContext;
    private MyViewHolder myViewHolder;
    private ClickListener listener;
    private final int VIEW_TYPE_ITEM = 0;
    private final int VIEW_TYPE_LOADING = 1;
    private OnLoadMoreListener onLoadMoreListener;
    private boolean isLoading;
    private int visibleThreshold = 5;
    private int pastVisibleItems, visibleItemCount, totalItemCount;
    private int lastPositions[];
    private int lastVisibleItem;



    private class LoadingViewHolder extends RecyclerView.ViewHolder {
        public ProgressBar progressBar;

        public LoadingViewHolder(View view) {
            super(view);
            progressBar = (ProgressBar) view.findViewById(R.id.progressBar1);
        }
    }


    public class MyViewHolder extends RecyclerView.ViewHolder {

        private ImageView fav;
        public TextView price;
        private TextView price_original;
        private TextView cat_name_text;
        public ImageView cat_image;
        private CheckBox imageView;
        private TextView count_all;


        public MyViewHolder(final View view) {
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


    public Cat_Image_Adapter_1(RecyclerView recyclerView, final Context mContext, List<Selection> home, ClickListener listener) {
        this.mContext = mContext;
        this.images = home;
        this.listener = listener;


        final StaggeredGridLayoutManager mLayoutManager = (StaggeredGridLayoutManager) recyclerView.getLayoutManager();

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                if (dy > 0) {

                    totalItemCount = mLayoutManager.getItemCount();
                    if (lastPositions == null)
                        lastPositions = new int[mLayoutManager.getSpanCount()];
                    lastPositions = mLayoutManager.findLastCompletelyVisibleItemPositions(lastPositions);
                    lastVisibleItem = Math.max(lastPositions[0], lastPositions[1]);//findMax(lastPositions);
                    Toast.makeText(mContext, lastVisibleItem, Toast.LENGTH_SHORT).show();

                    if (!isLoading &&totalItemCount <= (lastVisibleItem + visibleThreshold)) {
                        // End has been reached
                        if (onLoadMoreListener != null) {
                            Toast.makeText(mContext,"Loading..", Toast.LENGTH_SHORT).show();
                            onLoadMoreListener.onLoadMore();
                        }
                        Toast.makeText(mContext,"Loading1..", Toast.LENGTH_SHORT).show();

                        isLoading = true;
                    }
                }
            }
        });

    }

    @Override
    public  RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        if (viewType == VIEW_TYPE_ITEM) {
            View view = LayoutInflater.from(mContext).inflate(R.layout.gallery_thumbnail, parent, false);
            return new MyViewHolder(view);
        } else if (viewType == VIEW_TYPE_LOADING) {
            View view = LayoutInflater.from(mContext).inflate(R.layout.item_loading, parent, false);
            return new LoadingViewHolder(view);
        }

        return null;
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {

        if (holder instanceof MyViewHolder) {
            final MyViewHolder holder1 = (MyViewHolder) holder;
            Selection selection = images.get(position);

            final int pos = position;

            holder1.cat_image.setTag(position);

            holder1.imageView.setTag(images.get(position));
            holder1.imageView.setChecked(images.get(position).getSelect());

            holder1.count_all.setText(selection.getCount_all());

            holder1.imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    CheckBox cb = (CheckBox) v;
                    Selection contact = (Selection) cb.getTag();

                    contact.setSelect(cb.isChecked());
                    images.get(pos).setSelect(cb.isChecked());
                    if (cb.isChecked()){
                        holder1.cat_image.setBackgroundResource(R.drawable.black_border);
                    }else {
                        holder1.cat_image.setBackgroundResource(R.color.white);
                    }
                }
            });

            if (selection.getLiked()){
                holder1.fav.setImageResource(R.drawable.heart_re);
            }else {
                holder1.fav.setImageResource(R.drawable.heart_outline);
            }

            Picasso.with(mContext).load(selection.getImg_url()).placeholder(R.drawable.phimg).noFade().into(holder1.cat_image);

            holder1.price.setText("₹"+selection.getPrice());

            holder1.price_original.setText("₹"+selection.getActual_price());

            if(selection.getActual_price().equals("0.00")){
                holder1.price_original.setVisibility(View.GONE);
            }

            holder1.cat_name_text.setText(selection.getImage_name());

            holder1.price_original.setPaintFlags(holder1.price_original.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);

        }else {
            LoadingViewHolder loadingViewHolder = (LoadingViewHolder) holder;
            loadingViewHolder.progressBar.setIndeterminate(true);
        }

    }

    @Override
    public int getItemCount() {
        return images == null ? 0 : images.size();
    }

    public interface OnLoadMoreListener {
        void onLoadMore();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }


    public void setLoaded() {
        isLoading = false;
    }

    @Override
    public int getItemViewType(int position) {
        return images.get(position) == null ? VIEW_TYPE_LOADING : VIEW_TYPE_ITEM;
    }

    public void setOnLoadMoreListener(OnLoadMoreListener mOnLoadMoreListener) {
        this.onLoadMoreListener = mOnLoadMoreListener;
    }


    public interface ClickListener {
        void onFavClick(View v,int pos,View v2);
        void onClick(View view, int position);
        void onImgClick(View view,int position);
        void onLongClick(View view, int position);
    }

    public static class RecyclerTouchListener implements RecyclerView.OnItemTouchListener {

        private GestureDetector gestureDetector;
        private ClickListener clickListener;

        public RecyclerTouchListener(Context context, final RecyclerView recyclerView, final ClickListener clickListener) {
            this.clickListener = clickListener;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.CUPCAKE) {
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


}