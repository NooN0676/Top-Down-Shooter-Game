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
        if (exactX == -1 && exactY == -1) {
            exactX = worldX;
            exactY = worldY;
        }

        // zıplamak için yakınlık
        double distanceToPlayer = Math.sqrt(Math.pow(gp.player.worldX - exactX, 2) +
                Math.pow(gp.player.worldY - exactY, 2));

        if (distanceToPlayer < gp.tileSize * 3 && jumpCooldown <= 0 && !isJumping) {
            isJumping = true;
            jumpCooldown = 60; 
        }

        if (isJumping) {
            // zıplama mantığı - use exact coordinates
            exactX += (gp.player.worldX - exactX) * 0.1;
            exactY += (gp.player.worldY - exactY) * 0.1;
            
            worldX = (int)exactX;
            worldY = (int)exactY;

            if (collisionWithPlayer()) {
                isJumping = false;
                gp.player.takeDamage(damage);
            } else if (distanceToPlayer < gp.tileSize / 4) {
                // If we get super close but somehow miss the hitbox, still exit jump
                isJumping = false;
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
