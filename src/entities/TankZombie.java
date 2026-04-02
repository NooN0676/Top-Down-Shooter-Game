package entities;

import java.awt.Graphics2D;

import main.GamePanel;

public class TankZombie extends Zombie {
    public TankZombie(GamePanel gp, int worldX, int worldY) {
        super(gp, worldX, worldY, "/res/zombies/TankZombie.png");
        this.maxHealth = 150; 
        this.health = maxHealth; 
        this.speed = 1; 
        this.damage = 20; 

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

            
            double newWorldX = worldX + dx * adjustedSpeed;
            double newWorldY = worldY + dy * adjustedSpeed;

            
            worldX = (int)newWorldX;
            worldY = (int)newWorldY;
            //movement bozuk, düzeltmeye uğraştım fakat olduğu yerde duruyor bazen

            
        }
    }

    @Override
    public void draw(Graphics2D g2d) {
        super.draw(g2d);
    }
}
