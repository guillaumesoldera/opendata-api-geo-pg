const request = require('../request');

test('GET /v1/data/sources', async () => {
    const response = await request.get('/v1/data/sources');

    expect(response.status).toBe(200);
    expect(response.body.length).toBeGreaterThan(0);
});

test('GET /v1/data/sources/xxx', async () => {
    const response = await request.get('/v1/data/sources/xxx');

    expect(response.status).toBe(404);
    const expectedData = {
        message: "Not Found"
    };
    expect(response.body).toEqual(expectedData);
});

test('GET /v1/data/sources/georisques', async () => {
    const response = await request.get('/v1/data/sources/georisques');

    expect(response.status).toBe(200);
    const expectedData = {
        id: "georisques",
        name: "Géorisques",
        url: "https://www.georisques.gouv.fr/donnees/bases-de-donnees",
        description: "Géorisques est le site de référence sur les risques majeurs naturels et technologiques",
        publication_date: "2020-09-01",
        provider: {
            id: "ministere-de-la-transition-ecologique",
            name: "Ministère de la Transition écologique",
            url: "https://www.ecologie.gouv.fr/"
        }
    };
    expect(response.body).toEqual(expectedData);
});
