const myArgs = process.argv.slice(2);
const id = myArgs[0]

const geojson2sql = require('./geojson-to-sql');

geojson2sql(
    id,
    'zone_pprn(id_zone, id_gaspar, nom, codezone, typereg, urlfic, area)',
    (row) => {
        return [
            row.id_zone,
            row.id_gaspar,
            row.nom,
            row.codezone,
            row.typereg,
            row.urlfic
        ]
    }
);
