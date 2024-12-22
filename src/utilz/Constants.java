package utilz;

import main.Game;

public class Constants {
    public static class OrbConstants {
        public static final int BOOST = 0;
        public static final int REVERSE = 50;

        public static final int IDLE = 0;
        public static final int HIT = 1;

        public static final int BOOST_WIDTH_DEFAULT = 300;
        public static final int BOOST_HEIGHT_DEFAULT = 300;

        public static final int BOOST_WIDTH = (int) (BOOST_WIDTH_DEFAULT / 6.0 * Game.SCALE);
        public static final int BOOST_HEIGHT = (int) (BOOST_HEIGHT_DEFAULT / 6.0 * Game.SCALE);

        public static final int BOOST_DRAWOFFSET_X = (int) (26 * Game.SCALE);
        public static final int BOOST_DRAWOFFSET_Y = (int) (9 * Game.SCALE);

        public static int GetSpriteAmount(int orb_type, int orb_state) {

            switch (orb_type) {
                case BOOST:
                    switch (orb_state) {
                        case IDLE, HIT:
                            return 4;

                    }
            }
            return 0;

        }
    }

    public static class Environment {
        public static final int BIG_CLOUD_WIDTH_DEFAULT = 1248;
        public static final int BIG_CLOUD_HEIGHT_DEFAULT = 672;
        public static final int SMALL_CLOUD_WIDTH_DEFAULT = 1248;
        public static final int SMALL_CLOUD_HEIGHT_DEFAULT = 672;

        public static final int BIG_CLOUD_WIDTH = (int) (BIG_CLOUD_WIDTH_DEFAULT * Game.SCALE);
        public static final int BIG_CLOUD_HEIGHT = (int) (BIG_CLOUD_HEIGHT_DEFAULT * Game.SCALE);
        public static final int SMALL_CLOUD_WIDTH = (int) (SMALL_CLOUD_WIDTH_DEFAULT * Game.SCALE);
        public static final int SMALL_CLOUD_HEIGHT = (int) (SMALL_CLOUD_HEIGHT_DEFAULT * Game.SCALE);
    }

    public static class UI {
        public static class Buttons {
            public static final int B_WIDTH_DEFAULT = 140;
            public static final int B_HEIGHT_DEFAULT = 56;
            public static final int B_WIDTH = (int) (B_WIDTH_DEFAULT * Game.SCALE);
            public static final int B_HEIGHT = (int) (B_HEIGHT_DEFAULT * Game.SCALE);
        }
        public static class PauseButtons {
            public static final int SOUND_SIZE_DEFAULT = 42;
            public static final int SOUND_SIZE = (int) (SOUND_SIZE_DEFAULT * Game.SCALE);
        }

        public static class URMButtons {
            public static final int URM_DEFAULT_SIZE = 56;
            public static final int URM_SIZE = (int) (URM_DEFAULT_SIZE * Game.SCALE);
        }

        public static class VolumeButtons {
            public static final int VOLUME_DEFAULT_WIDTH = 28;
            public static final int VOLUME_DEFAULT_HEIGHT = 44;
            public static final int SLIDER_DEFAULT_WIDTH = 215;

            public static final int VOLUME_WIDTH = (int) (VOLUME_DEFAULT_WIDTH * Game.SCALE);
            public static final int VOLUME_HEIGHT = (int) (VOLUME_DEFAULT_HEIGHT * Game.SCALE);
            public static final int SLIDER_WIDTH = (int) (SLIDER_DEFAULT_WIDTH * Game.SCALE);
        }
    }

    public static class Directions {
        public static final int LEFT = 0;
        public static final int UP = 1;
        public static final int RIGHT = 2;
        public static final int DOWN = 3;
    }

    public static class PlayerConstants {
        public static final int IDLE = 0;
        public static final int RUNNING = 1;
        public static final int JUMPING = 2;
        public static final int DASHING = 3;
        public static final int FALLING = 4;
        public static final int DYING = 5;

        public static int GetSpriteAmount(int player_action) {

            switch (player_action) {
                case IDLE:
                    return 1;
                case RUNNING:
                    return 6;
                case JUMPING:
                    return 9;
                case DASHING:
//                    return 6;
                case FALLING:
                    return 6;
                case DYING:
                    return 9;
                default:
                    return 1;
            }

        }
    }

}
