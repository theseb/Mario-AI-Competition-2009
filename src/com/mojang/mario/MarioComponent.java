package com.mojang.mario;

import com.mojang.mario.sprites.Mario;
import com.mojang.mario.Agents.IAgent;
import com.mojang.mario.Agents.Human.CheaterKeyboardAgent;
import com.mojang.mario.Tools.GameViewer;
import com.mojang.mario.Tools.EvaluationInfo;
import com.mojang.mario.Environments.IEnvironment;
import com.mojang.mario.Environments.EnvCell;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.VolatileImage;
import java.util.ArrayList;
import java.util.List;


public class MarioComponent extends JComponent implements Runnable, /*KeyListener,*/ FocusListener, IEnvironment
{
    private static final long serialVersionUID = 739318775993206607L;
    public static final int TICKS_PER_SECOND = 24;

    private boolean running = false;
    private int width, height;
    private GraphicsConfiguration graphicsConfiguration;
    private Scene scene;
    private boolean focused = false;
    private boolean useScale2x = false;

    int frame;
    int delay;
    Thread animator;

    public void setGameViewer(GameViewer gameViewer) {        this.gameViewer = gameViewer;    }

    private GameViewer gameViewer = null;

    private IAgent agent = null;
    private CheaterKeyboardAgent cheatAgent = null;

    private Scale2x scale2x = new Scale2x(320, 240);
    private KeyAdapter prevHumanKeyBoardAgent;
    private Mario mario = null;
    private LevelScene levelScene = null;

    public MarioComponent(int width, int height, IAgent agent)
    {
        AdjustFPS();


        this.setFocusable(true);
        this.setEnabled(true);
        this.width = width;
        this.height = height;

        Dimension size = new Dimension(width, height);
        setPreferredSize(size);
        setMinimumSize(size);
        setMaximumSize(size);

        setFocusable(true);

        this.cheatAgent = new CheaterKeyboardAgent();
        this.addKeyListener(cheatAgent);
        this.setAgent(agent);
        
        GlobalOptions.registerMarioComponent(this);

    }

    public void AdjustFPS()
    {
        int fps = GlobalOptions.FPS;
        delay = (fps > 0) ? (fps >= GlobalOptions.InfiniteFPS) ? 0 : (1000 / fps) : 100;
//        System.out.println("Delay: " + delay);
    }

    private void toggleKey(int keyCode, boolean isPressed)
    {
//        System.out.println("key code:" + keyCode);
//        if (keyCode == KeyEvent.VK_LEFT)
//        {
//            System.out.println("Left");
//            scene.toggleKey(Mario.KEY_LEFT, isPressed);
//        }
//        if (keyCode == KeyEvent.VK_RIGHT)
//        {
//            System.out.println("right");
//            scene.toggleKey(Mario.KEY_RIGHT, isPressed);
//        }
//        if (keyCode == KeyEvent.VK_DOWN)
//        {
//            scene.toggleKey(Mario.KEY_DOWN, isPressed);
//        }
//        if (keyCode == KeyEvent.VK_UP)
//        {
//            scene.toggleKey(Mario.KEY_UP, isPressed);
//        }
//        if (keyCode == KeyEvent.VK_A)
//        {
//            System.out.println("A");
//            scene.toggleKey(Mario.KEY_SPEED, isPressed);
//        }
//        if (keyCode == KeyEvent.VK_S)
//        {
//            if (isPressed)
//                System.out.println("S");
//            scene.toggleKey(Mario.KEY_JUMP, isPressed);
//        }
//        if (keyCode == KeyEvent.VK_P)
//        {
//            if (isPressed)
//            {
//                System.out.println("MarioComponent: Paused On/Off");
//                GlobalOptions.pauseWorld = !GlobalOptions.pauseWorld;
//                if (GlobalOptions.pauseWorld)
//                    scene.toggleKey(Mario.KEY_PAUSE, true);
//                else
//                    scene.toggleKey(Mario.KEY_PAUSE, false);
//
//            }
//        }
//
//        if (keyCode == KeyEvent.VK_D)
//        {
//            if (isPressed)
//                System.out.println("Dump Current World");
//            gameViewer.tick();
//        }
//
//        if (keyCode == KeyEvent.VK_U)
//        {
//            if (isPressed)
//                System.out.println("Life UP!");
//            scene.toggleKey(Mario.KEY_LIFE_UP, isPressed);
//        }
//
//        if (keyCode == KeyEvent.VK_W)
//        {
//            if (isPressed)
//            {
//                System.out.println("Force WIn!");
////                stop();
//            }
//            scene.toggleKey(Mario.KEY_WIN, isPressed);
//        }
//
//
//        if (keyCode == KeyEvent.VK_L)
//        {
//            if (isPressed)
//            {
//                System.out.println("Labels On/Off");
//                GlobalOptions.Labels = !GlobalOptions.Labels;
//            }
//        }
//
//        if (keyCode == KeyEvent.VK_C)
//        {
//            if (isPressed)
//            {
//                System.out.println("Center On/Off");
//                GlobalOptions.MarioAlwaysInCenter = !GlobalOptions.MarioAlwaysInCenter;
//            }
//        }
//
//        if (keyCode == KeyEvent.VK_R)
//        {
//            if (isPressed)
//            {
//
//                System.out.println("Random Agent On/Off");
//                GlobalOptions.RandomAgent = !GlobalOptions.RandomAgent;
//            }
//        }
//
//        if (keyCode == 61)
//        {
//            if (isPressed)
//            {
//
//                System.out.println("FPS increase by 1. Current FPS is " + ++GlobalOptions.FPS);
//                AdjustFPS();
//            }
//        }
//
//        if (keyCode == 45)
//        {
//            if (isPressed)
//            {
//
//                System.out.println("FPS decrease . Current FPS is " + --GlobalOptions.FPS);
//                AdjustFPS();
//            }
//
//        }
//
//        if (isPressed && keyCode == KeyEvent.VK_F1)
//        {
//            useScale2x = !useScale2x;
//        }
    }

