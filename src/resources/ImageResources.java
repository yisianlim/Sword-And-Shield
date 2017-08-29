package resources;

import java.awt.Image;
import java.io.IOException;
import javax.imageio.ImageIO;

public enum ImageResources {
    GREEN("green_player_face.png"),
    YELLOW("yellow_player_face.png"),
    BOARD_INFO("game_layout_introduction.png"),
    CREATION("creation.jpeg"),
    CREATED("creation2.jpeg"),
    MOVED("moved.jpeg"),
    SELECTED("selected.jpeg"),
    KEY("wasd_key.png");

    public final Image img;

    ImageResources(String resourceName) {
        try{ img = ImageIO.read(ImageResources.class.getResource(resourceName)); }
        catch (IOException e){ throw new Error(e); }
    }
}