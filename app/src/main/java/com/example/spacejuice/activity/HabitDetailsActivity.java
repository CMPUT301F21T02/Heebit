package com.example.spacejuice.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

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
    private CheckBox Monday;
    private CheckBox Tuesday;
    private CheckBox Wednesday;
    private CheckBox Thursday;
    private CheckBox Friday;
    private CheckBox Saturday;
    private CheckBox Sunday;
    private TextView SelectedDate;
//    private Button DateButton;
    private Date date;
    private int mYear, mMonth, mDay;
    private Format DateToString;
    private Schedule schedule;
    private Habit habit;
    private Schedule currentSchedule;
    private Member user;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //set contentView and Editing user
        setContentView(R.layout.habit_description);

        /*
            Get the Unique Identifier of the Habit that we are editing
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
//        deleteB = findViewById(R.id.DeleteButtonHAE);
//        deleteB.setVisibility(View.VISIBLE); //Show the delete button

        backB = findViewById(R.id.backButtonHAE);
        editHabit = findViewById(R.id.editButtonHAE_hd);
        SelectedDate = findViewById(R.id.textView5HAE_hd);
        date = habit.getStartDate();
        DateToString = new SimpleDateFormat("yyyy-MM-dd");
        SelectedDate.setText(DateToString.format(date));

//        name = NameEdit.getText().toString();  //get the name
//        description = DescriptionEdit.getText().toString(); //Get the Description

//        NameEdit = findViewById(R.id.HabitNameHAE);     //set the habit name to current name
//        NameEdit.setText(habitEditing.getTitle());

//        DescriptionEdit = findViewById(R.id.HabitReasonHAE);
//        DescriptionEdit.setText(habitEditing.getReason());

        Monday = findViewById(R.id.Monday);
        Tuesday = findViewById(R.id.Tuesday);
        Wednesday = findViewById(R.id.Wednesday);
        Thursday = findViewById(R.id.Thursday);
        Friday = findViewById(R.id.Friday);
        Saturday = findViewById(R.id.Saturday);
        Sunday = findViewById(R.id.Sunday);

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
                startActivity(intent);
            }
        });


//        editHabit.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                name = user.getMemberName();  //get the name
//                description = DescriptionEdit.getText().toString(); //Get the Description
//                schedule = new Schedule(Sunday.isChecked(), Monday.isChecked(), Tuesday.isChecked(),
//                        Wednesday.isChecked(), Thursday.isChecked(), Friday.isChecked(), Saturday.isChecked());
//                habitEditing.setStartDate(date);
//                habitEditing.setTitle(name);
//                habitEditing.setReason(description);
//                habitEditing.setSchedule(schedule);
//                finish();
//
//
//            }
//        });

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
//    @Override
//    public void onClick(View view) {
//        final Calendar c = Calendar.getInstance();
//        mYear = c.get(Calendar.YEAR);
//        mMonth = c.get(Calendar.MONTH);
//        mDay = c.get(Calendar.DAY_OF_MONTH);
//        DatePickerDialog datePickerDialog = new DatePickerDialog(this,
//                new DatePickerDialog.OnDateSetListener() {
//
//                    @Override
//                    public void onDateSet(DatePicker view, int year,
//                                          int monthOfYear, int dayOfMonth) {
//                        date = new Date(year - 1900, monthOfYear, dayOfMonth);
//                        SelectedDate.setText(year + "-" + (monthOfYear + 1) + "-" + dayOfMonth);
//
//                    }
//                }, mYear, mMonth, mDay);
//        datePickerDialog.show();
//    }
}
