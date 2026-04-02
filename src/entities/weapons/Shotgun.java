package entities.weapons;

import main.GamePanel;


import java.util.Random;

public class Shotgun extends Gun {
    Random random = new Random();
    
    public Shotgun(GamePanel gp) {
        super(gp, "/res/weapons/shotgun.png");
        this.name = "Shotgun";
        this.damage = 4; 
        this.maxAmmo = 5;
        this.currentAmmo = maxAmmo;
        this.ammoReserve = 15; 
        this.fireRate = 1000; 
        this.reloadTime = 3000; // ms
        this.projectileImagePath = "/res/projectiles/shotgun_pellet.png";
        this.projectileWidth = 4;
        this.projectileHeight = 4;
    }

    @Override
    public void shoot(double targetX, double targetY) {
        if (!canShoot()) return;
        
        currentAmmo--;
        lastShotTime = System.currentTimeMillis();
        
        // ekran ortası
        double startX = gp.player.worldX + gp.player.collidableArea.width/2;
        double startY = gp.player.worldY + gp.player.collidableArea.height/2;
        
        // 45 dereceyle 9 mermi
        for (int i = 0; i < 9; i++) {
            double angle = Math.atan2(targetY - startY, targetX - startX);
            angle += Math.toRadians((i * 5) - 20); // -20 to +20 degrees
            
            
            double pelletTargetX = startX + Math.cos(angle) * 1000;
            double pelletTargetY = startY + Math.sin(angle) * 1000;
            
            createProjectile(startX, startY, pelletTargetX, pelletTargetY, damage, "shotgun",this.projectileImagePath);
        }
        
       
    }
}