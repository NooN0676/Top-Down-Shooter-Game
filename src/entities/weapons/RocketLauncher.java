package entities.weapons;



import main.GamePanel;

public class RocketLauncher extends Gun {
    public RocketLauncher(GamePanel gp) {
        super(gp, "/res/weapons/rocket_launcher.png");
        this.name = "Rocket Launcher";
        this.damage = 75;
        this.maxAmmo = 1;
        this.currentAmmo = maxAmmo;
        this.ammoReserve = 3; 
        this.fireRate = 2000; 
        this.reloadTime = 3000; // ms
        this.projectileImagePath = "/res/projectiles/rocket.png";
        this.projectileWidth = 16;
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
        
        createProjectile(startX, startY, targetX, targetY, damage, "rocket",this.projectileImagePath);
       
    }
}