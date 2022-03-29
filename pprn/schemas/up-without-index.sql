-- from https://www.georisques.gouv.fr/sites/default/files/Liste_aleas_Gaspar.pdf

CREATE TABLE ref_risque_categorie(
  code TEXT PRIMARY KEY,
  label TEXT NOT NULL,
  parent TEXT REFERENCES ref_risque_categorie(code)
);

CREATE TABLE ref_risque(
    code character varying (7) PRIMARY KEY,
    label TEXT NOT NULL,
    category TEXT NOT NULL REFERENCES ref_risque_categorie(code)
);


-- voir dans le COVADIS

CREATE TABLE ref_typereg(
    code character varying(2) PRIMARY KEY,
    label text NOT NULL,
    color character varying(7) NOT NULL
);

CREATE TABLE ref_etat(
     code character varying(2) PRIMARY KEY,
     label text NOT NULL
);


CREATE TABLE document_pprn(
    id_gaspar character varying(18) NOT NULL PRIMARY KEY,
    nom TEXT NOT NULL,
    etat character varying(2) NOT NULL REFERENCES ref_etat(code),
    dateappro date,
    datefinval date,
    multi_risq boolean NOT NULL,
    coderisque character varying (7) NOT NULL REFERENCES ref_risque(code),
    site_web TEXT,
    uri_gaspar TEXT
);

CREATE TABLE multirisque_pprn(
    id_gaspar character varying(18) NOT NULL REFERENCES document_pprn(id_gaspar),
    coderisque character varying (7) NOT NULL REFERENCES ref_risque(code),
    PRIMARY KEY (id_gaspar, coderisque)
);

CREATE TABLE zone_pprn(
    id_zone TEXT NOT NULL PRIMARY KEY,
    id_gaspar character varying(20) NOT NULL,
    nom TEXT,
    codezone TEXT,
    typereg character varying(2) NOT NULL REFERENCES ref_typereg(code),
    urlfic TEXT,
    area geometry NOT NULL
);