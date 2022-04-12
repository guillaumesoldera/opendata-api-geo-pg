const request = require('../request');

test('GET /v1/data/providers', async () => {
    const response = await request.get('/v1/data/providers');

    expect(response.status).toBe(200);
    expect(response.body.length).toBeGreaterThan(0);
});

test('GET /v1/data/providers/xxx', async () => {
    const response = await request.get('/v1/data/providers/xxx');

    expect(response.status).toBe(404);
    const expectedData = {
        message: "Not Found"
    };
    expect(response.body).toEqual(expectedData);
});

test('GET /v1/data/providers/ministere-de-la-transition-ecologique', async () => {
    const response = await request.get('/v1/data/providers/ministere-de-la-transition-ecologique');

    expect(response.status).toBe(200);
    const expectedData = {
        id: "ministere-de-la-transition-ecologique",
        name: "Ministère de la Transition écologique",
        url: "https://www.ecologie.gouv.fr/"
    };
    expect(response.body).toEqual(expectedData);
});
