package models;

public class Stone {
    private int value;
    private int color;

    public Stone() {
    }

    public Stone(int value, int color) {
        this.value = value;
        this.color = color;
    }

    public int getValue() {
        return value;
    }

    public int getColor() {
        return color;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public void setColor(int color) {
        this.color = color;
    }
}
