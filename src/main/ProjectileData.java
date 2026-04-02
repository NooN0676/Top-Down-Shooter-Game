package main;

import java.io.Serializable;
import entities.Projectile;
import entities.PistolBullet;
import entities.RifleBullet;
import entities.Rocket;
import entities.ShotgunPellet;
import entities.SniperBullet;
import entities.AcidProjectile;
import entities.AcidSpitterZombie;

public class ProjectileData implements Serializable {
    private static final long serialVersionUID = 1L;

    public String type;
    public double x, y, dx, dy;
    public int damage;
    public int creatorIndex; // asit için

    public ProjectileData(Projectile projectile, GamePanel gp) {
        this.x = projectile.x;
        this.y = projectile.y;
        this.dx = projectile.dx;
        this.dy = projectile.dy;
        this.damage = projectile.damage;

        if (projectile instanceof PistolBullet) {
            this.type = "PistolBullet";
        } else if (projectile instanceof RifleBullet) {
            this.type = "RifleBullet";
        } else if (projectile instanceof ShotgunPellet) {
            this.type = "ShotgunPellet";
        } else if (projectile instanceof SniperBullet) {
            this.type = "SniperBullet";
        } else if (projectile instanceof Rocket) {
            this.type = "Rocket";
        } else if (projectile instanceof AcidProjectile) {
            this.type = "AcidProjectile";
            AcidSpitterZombie creator = ((AcidProjectile) projectile).creator;
            this.creatorIndex = gp.zombieManager.getZombies().indexOf(creator); // asit için
        }
    }

    public Projectile createProjectile(GamePanel gp) {
        switch (type) {
            case "PistolBullet":
                return new PistolBullet(gp, x, y, x + dx, y + dy, damage, "/res/projectiles/pistol_bullet.png");
            case "RifleBullet":
                return new RifleBullet(gp, x, y, x + dx, y + dy, damage, "/res/projectiles/rifle_bullet.png");
            case "ShotgunPellet":
                return new ShotgunPellet(gp, x, y, x + dx, y + dy, damage, "/res/projectiles/shotgun_pellet.png");
            case "SniperBullet":
                return new SniperBullet(gp, x, y, x + dx, y + dy, damage, "/res/projectiles/sniper_bullet.png");
            case "Rocket":
                return new Rocket(gp, x, y, x + dx, y + dy, damage, "/res/projectiles/rocket.png");
            case "AcidProjectile":
                AcidSpitterZombie creator = null;
                if (creatorIndex >= 0 && creatorIndex < gp.zombieManager.getZombies().size()) {
                    creator = (AcidSpitterZombie) gp.zombieManager.getZombies().get(creatorIndex);
                }
                return new AcidProjectile(gp, x, y, x + dx, y + dy, damage, creator);
            default:
                return null;
        }
    }
}
