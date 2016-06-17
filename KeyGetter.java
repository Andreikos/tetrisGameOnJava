package tetris;

import java.awt.event.KeyEvent;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * The class is responsible for keys in the game.
 */
public class KeyGetter {
    public static HashMap<String, Integer> keys;
    public static ArrayList<String> keyNames;

    /**
     * Take keys from system.
     */
    public static void loadKeys() {
        keys = new HashMap<String, Integer>();
        keyNames = new ArrayList<String>();
        Field[] fields = KeyEvent.class.getFields();
        for (Field f : fields) {
            if (Modifier.isStatic(f.getModifiers())) {
                if (f.getName().startsWith("VK")) {
                    try {
                        int num = f.getInt(null);
                        String name = KeyEvent.getKeyText(num);
                        keys.put(name, num);
                        keyNames.add(name);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }
}
