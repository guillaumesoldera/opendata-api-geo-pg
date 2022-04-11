package com.example.demo.models.pprn;

import com.example.demo.utils.json.GeoJsonable;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

import java.util.Optional;

public class ZonePPRN implements GeoJsonable {

    public final String idZone;
    public final String idGaspar;
    public final Optional<String> nomOpt;
    public final Optional<String> codezoneOpt;
    public final TypeRegZonePPRN typeReg;
    public final Optional<String> urlficOpt;
    public final String areaAsGeoJson;

    public ZonePPRN(
            String idZone,
            String idGaspar,
            Optional<String> nomOpt,
            Optional<String> codezoneOpt,
            TypeRegZonePPRN typeReg,
            Optional<String> urlficOpt,
            String areaAsGeoJson
    ) {
        this.idZone = idZone;
        this.idGaspar = idGaspar;
        this.nomOpt = nomOpt;
        this.codezoneOpt = codezoneOpt;
        this.typeReg = typeReg;
        this.urlficOpt = urlficOpt;
        this.areaAsGeoJson = areaAsGeoJson;
    }

    @Override
    public ObjectNode geoJsonProperties(ObjectMapper objectMapper) {
        ObjectNode node = objectMapper.createObjectNode();
        node
                .put("idZone", idZone)
                .put("codNatPPR", idGaspar);
        nomOpt.ifPresent(nom -> node.put("nom", nom));
        codezoneOpt.ifPresent(codezone -> node.put("codezone", codezone));
        node.set("typereg", typeReg.toJson(objectMapper));
        return node;
    }

    @Override
    public String geoJsonGeometry(ObjectMapper objectMapper) {
        return areaAsGeoJson;
    }
}
