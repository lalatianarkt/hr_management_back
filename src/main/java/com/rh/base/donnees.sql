-- -----------------------------
-- emergency_contact
-- -----------------------------
INSERT INTO emergency_contact (contact, email, adresse, created_at)
VALUES
('Marie RAZAFINDRAKOTO', 'marie.contact@email.com', 'Antananarivo'),
('Jean RAKOTONDRABE', 'jean.contact@email.com', 'Fianarantsoa'),
('Lala RABEMANANJARA', 'lala.contact@email.com', 'Toamasina');

-- -----------------------------
-- infos_Administratives
-- -----------------------------
INSERT INTO infos_Administratives (num_cnaps, cin, nombre_enfants, situation_familiale)
VALUES
('CNAPS001', 'CIN123456', 2, 'Marié(e)'),
('CNAPS002', 'CIN234567', 0, 'Célibataire'),
('CNAPS003', 'CIN345678', 1, 'Marié(e)');

-- -----------------------------
-- Employe
-- -----------------------------
-- On suppose que les IDs des autres tables sont déjà générés via triggers
-- et que le premier emergency_contact, infos_pro, infos_admin ont été insérés.
INSERT INTO Employe (nom, prenom, sexe, date_naissance, telephone, email, adresse,
                     created_at, id_emergency_contact, id_info_pro, id_info_admin)
VALUES
('Rakoto', 'Andry', 'M', '1990-05-12', '0341234567', 'andry.rakoto@email.com', 'Antananarivo',
 NOW(), 'EC001', 'IP001', 'IA001'),
('Ratsimba', 'Hery', 'M', '1985-08-22', '0349876543', 'hery.ratsimba@email.com', 'Fianarantsoa',
 NOW(), 'EC002', 'IP002', 'IA002'),
('Rabarijaona', 'Lalao', 'F', '1992-11-30', '0345678901', 'lalao.rabarijaona@email.com', 'Toamasina',
 NOW(), 'EC003', 'IP003', 'IA003');



-- -----------------------------
-- type_user
-- -----------------------------
INSERT INTO type_user (type, created_at)
VALUES
('Admin'),
('Manager'),
('Employe');

-- -----------------------------
-- Token
-- -----------------------------
-- On ne mettra pas de token lié à un User pour le moment
-- Exemple de token générique (sera rélié après inscription)
-- INSERT INTO Token (token_genere, type, created_at, is_active, id_user)
-- VALUES
-- ('TOKEN001', 'Test', 1, 'USR001'), -- juste un exemple
-- ('TOKEN002', 'Test', 1, 'USR002');

INSERT INTO Employe (nom, prenom, sexe, date_naissance, telephone, email, adresse,
                     created_at, id_emergency_contact, id_info_pro, id_info_admin)
VALUES
('Rabarijaona', 'Sanda', 'F', '1992-11-30', '0345678901', 'sandakwl25@gmail.com', 'Toamasina',
 NOW(), 'EC003', 'IP005', 'IA004');


INSERT INTO emergency_contact (contact, email, adresse, created_at)
VALUES
('Lala RABEMANANJARA', 'lala.contact@email.com', 'Toamasina');

-- -----------------------------
-- infos_Professionnelles
-- -----------------------------
INSERT INTO infos_Professionnelles (matricule, date_embauche)
VALUES
('MAT004', '2022-03-05');

-- -----------------------------
-- infos_Administratives
-- -----------------------------
INSERT INTO infos_Administratives (num_cnaps, cin, nombre_enfants, situation_familiale)
VALUES
('CNAPS004', 'CIN345679', 1, 'Marié(e)');

-- -----------------------------
-- Type_document (sans ID)
-- -----------------------------
INSERT INTO type_document (intitule) VALUES
('CV'),
('Contrat'),
('Diplôme'),
('Attestation de travail'),
('Bulletin de paie'),
('Certificat médical'),
('Photo d''identité'),
('Carte d''identité'),
('Livret de famille'),
('Attestation de sécurité sociale');

-- -----------------------------
-- Document_employe (sans ID)
-- -----------------------------
-- Documents pour Andry Rakoto (EMP001)
INSERT INTO document_employe (nom_fichier, chemin_fichier, date_upload, id_type_document, id_employe) VALUES
('cv_andry_rakoto.pdf', '/documents/emp001/cv.pdf', 'TYP001', 'EMP001'),
('contrat_andry_rakoto.pdf', '/documents/emp001/contrat.pdf', 'TYP002', 'EMP001'),
('diplome_andry_rakoto.pdf', '/documents/emp001/diplome.pdf', 'TYP003', 'EMP001'),
('photo_andry_rakoto.jpg', '/documents/emp001/photo.jpg', 'TYP007', 'EMP001');

-- Documents pour Hery Ratsimba (EMP002)
INSERT INTO document_employe (nom_fichier, chemin_fichier, date_upload, id_type_document, id_employe) VALUES
('cv_hery_ratsimba.pdf', '/documents/emp002/cv.pdf', 'TYP001', 'EMP002'),
('contrat_hery_ratsimba.pdf', '/documents/emp002/contrat.pdf', 'TYP002', 'EMP002'),
('cin_hery_ratsimba.pdf', '/documents/emp002/cin.pdf', 'TYP008', 'EMP002'),
('bulletin_paie_hery_janv.pdf', '/documents/emp002/bulletin_janv.pdf', 'TYP005', 'EMP002');

-- Documents pour Lalao Rabarijaona (EMP003)
INSERT INTO document_employe (nom_fichier, chemin_fichier, date_upload, id_type_document, id_employe) VALUES
('cv_lalao_rabarijaona.pdf', '/documents/emp003/cv.pdf', 'TYP001', 'EMP003'),
('contrat_lalao_rabarijaona.pdf', '/documents/emp003/contrat.pdf', 'TYP002', 'EMP003'),
('livret_famille_lalao.pdf', '/documents/emp003/livret_famille.pdf', 'TYP009', 'EMP003'),
('certificat_medical_lalao.pdf', '/documents/emp003/certificat_medical.pdf', 'TYP006', 'EMP003');

-- Documents pour Sanda Rabarijaona (EMP006)
INSERT INTO document_employe (nom_fichier, chemin_fichier, date_upload, id_type_document, id_employe) VALUES
('cv_sanda_rabarijaona.pdf', '/documents/emp006/cv.pdf', 'TYP001', 'EMP006'),
('contrat_sanda_rabarijaona.pdf', '/documents/emp006/contrat.pdf', 'TYP002', 'EMP006'),
('diplome_sanda_rabarijaona.pdf', '/documents/emp006/diplome.pdf', 'TYP003', 'EMP006'),
('attestation_travail_sanda.pdf', '/documents/emp006/attestation_travail.pdf', 'TYP004', 'EMP006'),
('secu_sanda.pdf', '/documents/emp006/secu.pdf', 'TYP010', 'EMP006');

-- Documents supplémentaires sans type spécifique (id_type_document NULL)
INSERT INTO document_employe (nom_fichier, chemin_fichier, date_upload, id_employe) VALUES
('autre_document_andry.pdf', '/documents/emp001/autre.pdf', 'EMP001'),
('recommandation_hery.pdf', '/documents/emp002/recommandation.pdf', 'EMP002');


-- Insertion des données pour la table sexe
INSERT INTO sexe (sexe, code) VALUES 
('Masculin', 'M'),
('Féminin', 'F'),
('Non binaire', 'N'),
('Autre', 'A'),
('Non spécifié', 'X');

-- Insertion des postes (sans ID - adaptés au contexte malgache)
INSERT INTO Poste (nom, description, created_at) VALUES
-- Postes Ressources Humaines (regroupés pour contexte malgache)
('Responsable RH Polyvalent', 'Gestion complète RH : recrutement, paie, formation, administration du personnel'),
('Assistant RH Polyvalent', 'Support RH général : recrutement, paie, dossier personnel, formalités administratives'),
('Gestionnaire de Paie et Administration', 'Gestion paie + formalités administratives + déclarations sociales'),
('Chargé de Recrutement et Formation', 'Recrutement + organisation formations + suivi carrières'),

