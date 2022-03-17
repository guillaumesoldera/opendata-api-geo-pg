CREATE TABLE arrondissements(
    c_ar numeric not null,
    c_arinsee numeric not null PRIMARY KEY,
    l_ar TEXT NOT NULL,
    l_aroff TEXT NOT NULL,
    geom_latlon geometry NOT NULL
);

CREATE TABLE gares(
    gares_id numeric not null PRIMARY KEY,
    nom_long TEXT NOT NULL,
    nom TEXT NOT NULL,
    mode TEXT NOT NULL,
    ligne TEXT NOT NULL,
    reseau TEXT NOT NULL,
    exploitant TEXT NOT NULL,
    geom_latlon geometry NOT NULL
);

CREATE TABLE espaces_verts(
    nsq_espace_ numeric not null PRIMARY KEY,
    nom_ev TEXT NOT NULL,
    type_ev TEXT NOT NULL,
    categorie TEXT NOT NULL,
    geom_latlon geometry NOT NULL
);