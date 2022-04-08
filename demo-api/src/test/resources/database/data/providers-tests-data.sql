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
