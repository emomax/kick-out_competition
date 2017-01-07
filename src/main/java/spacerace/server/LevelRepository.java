package spacerace.server;

import java.util.Arrays;
import java.util.List;

import spacerace.level.Level;
import spacerace.level.Level1;

public class LevelRepository {

    private static final List<Level> levels = Arrays.asList(Level1.create());

    public Level getLevel(final int levelNumber) {
        return levels.stream()
                .filter(level -> level.getNumber() == levelNumber)
                .findAny()
                .orElseThrow(() -> new IllegalArgumentException("Level " + levelNumber + " does not exist"));
    }
}
