package entities;

import static utilz.Constants.PlayerConstants.*;
import static utilz.HelpMethods.*;
import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;

import gamestates.Playing;
import main.Game;
import utilz.LoadSave;

public class Player extends Entity {
    private BufferedImage[][] animations;
    private int aniTick, aniIndex, aniSpeed = 10;
    private int playerAction = IDLE;
    private boolean moving = false, attacking = false, dying = false;
    private boolean left, right, jump, grav;
    private float playerSpeed = 1 * Game.SCALE;
    private int[][] lvlData;
    private float xDrawOffset = 11 * Game.SCALE;
    private float yDrawOffset = 11 * Game.SCALE;

//    Jumping / Gravity
    private float airSpeed = 0f;
    private float gravity = 0.04f * Game.SCALE;
    private float jumpSpeed = -2.3f * Game.SCALE;
    private float fallSpeedAfterCollision = 0.5f * Game.SCALE;
    private boolean inAir = false;
    private boolean reverseGrav = false;


    // AttackBox
    private Rectangle2D.Float attackBox;
    private int flipX = 0;
    private int flipW = 1;

    private int state;
    private boolean attackChecked;
    private Playing playing;
    private OrbManager orbManager;

    public Player(float x, float y, int width, int height, Playing playing, OrbManager orbManager) {
        super(x, y, width, height);
        this.playing = playing;
        this.orbManager = orbManager;
        loadAnimations();
        initHitbox(x * Game.SCALE, y * Game.SCALE, (int) (26 * Game.SCALE), (int) (30 * Game.SCALE));
        initAttackBox();
    }

    private void initAttackBox() {
        attackBox = new Rectangle2D.Float(x * Game.SCALE, y * Game.SCALE, (int) (26 * Game.SCALE), (int) (30 * Game.SCALE));
    }

    public void update() {
        if (dying) {
            if (playerAction != DYING) {
                playerAction = DYING;
                aniTick = 0;
                aniIndex = 0;
                playing.setPlayerDying(true);
                System.out.println("Died");
            } else if (aniIndex == GetSpriteAmount(DYING) - 1 && aniTick >= aniSpeed - 1) {
                playing.setGameOver(true);
            } else {
                updateAnimationTick();
            }
            return;
        }

        updateAttackBox();
        updatePos();

        if (attacking) {
            checkBoostAttack();
            checkReverseAttack();
        }

        updateAnimationTick();
        setAnimation();

    }

    private void checkBoostAttack() {
        if (attackChecked || aniIndex != 1)
            return;
        attackChecked = true;
        playing.checkBoostHit(attackBox);
        boolean isHit = orbManager.isBoostHit(attackBox);
        if (isHit) {
            jump();
        }
    }

    private void checkReverseAttack() {
        if (attackChecked || aniIndex != 1)
            return;
        attackChecked = true;
        playing.checkReverseHit(attackBox);
        boolean isHit = orbManager.isReverseHit(attackBox);
        if (isHit) {
            changeGrav();
        }
    }

    private void updateAttackBox() {
        attackBox.x = hitbox.x;
        attackBox.y = hitbox.y;

    }

    public void render(Graphics g, int lvlOffset) {
        g.drawImage(animations[playerAction][aniIndex], (int) (hitbox.x - xDrawOffset) - lvlOffset + flipX, (int) (hitbox.y - yDrawOffset), width * flipW, height, null);
//        Show hit box
//        drawHitbox(g, lvlOffset);
//        drawAttackBox(g, lvlOffset);

    }

    private void drawAttackBox(Graphics g, int lvlOffsetX) {
        g.setColor(Color.red);
        g.drawRect((int) attackBox.x - lvlOffsetX, (int) attackBox.y, (int) attackBox.width, (int) attackBox.height);

    }

    private void updateAnimationTick() {
        aniTick++;
        if (aniTick >= aniSpeed) {
            aniTick = 0;
            aniIndex++;
            if (aniIndex >= GetSpriteAmount(playerAction)) {
                aniIndex = 0;
                attacking = false;
                attackChecked = false;
                jump = false;
            }
        }
    }

