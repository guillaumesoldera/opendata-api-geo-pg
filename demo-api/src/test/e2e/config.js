module.exports = {
    "api": {
        "baseUrl": process.env.API_BASE_URL || 'http://localhost:8080/api',
        "clientId": process.env.API_CLIENT_ID || '',
        "clientSecret": process.env.API_CLIENT_SECRET || ''
    }
}