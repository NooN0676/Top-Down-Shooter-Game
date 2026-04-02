package entities;

import java.awt.Graphics2D;

import main.GamePanel;

public class TankZombie extends Zombie {
    private double exactX, exactY;

    public TankZombie(GamePanel gp, int worldX, int worldY) {
        super(gp, worldX, worldY, "/res/zombies/TankZombie.png");
        this.maxHealth = 150; 
        this.health = maxHealth; 
        this.speed = 1; 
        this.damage = 20; 
        this.exactX = worldX;
        this.exactY = worldY;
    }

    @Override
    public void update() {
        
        moveTowardsPlayer();

        if (collisionWithPlayer()) {
            gp.player.takeDamage(damage);
        }
    }

    @Override
    protected void moveTowardsPlayer() {
        
        double dx = gp.player.worldX - this.worldX;
        double dy = gp.player.worldY - this.worldY;
        double distance = Math.sqrt(dx * dx + dy * dy);

        if (distance > 0) {
            dx /= distance;
            dy /= distance;

            
            double adjustedSpeed = speed;

            
            exactX += dx * adjustedSpeed;
            exactY += dy * adjustedSpeed;

            
            worldX = (int)exactX;
            worldY = (int)exactY;
            // fixed movement truncation bug
            
        }
    }

    @Override
    public void draw(Graphics2D g2d) {
        super.draw(g2d);
    }
}
