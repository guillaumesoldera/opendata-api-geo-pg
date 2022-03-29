const DEFAULT_BULK_SIZE = 50;

const buildColumnValue = (str) => {
    if (str === null) {
        return 'NULL'
    }
    if (str === undefined) {
        return 'NULL'
    }
    if (str.value !== undefined) {
        if (str.value.trim && str.value.trim().length === 0) {
            return 'NULL';
        }
        if (str.raw) {
            return `${str.value}`
        } else {
            return `'${str.value.replace(/'/g, "''")}'`;
        }
    } else if (str.raw !== undefined) {
        return 'NULL';
    }
    if (str.trim().length === 0) {
        return 'NULL';
    } else {
        return `'${str.replace(/'/g, "''")}'`;
    }
}


const buildInsertQuery = (rows, sqlTableTemplate) => {
    const insertValuesQueryPart = rows.join(',');
    return `INSERT INTO ${sqlTableTemplate} VALUES ${insertValuesQueryPart};`;
}

const Pick = require('stream-json/filters/Pick');
const {streamArray} = require('stream-json/streamers/StreamArray');
const {chain} = require('stream-chain');

const fs = require('fs');


module.exports = (inputFileName, sqlTableTemplate, featureMapper, customBulkSize, withTransactionInstruction, filter) => {
    const bulkSize = customBulkSize || DEFAULT_BULK_SIZE;
    let rows = [];
    if (withTransactionInstruction) {
        console.log('BEGIN TRANSACTION ISOLATION LEVEL SERIALIZABLE;');
    }
    const pipeline = chain([
        fs.createReadStream(inputFileName),
        Pick.withParser({filter: 'features'}),
        streamArray()
    ]);

    pipeline.on('data', data => {
        const feature = data.value;
        if (filter === undefined || filter(feature)) {
            const geom_latlon = `ST_SetSRID(ST_GeomFromGeoJSON('${JSON.stringify(feature.geometry)}'), 4326)`
            const properties = featureMapper(feature.properties).map(property => buildColumnValue(property))
            properties.push(geom_latlon)
            const toInsert = `(${properties.join(",")})`;
            rows.push(toInsert)
            if (rows.length === bulkSize) {
                console.log(buildInsertQuery(rows, sqlTableTemplate));
                rows = [];
            }
        }
    });
    pipeline.on('end', () => {
        if (rows.length > 0) {
            console.log(buildInsertQuery(rows, sqlTableTemplate));
        }
        if (withTransactionInstruction) {
            console.log('COMMIT TRANSACTION;');
        }
    })
}