package com.example.spacejuice;

import static org.junit.Assert.assertTrue;

import android.app.Activity;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;

import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.rule.ActivityTestRule;

import com.example.spacejuice.activity.AddHabitActivity;
import com.example.spacejuice.activity.AddHabitEventActivity;
import com.example.spacejuice.activity.EventDetailActivity;
import com.example.spacejuice.activity.HabitDetailsActivity;
import com.example.spacejuice.activity.LoginActivity;
import com.example.spacejuice.activity.MapsActivity;
import com.example.spacejuice.activity.OverviewActivity;
import com.robotium.solo.Solo;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

//import kotlinx.coroutines.internal.Segment;

public class HabitEventTest {

    private Solo solo;

    @Rule
    public ActivityTestRule<MainActivity> rule =
            new ActivityTestRule<>(MainActivity.class, true, true);


    @Before
    public void setUp(){
        solo = new Solo(InstrumentationRegistry.getInstrumentation(), rule.getActivity());
        solo.clickOnButton("Log in");
        assertTrue(solo.waitForActivity(LoginActivity.class));
        solo.enterText((EditText) solo.getView(R.id.userName), "MichelleObama");
        solo.enterText((EditText) solo.getView(R.id.editTextTextPassword), "Obama");
        solo.clickOnButton("Login");
        assertTrue(solo.waitForActivity("OverviewActivity", 10000));
        solo.assertCurrentActivity("Wrong activity", OverviewActivity.class);


    }


    @Test
    public void addHabitEventUploadText(){
        // upload description
        assertTrue(solo.waitForActivity(OverviewActivity.class));
        OverviewActivity activity = (OverviewActivity) solo.getCurrentActivity();
        final ListView listView = activity.today_habit_list;
        View view = listView.getAdapter().getView(0, null, listView);
        CheckBox checkBoxButton = (CheckBox)view.findViewById(R.id.habitCheckBox);
        solo.clickOnView(checkBoxButton);

        solo.assertCurrentActivity("Wrong activity", AddHabitEventActivity.class);

        solo.enterText((EditText) solo.getView(R.id.eventDescriptionEA), "I did it!!");
        assertTrue(solo.waitForActivity(AddHabitEventActivity.class));
        solo.clickOnButton("Publish");

    }

    @Test
    public void addHabitEventAddGPS(){
        // upload gps location
        assertTrue(solo.waitForActivity(OverviewActivity.class));
        OverviewActivity activity = (OverviewActivity) solo.getCurrentActivity();
        final ListView listView = activity.today_habit_list;
        View view = listView.getAdapter().getView(0, null, listView);
        CheckBox checkBoxButton = (CheckBox)view.findViewById(R.id.habitCheckBox);
        solo.clickOnView(checkBoxButton);
        solo.assertCurrentActivity("Wrong activity", AddHabitEventActivity.class);
        solo.clickOnButton("Add GPS");
        solo.assertCurrentActivity("Wrong activity", MapsActivity.class);
        solo.clickOnButton("Add");
        assertTrue(solo.waitForActivity(AddHabitEventActivity.class));
        solo.clickOnButton("Publish");

    }

    @Test
    public void editHabitEventText(){
        // edit text
        assertTrue(solo.waitForActivity(OverviewActivity.class));
        OverviewActivity activity = (OverviewActivity) solo.getCurrentActivity();
        final ListView listView = activity.today_habit_list;
        View view = listView.getAdapter().getView(0, null, listView);
        CheckBox checkBoxButton = (CheckBox)view.findViewById(R.id.habitCheckBox);
        solo.clickOnView(checkBoxButton);

        solo.assertCurrentActivity("Wrong activity", AddHabitEventActivity.class);
        solo.enterText((EditText) solo.getView(R.id.eventDescriptionEA), "I did it!!");
        solo.clickOnButton("Add GPS");
        solo.assertCurrentActivity("Wrong activity", MapsActivity.class);
        solo.clickOnButton("Add");
        assertTrue(solo.waitForActivity(AddHabitEventActivity.class));
        solo.clickOnButton("Publish");

        assertTrue(solo.waitForActivity(OverviewActivity.class));
        solo.clickInList(0);

        assertTrue(solo.waitForActivity(HabitDetailsActivity.class));
        HabitDetailsActivity activity2 = (HabitDetailsActivity) solo.getCurrentActivity();
        final ListView listView2 = activity2.habitEventList;
        View view2 = listView.getAdapter().getView(0, null, listView);
        solo.clickOnView(view2);

        solo.assertCurrentActivity("Wrong activity", HabitDetailsActivity.class);

        solo.enterText((EditText) solo.getView(R.id.edit_editText), "Good job!!");
        solo.sleep(1000);
        solo.clickOnButton("Confirm");
        //assertTrue(solo.waitForActivity(AddHabitEventActivity.class));


    }
    @After
    public void tearDown() throws Exception {
        solo.finishOpenedActivities();
    }



}
