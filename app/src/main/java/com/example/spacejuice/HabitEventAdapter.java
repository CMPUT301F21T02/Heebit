package com.example.spacejuice;

import static androidx.core.content.ContextCompat.startActivity;

import android.content.Context;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.media.MediaPlayer;
import android.net.Uri;
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
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

import com.example.spacejuice.activity.AddHabitEventActivity;
import com.example.spacejuice.activity.AllHabitsActivity;
import com.example.spacejuice.activity.HabitDetailsActivity;
import com.example.spacejuice.activity.OverviewActivity;
import com.example.spacejuice.activity.UploadImageActivity;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Date;

public class HabitEventAdapter extends ArrayAdapter {
    private final ArrayList<HabitEvent> eventItems;
    private final Context context;

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
        TextView eventLocation;
        ImageView gpsIcon;
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
        if (eventItems.get(position).getLocation(context).equals("Null")){
            viewHolder.eventLocation.setVisibility(View.INVISIBLE);
            viewHolder.gpsIcon.setVisibility(View.INVISIBLE);
        }
        else{
            viewHolder.eventLocation.setText(eventItems.get(position).getLocation(context));
        }
        if (!eventItems.get(position).isDone()) {
            viewHolder.eventImage.setVisibility(View.INVISIBLE);
            viewHolder.eventImage.setClickable(false);
            viewHolder.eventDateText.setTextColor(ContextCompat.getColor(context, R.color.DarkGray));
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
        View.OnClickListener goToEventDetails;

        goToEventDetails = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("debugInfo", "clicked on item (" + position + ") giving Uid: " + eventItems.get(position).getUid());
                HabitDetailsActivity inst = (HabitDetailsActivity) context;
                inst.launchEventDetails(eventItems.get(position).getUid(), eventItems.get(position));
            }
        };

        viewHolder.eventImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HabitDetailsActivity inst = (HabitDetailsActivity) context;
                inst.launchUpload(eventItems.get(position).getUid(), eventItems.get(position));
            }
        });

        row.findViewById(R.id.clickable_habit_segment).setOnClickListener(goToEventDetails);
        // todo implement click events
        return row;
    }
}
