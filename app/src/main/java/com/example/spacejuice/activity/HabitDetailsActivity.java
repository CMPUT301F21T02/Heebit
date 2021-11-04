package com.example.spacejuice.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Point;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.content.res.AppCompatResources;

import com.example.spacejuice.Habit;
import com.example.spacejuice.HabitEvent;
import com.example.spacejuice.HabitEventAdapter;
import com.example.spacejuice.HabitListAdapter;
import com.example.spacejuice.MainActivity;
import com.example.spacejuice.Member;
import com.example.spacejuice.R;
import com.example.spacejuice.Schedule;
import com.example.spacejuice.controller.HabitController;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
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


    private TextView Monday;
    private TextView Tuesday;
    private TextView Wednesday;
    private TextView Thursday;
    private TextView Friday;
    private TextView Saturday;
    private TextView Sunday;
    private Habit habit;
    private TextView reason;
    private TextView level;
    private ListView habitEventList;
    private LinearLayout indicatorImage;
    private ArrayList<HabitEvent> habitEventListItems;
    private HabitEventAdapter habitEventAdapter;
    ActivityResultLauncher<Intent> editLaunch;

    public HabitDetailsActivity() {
    }


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

        ImageButton backB = findViewById(R.id.backButtonHAE);
        ImageView editHabit = findViewById(R.id.desc_edit_button);
        ImageView deleteHabit = findViewById(R.id.desc_delete_button);
        TextView selectedDate = findViewById(R.id.textView5HAE_hd);
        Monday = findViewById(R.id.monday_text);
        Tuesday = findViewById(R.id.tuesday_text);
        Wednesday = findViewById(R.id.wednesday_text);
        Thursday = findViewById(R.id.thursday_text);
        Friday = findViewById(R.id.friday_text);
        Saturday = findViewById(R.id.saturday_text);
        Sunday = findViewById(R.id.sunday_text);
        title = findViewById(R.id.textViewHAE_hd);
        reason = findViewById(R.id.HabitReasonHAE_hd);
        indicatorImage = findViewById(R.id.LL_Indicator);
        level = findViewById(R.id.habit_level);

         /* date formatting retrieved from
        https://stackoverflow.com/questions/17192776/get-value-of-day-month-from-date-object-in-android
         */

        //    private Button DateButton;
        Date date = habit.getStartDate();

        String dayOfTheWeek = (String) DateFormat.format("EEEE", date); // Thursday
        String day          = (String) DateFormat.format("dd", date); // 20
        String monthString  = (String) DateFormat.format("MMMM", date); // September
        String year         = (String) DateFormat.format("yyyy", date); // 2013

        selectedDate.setText("Started on " + dayOfTheWeek + ", " + monthString + " " + day + ", " +
                year + ".");
        habitEventList = findViewById(R.id.list_of_my_habit_events);

        adjustForSmallDisplay(); // title width is resized if display is small
        refreshData();

        editHabit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HabitDetailsActivity.this, EditHabitActivity.class);
                intent.putExtra("habitUid", habitUid);
                editLaunch.launch(intent);
            }
        });

        deleteHabit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(HabitDetailsActivity.this);
                builder.setMessage("Are you sure you want to delete the habit: " + habit.getTitle() + "?")
                        .setCancelable(false)
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                HabitController.deleteHabit(habit);
                                Intent resultIntent = new Intent();
                                setResult(Activity.RESULT_OK, resultIntent);
                                finish();
                            }
                        })
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });
                AlertDialog alert = builder.create();
                alert.show();
            }
        });

        // for debugging: click on indicator within HabitDetails to simulate progress
        indicatorImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                habit.getIndicator().increase();
                refreshData();
            }
        });

        backB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    public void adjustForSmallDisplay() {
        /* if the display is small, adjusts the title width to fit properly */
        if (MainActivity.checkForSmallDisplay(this)) {
            ViewGroup.LayoutParams layoutParams = title.getLayoutParams();
            layoutParams.width = 520;
            title.setLayoutParams(layoutParams);
        }
    }

    public void launchEventDetails(int uid) {
        Log.d("debugInfo", "event details launched for habit uid #" + uid);
    }

    public void refreshData() {

        Schedule currentSchedule = habit.getSchedule();
        indicatorImage.setBackground(AppCompatResources.getDrawable(this, habit.getIndicator().getImage()));
        level.setText(habit.getIndicator().getIndicatorText());

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
        /* updates the list of Habits */
        habitEventListItems = HabitController.getHabitEvents(habit);

        this.habitEventAdapter = new HabitEventAdapter(this, R.layout.habit_event_content, habitEventListItems);
        Log.d("debugInfo", "habit event list updated");
        Log.d("debugInfo", "list size: " + habitEventListItems.size());
        this.habitEventList.setAdapter(habitEventAdapter);
        this.habitEventAdapter.notifyDataSetChanged();
    }

}

