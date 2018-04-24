package com.example.yousef.seniorproject_cpit499;

import android.app.Activity;
import android.app.Instrumentation;
import android.support.test.rule.ActivityTestRule;
import android.view.View;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import static android.support.test.InstrumentationRegistry.getInstrumentation;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.core.AllOf.allOf;
import static org.junit.Assert.*;

public class SignUpActivityTest {

    @Rule
    public ActivityTestRule<SignUpActivity> signUpTestRule = new ActivityTestRule<>(SignUpActivity.class);

    private String username = "test";
    private String email = "test@gamil.com";
    private String confirmEmail = "test@gmail.com";
    private String password = "123456";
    private String confirmPassword = "123456";

    private SignUpActivity signUpActivity = null;

    Instrumentation.ActivityMonitor monitor = getInstrumentation().addMonitor(HomeActivity.class.getName(), null, false);

    @Before
    public void setUp() throws Exception {

        signUpActivity = signUpTestRule.getActivity();

    }

    @Test
    public void test() {

        assertNotNull(username);
        assertNotNull(email);
        assertNotNull(confirmEmail);
        assertNotNull(password);
        assertNotNull(confirmPassword);
        assertNotNull(R.id.registerBot);

        onView(withId(R.id.userName)).perform(typeText(username));
        onView(withId(R.id.email)).perform(typeText(email));
        onView(withId(R.id.confirmEmail)).perform(typeText(confirmEmail));
        onView(withId(R.id.password)).perform(typeText(password));
        onView(withId(R.id.confirmPassword)).perform(typeText(confirmPassword));
        onView(withId(R.id.registerBot)).perform(click());

        Activity homeActivity = getInstrumentation().waitForMonitor(monitor);

        assertNotNull(homeActivity);
    }

    @After
    public void tearDown() throws Exception {
        signUpActivity = null;
    }
}