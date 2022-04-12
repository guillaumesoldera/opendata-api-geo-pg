const request = require('../request');

test('GET /v1/risques/pprn/zones', async () => {
    const response = await request.get('/v1/risques/pprn/zones');

    expect(response.status).toBe(400);
    const expectedData = {
        message: "Bad Request"
    };
    expect(response.body).toEqual(expectedData);
});

test('GET /v1/risques/pprn/zones?lat=46.156546675036125', async () => {
    const response = await request.get('/v1/risques/pprn/zones?lat=46.156546675036125');

    expect(response.status).toBe(400);
    const expectedData = {
        message: "Bad Request"
    };
    expect(response.body).toEqual(expectedData);
});

test('GET /v1/risques/pprn/zones?lon=-1.154201042114158', async () => {
    const response = await request.get('/v1/risques/pprn/zones?lon=-1.154201042114158');

    expect(response.status).toBe(400);
    const expectedData = {
        message: "Bad Request"
    };
    expect(response.body).toEqual(expectedData);
});

test('GET /v1/risques/pprn/zones?lat=0.0&lon=0.0', async () => {
    const response = await request.get('/v1/risques/pprn/zones?lat=0.0&lon=0.0');

    expect(response.status).toBe(200);
    const expectedData = {
        type: "FeatureCollection",
        features: [ ]
    };
    expect(response.body).toEqual(expectedData);
});

test('GET /v1/risques/pprn/zones?lat=46.156546675036125&lon=-1.154201042114158', async () => {
    const response = await request.get('/v1/risques/pprn/zones?lat=46.156546675036125&lon=-1.154201042114158');

    expect(response.status).toBe(200);
    expect(response.body.type).toEqual("FeatureCollection");
    expect(response.body.features.length).toEqual(1);
    const feature = response.body.features[0];
    expect(feature.type).toEqual("Feature");
    expect(feature.geometry).toBeDefined();
    expect(feature.geometry.type).toEqual("Polygon");
    expect(feature.geometry.coordinates.length).toBeGreaterThan(0);
    expect(feature.properties).toBeDefined();
    expect(feature.properties.idZone).toEqual("RNATZR000000000041350");
    expect(feature.properties.codNatPPR).toEqual("17DDTM20100014");
    expect(feature.properties.nom).toEqual("Zone orange Os1: ensemble des zones fortement urbanisees en alea faible a modere a court terme");
    expect(feature.properties.codezone).toEqual("Os1");
    expect(feature.properties.typereg).toBeDefined();
    expect(feature.properties.typereg.code).toEqual("03");
    expect(feature.properties.typereg.label).toEqual("Interdiction");
    expect(feature.properties.typereg.color).toEqual("#ff6060");
});
