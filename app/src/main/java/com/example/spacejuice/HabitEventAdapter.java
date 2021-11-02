package com.example.spacejuice;

import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.result.ActivityResultLauncher;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.spacejuice.activity.AddHabitEventActivity;
import com.example.spacejuice.activity.AllHabitsActivity;
import com.example.spacejuice.activity.OverviewActivity;

import java.util.ArrayList;

public class HabitEventAdapter extends ArrayAdapter {
    ArrayList<HabitEvent> eventItems;
    Context context;

    public HabitEventAdapter(Context context, int layout, ArrayList<HabitEvent> items) {
        super(context, layout);
        this.context = context;
        this.eventItems = items;
    }

    //ViewHolder contains elements for our list item layout.. and is an inner class

    public class ViewHolder {
        TextView eventDescription;
        ImageView eventIndicator;
    }

    @Override
    public int getCount() {
        return eventItems.size();
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {


        View row;
        row = convertView;
        CheckBox checkBox;

        ViewHolder viewHolder;
        if (row == null) // If the viewHolder was not previously initialized
        {
            row = LayoutInflater.from(getContext()).inflate(R.layout.habit_event_content, parent, false);
            viewHolder = new ViewHolder();
            viewHolder.eventIndicator = row.findViewById(R.id.habit_event_indicator);
            viewHolder.eventDescription = row.findViewById(R.id.habit_event_description);
            row.setTag(viewHolder);
        } else { // If the viewHolder was already initialized
            viewHolder = (ViewHolder) row.getTag();
        }


        viewHolder.eventIndicator.setImageResource(eventItems.get(position).getEventIndicator());
        viewHolder.eventIndicator.setClickable(false);
        viewHolder.eventDescription.setText(eventItems.get(position).getDescription());
        viewHolder.eventDescription.setClickable(false);
        View.OnClickListener goToEventDetails;
/*
        goToEventDetails = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("debugInfo", "clicked on item (" + position + ") giving Uid: " + eventItems.get(position).getUid());
                ActivityResultLauncher<String> launchEdit;
                if (context.getClass() == OverviewActivity.class) {
                    OverviewActivity inst = (OverviewActivity) context;
                    inst.launchEventDetails(eventItems.get(position).getUid());
                } else if (context.getClass() == AllHabitsActivity.class) {
                    AllHabitsActivity inst = (AllHabitsActivity) context;
                    inst.launchEventDetails(eventItems.get(position).getUid());
                }
            }
        };
/*
        row.findViewById(R.id.clickable_habit_segment).setOnClickListener(goToEventDetails);

        /* todo: implement a Habit Event Details page */

/*
        checkBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (((CompoundButton) view).isChecked()) {
                    checkBox.setClickable(false);
                    MediaPlayer song = MediaPlayer.create(context, R.raw.click);
                    song.start();
                    Intent intent = new Intent(context, EventDetailsActivity.class);
                    intent.putExtra("habitUid", eventItems.get(position).getUid());
                    context.startActivity(intent);

                } else {
                    Log.d("debugInfo", "item attempted to be unchecked");
                    checkBox.setChecked(true);
                }
            }
        });
*/
        return row;
    }
}