-- Postes Direction et Management
('Directeur Général', 'Direction générale entreprise - PME/PMI malgache'),
('Directeur Administratif et Financier', 'DAF : Gestion admin, finance, RH, juridique - TPE/PME'),
('Responsable d''Agence', 'Management agence : commercial, opérations, équipe'),
('Chef de Service Polyvalent', 'Encadrement équipe + gestion opérationnelle service'),

-- Postes Informatique (adaptés contexte local)
('Informaticien Polyvalent', 'Support technique + développement + maintenance réseau + assistance utilisateurs'),
('Développeur Full-Stack', 'Développement applications web + mobile - Startup/ESN malgache'),
('Technicien Maintenance Informatique', 'Maintenance parc informatique + support technique'),
('Responsable Systèmes et Réseaux', 'Gestion infrastructure IT + téléphonie + sécurité'),

-- Postes Commercial et Marketing
('Commercial Terrain', 'Prospection + vente + relation client + suivi commandes'),
('Responsable Commercial', 'Management équipe commerciale + stratégie vente'),
('Chargé Marketing et Communication', 'Marketing + communication + réseaux sociaux + événementiel'),
('Télévendeur', 'Vente téléphonique + prospection + fidélisation clients'),

-- Postes Comptabilité et Finance
('Comptable Unique', 'Comptabilité générale + analytique + paie + déclarations fiscales'),
('Gestionnaire Financier', 'Trésorerie + comptabilité + relation banque'),
('Assistant Comptable', 'Saisie comptable + facturation + relance clients'),
('Caissier', 'Gestion caisse + encaissement + facturation'),

-- Postes Production et Logistique
('Responsable Production', 'Management production + qualité + planning équipe'),
('Agent de Production Polyvalent', 'Production + contrôle qualité + maintenance préventive'),
('Chauffeur-Livreur', 'Conduite + livraison + gestion stock véhicule'),
('Magasinier', 'Gestion stock + préparation commandes + inventaire'),

-- Postes Services Généraux
('Agent d''Entretien', 'Nettoyage locaux + maintenance légère + courrier'),
('Gardien', 'Surveillance + accueil + sécurité site'),
('Secrétaire Polyvalente', 'Secrétariat + standard + accueil + administration'),
('Cuisinier', 'Préparation repas personnel - Cantine entreprise'),

-- Postes Spécialisés (grandes entreprises)
('Ingénieur en BTP', 'Conception + suivi chantier + management équipe technique'),
('Architecte', 'Conception plans + suivi travaux + relation client'),
('Infirmier d''Entreprise', 'Soins + prévention + santé au travail'),
('Formateur Consultant', 'Animation formations + conseil entreprises'),

-- Postes Secteur Tourisme et Hôtellerie
('Réceptionniste Hôtel', 'Accueil clients + réservations + facturation'),
('Guide Touristiques', 'Accompagnement touristes + animation visites'),
('Responsable Restauration', 'Gestion restaurant + équipe + approvisionnement'),
('Agent de Voyage', 'Conseil voyages + réservations + vente séjours'),

-- Postes Secteur Agricole et Agroalimentaire
('Technicien Agricole', 'Conseil techniques agricoles + suivi cultures'),
('Responsable Exploitation Agricole', 'Management exploitation + commercialisation produits'),
('Contrôleur Qualité Agroalimentaire', 'Contrôle qualité produits + normes sanitaires'),
('Commercial Produits Agricoles', 'Vente produits agricoles + relation clients export'),

-- Postes Secteur Textile et Artisanat
('Ouvrier Textile', 'Confection vêtements + contrôle qualité'),
('Artisan', 'Création produits artisanaux + vente'),
('Responsable Atelier', 'Management atelier production + qualité'),
('Designer Produit', 'Conception produits + développement collections');

-- Insertion des types de contrats courants à Madagascar (sans ID)
-- Types de contrats essentiels (sans ID et sans accents)
INSERT INTO Type_contrat (intitule, description) VALUES
('CDI', 'Contrat a duree indeterminee - Emploi permanent'),
('CDD', 'Contrat a duree determinee - Emploi temporaire'),
('Stage', 'Contrat de stage professionnel'),
('Alternance', 'Contrat en alternance ecole-entreprise'),
('Interim', 'Mission temporaire via agence d''interim'),
('Temps partiel', 'Horaires de travail reduits'),
('Consultant', 'Prestation de service independante'),
('Projet', 'Contrat pour projet specifique'),
('Essai', 'Periode d''essai en debut de contrat'),
('Saisonnier', 'Travail saisonnier ou periodique');

INSERT INTO Type_contrat (intitule, description) VALUES
('Essai', 'Periode d''essai en debut de contrat');

-- Postes pour Département RH (DEP1)
UPDATE poste SET id_departement = 'DEP1' WHERE id IN ('POST005', 'POST016', 'POST017', 'POST037', 'POST038');

-- Postes pour Département Ventes et Marketing (DEP2)
UPDATE poste SET id_departement = 'DEP2' WHERE id IN ('POST007', 'POST012', 'POST021', 'POST027', 'POST033', 'POST039');

-- Postes pour Département Développement Informatique (DEP3)
UPDATE poste SET id_departement = 'DEP3' WHERE id IN (
    'POST001', 'POST002', 'POST003', 'POST004', 'POST008', 'POST009', 
    'POST010', 'POST011', 'POST013', 'POST014', 'POST015', 'POST034'
);

-- Postes pour Département Comptabilité Générale (DEP4)
UPDATE poste SET id_departement = 'DEP4' WHERE id IN ('POST006', 'POST018', 'POST019');

-- Postes pour Département Maintenance et Logistique (DEP5)
UPDATE poste SET id_departement = 'DEP5' WHERE id IN (
    'POST020', 'POST022', 'POST023', 'POST024', 'POST028', 'POST029', 
    'POST030', 'POST035', 'POST040'
);

-- Postes restants (Santé, Éducation, etc.) - à associer à un département existant ou nouveau
UPDATE poste SET id_departement = 'DEP1' WHERE id IN ('POST025', 'POST026', 'POST031', 'POST032', 'POST036');

INSERT INTO situation_familiale (type)
VALUES 
('Célibataire'),
('Marié(e)'),
('Divorcé(e)'),
('Veuf(ve)'),
('Union libre'),
('Séparé(e)');

INSERT INTO departement (description, nom, nb_employe, id_departement, created_at, modified_at)
VALUES
('Gere le recrutement, les contrats et la paie du personnel', 'Ressources Humaines', 12, 'TDEP1', NOW(), NULL),
('Supervise les ventes, la publicite et les relations clients', 'Ventes et Marketing', 18, 'TDEP2', NOW(), NULL),
('Developpe et maintient les logiciels et systemes informatiques', 'Developpement Informatique', 25, 'TDEP3', NOW(), NULL),
('S occupe de la comptabilite, des budgets et des audits', 'Comptabilite Generale', 10, 'TDEP4', NOW(), NULL),
('Assure la maintenance des equipements et la logistique interne', 'Maintenance et Logistique', 8, 'TDEP5', NOW(), NULL);

