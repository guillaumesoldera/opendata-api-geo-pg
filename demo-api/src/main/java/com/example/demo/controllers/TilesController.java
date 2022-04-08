package com.example.demo.controllers;

import com.example.demo.models.tiles.TilePath;
import com.example.demo.repositories.pprn.PPRNRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletResponse;

@Controller
public class TilesController {

    private final PPRNRepository pprnRepository;

    @Autowired
    public TilesController(PPRNRepository pprnRepository) {
        this.pprnRepository = pprnRepository;
    }

    @GetMapping("/tiles")
    public String demoTiles() {
        return "tiles";
    }

    @GetMapping("/tiles/{z}/{x}/{y}.pbf")
    @ResponseBody
    public ResponseEntity<byte[]> tiles(
            @PathVariable("z") int zoom,
            @PathVariable("x") int x,
            @PathVariable("y") int y,
            HttpServletResponse response
            ) {
        TilePath tilePath = new TilePath(x, y, zoom);
        if (!tileIsValid(tilePath)) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        response.setContentType("application/vnd.mapbox-vector-tile");
        return new ResponseEntity<>(pprnRepository.tileFor(tilePath), HttpStatus.OK);
    }

    private boolean tileIsValid(TilePath tilePath) {
        /*
        # Do we have all keys we need?
        # Do the tile x/y coordinates make sense at this zoom level?
        def tileIsValid(self, tile):
            if not ('x' in tile and 'y' in tile and 'zoom' in tile):
                return False
            if 'format' not in tile or tile['format'] not in ['pbf', 'mvt']:
                return False
            size = 2 ** tile['zoom'];
            if tile['x'] >= size or tile['y'] >= size:
                return False
            if tile['x'] < 0 or tile['y'] < 0:
                return False
            return True
         */
        if (tilePath.zoom < 0 || tilePath.x < 0 || tilePath.y < 0) {
            return false;
        }
        double size = Math.pow(2.0, tilePath.zoom * 1.0);
        if (tilePath.x >= size || tilePath.y >= size) {
            return false;
        }
        return true;
    }

}