    public void paint(Graphics g)
    {
    }

    public void update(Graphics g)
    {
    }

    public void Init()
    {
//        if (GlobalOptions.VizualizationOn)
//        {
//
            graphicsConfiguration = getGraphicsConfiguration();
            Art.init(graphicsConfiguration);
//        }

    }

    public void start()
    {
        if (!running)
        {
            running = true;
            animator = new Thread(this, "Game Thread");
            animator.start();
        }
    }

    public void stop()
    {
        running = false;
    }

    public void run()
    {

    }

    public EvaluationInfo run1() {
        running = true;
        AdjustFPS();
        EvaluationInfo evaluationInfo = new EvaluationInfo();

        VolatileImage image = null;
        Graphics g = null;
        Graphics og = null;

        image = createVolatileImage(320, 240);
        g = getGraphics();
        og = image.getGraphics();

        if (!GlobalOptions.VizualizationOn)
        {
            String msgClick = "Vizualization is not available";
            drawString(og, msgClick, 160 - msgClick.length() * 4, 110, 1);
            drawString(og, msgClick, 160 - msgClick.length() * 4, 110, 7);
        }

        addFocusListener(this);

        // Remember the starting time
        long tm = System.currentTimeMillis();
        long tick = tm;
        int marioStatus = Mario.STATUS_RUNNING;

        mario = ((LevelScene) scene).mario;
        int totalActionsPerfomed = 0;
// TODO: Manage better place for this:
        Mario.resetCoins();

        while (/*Thread.currentThread() == animator*/ running) {
            // Display the next frame of animation.
//                repaint();
            scene.tick();
            if (gameViewer.getContinuousUpdatesState())
                gameViewer.tick();

            float alpha = 0;

//            og.setColor(Color.RED);
            if (GlobalOptions.VizualizationOn)
            {
                og.fillRect(0, 0, 320, 240);
                scene.render(og, alpha);
            }

            boolean[] action = agent.GetAction(this/*DummyEnvironment*/);
            for (int i = 0; i < IEnvironment.NumberOfActions; ++i)
            if (action[i])
            {
                ++totalActionsPerfomed;
                break;
            }
            //Apply Action;
//            scene.keys = action;
            ((LevelScene) scene).mario.keys = action;
            ((LevelScene) scene).mario.cheatKeys = cheatAgent.GetAction(null);

            if (GlobalOptions.VizualizationOn) {
                String msg = GlobalOptions.CurrentAgentStr + ". ";
                drawString(og, msg, 7, 41, 0);
                drawString(og, msg, 6, 40, 2);
                msg = "Selected Actions: ";
                drawString(og, msg, 7, 51, 0);
                drawString(og, msg, 6, 50, 2);

                msg = "";
                for (int i = 0; i < IEnvironment.NumberOfActions; ++i)
                    msg += (action[i]) ? scene.keysStr[i] : "      ";

                drawString(og, msg, 6, 70, 1);


                if (!this.hasFocus() && tick / 4 % 2 == 0) {
                    String msgClick = "CLICK TO PLAY";

//                    og.setColor(Color.YELLOW);
//                    og.drawString(msgClick, 320 + 1, 20 + 1);
                    drawString(og, msgClick, 160 - msgClick.length() * 4, 110, 1);
                    drawString(og, msgClick, 160 - msgClick.length() * 4, 110, 7);
                }
                og.setColor(Color.DARK_GRAY);
                drawString(og, "FPS: " + ((GlobalOptions.FPS > 99) ? "\\infty" : GlobalOptions.FPS.toString()), 5, 22, 0);
                drawString(og, "FPS: " + ((GlobalOptions.FPS > 99) ? "\\infty" : GlobalOptions.FPS.toString()), 4, 21, 7);

                if (width != 320 || height != 240) {
                    if (useScale2x) {
                        g.drawImage(scale2x.scale(image), 0, 0, null);
                    } else {
                        g.drawImage(image, 0, 0, 640 * 2, 480 * 2, null);
                    }
                } else {
                    g.drawImage(image, 0, 0, null);
                }
            } else
            {
                // Win or Die without renderer!! independently.
                marioStatus = ((LevelScene) scene).mario.getStatus();
                if (marioStatus != Mario.STATUS_RUNNING)
                    stop();
            }
            // Delay depending on how far we are behind.
            if (delay > 0)
                try {
                    tm += delay;
                    Thread.sleep(Math.max(0, tm - System.currentTimeMillis()));
                } catch (InterruptedException e) {
                    break;
                }

            // Advance the frame
            frame++;
        }
//=========
        // TODO: distinguish map coordinates, physical coordinates. done
        evaluationInfo.agentType = agent.getClass().getSimpleName();
        evaluationInfo.agentName = agent.getName();
        evaluationInfo.marioStatus = mario.getStatus();
        evaluationInfo.livesLeft = mario.lives;               
        evaluationInfo.lengthOfLevelPassedPhys = mario.x;
        evaluationInfo.lengthOfLevelPassedCells = mario.mapX;
        evaluationInfo.totalLengthOfLevelCells = levelScene.level.getWidthCells();
        evaluationInfo.totalLengthOfLevelPhys = levelScene.level.getWidthPhys();
        evaluationInfo.timeSpentOnLevel     = levelScene.getStartTime();
        evaluationInfo.timeLeft     = levelScene.getTimeLeft();
        evaluationInfo.totalTimeGiven       = levelScene.getTotalTime();
        evaluationInfo.numberOfGainedCoins  = Mario.coins;
//        evaluationInfo.totalNumberOfCoins   = -1 ; // TODO: total Number of coins.
        evaluationInfo.totalActionsPerfomed = totalActionsPerfomed; // Counted during the play/simulation process
        evaluationInfo.totalFramesPerfomed = frame;
        evaluationInfo.Memo = "Number of attempt: " + Mario.numberOfAttempts;
        return evaluationInfo;
    }

