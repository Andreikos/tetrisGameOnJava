package tetris;

import javax.swing.*;
import javax.swing.plaf.metal.MetalLookAndFeel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.*;
import java.util.ArrayList;
import java.util.Random;

/**
 * The class is responsible for handling much of the game logic and reading user input.
 */
public class TetrisGame {
    /**
     * Write in file.
     */
    private FileWriter writer;
    /**
     * Read from file.
     */
    FileReader reader;
    /**
     * The figure that writes in file.
     */
    private boolean newFigure;
    /**
     * File for writing.
     */
    File f;
    /**
     * List of notation.
     */
    java.util.List<Notation> notations;
    /**
     * Whether or not the game is replayed.
     */
    private boolean replay;
    /**
     * This is the panel with the game board.
     */
    protected Panel theFirstPanel;
    /**
     * This is the panel with the next figure and score.
     */
    protected Panels theSecondPanel;
    /**
     * Whether or not the game is paused.
     */
    protected boolean pause;
    /**
     * Whether or not we've played a game.
     */
    protected boolean newGame;
    /**
     * Whether or not the game is over.
     */
    protected boolean gameOver;
    /**
     * The flag for thread.
     */
    protected boolean flag = false;
    /**
     * The flag for AI.
     */
    protected boolean AI;
    /**
     * The current score.
     */
    protected int score;
    /**
     * The current level.
     */
    protected int level;
    /**
     * The random number generator.
     */
    protected Random random;
    /**
     * The clock used for updating logic.
     */
    protected Time timer;
    /**
     * The current type of tile.
     */
    protected Types curType;
    /**
     * The next type of tile.
     */
    protected Types nextType;
    /**
     * The current column of tile.
     */
    protected int curCol;
    /**
     * The current row of tile.
     */
    protected int curRow;
    /**
     * The current rotation of tile.
     */
    protected int curRotate;
    /**
     * Provides a certain amount of time passes after the piece is generated.
     */
    protected int dropCooldown;
    /**
     * The speed of the game.
     */
    protected float gameSpeed;
    /**
     * The number of pieces that exist.
     */
    protected static final int type = Types.values().length;
    public File file;
    /**
     * The number of figures that exist in file
     */
    protected int number = 1;
    /**
     * Set the window properties.
     */
    public JFrame frame;

    public String path;

    public int count = 0;

