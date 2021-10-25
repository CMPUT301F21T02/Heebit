package com.example.spacejuice.activity;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.spacejuice.HabitListItem;
import com.example.spacejuice.R;

import java.util.ArrayList;

public class HabitListAdapter extends ArrayAdapter {
   //1. Constructor   2. Viewholder inner class  3. override 2 methods getView(..)  getCount(..)
   ArrayList<HabitListItem> items;

   public HabitListAdapter(Context context, int layout, ArrayList<HabitListItem> items)
   {
      super(context, layout);
      this.items=items;
   }

   //ViewHolder contains elements for our list item layout.. and is an inner class

   public class ViewHolder{
      TextView textView;
      ImageView imageView;
   }

   @Override
   public int getCount() {
      return items.size();
   }

   @NonNull
   @Override
   public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

      View row;
      row = convertView;
      ViewHolder viewHolder;
      if (row==null) // If the viewHolder was not previously initialized
      {
         row= LayoutInflater.from(getContext()).inflate(R.layout.habit_content, parent, false);
         viewHolder = new ViewHolder();
         viewHolder.imageView=row.findViewById(R.id.habit_indicator);
         viewHolder.textView=row.findViewById(R.id.habit_text);
         row.setTag(viewHolder);
      }
      else { // If the viewHolder was already initialized
         viewHolder = (ViewHolder) row.getTag();
      }

      viewHolder.imageView.setImageResource(items.get(position).indicator);
      viewHolder.textView.setText(items.get(position).text);

      return row;
   }
}
