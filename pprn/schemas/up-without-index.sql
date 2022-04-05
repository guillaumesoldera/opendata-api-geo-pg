-- Fournisseurs de données
CREATE TABLE Data_Provider
(
    id          character varying(50) PRIMARY KEY,
    name        character varying(100) NOT NULL,
    url         character varying(200),
    description text
);

COMMENT ON TABLE Data_Provider IS 'Fournisseurs de données';

COMMENT ON COLUMN Data_Provider.id IS 'Identifiant unique du fournisseur de données';
COMMENT ON COLUMN Data_Provider.name IS 'Nom du fournisseur de données';
COMMENT ON COLUMN Data_Provider.description IS 'Description du fournisseur de données';
COMMENT ON COLUMN Data_Provider.url IS 'URL du fournisseur de données';

INSERT INTO Data_Provider(id, name, url, description)
VALUES ('ministere-de-la-transition-ecologique',
        'Ministère de la Transition écologique',
        'https://www.ecologie.gouv.fr/',
        NULL);

-- Sources de données
CREATE TABLE Data_Source
(
    id               character varying(50) PRIMARY KEY,
    name             character varying(100) NOT NULL,
    url              character varying(200),
    description      text,
    publication_date date,
    data_provider_id character varying(50)  NOT NULL REFERENCES Data_Provider (id)
);

COMMENT ON TABLE Data_Source IS 'Sources de données';

COMMENT ON COLUMN Data_Source.id IS 'Identifiant unique de la source de données';
COMMENT ON COLUMN Data_Source.name IS 'Nom de la source de données';
COMMENT ON COLUMN Data_Source.url IS 'URL de la source de données';
COMMENT ON COLUMN Data_Source.description IS 'Description de la source de données';
COMMENT ON COLUMN Data_Source.publication_date IS 'Date de publication de la source de données';
COMMENT ON COLUMN Data_Source.data_provider_id IS 'Identifiant du fournisseur de données';

INSERT INTO Data_Source(id, name, url, description, publication_date, data_provider_id)
VALUES ('georisques',
        'Géorisques',
        'https://www.georisques.gouv.fr/donnees/bases-de-donnees',
        'Géorisques est le site de référence sur les risques majeurs naturels et technologiques',
        '2020-09-01',
        'ministere-de-la-transition-ecologique');


-- Liens entre les tables et les sources de données
CREATE TABLE Table_Data_Source
(
    table_name text PRIMARY KEY,
    data_source_id character varying(50)  NOT NULL REFERENCES Data_Source (id)
);

COMMENT ON TABLE Table_Data_Source IS 'Liens entre les tables et les sources de données';

COMMENT ON COLUMN Table_Data_Source.table_name IS 'Nom de la table';
COMMENT ON COLUMN Table_Data_Source.data_source_id IS 'Identifiant de la source de données';


-- from https://www.georisques.gouv.fr/sites/default/files/Liste_aleas_Gaspar.pdf

CREATE TABLE ref_risque_categorie(
  code TEXT PRIMARY KEY,
  label TEXT NOT NULL,
  parent TEXT REFERENCES ref_risque_categorie(code)
);

INSERT INTO Table_Data_Source(table_name, data_source_id) VALUES ('ref_risque_categorie', 'georisques');

CREATE TABLE ref_risque(
    code character varying (7) PRIMARY KEY,
    label TEXT NOT NULL,
    category TEXT NOT NULL REFERENCES ref_risque_categorie(code)
);

INSERT INTO Table_Data_Source(table_name, data_source_id) VALUES ('ref_risque', 'georisques');

-- voir dans le COVADIS

CREATE TABLE ref_typereg(
    code character varying(2) PRIMARY KEY,
    label text NOT NULL,
    color character varying(7) NOT NULL
);

INSERT INTO Table_Data_Source(table_name, data_source_id) VALUES ('ref_typereg', 'georisques');

CREATE TABLE ref_etat(
     code character varying(2) PRIMARY KEY,
     label text NOT NULL
);

INSERT INTO Table_Data_Source(table_name, data_source_id) VALUES ('ref_etat', 'georisques');

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

INSERT INTO Table_Data_Source(table_name, data_source_id) VALUES ('document_pprn', 'georisques');

CREATE TABLE multirisque_pprn(
    id_gaspar character varying(18) NOT NULL REFERENCES document_pprn(id_gaspar),
    coderisque character varying (7) NOT NULL REFERENCES ref_risque(code),
    PRIMARY KEY (id_gaspar, coderisque)
);

INSERT INTO Table_Data_Source(table_name, data_source_id) VALUES ('multirisque_pprn', 'georisques');

CREATE TABLE zone_pprn(
    id_zone TEXT NOT NULL PRIMARY KEY,
    id_gaspar character varying(20) NOT NULL,
    nom TEXT,
    codezone TEXT,
    typereg character varying(2) NOT NULL REFERENCES ref_typereg(code),
    urlfic TEXT,
    area geometry NOT NULL
);

INSERT INTO Table_Data_Source(table_name, data_source_id) VALUES ('zone_pprn', 'georisques');