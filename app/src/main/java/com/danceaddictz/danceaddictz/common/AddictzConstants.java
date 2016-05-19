package com.danceaddictz.danceaddictz.common;

/**
 * Created by abhrte on 14/2/16.
 */
public class AddictzConstants {
    public static final String AUTH_KEY = "AUTHKEY_KEY";

    public static final String LOGIN_TYPE = "LOGIN_TYPE_KEY";
    public static final String ID = "ID_KEY";
    public static final String REST_KEY = "REST_KEY";
    public static final String FACEBOOK_ID = "FACEBOOK_ID_KEY";
    public static final String GOOGLE_ID = "GOOGLE_ID";
    public static final String EMAIL = "EMAIL_KEY";
    public static final String MOBILE = "MOBILE_KEY";
    public static final String PHOTO_URL = "PHOTO_URL_KEY";
    public static final String FIRST_NAME = "FIRST_NAME_KEY";
    public static final String LAST_NAME = "LAST_NAME_KEY";
    public static final String GENDER = "GENDER_KEY";

    public enum LOGIN_TYPES{GOOGLE(0), FB(1), ADDICTZ(2);
        private int id;

        LOGIN_TYPES(int id) {
            this.id = id;
        }

        public int getId() {
            return id;
        }
    };

    public static final String NAME = "NAME";
    public static final String USER_NAME = "USER_NAME";
    public static final String AGE = "AGE";
    public static final String SET_DANCE_FORMS = "SET_DANCE_FORMS";
}
