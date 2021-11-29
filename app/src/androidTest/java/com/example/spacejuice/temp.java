package com.example.spacejuice;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import android.app.Activity;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.rule.ActivityTestRule;

import com.example.spacejuice.activity.AddHabitActivity;
import com.example.spacejuice.activity.FollowerRequestsActivity;
import com.example.spacejuice.activity.LoginActivity;
import com.example.spacejuice.activity.MyProfileActivity;
import com.example.spacejuice.activity.OverviewActivity;
import com.example.spacejuice.activity.RequestSendFragment;
import com.example.spacejuice.activity.WelcomeActivity;
import com.robotium.solo.Solo;

import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runners.MethodSorters;

public class temp {

    private Solo solo;

    @Rule
    public ActivityTestRule<MainActivity> rule =
            new ActivityTestRule<>(MainActivity.class, true, true);

    @Before
    public void setUp() {
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
    public void addHabitEventTest() {
        solo.sleep(1000);
        OverviewActivity activity = (OverviewActivity) solo.getCurrentActivity();
        final ListView listView = activity.today_habit_list;
        View view = listView.getAdapter().getView(0, null, listView);
        CheckBox checkBoxButton = (CheckBox) view.findViewById(R.id.habitCheckBox);
        solo.clickOnView(checkBoxButton);
    }

//        @After
//        public void tearDown() throws Exception {
//            solo.finishOpenedActivities();
//        }


}
