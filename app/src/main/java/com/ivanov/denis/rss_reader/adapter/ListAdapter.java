package com.ivanov.denis.rss_reader.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.ivanov.denis.rss_reader.MainActivity;
import com.ivanov.denis.rss_reader.R;
import com.ivanov.denis.rss_reader.parser.Item;

import java.util.ArrayList;

public class ListAdapter extends BaseAdapter {
    private LayoutInflater mLayoutInflater;
    private ArrayList<Item> mObjects;
    private Context mContext;

    public ListAdapter(Context context, ArrayList<Item> items) {
        this.mContext = context;
        this.mObjects = items;
        this.mLayoutInflater = LayoutInflater.from(context);
        //this.layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return mObjects.size();
    }

    // элемент по позиции
    @Override
    public Item getItem(int position) {
        return mObjects.get(position);
    }

    // id по позиции
    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view;
        ViewHolder holder;
        if (convertView == null) {
            view = mLayoutInflater.inflate(R.layout.list_item, parent, false);
            holder = new ViewHolder();
            holder.label = (TextView) view.findViewById(R.id.label);
            holder.comment = (TextView) view.findViewById(R.id.comment);
            holder.checkBox = (CheckBox) view.findViewById(R.id.checkBox1);
            view.setTag(holder);
        } else {
            view = convertView;
            holder = (ViewHolder) view.getTag();
        }

        Item item = mObjects.get(position);
        holder.label.setText(item.getTitle());
        holder.comment.setText(item.getpubDateString());
        if (!item.isReaded()) {
            holder.label.setTextColor(Color.BLACK);
            holder.comment.setTextColor(Color.BLACK);
        } else {
            holder.label.setTextColor(Color.GRAY);
            holder.comment.setTextColor(Color.GRAY);
        }
        holder.checkBox.setChecked(item.isFavorite());
        holder.checkBox.setTag(item);
        holder.checkBox.setOnClickListener(mClickListener);


        return view;
    }

    private class ViewHolder {
        public TextView label;
        public TextView comment;
        public CheckBox checkBox;
    }

    CompoundButton.OnClickListener mClickListener = new CompoundButton.OnClickListener() {
        @Override
        public void onClick(View v) {
            boolean checked = ((Item) v.getTag()).isFavorite();
            ((Item) v.getTag()).setFavorite(!checked);
            ((MainActivity)mContext).update((Item) v.getTag());
        }
    };
}