package com.example.clip.education;

import java.util.ArrayList;

import com.example.clip.R;
import com.example.clip.R.id;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import android.app.Activity;



public class EducationGraduateAdapter extends ArrayAdapter<EducationGraduateData> {
    private final Context context;
    private final ArrayList<EducationGraduateData> data;
    private final int layoutResourceId;

    public EducationGraduateAdapter(Context context, int layoutResourceId, ArrayList<EducationGraduateData> data) {
        super(context, layoutResourceId, data);
        this.context = context;
        this.data = data;
        this.layoutResourceId = layoutResourceId;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;
        ViewHolder holder = null;

        if(row == null)
        {
            LayoutInflater inflater = ((Activity)context).getLayoutInflater();
            row = inflater.inflate(layoutResourceId, parent, false);

            holder = new ViewHolder();
            holder.textView1 = (TextView)row.findViewById(R.id.educationGraduate_listName);
            holder.textView2 = (TextView)row.findViewById(R.id.educationGraduate_listType);

            row.setTag(holder);
        }
        else
        {
            holder = (ViewHolder)row.getTag();
        }

        EducationGraduateData eduData = data.get(position);

        holder.textView1.setText(eduData.getName());
        holder.textView2.setText(eduData.getType());

        return row;
    }

    static class ViewHolder
    {
        TextView textView1;
        TextView textView2;
    }
}