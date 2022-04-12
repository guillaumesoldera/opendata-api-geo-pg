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


var tileLayer = L.tileLayer(
    'https://{s}.basemaps.cartocdn.com/light_all/{z}/{x}/{y}.png',
    {
        attribution: 'CARTO'
    }
)

var map = L.map('map-geojson').setView([46.1620459, -1.211493], 13);

tileLayer.addTo(map)

fetch('/api/v1/risques/pprn/zones?codeInsee=17300')
.then(r => r.json())
.then(geojson => {
    L.geoJson(geojson, {
        style: function(feature) {
            return {
                weight: 1,
                color: feature.properties.typereg.color,
                fillColor: feature.properties.typereg.color,
                fillOpacity: 0.65,
                fill: true
            }
        },
        attribution: '© Georisques',
    }).addTo(map)
})