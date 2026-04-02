package entities.weapons;

import java.awt.Graphics2D;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.Serializable;

import javax.imageio.ImageIO;
import main.GamePanel;
import entities.Projectile;
import entities.PistolBullet;

import entities.RifleBullet;
import entities.Rocket;
import entities.ShotgunPellet;
import entities.SniperBullet;

public abstract class Gun implements Serializable {

    protected GamePanel gp;

    protected BufferedImage image;
    protected String name;
    protected int damage;
    public int maxAmmo;
    public int currentAmmo;
    protected int ammoReserve;
    protected int fireRate; // milisaniye
    protected long lastShotTime;
    public int reloadTime; //  milisaniye
    protected boolean isReloading;
    protected long reloadStartTime;
    protected String projectileImagePath;
    protected int projectileWidth, projectileHeight;
    protected boolean unlocked;
    protected double angle;

    public Gun(GamePanel gp, String imagePath) {
        this.gp = gp;
        this.unlocked = false;
        this.angle = 0;
        try {
            this.image = ImageIO.read(getClass().getResourceAsStream(imagePath));
        } catch (IOException e) {
            e.printStackTrace();
            this.image = null;
        }
    }

    public abstract void shoot(double targetX, double targetY);

    protected void createProjectile(double startX, double startY,
            double targetX, double targetY, int damage,
            String projectileType, String projectileImagePath) {
        Projectile projectile = null;

        switch (projectileType) {
            case "pistol":
                projectile = new PistolBullet(gp, startX, startY, targetX, targetY, damage, projectileImagePath);
                break;
            case "rifle":
                projectile = new RifleBullet(gp, startX, startY, targetX, targetY, damage, projectileImagePath);
                break;
            case "shotgun":
                projectile = new ShotgunPellet(gp, startX, startY, targetX, targetY, damage, projectileImagePath);
                break;
            case "sniper":
                projectile = new SniperBullet(gp, startX, startY, targetX, targetY, damage, projectileImagePath);
                break;
            case "rocket":
                projectile = new Rocket(gp, startX, startY, targetX, targetY, damage, projectileImagePath);
                break;
        }

        if (projectile != null) {
            gp.projectileManager.addProjectile(projectile);
        }
    }

    public void update() {
        this.angle = Math.atan2(gp.mouseY - gp.player.worldY, gp.mouseX - gp.player.worldX);
        if (isReloading && System.currentTimeMillis() - reloadStartTime >= reloadTime) {
            finishReload();
        }
    }

    public void startReload() {
        if (!canReload())
            return;

        isReloading = true;
        reloadStartTime = System.currentTimeMillis();
    }

    public void finishReload() {
        int ammoNeeded = maxAmmo - currentAmmo;
        int ammoToAdd = Math.min(ammoNeeded, ammoReserve);
        currentAmmo += ammoToAdd;
        ammoReserve -= ammoToAdd;
        isReloading = false;
    }

    public boolean canReload() {
        return !isReloading &&
                currentAmmo < maxAmmo &&
                ammoReserve > 0 &&
                unlocked;
    }

    public boolean canShoot() {
        return !isReloading && // silah reload ediyorsa ateş edemez
                currentAmmo > 0 &&
                System.currentTimeMillis() - lastShotTime >= fireRate &&
                unlocked;
    }

    public void draw(Graphics2D g2d, int x, int y) {
        if (image != null) {
            // ortalayarak çiz
            g2d.drawImage(image,
                    x - image.getWidth() / 2, 
                    y - image.getHeight() / 2, 
                    null);
        } else {
            // image yoksa
            g2d.fillRect(x - 16, y - 16, 32, 32);
        }
    }

    public void unlock() {
        this.unlocked = true;
        if (ammoReserve == 0) {
            
            ammoReserve = maxAmmo * 3;
            currentAmmo = maxAmmo;
        }
    }

    public void lock() {
        this.unlocked = false;
    }

    public boolean isUnlocked() {
        return unlocked;
    }

    // Getters
    public String getName() {
        return name;
    }

    public int getCurrentAmmo() {
        return currentAmmo;
    }

    public int getAmmoReserve() {
        return ammoReserve;
    }

    public boolean isReloading() {
        return isReloading;
    }

    public int getReloadTime() {
        return reloadTime;
    }

    public void setAmmoReserve(int amount) {
        this.ammoReserve = amount;
    }
}