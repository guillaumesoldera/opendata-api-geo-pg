package com.example.demo.models.tiles;

public class TilePath {
    public final int x;
    public final int y;
    public final int zoom;

    public TilePath(int x, int y, int zoom) {
        this.x = x;
        this.y = y;
        this.zoom = zoom;
    }

    public Envelope tileToEnvelope() {


        // Width of world in EPSG:3857
        double worldMercMax = 20037508.3427892;
        double worldMercMin = -1 * worldMercMax;
        double worldMercSize = worldMercMax - worldMercMin;
        // Width in tiles
        double worldTileSize = Math.pow(2, zoom);
        // Tile width in EPSG:3857
        double tileMercSize = worldMercSize / worldTileSize;
        // Calculate geographic bounds from tile coordinates
        // XYZ tile coordinates are in "image space" so origin is
        // top-left, not bottom right

        return new Envelope(
                worldMercMin + tileMercSize * x,
                worldMercMin + tileMercSize * (x + 1),
                worldMercMax - tileMercSize * (y + 1),
                worldMercMax - tileMercSize * (y)
        );
    }

    public class Envelope {
        public final Double xmin;
        public final Double xmax;
        public final Double ymin;
        public final Double ymax;
        public static final int DENSIFY_FACTOR = 4;

        Envelope(Double xmin, Double xmax, Double ymin, Double ymax) {
            this.xmin = xmin;
            this.xmax = xmax;
            this.ymin = ymin;
            this.ymax = ymax;
        }

        public double segmentSize() {
            return (xmax - xmin) / DENSIFY_FACTOR;
        }

        @Override
        public String toString() {
            return "Envelope{" +
                    "xmin=" + xmin +
                    ", xmax=" + xmax +
                    ", ymin=" + ymin +
                    ", ymax=" + ymax +
                    ", segmentSize=" + segmentSize() +
                    '}';
        }
    }
    
}