BEGIN TRANSACTION ISOLATION LEVEL SERIALIZABLE;
INSERT INTO ref_risque_categorie(code, label, parent) VALUES ('risques-naturels', 'Risques naturels', NULL),('inondation', 'Inondation', 'risques-naturels'),('mouvement-de-terrain', 'Mouvement de terrain', 'risques-naturels'),('phenomenes-meteorologiques', 'Phénomènes météorologiques', 'risques-naturels'),('risques-technologiques', 'Risques technologiques', NULL),('risques-industriels', 'Risques industriels', 'risques-technologiques'),('risques-miniers', 'Risques miniers', NULL),('mouvements-de-terrains-miniers', 'Mouvements de terrains miniers', 'risques-miniers'),('inondations-de-terrains-miniers', 'Inondations de terrain minier', 'risques-miniers'),('multi-alea', 'Multi-aléa', NULL);
INSERT INTO ref_risque(code, label, category) VALUES ('1000000', 'Risques naturels', 'risques-naturels'),('1100000', 'Inondation', 'inondation'),('1110000', 'Inondation - Par une crue (débordement de cours d’eau)', 'inondation'),('1120000', 'Inondation - Par une crue à débordement lent de cours d’eau', 'inondation'),('1130000', 'Inondation - Par une crue torrentielle ou à montée rapide de cours d’eau', 'inondation'),('1140000', 'Inondation - Par ruissellement et coulée de boue', 'inondation'),('1150000', 'Inondation - Par lave torrentielle (torrent et talweg)', 'inondation'),('1160000', 'Inondation - Par remontées de nappes naturelles', 'inondation'),('1170000', 'Inondation - Par submersion marine', 'inondation'),('1200000', 'Mouvement de terrain', 'mouvement-de-terrain'),('1210000', 'Mouvement de terrain - Affaissements et effondrements liés aux cavités souterraines (hors mines)', 'mouvement-de-terrain'),('1230000', 'Mouvement de terrain - Éboulement, chutes de pierres et de blocs', 'mouvement-de-terrain'),('1240000', 'Mouvement de terrain - Glissement de terrain', 'mouvement-de-terrain'),('1250000', 'Mouvement de terrain - Avancée dunaire', 'mouvement-de-terrain'),('1260000', 'Mouvement de terrain - Recul du trait de côte et de falaises', 'mouvement-de-terrain'),('1270000', 'Mouvement de terrain - Tassements différentiels', 'mouvement-de-terrain'),('1300000', 'Séisme', 'risques-naturels'),('1400000', 'Avalanche', 'risques-naturels'),('1500000', 'Éruption volcanique', 'risques-naturels'),('1600000', 'Feu de forêt', 'risques-naturels'),('1700000', 'Phénomènes météorologiques', 'phenomenes-meteorologiques'),('1710000', 'Phénomènes météorologiques - Cyclone/ouragan (vent)', 'phenomenes-meteorologiques'),('1720000', 'Phénomènes météorologiques - Tempête et grains (vent)', 'phenomenes-meteorologiques'),('1740000', 'Phénomènes météorologiques - Foudre', 'phenomenes-meteorologiques'),('1750000', 'Phénomènes météorologiques - Grêle', 'phenomenes-meteorologiques'),('1760000', 'Phénomènes météorologiques - Neige et Pluies verglaçantes', 'phenomenes-meteorologiques'),('1800000', 'Radon', 'risques-naturels'),('2000000', 'Risques technologiques', 'risques-technologiques'),('2100000', 'Risques industriels', 'risques-industriels'),('2110000', 'Risques industriels - Effet thermique', 'risques-industriels'),('2120000', 'Risques industriels - Effet de surpression', 'risques-industriels'),('2130000', 'Risques industriels - Effet toxique', 'risques-industriels'),('2140000', 'Risques industriels - Effet de projection', 'risques-industriels'),('2200000', 'Nucléaire', 'risques-technologiques'),('2300000', 'Rupture de barrage', 'risques-technologiques'),('2400000', 'Transport de marchandises dangereuses', 'risques-technologiques'),('2500000', 'Engins de guerre', 'risques-technologiques'),('3000000', 'Risques miniers', 'risques-miniers'),('3100000', 'Mouvements de terrains miniers', 'mouvements-de-terrains-miniers'),('3110000', 'Mouvements de terrains miniers - Effondrements généralisés', 'mouvements-de-terrains-miniers'),('3120000', 'Mouvements de terrains miniers - Effondrements localisés', 'mouvements-de-terrains-miniers'),('3130000', 'Mouvements de terrains miniers - Affaissements progressifs', 'mouvements-de-terrains-miniers'),('3140000', 'Mouvements de terrains miniers - Tassements', 'mouvements-de-terrains-miniers'),('3150000', 'Mouvements de terrains miniers - Glissements ou mouvements de pente', 'mouvements-de-terrains-miniers'),('3160000', 'Mouvements de terrains miniers - Coulées', 'mouvements-de-terrains-miniers'),('3170000', 'Mouvements de terrains miniers - Écroulements rocheux', 'mouvements-de-terrains-miniers'),('3200000', 'Inondations de terrain minier', 'inondations-de-terrains-miniers'),('3210000', 'Inondations de terrain minier - Pollution des eaux souterraines et de surface', 'inondations-de-terrains-miniers'),('3220000', 'Inondations de terrain minier - Pollution des sédiments et des sols', 'inondations-de-terrains-miniers'),('3300000', 'Émissions en surface de gaz de mine', 'risques-miniers'),('9999999', 'Multi-alea', 'multi-alea');
INSERT INTO ref_typereg(code, label, color) VALUES ('01', 'Prescriptions hors zone d''aléa', '#89d9e7'),('02', 'Prescriptions', '#0000ff'),('03', 'Interdiction', '#ff6060'),('04', 'Interdiction stricte', '#e00000'),('05', 'Délaissement possible', '#c993ff'),('06', 'Expropriation possible', '#9a359b');
INSERT INTO ref_etat(code, label) VALUES ('01', 'Prescrit'),('02', 'Approuvé'),('03', 'Abrogé'),('04', 'Appliqué par anticipation');
COMMIT TRANSACTION;