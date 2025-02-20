package entities;

import main.Game;

import static utilz.Constants.OrbConstants.*;

public class Boost extends Orb {
    public Boost(float x, float y) {
        super(x, y, BOOST_WIDTH, BOOST_HEIGHT, BOOST);
        initHitbox(x, y, (int) (31 * Game.SCALE), (int) (31 * Game.SCALE));
    }

    public void update(int[][] lvlData) {
        updateMove(lvlData);
        updateAnimationTick();
    }

    protected void updateMove(int[][] lvlData) {

    }

    private void updateBehavior(int[][] lvlData, Player player) {
        switch (orbState) {
            case HIT:
                break;
        }
    }
}
