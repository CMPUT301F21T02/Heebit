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
import com.example.spacejuice.activity.LoginActivity;
import com.example.spacejuice.activity.OverviewActivity;
import com.robotium.solo.Solo;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

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


    }

//    @Test
//    public void start() throws Exception{
//        Activity activity = rule.getActivity();
//
//    }


    @Test
    public void addHabitEventTest() {
        ListView ListView=(android.widget.ListView)solo.getView(R.id.overview_habit_listview);
        View view=ListView.getChildAt(0);
        CheckBox checkBoxButton = (CheckBox)view.findViewById(R.id.overview_checkBox);
        solo.clickOnView(checkBoxButton);
        // solo.clickOnImageButton(1);
        solo.assertCurrentActivity("Wrong activity", AddHabitEventActivity.class);
    }

    @After
    public void tearDown() throws Exception {
        solo.finishOpenedActivities();
    }



}
