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

    // LaunchScreen.java
    public static final int NB_DAYS_BEFORE_DOWNLOAD_MODEL = 7;

    // MainActivity.java
    public static final String IS_ACTIVITY_RECREATED = "isActivityRecreated";
    public static final String UPDATE_THEME_EXTRA = "updateThemeExtra";

    // ArticleActivity.java
    public static final String TEXT_PLAIN = "text/plain";
    public static final String URI_ARTICLE = "https://informationspositives?id_article=";
    public static final String DATA_ARTICLE = "id_article";
    public static final String ARTICLE_INFORMATION = "articleInformation";
    public static final String RECOMMENDED_ARTICLES = "recommendedArticles";

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

    // StoryFragment.java
    public static final int MAX_STORY_TITLE_SIZE = 100;

    // ArticleInStory.java
    public static final String ARTICLES_IN_STORY = "articlesInStory";
    public static final String CURRENT_STORY_INDEX = "currentIndexArticleInStory";
    public static final float SWIPE_MAX_PERCENT = 0.5f;
    public static final int NEXT_STORY_VALUE = 1;
    public static final int PREVIOUS_STORY_VALUE = -1;
    public static final int LAST_INDEX_BOUND = 1;
    public static final int INDEX_FIRST_STORY = 0;

    // CategoryActivity.java
    public static final String CURRENT_CATEGORY = "current_category";
    public static final float NO_ELEVATION = 0;
    public static final float ELEVATION_TEN = 10;
    public static final int NO_BACKGROUND_RESOURCE = 0;

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
    public static final String MODEL_FILE_NAME = "sentiment_analysis";
    public static final int INDEX_OF_FAILURE = -1;

    // SearchActivity.java
    public static final String DATE_FORMAT_FILTER = "dd/MM/yyyy";
    public static final int LENGTH_EMPTY_KEYWORD_SEARCH = 0;
    public static final int SIZE_EMPTY_LIST = 0;
    public static final int MIN_SIZE = 0;

    // SearchResultsFragment.java
    public static final String AND_SUFFIX = " AND (";
    public static final String OR_SUFFIX = " OR ";
    public static final String PARENTHESIS_REGEX = ")";
    public static final String DOUBLE_QUOTE_REGEX = "\"";
    public static final long DELAY_BEFORE_SET_FEED_ADAPTER = 500;
    public static final int DEFAULT_VALUE_CATEGORIES_ADDED = 0;
    public static final int ADD_CATEGORY = 1;

    // TextRecognition.java
    public static final String NEGATIVE_CATEGORY = "negative";
    public static final float MAX_NEGATIVE_SCORE = 0.4f;

    // NewsApi.java
    public static final String BASE_URL = " https://newsapi.org";
    public static final String NEWS_API_TOKEN = "dd641eb4db424a0b9dcfbeb918dc94e1";
    public static final long MILLIS_IN_A_DAY = (long) 1000 * 60 * 60 * 24;
    public static final int NO_ARTICLE = 0;
    public static final int DEFAULT_PAGINATION_VALUE = 1;
    public static final int NEXT_PAGE = 1;
    public static final int FAILURE_ITERATION_INIT_VALUE = 0;
    public static final int FAILURE_VALUE = 1;
    public static final int MAX_FAILURE_ITERATION = 2;

    // NetworkErrorFragment.java
    public static final String NETWORK_ERROR_CAUSE = "networkErrorCause";
    public static final String IS_RELOAD_DATA_ENABLED = "isReloadDataEnabled";
    public static final int NETWORK_ERR_ANIM_MAX_FRAME = 40;
    public static final int NETWORK_ERR_ANIM_REVERSE_FRAME = 110;
    public static final int NETWORK_ERR_ANIM_REVERSE_MAX_FRAME = 150;
    public static final int ANIM_NO_REPEAT = 0;

    // FeedFragment.java
    public static final int TOP_SCROLL_POSITION = 0;
    public static final int NO_ELEMENT_ADDED = 0;
    public static final int INITIAL_VALUE_NB_ELEMENTS_ADDED = 0;
    public static final int ADD_NEW_ELEMENT = 1;
    public static final int DELAY_BEFORE_RELOAD_FEED_ARTICLES = 200;

    // ArticlesFragment.java
    public static final int DIRECTION_SCROLL_VERTICALLY = 1;

    // AdapterUtils.java
    public static final String SUSPENSION_POINTS = "...";
    public static final String OPENING_BRACKET = "[";
    public static final String ENDING_BRACKET = "]";
    public static final int NB_RECOMMENDED_ARTICLES = 4;

     // DateUtils.java
    public static final String DEFAULT_TIMESTAMP_FORMAT = "yyyy-MM-dd HH:mm:ss.SSS";
    public static final String NEWS_SEARCHES_DATE_FORMAT = "yyyy-MM-dd";
    public static final String NEWS_RESULTS_DATE_FORMAT = "yyyy-MM-dd'T'HH:mm:ss'Z'";
    public static final String ARTICLE_DATE_FORMAT = "HH:mm:ss-dd/MM/yyyy";
}
