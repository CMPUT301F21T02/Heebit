package com.example.spacejuice;

import android.content.Context;
import android.media.MediaPlayer;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import androidx.activity.result.ActivityResultLauncher;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.content.res.AppCompatResources;

import com.example.spacejuice.activity.OverviewActivity;
import com.example.spacejuice.controller.HabitController;

import java.util.ArrayList;

public class HabitListAdapter extends ArrayAdapter {
    private final ArrayList<Habit> items;
    private final Context context;

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
        View ll_swapping;
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

        HabitController.calculateScore(items.get(position));

        ViewHolder viewHolder;
        if (row == null) // If the viewHolder was not previously initialized
        {
            row = LayoutInflater.from(getContext()).inflate(R.layout.habit_content, parent, false);
            viewHolder = new ViewHolder();
            viewHolder.ll_indicator = row.findViewById(R.id.LL_Habit_Indicator);
            viewHolder.textView = row.findViewById(R.id.habit_text);
            viewHolder.levelText = row.findViewById(R.id.habit_content_habit_level);
            viewHolder.ll_swapping = row.findViewById(R.id.swap_habit_marker);
            row.setTag(viewHolder);
        } else { // If the viewHolder was already initialized
            viewHolder = (ViewHolder) row.getTag();
        }

        checkBox = row.findViewById(R.id.habitCheckBox);
        viewHolder.ll_indicator.setBackground(AppCompatResources.getDrawable(context, items.get(position).getIndicator().getImage()));
        viewHolder.textView.setText(items.get(position).getTitle());
        viewHolder.levelText.setText(items.get(position).getIndicator().getIndicatorText());
        viewHolder.textView.setClickable(false);
        if (items.get(position).getSwapping()) {
            viewHolder.ll_swapping.setVisibility(View.VISIBLE);
        } else {
            viewHolder.ll_swapping.setVisibility(View.INVISIBLE);
        }
        View.OnClickListener clickedOnHabit;
        View.OnLongClickListener longClickedOnHabit;

        clickedOnHabit = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("debugInfo", "clicked on item (" + position + ") giving Uid: " + items.get(position).getUid());
                ActivityResultLauncher<String> launchEdit;

                if (context.getClass() == OverviewActivity.class) {
                    OverviewActivity inst = (OverviewActivity) context;
                    Habit longClickedHabit = inst.getLongClickedHabit();
                    if (longClickedHabit == null) {
                        inst.launchHabitDetails(items.get(position).getUid());
                    } else {
                        if (longClickedHabit == items.get(position)) {
                            // if the longclicked habit was clicked on
                            longClickedHabit.setSwapping(false);

                            inst.launchHabitDetails(items.get(position).getUid());
                        } else {
                            longClickedHabit.setSwapping(false);
                            HabitController.swapHabits(longClickedHabit, items.get(position), inst);

                        }

                    }
                }
            }
        };

        longClickedOnHabit = new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                Log.d("debugInfo", "habit was long clicked");
                if (context.getClass() == OverviewActivity.class) {
                    OverviewActivity inst = (OverviewActivity) context;
                    inst.setLongClickedHabit(items.get(position)); }
                return true;

            }
        };

        row.findViewById(R.id.clickable_habit_segment).setOnClickListener(clickedOnHabit);
        row.findViewById(R.id.clickable_habit_segment).setOnLongClickListener(longClickedOnHabit);


        if (items.get(position).isToday()) {
            checkBox.setVisibility(View.VISIBLE);
            if (HabitController.completedToday(items.get(position))) {
                checkBox.setChecked(true);
                checkBox.setClickable(false);
            } else{
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
