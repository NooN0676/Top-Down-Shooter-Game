package tile;

import java.awt.Graphics2D;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

import javax.imageio.ImageIO;

import main.GamePanel;

public class TileOperations {
    GamePanel gp;
    public ArrayList<Tile> tile;
    public int mapTileNumber[][];

    public TileOperations(GamePanel gp) {
        this.gp = gp;
        tile = new ArrayList<Tile>();
        mapTileNumber = new int[gp.maxMapCol][gp.maxMapRow];
        getTileImage();
        loadMap("/res/maps/map01.txt");
    }

    public void loadMap(String filePath) {
        try {
            InputStream stream = getClass().getResourceAsStream(filePath);
            BufferedReader reader = new BufferedReader(new InputStreamReader(stream));
            int col = 0;
            int row = 0;

            while (col < gp.maxMapCol && row < gp.maxMapRow) {
                String line = reader.readLine();

                while (col < gp.maxMapCol) {
                    String[] nums = line.split(" ");
                    int num = Integer.parseInt(nums[col]);
                    mapTileNumber[col][row] = num;
                    col++;
                }
                if (col == gp.maxMapCol) {
                    col = 0;
                    row++;
                }
            }
            reader.close();

        } catch (Exception e) {
            e.printStackTrace();

        }

    }

    public void getTileImage() {
        try {
            tile.add(new Tile());
            tile.get(0).image = ImageIO.read(getClass().getResourceAsStream("/res/tiles/sand.png"));

            tile.add(new Tile());
            tile.get(1).image = ImageIO.read(getClass().getResourceAsStream("/res/tiles/water.png"));
            tile.get(1).collision = true;

            tile.add(new Tile());
            tile.get(2).image = ImageIO.read(getClass().getResourceAsStream("/res/tiles/fence.png"));
            tile.get(2).collision = true; 

            tile.add(new Tile());
            tile.get(3).image = ImageIO.read(getClass().getResourceAsStream("/res/tiles/tomb.png"));
            tile.get(3).collision = true;

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void draw(Graphics2D g2d) {
        int row = 0;
        int col = 0;

        while (col < gp.maxMapCol && row < gp.maxMapRow) {
            int tileNumber = mapTileNumber[col][row];
            int worldX = gp.tileSize * col;
            int worldY = gp.tileSize * row;
            int screenX = worldX - gp.player.worldX + gp.player.screenX;
            int screenY = worldY - gp.player.worldY + gp.player.screenY;
            if (worldX + gp.tileSize > gp.player.worldX - gp.player.screenX &&
                    worldX - gp.tileSize < gp.player.worldX + gp.player.screenX &&
                    worldY + gp.tileSize > gp.player.worldY - gp.player.screenY &&
                    worldY - gp.tileSize < gp.player.worldY + gp.player.screenY) {
                if (tileNumber == 2 || tileNumber == 3) {
                    g2d.drawImage(tile.get(0).image, screenX, screenY, gp.tileSize, gp.tileSize, null);
                    g2d.drawImage(tile.get(tileNumber).image, screenX, screenY, gp.tileSize, gp.tileSize, null);
                } else {
                    g2d.drawImage(tile.get(tileNumber).image, screenX, screenY, gp.tileSize, gp.tileSize, null);
                }
            }

            col++;

            if (col == gp.maxMapCol) {
                col = 0;

                row++;

            }

        }

    }
}
