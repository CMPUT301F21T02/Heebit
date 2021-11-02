package com.example.spacejuice.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.spacejuice.Habit;
import com.example.spacejuice.Member;
import com.example.spacejuice.R;
import com.example.spacejuice.Schedule;
import com.example.spacejuice.controller.HabitController;

import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.Date;

//public class HabitDetailsActivity extends AppCompatActivity implements View.OnClickListener{
public class HabitDetailsActivity extends AppCompatActivity {

    /*
This Activity is used to edit a habit
 */
    private TextView title;
//    private Button deleteB;
//    private Button confirmB;
    // i changed it from confirmB to editHabit
    private Button editHabit;

    private ImageButton backB;
//    private EditText NameEdit;
    private String name;
    private EditText DescriptionEdit;
    private String description;
    private TextView Monday;
    private TextView Tuesday;
    private TextView Wednesday;
    private TextView Thursday;
    private TextView Friday;
    private TextView Saturday;
    private TextView Sunday;
    private TextView SelectedDate;
//    private Button DateButton;
    private Date date;
    private int mYear, mMonth, mDay;
    private Format DateToString;
    private Schedule schedule;
    private Habit habit;
    private Schedule currentSchedule;
    private Member user;
    ActivityResultLauncher<Intent> editLaunch;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        editLaunch = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        Log.d("debugInfo", "result code: " + result.getResultCode());

//                        refreshData();
                        // copy/change from overview

                    }
        });
        super.onCreate(savedInstanceState);
        //set contentView and Editing user
        setContentView(R.layout.habit_description);

        /*
            Get the Unique Identifier of the Habit that we are viewing
         */
        int habitUid;
        if (savedInstanceState == null) {
            Bundle extras = getIntent().getExtras();
            if (extras == null) {
                habitUid = 0;
            } else {
                habitUid = extras.getInt("habitUid");
            }
        } else {
            habitUid = (int) savedInstanceState.getSerializable("habitUid");
        }
        habit = HabitController.getHabitFromUid(habitUid);

        //initializing
        title = findViewById(R.id.textViewHAE_hd);
        title.setText(habit.getTitle()); //Set the title into Add a Habit

        backB = findViewById(R.id.backButtonHAE);
        editHabit = findViewById(R.id.editButtonHAE_hd);
        SelectedDate = findViewById(R.id.textView5HAE_hd);
        date = habit.getStartDate();
        DateToString = new SimpleDateFormat("yyyy-MM-dd");
        SelectedDate.setText(DateToString.format(date));

        Monday = findViewById(R.id.monday_text);
        Tuesday = findViewById(R.id.tuesday_text);
        Wednesday = findViewById(R.id.wednesday_text);
        Thursday = findViewById(R.id.thursday_text);
        Friday = findViewById(R.id.friday_text);
        Saturday = findViewById(R.id.satuday_text);
        Sunday = findViewById(R.id.sunday_text);

//        if(!schedule.Mon()){
//            Monday.setVisibility(View.INVISIBLE);
//        }
//
//        if(!schedule.Tue()){
//            Tuesday.setVisibility(View.INVISIBLE);
//        }
//
//        if(!schedule.Wed()){
//            Wednesday.setVisibility(View.INVISIBLE);
//        }
//        if(!schedule.Thu()){
//            Thursday.setVisibility(View.INVISIBLE);
//        }
//
//        if(!schedule.Fri()){
//            Friday.setVisibility(View.INVISIBLE);
//        }
//
//        if(!schedule.Sat()){
//            Saturday.setVisibility(View.INVISIBLE);
//        }
//
//        if(!schedule.Sun()){
//            Sunday.setVisibility(View.INVISIBLE);
//        }

        currentSchedule = habit.getSchedule();
//        Monday.setChecked(currentSchedule.Mon());
//        Tuesday.setChecked(currentSchedule.Tue());
//        Wednesday.setChecked(currentSchedule.Wed());
//        Thursday.setChecked(currentSchedule.Thu());
//        Friday.setChecked(currentSchedule.Fri());
//        Saturday.setChecked(currentSchedule.Sat());
//        Sunday.setChecked(currentSchedule.Sun());

//        DateButton = findViewById(R.id.DateButtonHAE);
//        DateButton.setOnClickListener(this);

        // got rid of the edit habit button for now


        editHabit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HabitDetailsActivity.this, EditHabitActivity.class);
                intent.putExtra("habitUid", habitUid);
                editLaunch.launch(intent);

                startActivity(intent);
            }
        });


        backB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

//        deleteB.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                user = MainActivity.getUser();
//                user.deleteHabit(habit);
//                finish();
//            }
//        });

    }

    public void launchEventDetails(int uid) {
        Log.d("debugInfo", "event details launched for habit uid #" + uid);
    }

}
