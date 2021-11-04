package com.example.spacejuice;

import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.text.format.DateFormat;
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
import com.example.spacejuice.activity.HabitDetailsActivity;
import com.example.spacejuice.activity.OverviewActivity;

import java.util.ArrayList;
import java.util.Date;

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
        TextView eventDateText;
        ImageView eventIndicator;
        ImageView eventImage;
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

        ViewHolder viewHolder;
        if (row == null) // If the viewHolder was not previously initialized
        {
            row = LayoutInflater.from(getContext()).inflate(R.layout.habit_event_content, parent, false);
            viewHolder = new ViewHolder();
            viewHolder.eventIndicator = row.findViewById(R.id.habit_event_indicator);
            viewHolder.eventDescription = row.findViewById(R.id.habit_event_description);
            viewHolder.eventDateText = row.findViewById(R.id.habit_event_date);
            viewHolder.eventImage = row.findViewById(R.id.eventImage);
            row.setTag(viewHolder);
        } else { // If the viewHolder was already initialized
            viewHolder = (ViewHolder) row.getTag();
        }
        Date date = eventItems.get(position).getDate();

        String dayOfWk = (String) DateFormat.format("EEE", date); // Thursday
        String day          = (String) DateFormat.format("dd", date); // 20
        String month  = (String) DateFormat.format("MMM", date); // September
        String dateText = dayOfWk + " " + month + " " + day;

        if (MainActivity.checkForSmallDisplay(context)) {
            ViewGroup.LayoutParams layoutParams = viewHolder.eventDescription.getLayoutParams();
            layoutParams.width = 350; // resizes description width for smaller displays
            viewHolder.eventDescription.setLayoutParams(layoutParams);
        }

        viewHolder.eventIndicator.setImageResource(eventItems.get(position).getEventIndicator());
        viewHolder.eventIndicator.setClickable(false);
        viewHolder.eventDescription.setText(eventItems.get(position).getShortDescription());
        viewHolder.eventDescription.setClickable(false);
        viewHolder.eventDateText.setText(dateText);
        viewHolder.eventDateText.setClickable(false);
        viewHolder.eventImage.setImageResource(R.drawable.empty_image);
        View.OnClickListener goToEventDetails;

        goToEventDetails = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("debugInfo", "clicked on item (" + position + ") giving Uid: " + eventItems.get(position).getUid());
                HabitDetailsActivity inst = (HabitDetailsActivity) context;
                inst.launchEventDetails(eventItems.get(position).getUid());
            }
        };

        row.findViewById(R.id.clickable_habit_segment).setOnClickListener(goToEventDetails);
        // todo implement click events

        return row;
    }
}
