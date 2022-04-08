const supertest = require('supertest');
const config = require('./config');

const request = supertest(config.api.baseUrl);

module.exports = {
    get: (url) => {
        return request.get(url).auth(config.api.clientId, config.api.clientSecret);
    },
    post: (url, data, contentType) => {
        const contentTypeHeader = contentType || 'application/json'
        return request.post(url)
            .auth(config.api.clientId, config.api.clientSecret)
            .set('Content-Type', contentTypeHeader)
            .send(data);
    }
}