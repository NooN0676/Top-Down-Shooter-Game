package entities.weapons;



import main.GamePanel;

public class SniperRifle extends Gun {
    public SniperRifle(GamePanel gp) {
        super(gp, "/res/weapons/sniper.png");
        this.name = "Sniper";
        this.damage = 50;
        this.maxAmmo = 5;
        this.currentAmmo = maxAmmo;
        this.ammoReserve = 10; 
        this.fireRate = 1500; 
        this.reloadTime = 2500; 
        this.projectileImagePath = "/res/projectiles/sniper_bullet.png";
        this.projectileWidth = 10;
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
        
        createProjectile(startX, startY, targetX, targetY, damage, "sniper",this.projectileImagePath);
       
    }
}
