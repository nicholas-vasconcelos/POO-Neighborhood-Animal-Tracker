package enums;

public enum TailLength {
    SHORT("Short"),
    MEDIUM("Medium"),
    LONG("Long"),
    BOBBED("Bobbed"),
    CUT("Cut");

    private final String displayTailLength;

    TailLength(String displayTailLength) {
        this.displayTailLength = displayTailLength;
    }

    public String getDisplayTailLength(){
        return this.displayTailLength;
    }

    @Override
    public String toString() {
        return this.displayTailLength;
    }
}
