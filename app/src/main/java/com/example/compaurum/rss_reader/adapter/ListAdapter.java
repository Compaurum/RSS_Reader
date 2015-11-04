package com.example.compaurum.rss_reader.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.example.compaurum.rss_reader.R;
import com.example.compaurum.rss_reader.parser.Item;

import java.util.List;

/**
 * Created by compaurum on 03.11.2015.
 */
/*
public class ListAdapter extends ArrayAdapter<String> {

    private List<String> Strings;

    //resource - это id вашего list item, a Strings - это строки, которые будут отображаться в TextView
    public ListAdapter(Context context, int resource, List<String> Strings) {

        super(context, resource, Strings);

        //this.Strings = Strings;

    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View v = convertView;

        if (v == null) {
            LayoutInflater vi;
            vi = LayoutInflater.from(getContext());
            v = vi.inflate(R.layout.list_item, null);
        }

        String p = Strings.get(position);

        if (p != null) {
            TextView tt = (TextView) v.findViewById(R.id.label);
            Button button = (Button) v.findViewById(R.id.toSaveIcon);
            button.setTag(tt);
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ((TextView) v.getTag()).setText("ist click");
                }
            });

            if (tt != null) {
                tt.setText(p);
            }

        }

        return v;

    }
}
*/