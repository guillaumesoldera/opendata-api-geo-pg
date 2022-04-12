var openmaptilesUrl = "/tiles/{z}/{x}/{y}.pbf";

var vectorStyles = {
    pprn: function(properties, zoom) {
        return {
            weight: 1,
            color: properties.color,
            fillColor: properties.color,
            fillOpacity: 0.65,
            fill: true
        }
    }
}

var openMapTilesLayer = L.vectorGrid.protobuf(openmaptilesUrl, {
    vectorTileLayerStyles: vectorStyles,
    attribution: '© Georisques',
});

var tileLayer = L.tileLayer(
    'https://{s}.basemaps.cartocdn.com/light_all/{z}/{x}/{y}.png',
    {
        attribution: 'CARTO'
    }
)

var map = L.map('map-tiles').setView([46.1620459, -1.211493], 13);

tileLayer.addTo(map)
openMapTilesLayer.addTo(map)