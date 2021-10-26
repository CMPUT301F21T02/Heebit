package com.example.spacejuice.activity;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.spacejuice.Habit;
import com.example.spacejuice.Member;
import com.example.spacejuice.R;

import java.util.ArrayList;

public class FollowRequestAdapter extends ArrayAdapter {
   ArrayList<Member> requestingMembers;
   Context context;

   public FollowRequestAdapter(Context context, int layout, ArrayList<Member> requestingMembers)
   {
      super(context, layout);
      this.context = context;
      this.requestingMembers = requestingMembers;
   }

   //ViewHolder contains elements for our list item layout.. and is an inner class

   public class ViewHolder{
      TextView textView;
      ImageButton acceptButton;
      ImageButton denyButton;
   }

   @Override
   public int getCount() {
      return requestingMembers.size();
   }

   @NonNull
   @Override
   public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

      View row;
      row = convertView;
      ViewHolder viewHolder;
      if (row==null) // If the viewHolder was not previously initialized
      {
         row= LayoutInflater.from(getContext()).inflate(R.layout.follow_request_content, parent, false);
         viewHolder = new ViewHolder();
         viewHolder.textView=row.findViewById(R.id.follower_name_textview);
         viewHolder.acceptButton=row.findViewById(R.id.button_follower_accept);
         viewHolder.denyButton=row.findViewById(R.id.button_follower_deny);
         row.setTag(viewHolder);
         Log.d("debugInfo", "position: " + position);

         viewHolder.acceptButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               FollowerRequestsActivity FReqAct;
               FReqAct = (FollowerRequestsActivity) context;
               FReqAct.acceptFollowRequest(position);
            }
         });

         viewHolder.denyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               FollowerRequestsActivity FReqAct;
               FReqAct = (FollowerRequestsActivity) context;
               FReqAct.denyFollowRequest(position);
            }
         });
      }
      else { // If the viewHolder was already initialized
         viewHolder = (ViewHolder) row.getTag();
      }

      viewHolder.textView.setText(requestingMembers.get(position).getMemberName());

      return row;
   }
}
