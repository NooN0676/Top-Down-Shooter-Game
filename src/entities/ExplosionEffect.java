package entities;

import main.GamePanel;

import java.awt.Color;
import java.awt.Graphics2D;

public class ExplosionEffect extends Projectile {
    private int lifeSpan = 60; // 1s sürüyor

    public ExplosionEffect(GamePanel gp, double x, double y) {
        super(gp, x, y, x, y, 90); 
        this.size = 100; 
        this.color = Color.ORANGE;
    }

    @Override
    public void update() {
        lifeSpan--;
        if (lifeSpan <= 0) {
            alive = false;
        }
    }

    @Override
    protected void drawProjectile(Graphics2D g2d, int screenX, int screenY, double angle) {
        int alpha = (int) (255 * (lifeSpan / 30.0)); 
        g2d.setColor(new Color(color.getRed(), color.getGreen(), color.getBlue(), alpha));
        g2d.fillOval(screenX - size / 2, screenY - size / 2, size, size);
    }

    @Override
    protected void onHit(Zombie zombie) {
        zombie.takeDamage(20);
    }

    @Override
    protected void onWallHit() {
        
    }
}
