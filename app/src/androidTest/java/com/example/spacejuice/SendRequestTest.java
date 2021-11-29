package com.example.spacejuice;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import android.app.Activity;
import android.view.View;
import android.widget.Button;
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

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class SendRequestTest {
    private Solo solo;

    @Rule
    public ActivityTestRule<MainActivity> rule =
            new ActivityTestRule<>(MainActivity.class, true, true);


    @Before
    public void setUp(){
        solo = new Solo(InstrumentationRegistry.getInstrumentation(), rule.getActivity());

    }

    /**
     * Send Request from Obama Account
     */
    @Test
    public void Test1SendRequest(){
        solo.clickOnButton("Log in");
        assertTrue(solo.waitForActivity(LoginActivity.class));
        solo.enterText((EditText) solo.getView(R.id.userName), "Obama");
        solo.enterText((EditText) solo.getView(R.id.editTextTextPassword), "Murica22");
        solo.clickOnButton("Login");
        ImageButton profileButton = (ImageButton) solo.getView("profile_imagebutton");
        solo.clickOnView(profileButton);
        solo.assertCurrentActivity("wrong activity", MyProfileActivity.class);
        solo.clickOnButton("Send Request");
        solo.enterText((EditText) solo.getView(R.id.TargetMemberNameRF), "Joe");
        Button Button = (Button) solo.getView("requestsButtonRF");
        solo.clickOnView(Button);

    }

    /**
     * Decline Request From Joe Account
     */
    @Test
    public void Test2DeclineRequest(){
        solo.clickOnButton("Log in");
        assertTrue(solo.waitForActivity(LoginActivity.class));
        solo.enterText((EditText) solo.getView(R.id.userName), "Joe");
        solo.enterText((EditText) solo.getView(R.id.editTextTextPassword), "Biden");
        solo.clickOnButton("Login");
        ImageButton profileButton = (ImageButton) solo.getView("profile_imagebutton");
        solo.clickOnView(profileButton);
        solo.assertCurrentActivity("wrong activity", MyProfileActivity.class);
        CardView requestCard = (CardView) solo.getView("RequestCountCard");
        solo.clickOnView(requestCard);
        solo.assertCurrentActivity("Wrong Activity", FollowerRequestsActivity.class);

        // Click Decline Button
        solo.sleep(1000);
        FollowerRequestsActivity followerRequestsActivity = (FollowerRequestsActivity) solo.getCurrentActivity();
        final ListView listView = followerRequestsActivity.followerList;
        View view = listView.getAdapter().getView(0, null, listView);
        ImageButton declineButton = (ImageButton)view.findViewById(R.id.button_follower_deny);
        solo.clickOnView(declineButton);

        ImageButton backButton = (ImageButton) solo.getView("backButtonFollowerRequests");
        solo.clickOnView(backButton);

        assertTrue(solo.waitForActivity("MyProfileActivity", 4000));

        MyProfileActivity myProfileActivity = (MyProfileActivity) solo.getCurrentActivity();
        TextView followersCount = myProfileActivity.followersCount;
        String followersCountString = followersCount.getText().toString();
        assertEquals("0", followersCountString);

    }

}