INSERT INTO nationalite (nationalite) VALUES
('Malagasy'),
('Sud Africaine'),
('Algerienne'),
('Anglaise'),
('Americaine'),
('Allemande'),
('Argentine'),
('Australienne'),
('Autrichienne'),
('Belge'),
('Beninoise'),
('Bresilienne'),
('Britannique'),
('Burkinabe'),
('Burundaise'),
('Cambodgienne'),
('Canadienne'),
('Chilienne'),
('Chinoise'),
('Colombienne'),
('Comorienne'),
('Congolaise'),
('Coreenne'),
('Croate'),
('Danoise'),
('Djiboutienne'),
('Egyptienne'),
('Espagnole'),
('Estonienne'),
('Ethiopienne'),
('Finlandaise'),
('Francaise'),
('Gabonaise'),
('Ghanenne'),
('Grecque'),
('Guineenne'),
('Haïtienne'),
('Hongroise'),
('Indienne'),
('Indonesienne'),
('Irlandaise'),
('Israélienne'),
('Italienne'),
('Ivoirienne'),
('Jamaicaine'),
('Japonaise'),
('Kenyane'),
('Libanaise'),
('Malaisienne'),
('Malienne'),
('Marocaine'),
('Mauricienne'),
('Mexicaine'),
('Mozambicaine'),
('Nepalaise'),
('Nigerienne'),
('Norvegienne'),
('Pakistanaise'),
('Peruvienne'),
('Philippine'),
('Polonaise'),
('Portugaise'),
('Roumaine'),
('Russe'),
('Rwandaise'),
('Senegalaise'),
('Suedeoise'),
('Suisse'),
('Tanzanienne'),
('Togolaise'),
('Tunisienne'),
('Turque'),
('Ukrainienne'),
('Vietnamienne'),
('Zambienne'),
('Zimbabweenne');

INSERT INTO categorie (nom, description, couleur) VALUES
-- Catégories principales pour le contexte RH malgache
('Ressources Humaines', 'Gestion du personnel, recrutement, paie et administration', '#1890ff'),
('Direction et Management', 'Postes de direction, encadrement et prise de décision', '#eb2f96'),
('Informatique et Digital', 'Technologies, développement, support et infrastructure IT', '#52c41a'),
('Commercial et Vente', 'Prospection, vente, relation client et développement commercial', '#fa541c'),
('Comptabilité et Finance', 'Gestion financière, comptabilité, trésorerie et fiscalité', '#faad14'),
('Production et Logistique', 'Fabrication, opérations, maintenance et gestion des stocks', '#722ed1'),
('Services Généraux', 'Support administratif, entretien, sécurité et services internes', '#13c2c2'),
('BTP et Ingénierie', 'Construction, génie civil, architecture et travaux', '#ff7a45'),
('Santé et Sécurité', 'Médecine du travail, prévention et bien-être au travail', '#f759ab'),
('Formation et Conseil', 'Développement compétences, consulting et accompagnement', '#9254de'),
('Tourisme et Hôtellerie', 'Accueil, restauration, guidage et services touristiques', '#36cfc9'),
('Agriculture et Agroalimentaire', 'Production agricole, transformation et commercialisation', '#73d13d'),
('Textile et Artisanat', 'Confection, création artisanale et design', '#ff4d4f'),
('Qualité et Contrôle', 'Assurance qualité, normes et processus de contrôle', '#597ef7'),
('Support Administratif', 'Secrétariat, standard et gestion administrative', '#ffa940');

INSERT INTO type_mouvement (id, type, created_at, modified_at) VALUES
('TM-001', 'Changement de poste', NOW(), NULL),
('TM-002', 'Mutation', NOW(), NULL),
('TM-003', 'Promotion', NOW(), NULL),
('TM-004', 'Renouvellement de contrat', NOW(), NULL),
('TM-005', 'Départ volontaire', NOW(), NULL),
('TM-006', 'Retraite', NOW(), NULL),
('TM-007', 'Suspension', NOW(), NULL),
('TM-008', 'Avenant de contrat', NOW(), NULL);

INSERT INTO Niveau_hierarchique (id, nom, rang, description, created_at, modified_at) VALUES
('NIV1', 'Employe', 1, 'Personnel d execution', NOW(), NULL),
('NIV2', 'Assistant', 2, 'Personnel d appui administratif ou technique', NOW(), NULL),
('NIV3', 'Agent Principal', 3, 'Employe experimente avec taches specifiques', NOW(), NULL),
('NIV4', 'Superviseur', 4, 'Encadrement operationnel d equipe', NOW(), NULL),
('NIV5', 'Chef de Service', 5, 'Responsable d un service ou departement', NOW(), NULL),
('NIV6', 'Directeur', 6, 'Membre de la direction, responsable d une division', NOW(), NULL),
('NIV7', 'Direction Generale', 7, 'Plus haut niveau hierarchique', NOW(), NULL);


UPDATE Poste SET id_niveau = 'NIV6' WHERE id IN ('POST005', 'POST018');
UPDATE Poste SET id_niveau = 'NIV5' WHERE id IN ('POST012', 'POST004', 'POST020', 'POST029');
UPDATE Poste SET id_niveau = 'NIV4' WHERE id IN ('POST037', 'POST033', 'POST039', 'POST034', 'POST019', 'POST035');
UPDATE Poste SET id_niveau = 'NIV3' WHERE id IN ('POST016', 'POST007', 'POST027', 'POST021', 'POST009', 'POST010', 'POST011', 'POST013', 'POST014', 'POST015', 'POST006', 'POST022', 'POST023', 'POST024', 'POST028', 'POST030', 'POST040', 'POST025', 'POST026', 'POST031', 'POST032', 'POST036');
UPDATE Poste SET id_niveau = 'NIV2' WHERE id IN ('POST017');
UPDATE Poste SET id_niveau = 'NIV2' WHERE id = 'POST038';
UPDATE Poste SET id_niveau = 'NIV3' WHERE id IN ('POST001','POST002','POST003','POST008');

-- Insertion des 7 jours de la semaine
INSERT INTO jour_travail (nom_jour, code_jour, created_at, statut, est_weekend) VALUES
('Lundi', 1, NOW(), 1, 0),
('Mardi', 2, NOW(), 1, 0),
('Mercredi', 3, NOW(), 1, 0),
('Jeudi', 4, NOW(), 1, 0),
('Vendredi', 5, NOW(), 1, 0),
('Samedi', 6, NOW(), 1, 1),
('Dimanche', 7, NOW(), 1, 1);


-- Insertion des horaires journaliers pour tous les employés
-- Lundi à Vendredi: horaire de travail, Samedi et Dimanche: repos ou horaire réduit

-- Insertion des horaires journaliers pour chaque jour de travail
INSERT INTO horaire_journalier (id, created_at, modified_at, duree_pause_minutes, duree_journaliere_attendue_minutes, statut, id_jour) VALUES
-- Lundi (jour 1) - Horaire standard bureau
('HOR-001', '2025-12-02 08:00:00', '2025-12-02 08:00:00', 60, 480, 1, 1),

-- Mardi (jour 2) - Horaire standard bureau
('HOR-002', '2025-12-02 08:00:00', '2025-12-02 08:00:00', 60, 480, 1, 2),

-- Mercredi (jour 3) - Horaire standard bureau
('HOR-003', '2025-12-02 08:00:00', '2025-12-02 08:00:00', 60, 480, 1, 3),

-- Jeudi (jour 4) - Horaire standard bureau
('HOR-004', '2025-12-02 08:00:00', '2025-12-02 08:00:00', 60, 480, 1, 4),

-- Vendredi (jour 5) - Horaire court pour vendredi
('HOR-005', '2025-12-02 08:00:00', '2025-12-02 08:00:00', 45, 420, 1, 5),

-- Samedi (jour 6) - Horaire du weekend (matin seulement)
('HOR-006', '2025-12-02 08:00:00', '2025-12-02 08:00:00', 30, 240, 1, 6),

-- Dimanche (jour 7) - Fermé
('HOR-007', '2025-12-02 08:00:00', '2025-12-02 08:00:00', 0, 0, 0, 7);


