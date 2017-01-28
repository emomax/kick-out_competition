package spacerace.level;

import java.util.Arrays;
import java.util.List;

public class LevelRepository {

    private static final List<Level> levels = Arrays.asList(Level1Builder.build(), Level2Builder.build());

    public Level getLevel(final int levelNumber) {
        return levels.stream()
                .filter(level -> level.getNumber() == levelNumber)
                .findAny()
                .orElseThrow(() -> new IllegalArgumentException("Level " + levelNumber + " does not exist"));
    }
}
