package com.example.spacejuice.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
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
    private int habitIndicator;
    //    private Button DateButton;
    private Date date;
    private int mYear, mMonth, mDay;
    private Format DateToString;
    private String dateAsString;
    private Schedule schedule;
    private Habit habit;
    private Schedule currentSchedule;
    private Member user;
    private TextView reason;
    private ImageView indicatorImage;
    ActivityResultLauncher<Intent> editLaunch;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        editLaunch = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        Log.d("debugInfo", "result code: " + result.getResultCode());

                        refreshData();
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

        backB = findViewById(R.id.backButtonHAE);
        editHabit = findViewById(R.id.editButtonHAE_hd);
        SelectedDate = findViewById(R.id.textView5HAE_hd);
        indicatorImage = findViewById(R.id.desc_habit_indicator);
        indicatorImage.setImageResource(habitIndicator);
        Monday = findViewById(R.id.monday_text);
        Tuesday = findViewById(R.id.tuesday_text);
        Wednesday = findViewById(R.id.wednesday_text);
        Thursday = findViewById(R.id.thursday_text);
        Friday = findViewById(R.id.friday_text);
        Saturday = findViewById(R.id.saturday_text);
        Sunday = findViewById(R.id.sunday_text);
        title = findViewById(R.id.textViewHAE_hd);
        reason = findViewById(R.id.HabitReasonHAE_hd);

         /* date formatting retrieved from
        https://stackoverflow.com/questions/17192776/get-value-of-day-month-from-date-object-in-android
         */

        date = habit.getStartDate();

        String dayOfTheWeek = (String) DateFormat.format("EEEE", date); // Thursday
        String day          = (String) DateFormat.format("dd",   date); // 20
        String monthString  = (String) DateFormat.format("MMMM",  date); // September
        String year         = (String) DateFormat.format("yyyy", date); // 2013

        SelectedDate.setText("Started on " + dayOfTheWeek + ", " + monthString + " " + day + ", " +
                year + ".");
        habitIndicator = habit.getIndicator().getImage();

        refreshData();

        editHabit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HabitDetailsActivity.this, EditHabitActivity.class);
                intent.putExtra("habitUid", habitUid);
                editLaunch.launch(intent);
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

    public void refreshData() {

        currentSchedule = habit.getSchedule();
        title.setText(habit.getTitle()); //Set the title into Add a Habit
        String reasonText = "\"" + habit.getReason() + "\"";
        reason.setText(reasonText);

        if (!currentSchedule.Mon()) {
            Monday.setVisibility(View.INVISIBLE);
        } else {
            Monday.setVisibility(View.VISIBLE);
        }

        if (!currentSchedule.Tue()) {
            Tuesday.setVisibility(View.INVISIBLE);
        } else {
            Tuesday.setVisibility(View.VISIBLE);
        }

        if (!currentSchedule.Wed()) {
            Wednesday.setVisibility(View.INVISIBLE);
        } else {
            Wednesday.setVisibility(View.VISIBLE);
        }

        if (!currentSchedule.Thu()) {
            Thursday.setVisibility(View.INVISIBLE);
        } else {
            Thursday.setVisibility(View.VISIBLE);
        }

        if (!currentSchedule.Fri()) {
            Friday.setVisibility(View.INVISIBLE);
        } else {
            Friday.setVisibility(View.VISIBLE);
        }

        if (!currentSchedule.Sat()) {
            Saturday.setVisibility(View.INVISIBLE);
        } else {
            Saturday.setVisibility(View.VISIBLE);
        }

        if (!currentSchedule.Sun()) {
            Sunday.setVisibility(View.INVISIBLE);
        } else {
            Sunday.setVisibility(View.VISIBLE);
        }
    }

}

