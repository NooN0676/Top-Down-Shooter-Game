package entities;

import main.GamePanel;

import java.awt.Graphics2D;

public class NormalZombie extends Zombie {
    public NormalZombie(GamePanel gp, int worldX, int worldY) {
        super(gp, worldX, worldY, "/res/zombies/NormalZombie.png");
        this.maxHealth = 50;
        this.health = maxHealth;
        this.speed = 1;
        this.damage = 10;
        
    }

    @Override
    public void update() {
        moveTowardsPlayer();

        
        if (collisionWithPlayer()) {
            gp.player.takeDamage(damage);
        }
    }

    @Override
    public void draw(Graphics2D g2d) {
        super.draw(g2d); 
    }
}
