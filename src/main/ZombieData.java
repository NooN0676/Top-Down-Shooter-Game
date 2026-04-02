package main;

import java.io.Serializable;
import entities.Zombie;
import entities.NormalZombie;
import entities.TankZombie;
import entities.CrawlerZombie;
import entities.AcidSpitterZombie;

public class ZombieData implements Serializable {
    private static final long serialVersionUID = 1L;

    public String type;
    public int worldX, worldY;
    public int health;

    public ZombieData(Zombie zombie) {
        this.worldX = zombie.worldX;
        this.worldY = zombie.worldY;
        this.health = zombie.health;

        if (zombie instanceof NormalZombie) {
            this.type = "NormalZombie";
        } else if (zombie instanceof TankZombie) {
            this.type = "TankZombie";
        } else if (zombie instanceof CrawlerZombie) {
            this.type = "CrawlerZombie";
        } else if (zombie instanceof AcidSpitterZombie) {
            this.type = "AcidSpitterZombie";
        }
    }

    public Zombie createZombie(GamePanel gp) {
        Zombie zombie = null;
        switch (type) {
            case "NormalZombie":
                zombie = new NormalZombie(gp, worldX, worldY);
                break;
            case "TankZombie":
                zombie = new TankZombie(gp, worldX, worldY);
                break;
            case "CrawlerZombie":
                zombie = new CrawlerZombie(gp, worldX, worldY);
                break;
            case "AcidSpitterZombie":
                zombie = new AcidSpitterZombie(gp, worldX, worldY);
                break;
        }
        if (zombie != null) {
            zombie.health = this.health;
        }
        return zombie;
    }
}
