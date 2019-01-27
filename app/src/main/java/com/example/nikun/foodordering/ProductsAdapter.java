package com.example.nikun.foodordering;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class ProductsAdapter extends BaseAdapter implements View.OnClickListener {
    private LayoutInflater mInflater;
    //    private int resourceID;
    private Callback mCallback;
    private ArrayList<Products> mproducts;


    public interface Callback{
        void click(View v);
    }

    public ProductsAdapter(@NonNull Context context, @NonNull List<Products> products, MenuActivity callback) {
        mCallback =  callback;
        mproducts = (ArrayList<Products>) products;
        mInflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return mproducts.size();
    }

    @Override
    public Object getItem(int position) {
        return mproducts.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @NonNull
    @Override
    public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        final Products products = (Products) getItem(position);
        ViewHolder viewHolder = null;
        if (convertView == null) {
            viewHolder = new ViewHolder();
            convertView = mInflater.inflate(R.layout.menu_itemsarray,null);

//        View view = LayoutInflater.from(getContext()).inflate(resourceID, parent, false);
//        ImageView productsImage = (ImageView) convertView.findViewById(R.id.head);

            viewHolder.name = convertView.findViewById(R.id.name);
            viewHolder.price = convertView.findViewById(R.id.price);
            viewHolder.increase = convertView.findViewById(R.id.increase);
            viewHolder.reduce = convertView.findViewById(R.id.reduce);
            viewHolder.num = convertView.findViewById(R.id.shoppingNum);
            viewHolder.image = convertView.findViewById(R.id.head);

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.name.setText(String.valueOf(products.getGoods()));
        viewHolder.num.setText(String.valueOf(products.getNumber()));
        viewHolder.price.setText(String.valueOf(products.getPrice()));
        viewHolder.image.setImageDrawable(products.getPicture());


        final ViewHolder finalViewHolder = viewHolder;
        viewHolder.increase.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int num = products.getNumber();
                num++;
                products.setNumber(num);
                finalViewHolder.num.setText(String.valueOf(products.getNumber()));
                mCallback.click(v);
            }


        });
        final ViewHolder finalViewHolder1 = viewHolder;
        viewHolder.reduce.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int num = products.getNumber();
                if (num > 0) {
                    num--;
                    products.setNumber(num);
                    finalViewHolder1.num.setText(String.valueOf(products.getNumber()));
                }
                mCallback.click(v);
            }
        });



        return convertView;
    }

    @Override
    public void onClick(View v) {

    }

    public class ViewHolder {
        public ImageView image;
        /**
         * Name of item
         */
        public TextView name;
        /**
         * Price of item
         */
        public TextView price;
        /**
         * Number of item
         */
        public TextView num;

        public TextView increase;

        public TextView reduce;

    }
}

