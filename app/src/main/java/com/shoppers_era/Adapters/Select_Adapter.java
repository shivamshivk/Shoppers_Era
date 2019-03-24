package com.shoppers_era.Adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;
import com.shoppers_era.Model.Address;
import com.shoppers_era.R;
import java.util.List;


public class Select_Adapter extends RecyclerView.Adapter<Select_Adapter.MyViewHolder> {

    private List<Address> products;
    private Context mContext;
    private int selected_position = -1;
    private ClickListener clickListener;

    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView name;
        TextView phone_no;
        TextView adrress;
        TextView address2;
        TextView landmark;
        TextView pin;
        TextView city;
        TextView state;
        CheckBox ck;

        public MyViewHolder(View view) {
            super(view);

            name = (TextView) view.findViewById(R.id.name);
            phone_no = (TextView) view.findViewById(R.id.phone_no);
            adrress = (TextView) view.findViewById(R.id.address);
            address2 = (TextView) view.findViewById(R.id.address_2);
            landmark = (TextView) view.findViewById(R.id.landmark);
            pin = (TextView) view.findViewById(R.id.pin);
            city = (TextView)view.findViewById(R.id.city);
            state = (TextView) view.findViewById(R.id.state);
            ck = (CheckBox) view.findViewById(R.id.select);
        }

    }


    public Select_Adapter(List<Address> addresses,ClickListener click) {
        this.products = addresses;
        this.clickListener = click;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.address_select_design, parent, false);

        return new MyViewHolder(itemView);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {

        Address address = products.get(position);

        holder.name.setText(address.getFirstName());
        holder.phone_no.setText("+91 "+address.getPhoneNumber());
        holder.adrress.setText(address.getAddress());
        holder.address2.setText(address.getmAddress2());
        holder.landmark.setText(address.getLandmark());
        holder.pin.setText(address.getPin());
        holder.city.setText(address.getCity());
        holder.state.setText(address.getmState());

        holder.ck.setTag(position);

        holder.ck.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (holder.ck.isChecked()){
                    clickListener.onSelect(buttonView,position);
                }
            }
        });

        if (position == selected_position) {
            holder.ck.setChecked(true);
        } else holder.ck.setChecked(false);

        holder.ck.setOnClickListener(onStateChangedListener(holder.ck, position));


    }

    private View.OnClickListener onStateChangedListener(final CheckBox checkBox, final int position) {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkBox.isChecked()) {
                    selected_position = position;
                } else {
                    selected_position = -1;
                }
                notifyDataSetChanged();
            }
        };
    }


    @Override
    public int getItemCount() {
        return products.size();
    }


    public interface ClickListener {
        void onClick(View view, int position);

        void onLongClick(View view, int position);

        void onSelect(View view, int position);

    }

    public static class RecyclerTouchListener implements RecyclerView.OnItemTouchListener {

        private GestureDetector gestureDetector;
        private ClickListener clickListener;

        public RecyclerTouchListener(Context context, final RecyclerView recyclerView, final ClickListener clickListener) {
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