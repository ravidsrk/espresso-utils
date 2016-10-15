package com.ravidsrk;

import android.app.Activity;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.support.annotation.IdRes;
import android.support.annotation.StringRes;
import android.support.test.InstrumentationRegistry;
import android.support.test.espresso.ViewInteraction;
import android.support.test.espresso.matcher.BoundedMatcher;
import android.support.test.rule.ActivityTestRule;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.scrollTo;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isAssignableFrom;
import static android.support.test.espresso.matcher.ViewMatchers.isDescendantOfA;
import static android.support.test.espresso.matcher.ViewMatchers.withChild;
import static android.support.test.espresso.matcher.ViewMatchers.withHint;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withParent;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.core.IsNot.not;

public class EspressoUtils {

    /*
    /* Use this method to find the EditText within the TextInputLayout. Useful for typing into the TextInputLayout
    */
    public static ViewInteraction onEditTextWithinTilWithId(@IdRes int textInputLayoutId) {
        //Note, if you have specified an ID for the EditText that you place inside
        //the TextInputLayout, use that instead - i.e, onView(withId(R.id.my_edit_text));
        return onView(allOf(isDescendantOfA(withId(textInputLayoutId)), isAssignableFrom(EditText.class)));
    }

    /*
    * Use this method to find the error view within the TextInputLayout. Useful for asseting that certain errors are displayed to the user
    */
    public static ViewInteraction onErrorViewWithinTilWithId(@IdRes int textInputLayoutId) {
        return onView(allOf(isDescendantOfA(withId(textInputLayoutId)), not(isAssignableFrom(EditText.class)), isAssignableFrom(TextView.class)));
    }

    /*
    * Use this method to rotate screen
    */
    private void rotateScreen(ActivityTestRule<?> activityRule) {
        Context context = InstrumentationRegistry.getTargetContext();
        int orientation = context.getResources().getConfiguration().orientation;

        Activity activity = activityRule.getActivity();
        activity.setRequestedOrientation(
                (orientation == Configuration.ORIENTATION_PORTRAIT) ?
                        ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE : ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
    }

    /*
    * Use this method to check toolbar title
    */
    private static ViewInteraction matchToolbarTitle(CharSequence title) {
        return onView(isAssignableFrom(Toolbar.class))
                .check(matches(withToolbarTitle(is(title))));
    }

    private static Matcher<Object> withToolbarTitle(final Matcher<CharSequence> textMatcher) {
        return new BoundedMatcher<Object, Toolbar>(Toolbar.class) {
            @Override public boolean matchesSafely(Toolbar toolbar) {
                return textMatcher.matches(toolbar.getTitle());
            }
            @Override public void describeTo(Description description) {
                description.appendText("with toolbar title: ");
                textMatcher.describeTo(description);
            }
        };
    }

    public static Matcher<Object> withValue(final int value) {
        return new BoundedMatcher<Object, Object>(Object.class) {
            @Override
            public void describeTo(Description description) {
                description.appendText("has value " + value);
            }
            @Override
            public boolean matchesSafely(Object item) {
                return item.toString().equals(String.valueOf(value));
            }
        };
    }

    /**************
     * TEXT ENTRY *
     **************/
    public static void enterTextIntoViewWithHint(String aTextToEnter, @StringRes int aHintResID) {
        onView(withHint(aHintResID)).perform(typeText(aTextToEnter));
    }

    public static void enterTextIntoViewWithID(String aTextToEnter, @IdRes int aViewID) {
        onView(withId(aViewID)).perform(typeText(aTextToEnter));
    }

    /*************
     * SCROLLING *
     *************/
    public static void scrollToViewWithID(@IdRes int aViewIDRes) {
        onView(withId(aViewIDRes)).perform(scrollTo());
    }

    /***********
     * TAPPING *
     ***********/
    public static void tapViewWithText(String aText) {
        onView(withText(aText)).perform(click());
    }

    public static void tapViewWithText(@StringRes int aTextResID) {
        onView(withText(aTextResID)).perform(click());
    }

    public static void tapViewWithID(@IdRes int aViewResID) {
        onView(withId(aViewResID)).perform(click());
    }

    /**********************
     * RECYCLERVIEW STUFF *
     **********************/
    public static Matcher<View> withRecyclerView(@IdRes int viewId) {
        return allOf(isAssignableFrom(RecyclerView.class), withId(viewId));
    }

    //Modified from https://gist.github.com/tommyd3mdi/2622caecc1b2d498cd1a

    /**
     * Allows performing actions on a RecyclerView item with a given title. Mostly useful for
     * situations where all titles are guaranteed to be unique
     * @param aParentRecyclerViewID     The resource ID of the RecyclerView
     * @param aRecyclerViewTextViewID   The resource ID of the text view where the title should be displayed
     * @param title                     The title which should be displayed, as a string
     * @return A ViewInteraction where actions will be performed on a row matching the given parameters.
     */
    public static ViewInteraction onRecyclerItemViewWithTitle(@IdRes int aParentRecyclerViewID,
                                                              @IdRes int aRecyclerViewTextViewID,
                                                              String title) {

        Matcher<View> hasRecyclerViewAsParent = withParent(withRecyclerView(aParentRecyclerViewID));
        Matcher<View> hasChildWithTitleInTextView = withChild(allOf(withId(aRecyclerViewTextViewID), withText(title)));
        Matcher<View> hasChildWithChildWithTitleInTextView = withChild(hasChildWithTitleInTextView);

        return onView(allOf(hasRecyclerViewAsParent,
                hasChildWithChildWithTitleInTextView));

    }

    private static Matcher<View> nthChildOf(final Matcher<View> parentMatcher, final int childPosition) {
        return new TypeSafeMatcher<View>() {
            @Override
            public void describeTo(Description description) {
                description.appendText("with " + childPosition + " child view of type parentMatcher");
            }

            @Override
            public boolean matchesSafely(View view) {
                if (!(view.getParent() instanceof ViewGroup)) {
                    return parentMatcher.matches(view.getParent());
                }

                ViewGroup group = (ViewGroup) view.getParent();
                return parentMatcher.matches(view.getParent()) && group.getChildAt(childPosition).equals(view);
            }
        };
    }

    /**
     * Creates a view interaction on a RecylerView's child at a given position.
     * @param aParentRecyclerViewID The resource ID of the parent recycler view
     * @param aPosition             The index of the subview you wish to examine (ie, the row)
     * @return A ViewInteraction where actions will be performed on the row's parent view at the
     *         given index.
     */
    public static ViewInteraction onRecyclerItemViewAtPosition(@IdRes int aParentRecyclerViewID,
                                                               int aPosition) {


        return onView(nthChildOf(withRecyclerView(aParentRecyclerViewID), aPosition));
    }
}