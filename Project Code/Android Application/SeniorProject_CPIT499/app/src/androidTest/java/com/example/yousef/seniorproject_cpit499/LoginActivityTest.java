package com.example.yousef.seniorproject_cpit499;

import android.app.Activity;
import android.app.Instrumentation;
import android.support.test.espresso.Espresso;
import android.support.test.espresso.IdlingPolicies;
import android.support.test.espresso.contrib.RecyclerViewActions;
import android.support.test.rule.ActivityTestRule;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import java.util.concurrent.TimeUnit;

import static android.support.test.InstrumentationRegistry.getInstrumentation;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.Espresso.openActionBarOverflowOrOptionsMenu;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.contrib.RecyclerViewActions.actionOnItemAtPosition;
import static android.support.test.espresso.matcher.ViewMatchers.hasDescendant;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.CoreMatchers.allOf;
import static org.junit.Assert.*;

public class LoginActivityTest {

    @Rule
    public ActivityTestRule<LoginActivity> LogTestRule = new ActivityTestRule<LoginActivity>(LoginActivity.class);

    Instrumentation.ActivityMonitor home = getInstrumentation().addMonitor(HomeActivity.class.getName(), null, false);
    Instrumentation.ActivityMonitor fridge = getInstrumentation().addMonitor(FrigdeActivity.class.getName(), null, false);
    Instrumentation.ActivityMonitor cart = getInstrumentation().addMonitor(CartActivity.class.getName(), null, false);

    private String email = "abamby2007@gmail.com";
    private String password = "123456";

    @Before
    public void setUp() throws Exception {
    }

    @Test
    public void TestUserInput() throws InterruptedException {
        // enter user email
        onView(withId(R.id.loginEmail)).perform(typeText(email));

        //enter user password
        onView(withId(R.id.password)).perform(typeText(password));

        //close soft keyboard
        Espresso.closeSoftKeyboard();
        //click login button
        onView(withId(R.id.logInBot)).perform(click());

        Thread.sleep(500);
        Activity homeActivity = getInstrumentation().waitForMonitor(home);

        assertNotNull(homeActivity);
        onView(withId(R.id.fridgeItem)).perform(click());
        Thread.sleep(3000);

        Activity fridgeActivity = getInstrumentation().waitForMonitor(fridge);
        assertNotNull(fridgeActivity);
        Thread.sleep(500);
        Espresso.pressBack();
        Thread.sleep(500);

        // onView(withRecyclerView(R.id.recyclerView) .atPositionOnView(1, R.id.addToCart_button)).perform(click());

       /* onView(withId(R.id.recylcer_view))
                .perform(RecyclerViewActions.actionOnItem(
                        hasDescendant(withText("Text of item you want to scroll to")),
                        click()));*/


        onView(withId(R.id.recyclerView))
                .check(matches(hasDescendant(withText("add to cart"))));
        onView(withId(R.id.recyclerView))
                .perform(actionOnItemAtPosition(0, click()));


        Thread.sleep(500);

        openActionBarOverflowOrOptionsMenu(getInstrumentation().getTargetContext());
        onView(withText("Cart")).perform(click());

        Thread.sleep(500);

        onView(withId(R.id.proceedToCheckout_button)).perform(click());

        Activity cartActivity = getInstrumentation().waitForMonitor(cart);

        Thread.sleep(500);
        onView(withId(R.id.confirmOrder_button)).perform(click());

        Thread.sleep(500);
        IdlingPolicies.setIdlingResourceTimeout(10, TimeUnit.SECONDS);

    }

    @After
    public void tearDown() throws Exception {
    }
}