INSERT INTO type_conge (id, intitule, description, obligatoire_doc, created_at, modified_at, is_cumulable) VALUES
('TC001', 'Conge annuel', 'Conge paye annuel pour tous les employes', FALSE, CURRENT_TIMESTAMP, NULL, false),
('TC002', 'Conge maladie', 'Conge pour raison de sante sur presentation d un certificat medical', false, CURRENT_TIMESTAMP, NULL, FALSE),
('TC003', 'Conge maternite', 'Conge pour maternite selon la legislation en vigueur', false, CURRENT_TIMESTAMP, NULL, FALSE),
('TC004', 'Conge paternite', 'Conge pour paternite selon la legislation', false, CURRENT_TIMESTAMP, NULL, FALSE),
('TC005', 'RTT', 'Jours de reduction du temps de travail', FALSE, CURRENT_TIMESTAMP, NULL, false),
('TC006', 'Conge sans solde', 'Conge autorise mais non paye', FALSE, CURRENT_TIMESTAMP, NULL, FALSE),
('TC007', 'Conge pour mariage', 'Conge accorde pour mariage ou PACS', FALSE, CURRENT_TIMESTAMP, NULL, FALSE),
('TC008', 'Conge pour deces', 'Conge en cas de deces d un proche', FALSE, CURRENT_TIMESTAMP, NULL, FALSE);

-- Namasy Manorintsoa (embauché le 2021-12-12)

INSERT INTO mouvement_solde
(nb_conge_total, nb_conge_restant, nb_conge_pris, annee, created_at, modified_at, mois, statut, commentaire, type_mouvement, id_employe)
VALUES
-- Janvier à Mars : acquisitions
(2.5, 2.5, 0.0, 2025, NOW(), NOW(), 1, 1, 'Acquisition mensuelle', 'ACQUISITION', 'EMP-20251124-62B184'),
(5.0, 5.0, 0.0, 2025, NOW(), NOW(), 2, 1, 'Acquisition mensuelle', 'ACQUISITION', 'EMP-20251124-62B184'),
(7.5, 7.5, 0.0, 2025, NOW(), NOW(), 3, 1, 'Acquisition mensuelle', 'ACQUISITION', 'EMP-20251124-62B184'),

-- Avril : acquisition
(10.0, 10.0, 0.0, 2025, NOW(), NOW(), 4, 1, 'Acquisition mensuelle', 'ACQUISITION', 'EMP-20251124-62B184'),

-- Avril : prise 5 jours
(10.0, 5.0, 5.0, 2025, NOW(), NOW(), 4, 1, 'Congé annuel', 'PRISE', 'EMP-20251124-62B184'),

-- Mai à Juillet : acquisitions
(12.5, 7.5, 0.0, 2025, NOW(), NOW(), 5, 1, 'Acquisition mensuelle', 'ACQUISITION', 'EMP-20251124-62B184'),
(15.0, 10.0, 0.0, 2025, NOW(), NOW(), 6, 1, 'Acquisition mensuelle', 'ACQUISITION', 'EMP-20251124-62B184'),
(17.5, 12.5, 0.0, 2025, NOW(), NOW(), 7, 1, 'Acquisition mensuelle', 'ACQUISITION', 'EMP-20251124-62B184'),

-- Août : prise 3 jours
(17.5, 9.5, 3.0, 2025, NOW(), NOW(), 8, 1, 'Congé personnel', 'PRISE', 'EMP-20251124-62B184'),

-- Août à Novembre : acquisitions
(20.0, 12.0, 0.0, 2025, NOW(), NOW(), 8, 1, 'Acquisition mensuelle', 'ACQUISITION', 'EMP-20251124-62B184'),
(22.5, 14.5, 0.0, 2025, NOW(), NOW(), 9, 1, 'Acquisition mensuelle', 'ACQUISITION', 'EMP-20251124-62B184'),
(25.0, 17.0, 0.0, 2025, NOW(), NOW(), 10, 1, 'Acquisition mensuelle', 'ACQUISITION', 'EMP-20251124-62B184'),
(27.5, 19.5, 0.0, 2025, NOW(), NOW(), 11, 1, 'Acquisition mensuelle', 'ACQUISITION', 'EMP-20251124-62B184'),

-- Décembre : acquisition
(30.0, 19.5, 0.0, 2025, NOW(), NOW(), 12, 1, 'Acquisition mensuelle', 'ACQUISITION', 'EMP-20251124-62B184');


-- Randrianarisoa Ravotiana
INSERT INTO mouvement_solde
(nb_conge_total, nb_conge_restant, nb_conge_pris, annee, created_at, modified_at, mois, statut, commentaire, type_mouvement, id_employe)
VALUES
-- Septembre : acquisition  
(2.5, 2.5, 0.0, 2025, NOW(), NOW(), 9, 1, 'Acquisition mensuelle', 'ACQUISITION', 'EMP-20251124-2275AF'),

-- Octobre : acquisition
(5.0, 5.0, 0.0, 2025, NOW(), NOW(), 10, 1, 'Acquisition mensuelle', 'ACQUISITION', 'EMP-20251124-2275AF'),

-- Octobre : prise 2 jours
(5.0, 3.0, 2.0, 2025, NOW(), NOW(), 10, 1, 'Congé exceptionnel', 'PRISE', 'EMP-20251124-2275AF'),

-- Novembre : acquisition
(7.5, 5.5, 0.0, 2025, NOW(), NOW(), 11, 1, 'Acquisition mensuelle', 'ACQUISITION', 'EMP-20251124-2275AF'),

-- Décembre : acquisition
(10.0, 5.5, 0.0, 2025, NOW(), NOW(), 12, 1, 'Acquisition mensuelle', 'ACQUISITION', 'EMP-20251124-2275AF');


-- Mettre à jour les durées maximales selon le Code du travail malgasy
UPDATE type_contrat SET duree_max_mois = NULL WHERE intitule = 'CDI';
UPDATE type_contrat SET duree_max_mois = 6 WHERE intitule = 'CDD';
UPDATE type_contrat SET duree_max_mois = 3 WHERE intitule = 'Stage';
UPDATE type_contrat SET duree_max_mois = 24 WHERE intitule = 'Alternance';
UPDATE type_contrat SET duree_max_mois = 6 WHERE intitule = 'Interim';
UPDATE type_contrat SET duree_max_mois = NULL WHERE intitule = 'Temps partiel';
UPDATE type_contrat SET duree_max_mois = 12 WHERE intitule = 'Consultant';
UPDATE type_contrat SET duree_max_mois = 24 WHERE intitule = 'Projet';
UPDATE type_contrat SET duree_max_mois = 6 WHERE intitule = 'Essai';
UPDATE type_contrat SET duree_max_mois = 6 WHERE intitule = 'Saisonnier';

SELECT 
    -- Informations employé de base
    e.id AS id_employe,
    e.nom AS nom_employe,
    e.prenom AS prenom_employe,
    
    -- Informations professionnelles
    ip.matricule,
    ip.date_embauche,
    ip.statut AS statut_professionnel,
    ip.salaire_base,
    
    -- Informations département
    d.id AS id_departement,
    d.nom AS nom_departement,
    d.description AS description_departement,
    d.statut AS statut_departement,
    
    -- Informations manager du département
    m.id_employe AS id_manager_departement,
    m.date_debut AS date_debut_management,
    m.date_fin AS date_fin_management,
    
    -- Dates d'assignation
    ip.date_debut_assignation_poste,
    ip.date_fin_assignation_poste,
    
    -- Informations complémentaires
    p.nom AS nom_poste,
    tc.intitule AS type_contrat
    
FROM Employe e
INNER JOIN infos_professionnelles ip ON e.id = ip.id_employe
INNER JOIN manager m ON ip.id_employe = m.id_employe
INNER JOIN Departement d ON m.id_departement = d.id
LEFT JOIN Poste p ON ip.id_poste = p.id
LEFT JOIN Type_contrat tc ON ip.id_type_contrat = tc.id

-- Filtrer les employés actifs (si vous avez une logique de statut)
WHERE ip.statut = 0 
  AND d.statut = 0
  and e.statut = 0
  -- Filtrer les managers actifs (date_fin IS NULL ou date_fin > maintenant)
  AND (m.date_fin IS NULL OR m.date_fin > CURRENT_DATE)
  -- Filtrer les assignations de poste actives
  AND (ip.date_fin_assignation_poste IS NULL OR ip.date_fin_assignation_poste > CURRENT_DATE)