    /**
     * Sets up the window's properties and adds a controller listener.
     */
    TetrisGame() {
        notations = new ArrayList<Notation>();
        newFigure = true;
        replay = false;
        frame = new JFrame("Tetris");
        frame.setLayout(new BorderLayout());
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setResizable(false);
        KeyGetter.loadKeys();

        JMenuBar menuBar = new JMenuBar();
        JMenu menu = new JMenu("File");
        JMenuItem itemNewGame = new JMenuItem("New");
        itemNewGame.addActionListener(new NewGameButtonListener());
        JMenuItem itemPauseGame = new JMenuItem("Pause");
        itemPauseGame.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_P, ActionEvent.CTRL_MASK));
        itemPauseGame.addActionListener(new PauseGameButtonListener());
        JMenuItem gameAI = new JMenuItem("GameAI");
        gameAI.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_L, ActionEvent.CTRL_MASK));
        gameAI.addActionListener(new gameAIListener());
        JMenuItem itemReplayGame = new JMenuItem("Replay");
        itemReplayGame.addActionListener(new replayListener());
        JMenuItem options = new JMenuItem("Options");
        options.addActionListener(new OptionsListener());
        JMenuItem itemInformation = new JMenuItem("About us");
        itemInformation.addActionListener(new InformationListener());
        JMenuItem itemExitGame = new JMenuItem("Exit");
        itemExitGame.addActionListener(new ExitButtonListener());
        menu.add(itemNewGame);
        menu.add(itemPauseGame);
        menu.add(gameAI);
        menu.add(itemReplayGame);
        menu.add(options);
        menu.add(itemInformation);
        menu.add(itemExitGame);

        JMenu statistics = new JMenu("Statistics");
        JMenuItem javaSortButton = new JMenuItem("Sort in Java");
        javaSortButton.addActionListener(new JavaSortButtonListener());
        JMenuItem scalaSortButton = new JMenuItem("Sort in Scala");
        scalaSortButton.addActionListener(new ScalaSortButtonListener());
        JMenuItem scalaStatisticButton = new JMenuItem("Statistic in Scala");
        scalaStatisticButton.addActionListener(new ScalaStatisticButtonListener());
        JMenuItem scalaNotationTransform = new JMenuItem("Transform notations");
        scalaNotationTransform.addActionListener(new ScalaNotationTransformListener());

        statistics.add(javaSortButton);
        statistics.add(scalaSortButton);
        statistics.add(scalaStatisticButton);
        statistics.add(scalaNotationTransform);

        JMenu otherMenu = new JMenu("Look and Feel");
        JMenuItem metalLookAndFeel = new JMenuItem("Metal");
        metalLookAndFeel.addActionListener(new MetalLookAndFeelListener());
        JMenuItem systemLookAndFeel = new JMenuItem("System");
        systemLookAndFeel.addActionListener(new SystemLookAndFeelListener());
        otherMenu.add(metalLookAndFeel);
        otherMenu.add(systemLookAndFeel);

        menu.add(new JSeparator(JSeparator.NORTH));
        frame.setJMenuBar(menuBar);
        this.theFirstPanel = new Panel(this);
        this.theSecondPanel = new Panels(this);
        menuBar.add(menu, BorderLayout.NORTH);
        menuBar.add(otherMenu, BorderLayout.NORTH);
        menuBar.add(statistics, BorderLayout.NORTH);
        frame.add(theFirstPanel, BorderLayout.CENTER);
        frame.add(theSecondPanel, BorderLayout.EAST);
        frame.setTitle("Tetris. Play with us!");
        new scoreUpdate();

        frame.addKeyListener(keyAdapter);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    /**
     * Starts the game running, initializes all and enters the game loop.
     */
    protected void startGame() {
        this.random = new Random();
        this.newGame = true;
        this.AI = false;
        this.gameSpeed = 1.0f;
        this.timer = new Time(gameSpeed);
        timer.setPause(true);

        while (true) {
            System.currentTimeMillis();
            timer.update();
            if (timer.elapsedCycle()) {
                updateGame();
            }
            if (dropCooldown > 0) {
                dropCooldown--;
            }
            renderGame();
        }
    }

    /**
     * Updates the bot.
     */
    protected void updateGame() {
        if (AI && !replay) {
            if (theFirstPanel.isEmpty(curType, curCol, curRow + 1, curRotate)) {
                curCol = 0;
                curRow++;

                for (int row = curRow; row < Panel.lines; row++) {
                    for (int col = curCol; col < Panel.columns; col++) {

                        if (theFirstPanel.isBusy1(col, row)) {
                            System.out.print(col);
                            System.out.println(row);
                            //while (theFirstPanel.isBusy1(col, row))
                            col++;
                            //if(theFirstPanel.isBusy2(col, row)) col++;
                        }
                        if (!theFirstPanel.isBusy(col, row)) {
                            if (theFirstPanel.isEmpty(curType, col, curRow, curRotate)) {
                                curCol = col;
                                break;
                            }
                        }
                    }
                }
        /*if(curType == Types.Type7 && theFirstPanel.isEmpty(curType, curCol-1, curRow, curRotate)) {
          curCol--;
        }*/
                printNotation(createNotation());
            } else {
                up();
            }
        } else if (replay) {
            Notation nextNotation = getNextNotation();
            if (nextNotation == null) {
                replay = false;
                timer.setPause(true);
            } else {
                int col = nextNotation.getCol();
                int row = nextNotation.getRow();
                Types type = nextNotation.getEnumType();
                Types t = nextNotation.getEnumNextType();
                if (theFirstPanel.isEmpty(curType, curCol, curRow + 1, curRotate)) {
                    curRow++;

                    if (nextNotation.getNumber() == number) {
                        curCol = col;
                        curRow = row;
                        curRotate = 0;
                        curType = type;
                        nextType = t;
                        number++;
                    }
                } else {
                    up();
                }
            }
        } else {
            if (theFirstPanel.isEmpty(curType, curCol, curRow + 1, curRotate)) {
                curRow++;
                printNotation(createNotation());
            } else {
                up();
        /*if(curType == Types.Type5 && theFirstPanel.isEmpty(curType, curCol+1, curRow, curRotate)) {
          curCol++;
        }*/
            }
        }
    }

    /**
     * Show notation for saving.
     *
     * @param notation
     */
    private void printNotation(Notation notation) {
        try {
            String s = notation.getScore() + " " + notation.getId() + " " + notation.getNumber() + " " + notation.getCol() + " "
                    + notation.getRow() + " " + notation.getType() + " " + notation.getNextType() + " " + notation.getId();
            writer.write(s);
            writer.append('\r');
            writer.append('\n');
            writer.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Create new notation.
     *
     * @return The notation.
     */
    private Notation createNotation() {
        Notation notation = new Notation(curCol, curRow, count, curType, nextType, newFigure, score);
        if (newFigure) {
            newFigure = false;
        }
        return notation;
    }

    /**
     * Get next notation for repeat game.
     *
     * @return Remove elements.
     */
    private Notation getNextNotation() {
        if (notations.size() > 0) {
            return notations.remove(0);
        }
        return null;
    }

    /**
     * Write elements in notation.
     */
    private void initAllNotations() {
        char[] buffer = new char[(int) f.length()];
        try {
            reader.read(buffer);
        } catch (IOException e) {
            e.printStackTrace();
        }
        String s = new String(buffer);
        String[] split = s.split("\r\n");
        notations.clear();
        for (String s1 : split) {
            String[] split1 = s1.split(" ");
            Notation notation = new Notation(
                    Integer.parseInt(split[3]), // col
                    Integer.parseInt(split[4]), // row
                    Integer.parseInt(split[5]), // type
                    Integer.parseInt(split[1]), // name
                    Integer.parseInt(split[2]), // number of figure
                    Integer.parseInt(split[6]),// next type
                    Integer.parseInt(split[0]) // score
            );
            notations.add(notation);
        }
    }

    /**
     * Replay the last game.
     */
    private void replay() {
        replay = true;
        f = new File("D:\\Saves\\ " + path + ".txt");
        try {
            reader = new FileReader("D:\\Saves\\ " + path + ".txt");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        initAllNotations();
        resetGame();
    }

    /**
     * Updates the game and handles part of it's logic.
     */
    public void up() {
        theFirstPanel.addPiece(curType, curCol, curRow, curRotate);
        flag = true;
        synchronized (theFirstPanel) {
            spawnPiece();
        }
    }

    /**
     * Updates the level and score in game.
     */
    public void scores() {
        int cleared = theFirstPanel.checkLines();
        if (cleared > 0) {
            score += 25 << cleared;
            gameSpeed += 0.1f;
        }
        timer.setCycles(gameSpeed);
        timer.setTime();
        dropCooldown = 20;
        level = (int) (gameSpeed * 1.60f);
    }

    /**
     * Forces the panels to repaint.
     */
    protected void renderGame() {
        theFirstPanel.repaint();
        theSecondPanel.repaint();
    }

    /**
     * Resets the game variables to their default values at the start of a new game.
     */
    protected void resetGame() {
        this.level = 1;
        this.score = 0;
        this.AI = false;
        this.gameSpeed = 1.0f;
        this.nextType = Types.values()[random.nextInt(type)];
        this.newGame = false;
        this.gameOver = false;
        theFirstPanel.clear();
        timer.setTime();
        timer.setCycles(gameSpeed);
        spawnPiece();
    }

    /**
     * Spawns a new piece and resets our piece's variables to their default values.
     */
    protected void spawnPiece() {

        newFigure = true;
        this.curType = nextType;
        this.curCol = curType.getSpawnColumn();
        this.curRow = curType.getSpawnRow();
        this.nextType = Types.values()[random.nextInt(type)];
        if (!theFirstPanel.isEmpty(curType, curCol, curRow, curRotate)) {
            this.gameOver = true;
            timer.setPause(true);
        }
    }

    /**
     * Set the rotation of the current piece to newRotation.
     *
     * @param newRotate The rotation of the new piece.
     */
    private void RotatePiece(int newRotate) {
        int newCol = curCol;
        int newRow = curRow;
        int left = curType.getLeftInset(newRotate);
        int right = curType.getRightInset(newRotate);
        int top = curType.getTopInset(newRotate);
        int bottom = curType.getBottomInset(newRotate);
        if (curCol < -left) {
            newCol -= curCol - left;
        } else if (curCol + curType.getDimension() - right >= Panel.columns) {
            newCol -= (curCol + curType.getDimension() - right) - Panel.columns + 1;
        }
        if (curRow < -top) {
            newRow -= curRow - top;
        } else if (curRow + curType.getDimension() - bottom >= Panel.lines) {
            newRow -= (curRow + curType.getDimension() - bottom) - Panel.lines + 1;
        }
        if (theFirstPanel.isEmpty(curType, newCol, newRow, newRotate)) {
            curRotate = newRotate;
            curRow = newRow;
            curCol = newCol;
        }
    }

    /**
     * Adds a custom anonymous KeyListener to the frame. We set the settings yourself, but some keys
     * are used for the default game.
     */
    KeyAdapter keyAdapter = new KeyAdapter() {
        public void keyPressed(KeyEvent e) {
            if (!AI && !replay) {
                if (KeyEvent.getKeyText(e.getKeyCode()).equals(Configuration.down)) {
                    if (!pause && dropCooldown == 0) {
                        timer.setCycles(25.0f);
                    }
                } else if (KeyEvent.getKeyText(e.getKeyCode()).equals(Configuration.left)) {
                    if (!pause && theFirstPanel.isEmpty(curType, curCol - 1, curRow, curRotate)) {
                        curCol--;
                    }
                } else if (KeyEvent.getKeyText(e.getKeyCode()).equals(Configuration.right)) {
                    if (!pause && theFirstPanel.isEmpty(curType, curCol + 1, curRow, curRotate)) {
                        curCol++;
                    }
                } else if (KeyEvent.getKeyText(e.getKeyCode()).equals(Configuration.pause)) {
                    if (!gameOver && !newGame) {
                        pause = !pause;
                        timer.setPause(pause);
                    }
                } else if (KeyEvent.getKeyText(e.getKeyCode()).equals(Configuration.rotateLeft)) {
                    if (!pause) {
                        RotatePiece((curRotate == 0) ? 3 : curRotate - 1);
                    }
                } else if (KeyEvent.getKeyText(e.getKeyCode()).equals(Configuration.rotateRight)) {
                    if (!pause) {
                        RotatePiece((curRotate == 3) ? 0 : curRotate + 1);
                    }
                } else if (KeyEvent.getKeyText(e.getKeyCode()).equals(Configuration.game)) {
                    resetGame();
                } else {
                    switch (e.getKeyCode()) {
                        case KeyEvent.VK_DOWN:
                            if (!pause && dropCooldown == 0) {
                                timer.setCycles(25.0f);
                            }
                            break;
                        case KeyEvent.VK_LEFT:
                            if (!pause && theFirstPanel.isEmpty(curType, curCol - 1, curRow, curRotate)) {
                                curCol--;
                            }
                            break;
                        case KeyEvent.VK_RIGHT:
                            if (!pause && theFirstPanel.isEmpty(curType, curCol + 1, curRow, curRotate)) {
                                curCol++;
                            }
                            break;
                        case KeyEvent.VK_P:
                            if (!gameOver && !newGame) {
                                pause = !pause;
                                timer.setPause(pause);
                            }
                            break;
                        case KeyEvent.VK_SPACE:
                            if (!pause) {
                                RotatePiece((curRotate == 0) ? 3 : curRotate - 1);
                            }
                            break;
                        case KeyEvent.VK_UP:
                            if (!pause) {
                                RotatePiece((curRotate == 3) ? 0 : curRotate + 1);
                            }
                            break;
                        case KeyEvent.VK_ENTER:
                            if (gameOver || newGame) {
                                resetGame();
                            }
                            break;
                    }
                }
            }
        }
    };

    /**
     * Set the style of our window.
     */
    private class SystemLookAndFeelListener implements ActionListener {
        public void actionPerformed(ActionEvent event) {
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (Exception ex) {
            }
        }
    }

    /**
     * Set the style of our window.
     */
    private class MetalLookAndFeelListener implements ActionListener {
        public void actionPerformed(ActionEvent event) {
            try {
                MetalLookAndFeel look = new MetalLookAndFeel();
                UIManager.setLookAndFeel(look);
            } catch (Exception ex) {
            }
        }
    }

    /**
     * Start the new game in menu.
     */
    private class NewGameButtonListener implements ActionListener {
        public void actionPerformed(ActionEvent event) {
            resetGame();
        }
    }

    /**
     * Some information about author. Set the window with this information and button for exit.
     */
    private class InformationListener implements ActionListener {
        public void actionPerformed(ActionEvent event) {
            JFrame we = new JFrame("About us");
            we.setLayout(new FlowLayout());
            we.setSize(450, 150);
            we.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            JLabel text = new JLabel("Game was created by Varenik corporation.");
            JLabel email =
                    new JLabel("With all suggestions please contact with us " + "by andre.war@mail.ru");
            we.add(text);
            we.add(email);
            JButton okay = new JButton("Okay");
            okay.addActionListener(new ExitButtonListener());
            we.add(okay);
            we.pack();
            we.setLocationRelativeTo(null);
            we.setVisible(true);
        }
    }

    /**
     * Start the bot in menu.
     *
     * @author Andreikos
     */
    private class gameAIListener implements ActionListener {
        public void actionPerformed(ActionEvent event) {
            resetGameForBot();
            count++;
        }
    }

    /**
     * Reset game for bot.
     */
    protected void resetGameForBot() {
        path = new String();
        path = Integer.toString(count);
        f = new File("D:\\Saves\\ " + path + ".txt");
        System.out.println(path);
        try {
            if (f.exists()) {
                f.delete();
            }
            f.createNewFile();
            writer = new FileWriter(f.getAbsoluteFile(), false);
        } catch (IOException e) {
            e.printStackTrace();
        }
        this.level = 1;
        this.score = 0;
        this.gameSpeed = 5.0f;
        this.nextType = Types.values()[random.nextInt(type)];
        this.newGame = false;
        this.gameOver = false;
        theFirstPanel.clear();
        timer.setTime();
        timer.setCycles(gameSpeed);
        this.AI = false;
        spawnPiece();
    }

    /**
     * Replay the last game.
     *
     * @author Andreikos
     */
    private class replayListener implements ActionListener {
        public void actionPerformed(ActionEvent event) {
            replay();
        }
    }

    /**
     * Pause our game with helping menu.
     */
    private class PauseGameButtonListener implements ActionListener {
        public void actionPerformed(ActionEvent event) {
            pause = !pause;
            timer.setPause(pause);
        }
    }

    /**
     * Open options and set the keys for game.
     */
    private class OptionsListener implements ActionListener {
        public void actionPerformed(ActionEvent event) {
            Configuration.openConfig(null);
        }
    }


    private class JavaSortButtonListener implements ActionListener {
        public void actionPerformed(ActionEvent event) {
            notations.clear();

            String path = "D:\\Saves";
            File[] files = new File(path).listFiles();

            JavaSort javaSort = new JavaSort();
            long time = System.currentTimeMillis();

            try {
                for (int i = 0; i < files.length; i++) {
                    BufferedReader reader = new BufferedReader(new FileReader(files[i]));
                    String s = reader.readLine();
                    while (s != null) {
                        String[] split = s.split(" ");
                        Notation notation = new Notation(
                                Integer.parseInt(split[3]), // col
                                Integer.parseInt(split[4]), // row
                                Integer.parseInt(split[5]), // type
                                Integer.parseInt(split[1]), // name
                                Integer.parseInt(split[2]), // number of figure
                                Integer.parseInt(split[6]),// next type
                                Integer.parseInt(split[0]) // score
                        );
                        notations.add(notation);
                        s = reader.readLine();
                    }
                }
            } catch (IOException ex) {
                ex.printStackTrace();
            }
            Notation[] n = new Notation[notations.size()];

            for (int i = 0; i < notations.size(); i++) {
                n[i] = notations.get(i);
            }

            javaSort.qSort(n, 0, n.length - 1);

            time = System.currentTimeMillis() - time;
            new SortTable("Java", n, Long.toString(time));

        }
    }

    private class ScalaStatisticButtonListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            notations.clear();

            String path = "D:\\Saves";
            File[] files = new File(path).listFiles();

            ScalaSort scalaSort = new ScalaSort();
            long time = System.currentTimeMillis();

            try {
                for (int i = 0; i < files.length; i++) {
                    BufferedReader reader = new BufferedReader(new FileReader(files[i]));
                    String s = reader.readLine();
                    while (s != null) {
                        String[] split = s.split(" ");
                        Notation notation = new Notation(
                                Integer.parseInt(split[3]), // col
                                Integer.parseInt(split[4]), // row
                                Integer.parseInt(split[5]), // type
                                Integer.parseInt(split[1]), // name
                                Integer.parseInt(split[2]), // number of figure
                                Integer.parseInt(split[6]),// next type
                                Integer.parseInt(split[0]) // score
                        );
                        notations.add(notation);
                        s = reader.readLine();
                    }
                }
            } catch (IOException ex) {
                ex.printStackTrace();
            }
            Notation[] n = new Notation[notations.size()];

            for (int i = 0; i < notations.size(); i++) {
                n[i] = notations.get(i);
            }

            ScalaStatistics scalaStatistic = new ScalaStatistics();
            scalaStatistic.getStatistic(n);
        }
    }

    private class ScalaNotationTransformListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            notations.clear();

            String path = "D:\\Saves";
            File[] files = new File(path).listFiles();

            ScalaSort scalaSort = new ScalaSort();
            long time = System.currentTimeMillis();

            try {
                for (int i = 0; i < files.length; i++) {
                    BufferedReader reader = new BufferedReader(new FileReader(files[i]));
                    String s = reader.readLine();
                    while (s != null) {
                        String[] split = s.split(" ");
                        Notation notation = new Notation(
                                Integer.parseInt(split[3]), // col
                                Integer.parseInt(split[4]), // row
                                Integer.parseInt(split[5]), // type
                                Integer.parseInt(split[1]), // name
                                Integer.parseInt(split[2]), // number of figure
                                Integer.parseInt(split[6]),// next type
                                Integer.parseInt(split[0]) // score
                        );
                        notations.add(notation);
                        s = reader.readLine();
                    }
                }
            } catch (IOException ex) {
                ex.printStackTrace();
            }
            NotationTransformer notationTransformer = new NotationTransformer();
            int size = 0;
            System.out.println("New botgame is started.");
            for (int i = 0; i < notations.size(); i++) {
                size++;
                System.out.println(notationTransformer.parse(notations.get(i)));
            }

            System.out.println(notationTransformer.parse(size));
        }
    }

    private class ScalaSortButtonListener implements ActionListener {
        public void actionPerformed(ActionEvent event) {

            notations.clear();

            String path = "D:\\Saves";
            File[] files = new File(path).listFiles();

            ScalaSort scalaSort = new ScalaSort();
            long time = System.currentTimeMillis();

            try {
                for (int i = 0; i < files.length; i++) {
                    BufferedReader reader = new BufferedReader(new FileReader(files[i]));
                    String s = reader.readLine();
                    while (s != null) {
                        String[] split = s.split(" ");
                        Notation notation = new Notation(
                                Integer.parseInt(split[3]), // col
                                Integer.parseInt(split[4]), // row
                                Integer.parseInt(split[5]), // type
                                Integer.parseInt(split[1]), // name
                                Integer.parseInt(split[2]), // number of figure
                                Integer.parseInt(split[6]),// next type
                                Integer.parseInt(split[0]) // score
                        );
                        notations.add(notation);
                        s = reader.readLine();
                    }
                }
            } catch (IOException ex) {
                ex.printStackTrace();
            }
            Notation[] n = new Notation[notations.size()];

            for (int i = 0; i < notations.size(); i++) {
                n[i] = notations.get(i);
            }

            scalaSort.sort(n);

            time = System.currentTimeMillis() - time;
            new SortTable("Scala", n, Long.toString(time));
        }

    }

    /**
     * Exit from game.
     */
    private class ExitButtonListener implements ActionListener {
        public void actionPerformed(ActionEvent event) {
            System.exit(0);
        }
    }

    /**
     * Checks to see whether or not the game is over.
     *
     * @return Whether or not the game is over.
     */
    public boolean gameOver() {
        return gameOver;
    }

    /**
     * Checks to see whether or not the game started.
     *
     * @return Whether or not the game started.
     */
    public boolean newGame() {
        return newGame;
    }

    /**
     * Checks to see whether or not the game used AI.
     *
     * @return Whether or not the game used AI.
     */
    public boolean AI() {
        return AI;
    }

    /**
     * Checks to see whether or not the game is paused.
     *
     * @return Whether or not the game is paused.
     */
    public boolean pause() {
        return pause;
    }

    /**
     * Gets the current score.
     *
     * @return The score.
     */
    public int getScore() {
        return score;
    }

    /**
     * Gets the current level.
     *
     * @return The level.
     */
    public int getLevel() {
        return level;
    }

    /**
     * Gets the current type of piece we're using.
     *
     * @return The piece type.
     */
    public Types getPieceType() {
        return curType;
    }

    /**
     * Gets the current type of piece we'll use.
     *
     * @return The piece type.
     */
    public Types getNextPieceType() {
        return nextType;
    }

    /**
     * Gets the column of the current piece.
     *
     * @return The column.
     */
    public int getPieceCol() {
        return curCol;
    }

    /**
     * Gets the row of the current piece.
     *
     * @return The row.
     */
    public int getPieceRow() {
        return curRow;
    }

    /**
     * Gets the rotation of the current piece.
     *
     * @return The rotation.
     */
    public int getPieceRotate() {
        return curRotate;
    }

    /**
     * Gets the panel for game.
     *
     * @return The Panel.
     */
    public Panel getPanel() {
        return theFirstPanel;
    }

    /**
     * Use other thread for score and level.
     *
     * @author Andreikos
     */
    private class scoreUpdate implements Runnable {

        Thread update = null;

        public scoreUpdate() {
            if (update == null) {
                update = new Thread(this);
                update.start();
            }
        }

        @Override
        public void run() {
            while (true) {
                synchronized (theFirstPanel) {
                    if (flag) {
                        scores();
                        flag = false;
                    }
                }
            }
        }
    }

    /**
     * Entry-point of the game that responsible for creating and starting a new game.
     *
     * @param args Unused.
     */
    public static void main(String[] args) {
        TetrisGame tetris = new TetrisGame();
        tetris.startGame();
    }
}
