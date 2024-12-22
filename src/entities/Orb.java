package entities;

import static utilz.Constants.OrbConstants.*;

public abstract class Orb extends Entity {
    protected int aniIndex, orbState, orbType;
    protected int aniTick, aniSpeed = 25;
    protected boolean firstUpdate = true;
    protected boolean inAir;

    public Orb(float x, float y, int width, int height, int orbType) {
        super(x, y, width, height);
        this.orbType = orbType;
        initHitbox(x, y, width, height);

    }

    protected void firstUpdateCheck(int[][] lvlData) {

    }

    protected void updateInAir(int[][] lvlData) {

    }

    protected void move(int[][] lvlData) {

    }

    protected void newState(int orbState) {
        this.orbState = orbState;
        aniTick = 0;
        aniIndex = 0;

    }

    public void hit() {
        newState(HIT);
        System.out.println("Perfect!");
    }

    protected void updateAnimationTick() {
        aniTick++;
        if (aniTick >= aniSpeed) {
            aniTick = 0;
            aniIndex++;
            if (aniIndex >= GetSpriteAmount(orbType, orbState)) {
                aniIndex = 0;
                if (orbState == HIT)
                    orbState = IDLE;

            }
        }
    }

    public void resetOrb() {
        hitbox.x = x;
        hitbox.y = y;
        firstUpdate = true;
        newState(IDLE);
    }

    public int getAniIndex() {
        return aniIndex;
    }

    public int getOrbState() {
        return orbState;
    }
}
