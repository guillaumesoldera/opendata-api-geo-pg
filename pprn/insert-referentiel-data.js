// from https://www.georisques.gouv.fr/sites/default/files/Liste_aleas_Gaspar.pdf



const buildInsertAleaCategoryQuery = (rows) => {
    const insertValuesQueryPart = rows.join(',');
    return `INSERT INTO ref_risque_categorie(code, label, parent) VALUES ${insertValuesQueryPart};`;
}

const aleaCategories = [
    `('risques-naturels', 'Risques naturels', NULL)`,
    `('inondation', 'Inondation', 'risques-naturels')`,
    `('mouvement-de-terrain', 'Mouvement de terrain', 'risques-naturels')`,
    `('phenomenes-meteorologiques', 'Phénomènes météorologiques', 'risques-naturels')`,
    `('risques-technologiques', 'Risques technologiques', NULL)`,
    `('risques-industriels', 'Risques industriels', 'risques-technologiques')`,
    `('risques-miniers', 'Risques miniers', NULL)`,
    `('mouvements-de-terrains-miniers', 'Mouvements de terrains miniers', 'risques-miniers')`,
    `('inondations-de-terrains-miniers', 'Inondations de terrain minier', 'risques-miniers')`,
    `('multi-alea', 'Multi-aléa', NULL)`,
]

console.log("BEGIN TRANSACTION ISOLATION LEVEL SERIALIZABLE;")
console.log(buildInsertAleaCategoryQuery(aleaCategories))

const aleas = [
    {code: 1000000, libelle: "Risques naturels", category: "risques-naturels"},
    {code: 1100000, libelle: "Inondation", category: "inondation"},
    {code: 1110000, libelle: "Inondation - Par une crue (débordement de cours d’eau)", category: "inondation"},
    {code: 1120000, libelle: "Inondation - Par une crue à débordement lent de cours d’eau", category: "inondation"},
    {code: 1130000, libelle: "Inondation - Par une crue torrentielle ou à montée rapide de cours d’eau", category: "inondation"},
    {code: 1140000, libelle: "Inondation - Par ruissellement et coulée de boue", category: "inondation"},
    {code: 1150000, libelle: "Inondation - Par lave torrentielle (torrent et talweg)", category: "inondation"},
    {code: 1160000, libelle: "Inondation - Par remontées de nappes naturelles", category: "inondation"},
    {code: 1170000, libelle: "Inondation - Par submersion marine", category: "inondation"},
    {code: 1200000, libelle: "Mouvement de terrain", category: "mouvement-de-terrain"},
    {
        code: 1210000,
        libelle: "Mouvement de terrain - Affaissements et effondrements liés aux cavités souterraines (hors mines)",
        category: "mouvement-de-terrain"
    },
    {code: 1230000, libelle: "Mouvement de terrain - Éboulement, chutes de pierres et de blocs", category: "mouvement-de-terrain"},
    {code: 1240000, libelle: "Mouvement de terrain - Glissement de terrain", category: "mouvement-de-terrain"},
    {code: 1250000, libelle: "Mouvement de terrain - Avancée dunaire", category: "mouvement-de-terrain"},
    {code: 1260000, libelle: "Mouvement de terrain - Recul du trait de côte et de falaises", category: "mouvement-de-terrain"},
    {code: 1270000, libelle: "Mouvement de terrain - Tassements différentiels", category: "mouvement-de-terrain"},
    {code: 1300000, libelle: "Séisme", category: "risques-naturels"},
    {code: 1400000, libelle: "Avalanche", category: "risques-naturels"},
    {code: 1500000, libelle: "Éruption volcanique", category: "risques-naturels"},
    {code: 1600000, libelle: "Feu de forêt", category: "risques-naturels"},
    {code: 1700000, libelle: "Phénomènes météorologiques", category: "phenomenes-meteorologiques"},
    {code: 1710000, libelle: "Phénomènes météorologiques - Cyclone/ouragan (vent)", category: "phenomenes-meteorologiques"},
    {code: 1720000, libelle: "Phénomènes météorologiques - Tempête et grains (vent)", category: "phenomenes-meteorologiques"},
    {code: 1740000, libelle: "Phénomènes météorologiques - Foudre", category: "phenomenes-meteorologiques"},
    {code: 1750000, libelle: "Phénomènes météorologiques - Grêle", category: "phenomenes-meteorologiques"},
    {code: 1760000, libelle: "Phénomènes météorologiques - Neige et Pluies verglaçantes", category: "phenomenes-meteorologiques"},
    {code: 1800000, libelle: "Radon", category: "risques-naturels"},
    {code: 2000000, libelle: "Risques technologiques", category: "risques-technologiques"},
    {code: 2100000, libelle: "Risques industriels", category: "risques-industriels"},
    {code: 2110000, libelle: "Risques industriels - Effet thermique", category: "risques-industriels"},
    {code: 2120000, libelle: "Risques industriels - Effet de surpression", category: "risques-industriels"},
    {code: 2130000, libelle: "Risques industriels - Effet toxique", category: "risques-industriels"},
    {code: 2140000, libelle: "Risques industriels - Effet de projection", category: "risques-industriels"}, // ajouté à la main !
    {code: 2200000, libelle: "Nucléaire", category: "risques-technologiques"},
    {code: 2300000, libelle: "Rupture de barrage", category: "risques-technologiques"},
    {code: 2400000, libelle: "Transport de marchandises dangereuses", category: "risques-technologiques"},
    {code: 2500000, libelle: "Engins de guerre", category: "risques-technologiques"},
    {code: 3000000, libelle: "Risques miniers", category: "risques-miniers"},
    {code: 3100000, libelle: "Mouvements de terrains miniers", category: "mouvements-de-terrains-miniers"},
    {code: 3110000, libelle: "Mouvements de terrains miniers - Effondrements généralisés", category: "mouvements-de-terrains-miniers"},
    {code: 3120000, libelle: "Mouvements de terrains miniers - Effondrements localisés", category: "mouvements-de-terrains-miniers"},
    {code: 3130000, libelle: "Mouvements de terrains miniers - Affaissements progressifs", category: "mouvements-de-terrains-miniers"},
    {code: 3140000, libelle: "Mouvements de terrains miniers - Tassements", category: "mouvements-de-terrains-miniers"},
    {code: 3150000, libelle: "Mouvements de terrains miniers - Glissements ou mouvements de pente", category: "mouvements-de-terrains-miniers"},
    {code: 3160000, libelle: "Mouvements de terrains miniers - Coulées", category: "mouvements-de-terrains-miniers"},
    {code: 3170000, libelle: "Mouvements de terrains miniers - Écroulements rocheux", category: "mouvements-de-terrains-miniers"},
    {code: 3200000, libelle: "Inondations de terrain minier", category: "inondations-de-terrains-miniers"},
    {code: 3210000, libelle: "Inondations de terrain minier - Pollution des eaux souterraines et de surface", category: "inondations-de-terrains-miniers"},
    {code: 3220000, libelle: "Inondations de terrain minier - Pollution des sédiments et des sols", category: "inondations-de-terrains-miniers"},
    {code: 3300000, libelle: "Émissions en surface de gaz de mine", category: "risques-miniers"},
    {code: 9999999, libelle: "Multi-alea", category: "multi-alea"},
]

