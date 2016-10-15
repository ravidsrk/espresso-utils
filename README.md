# Espresso-Utils
Collection of handy espresso utils collected over a period of time.

### EditText Utils

```java
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

```

### Screen Rotation

```java
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

```

### Toolbar Utils

```java
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

```

### Text Entry Utils

```java

public static void enterTextIntoViewWithHint(String aTextToEnter, @StringRes int aHintResID) {
    onView(withHint(aHintResID)).perform(typeText(aTextToEnter));
}

public static void enterTextIntoViewWithID(String aTextToEnter, @IdRes int aViewID) {
    onView(withId(aViewID)).perform(typeText(aTextToEnter));
}
```

### Scrolling Utils

```java

public static void scrollToViewWithID(@IdRes int aViewIDRes) {
    onView(withId(aViewIDRes)).perform(scrollTo());
}
```

### Taping Utils

```java

public static void tapViewWithText(String aText) {
    onView(withText(aText)).perform(click());
}

public static void tapViewWithText(@StringRes int aTextResID) {
    onView(withText(aTextResID)).perform(click());
}

public static void tapViewWithID(@IdRes int aViewResID) {
    onView(withId(aViewResID)).perform(click());
}
```