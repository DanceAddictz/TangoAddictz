package com.danceaddictz.danceaddictz.utils;

import android.app.Activity;
import android.support.design.widget.TextInputLayout;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Shwetha on 12/1/2015.
 */
public class ValidateUtils {
    private static final String EMAIL_PATTERN = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
            + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
    private static final String MOBILE_PATTERN = "^(?:0091|\\+91|0|)[7-9][0-9]{9}$";
    private static final String NAME_PATTERN = "^.*[a-zA-Z]+.*";
    private static final String TAG = ValidateUtils.class.getSimpleName();

    public static boolean checkMobileNoFormat(String mobileNo) {
        String mobile = mobileNo.replace(" ", "");
        mobile = mobile.replace("-", "");
        Pattern mobileNoPattern = Pattern.compile(MOBILE_PATTERN);
        Matcher mat = mobileNoPattern.matcher(mobile);
        Log.d(TAG, "Mobile Number" + mobileNo + " Validation result :" + mat.matches());
        return mat.matches();
    }

    public static boolean checkIsName(String name) {
        Pattern mobileNoPattern = Pattern.compile(NAME_PATTERN);
        Matcher mat = mobileNoPattern.matcher(name);
        return mat.matches();
    }

    public static boolean checkIsPassword(String password) {
        return !(password.trim().isEmpty() || !(password.length() >= 8));
    }

    private static void requestFocus(View view, Activity activity) {
        if (view.requestFocus()) {
            activity.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        }
    }

    public static boolean validateEditText(Activity activity, TextInputLayout textInputLayout, String errorMessage, Validate validate, EditText extra) {
        if (!validateResult(validate, textInputLayout.getEditText().getText().toString(), extra)) {
            textInputLayout.setError(errorMessage);
            //requestFocus(textInputLayout.getEditText(), activity);
            return false;
        } else {
            textInputLayout.setErrorEnabled(false);
        }
        return true;
    }

    public static boolean validateEditText(Activity activity, TextInputLayout textInputLayout, String errorMessage) {
        return validateEditText(activity, textInputLayout, errorMessage, true);
    }

    public static boolean validateEditText(Activity activity, TextInputLayout textInputLayout, String errorMessage, boolean requestFocus) {
        textInputLayout.setError(errorMessage);
        // if (requestFocus) requestFocus(textInputLayout.getEditText(), activity);
        return false;
    }

    public static boolean validateResult(Validate validate, String validateText, EditText extra) {
        switch (validate){
            case PASSWORD:
                return checkIsPassword(validateText);
            case NAME:
                return checkIsName(validateText);
            case MOBILE:
                return checkMobileNoFormat(validateText);
            case EMAIL:
                return checkEmail(validateText);
            case PASSWORD_CONFIRM:
                return validateText.equals(extra.getText().toString());
        }
        return false;
    }

    public static boolean validateEditText(Activity activity, TextInputLayout textInputLayout, String errorMessage, Validate validate) {
        return validateEditText(activity, textInputLayout, errorMessage, validate, textInputLayout.getEditText());
    }

    public static boolean checkEmail(String s) {
        return s.matches(EMAIL_PATTERN);
    }

    public enum Validate {
        EMAIL,
        MOBILE,
        NAME,
        PASSWORD_CONFIRM,
        PASSWORD
    }
}
