package com.example.spacejuice;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.junit.Assert.assertTrue;

import static java.util.regex.Pattern.matches;

import android.app.Activity;
import android.widget.EditText;

import androidx.test.espresso.Espresso;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.LargeTest;
import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.rule.ActivityTestRule;

import com.example.spacejuice.activity.AddHabitActivity;
import com.example.spacejuice.activity.AllHabitsActivity;
import com.example.spacejuice.activity.LoginActivity;
import com.example.spacejuice.activity.OverviewActivity;
import com.robotium.solo.Solo;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
@LargeTest
public class MainActivityTest {

    @Rule
    public ActivityScenarioRule<MainActivity> activityRule =
            new ActivityScenarioRule<>(MainActivity.class);

    @Before
    public void login(){
        Espresso.onView(withId(R.id.userName)).perform(typeText("Josh"));
        Espresso.onView(withId(R.id.editTextTextPassword)).perform(typeText("12345"));
        Espresso.closeSoftKeyboard();
        Espresso.onView(withId(R.id.loginButton)).perform(click());
    }

    @Test
    public void test() {
        
    }
}
