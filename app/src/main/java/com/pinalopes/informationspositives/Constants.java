package com.pinalopes.informationspositives;

import com.polyak.iconswitch.IconSwitch;

public class Constants {

    private Constants() {
        throw new AssertionError();
    }

    public static final String DRAWABLE = "drawable";
    public static final String PACKAGE_NAME = "com.pinalopes.informationspositives";
    public static final String FILTERS = "filters";
    public static final String KEY_WORD_SEARCH = "keyWordSearch";
    public static final String BIG_PIC = "_big";
    public static final String BLACK_ICON = "_black";
    public static final float START_LIKE_PROGRESS = 0.05f;
    public static final float END_LIKE_PROGRESS = 0.5f;
    public static final float START_DISLIKE_PROGRESS = 0.55f;
    public static final float END_DISLIKE_PROGRESS = 1.0f;
    public static final int NO_FLAG = 0;
    public static final int TYPE_HEADER = 0;
    public static final int TYPE_ITEM = 1;
    public static final int FIRST_INDEX = 0;
    public static final int NO_ANIM = 0;

    // MainActivity.java
    public static final String IS_ACTIVITY_RECREATED = "isActivityRecreated";
    public static final String UPDATE_THEME_EXTRA = "updateThemeExtra";

    // ArticleActivity.java
    public static final String TEXT_PLAIN = "text/plain";
    public static final String URI_ARTICLE = "https://informationspositives?id_article=";
    public static final String DATA_ARTICLE = "id_article";

    // StoryCountDownTimer.java
    public static final String ANIMATOR_PROPERTY_NAME = "progress";
    public static final int ONE_SECOND_IN_MILLI = 1000;
    public static final int ONE_THOUSAND = 1000;
    public static final int TEN = 10;
    public static final int ONE = 1;
    public static final int END_PROGRESS = 0;
    public static final int TIMER_INITIAL_VALUE = 11000;
    public static final int COUNT_DOWN_INTERVAL = 1000;

    // StoryHeaderService.java
    public static final float INITIAL_Y_VALUE = 0.0f;
    public static final int INITIAL_VIEW_POSITION = 0;
    public static final int DURATION_VIEW_ANIMATION = 300;

    // ArticleInStory.java
    public static final float SWIPE_MAX_PERCENT = 0.5f;

    // CategoryArticle.java
    public static final String CURRENT_CATEGORY = "current_category";
    public static final int NO_BACKGROUND_RESOURCE = 0;
    public static final float NO_ELEVATION = 0;
    public static final float ELEVATION_TEN = 10;

    // TopCategoriesAdapter.java
    public static final int MAX_CATEGORIES_SELECTED = 3;

    // SettingsFragment.java
    public static final String NIGHT_MODE_ALERT_DIALOG = "night_mode_dialog_alert";
    public static final String ABOUT_US_ALERT_DIALOG = "about_us_dialog_alert";
    public static final String PLAIN_TEXT = "plain/text";
    public static final int ITEM_NOTIFICATIONS_INDEX = 1;
    public static final int ITEM_NIGHT_MODE_INDEX = 2;
    public static final int ITEM_CONTACT_US_INDEX = 3;
    public static final int ITEM_OTHER_APPS_INDEX = 4;
    public static final int ITEM_ABOUT_US_INDEX = 5;

    // NightModeActivity.java
    public static final int NIGHT_MODE_INITIAL_FRAME = 0;
    public static final int NIGHT_MODE_MAX_FRAME = 90;
    public static final int DAY_MODE_INITIAL_FRAME = 150;
    public static final int DAY_MODE_MAX_FRAME = 240;
    public static final IconSwitch.Checked NIGHT_MODE = IconSwitch.Checked.LEFT;
    public static final IconSwitch.Checked DAY_MODE = IconSwitch.Checked.RIGHT;

    // AboutUsDialogFragment.java
    public static final String PREFIX_HEADER_IMAGE = "smile_";
    public static final int TOTAL_IMAGE_HEADER_ABOUT_US = 12;

    // DataStorage.java
    public static final int INDEX_OF_FAILURE = -1;
}
