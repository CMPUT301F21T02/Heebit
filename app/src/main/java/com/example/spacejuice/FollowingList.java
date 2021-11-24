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

public class FollowingList extends ArrayAdapter<Member> {
    /**
     * This sets the content for following list
     */
    private final ArrayList<Member> names;
    private final Context context;

    public FollowingList(Context context, ArrayList<Member> names){
        super(context, 0, names);
        this.names = names;
        this.context = context;
    }
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view = convertView;
        if(view == null){
            view = LayoutInflater.from(context).inflate(R.layout.following_content, parent, false);
        }
        Member member = names.get(position);
        TextView name = view.findViewById(R.id.nameTextFC);
        name.setText(member.getMemberName());

        return view;
    }
}
