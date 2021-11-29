package com.example.spacejuice;

import static org.junit.Assert.assertTrue;

import android.app.Activity;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.rule.ActivityTestRule;

import com.example.spacejuice.activity.AddHabitActivity;
import com.example.spacejuice.activity.EditHabitActivity;
import com.example.spacejuice.activity.HabitDetailsActivity;
import com.example.spacejuice.activity.LoginActivity;
import com.example.spacejuice.activity.OverviewActivity;
import com.example.spacejuice.activity.WelcomeActivity;
import com.robotium.solo.Solo;

import org.junit.After;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import org.w3c.dom.Text;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)

public class HabitTests {

    private Solo solo;

    @Rule
    public ActivityTestRule<MainActivity> rule =
            new ActivityTestRule<>(MainActivity.class, true, true);


    @Before
    public void setUp(){
        solo = new Solo(InstrumentationRegistry.getInstrumentation(), rule.getActivity());
        solo.clickOnButton("Log in");
        assertTrue(solo.waitForActivity(LoginActivity.class));
        solo.enterText((EditText) solo.getView(R.id.userName), "Obama");
        solo.enterText((EditText) solo.getView(R.id.editTextTextPassword), "Murica22");
        solo.clickOnButton("Login");
        assertTrue(solo.waitForActivity("OverviewActivity", 10000));
        solo.assertCurrentActivity("Wrong activity", OverviewActivity.class);

    }


    @Test
    public void Test1_addHabitTest() {
        solo.assertCurrentActivity("Wrong activity", OverviewActivity.class);
        ImageButton image1 = (ImageButton) solo.getView("add_habit_imagebutton");
        solo.clickOnView(image1);
        // solo.clickOnImageButton(1);
        solo.assertCurrentActivity("Wrong activity", AddHabitActivity.class);
        solo.enterText((EditText) solo.getView(R.id.HabitNameHAE), "Work On Talking");
        solo.enterText((EditText) solo.getView(R.id.HabitReasonHAE), "For Speeche"); // Deliberate spelling mistake

        CheckBox sunday = (CheckBox) solo.getView("Sunday");
        solo.clickOnView(sunday);

        CheckBox monday = (CheckBox) solo.getView("Monday");
        solo.clickOnView(monday);

        CheckBox tuesday = (CheckBox) solo.getView("Tuesday");
        solo.clickOnView(tuesday);

        CheckBox wednesday = (CheckBox) solo.getView("Wednesday");
        solo.clickOnView(wednesday);

        CheckBox thursday = (CheckBox) solo.getView("Thursday");
        solo.clickOnView(thursday);

        CheckBox friday = (CheckBox) solo.getView("Friday");
        solo.clickOnView(friday);

        CheckBox saturday = (CheckBox) solo.getView("Saturday");
        solo.clickOnView(saturday);

        solo.clickOnButton("CONFIRM");
    }

    @Test
    public void Test2_editHabitTest() {
//        TextView habit_text_view = (TextView) solo.getView("overview_habit_text");
        solo.clickInList(0);
        solo.assertCurrentActivity("Wrong activity", HabitDetailsActivity.class);
        ImageView editButtonImageView = (ImageView) solo.getView("desc_edit_button");
        solo.clickOnView(editButtonImageView);
        solo.assertCurrentActivity("Wrong activity", EditHabitActivity.class);
        solo.enterText((EditText) solo.getView(R.id.HabitReasonHAE), "For Speeches");
        solo.clickOnButton("CONFIRM");
        solo.assertCurrentActivity("Wrong activity", HabitDetailsActivity.class);
    }

    @Test
    public void Test3_deleteHabitTest() {
        solo.clickInList(0);
        solo.assertCurrentActivity("Wrong activity", HabitDetailsActivity.class);
        ImageView trash = (ImageView) solo.getView("desc_delete_button");
        solo.clickOnView(trash);
        solo.clickOnButton("No");
        solo.assertCurrentActivity("Wrong activity", HabitDetailsActivity.class);
        solo.clickOnView(trash);
        solo.clickOnButton("Yes");
        solo.assertCurrentActivity("Wrong activity", OverviewActivity.class);

    }


    @After
    public void tearDown() throws Exception {
        solo.finishOpenedActivities();
    }




}