-- Optionnel : tri par département et nom d'employé
ORDER BY d.nom, e.nom, e.prenom;


-- Désactiver temporairement les contraintes de clé étrangère si besoin
-- SET FOREIGN_KEY_CHECKS = 0;

-- Nettoyer la table (optionnel - pour réinitialisation)
-- TRUNCATE TABLE rubrique_paie;

-- Insertion des rubriques de GAINS (éléments positifs)
-- Admin IT insère UNIQUEMENT les structures vides/inactives comme TEMPLATES
-- Tous les champs métier sont NULL ou false, est_actif = false
-- Les RH devront activer et configurer via l'interface

-- 1. Insertion des types de rubriques (GAIN/RETENUE/TOTAL)
INSERT INTO rubrique_types (id, libelle, description, created_at, modified_at) VALUES
('GAIN', 'Gain', 'Element qui augmente le salaire', NOW(), NOW()),
('RETENUE', 'Retenue', 'Element qui diminue le salaire', NOW(), NOW());
INSERT INTO rubrique_types (id, libelle, description, created_at, modified_at)
VALUES ('CHARGE', 'Charge patronale', 'Cotisation à la charge de l''employeur', NOW(), NOW());

-- 2. Insertion des categories de rubriques
INSERT INTO categorie_rub (id, libelle, created_at, modified_at) VALUES
('SALAIRE', 'Salaire', NOW(), NOW()),
('HEURES_SUP', 'Heures supplementaires', NOW(), NOW()),
('PRIME', 'Prime', NOW(), NOW()),
('INDEMNITE', 'Indemnite', NOW(), NOW()),
('COTISATION', 'Cotisation sociale', NOW(), NOW()),
('IMPOT', 'Impot', NOW(), NOW()),
('RETENUE_DIV', 'Retenue diverse', NOW(), NOW()),
('TOTAL_INT', 'Total intermediaire', NOW(), NOW());

-- 3. Insertion des modes de calcul
INSERT INTO mode_calcul (id, libelle, description, unite, created_at, modified_at) VALUES
('FORFAIT', 'Forfait', 'Montant fixe', 'Ar', NOW(), NOW()),
('POURCENTAGE', 'Pourcentage', 'Pourcentage dune base', '%', NOW(), NOW()),
('HEURE', 'A lheure', 'Calcul horaire', 'Ar/heure', NOW(), NOW()),
('JOUR', 'A la journee', 'Calcul journalier', 'Ar/jour', NOW(), NOW()),
('CALCULE', 'Calcule', 'Calcul automatique par formule', '', NOW(), NOW()),
('VARIABLE', 'Variable', 'Saisie manuelle mensuelle', 'Ar', NOW(), NOW());

-- 4. Insertion des rubriques de paie (toutes INACTIVES par defaut)
INSERT INTO rubrique_paie (
    id, code, libelle, assiette_calcul, taux, formule_calcul,
    plafond_mensuel, est_imposable, est_soumis_cotisations,
    compte_comptable, ordre, est_actif, plafond_annuel, commentaire,
    created_at, modified_at, id_categorie, id_mode_calcul, id_type
) VALUES
-- SECTION 1: SALAIRES DE BASE (GAINS)
('RP001', 'SB', 'Salaire de base', NULL, NULL, NULL,
 NULL, false, false, NULL, 1, false, NULL,
 'Salaire mensuel de base selon contrat - A configurer par RH',
 NOW(), NOW(), 'SALAIRE', 'FORFAIT', 'GAIN'),

('RP002', 'SBM', 'Salaire de base mensualise', NULL, NULL, NULL,
 NULL, false, false, NULL, 2, false, NULL,
 'Pour les employes mensualises - A configurer',
 NOW(), NOW(), 'SALAIRE', 'FORFAIT', 'GAIN'),

-- SECTION 2: HEURES SUPPLEMENTAIRES (GAINS)
('RP003', 'HS25', 'Heures supplementaires 25%', NULL, NULL, NULL,
 NULL, false, false, NULL, 3, false, NULL,
 'Majoration 25% - A configurer selon politique entreprise',
 NOW(), NOW(), 'HEURES_SUP', 'HEURE', 'GAIN'),

('RP004', 'HS50', 'Heures supplementaires 50%', NULL, NULL, NULL,
 NULL, false, false, NULL, 4, false, NULL,
 'Majoration 50% - A configurer selon politique entreprise',
 NOW(), NOW(), 'HEURES_SUP', 'HEURE', 'GAIN'),

('RP005', 'HS100', 'Heures supplementaires 100%', NULL, NULL, NULL,
 NULL, false, false, NULL, 5, false, NULL,
 'Majoration 100% - A configurer selon politique entreprise',
 NOW(), NOW(), 'HEURES_SUP', 'HEURE', 'GAIN'),

-- SECTION 3: PRIMES (GAINS)
('RP006', 'PR_ANC', 'Prime danciennete', NULL, NULL, NULL,
 NULL, false, false, NULL, 6, false, NULL,
 'Prime anciennete - A configurer par les RH',
 NOW(), NOW(), 'PRIME', 'POURCENTAGE', 'GAIN'),

('RP007', 'PR_PAN', 'Prime de panier', NULL, NULL, NULL,
 NULL, false, false, NULL, 7, false, NULL,
 'Prime panier - A configurer par les RH',
 NOW(), NOW(), 'PRIME', 'FORFAIT', 'GAIN'),

('RP008', 'PR_RISQ', 'Prime de risque', NULL, NULL, NULL,
 NULL, false, false, NULL, 8, false, NULL,
 'Prime risque - A configurer par les RH',
 NOW(), NOW(), 'PRIME', 'POURCENTAGE', 'GAIN'),

('RP009', 'PR_PERF', 'Prime de performance', NULL, NULL, NULL,
 NULL, false, false, NULL, 9, false, NULL,
 'Prime performance - A configurer par les RH',
 NOW(), NOW(), 'PRIME', 'VARIABLE', 'GAIN'),

-- SECTION 4: INDEMNITES (GAINS)
('RP010', 'IND_TR', 'Indemnite de transport', NULL, NULL, NULL,
 NULL, false, false, NULL, 10, false, NULL,
 'Indemnite transport - A configurer par les RH',
 NOW(), NOW(), 'INDEMNITE', 'FORFAIT', 'GAIN'),

('RP011', 'IND_LOG', 'Indemnite de logement', NULL, NULL, NULL,
 NULL, false, false, NULL, 11, false, NULL,
 'Indemnite logement - A configurer par les RH',
 NOW(), NOW(), 'INDEMNITE', 'POURCENTAGE', 'GAIN'),

-- SECTION 5: ELEMENTS EXCEPTIONNELS (GAINS)
('RP012', '13EME', '13eme mois', NULL, NULL, NULL,
 NULL, false, false, NULL, 12, false, NULL,
 '13eme mois - A configurer par les RH',
 NOW(), NOW(), 'PRIME', 'CALCULE', 'EXCEPTIONNEL'),

('RP013', 'RAPPEL', 'Rappel de salaire', NULL, NULL, NULL,
 NULL, false, false, NULL, 13, false, NULL,
 'Rappel salaire - A configurer par les RH',
 NOW(), NOW(), 'SALAIRE', 'FORFAIT', 'EXCEPTIONNEL'),

('RP014', 'IND_CONG', 'Indemnite conge paye', NULL, NULL, NULL,
 NULL, false, false, NULL, 14, false, NULL,
 'Indemnite conges payes - A configurer par les RH',
 NOW(), NOW(), 'INDEMNITE', 'CALCULE', 'GAIN'),

-- SECTION 6: COTISATIONS SOCIALES (RETENUES OBLIGATOIRES)
('RP015', 'CNPS_SAL', 'CNaPS salarie', NULL, 5.0000, NULL,
 NULL, false, false, NULL, 15, false, NULL,
 'OBLIGATOIRE - Cotisation retraite salarie (5%) - Verifier avec comptable',
 NOW(), NOW(), 'COTISATION', 'POURCENTAGE', 'RETENUE'),

