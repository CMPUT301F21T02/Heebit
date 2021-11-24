package com.example.spacejuice.activity;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.transition.Scene;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.spacejuice.Habit;
import com.example.spacejuice.MainActivity;
import com.example.spacejuice.Member;
import com.example.spacejuice.R;
import com.example.spacejuice.Schedule;
import com.example.spacejuice.controller.HabitController;
import com.example.spacejuice.controller.TimeController;

import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class EditHabitActivity extends AppCompatActivity implements View.OnClickListener{
    /*
This Activity is used to edit a habit
 */
    private TextView title;
    private Button deleteB;
    private Button confirmB;
    private ImageButton backB;
    private EditText NameEdit;
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
    private Button DateButton;
    private Date date;
    private int mYear, mMonth, mDay;
    private Format DateToString;
    private Schedule schedule;
    private Habit habitEditing;
    private CheckBox habitPrivate;
    private Schedule currentSchedule;
    private Member user;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //set contentView and Editing user
        setContentView(R.layout.habit_add_edit);

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
        habitEditing = HabitController.getHabitFromUid(habitUid);

        // modify the "Add a Habit layout for Editing a habit
        title = findViewById(R.id.textViewHAE);
        title.setText(getString(R.string.editThisHabit)); //Set the title into Add a Habit
        LinearLayout dateContainer = findViewById(R.id.LL_DateContainer);
        ViewGroup.LayoutParams dateContainerParams = dateContainer.getLayoutParams();
        ViewGroup.MarginLayoutParams dateContainerMargins =
                (ViewGroup.MarginLayoutParams) dateContainer.getLayoutParams();
        // change the height and margins of the "set date" container to zero and set it to INVISIBLE
        dateContainerParams.height = 0;
        dateContainerMargins.setMargins(0,0,0,0);
        dateContainer.setVisibility(View.INVISIBLE);
        dateContainer.setLayoutParams(dateContainerParams);
        dateContainer.setLayoutParams(dateContainerMargins);

        backB = findViewById(R.id.backButtonHAE);
        confirmB = findViewById(R.id.confirmButtonHAE);
        SelectedDate = findViewById(R.id.textView5HAE);
        date = habitEditing.getStartDate();
        DateToString = new SimpleDateFormat("yyyy-MM-dd");
        SelectedDate.setText(DateToString.format(date));
        NameEdit = findViewById(R.id.HabitNameHAE);     //set the habit name to current name
        NameEdit.setText(habitEditing.getTitle());

        DescriptionEdit = findViewById(R.id.HabitReasonHAE);
        DescriptionEdit.setText(habitEditing.getReason());

        Monday = findViewById(R.id.Monday);
        Tuesday = findViewById(R.id.Tuesday);
        Wednesday = findViewById(R.id.Wednesday);
        Thursday = findViewById(R.id.Thursday);
        Friday = findViewById(R.id.Friday);
        Saturday = findViewById(R.id.Saturday);
        Sunday = findViewById(R.id.Sunday);

        currentSchedule = habitEditing.getSchedule();
        Monday.setChecked(currentSchedule.Mon());
        Tuesday.setChecked(currentSchedule.Tue());
        Wednesday.setChecked(currentSchedule.Wed());
        Thursday.setChecked(currentSchedule.Thu());
        Friday.setChecked(currentSchedule.Fri());
        Saturday.setChecked(currentSchedule.Sat());
        Sunday.setChecked(currentSchedule.Sun());

        habitPrivate = findViewById(R.id.private_checkbox);
        habitPrivate.setChecked(habitEditing.isPrivate());

        DateButton = findViewById(R.id.DateButtonHAE);
        DateButton.setOnClickListener(this);



        confirmB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                name = NameEdit.getText().toString();  //get the name
                description = DescriptionEdit.getText().toString(); //Get the Description
                habitEditing.setStartDate(date);
                habitEditing.setTitle(name);
                habitEditing.setReason(description);
                habitEditing.setPrivacy(habitPrivate.isChecked());
                habitEditing.getSchedule().changeTo(Sunday.isChecked(), Monday.isChecked(), Tuesday.isChecked(),
                        Wednesday.isChecked(), Thursday.isChecked(), Friday.isChecked(), Saturday.isChecked());
                Log.d("debugInfo", "sending habit uid: " + habitUid + " in for updating");
                HabitController.updateHabit(habitEditing);
                Intent resultIntent = new Intent();
                setResult(Activity.RESULT_OK, resultIntent);
                finish();

            }
        });

        backB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }
    @Override
    public void onClick(View view) {
        Calendar c = TimeController.getCurrentTime();
        mYear = c.get(Calendar.YEAR);
        mMonth = c.get(Calendar.MONTH);
        mDay = c.get(Calendar.DAY_OF_MONTH);
        DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                new DatePickerDialog.OnDateSetListener() {

                    @Override
                    public void onDateSet(DatePicker view, int year,
                                          int monthOfYear, int dayOfMonth) {
                        Calendar cal = Calendar.getInstance();
                        cal.set(Calendar.YEAR, year);
                        cal.set(Calendar.MONTH, monthOfYear);
                        cal.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                        date = cal.getTime();
                        SelectedDate.setText(getString(R.string.dateString, year, monthOfYear+1, dayOfMonth));
                    }
                }, mYear, mMonth, mDay);
        datePickerDialog.show();
    }
}
