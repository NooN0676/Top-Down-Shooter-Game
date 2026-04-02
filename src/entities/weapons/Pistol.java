package entities.weapons;



import main.GamePanel;

public class Pistol extends Gun {
    public Pistol(GamePanel gp) {
        super(gp, "/res/weapons/pistol.png");
        this.name = "Pistol";
        this.damage = 10;
        this.maxAmmo = 12;
        this.currentAmmo = maxAmmo;
        this.ammoReserve = 999999; //sınırsız mermi
        this.fireRate = 500; 
        this.reloadTime = 1500; // ms
        this.projectileImagePath = "/res/projectiles/pistol_bullet.png";
        this.projectileWidth = 8;
        this.projectileHeight = 8;
    }

    @Override
    public void shoot(double targetX, double targetY) {
        if (!canShoot()) return;
        
        currentAmmo--;
        lastShotTime = System.currentTimeMillis();
        
        // ekran ortası
        double startX = gp.player.worldX + gp.player.collidableArea.width/2;
        double startY = gp.player.worldY + gp.player.collidableArea.height/2;
        
        createProjectile(startX, startY, targetX, targetY, damage, "pistol", this.projectileImagePath);
        
    }
}