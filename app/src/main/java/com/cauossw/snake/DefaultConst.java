package com.cauossw.snake;

public class DefaultConst {
    static final int SINGLE_WIDTH = 40;
    static final int DUAL_WIDTH = 40;
    static final int SINGLE_HEIGHT = 40;
    static final int DUAL_HEIGHT = 80;

    static final int SNAKE_LENGTH = 4;
    static final int SNAKE_SPEED = 300;

    static final int SNAKE_DUAL_APPLE_NUM = 2;
    static final int SNAKE_DUAL_1_X = 0;
    static final int SNAKE_DUAL_1_Y = 0;
    static final Direction SNAKE_DUAL_1_DIR = Direction.DOWN;

    static final int SNAKE_DUAL_2_X = DUAL_WIDTH - 1;
    static final int SNAKE_DUAL_2_Y = DUAL_HEIGHT - 1;
    static final Direction SNAKE_DUAL_2_DIR = Direction.UP;

    static final int SNAKE_SINGLE_APPLE_NUM = 2;
    static final int SNAKE_SINGLE_X = SINGLE_WIDTH / 2;
    static final int SNAKE_SINGLE_Y = SINGLE_HEIGHT / 2;
    static final Direction SNAKE_SINGLE_DIR = Direction.UP;

    public static int getWidth(PlayMode mode) {
        if (mode == PlayMode.Dual) return DUAL_WIDTH;
        else return SINGLE_WIDTH;
    }

    public static int getHeight(PlayMode mode) {
        if (mode == PlayMode.Dual) return DUAL_HEIGHT;
        else return SINGLE_HEIGHT;
    }
}
