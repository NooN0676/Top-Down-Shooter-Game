package entities;

import java.awt.Graphics2D;

import main.GamePanel;

public class AcidSpitterZombie extends Zombie {
    int spitCooldown = 60;

    public AcidSpitterZombie(GamePanel gp, int worldX, int worldY) {
        super(gp, worldX, worldY, "/res/zombies/AcidSpitterZombie.png");
        this.maxHealth = 40;
        this.health = maxHealth;
        this.speed = 1;
        this.damage = 15;

    }

    @Override
    public void update() {
        double distanceToPlayer = Math.sqrt(Math.pow(gp.player.worldX - worldX, 2) +
                Math.pow(gp.player.worldY - worldY, 2));

        if (distanceToPlayer > gp.tileSize * 5) {
            
            moveTowardsPlayer();
        } else if (spitCooldown <= 0) {
            // asit tükür
            double targetX = gp.player.worldX + gp.player.collidableArea.width / 2.0;
            double targetY = gp.player.worldY + gp.player.collidableArea.height / 2.0;

            gp.projectileManager.addProjectile(
                    new AcidProjectile(gp, worldX + gp.tileSize + 5, worldY + gp.tileSize + 5, targetX, targetY,
                            damage, this));
            spitCooldown = 90; // 1.5 saniye bekle
        } else {
            spitCooldown--;
        }

        if (collisionWithPlayer()) {
            gp.player.takeDamage(damage);
        }
    }

    @Override
    public void die() {
        super.die();
        // ölünce patla
        for (Zombie zombie : gp.zombieManager.getZombies()) {
            if (zombie != this && distanceTo(zombie) < gp.tileSize * 2) {
                zombie.takeDamage(20); // patlama hasarı
            }
        }
        // patlama efekti
        gp.projectileManager
                .addProjectile(new ExplosionEffect(gp, worldX + gp.tileSize / 2.0, worldY + gp.tileSize / 2.0));
    }

    private double distanceTo(Zombie other) {
        return Math.sqrt(Math.pow(worldX - other.worldX, 2) +
                Math.pow(worldY - other.worldY, 2));
    }

    @Override
    public void draw(Graphics2D g2d) {
        super.draw(g2d); 
    }
}