('RP016', 'OSTIE', 'OSTIE employeur', NULL, 1.0000, NULL,
 NULL, false, false, NULL, 16, false, NULL,
 'OBLIGATOIRE - Sante employeur (1%) - Verifier avec comptable',
 NOW(), NOW(), 'COTISATION', 'POURCENTAGE', 'RETENUE'),

-- SECTION 7: IMPOTS (RETENUES OBLIGATOIRES)
('RP017', 'IRSA', 'Precompte IRSA', NULL, NULL, NULL,
 NULL, false, false, NULL, 17, false, NULL,
 'OBLIGATOIRE - Impot sur le revenu selon bareme - Verifier avec comptable',
 NOW(), NOW(), 'IMPOT', 'CALCULE', 'RETENUE'),

-- SECTION 8: AUTRES RETENUES
('RP018', 'AVANCE', 'Avance sur salaire', NULL, NULL, NULL,
 NULL, false, false, NULL, 18, false, NULL,
 'Avance salaire - A configurer par les RH',
 NOW(), NOW(), 'RETENUE_DIV', 'FORFAIT', 'RETENUE'),

('RP019', 'ABS_NP', 'Absences non payees', NULL, NULL, NULL,
 NULL, false, false, NULL, 19, false, NULL,
 'Absences non payees - A configurer par les RH',
 NOW(), NOW(), 'RETENUE_DIV', 'CALCULE', 'RETENUE'),

('RP020', 'COT_SYN', 'Cotisation syndicale', NULL, 1.0000, NULL,
 NULL, false, false, NULL, 20, false, NULL,
 'Cotisation syndicale - A configurer par les RH',
 NOW(), NOW(), 'RETENUE_DIV', 'POURCENTAGE', 'RETENUE'),

-- SECTION 9: TOTAUX INTERMEDIAIRES (TOTAUX TECHNIQUES)
('RP021', 'T_BRUT', 'Total brut', NULL, NULL, 'SUM(gains)',
 NULL, false, false, NULL, 21, false, NULL,
 'Total technique calcule automatiquement',
 NOW(), NOW(), 'TOTAL_INT', 'CALCULE', 'TOTAL'),

('RP022', 'T_COT', 'Total cotisations', NULL, NULL, 'SUM(cotisations)',
 NULL, false, false, NULL, 22, false, NULL,
 'Total technique calcule automatiquement',
 NOW(), NOW(), 'TOTAL_INT', 'CALCULE', 'TOTAL'),

('RP023', 'NET_IMP', 'Net imposable', NULL, NULL, 'T_BRUT - T_COT',
 NULL, false, false, NULL, 23, false, NULL,
 'Total technique calcule automatiquement',
 NOW(), NOW(), 'TOTAL_INT', 'CALCULE', 'TOTAL'),

('RP024', 'T_RET', 'Total retenues', NULL, NULL, 'SUM(autres_retenues)',
 NULL, false, false, NULL, 24, false, NULL,
 'Total technique calcule automatiquement',
 NOW(), NOW(), 'TOTAL_INT', 'CALCULE', 'TOTAL'),

-- SECTION 10: NET A PAYER (TOTAL FINAL)
('RP025', 'NET_PAYER', 'Net a payer', NULL, NULL, 'T_BRUT - T_COT - IRSA - T_RET',
 NULL, false, false, NULL, 25, false, NULL,
 'Total technique calcule automatiquement - Montant verse a lemploye',
 NOW(), NOW(), 'TOTAL_INT', 'CALCULE', 'TOTAL');

--  Abréviation 
insert into abreviation(libelle, abreviation, description, created_at, modified_at) VALUES
('Salaire horaire', 'SH', 'Equivalent de salaire en heures', now(), now());
insert into abreviation(libelle, abreviation, description, created_at, modified_at) VALUES
('nombre', 'nb', 'quantité', now(), now());
insert into abreviation(libelle, abreviation, description, created_at, modified_at) VALUES
('taux', 'taux', 'taux de majoration', now(), now());
insert into abreviation(libelle, abreviation, description, created_at, modified_at) VALUES
('Salaire journalier', 'SJ', 'Equivalent de salaire par jour', now(), now());
insert into abreviation(libelle, abreviation, description, created_at, modified_at) VALUES
('base irsa', 'base_irsa', NULL, now(), now());

INSERT INTO abreviation (libelle, abreviation, description, created_at, modified_at)
VALUES
('HS50', 'HS50', NULL, NOW(), NOW()),
('HS100', 'HS100', NULL, NOW(), NOW()),
('PR_ANC', 'PR_ANC', NULL, NOW(), NOW()),
('PR_RISQ', 'PR_RISQ', NULL, NOW(), NOW()),
('COT_SYN', 'COT_SYN', NULL, NOW(), NOW()),
('ABS_NP', 'ABS_NP', NULL, NOW(), NOW()),
('OSTIE-EMP', 'OSTIE-EMP', NULL, NOW(), NOW()),
('13EME', '13EME', NULL, NOW(), NOW()),
('RAPPEL', 'RAPPEL', NULL, NOW(), NOW()),
('T_BRUT', 'T_BRUT', NULL, NOW(), NOW()),
('SBM', 'SBM', NULL, NOW(), NOW()),
('HS25', 'HS25', NULL, NOW(), NOW()),
('IND_LOG', 'IND_LOG', NULL, NOW(), NOW()),
('IND_TR', 'IND_TR', NULL, NOW(), NOW()),
('PR_PAN', 'PR_PAN', NULL, NOW(), NOW()),
('PR_PERF', 'PR_PERF', NULL, NOW(), NOW()),
('IND_CONG', 'IND_CONG', NULL, NOW(), NOW()),
('CNPS_SAL', 'CNPS_SAL', NULL, NOW(), NOW()),
('IRSA', 'IRSA', NULL, NOW(), NOW()),
('AVANCE', 'AVANCE', NULL, NOW(), NOW()),
('SB', 'SB', NULL, NOW(), NOW()),
('HS130', 'HS130', NULL, NOW(), NOW()),
('HS 150', 'HS150', NULL, NOW(), NOW()),
('HS130 EXO', 'HS130_EXO', NULL, NOW(), NOW()),
('HS150 EXO', 'HS150_EXO', NULL, NOW(), NOW()),
('PA', 'PA', NULL, NOW(), NOW()),
('MN30', 'MN30', NULL, NOW(), NOW()),
('AFAM', 'AFAM', NULL, NOW(), NOW()),
('AMENDE', 'AMENDE', NULL, NOW(), NOW()),
('OSTIE-SAL', 'OSTIE-SAL', NULL, NOW(), NOW()),
('tiana', 'TIANA', NULL, NOW(), NOW()),
('CNPS_PAT', 'CNPS_PAT', NULL, NOW(), NOW()),
('T_COT', 'T_COT', NULL, NOW(), NOW()),
('NET_IMP', 'NET_IMP', NULL, NOW(), NOW()),
('T_RET', 'T_RET', NULL, NOW(), NOW()),
('NET_PAYER', 'NET_PAYER', NULL, NOW(), NOW());


INSERT INTO abreviation (libelle, abreviation, description, created_at, modified_at)
VALUES
('Salaire horaire', 'SH', 'Salaire mensuel de base', NOW(), NOW()),
('Salaire journalier', 'SJ', 'Salaire utilisé comme référence pour les calculs', NOW(), NOW()),
('Salaire brut', 'SBRUT', 'Salaire total avant déductions', NOW(), NOW()),
('Salaire net', 'SNET', 'Salaire après déductions sociales et fiscales', NOW(), NOW()),
('Salaire brut imposable', 'SBRUT_IMP', 'Salaire brut soumis à l''impôt', NOW(), NOW()),
('Salaire net imposable', 'SNET_IMP', 'Salaire net soumis à l''impôt', NOW(), NOW());


