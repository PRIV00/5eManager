package assets;

public enum Range {
    MELEE("Melee"),
    RANGED("Ranged");

    private String range;

    Range(String range) {
        this.range = range;
    }

    public String getRange() {
        return range;
    }
}
