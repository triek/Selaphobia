package entities;

import java.awt.Graphics;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import gamestates.Playing;
import utilz.LoadSave;
import static utilz.Constants.OrbConstants.*;

public class OrbManager {
    private Playing playing;
    private BufferedImage[][] boostArr, reverseArr;

    private ArrayList<Boost> boosts = new ArrayList<>();
    private ArrayList<Reverse> reverses = new ArrayList<>();


    public OrbManager(Playing playing) {
        this.playing = playing;
        loadOrbImgs();
        addOrbs();


    }

    private void addOrbs() {
        boosts = LoadSave.GetBoost();
        System.out.println("Size of boosts: " + boosts.size());
        reverses = LoadSave.GetReverse();
        System.out.println("Size of reverses: " + reverses.size());
    }

    public void update(int[][] lvlData) {
        for (Boost c : boosts)
            c.update(lvlData);
        for (Reverse r : reverses)
            r.update(lvlData);
    }

    public void draw(Graphics g, int xLvlOffset) {
        drawBoosts(g, xLvlOffset);
        drawReverses(g, xLvlOffset);
    }

    private void drawBoosts(Graphics g, int xLvlOffset) {
        for (Boost c : boosts) {
            g.drawImage(boostArr[c.getOrbState()][c.getAniIndex()], (int) c.getHitbox().x - xLvlOffset - 14, (int) c.getHitbox().y - 14, BOOST_WIDTH, BOOST_HEIGHT, null);
//            Draw hitbox
//            c.drawHitbox(g, xLvlOffset);
        }
    }
    private void drawReverses(Graphics g, int xLvlOffset) {
        for (Reverse r : reverses) {
            g.drawImage(reverseArr[r.getOrbState()][r.getAniIndex()], (int) r.getHitbox().x - xLvlOffset - 14, (int) r.getHitbox().y - 14, BOOST_WIDTH, BOOST_HEIGHT, null);
//            Draw hitbox
//            r.drawHitbox(g, xLvlOffset);
        }


    }

    public void checkBoostHit(Rectangle2D.Float attackBox) {
        for (Boost c : boosts)
            if (attackBox.intersects(c.getHitbox())) {
                c.hit();
                return;
            }
    }

    public void checkReverseHit(Rectangle2D.Float attackBox) {
        for (Reverse r : reverses)
            if (attackBox.intersects(r.getHitbox())) {
                r.hit();
                return;
            }
    }

    public boolean isBoostHit(Rectangle2D.Float attackBox) {
        for (Boost c : boosts) {
            if (attackBox.intersects(c.getHitbox())) {
                return true;
            }
        }
        return false;
    }

    public boolean isReverseHit(Rectangle2D.Float attackBox) {
        for (Reverse r : reverses) {
            if (attackBox.intersects(r.getHitbox())) {
                return true;
            }
        }
        return false;
    }

    private void loadOrbImgs() {
        boostArr = new BufferedImage[2][4];
        BufferedImage tempb = LoadSave.GetSpriteAtlas(LoadSave.BOOST_SPRITE);
        for (int j = 0; j < boostArr.length; j++)
            for (int i = 0; i < boostArr[j].length; i++)
                boostArr[j][i] = tempb.getSubimage(i * BOOST_WIDTH_DEFAULT, j * BOOST_HEIGHT_DEFAULT, BOOST_WIDTH_DEFAULT, BOOST_HEIGHT_DEFAULT);

        reverseArr = new BufferedImage[2][4];
        BufferedImage tempr = LoadSave.GetSpriteAtlas(LoadSave.REVERSE_SPRITE);
        for (int j = 0; j < reverseArr.length; j++)
            for (int i = 0; i < reverseArr[j].length; i++)
                reverseArr[j][i] = tempr.getSubimage(i * BOOST_WIDTH_DEFAULT, j * BOOST_HEIGHT_DEFAULT, BOOST_WIDTH_DEFAULT, BOOST_HEIGHT_DEFAULT);
    }


    public void resetAllOrbs() {
        for (Boost c : boosts)
            c.resetOrb();
        for (Reverse r : reverses)
            r.resetOrb();
    }
}
