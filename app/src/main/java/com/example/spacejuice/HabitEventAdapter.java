package com.example.spacejuice;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

import com.example.spacejuice.activity.HabitDetailsActivity;
import com.example.spacejuice.activity.MapsActivity;
import com.example.spacejuice.controller.HabitEventController;
import com.example.spacejuice.controller.TimeController;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Date;

public class HabitEventAdapter extends ArrayAdapter {
    private final ArrayList<HabitEvent> eventItems;
    private final Context context;
    private final Habit habit;

    public HabitEventAdapter(Context context, int layout, ArrayList<HabitEvent> items, Habit habit) {
        super(context, layout);
        this.context = context;
        this.eventItems = items;
        this.habit = habit;

    }

    //ViewHolder contains elements for our list item layout.. and is an inner class

    public class ViewHolder {
        TextView eventDescription;
        TextView eventDateText;
        ImageView eventIndicator;
        ImageView eventImage;
        TextView eventLocation;
        ImageView gpsIcon;
        ImageView deleteIcon;
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
            viewHolder.eventLocation = row.findViewById(R.id.location_text);
            viewHolder.gpsIcon = row.findViewById(R.id.location_icon);
            viewHolder.deleteIcon = row.findViewById(R.id.event_delete);
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
        viewHolder.gpsIcon.setClickable(true);
        viewHolder.gpsIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, MapsActivity.class);
                int eventUid = eventItems.get(position).getUid();
                intent.putExtra("eventUid", eventUid);
                ((Activity) context).startActivityForResult(intent, 1);;
            }
        });
        if (eventItems.get(position).getLocation(context).equals("Null")){
            viewHolder.eventLocation.setVisibility(View.INVISIBLE);
            viewHolder.gpsIcon.setVisibility(View.VISIBLE);
        }
        else{
            viewHolder.eventLocation.setText(eventItems.get(position).getLocation(context));
            viewHolder.eventLocation.setVisibility(View.VISIBLE);
            viewHolder.gpsIcon.setVisibility(View.VISIBLE);
        }
        if (!eventItems.get(position).isDone()) {
            viewHolder.eventImage.setVisibility(View.INVISIBLE);
            viewHolder.eventImage.setClickable(false);
            viewHolder.eventDateText.setTextColor(ContextCompat.getColor(context, R.color.DarkGray));
            viewHolder.gpsIcon.setVisibility(View.INVISIBLE);
            viewHolder.gpsIcon.setClickable(false);
            viewHolder.deleteIcon.setVisibility(View.INVISIBLE);
            viewHolder.deleteIcon.setClickable(false);
            viewHolder.eventDescription.setVisibility(View.INVISIBLE);
            viewHolder.eventDescription.setClickable(false);
        } else {
            viewHolder.deleteIcon.setVisibility(View.VISIBLE);
            viewHolder.deleteIcon.setClickable(true);
            viewHolder.eventImage.setVisibility(View.VISIBLE);
            viewHolder.eventImage.setClickable(true);
            viewHolder.eventDateText.setTextColor(ContextCompat.getColor(context, R.color.Green));
            viewHolder.eventDescription.setVisibility(View.VISIBLE);
            viewHolder.eventDescription.setClickable(true);
        }

        String stringUri = eventItems.get(position).getImage();
        if(stringUri!=null) {
            Log.d("debugInfo", "clicked on item (" + position + ") giving Uid: " + eventItems.get(position).getUid());
            Log.d("debugInfo", stringUri);
            Uri uri = Uri.parse(stringUri);
            //viewHolder.eventImage.setImageResource(R.drawable.empty_image);
            Picasso.get().load(uri).into(viewHolder.eventImage);

        }else {
            Picasso.get().load(R.drawable.empty_image).into(viewHolder.eventImage);
        }
//        View.OnClickListener goToEventDetails;

        viewHolder.eventImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("debugInfo", "clicked on item (" + position + ") giving Uid: " + eventItems.get(position).getUid());
                HabitDetailsActivity inst = (HabitDetailsActivity) context;
                inst.launchEventDetails(eventItems.get(position),stringUri);
            }
        });
        viewHolder.eventDescription.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("debugInfo", "clicked on item (" + position + ") giving Uid: " + eventItems.get(position).getUid());
                HabitDetailsActivity inst = (HabitDetailsActivity) context;
                inst.launchEventDetails(eventItems.get(position),stringUri);
            }
        });

        viewHolder.deleteIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                HabitEvent event = eventItems.get(position);
                if (TimeController.compareDates(event.getDate(), TimeController.getCurrentTime().getTime()) == 0){
                    habit.deleteEvent(event);
                    HabitEventController.DeleteHabitEvent(habit, event, false);
                }
                else{
                    event.setDescription("");
                    event.setLocation(0.00, 0.00);
                    event.setImage(null);
                    event.setDone(false);
                    HabitEventController.DeleteHabitEvent(habit, event, true);
                }
                ((HabitDetailsActivity) context).refreshData();

            }
        });

//        row.findViewById(R.id.clickable_habit_segment).setOnClickListener(goToEventDetails);
        // todo implement click events
        return row;
    }
}
