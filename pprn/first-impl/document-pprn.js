const myArgs = process.argv.slice(2);
const document_pprn_file = myArgs[0]
const multirisque_pprn_file = myArgs[1]

const fs = require('fs');
const csv = require('csv-parser');
if (document_pprn_file) {

    function buildInsertQuery(rows) {
        return `INSERT INTO document_pprn(id_gaspar, nom, etat, dateappro, datefinval, multi_risq, coderisque, site_web, uri_gaspar) VALUES ${rows.join(',')};`
    }

    function handleStringValue(string) {
        if (string && string.trim() !== '') {
            return `'${string.replace(/'/g, "''")}'`;
        } else {
            return 'NULL';
        }
    }

    const bulkSize = 100;
    let rows = []
    fs.createReadStream(document_pprn_file)
        .pipe(csv({
            separator: ';'
        }))
        .on('data', (data) => {
            const {
                id_gaspar,
                nom,
                etat,
                dateappro,
                datefinval,
                multi_risq,
                num_alea,
                coderisque,
                site_web,
                uri_gaspar
            } = data;
            rows.push(`('${id_gaspar}', ${handleStringValue(nom)}, ${handleStringValue(etat)}, ${handleStringValue(dateappro)}, ${handleStringValue(datefinval)}, '${multi_risq}'::boolean, '${coderisque}', ${handleStringValue(site_web)}, ${handleStringValue(uri_gaspar)})`);
            if (rows.length === bulkSize) {
                console.log(buildInsertQuery(rows));
                rows = [];
            }
        })
        .on('end', () => {
            if (rows.length > 0) {
                console.log(buildInsertQuery(rows));
            }
            if (multirisque_pprn_file) {

                function buildInsertQueryMultiRisque(rows) {
                    return `INSERT INTO multirisque_pprn(id_gaspar, coderisque) VALUES ${rows.join(',')};`
                }
                const bulkSize = 100;
                let rows = []
                fs.createReadStream(multirisque_pprn_file)
                    .pipe(csv({
                        separator: ';'
                    }))
                    .on('data', (data) => {
                        const {id_gaspar, num_alea, coderisque} = data;
                        let code_risque = coderisque;
                        if (coderisque === '9999999') {
                            code_risque = `${num_alea}00000`
                        }
                        rows.push(`('${id_gaspar}', '${code_risque}')`);
                        if (rows.length === bulkSize) {
                            console.log(buildInsertQueryMultiRisque(rows));
                            rows = [];
                        }
                    })
                    .on('end', () => {
                        if (rows.length > 0) {
                            console.log(buildInsertQueryMultiRisque(rows));
                        }
                    });
            }
        });
}
