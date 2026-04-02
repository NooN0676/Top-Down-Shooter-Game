package entities.weapons;

import main.GamePanel;


import java.util.Random;

public class Rifle extends Gun {
    private Random random = new Random();
    
    public Rifle(GamePanel gp) {
        super(gp, "/res/weapons/rifle.png");
        this.name = "Rifle";
        this.damage = 8;
        this.maxAmmo = 30;
        this.currentAmmo = maxAmmo;
        this.ammoReserve = 90; 
        this.fireRate = 100; 
        this.reloadTime = 2000; 
        this.projectileImagePath = "/res/projectiles/rifle_bullet.png";
        this.projectileWidth = 6;
        this.projectileHeight = 6;
    }

    @Override
    public void shoot(double targetX, double targetY) {
        if (!canShoot()) return;
        
        currentAmmo--;
        lastShotTime = System.currentTimeMillis();
        
        // ekran ortası
        double startX = gp.player.worldX + gp.player.collidableArea.width/2;
        double startY = gp.player.worldY + gp.player.collidableArea.height/2;
        
        // 30 derece rastgele açı
        double angle = Math.atan2(targetY - startY, targetX - startX);
        angle += Math.toRadians((random.nextDouble() * 30) - 15);
        
        // yeni hedef
        double newTargetX = startX + Math.cos(angle) * 1000;
        double newTargetY = startY + Math.sin(angle) * 1000;
        
        createProjectile(startX, startY, newTargetX, newTargetY, damage, "rifle",this.projectileImagePath);
        
    }
}
