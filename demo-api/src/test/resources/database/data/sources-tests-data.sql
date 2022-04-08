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