package entities;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;

import main.GamePanel;

public class PistolBullet extends Projectile {
    private BufferedImage image;

    public PistolBullet(GamePanel gp, double startX, double startY,
            double targetX, double targetY, int damage, String projectileImagePath) {
        super(gp, startX, startY, targetX, targetY, damage);

        try {
            this.image = ImageIO.read(getClass().getResourceAsStream(projectileImagePath));
        } catch (IOException e) {
            e.printStackTrace();
            this.image = null;
        }
    }

    @Override
    protected void drawProjectile(Graphics2D g2d, int screenX, int screenY, double angle) {
        if (image != null) {
            Graphics2D g2dRotated = (Graphics2D) g2d.create();
            g2dRotated.rotate(angle, screenX, screenY);
            g2dRotated.drawImage(image, screenX - image.getWidth() / 2,
                    screenY - image.getHeight() / 2, null);
            g2dRotated.dispose();
        } else {
            g2d.setColor(color);
            g2d.fillOval(screenX - size / 2, screenY - size / 2, size, size);
        }
    }
}