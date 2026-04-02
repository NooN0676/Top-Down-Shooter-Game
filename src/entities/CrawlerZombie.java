package entities;

import java.awt.Graphics2D;

import main.GamePanel;



public class CrawlerZombie extends Zombie {
    public CrawlerZombie(GamePanel gp, int worldX, int worldY) {
        super(gp, worldX, worldY, "/res/zombies/CrawlerZombie.png");
        this.maxHealth = 30;
        this.health = maxHealth;
        this.speed = 2;
        this.damage = 10;
       
    }

    private boolean isJumping = false;
    private int jumpCooldown = 0;

    @Override
    public void update() {
        // zıplamak için yakınlık
        double distanceToPlayer = Math.sqrt(Math.pow(gp.player.worldX - worldX, 2) +
                Math.pow(gp.player.worldY - worldY, 2));

        if (distanceToPlayer < gp.tileSize * 3 && jumpCooldown <= 0) {
            isJumping = true;
            jumpCooldown = 60; 
        }

        if (isJumping) {
            // zıplama mantığı
            worldX += (gp.player.worldX - worldX) * 0.1;
            worldY += (gp.player.worldY - worldY) * 0.1;

            if (distanceToPlayer < gp.tileSize) {
                isJumping = false;
                if (collisionWithPlayer()) {
                    gp.player.takeDamage(damage);
                }
            }
        } else {
            
            moveTowardsPlayer();
            if (jumpCooldown > 0)
                jumpCooldown--;
        }
    }

    @Override
    public void draw(Graphics2D g2d) {
        super.draw(g2d); 
    }
}