    private void drawString(Graphics g, String text, int x, int y, int c)
    {
        char[] ch = text.toCharArray();
        for (int i = 0; i < ch.length; i++)
        {
            g.drawImage(Art.font[ch[i] - 32][c], x + i * 8, y, null);
        }
    }

//    public void keyPressed(KeyEvent arg0)
//    {
//        toggleKey(arg0.getKeyCode(), true);
//    }
//
//    public void keyReleased(KeyEvent arg0)
//    {
//        toggleKey(arg0.getKeyCode(), false);
//    }

    public void startLevel(long seed, int difficulty, int type, int levelLength)
    {
        scene = new LevelScene(graphicsConfiguration, this, seed, difficulty, type, levelLength);
        levelScene = ((LevelScene) scene);
        scene.init();
    }

    public void levelFailed()
    {
//        scene = mapScene;
        Mario.lives--;
        stop();
    }

    public void keyTyped(KeyEvent arg0)
    {
    }

    public void focusGained(FocusEvent arg0)
    {
        focused = true;
    }

    public void focusLost(FocusEvent arg0)
    {
        focused = false;
    }

    public void levelWon()
    {
        stop();
//        scene = mapScene;
//        mapScene.levelWon();
    }

    public void toTitle()
    {
//        Mario.resetStatic();
//        scene = new TitleScene(this, graphicsConfiguration);
//        scene.init();
    }

    public List<String> getObservation(boolean Enemies, boolean LevelMap, boolean Complete, int ZLevel)
    {
        if (scene instanceof LevelScene)
            return ((LevelScene)scene).LevelSceneAroundMarioASCIIDump(Enemies, LevelMap, Complete, ZLevel);
        else
        {
            List<String> ret = new ArrayList<String>();
//
            return ret;
        }
    }

    public EnvCell[][] getCompleteObservation() {
        return new EnvCell[0][];  //To change body of implemented methods use File | Settings | File Templates.
    }

    public int[][] getEnemiesObservation() {
        return new int[0][];  //To change body of implemented methods use File | Settings | File Templates.
    }

    public byte[][] getLevelSceneObservation() {
        if (scene instanceof LevelScene)
            return ((LevelScene)scene).LevelSceneObservation(1);
        return null;
    }

    public boolean isMarioOnGround() {
        return mario.isOnGround();
    }

    public boolean mayMarioJump() {
        return mario.mayJump();
    }

//    public Point getMarioPosition()
//    {
//        if (scene instanceof LevelScene)
//            return new Point(((LevelScene)scene).mario.mapX, ((LevelScene)scene).mario.mapY);
//
//        return null;
//    }

    public void setAgent(IAgent agent)
    {
        this.agent = agent;
        if (agent instanceof KeyAdapter)
        {
            if (prevHumanKeyBoardAgent != null)
                this.removeKeyListener(prevHumanKeyBoardAgent);
            this.prevHumanKeyBoardAgent = (KeyAdapter)agent;
            this.addKeyListener(prevHumanKeyBoardAgent);
        }
    }

    public void setPaused(boolean paused) {
        levelScene.paused = paused;
    }
}