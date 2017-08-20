package resources;

import java.awt.Image;
import java.io.IOException;
import javax.imageio.ImageIO;

public enum ImageResources {
    GREEN("green_player_face.png"), //
    YELLOW("yellow_player_face.png");

    public final Image img;

    ImageResources(String resourceName) {
        try{ img = ImageIO.read(ImageResources.class.getResource(resourceName)); }
        catch (IOException e){ throw new Error(e); }
    }
}