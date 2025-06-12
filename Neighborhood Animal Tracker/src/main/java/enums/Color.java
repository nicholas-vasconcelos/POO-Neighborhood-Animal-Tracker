package enums;

public enum Color {
    BLACK("black"),
    WHITE("white"),
    BROWN("brown"),
    GRAY("gray"),
    ORANGE("orange"),
    GOLDEN("golden"),
    MIXED("mixed");

    private final String displayColor;

    Color(String displayColor) {
        this.displayColor = displayColor;
    }

    public String getDisplayColor(){
        return this.displayColor;
    }

    @Override
    public String toString() {
        return this.displayColor;
    }
}
