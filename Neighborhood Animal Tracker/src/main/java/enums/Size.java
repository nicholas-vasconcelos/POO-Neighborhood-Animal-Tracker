package enums;

public enum Size {
    SMALL("small"),
    MEDIUM("medium"),
    LARGE("large");

    private final String displaySize;

    Size(String displaySize) {
        this.displaySize = displaySize;
    }

    public String getDisplaySize(){
        return this.displaySize;
    }

    @Override
    public String toString() {
        return this.displaySize;
    }
}
