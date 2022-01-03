package com.aga.woodentangrampuzzle2.common;

import android.graphics.RectF;
import android.graphics.Typeface;

/**
 *
 * Created by Andrii Husiev on 09.11.2016.
 *
 */

public class TangramGlobalConstants {

    //<editor-fold desc="Load Screen">
    public static final float LOADSCREEN_TEXT_OFFSET_FROM_TOP = 0.15f;
    public static final float LOADSCREEN_TEXT_HEIGHT = 0.12f;
    //</editor-fold>

    //<editor-fold desc="Main Menu">
    public static final float MM_TITLE_OFFSET_FROM_TOP = 0.15f; // percents of screen height
    public static final float MM_TITLE_HEIGHT = 0.12f;
    public static final float MM_BUTTON_OFFSET_FROM_TOP = 0.407f;
    public static final float MM_BUTTON_HEIGHT = 0.15f;
    public static final float MM_BUTTON_TEXT_HEIGHT = 0.66f; //0.1f; - предыдущее было % от экрана, а теперь от кнопки
    public static final float MM_BUTTON_GAP = 0.05f;
    public static final float MM_VERSION_TEXT_HEIGHT = 0.03f;
    public static final float MM_VERSION_TEXT_OFFSET = 0.01f;
    //</editor-fold>

    //<editor-fold desc="Levels Set Selection Menu">
    public static final float LSS_TITLE_OFFSET_FROM_TOP = 0.04f;
    public static final float LSS_TITLE_HEIGHT = 0.085f;
    public static final float LSS_OFFSET_FROM_TOP_DC = 0.6f; // DC means "Device Coordinates"
    public static final float LSS_GRADIENT_HEADER_OFFSET_FROM_TOP = 0.14f; //-0.206f;
    public static final float LSS_GRADIENT_HEADER_WIDTH = 1.277f;
    public static final float LSS_GRADIENT_HEADER_HEIGHT = 0.06f;
    public static final float LSS_BUTTON_OFFSET_FROM_TOP = 0.2f;
    public static final float LSS_BUTTON_HEIGHT = 0.25f;
    public static final float LSS_BUTTON_TEXT_HEIGHT = 0.66f;
    public static final float LSS_BUTTON_GAP = 0.04f;
    public static final float LSS_LOCK_SIZE = 0.71f; // 0.17f;
    //</editor-fold>

    //<editor-fold desc="Levels Selection Menu">
    public static final float LS_TITLE_OFFSET_FROM_TOP = 0.04f;//0.116f;
    public static final float LS_TITLE_HEIGHT = 0.085f;
    public static final float LS_BUTTONS_OFFSET_FROM_TOP = 0.2f;
    public static final float LS_BUTTONS_OFFSET_FROM_TOP_DC  = 0.6f; // DC means "Device Coordinates"
//    public static final float LS_OFFSET_TIMER_FROM_BUTTONS_BOTTOM = 0.104f;//0.053f;
    public static final float LS_BUTTONS_TIMER_TEXT_OFFSET = 0.815f; // 0.195f;
    public static final float LS_BUTTONS_TIMER_TEXT_HEIGHT = 0.145f;//0.031f;
    public static final float LS_GRADIENT_HEADER_OFFSET_FROM_TOP = 0.14f;
    public static final float LS_GRADIENT_HEADER_HEIGHT = 0.06f;
    public static final float LS_BUTTON_WIDTH = 0.25f;
    public static final float LS_BUTTON_GAP = 0.04f;
    public static final float LS_CUP_SIZE = 0.06f; //0.175f; //0.10875f; //0.087f;
    public static final float LS_PREVIEW_BITMAP_SIZE = 0.535f;
    public static final float LS_PREVIEW_PATH_SIZE = 0.94f;
    public static final float LS_PREVIEW_OFFSET_FROM_TOP = 0.03f;
    public static final float LS_LOCK_SIZE = 0.55f;
    public static final float LS_LOCK_OFFSET_FROM_TOP = 0.0385f;
    public static final float LS_LOCK_TEXT_BITMAP_START_SIZE = 0.6f;
    //</editor-fold>

