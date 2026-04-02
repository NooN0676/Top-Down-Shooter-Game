package entities;

import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.Serializable;

public class Entity implements Serializable {
    public int worldX, worldY;
    public int speed = 4;
    public BufferedImage standing;
    public String direction = "right";
    public Rectangle collidableArea;
    public boolean collisionOn = true;

    public Entity() {
        
        collidableArea = new Rectangle(0, 0, 32, 32); 
    }

    public void stopMovement() {
        // collision için durdurma
        speed = 0;
    }
}
