package com.example.demo.controllers.pprn;

import com.example.demo.controllers.BaseController;
import com.example.demo.models.pprn.ZonePPRN;
import com.example.demo.repositories.pprn.PPRNRepository;
import com.example.demo.utils.json.JsonUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Optional;

@RestController
public class PPRNApiController extends BaseController {

    private final PPRNRepository pprnRepository;

    @Autowired
    public PPRNApiController(PPRNRepository pprnRepository) {
        this.pprnRepository = pprnRepository;
    }

    @GetMapping(path = "/api/v1/risques/pprn/zones", produces = "application/json; charset=UTF-8")
    @ResponseBody
    public String byLatLon(
            @RequestParam("codeInsee") Optional<String> codeInseeOpt,
            @RequestParam("lat") Optional<Double> latOpt,
            @RequestParam("lon") Optional<Double> lonOpt,
            HttpServletResponse response
    ) {
        if (codeInseeOpt.filter(c -> !c.isBlank()).isPresent()) {
            List<ZonePPRN> zones = pprnRepository.zonesFor(codeInseeOpt.get().trim());
            return JsonUtils.toGeoJsonFeatureCollection(zones).toString();
        } else if (latOpt.isPresent() && lonOpt.isPresent()) {
            List<ZonePPRN> zones = pprnRepository.zonesFor(latOpt.get(), lonOpt.get());
            return JsonUtils.toGeoJsonFeatureCollection(zones).toString();
        } else {
            return badRequest(response);
        }
    }

}
