package com.example.spacejuice;

import android.app.Activity;
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
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.DrawableRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.content.res.AppCompatResources;

import com.example.spacejuice.activity.AddHabitEventActivity;
import com.example.spacejuice.activity.AllHabitsActivity;
import com.example.spacejuice.activity.EditHabitActivity;
import com.example.spacejuice.activity.OverviewActivity;
import com.example.spacejuice.controller.HabitEventController;

import java.util.ArrayList;

public class HabitListAdapter extends ArrayAdapter {
    ArrayList<Habit> items;
    Context context;

    public HabitListAdapter(Context context, int layout, ArrayList<Habit> items) {
        super(context, layout);
        this.context = context;
        this.items = items;
    }

    //ViewHolder contains elements for our list item layout.. and is an inner class

    public class ViewHolder {
        TextView textView;
        TextView levelText;
        View ll_indicator;
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
        if (row == null) // If the viewHolder was not previously initialized
        {
            row = LayoutInflater.from(getContext()).inflate(R.layout.habit_content, parent, false);
            viewHolder = new ViewHolder();
            viewHolder.ll_indicator = row.findViewById(R.id.LL_Habit_Indicator);
            viewHolder.textView = row.findViewById(R.id.habit_text);
            viewHolder.levelText = row.findViewById(R.id.habit_content_habit_level);
            row.setTag(viewHolder);
        } else { // If the viewHolder was already initialized
            viewHolder = (ViewHolder) row.getTag();
        }

        checkBox = row.findViewById(R.id.habitCheckBox);
        viewHolder.ll_indicator.setBackground(AppCompatResources.getDrawable(context, items.get(position).getIndicator().getImage()));
        viewHolder.textView.setText(items.get(position).getTitle());
        viewHolder.levelText.setText(items.get(position).getIndicator().getIndicatorText());
        viewHolder.textView.setClickable(false);
        View.OnClickListener goToHabitDetails;

        goToHabitDetails = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("debugInfo", "clicked on item (" + position + ") giving Uid: " + items.get(position).getUid());
                ActivityResultLauncher<String> launchEdit;

                items.get(position).completedToday(); //test
                // todo loadHabitEventFromFirebase
                //HabitEventController.loadHabitEventsFromFirebase(items.get(position));

                if (context.getClass() == OverviewActivity.class) {
                    OverviewActivity inst = (OverviewActivity) context;
                    Toast.makeText(inst,"loadSuccess",Toast.LENGTH_SHORT).show();
                    inst.launchHabitDetails(items.get(position).getUid());
                } else if (context.getClass() == AllHabitsActivity.class) {
                    AllHabitsActivity inst = (AllHabitsActivity) context;
                    Toast.makeText(inst,"loadSuccess",Toast.LENGTH_SHORT).show();
                    inst.launchHabitDetails(items.get(position).getUid());
                }
            }
        };

        row.findViewById(R.id.clickable_habit_segment).setOnClickListener(goToHabitDetails);


        if (items.get(position).isToday()) {
            checkBox.setVisibility(View.VISIBLE);
            if (items.get(position).completedToday()) {
                checkBox.setChecked(true);
                checkBox.setClickable(false);
            } else {
                checkBox.setChecked(false);
                checkBox.setClickable(true);


                checkBox.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (((CompoundButton) view).isChecked()) {
                            checkBox.setClickable(false);
                            ActivityResultLauncher<String> launch;
                            MediaPlayer song = MediaPlayer.create(context, R.raw.click);
                            song.start();
                            ActivityResultLauncher<String> launchEdit;
                            OverviewActivity inst = (OverviewActivity) context;
                            inst.launchAddNewEvent(items.get(position).getUid());
                        } else {
                            Log.d("debugInfo", "item attempted to be unchecked");
                            checkBox.setChecked(true);
                        }
                    }
                });
            }
        } else {
            // remove the checkbox if the habit is not scheduled for today
            checkBox.setVisibility(View.INVISIBLE);
            checkBox.setClickable(false);

        }
        return row;
    }
}
