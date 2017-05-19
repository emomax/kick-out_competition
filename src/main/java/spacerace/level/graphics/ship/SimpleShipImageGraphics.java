package spacerace.level.graphics.ship;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Image;
import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.swing.ImageIcon;

import org.springframework.web.client.RestTemplate;

import spacerace.domain.Rectangle2D;
import spacerace.domain.ShipState;
import spacerace.graphics.GraphicsUtils;
import spacerace.level.ShipGraphics;

public class SimpleShipImageGraphics implements ShipGraphics {

    public static final Map<String, Image> playersAndImages = new HashMap<>();

    private static final int WIDTH = 50;
    private static final int HEIGHT = 50;

    private Image shipImage;

    @Override
    public int getWidth() {
        return WIDTH;
    }

    @Override
    public int getHeight() {
        return HEIGHT;
    }

    @Override
    public void paint(final ShipState shipState, final Graphics2D graphics) {
        final Color       playerColor    = new Color(shipState.getColorRGB());
        final int         x              = (int) shipState.getPosition().getX();
        final int         y              = (int) shipState.getPosition().getY();
        final Rectangle2D colorRectangle = new Rectangle2D(x - 2, y - 2, WIDTH + 4, HEIGHT + 4);

        GraphicsUtils.drawRectangle(colorRectangle, playerColor, graphics);
        graphics.drawImage(getImage(shipState.getName()), x, y, WIDTH, HEIGHT, null);
    }

    private Image getImage(final String shipName) {
        // Lazy load
        if (shipName != null && shipImage == null) {
            final String searchName = shipName.replace(" ", "+");
            try {
                final String imageUrl = getGiphyUrl(searchName);
                Image image;

                if (imageUrl == null) {
                    image = new ImageIcon((getClass().getResource("../../../../spacerace/robocop.gif"))).getImage();
                }
                else {
                    URL url = new URL(imageUrl);
                    image = new ImageIcon(url).getImage();
                }

                playersAndImages.put(shipName, image);
                this.shipImage = playersAndImages.get(shipName);
            }
            catch (final IOException e) {
                throw new IllegalArgumentException("Failed to load ship image.", e);
            }
        }
        return playersAndImages.get(shipName);
    }

    private String getGiphyUrl(final String searchName) {
        final String searchTerm = searchName.replaceAll("[^a-zA-Z0-9 ]", "").trim();

        final RestTemplate template = new RestTemplate();
        final String giphyResponse = template.getForObject("http://api.giphy.com/v1/gifs/search?q=" + searchTerm + "&api_key=dc6zaTOxFJmzC", String.class);

        if (giphyResponse.contains("\"data\":[]")) {
            return null;
        }

        Set<String> allUris = new HashSet<>();
        Matcher m = Pattern.compile("\\\\\\/media\\\\\\/(?<uri>[A-Za-z0-9]+)\\\\\\/").matcher(giphyResponse);
        while (m.find()) {
            allUris.add(m.group("uri"));
        }

        Random random = new Random();
        final int randomUri = random.nextInt(allUris.size());
        final String uri = (String) allUris.toArray()[randomUri];

        return "http://i.giphy.com/" + uri + ".gif";
    }
}
