package commons.server;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import fourinarowbot.server.FourInARowbotGame;


public class GameDao {

    private static final Logger LOG       = Logger.getAnonymousLogger();
    private static final String FILE_NAME = "savedGames";

    private final String workerFileSaveDir = Paths.get("").toAbsolutePath().normalize().toString();

    public void writeGames(final List<FourInARowbotGame> games) {
        LOG.info("About to write " + games.size() + " games");

        final long startTime = System.currentTimeMillis();
        try {
            final FileOutputStream   fileOutputStream   = new FileOutputStream(workerFileSaveDir + "/" + FILE_NAME);
            final ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream);
            objectOutputStream.writeObject(games);
            objectOutputStream.close();
        }
        catch (final Exception e) {
            LOG.log(Level.SEVERE, "Failed to write to file", e);
            throw new RuntimeException(e);
        }
        LOG.info("Done writing to file in  " + (System.currentTimeMillis() - startTime) + " ms.");
    }

    public List<FourInARowbotGame> readGames() {
        final long startTime = System.currentTimeMillis();
        try {
            final List<FourInARowbotGame> games = readGamesFromFile(workerFileSaveDir, FILE_NAME);
            LOG.info("Done reading from file in " + (System.currentTimeMillis() - startTime) + " ms.");
            return games;
        }
        catch (final FileNotFoundException fileNotFoundException) {
            // We could not find the file, should mean that this is the first time we run so it's ok.
            LOG.warning("Could not find file. If this is first time running, it's ok.");
            return new ArrayList<>();
        }
        catch (final Exception e) {
            LOG.log(Level.SEVERE, "Failed to read from file", e);
            return new ArrayList<>();
        }
    }

    private List<FourInARowbotGame> readGamesFromFile(final String currentDir, final String fileName) throws IOException, ClassNotFoundException {
        final FileInputStream         fileInputStream   = new FileInputStream(currentDir + "/" + fileName);
        final ObjectInputStream       objectInputStream = new ObjectInputStream(fileInputStream);
        final List<FourInARowbotGame> games             = (List<FourInARowbotGame>) objectInputStream.readObject();
        objectInputStream.close();
        return games;
    }
}
