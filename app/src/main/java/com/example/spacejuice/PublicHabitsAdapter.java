package com.example.spacejuice;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;

public class PublicHabitsAdapter extends ArrayAdapter<Habit> {
    private final ArrayList<Habit> habits;
    private final Context context;

    public PublicHabitsAdapter(Context context, ArrayList<Habit> habits){
        super(context, 0, habits);
        this.habits = habits;
        this.context = context;
    }
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view = convertView;
        if(view == null){
            view = LayoutInflater.from(context).inflate(R.layout.public_habit_content, parent, false);
        }
        String string = habits.get(position).getTitle();
        TextView name = view.findViewById(R.id.habit_textPHC);
        TextView level = view.findViewById(R.id.habit_content_habit_levelPHC);
        name.setText(string);
        level.setText(habits.get(position).getIndicator().getIndicatorText());
        return view;
    }
}