('Salaire imposable', 'SIMPO', 'Salaire soumis à l''impôt', NOW()),
('Heures normales', 'HN', 'Heures de travail normales', NOW()),
('Heures supplémentaires', 'HS', 'Heures supplémentaires travaillées', NOW()),
('Heures supplémentaires 25%', 'HS25', 'Heures supplémentaires majorées à 25%', NOW()),
('Heures supplémentaires 50%', 'HS50', 'Heures supplémentaires majorées à 50%', NOW()),
('Heures supplémentaires 100%', 'HS100', 'Heures supplémentaires majorées à 100%', NOW()),
('Heures de nuit', 'HNuit', 'Heures travaillées de nuit', NOW()),
('Heures de dimanche', 'HDim', 'Heures travaillées le dimanche', NOW()),
('Heures de jour férié', 'HFer', 'Heures travaillées les jours fériés', NOW());

-- Cotisations sociales (CNaPS, OSTIE, etc.)
INSERT INTO abreviation (libelle, abreviation, description, created_at) VALUES
('Caisse Nationale de Prévoyance Sociale Salarié', 'CNAPS_S', 'Cotisation salariale CNaPS (1%)', NOW()),
('Caisse Nationale de Prévoyance Sociale Employeur', 'CNAPS_E', 'Cotisation employeur CNaPS (13%)', NOW()),
('Organisme de Santé au Travail et d''Inspection du Travail Salarié', 'OSTIE_S', 'Cotisation salariale OSTIE (1%)', NOW()),
('Organisme de Santé au Travail et d''Inspection du Travail Employeur', 'OSTIE_E', 'Cotisation employeur OSTIE (5%)', NOW()),
('Assurance Vieillesse', 'AV', 'Assurance vieillesse (retraite)', NOW()),
('Assurance Maladie', 'AM', 'Assurance maladie', NOW()),
('Assurance Maternité', 'AMAT', 'Assurance maternité', NOW()),
('Assurance Invalidité Décès', 'AID', 'Assurance invalidité décès', NOW()),
('Prestations Familiales', 'PF', 'Allocations familiales', NOW());

-- Primes et avantages 
INSERT INTO abreviation (libelle, abreviation, description, created_at) VALUES
('Prime d''ancienneté', 'PA', 'Prime basée sur l''ancienneté', NOW()),
('Prime de rendement', 'PR', 'Prime liée à la performance', NOW()),
('Prime de transport', 'PT', 'Indemnité de transport', NOW()),
('Prime de logement', 'PL', 'Indemnité de logement', NOW()),
('Prime de panier', 'PP', 'Indemnité repas/panier', NOW()),
('Prime de risque', 'PRISQ', 'Prime pour travaux dangereux', NOW()),
('Prime de fonction', 'PFCT', 'Prime liée à la fonction occupée', NOW()),
('Prime de technicité', 'PTECH', 'Prime pour compétences techniques', NOW()),
('Prime de treizième mois', 'P13', 'Prime de fin d''année', NOW()),
('Prime de vacances', 'PVAC', 'Prime de congés payés', NOW()),
('Indemnité de congé', 'IC', 'Indemnité pour congés pris', NOW()),
('Indemnité de licenciement', 'IL', 'Indemnité de rupture de contrat', NOW()),
('Indemnité de préavis', 'IP', 'Indemnité de préavis', NOW());

-- Déductions et retenues 
INSERT INTO abreviation (libelle, abreviation, description, created_at) VALUES
('Impôt sur le revenu des salaires', 'IRSA', 'Impôt retenu à la source', NOW()),
('Retenue avance sur salaire', 'RAS', 'Avance sur salaire remboursée', NOW()),
('Retenue prêt personnel', 'RPP', 'Remboursement de prêt personnel', NOW()),
('Retenue coopérative', 'RCOOP', 'Cotisation coopérative', NOW()),
('Retenue mutuelle', 'RMUT', 'Cotisation mutuelle santé', NOW()),
('Retenue syndicale', 'RSYN', 'Cotisation syndicale', NOW()),
('Retenue assurance groupe', 'RAG', 'Assurance groupe déduite', NOW());

-- Congés et absences 
INSERT INTO abreviation (libelle, abreviation, description, created_at) VALUES
('Congés payés', 'CP', 'Jours de congés payés', NOW()),
('Congés sans solde', 'CSS', 'Congés non rémunérés', NOW()),
('Congés maladie', 'CMAL', 'Congés pour maladie', NOW()),
('Congés maternité', 'CMAT', 'Congés de maternité', NOW()),
('Congés paternité', 'CPAT', 'Congés de paternité', NOW()),
('Absences non justifiées', 'ANJ', 'Absences non rémunérées', NOW()),
('Absences justifiées', 'AJ', 'Absences justifiées payées', NOW()),
('Jours fériés', 'JF', 'Jours fériés payés', NOW()),
('RTT', 'RTT', 'Réduction du temps de travail', NOW());

-- Autres éléments de paie 
INSERT INTO abreviation (libelle, abreviation, description, created_at) VALUES
('Taux horaire', 'TH', 'Taux de rémunération horaire', NOW()),
('Taux journalier', 'TJ', 'Taux de rémunération journalier', NOW()),
('Nombre de jours travaillés', 'NJT', 'Nombre de jours effectivement travaillés', NOW()),
('Nombre de jours ouvrables', 'NJO', 'Nombre de jours ouvrables dans le mois', NOW()),
('Nombre de jours calendaires', 'NJC', 'Nombre de jours du mois', NOW()),
('Majoration', 'MAJ', 'Pourcentage de majoration', NOW()),
('Part patronale', 'PPAT', 'Part prise en charge par l''employeur', NOW()),
('Part salariale', 'PSAL', 'Part déduite du salarié', NOW()),
('Forfait jour', 'FJ', 'Rémunération en forfait jours', NOW()),
('Forfait heure', 'FH', 'Rémunération en forfait heures', NOW()),
('Salaire minimum interprofessionnel garanti', 'SMIG', 'Salaire minimum légal', NOW()),
('Salaire minimum d''embauche', 'SME', 'Salaire minimum d''embauche', NOW());

-- Formules et calculs spécifiques
INSERT INTO abreviation (libelle, abreviation, description, created_at) VALUES
('Base de calcul CNaPS', 'BC_CNAPS', 'Base utilisée pour calculer les cotisations CNaPS', NOW()),
('Base de calcul OSTIE', 'BC_OSTIE', 'Base utilisée pour calculer les cotisations OSTIE', NOW()),
('Base de calcul IRS', 'BC_IRS', 'Base imposable pour l''IRS', NOW()),
('Plafond de sécurité sociale', 'PSS', 'Plafond mensuel de sécurité sociale', NOW()),
('Tranche IRS', 'TIRS', 'Tranche d''imposition pour l''IRS', NOW()),
('Abattement', 'ABATT', 'Abattement fiscal', NOW()),
('Déduction forfaitaire', 'DF', 'Déduction forfaitaire sur salaire', NOW()),
('Frais professionnels', 'FPRO', 'Frais professionnels déductibles', NOW());

-- Types de demande d'absence basiques
INSERT INTO type_demande (id, type, created_at, modified_at) VALUES
('MEDICAL', 'Absence médicale', NOW(), NOW()),
('FAMILLE', 'Absence familiale', NOW(), NOW()),
('ADMIN', 'Absence administrative', NOW(), NOW()),
('PERSO', 'Absence personnelle', NOW(), NOW()),
('FORMATION', 'Absence formation', NOW(), NOW()),
('AUTRE', 'Autre absence', NOW(), NOW());

INSERT INTO information_societe(nom_company, logo, created_at, modified_at)
VALUES('SMARTDEV SOLUTIONS', 'uploads/logo/logo.png', NOW(), NOW());

 -- Insertion des 12 mois de l'année
