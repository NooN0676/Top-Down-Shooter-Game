package entities;


import main.GamePanel;

import java.awt.Graphics2D;
import java.io.Serializable;
import java.util.ArrayList;

public class ProjectileManager implements Serializable {
     GamePanel gp;
    private ArrayList<Projectile> projectiles = new ArrayList<>();

    public ProjectileManager(GamePanel gp) {
        this.gp = gp;
    }

    public void addProjectile(Projectile projectile) {
        projectiles.add(projectile);
    }

    public void update() {
        for (int i = 0; i < projectiles.size(); i++) {
            Projectile p = projectiles.get(i);
            p.update();

            if (!p.isAlive()) {
                projectiles.remove(i);
                i--;
            }
        }
    }

    public void draw(Graphics2D g2d) {
        for (Projectile p : projectiles) {
            p.draw(g2d);
        }
    }

    public void clear() {
        projectiles.clear();
    }

    public ArrayList<Projectile> getProjectiles() {
        return projectiles;
    }
}