    //<editor-fold desc="Ingame Constants">
    public static final float INGAME_TITLE_OFFSET_FROM_TOP = 0.06f;
    public static final float INGAME_TITLE_HEIGHT = 0.05f;
    public static final float INGAME_HEADER_HEIGHT = 0.06f;
    public static final float INGAME_ANGULAR_HEADER_HEIGHT = 0.09f;
    public static final float INGAME_HEADER_SHADOW_HEIGHT = 0.015f;
    public static final float INGAME_TIMER_HEIGHT = 0.053f;
    public static final float INGAME_TIMER_OFFSET_FROM_TOP = 0.067f;
    public static final float INGAME_TIMER_OFFSET_FROM_LEFT = 0.025f;
    public static final float INGAME_TIMER_DIGITS_GAP = 0.002f;
    public static final float INGAME_LEVEL_PROGRESS_OFFSET_FROM_TOP = 0.04f;
    public static final float INGAME_LEVEL_PROGRESS_OFFSET_FROM_RIGHT = 0.14f;
    public static final float INGAME_CUP_HEIGHT = 0.08f;
    public static final float INGAME_CUP_OFFSET_FROM_RIGHT = -0.023f;
    public static final float INGAME_CUP_BRONZE = 80.0f;
    public static final float INGAME_CUP_SILVER = 95.0f;
    public static final float INGAME_CUP_GOLD = 99.0f;
    public static final float INGAME_TILE_ROTATION_MOVEMENT_THRESHOLD = 0.01f;

    public static final float INGAME_TILE0_OFFSET_X = 0.2f;
    public static final float INGAME_TILE0_OFFSET_Y = 0.2f;
    public static final float INGAME_TILE1_OFFSET_X = 0.17f;
    public static final float INGAME_TILE1_OFFSET_Y = 0.47f;
    public static final float INGAME_TILE2_OFFSET_X = 0.27f;
    public static final float INGAME_TILE2_OFFSET_Y = 0.41f;
    public static final float INGAME_TILE3_OFFSET_X = 0.187f;
    public static final float INGAME_TILE3_OFFSET_Y = 0.79f;
    public static final float INGAME_TILE4_OFFSET_X = 0.317f;
    public static final float INGAME_TILE4_OFFSET_Y = 0.703f;
    public static final float INGAME_TILE5_OFFSET_Y = 0.267f;
    public static final float INGAME_TILE6_OFFSET_Y = 0.689f;
    public static final float INGAME_TILES_OFFSET_FROM_RIGHT = 0.3f;
    //</editor-fold>

    //<editor-fold desc="Fling Constants">
    public static final int FLING_MIN_DISTANCE = 120;
    public static final int FLING_MAX_OFF_PATH = 250;
    public static final int FLING_THRESHOLD_VELOCITY = 200;
    public static final long SCROLLING_ANIMATION_DURATION = 2000;
    //</editor-fold>

    //<editor-fold desc="Mixed Constants">
    public static final float ALL_MENUS_HEADER_SHADOW_TAIL = 0.007f;
    public static final float INSENSITIVE_BACKLASH_ON_SCROLL = 0.002f;
    public static final float SHADOW_LAYER_OFFSET = 2;
    public static final int LEVEL_SET_NUMBER = 4;
    public static final int LEVELS_IN_THE_ROW = 4;
    public static final int LEVELS_NUMBER = 24;
    public static Typeface digitalTF;
    public static final String FONT_DIGITAL = "fonts/digitaldismay.otf";
    public static final float ALL_FONTS_SIZE = 100.0f;
    public static final int NO_CUP = 0;
    public static final int BRONZE_CUP = 1;
    public static final int SILVER_CUP = 2;
    public static final int GOLDEN_CUP = 3;
    public static final int TILES_NUMBER = 7;
    public static final int ZERO = 0;
    public static final int ONE = 1;
    public static final long TIME_GAP = 500; //milliseconds
    //</editor-fold>

    //<editor-fold desc="Texture RectF Constants">
    public static final float TEXTURE_SIZE_XSMALL = 128;
    public static final float TEXTURE_SIZE_SMALL = 256;
    public static final float TEXTURE_SIZE_MEDIUM = 512;
    public static final float TEXTURE_SIZE_LARGE = 1024;
    public static final float TEXTURE_SIZE_XLARGE = 1024;
    //</editor-fold>

    //<editor-fold desc="Colors">
    public static final int COLOR_TEXT_ON_BUTTONS = 0xFFFFD89E;//0xFFFFA216;
    public static final int COLOR_TEXT_INGAME_HEADER = 0xde5B1B00;
    public static final int COLOR_LEVEL_BG = 0x5f07c446;
    public static final int COLOR_GREEN_FILTER = 0x0000ff00;
    public static final int COLOR_RED_FILTER = 0x00ff0000;
    public static final int COLOR_GREEN_BOUND = 0x0000d200;
    public static final int COLOR_RED_BOUND = 0x00dc0000;
    public static final int COLOR_BLACK_BOUND = 0x00202020;
    public static final int COLOR_SHADOW = 0x80242424;
    public static final int COLOR_CLEANUP = 0x00ffffff;
    //</editor-fold>

}
