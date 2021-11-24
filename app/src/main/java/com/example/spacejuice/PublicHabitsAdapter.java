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

public class PublicHabitsAdapter extends ArrayAdapter<String> {
    private final ArrayList<String> title;
    private final Context context;

    public PublicHabitsAdapter(Context context, ArrayList<String> titles){
        super(context, 0, titles);
        this.title = titles;
        this.context = context;
    }
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view = convertView;
        if(view == null){
            view = LayoutInflater.from(context).inflate(R.layout.public_habit_content, parent, false);
        }
        String string = title.get(position);
        TextView name = view.findViewById(R.id.habit_textPHC);
        name.setText(string);

        return view;
    }
}
