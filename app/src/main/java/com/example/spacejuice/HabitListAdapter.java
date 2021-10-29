package com.example.spacejuice;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.spacejuice.Habit;
import com.example.spacejuice.R;
import com.example.spacejuice.activity.AddHabitEventActivity;
import com.example.spacejuice.activity.AllHabitsActivity;
import com.example.spacejuice.activity.MyProfileActivity;

import java.util.ArrayList;

public class HabitListAdapter extends ArrayAdapter {
   ArrayList<Habit> items;
   Context context;

   public HabitListAdapter(Context context, int layout, ArrayList<Habit> items)
   {
      super(context, layout);
      this.context = context;
      this.items = items;
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
      CheckBox checkBox;

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

      checkBox = row.findViewById(R.id.habitCheckBox);

      viewHolder.imageView.setImageResource(items.get(position).getIndicator().getImage());
      viewHolder.textView.setText(items.get(position).getTitle());

      if (items.get(position).isToday()) {
         checkBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               if (((CompoundButton) view).isChecked()) {
                  Log.d("debugInfo", "item is checked");
                  Intent intent = new Intent(context, AddHabitEventActivity.class);
                  context.startActivity(intent);

               } else {
                  Log.d("debugInfo", "item attempted to be unchecked");
                  checkBox.setChecked(true);
               }
            }
         });
      } else {
         // remove the checkbox if the habit is not scheduled for today
         checkBox.setButtonDrawable(null);
         checkBox.setClickable(false);
      }

      return row;
   }
}
