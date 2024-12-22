package gamestates;

import entities.OrbManager;
import entities.Player;
import levels.LevelManager;
import main.Game;
import ui.GameOverOverlay;
import ui.PauseOverlay;
import utilz.LoadSave;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.util.Random;
import static utilz.Constants.Environment.*;

public class Playing extends State implements Statemethods{
    private Player player;
    private LevelManager levelManager;
    private OrbManager orbManager;
    private PauseOverlay pauseOverlay;
    private GameOverOverlay gameOverOverlay;
    private boolean paused = false;

    private int xLvlOffset;
    private int leftBorder = (int) (0.5 * Game.GAME_WIDTH);
    private int rightBorder = (int) (0.5 * Game.GAME_WIDTH);
    private int lvlTilesWide = LoadSave.GetLevelData()[0].length;
    private int maxTilesOffset = lvlTilesWide - Game.TILES_IN_WIDTH;
    private int maxLvlOffsetX = maxTilesOffset * Game.TILES_SIZE;

    private BufferedImage backgroundImg, bigCloud, smallCloud;
    private int[] smallCloudsPos;
    private Random rnd = new Random();

    private boolean gameOver;
    private boolean playerDying;

    public Playing(Game game) {
        super(game);
        if (paused) System.out.println("Paused");
        initClasses();

        backgroundImg = LoadSave.GetSpriteAtlas(LoadSave.PLAYING_BG_IMG);
        bigCloud = LoadSave.GetSpriteAtlas(LoadSave.BIG_CLOUDS);
        smallCloud = LoadSave.GetSpriteAtlas(LoadSave.SMALL_CLOUDS);
        smallCloudsPos = new int[8];
        for (int i = 0; i < smallCloudsPos.length; i++)
            smallCloudsPos[i] = (int) (90 * Game.SCALE) + rnd.nextInt((int) (100 * Game.SCALE));
    }

    private void initClasses() {
        levelManager = new LevelManager(game);
        orbManager = new OrbManager(this);
        player = new Player(100, 200, (int) (49 * Game.SCALE), (int) (49 * Game.SCALE), this, orbManager);
        OrbManager orbManager = new OrbManager(this);
        player.loadLvlData(levelManager.getCurrentLevel().getLevelData());
        pauseOverlay = new PauseOverlay(this);
        gameOverOverlay = new GameOverOverlay(this);

    }

    @Override
    public void update() {
        if (paused)
            pauseOverlay.update();
        else if (gameOver)
            gameOverOverlay.update();
        else if (playerDying)
            player.update();
        if (!paused) {
            levelManager.update();
            player.update();
            orbManager.update(levelManager.getCurrentLevel().getLevelData());
            checkCloseToBorder();
        } else {
            pauseOverlay.update();
        }
    }

    private void checkCloseToBorder() {
        int playerX = (int) player.getHitbox().x;
        int diff = playerX - xLvlOffset;

        if (diff > rightBorder)
            xLvlOffset += diff - rightBorder;
        else if(diff < leftBorder)
            xLvlOffset += diff - leftBorder;
        
        if (xLvlOffset > maxLvlOffsetX)
            xLvlOffset = maxLvlOffsetX;
        else if (xLvlOffset < 0)
            xLvlOffset = 0;
    }

    @Override
    public void draw(Graphics g) {
        g.drawImage(backgroundImg, 0, 0, Game.GAME_WIDTH, Game.GAME_HEIGHT, null);
        drawClouds(g);

        levelManager.draw(g, xLvlOffset);
        player.render(g, xLvlOffset);
        orbManager.draw(g, xLvlOffset);

        if (paused) {
            g.setColor(new Color(0, 0, 0, 100));
            g.fillRect(0, 0, Game.GAME_WIDTH, Game.GAME_HEIGHT);
            pauseOverlay.draw(g);
        } else if (gameOver) {
            gameOverOverlay.draw(g);
        }
    }

    private void drawClouds(Graphics g) {
        for (int i = 0; i < 5; i++)
            g.drawImage(bigCloud, i * BIG_CLOUD_WIDTH - (int) (xLvlOffset * 0.2), 0, BIG_CLOUD_WIDTH, BIG_CLOUD_HEIGHT, null);

        for (int i = 0; i < 10; i++)
            g.drawImage(smallCloud, i * SMALL_CLOUD_WIDTH - (int) (xLvlOffset * 0.7), 0, SMALL_CLOUD_WIDTH, SMALL_CLOUD_HEIGHT, null);

    }

    public void resetAll() {
        gameOver = false;
        paused = false;
        playerDying = false;
        player.resetAll();
        orbManager.resetAllOrbs();

    }

    public void checkBoostHit(Rectangle2D.Float attackBox) {
        orbManager.checkBoostHit(attackBox);
    }

    public void checkReverseHit(Rectangle2D.Float attackBox) {
        orbManager.checkReverseHit(attackBox);
    }

    public void setGameOver(boolean gameOver) {
        this.gameOver = gameOver;
    }

    @Override
    public void mouseClicked(MouseEvent e) {
    }

    public void mouseDragged(MouseEvent e) {
        if (paused)
            pauseOverlay.mouseDragged(e);
    }

    @Override
    public void mousePressed(MouseEvent e) {
        if (gameOver)
            gameOverOverlay.mousePressed(e);
        else if (paused)
        pauseOverlay.mousePressed(e);
        if (e.getButton() == MouseEvent.BUTTON1) {
            player.setAttacking(true);
            player.setJump(true);
            player.setChangeGravity(true);
        }
    }


    @Override
    public void mouseReleased(MouseEvent e) {
        if (gameOver)
            gameOverOverlay.mouseReleased(e);
        else if (paused)
            pauseOverlay.mouseReleased(e);
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        if (gameOver)
            gameOverOverlay.mouseMoved(e);
        else if (paused)
            pauseOverlay.mouseMoved(e);
    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (gameOver)
            gameOverOverlay.keyPressed(e);
        else
            switch (e.getKeyCode()) {
//            case KeyEvent.VK_A:
//                player.setLeft(true);
//                break;
//            case KeyEvent.VK_D:
//                player.setRight(true);
//                break;
//            case KeyEvent.VK_SPACE:
//                player.setJump(true);
//                break;
            case KeyEvent.VK_ESCAPE:
                paused = !paused;
                break;
        }

    }

    @Override
    public void keyReleased(KeyEvent e) {
        if (!gameOver) {

        }
    }

    public void unpauseGame() {
        paused = false;
    }

    public void windowFocusLost() {
        player.resetDirBooleans();
    }

    public Player getPlayer() {
        return player;
    }

    public void setPlayerDying(boolean playerDying) {
        this.playerDying = playerDying;
    }
}
