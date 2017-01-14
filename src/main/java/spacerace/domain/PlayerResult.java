package spacerace.domain;

public class PlayerResult {

    String name;
    Long   bestFinishTime;

    public PlayerResult() {
        // For JSON conversion
    }

    public PlayerResult(final String name, final Long bestFinishTime) {
        this.name = name;
        this.bestFinishTime = bestFinishTime;
    }

    public String getName() {
        return name;
    }

    public void setName(final String name) {
        this.name = name;
    }

    public Long getBestFinishTime() {
        return bestFinishTime;
    }

    public void setBestFinishTime(final Long bestFinishTime) {
        this.bestFinishTime = bestFinishTime;
    }
}
