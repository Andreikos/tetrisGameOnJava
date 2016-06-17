package tetris;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

/**
 * The class is responsible for choosing keys for the game.
 */
public class Configuration {

    public static String rotateLeft = "Up", rotateRight = "Up1", left = "Left", right = "Right",
            down = "Down", pause = "Pause", game = "Game";
    public static String A = "A", D = "D", S = "S", Q = "Q", W = "W", E = "E", R = "R";
    private static ArrayList<Choice> choices;

    /**
     * Set the properties of the window, determine the position of the buttons, adds a controller
     * listener.
     *
     * @param frame The Frame.
     */
    public static void openConfig(JFrame frame) {
        int x = 30, x1 = 150, x0 = 400;
        int y = 30, y1 = 80, y2 = 130, y3 = 180, y4 = 220, y0 = 300;
        int height = 50, width = 100;
        choices = new ArrayList<Choice>();
        final JFrame options = new JFrame("Options");
        options.setSize(x0, y0);
        options.setResizable(false);
        options.setLocationRelativeTo(frame);
        options.setLayout(null);
        Choice left = addChoice("Left", options, x, y);
        left.select(Configuration.A);
        Choice right = addChoice("Right", options, x1, y);
        right.select(Configuration.D);
        Choice down = addChoice("Down", options, x, y1);
        down.select(Configuration.S);
        Choice rotateLeft = addChoice("RotateLeft", options, x, y2);
        rotateLeft.select(Configuration.Q);
        Choice rotateRight = addChoice("RotateRight", options, x1, y2);
        rotateRight.select(Configuration.E);
        Choice pause = addChoice("Pause", options, x1, y1);
        pause.select(Configuration.R);
        Choice game = addChoice("New Game", options, x, y3);
        game.select(Configuration.W);
        JButton done = new JButton("Done");
        done.setBounds(x1, y4, width, height);
        done.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                options.dispose();
                saveChanges();
            }
        });
        options.add(done);
        options.setVisible(true);
    }

    /**
     * Save our choose for this game.
     */
    public static void saveChanges() {
        Choice left = choices.get(0);
        Choice right = choices.get(1);
        Choice down = choices.get(2);
        Choice rotateLeft = choices.get(3);
        Choice rotateRight = choices.get(4);
        Choice pause = choices.get(5);
        Choice game = choices.get(6);
        Configuration.left = left.getSelectedItem();
        Configuration.right = right.getSelectedItem();
        Configuration.down = down.getSelectedItem();
        Configuration.rotateLeft = rotateLeft.getSelectedItem();
        Configuration.rotateRight = rotateRight.getSelectedItem();
        Configuration.pause = pause.getSelectedItem();
        Configuration.game = game.getSelectedItem();
    }

    /**
     * Set properties for windows
     */
    public static Choice addChoice(String name, JFrame options, int x, int y) {
        int height = 20, width = 100, sizeLeft = 20;
        JLabel label = new JLabel(name);
        label.setBounds(x, y - sizeLeft, width, height);
        Choice key = new Choice();
        for (String s : getKeyNames()) {
            key.add(s);
        }
        key.setBounds(x, y, width, height);
        options.add(key);
        options.add(label);
        choices.add(key);
        return key;
    }

    /**
     * Array of lines
     *
     * @return The ArrayList<String>.
     */
    public static ArrayList<String> getKeyNames() {
        ArrayList<String> result = new ArrayList<String>();
        for (String s : KeyGetter.keyNames) {
            result.add(s);
            if (s.equalsIgnoreCase("F24")) {
                break;
            }
        }
        return result;
    }
}
