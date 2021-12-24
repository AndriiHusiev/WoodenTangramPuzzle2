package com.aga.woodentangrampuzzle2.opengles20.level;

/**
 *
 * Created by Andrii Husiev on 26.11.2016.
 * Этот класс отвечает за отображение всего окружения выбранного уровня:
 * рамки, заголовка, текста, вспомогательных иконок и кнопок, таймера и
 * контура на фоне, с которым и надо совмещать плитки танграма.
 *
 */

public class TangramGLLevel {
    public TangramGLLevelForeground foreground;
    public TangramGLLevelTiles tiles;
    public TangramGLLevelBackground background;
    public TangramGLLevelCup cup;
    public TangramGLLevelButtons buttons;
    public TangramGLLevelTimer timer;

    public final int selectedLevel;

    public TangramGLLevel(int selectedLevel) {
        this.selectedLevel = selectedLevel;
    }

}
