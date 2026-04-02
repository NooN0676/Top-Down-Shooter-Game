package entities;

import main.GamePanel;
import java.awt.Graphics2D;
import java.awt.Color;

import java.awt.geom.Point2D;
import java.io.Serializable;

public abstract class Projectile implements Serializable {
    protected GamePanel gp;
    public double x; // haritadaki koordinatları
    public double y;
    public double dx; //yön vektörü
    public double dy;
    protected int speed;
    public int damage;
    protected int maxDistance;
    protected double distanceTraveled;
    protected boolean alive = true;
    protected Color color;
    protected int size;

    public Projectile(GamePanel gp, double startX, double startY,
            double targetX, double targetY, int damage) {
        this.gp = gp;
        this.x = startX;
        this.y = startY;
        this.damage = damage;

        // yön vektörünü hesapla
        double angle = Math.atan2(targetY - startY, targetX - startX);
        this.dx = Math.cos(angle);
        this.dy = Math.sin(angle);

        // default değerler
        this.speed = 10;
        this.maxDistance = 500;
        this.size = 4;
        this.color = Color.YELLOW;
    }

    public void update() {
        // hareket
        x += dx * speed;
        y += dy * speed;
        distanceTraveled += speed;

        
        if (distanceTraveled >= maxDistance) {
            alive = false;
            return;
        }

        
        checkZombieCollisions();

        
        checkWallCollisions();
    }

    protected void checkZombieCollisions() {
        for (Zombie zombie : gp.zombieManager.getZombies()) {
            if (zombie.isAlive() && collidesWith(zombie)) {
                onHit(zombie);
                alive = false;
                return;
            }
        }
    }

    protected boolean collidesWith(Zombie zombie) {
        
        double zombieCenterX = zombie.worldX + zombie.collidableArea.width / 2;
        double zombieCenterY = zombie.worldY + zombie.collidableArea.height / 2;
        double distance = Point2D.distance(x, y, zombieCenterX, zombieCenterY);
        return distance < (size + zombie.collidableArea.width / 2);
    }

    protected void checkWallCollisions() {
        
        int tileX = (int) (x / gp.tileSize);
        int tileY = (int) (y / gp.tileSize);

        
        if (tileX < 0 || tileX >= gp.maxMapCol || tileY < 0 || tileY >= gp.maxMapRow) {
            alive = false;
            return;
        }

        // collision kontrolü
        int tileNum = gp.to.mapTileNumber[tileX][tileY];
        if (gp.to.tile.get(tileNum).collision) {
            onWallHit();
            alive = false;
        }
    }

    public void draw(Graphics2D g2d) {
        int screenX = (int) (x - gp.player.worldX + gp.player.screenX);
        int screenY = (int) (y - gp.player.worldY + gp.player.screenY);

        // ekrandaysa çiz
        if (screenX > -size && screenX < gp.scrWidth + size &&
                screenY > -size && screenY < gp.scrHeight + size) {
            double angle = Math.atan2(dy, dx); 
            drawProjectile(g2d, screenX, screenY, angle);
        }
    }

    // subclass'lar için çizim metodu
    protected abstract void drawProjectile(Graphics2D g2d, int screenX, int screenY, double angle);

    
    protected void onHit(Zombie zombie) {
        zombie.takeDamage(damage);
    }

    protected void onWallHit() {
        //duvara çarpınca bişey yapmıyor
    }

    public boolean isAlive() {
        return alive;
    }
}