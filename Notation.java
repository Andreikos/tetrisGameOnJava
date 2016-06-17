package tetris;


public class Notation {

    private static int id;
    private static int number;
    private int col;
    private int row;
    private int type;
    private int num;
    int scor;

    private int nextType;

    public Notation(int col, int row, int type, int count, int number, int nextType, int score) {
        this.col = col;
        this.row = row;
        this.type = type;
        num = count;
        scor = score;
        this.nextType = nextType;
    }

    public Notation(int col, int row, int count, Types t, Types nextT, boolean newFigure, int score) {
        this.col = col;
        this.row = row;
        id++;
        if (newFigure) {
            number++;
        }
        num = number;
        scor = score;
        switch (t) {
            case Type1:
                type = 1;
                break;
            case Type2:
                type = 2;
                break;
            case Type3:
                type = 3;
                break;
            case Type4:
                type = 4;
                break;
            case Type5:
                type = 5;
                break;
            case Type6:
                type = 6;
                break;
            case Type7:
                type = 7;
                break;
        }
        switch (nextT) {
            case Type1:
                nextType = 1;
                break;
            case Type2:
                nextType = 2;
                break;
            case Type3:
                nextType = 3;
                break;
            case Type4:
                nextType = 4;
                break;
            case Type5:
                nextType = 5;
                break;
            case Type6:
                nextType = 6;
                break;
            case Type7:
                nextType = 7;
                break;
        }
    }

    public int getId() {
        return id;
    }

    public int getCol() {
        return col;
    }

    public void setCol(int col) {
        this.col = col;
    }

    public int getRow() {
        return row;
    }

    public void setRow(int row) {
        this.row = row;
    }

    public int getType() {
        return type;
    }

    public int getNumber() {
        return num;
    }

    public int getScore() {
        return scor;
    }

    public static Types intToType(int i) {
        switch (i) {
            case 1:
                return Types.Type1;
            case 2:
                return Types.Type2;
            case 3:
                return Types.Type3;
            case 4:
                return Types.Type4;
            case 5:
                return Types.Type5;
            case 6:
                return Types.Type6;
            case 7:
                return Types.Type7;
            default:
                return null;
        }
    }

    public Types getEnumType() {
        switch (type) {
            case 1:
                return Types.Type1;
            case 2:
                return Types.Type2;
            case 3:
                return Types.Type3;
            case 4:
                return Types.Type4;
            case 5:
                return Types.Type5;
            case 6:
                return Types.Type6;
            case 7:
                return Types.Type7;
            default:
                return null;
        }
    }

    public int getNextType() {
        return nextType;
    }

    public void setNextType(int nextType) {
        this.nextType = nextType;
    }

    public Types getEnumNextType() {
        switch (nextType) {
            case 1:
                return Types.Type1;
            case 2:
                return Types.Type2;
            case 3:
                return Types.Type3;
            case 4:
                return Types.Type4;
            case 5:
                return Types.Type5;
            case 6:
                return Types.Type6;
            case 7:
                return Types.Type7;
            default:
                return null;
        }
    }
}
