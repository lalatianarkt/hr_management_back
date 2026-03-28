-- Donnees de test RH (Smartdev Solutions)
-- Remplacez les IDs si votre schema utilise d'autres formats.

BEGIN;

-- 1) Tables de reference
INSERT INTO sexe (id, sexe, code)
VALUES (1, 'MASCULIN', 'M')
ON CONFLICT DO NOTHING;

INSERT INTO nationalite (id, nationalite)
VALUES (1, 'Malagasy')
ON CONFLICT DO NOTHING;

INSERT INTO situation_familiale (id, type)
VALUES (1, 'MARIE')
ON CONFLICT DO NOTHING;

INSERT INTO region (id, nom, created_at, modified_at)
VALUES ('REG-AN', 'Analamanga', NOW(), NOW())
ON CONFLICT DO NOTHING;

INSERT INTO emergency_contact (id, contact, email, adresse, created_at, modified_at)
VALUES ('EC-001', 'Rasoa Rakoto', 'urgence@test.smartdev.mg', 'Lot II B 45, Antananarivo', NOW(), NOW())
ON CONFLICT DO NOTHING;

INSERT INTO departement (id, nom, description, statut, created_at, modified_at)
VALUES ('DEP-RH', 'Ressources Humaines', 'Gestion RH', 0, NOW(), NOW())
ON CONFLICT DO NOTHING;

INSERT INTO niveau_hierarchique (id, nom, rang, description, created_at, modified_at)
VALUES ('NIV-3', 'Chef de Service', 3, 'Niveau manager', NOW(), NOW())
ON CONFLICT DO NOTHING;

INSERT INTO poste (id, nom, description, created_at, modified_at, id_departement, id_niveau)
VALUES ('POST-RH-001', 'Responsable RH', 'Responsable du departement RH', NOW(), NOW(), 'DEP-RH', 'NIV-3')
ON CONFLICT DO NOTHING;

INSERT INTO categorie_professionnelle (id, code, libelle, description, created_at, modified_at)
VALUES ('CAT-A', 'A1', 'Cadre', 'Cadre administratif', NOW(), NOW())
ON CONFLICT DO NOTHING;

INSERT INTO type_entree (id, nom, created_at, modified_at)
VALUES ('TE-REC', 'Recrutement', NOW(), NOW())
ON CONFLICT DO NOTHING;

INSERT INTO type_contrat (id, intitule, description, duree_max_mois, created_at, modified_at)
VALUES ('TC-CDI', 'CDI', 'Contrat a duree indeterminee', 0, NOW(), NOW())
ON CONFLICT DO NOTHING;

-- 2) Employe manager (necessaire pour la FK manager)
INSERT INTO employe (
    id, nom, prenom, date_naissance, lieu_naissance, telephone, email, statut,
    adresse, created_at, modified_at, nom_mere, nom_pere, id_emergency_contact,
    nb_enfants, nom_conjoint, etat_civil, id_sexe, id_nationalite, num_cnaps, cin,
    num_ostie, code_postal, id_region
) VALUES (
    'EMP-MGR-0001', 'Rakoto', 'Hery', '1985-06-15', 'Antananarivo', '0341234567',
    'hery.rakoto@smartdev.mg', 0, 'Lot III D 12, Antananarivo', NOW(), NOW(),
    'Rasoa', 'Razafy', 'EC-001', 2, 'Mina Rakoto', 'MARIE', '1', '1',
    'CNP-0001', 'CIN1234567890', 'OST-0001', '101', 'REG-AN'
);

INSERT INTO manager (
    id, date_debut, date_fin, created_at, modified_at, statut, id_departement, id_employe, commentaire
) VALUES (
    'MGR-0001', '2024-01-10', NULL, NOW(), NOW(), 0, 'DEP-RH', 'EMP-MGR-0001', 'Manager RH'
);

-- 3) Employe test (cible)
INSERT INTO employe (
    id, nom, prenom, date_naissance, lieu_naissance, telephone, email, statut,
    adresse, created_at, modified_at, nom_mere, nom_pere, id_emergency_contact,
    nb_enfants, nom_conjoint, etat_civil, id_sexe, id_nationalite, num_cnaps, cin,
    num_ostie, code_postal, id_region
) VALUES (
    'EMP-TEST-0001', 'Randriamisoa', 'Fetra', '1996-11-04', 'Antsirabe', '0329876543',
    'fetra.randriamisoa@smartdev.mg', 0, 'Lot IV A 21, Antsirabe', NOW(), NOW(),
    'Ramaroson', 'Andriamatoa', 'EC-001', 1, 'Lala Randriamisoa', 'MARIE', '1', '1',
    'CNP-0101', 'CIN9876543210', 'OST-0101', '110', 'REG-AN'
);

-- 4) Infos professionnelles
INSERT INTO infos_professionnelles (
    id, date_embauche, date_debauche, statut, created_at, modified_at,
    date_debut_assignation_poste, date_fin_assignation_poste, salaire_base,
    motif_depart, classification, periode_debut_paiement, periode_fin_paiement,
    periodicite_paiement, type_temps_travail, unicite_temps_travail,
    id_categorie, id_departement, id_type_entree, id_manager, id_poste,
    id_type_contrat, id_employe
) VALUES (
    'INFPRO-0001', '2025-02-01', NULL, 0, NOW(), NOW(),
    '2025-02-01', NULL, 850000.00, NULL, 'C1', '2025-02-01', NULL,
    'MENSUEL', 'PLEIN', 'HEBDO',
    'CAT-A', 'DEP-RH', 'TE-REC', 'MGR-0001', 'POST-RH-001',
    'TC-CDI', 'EMP-TEST-0001'
);

-- 5) Type user + user
INSERT INTO type_user (id, type, created_at, modified_at)
VALUES (1, 'Employe', NOW(), NOW())
ON CONFLICT DO NOTHING;

INSERT INTO users (id, email, password, created_at, modified_at, statut, id_type_user, id_employe)
VALUES ('USR-TEST-0001', 'fetra.randriamisoa@smartdev.mg', 'password123', NOW(), NOW(), 1, 1, 'EMP-TEST-0001')
ON CONFLICT DO NOTHING;

-- 6) Type paiement + mode paiement
INSERT INTO type_paiement (id, code, libelle, created_at, modified_at)
VALUES ('TP-BANK', 'VIR', 'Virement bancaire', NOW(), NOW())
ON CONFLICT DO NOTHING;

INSERT INTO mode_paiement (
    id, nom_banque, code_banque, code_guichet, numero_compte, telephone_mobile,
    cle_rib, titulaire_compte, domiciliation_agence, created_at, modified_at,
    est_actif, est_par_defaut, id_type_paiement, id_employe
) VALUES (
    'MP-0001', 'BFV', '00010', '01234', '12345678901', '0329876543',
    '12', 'Fetra Randriamisoa', 'Antananarivo', NOW(), NOW(),
    TRUE, TRUE, 'TP-BANK', 'EMP-TEST-0001'
);

COMMIT;