    private void setAnimation() {
        int startAni = playerAction;

        if (moving)
            playerAction = RUNNING;
        else
            playerAction = IDLE;

        if(inAir) {
//            if(airSpeed < 0)
                playerAction = JUMPING;
//            else playerAction = FALLING;
        }

        if (attacking)
            playerAction = JUMPING;

        if (dying)
            playerAction = DYING;

        if (startAni != playerAction)
            resetAniTick();
    }

    private void resetAniTick() {
        aniTick = 0;
        aniIndex = 0;
    }

    private void updatePos() {
        moving = true;
        setRight(true);

        if(jump)
            jump();

        if (!inAir)
            if (!left && !right || (right && left))
                return;

        float xSpeed = playerSpeed;
        flipX = 0; flipW = 1;
        if (!inAir) {
            if (!IsEntityOnFloor(hitbox, lvlData)) {
                inAir = true;
            }
        }

        //FREE MOVING MODE
//        float xSpeed = 0;
//        if (left) {
//            xSpeed -= playerSpeed;
//            flipX = width;
//            flipW = -1;
//        }
//        if (right) {
//            xSpeed += playerSpeed;
//            flipX = 0;
//            flipW = 1;
//        }

        if(!inAir)
            if(!IsEntityOnFloor(hitbox, lvlData))
                inAir = true;

        if(inAir) {
            if(CanMoveHere(hitbox.x, hitbox.y + airSpeed, hitbox.width, hitbox.height, lvlData)) {
                hitbox.y += airSpeed;
                airSpeed += gravity;
                updateXPos(xSpeed);
            } else {
                hitbox.y = GetEntityYPosUnderRoofOrAboveFloor(hitbox, airSpeed);
                if((gravity > 0 && airSpeed > 0) || (gravity < 0 && airSpeed < 0))
                    resetInAir();
                else
                    airSpeed = fallSpeedAfterCollision;
                updateXPos((xSpeed));

            }
        } else
            updateXPos(xSpeed);

    }

    private void jump() {
        boolean isBoostHit = orbManager.isBoostHit(attackBox);
        //Check if in air and not hit orb
        if (inAir && !isBoostHit) {
            return;
        }
        inAir = true;
        airSpeed = jumpSpeed;

    }

    private void changeGrav() {
        boolean isReverseHit = orbManager.isReverseHit(attackBox);
        if (isReverseHit) {
            reverseGrav = !reverseGrav;
            gravity = -gravity; // Toggle gravity direction
            jumpSpeed = -jumpSpeed; // Toggle jump direction
            fallSpeedAfterCollision = -fallSpeedAfterCollision; // Toggle fall speed direction
            airSpeed = 0; // Reset air speed to avoid sudden jumps
        }
    }


    private void resetInAir() {
        inAir = false;
        reverseGrav = false;
        airSpeed = 0;

    }

    private void updateXPos(float xSpeed) {
        if(CanMoveHere(hitbox.x + xSpeed, hitbox.y, hitbox.width, hitbox.height, lvlData)) {
            hitbox.x += xSpeed;

        } else
            dying = true;
    }


    private void loadAnimations() {
            BufferedImage img = LoadSave.GetSpriteAtlas(LoadSave.PLAYER_ATLAS);

            animations = new BufferedImage[6][9];
            for (int j = 0; j < animations.length; j++)
                for (int i = 0; i < animations[j].length; i++)
                    animations[j][i] = img.getSubimage((i+1)*135, (j+1)*135, 135, 135);

    }

    public void loadLvlData(int[][] lvlData) {
        this.lvlData = lvlData;
        if(!IsEntityOnFloor(hitbox, lvlData))
            inAir = true;
    }

    public void resetDirBooleans() {
        left = false;
        right = false;
    }

    public void setAttacking (boolean attacking) {
        this.attacking = attacking;
    }

    public boolean isLeft() {
        return left;
    }

    public void setLeft(boolean left) {
        this.left = left;
    }

    public boolean isRight() {
        return right;
    }

    public void setRight(boolean right) {
        this.right = right;
    }

    public void setJump(boolean jump) {
        this.jump = jump;
    }

    public void setChangeGravity(boolean grav) {
        this.grav = grav;
    }

    public void resetAll() {
        resetDirBooleans();
        inAir = false;
        attacking = false;
        moving = false;
        playerAction = IDLE;
        dying = false;

        hitbox.x = x;
        hitbox.y = y;

        if (!IsEntityOnFloor(hitbox, lvlData))
            inAir = true;
    }
}
