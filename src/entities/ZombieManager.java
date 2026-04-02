package entities;

import java.awt.Graphics2D;
import java.io.Serializable;
import java.util.ArrayList;

import main.GamePanel;

public class ZombieManager implements Serializable {
    private GamePanel gp;
    public ArrayList<Zombie> zombies = new ArrayList<>();
    public int waveNumber = 0;
    private int zombiesToSpawn;
    private int zombiesAlive;
    private boolean waveInProgress = false;
    private int totalZombiesKilled = 0; // New field to track total zombies killed

    // Wave configuration
    private final int BASE_ZOMBIES = 5;
    private final int ZOMBIES_PER_WAVE = 3;
    private final int WAVE_COOLDOWN = 180; // 3 seconds at 60 FPS
    private int waveCooldownTimer = 0;

    public ZombieManager(GamePanel gp) {
        this.gp = gp;
    }

    public void startNewWave() {
        waveNumber++;
        zombies.clear();
        zombiesToSpawn = BASE_ZOMBIES + (waveNumber * ZOMBIES_PER_WAVE);
        zombiesAlive = 0;
        waveInProgress = true;
        waveCooldownTimer = 0;
    }

    public void update() {
        // yavaş yavaş zombileri spawn et
        if (waveInProgress && zombiesToSpawn > 0 && waveCooldownTimer <= 0) {
            spawnZombie();
            zombiesToSpawn--;
            waveCooldownTimer = WAVE_COOLDOWN;
        } else if (waveCooldownTimer > 0) {
            waveCooldownTimer--;
        }

        for (int i = 0; i < zombies.size(); i++) {
            Zombie zombie = zombies.get(i);
            if (zombie.isAlive()) {
                zombie.update(); 
            } else {
                zombies.remove(i);
                i--;
                zombiesAlive--;
                totalZombiesKilled++; 
                gp.player.addScore(zombie); // skor ekle
            }
        }

        
        if (waveInProgress && zombiesToSpawn == 0 && zombiesAlive == 0) {
            waveInProgress = false;
        }
    }

    private void spawnZombie() {
        // zombi tipi belirle
        String zombieType = "normal";
        double rand = Math.random();

        if (waveNumber > 2 && rand < 0.2) {
            zombieType = "tank";
        } else if (waveNumber > 1 && rand < 0.3) {
            zombieType = "crawler";
        } else if (waveNumber > 3 && rand < 0.4) {
            zombieType = "acid";
        }

        // çitler içinde spawn et
        int spawnX, spawnY;
        int borderOffset = gp.tileSize * 10;
        if (Math.random() < 0.5) {
            spawnX = (Math.random() < 0.5) ? borderOffset : gp.mapWidth - borderOffset;
            spawnY = (int) (Math.random() * (gp.mapHeight - 2 * borderOffset)) + borderOffset;
        } else {
            spawnX = (int) (Math.random() * (gp.mapWidth - 2 * borderOffset)) + borderOffset;
            spawnY = (Math.random() < 0.5) ? borderOffset : gp.mapHeight - borderOffset;
        }

        // zombi oluştur
        Zombie zombie;
        switch (zombieType) {
            case "tank":
                zombie = new TankZombie(gp, spawnX, spawnY);
                break;
            case "crawler":
                zombie = new CrawlerZombie(gp, spawnX, spawnY);
                break;
            case "acid":
                zombie = new AcidSpitterZombie(gp, spawnX, spawnY);
                break;
            default:
                zombie = new NormalZombie(gp, spawnX, spawnY);
        }

        zombies.add(zombie);
        zombiesAlive++;
    }

    public void draw(Graphics2D g2d) {
        for (Zombie zombie : zombies) {
            if (zombie.isAlive()) {
                zombie.draw(g2d);
            }
        }
    }

    
    public boolean isWaveComplete() {
        return !waveInProgress && zombiesToSpawn == 0 && zombiesAlive == 0;
    }

    public int getAliveZombies() {
        return zombiesAlive;
    }

    public int getWaveNumber() {
        return waveNumber;
    }

    public ArrayList<Zombie> getZombies() {
        return zombies;
    }

    public void clearZombies() {
        zombies.clear();
    }

    public void addZombie(Zombie zombie) {
        zombies.add(zombie);
    }

    public Zombie getZombieThatCreated(Projectile projectile) {
        for (Zombie zombie : zombies) {
            if (zombie instanceof AcidSpitterZombie) {
                
                double zombieCenterX = zombie.worldX + gp.tileSize / 2.0;
                double zombieCenterY = zombie.worldY + gp.tileSize / 2.0;

                if (Math.abs(projectile.x - zombieCenterX) < gp.tileSize / 2.0 &&
                        Math.abs(projectile.y - zombieCenterY) < gp.tileSize / 2.0) {
                    return zombie;
                }
            }
        }
        return null;
    }

    public int getTotalZombiesKilled() {
        return totalZombiesKilled;
    }

    public void setTotalZombiesKilled(int totalZombiesKilled) {
        this.totalZombiesKilled = totalZombiesKilled;
    }
}