INSERT INTO mois (libelle, created_at, modified_at) VALUES
('Janvier', NOW(), NOW()),
('Février', NOW(), NOW()),
('Mars', NOW(), NOW()),
('Avril', NOW(), NOW()),
('Mai', NOW(), NOW()),
('Juin', NOW(), NOW()),
('Juillet', NOW(), NOW()),
('Août', NOW(), NOW()),
('Septembre', NOW(), NOW()),
('Octobre', NOW(), NOW()),
('Novembre', NOW(), NOW()),
('Décembre', NOW(), NOW());

INSERT INTO base_irsa (id, tranche_min, tranche_max, taux, num_tranche, created_at, modified_at)
VALUES
('IRSA_T1', 0, 350000, 0, 1, NOW(), NOW()),
('IRSA_T2', 350001, 400000, 5, 2, NOW(), NOW()),
('IRSA_T3', 400001, 500000, 10, 3, NOW(), NOW()),
('IRSA_T4', 500001, 600000, 15, 4, NOW(), NOW()),
('IRSA_T5', 600001, 4000000, 20, 5, NOW(), NOW()),
('IRSA_T6', 4000001, NULL, 25, 6, NOW(), NOW());

update regle_gestion_conges set anciennete_requis_prime = 5;
update regle_gestion_conges set prime_anciennete = 1000;
update regle_gestion_conges set allocation_familiale = 10000;

-- Création de la séquence
CREATE SEQUENCE paie_fille_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;

-- Insertion des 22 régions de Madagascar
INSERT INTO region (id, nom, created_at, modified_at) VALUES
('RG01', 'Analamanga', NOW(), NULL),
('RG02', 'Vakinankaratra', NOW(), NULL),
('RG03', 'Itasy', NOW(), NULL),
('RG04', 'Bongolava', NOW(), NULL),
('RG05', 'Haute Matsiatra', NOW(), NULL),
('RG06', 'Amoron''i Mania', NOW(), NULL),
('RG07', 'Atsinanana', NOW(), NULL),
('RG08', 'Analanjirofo', NOW(), NULL),
('RG09', 'Sava', NOW(), NULL),
('RG10', 'Diana', NOW(), NULL),
('RG11', 'Sofia', NOW(), NULL),
('RG12', 'Boeny', NOW(), NULL),
('RG13', 'Melaky', NOW(), NULL),
('RG14', 'Betsiboka', NOW(), NULL),
('RG15', 'Alaotra-Mangoro', NOW(), NULL),
('RG16', 'Androy', NOW(), NULL),
('RG17', 'Anosy', NOW(), NULL),
('RG18', 'Atsimo-Andrefana', NOW(), NULL),
('RG19', 'Atsimo-Atsinanana', NOW(), NULL),
('RG20', 'Menabe', NOW(), NULL),
('RG21', 'Haute Matsiatra', NOW(), NULL),
('RG22', 'Diana', NOW(), NULL);

-- Insertion des catégories professionnelles pour Madagascar
INSERT INTO categorie_professionnelle (id, code, libelle, description, created_at, modified_at) VALUES
('CP01', 'A', 'Groupe A', 'Cadres supérieurs et personnels de direction', NOW(), NULL),
('CP02', 'B', 'Groupe B', 'Cadres intermédiaires et techniciens', NOW(), NULL),
('CP03', 'C', 'Groupe C', 'Employés et agents administratifs', NOW(), NULL),
('CP04', 'D', 'Groupe D', 'Ouvriers qualifiés', NOW(), NULL),
('CP05', 'E', 'Groupe E', 'Ouvriers non qualifiés', NOW(), NULL);


-- Types d'entrée du salarié dans l'entreprise
INSERT INTO type_entree (id, libelle, created_at, modified_at) VALUES
('TE01', 'Recrutement externe', NOW(), NULL),
('TE02', 'Stage suivi d''embauche', NOW(), NULL),
('TE03', 'Mutation interne', NOW(), NULL),
('TE04', 'Réembauche', NOW(), NULL),
('TE05', 'Intégration après intérim', NOW(), NULL),
('TE06', 'Promotion interne', NOW(), NULL),
('TE07', 'Reprise de contrat', NOW(), NULL);
('TE08', 'Autre', now(), now()),
('TE09', 'Autre', now(), now());

-- Types de temps de travail
INSERT INTO type_temps_travail (temps_travail, created_at, modified_at) VALUES
('Temps plein', NOW(), NULL),
('Temps partiel', NOW(), NULL),
('Horaire réduit', NOW(), NULL),
('Forfait jour', NOW(), NULL);

-- Catégories professionnelles selon la pratique du droit du travail malgache
INSERT INTO categorie_professionnelle (id, code, libelle, description, created_at, modified_at) VALUES
('CPG01', 'G1', 'Groupe 1', 'Manœuvres, ouvriers non qualifiés, agents d''exécution sans qualification', NOW(), NULL),
('CPG02', 'G2', 'Groupe 2', 'Ouvriers qualifiés, employés d''exécution avec expérience ou formation de base', NOW(), NULL),
('CPG03', 'G3', 'Groupe 3', 'Employés qualifiés, techniciens, agents de maîtrise', NOW(), NULL),
('CPG04', 'G4', 'Groupe 4', 'Cadres moyens, chefs d''équipe, responsables de service', NOW(), NULL),
('CPG05', 'G5', 'Groupe 5', 'Cadres supérieurs, direction, management stratégique', NOW(), NULL);

INSERT INTO regles_annulation_conges (
    id,
    delai_min_jours,
    duree_max_jours,
    besoin_validation_manager,
    besoin_validation_rh,
    actif
) VALUES (
    'REG-CONG',
    2,      -- Annulation possible au moins 2 jours avant le début du congé
    30,     -- Durée maximale du congé pouvant être demandée
    false,   -- Validation du manager requise
    false,   -- Validation RH requise
    true    -- Règle active
);

SELECT ip FROM Infos_Professionnelles ip WHERE ip.employe.id = 'EMP-20260122-C4C545' AND ip.statut = 0 
AND (ip.dateFinAssignationPoste IS NULL OR ip.dateFinAssignationPoste >= CURRENT_DATE) 
ORDER BY ip.dateDebutAssignationPoste DESC;

INSERT INTO type_paiement (id, code, libelle, created_at, modified_at) VALUES
('TP001', 'BANCAIRE', 'Compte bancaire', NOW(), NOW()),
('TP002', 'MOBILE_MONEY', 'Mobile Money', NOW(), NOW()),
('TP003', 'ESPECES', 'Espèces', NOW(), NOW());

-- Insérer la configuration par défaut
INSERT INTO preparation_export_employe (
    is_matricule,
    is_nom,
    is_prenom,
    is_date_naissance,
    is_email,
    is_cin,
    is_lieu_naissance,
    is_telephone,
    is_code_postal,
    is_adresse,
    is_num_cnaps,
    is_num_ostie,
    is_nom_complet_mere,
    is_nom_complet_pere,
    is_nb_enfants,
    is_nom_conjoint,
    is_mode_paiement_nom_banque,
    is_mode_paiement_code_banque,
    is_mode_paiement_code_guichet,
    is_info_pro_date_embauche,
    is_info_pro_salaire_base,
    is_info_pro_classification,
    is_info_pro_periodicite_paiement,
    is_info_pro_categorie,
    is_info_pro_poste,
    is_info_pro_departement,
    is_situation_familiale,
    is_nationalite,
    is_sexe,
    is_emergency_contact_telephone,
    is_emergency_contact_nom,
    is_emergency_contact_email,
    created_at
) VALUES (
    TRUE, TRUE, TRUE, FALSE, FALSE, FALSE, FALSE, FALSE, FALSE, FALSE,
    FALSE, FALSE, FALSE, FALSE, FALSE, FALSE, FALSE, FALSE, FALSE,
    TRUE, TRUE, FALSE, FALSE, FALSE, TRUE, TRUE,
    FALSE, FALSE, FALSE, FALSE, FALSE, FALSE,
    NOW()
);