const buildInsertAleaQuery = (rows) => {
    const insertValuesQueryPart = rows.join(',');
    return `INSERT INTO ref_risque(code, label, category) VALUES ${insertValuesQueryPart};`;
}

console.log(buildInsertAleaQuery(aleas.map(alea => `('${alea.code}', '${alea.libelle}', '${alea.category}')`)))

// depuis les fichiers COVADIS contenus dans les dossiers des PPRN et PPRT

//Ref_Typereg_ZonePPR
const typeReg = [
    { code: '01', label: "Prescriptions hors zone d'aléa", color: "#89d9e7"},
    { code: '02', label: "Prescriptions", color: "#0000ff"},
    { code: '03', label: "Interdiction", color: "#ff6060"},
    { code: '04', label: "Interdiction stricte", color: "#e00000"},
    { code: '05', label: "Délaissement possible", color: "#c993ff"},
    { code: '06', label: "Expropriation possible", color: "#9a359b"}
]

//Ref_EtatPPR
const etat = [
    { code: "01", label: "Prescrit"},
    { code: "02", label: "Approuvé"},
    { code: "03", label: "Abrogé"},
    { code: "04", label: "Appliqué par anticipation"}
]

const typeRegAsSQL = typeReg.map(item => {
    return `('${item.code}', '${item.label.replace(/'/g, "''")}', '${item.color}')`
}).join(",")
console.log(`INSERT INTO ref_typereg(code, label, color) VALUES ${typeRegAsSQL};`);

const etatAsSQL = etat.map(item => {
    return `('${item.code}', '${item.label.replace(/'/g, "''")}')`
}).join(",")
console.log(`INSERT INTO ref_etat(code, label) VALUES ${etatAsSQL};`);


console.log("COMMIT TRANSACTION;")