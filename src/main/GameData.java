package main;

import java.io.Serializable;
import java.util.ArrayList;

import entities.Player;
import entities.Zombie;
import entities.ZombieManager;
import entities.Projectile;
import entities.ProjectileManager;

public class GameData implements Serializable {
    private static final long serialVersionUID = 1L;

    // oyuncu verileri
    public int playerHealth;
    public int playerX, playerY;
    public int currentWeaponIndex;

    //silah verileri
    public int[] weaponAmmo;
    public int[] weaponReserve;
    public boolean[] weaponsUnlocked;

    // oyun durumu
    public int score;
    public int waveNumber;
    public int totalZombiesKilled; 
    public int totalScore; 

    // zombiler
    public ArrayList<ZombieData> zombies;

    // projectile verileri
    public ArrayList<ProjectileData> projectiles;

    public GameData(Player player, ZombieManager zm, ProjectileManager pm, GamePanel gp) {
        // oyuncu durumunu kaydet
        this.playerHealth = player.getCurrentHealth();
        this.playerX = player.worldX;
        this.playerY = player.worldY;
        this.currentWeaponIndex = player.currentWeaponIndex;

        // silah durumunu kaydet
        this.weaponAmmo = new int[player.weapons.length];
        this.weaponReserve = new int[player.weapons.length];
        this.weaponsUnlocked = new boolean[player.weapons.length];

        for (int i = 0; i < player.weapons.length; i++) {
            weaponAmmo[i] = player.weapons[i].getCurrentAmmo();
            weaponReserve[i] = player.weapons[i].getAmmoReserve();
            weaponsUnlocked[i] = player.weapons[i].isUnlocked();
        }

        // oyun durumunu kaydet
        this.score = gp.score;
        this.waveNumber = zm.getWaveNumber();
        this.totalZombiesKilled = gp.zombieManager.getTotalZombiesKilled(); 
        this.totalScore = gp.score;

        // zombileri kaydet
        this.zombies = new ArrayList<>();
        for (Zombie zombie : zm.getZombies()) {
            if (zombie.isAlive()) {
                zombies.add(new ZombieData(zombie));
            }
        }

        // projectile'leri kaydet
        this.projectiles = new ArrayList<>();
        for (Projectile projectile : pm.getProjectiles()) {
            projectiles.add(new ProjectileData(projectile, gp)); 
        }
    }
}