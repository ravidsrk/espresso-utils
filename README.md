# Espresso-Utils
Collection of handy espresso utils collected over a period of time.

## EditText Utils

```java
/*
/* Use this method to find the EditText within the TextInputLayout. Useful for typing into the TextInputLayout
*/
public static ViewInteraction onEditTextWithinTilWithId(@IdRes int textInputLayoutId) {
    //Note, if you have specified an ID for the EditText that you place inside
    //the TextInputLayout, use that instead - i.e, onView(withId(R.id.my_edit_text));
    return onView(allOf(isDescendantOfA(withId(textInputLayoutId)), isAssignableFrom(EditText.class)));
}

```