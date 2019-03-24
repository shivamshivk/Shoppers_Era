package com.shoppers_era.Adapters;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import com.shoppers_era.R;
import com.squareup.picasso.Picasso;
import java.util.List;
import com.shoppers_era.Model.Slider;


public class MyAdapter extends PagerAdapter {

    private List<Slider> images;
    private LayoutInflater inflater;
    private Context context;
    private ClickListener clickListener;

    public MyAdapter(Context context, List<Slider> images,ClickListener clickListener) {
        this.context = context;
        this.images=images;
        this.clickListener = clickListener;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

    @Override
    public int getCount() {
        return images.size();
    }

    @Override
    public Object instantiateItem(ViewGroup view, final int position) {
        View myImageLayout = inflater.inflate(R.layout.slider_image_ui, view, false);
        ImageView myImage = (ImageView) myImageLayout
                .findViewById(R.id.image);

        Picasso.with(context).load(images.get(position).getImg_url()).placeholder(R.drawable.phimg).into(myImage);
        myImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickListener.onClick(v,position);
            }
        });

        view.addView(myImageLayout, 0);
        return myImageLayout;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view.equals(object);
    }

    public interface ClickListener {
        void onClick(View view, int position);
    }

}
