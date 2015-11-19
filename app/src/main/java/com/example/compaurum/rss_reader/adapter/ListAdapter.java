package com.example.compaurum.rss_reader.adapter;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.example.compaurum.rss_reader.MainActivity;
import com.example.compaurum.rss_reader.R;
import com.example.compaurum.rss_reader.parser.Item;
import com.example.compaurum.rss_reader.parser.Items;

import java.util.ArrayList;
import java.util.List;

public class ListAdapter extends BaseAdapter {
    LayoutInflater layoutInflater;
    ArrayList<Item> objects;
    MainActivity mActivity;

    public ListAdapter(MainActivity activity, ArrayList<Item> items) {
        this.mActivity = activity;
        this.objects = items;
        this.layoutInflater = LayoutInflater.from(activity);
        //this.layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return objects.size();
    }

    // элемент по позиции
    @Override
    public Item getItem(int position) {
        return objects.get(position);
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
            view = layoutInflater.inflate(R.layout.list_item, parent, false);
            holder = new ViewHolder();
            holder.label = (TextView) view.findViewById(R.id.label);
            holder.comment = (TextView) view.findViewById(R.id.comment);
            holder.checkBox = (CheckBox) view.findViewById(R.id.checkBox1);
            view.setTag(holder);
        }else {
            view = convertView;
            holder = (ViewHolder) view.getTag();
        }

        Item item = objects.get(position);
        holder.label.setText(item.getTitle());
        holder.comment.setText(item.getpubDateString());
        if (!item.isReaded()) {
            holder.label.setTextColor(Color.BLACK);
            holder.comment.setTextColor(Color.BLACK);
        }else {
            holder.label.setTextColor(Color.GRAY);
            holder.comment.setTextColor(Color.GRAY);
        }
        //holder.checkBox.setOnCheckedChangeListener(null);
        holder.checkBox.setChecked(item.isFavorite());
        //OnCheckedChangeListener(mOnCheckedChangeListener);
        holder.checkBox.setTag(item);
        holder.checkBox.setOnClickListener(mClickListener);


        return view;
    }

    private class ViewHolder{
        public TextView label;
        public TextView comment;
        public CheckBox checkBox;
    }

    Item getNewsItem(int position) {
        return ((Item) getItem(position));
    }

    CompoundButton.OnClickListener mClickListener = new CompoundButton.OnClickListener() {
        @Override
        public void onClick(View v) {
            boolean checked = ((Item)v.getTag()).isFavorite();
            ((Item) v.getTag()).setFavorite(!checked);
            mActivity.update((Item) v.getTag());
        }
    };

    /*
    CompoundButton.OnCheckedChangeListener mOnCheckedChangeListener = new CompoundButton.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            Log.d("LISTENER", ((Item) buttonView.getTag()).toString());
            Log.d("LISTENER", isChecked+"");
            Log.d("LISTENER", ((Item) buttonView.getTag()).isFavorite()+"");
            ((Item) buttonView.getTag()).setFavorite(isChecked);
            Log.d("LISTENER", ((Item) buttonView.getTag()).isFavorite()+"");
        }
    };
    */
}