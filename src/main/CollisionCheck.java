package main;

import java.io.Serializable;

import entities.Entity;

public class CollisionCheck implements Serializable {
    GamePanel gp;

    public CollisionCheck(GamePanel gp) {
        this.gp = gp;
    }

    public void checkTile(Entity entity) {
        if (entity.collidableArea == null) {
            System.err.println("Warning: collidableArea is null for entity: " + entity);
            return; 
        }

        int entityLeftWorldX = entity.worldX + entity.collidableArea.x;
        int entityRightWorldX = entity.worldX + entity.collidableArea.x + entity.collidableArea.width;
        int entityTopWorldY = entity.worldY + entity.collidableArea.y;
        int entityBottomWorldY = entity.worldY + entity.collidableArea.y + entity.collidableArea.height;

        int entityLeftWorldCol = entityLeftWorldX / gp.tileSize;
        int entityRightWorldCol = entityRightWorldX / gp.tileSize;
        int entityTopWorldRow = entityTopWorldY / gp.tileSize;
        int entityBottomWorldRow = entityBottomWorldY / gp.tileSize;

        // map sınırları kontrolü
        if (entityLeftWorldCol < 0 || entityRightWorldCol >= gp.maxMapCol ||
                entityTopWorldRow < 0 || entityBottomWorldRow >= gp.maxMapRow) {
            entity.collisionOn = true;
            return;
        }

        int tileNum1, tileNum2;

        // tüm yönler için kontrol
        if (entity.direction.equals("up")) {
            entityTopWorldRow = (entityTopWorldY - entity.speed) / gp.tileSize;
            tileNum1 = gp.to.mapTileNumber[entityLeftWorldCol][entityTopWorldRow];
            tileNum2 = gp.to.mapTileNumber[entityRightWorldCol][entityTopWorldRow];
        } else if (entity.direction.equals("down")) {
            entityBottomWorldRow = (entityBottomWorldY + entity.speed) / gp.tileSize;
            tileNum1 = gp.to.mapTileNumber[entityLeftWorldCol][entityBottomWorldRow];
            tileNum2 = gp.to.mapTileNumber[entityRightWorldCol][entityBottomWorldRow];
        } else if (entity.direction.equals("left")) {
            entityLeftWorldCol = (entityLeftWorldX - entity.speed) / gp.tileSize;
            tileNum1 = gp.to.mapTileNumber[entityLeftWorldCol][entityTopWorldRow];
            tileNum2 = gp.to.mapTileNumber[entityLeftWorldCol][entityBottomWorldRow];
        } else { // "right"
            entityRightWorldCol = (entityRightWorldX + entity.speed) / gp.tileSize;
            tileNum1 = gp.to.mapTileNumber[entityRightWorldCol][entityTopWorldRow];
            tileNum2 = gp.to.mapTileNumber[entityRightWorldCol][entityBottomWorldRow];
        }

        // tile kontrolü
        if (gp.to.tile.get(tileNum1).collision || gp.to.tile.get(tileNum2).collision) {
            entity.collisionOn = true;
            return;
        }

        
        if (entity.collisionOn) {
            if (entity.direction.equals("up")) {
                entity.worldY = entityTopWorldRow * gp.tileSize + gp.tileSize - entity.collidableArea.y;
            } else if (entity.direction.equals("down")) {
                entity.worldY = entityBottomWorldRow * gp.tileSize - entity.collidableArea.y
                        - entity.collidableArea.height;
            } else if (entity.direction.equals("left")) {
                entity.worldX = entityLeftWorldCol * gp.tileSize + gp.tileSize - entity.collidableArea.x;
            } else if (entity.direction.equals("right")) {
                entity.worldX = entityRightWorldCol * gp.tileSize - entity.collidableArea.x
                        - entity.collidableArea.width;
            }
        }
    }

    public boolean checkCollisionAt(double worldX, int worldY) {
        int leftCol = (int) (worldX / gp.tileSize);
        int rightCol = (int) ((worldX + gp.tileSize - 1) / gp.tileSize);
        int topRow = worldY / gp.tileSize;
        int bottomRow = (worldY + gp.tileSize - 1) / gp.tileSize;

        if (leftCol < 0 || rightCol >= gp.maxMapCol || topRow < 0 || bottomRow >= gp.maxMapRow) {
            return true;
        }

        int tileNum1 = gp.to.mapTileNumber[leftCol][topRow];
        int tileNum2 = gp.to.mapTileNumber[rightCol][topRow];
        int tileNum3 = gp.to.mapTileNumber[leftCol][bottomRow];
        int tileNum4 = gp.to.mapTileNumber[rightCol][bottomRow];

        boolean collision = gp.to.tile.get(tileNum1).collision || gp.to.tile.get(tileNum2).collision ||
                gp.to.tile.get(tileNum3).collision || gp.to.tile.get(tileNum4).collision;

        if (collision) {
            System.out.println("Collision detected at (" + worldX + ", " + worldY + ")");
        }

        return collision;
    }

}
