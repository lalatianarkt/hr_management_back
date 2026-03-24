CREATE TABLE emergency_contact(
   id VARCHAR(50) ,
   contact VARCHAR(50) ,
   email VARCHAR(50) ,
   adresse VARCHAR(50) ,
   created_at TIMESTAMP NOT NULL,
   modified_at TIMESTAMP,
   PRIMARY KEY(id)
);

-- CREATE TABLE infos_professionnelles(
--    id VARCHAR(50) ,
--    date_embauche DATE NOT NULL,
--    created_at TIMESTAMP NOT NULL,
--    modified_at TIMESTAMP,
--    id_manager VARCHAR(50) ,
--    id_departement VARCHAR(50) ,
--    id_poste VARCHAR(50)  NOT NULL,
--    id_type_contrat VARCHAR(50)  NOT NULL,
--    id_employe VARCHAR(50)  NOT NULL,
--    date_debut DATE NOT NULL,
--    date_fin DATE,
--    PRIMARY KEY(id),
--    FOREIGN KEY(id_manager) REFERENCES Employe(id),
--    FOREIGN KEY(id_departement) REFERENCES Departement(id),
--    FOREIGN KEY(id_poste) REFERENCES Poste(id),
--    FOREIGN KEY(id_type_contrat) REFERENCES Type_contrat(id),
--    FOREIGN KEY(id_employe) REFERENCES Employe(id)
-- );



-- alter table infos_professionnelles add column date_debut date default '2025-11-07';
-- alter table infos_professionnelles add column date_fin date; 

-- ALTER TABLE infos_professionnelles 
-- DROP CONSTRAINT manager_employe_id_employe_fkey;

-- alter table infos_professionnelles 
-- add column id_manager VARCHAR(50);

-- alter table infos_professionnelles 
-- add CONSTRAINT manager_

-- alter table infos_professionnelles drop column id_manager;

CREATE TYPE etat_civil_enum AS ENUM (
    'CELIBATAIRE',
    'MARIE',
    'DIVORCE',
    'VEUF'
);

alter table employe add column num_ostie VARCHAR(50);
alter table employe update column code_postal integer default 101;
alter table employe add column id_region VARCHAR(50) not null default 'RG01';
ALTER TABLE employe
ADD CONSTRAINT fk_region_id_region
FOREIGN KEY (id_region)
REFERENCES region(id);

CREATE TABLE Employe(
   id VARCHAR(50) ,
   nom VARCHAR(100)  NOT NULL,
   prenom VARCHAR(250)  NOT NULL,
   date_naissance DATE NOT NULL,
   lieu_naissance VARCHAR(250)  NOT NULL,
   telephone VARCHAR(12)  NOT NULL,
   email VARCHAR(100)  NOT NULL,
   statut INTEGER NOT NULL,
   adresse VARCHAR(255)  NOT NULL,
   created_at TIMESTAMP NOT NULL,
   modified_at TIMESTAMP,
   nom_mere VARCHAR(255) ,
   nom_pere VARCHAR(255) ,
   id_emergency_contact VARCHAR(50)  NOT NULL,
   nb_enfants INTEGER,
   nom_conjoint VARCHAR(255) ,
   etat_civil etat_civil_enum NOT NULL DEFAULT 'CELIBATAIRE',
   id_sexe VARCHAR(50)  NOT NULL,
   id_nationalite VARCHAR(50) not NULL,
   num_cnaps VARCHAR(50),
   cin VARCHAR(12) NOT NULL,
   num_ostie VARCHAR(50) ,
   code_postal VARCHAR(50) ,
   id_region VARCHAR(50)  NOT NULL,
   FOREIGN KEY(id_region) REFERENCES region(id),
   FOREIGN KEY(id_sexe) REFERENCES sexe(id),
   PRIMARY KEY(id),
   FOREIGN KEY(id_nationalite) REFERENCES nationalite(id),
   FOREIGN KEY(id_emergency_contact) REFERENCES emergency_contact(id),
   -- Contrainte d'intégrité
   CONSTRAINT conjoint_si_marie 
        CHECK ((etat_civil = 'MARIE' AND nom_conjoint IS NOT NULL) 
               OR (etat_civil != 'MARIE' AND nom_conjoint IS NULL)),
   UNIQUE(cin)
);



CREATE TABLE categorie_professionnelle(
   id VARCHAR(50) ,
   code VARCHAR(10) ,
   libelle TEXT,
   description TEXT,
   created_at TIMESTAMP NOT NULL,
   modified_at TIMESTAMP,
   PRIMARY KEY(id)
);

CREATE TABLE type_entree(
   id VARCHAR(50) ,
   nom VARCHAR(255) ,
   created_at TIMESTAMP NOT NULL,
   modified_at TIMESTAMP,
   PRIMARY KEY(id)
);

CREATE TABLE infos_professionnelles(
   id VARCHAR(50) ,
   date_embauche DATE NOT NULL,
   date_debauche DATE,
   statut INTEGER NOT NULL,
   created_at TIMESTAMP NOT NULL,
   modified_at TIMESTAMP,
   date_debut_assignation_poste DATE NOT NULL,
   date_fin_assignation_poste DATE,
   salaire_base NUMERIC(15,2)  ,
   motif_depart TEXT,
   classification VARCHAR(20) ,
   periode_debut_paiement DATE,
   periode_fin_paiement DATE,
   periodicite_paiement VARCHAR(50) ,
   type_temps_travail VARCHAR(50) ,
   unicite_temps_travail VARCHAR(50) ,
   id_categorie VARCHAR(50) ,
   id_departement VARCHAR(50)  NOT NULL,
   id_type_entree VARCHAR(50)  NOT NULL,
   id_manager VARCHAR(50)  NOT NULL,
   id_poste VARCHAR(50)  NOT NULL,
   id_type_contrat VARCHAR(50)  NOT NULL,
   id_employe VARCHAR(50)  NOT NULL,
   PRIMARY KEY(id),
   FOREIGN KEY(id_categorie) REFERENCES categorie_professionnelle(id),
   FOREIGN KEY(id_departement) REFERENCES Departement(id),
   FOREIGN KEY(id_type_entree) REFERENCES type_entree(id),
   FOREIGN KEY(id_manager) REFERENCES manager(id),
   FOREIGN KEY(id_poste) REFERENCES Poste(id),
   FOREIGN KEY(id_type_contrat) REFERENCES Type_contrat(id),
   FOREIGN KEY(id_employe) REFERENCES Employe(id)
);

CREATE TABLE type_temps_travail(
   id SERIAL,
   temps_travail VARCHAR(50) ,
   created_at TIMESTAMP NOT NULL,
   modified_at TIMESTAMP,
   PRIMARY KEY(id)
);

alter table infos_Professionnelles add column id_categorie VARCHAR(50);
alter table infos_Professionnelles add CONSTRAINT fk_categorie_professionnelle_id 
FOREIGN key(id_categorie) REFERENCES categorie_professionnelle(id);
alter table infos_Professionnelles  add column id_type_entree VARCHAR(50);
alter table infos_professionnelles add CONSTRAINT fk_type_entree_id
FOREIGN key(id_type_entree) REFERENCES type_entree(id);
alter table infos_professionnelles add column classification VARCHAR(10);
alter table infos_professionnelles add column id_temps_travail integer;
alter table infos_professionnelles add CONSTRAINT fk_type_temps_travail
FOREIGN key(id_temps_travail) REFERENCES type_temps_travail(id);

CREATE TABLE region(
   id VARCHAR(50) ,
   nom VARCHAR(255) ,
   created_at TIMESTAMP NOT NULL,
   modified_at TIMESTAMP,
   PRIMARY KEY(id)
);

CREATE TABLE district(
   id VARCHAR(50) ,
   nom VARCHAR(255) ,
   created_at TIMESTAMP NOT NULL,
   modified_at TIMESTAMP,
   id_region VARCHAR(50)  NOT NULL,
   PRIMARY KEY(id),
   FOREIGN KEY(id_region) REFERENCES region(id)
);

CREATE TABLE commune(
   id VARCHAR(50) ,
   nom VARCHAR(255) ,
   created_at TIMESTAMP NOT NULL,
   modified_at TIMESTAMP,
   id_district VARCHAR(50)  NOT NULL,
   PRIMARY KEY(id),
   FOREIGN KEY(id_district) REFERENCES district(id)
);

-- alter table employe drop column cin; 
-- alter table employe add column cin VARCHAR(50) UNIQUE not null;

CREATE TABLE type_entree(
   id VARCHAR(50) ,
   libelle VARCHAR(255) ,
   created_at TIMESTAMP NOT NULL,
   modified_at TIMESTAMP,
   PRIMARY KEY(id)
);

CREATE TABLE infos_professionnelles(
   id VARCHAR(50),
   date_embauche DATE NOT NULL,
   date_debauche DATE,
   created_at TIMESTAMP NOT NULL,
   modified_at TIMESTAMP,
   date_debut_assignation_poste DATE NOT NULL,
   date_fin_assignation_poste DATE,
   statut INTEGER NOT NULL,
   id_type_entree VARCHAR(50)  NOT NULL,
   id_manager VARCHAR(50),
   id_poste VARCHAR(50)  NOT NULL,
   id_type_contrat VARCHAR(50)  NOT NULL,
   id_departement VARCHAR(50)  NOT NULL,
   id_employe VARCHAR(50)  NOT NULL,
   salaire_base NUMERIC(15,2),
   matricule VARCHAR(50) not null UNIQUE,
   motif_depart TEXT,
   PRIMARY KEY(id),
   FOREIGN KEY(id_manager) REFERENCES manager(id),
   FOREIGN KEY(id_poste) REFERENCES Poste(id),
   FOREIGN KEY(id_departement) REFERENCES Departement(id),
   FOREIGN KEY(id_type_entree) REFERENCES type_entree(id),
   FOREIGN KEY(id_type_contrat) REFERENCES Type_contrat(id),
   FOREIGN KEY(id_employe) REFERENCES Employe(id)
); 
-- D'abord, ajouter la colonne pour la clé étrangère
ALTER TABLE infos_professionnelles 
ADD COLUMN id_departement VARCHAR(50);

-- Ensuite, ajouter la contrainte de clé étrangère
ALTER TABLE infos_professionnelles 
ADD CONSTRAINT fk_infos_professionnelles_departement 
FOREIGN KEY (id_departement) 
REFERENCES departement(id_departement);

-- Ajouter la colonne
ALTER TABLE infos_professionnelles 
ADD COLUMN id_type_entree VARCHAR(50);

-- Ajouter la contrainte de clé étrangère
ALTER TABLE infos_professionnelles 
ADD CONSTRAINT fk_infos_professionnelles_type_entree 
FOREIGN KEY (id_type_entree) 
REFERENCES type_entree(id_type_entree);

CREATE TABLE periode_paie(
   id VARCHAR(50) ,
   date_debut DATE,
   date_fin DATE,
   statut INTEGER,
   created_at TIMESTAMP NOT NULL,
   modified_at TIMESTAMP,
   PRIMARY KEY(id)
);

-- alter table infos_professionnelles add column motif_depart TEXT;
-- alter table infos_professionnelles add column matricule VARCHAR(50) Unique;
-- alter table infos_professionnelles add column salaire_base NUMERIC(15,2);
-- alter table infos_professionnelles drop column date_debut;
-- alter table infos_professionnelles add column date_debut_assignation_poste date NOT NULL;
-- alter table infos_professionnelles add_column date_fin_assignation_poste DATE;

-- -- Supprimer dans l'ordre logique
-- BEGIN;

-- -- 1. D'abord supprimer la colonne matricule
-- ALTER TABLE employe DROP COLUMN IF EXISTS matricule;

-- -- 2. Ensuite supprimer la contrainte de clé étrangère
-- ALTER TABLE employe DROP CONSTRAINT IF EXISTS employe_id_info_admin_fkey;

-- -- 3. Finalement supprimer la colonne id_info_admin
-- ALTER TABLE employe DROP COLUMN IF EXISTS id_info_admin;

-- COMMIT;

-- -- 1. Créer le type ENUM d'abord
-- CREATE TYPE etat_civil_enum AS ENUM (
--     'CELIBATAIRE',
--     'MARIE',
--     'DIVORCE',
--     'VEUF'
-- );

-- -- 2. Ajouter les colonnes sans NOT NULL d'abord (si table non vide)
-- ALTER TABLE employe 
-- ADD COLUMN nom_conjoint VARCHAR(255);

-- ALTER TABLE employe 
-- ADD COLUMN nb_enfants INTEGER;

-- ALTER TABLE employe 
-- ADD COLUMN etat_civil etat_civil_enum DEFAULT 'CELIBATAIRE';

-- ALTER TABLE employe 
-- ADD COLUMN cin INTEGER;

-- ALTER TABLE employe 
-- ADD COLUMN num_cnaps VARCHAR(50);

-- -- 3. Mettre à jour les valeurs existantes si nécessaire
-- -- (Adapter selon vos données existantes)
-- UPDATE employe 
-- SET etat_civil = 'CELIBATAIRE' 
-- WHERE etat_civil IS NULL;

-- -- 4. Modifier la colonne pour ajouter NOT NULL (maintenant que toutes les lignes ont une valeur)
-- ALTER TABLE employe 
-- ALTER COLUMN etat_civil SET NOT NULL;

-- -- 5. Ajouter la contrainte d'intégrité entre etat_civil et nom_conjoint
-- ALTER TABLE employe
-- ADD CONSTRAINT check_conjoint_si_marie 
-- CHECK (
--     (etat_civil = 'MARIE' AND nom_conjoint IS NOT NULL) 
--     OR 
--     (etat_civil != 'MARIE' AND nom_conjoint IS NULL)
-- );

-- -- 6. Optionnel : Ajouter des contraintes d'unicité si nécessaire
-- ALTER TABLE employe
-- ADD CONSTRAINT unique_cin UNIQUE (cin);

-- ALTER TABLE employe
-- ADD CONSTRAINT unique_num_cnaps UNIQUE (num_cnaps);

CREATE TABLE sexe(
   id SERIAL,
   sexe VARCHAR(50)  NOT NULL,
   code VARCHAR(1) ,
   PRIMARY KEY(id)
);

CREATE TABLE type_user(
   id SERIAL,
   type VARCHAR(50)  NOT NULL,
   created_at TIMESTAMP NOT NULL,
   modified_at TIMESTAMP,
   PRIMARY KEY(id)
);

CREATE TABLE Users(
   id VARCHAR(50) ,
   email VARCHAR(150)  NOT NULL,
   password VARCHAR(150)  NOT NULL,
   created_at TIMESTAMP NOT NULL,
   modified_at TIMESTAMP,
   id_type_user INTEGER NOT NULL,
   id_employe VARCHAR(50)  NOT NULL,
   PRIMARY KEY(id),
   UNIQUE(email),
   FOREIGN KEY(id_type_user) REFERENCES type_user(id), --type ohatra hoe admin, ...
   FOREIGN KEY(id_employe) REFERENCES Employe(id)
);

-- ALTER TABLE Users
-- ADD COLUMN statut INTEGER DEFAULT 0;

CREATE TABLE type_user(
   id SERIAL,
   type VARCHAR(50)  NOT NULL,
   created_at TIMESTAMP NOT NULL,
   modified_at TIMESTAMP,
   PRIMARY KEY(id)
);

CREATE TABLE Token(
   id SERIAL,
   token_genere VARCHAR(255)  NOT NULL,
   type VARCHAR(150) ,
   created_at TIMESTAMP NOT NULL,
   expires_at TIMESTAMP,
   is_active INTEGER,
   id_user VARCHAR(50)  NOT NULL,
   PRIMARY KEY(id),
   FOREIGN KEY(id_user) REFERENCES Users(id)
);

CREATE TABLE Type_document(
   id VARCHAR(50) ,
   intitule VARCHAR(50)  NOT NULL,
   PRIMARY KEY(id)
);

CREATE TABLE Document_employe(
   id VARCHAR(50) ,
   nom_fichier VARCHAR(100)  NOT NULL,
   chemin_fichier VARCHAR(150)  NOT NULL,
   date_upload TIMESTAMP NOT NULL,
   date_modified TIMESTAMP,
   id_type_document VARCHAR(50) ,
   id_employe VARCHAR(50)  NOT NULL,
   PRIMARY KEY(id),
   FOREIGN KEY(id_type_document) REFERENCES Type_document(id),
   FOREIGN KEY(id_employe) REFERENCES Employe(id)
);  

CREATE TABLE Niveau_hierarchique(
   id VARCHAR(50) ,
   nom VARCHAR(150)  NOT NULL,
   rang INTEGER NOT NULL,
   description VARCHAR(255) ,
   created_at TIMESTAMP NOT NULL,
   modified_at TIMESTAMP,
   PRIMARY KEY(id)
);

CREATE TABLE Poste(
   id VARCHAR(50) ,
   nom VARCHAR(100)  NOT NULL,
   description VARCHAR(255) ,
   created_at TIMESTAMP,
   modified_at TIMESTAMP,
   id_departement VARCHAR(50)  NOT NULL,
   id_niveau VARCHAR(50) ,
   PRIMARY KEY(id),
   FOREIGN KEY(id_departement) REFERENCES Departement(id)
   FOREIGN KEY(id_niveau) REFERENCES Niveau_hierarchique(id),
);

ALTER TABLE Poste 
ADD COLUMN id_niveau VARCHAR(50);

ALTER TABLE Poste
ADD CONSTRAINT fk_poste_niveau
FOREIGN KEY (id_niveau) REFERENCES Niveau_hierarchique(id);

CREATE TABLE Type_contrat(
   id VARCHAR(50) ,
   intitule VARCHAR(50)  NOT NULL,
   description VARCHAR(255) ,
   duree_max_mois INTEGER NOT NULL,
   created_at TIMESTAMP NOT NULL,
   modified_at TIMESTAMP,
   PRIMARY KEY(id)
);

-- alter table infos_Professionnelles drop column date_fin;
-- alter table Type_contrat add column duree_max_mois integer not null default 6;
-- alter table Type_contrat add column created_at TIMESTAMP not NULL;
-- alter table Type_contrat add column modified_at TIMESTAMP;


CREATE TABLE Contrat(
   id VARCHAR(50) ,
   date_debut DATE NOT NULL,
   date_fin DATE,
   duree INTEGER,
   id_poste VARCHAR(50)  NOT NULL,
   id_type_contrat VARCHAR(50)  NOT NULL,
   id_employe VARCHAR(50)  NOT NULL,
   PRIMARY KEY(id),
   FOREIGN KEY(id_poste) REFERENCES Poste(id),
   FOREIGN KEY(id_type_contrat) REFERENCES Type_contrat(id),
   FOREIGN KEY(id_employe) REFERENCES Employe(id)
);

CREATE TABLE situation_familiale(
   id SERIAL,
   type VARCHAR(50)  NOT NULL,
   PRIMARY KEY(id)
);

CREATE TABLE Departement(
   id VARCHAR(50) ,
   nom VARCHAR(50)  NOT NULL,
   description VARCHAR(100) ,
   statut INTEGER,
   created_at TIMESTAMP NOT NULL,
   modified_at TIMESTAMP,
   PRIMARY KEY(id)
);

alter table departement add column statut integer default 0;

CREATE TABLE nationalite(
   id SERIAL,
   nationalite VARCHAR(150)  NOT NULL,
   PRIMARY KEY(id)
);

CREATE TABLE manager(
   id VARCHAR(50) ,
   date_debut DATE NOT NULL,
   date_fin DATE,
   created_at TIMESTAMP NOT NULL,
   modified_at VARCHAR(50) ,
   statut INTEGER NOT NULL,
   id_departement VARCHAR(50) ,
   id_employe VARCHAR(50)  NOT NULL,
   commentaire TEXT,
   PRIMARY KEY(id),
   FOREIGN KEY(id_departement) REFERENCES Departement(id),
   FOREIGN KEY(id_employe) REFERENCES Employe(id)
);

alter table manager add column commentaire text;
alter table manager add column id_departement VARCHAR(50);
alter table manager
ADD CONSTRAINT fk_manager_departement
FOREIGN KEY (id_departement) REFERENCES departement(id);

CREATE TABLE Pointage(
   id VARCHAR(50) ,
   date_pointage DATE NOT NULL,
   duree_heure_travaillee_minute NUMERIC(10,2)  ,
   duree_retard_minute NUMERIC(10,2)  ,
   created_at TIMESTAMP,
   modified_at TIMESTAMP,
   duree_heure_supplementaire NUMERIC(10,2)  ,
   is_shift_jour BOOLEAN,
   commentaire TEXT,
   is_week_end BOOLEAN,
   statut_pointage INTEGER NOT NULL,
   is_ferie BOOLEAN,
   date_heure_arrivee TIMESTAMP NOT NULL,
   date_heure_depart TIMESTAMP NOT NULL,
   id_employe VARCHAR(50)  NOT NULL,
   PRIMARY KEY(id),
   FOREIGN KEY(id_employe) REFERENCES Employe(id)
);

CREATE TABLE Pointage_fille(
   id VARCHAR(50) ,
   date_heure_pointage TIMESTAMP NOT NULL,
   created_at TIMESTAMP NOT NULL,
   modified_at TIMESTAMP,
   type_action VARCHAR(3) ,
   source VARCHAR(50) ,
   id_pointage VARCHAR(50)  NOT NULL,
   PRIMARY KEY(id),
   FOREIGN KEY(id_pointage) REFERENCES Pointage(id)
); 

CREATE TABLE reglement_horaire_interieur(
   id SERIAL,
   heure_mat_entree TIME,
   heure_aprem_sortie TIME,
   heure_normale_journaliere NUMERIC(10,2)  ,
   heure_normale_semaine TIME,
   heure_normale_mois TIME,
   created_at TIMESTAMP NOT NULL,
   modified_at TIMESTAMP,
   duree_normale_pause_minutes NUMERIC(10,2)  ,
   PRIMARY KEY(id)
);

CREATE TABLE Historique_poste(
   id SERIAL,
   date_debut DATE,
   date_fin DATE,
   date_du_jour DATE,
   id_employe VARCHAR(50) ,
   id_poste VARCHAR(50) ,
   PRIMARY KEY(id),
   FOREIGN KEY(id_employe) REFERENCES Employe(id),
   FOREIGN KEY(id_poste) REFERENCES Poste(id)
);

CREATE TABLE categorie(
   id VARCHAR(50) ,
   nom VARCHAR(150)  NOT NULL,
   description VARCHAR(250) ,
   couleur VARCHAR(7) ,
   created_at TIMESTAMP NOT NULL,
   modified_at TIMESTAMP,
   PRIMARY KEY(id)
); 

CREATE TABLE competences(
   id VARCHAR(50) ,
   nom VARCHAR(255) ,
   description VARCHAR(255) ,
   created_at TIMESTAMP NOT NULL,
   modified_at TIMESTAMP,
   id_categorie VARCHAR(50) ,
   PRIMARY KEY(id),
   FOREIGN KEY(id_categorie) REFERENCES categorie(id)
); 

CREATE TABLE competence_employe(
   id VARCHAR(50) ,
   niveau INTEGER NOT NULL,
   date_acquisition DATE NOT NULL,
   created_at TIMESTAMP NOT NULL,
   modified_at TIMESTAMP,
   id_competence VARCHAR(50) ,
   id_employe VARCHAR(50) ,
   PRIMARY KEY(id),
   FOREIGN KEY(id_competence) REFERENCES competences(id),
   FOREIGN KEY(id_employe) REFERENCES Employe(id)
);

CREATE TABLE formation(
   id VARCHAR(50) ,
   nom VARCHAR(150)  NOT NULL,
   description VARCHAR(255) ,
   duree INTEGER,
   cout NUMERIC(10,2)  ,
   prerequis VARCHAR(255) ,
   objectifs VARCHAR(255) ,
   created_at TIMESTAMP NOT NULL,
   modified_at TIMESTAMP,
   PRIMARY KEY(id)
); 

CREATE TABLE formateur (
   id VARCHAR(50) ,
   nom VARCHAR(255)  NOT NULL,
   email VARCHAR(255) ,
   telephone VARCHAR(20) ,
   specialite VARCHAR(255) ,
   type INTEGER,
   created_at TIMESTAMP NOT NULL,
   modified_at TIMESTAMP,
   id_employe VARCHAR(50) ,
   PRIMARY KEY(id),
   FOREIGN KEY(id_employe) REFERENCES Employe(id)
);  

CREATE TABLE session_formation(
   id VARCHAR(50) ,
   date_debut DATE NOT NULL,
   date_fin VARCHAR(50)  NOT NULL,
   lieu VARCHAR(255) ,
   statut INTEGER NOT NULL,
   place_max INTEGER,
   created_at TIMESTAMP NOT NULL,
   modified_at TIMESTAMP,
   id_formation VARCHAR(50)  NOT NULL,
   id_formateur VARCHAR(50)  NOT NULL,
   PRIMARY KEY(id),
   FOREIGN KEY(id_formation) REFERENCES formation(id),
   FOREIGN KEY(id_formateur) REFERENCES formateur(id)
);

CREATE TABLE Historique_mouvement(
   id VARCHAR(50) ,
   ancien_info_id VARCHAR(50) ,
   nouvelle_info_id VARCHAR(50) ,
   date_mouvement DATE NOT NULL,
   commentaire VARCHAR(255) ,
   created_at TIMESTAMP NOT NULL,
   ancien_poste VARCHAR(150) ,
   nouvel_poste VARCHAR(150) ,
   ancien_departement VARCHAR(150) ,
   nouveau_departement VARCHAR(150) ,
   ancien_type_contrat VARCHAR(150) ,
   nouvel_type_contrat VARCHAR(150) ,
   isManager INTEGER NOT NULL,
   effectue_par_id VARCHAR(50) ,
   type_mouvement VARCHAR(250) ,
   PRIMARY KEY(id)
);

CREATE TABLE type_mouvement(
   id VARCHAR(50) ,
   type VARCHAR(250)  NOT NULL,
   created_at TIMESTAMP NOT NULL,
   modified_at TIMESTAMP,
   PRIMARY KEY(id)
);

CREATE TABLE mouvement(
   id VARCHAR(50) ,
   motif VARCHAR(255) ,
   statut INTEGER NOT NULL,
   date_demande DATE NOT NULL,
   date_validation DATE,
   commentaire VARCHAR(255) ,
   created_at TIMESTAMP NOT NULL,
   modified_at TIMESTAMP,
   id_employe_validateur VARCHAR(50) ,
   id_employe_demandeur VARCHAR(50) ,
   id_infos_pro_actuel VARCHAR(50)  NOT NULL,
   id_infos_pro_propose VARCHAR(50) ,
   id_employe_concerne VARCHAR(50) ,
   id_type_mouvement VARCHAR(50) ,
   PRIMARY KEY(id),
   FOREIGN KEY(id_employe_validateur) REFERENCES Employe(id),
   FOREIGN KEY(id_employe_demandeur) REFERENCES Employe(id),
   FOREIGN KEY(id_infos_pro_actuel) REFERENCES infos_professionnelles(id),
   FOREIGN KEY(id_infos_pro_propose) REFERENCES infos_professionnelles(id),
   FOREIGN KEY(id_employe_concerne) REFERENCES Employe(id),
   FOREIGN KEY(id_type_mouvement) REFERENCES type_mouvement(id)
);

alter table infos_professionnelles add column statut INTEGER not NULL default 1;

// Dans Mouvement.java
public static final Integer STATUT_EN_ATTENTE = 1;
public static final Integer STATUT_VALIDE = 2;
public static final Integer STATUT_REJETE = 3;
public static final Integer STATUT_ANNULE = 4;

// Dans InfosProfessionnelles.java  
public static final Integer STATUT_ACTIF = 1;
public static final Integer STATUT_HISTORIQUE = 2;
public static final Integer STATUT_PROPOSE = 3;
public static final Integer STATUT_ANNULE = 4;


-- type de mouvement : mutation(changement koa), promotion(si changement de grade => grade ++), changement de département(Avenant de poste)
CREATE TABLE jour_travail(
   id SERIAL,
   nom_jour VARCHAR(50) ,
   code_jour INTEGER,
   created_at TIMESTAMP NOT NULL,
   modified_at TIMESTAMP,
   statut INTEGER NOT NULL,
   est_weekend INTEGER,
   PRIMARY KEY(id)
);

CREATE TABLE horaire_journalier(
   id VARCHAR(50) ,
   created_at TIMESTAMP NOT NULL,
   modified_at TIMESTAMP,
   duree_pause_minutes INTEGER default 60,
   duree_journaliere_attendue_minutes INTEGER default 480,
   statut INTEGER,
   id_jour INTEGER,
   PRIMARY KEY(id),
   FOREIGN KEY(id_jour) REFERENCES jour_travail(id)
); 

CREATE TABLE edt_employe(
   id VARCHAR(50) ,
   date_du_jour DATE NOT NULL,
   heure_debut TIME,
   heure_fin TIME,
   created_at TIMESTAMP NOT NULL,
   modified_at TIMESTAMP,
   statut INTEGER,
   is_shift_jour BOOLEAN,
   id_horaire VARCHAR(50) ,
   id_employe VARCHAR(50) ,
   PRIMARY KEY(id),
   FOREIGN KEY(id_horaire) REFERENCES horaire_journalier(id),
   FOREIGN KEY(id_employe) REFERENCES Employe(id)
); 

----------------------------------------------

CREATE TABLE edt_horaire_employe(
   id VARCHAR(50) ,
   date_du_jour DATE NOT NULL,
   heure_debut TIME,
   heure_fin TIME,
   created_at TIMESTAMP NOT NULL,
   modified_at TIMESTAMP,
   statut INTEGER,
   is_shift_jour BOOLEAN,
   est_weekend BOOLEAN,
   id_employe VARCHAR(50) ,
   PRIMARY KEY(id),
   FOREIGN KEY(id_employe) REFERENCES Employe(id)
);

CREATE TABLE param_horaire(
   id VARCHAR(50) ,
   created_at TIMESTAMP NOT NULL,
   modified_at TIMESTAMP,
   duree_pause_minutes INTEGER,
   duree_journaliere_attendue_minutes INTEGER,
   statut INTEGER,
   PRIMARY KEY(id)
);

CREATE TABLE type_conge(
   id VARCHAR(50) ,
   intitule VARCHAR(50)  NOT NULL,
   description VARCHAR(255) ,
   obligatoire_doc BOOLEAN,
   created_at TIMESTAMP NOT NULL,
   modified_at TIMESTAMP,
   is_cumulable BOOLEAN,
   PRIMARY KEY(id)
);

CREATE TABLE Demande_conge(
   id VARCHAR(50) ,
   date_debut DATE NOT NULL,
   date_fin DATE NOT NULL,
   statut INTEGER NOT NULL,
   date_demande DATE NOT NULL,
   decision_manager INTEGER,
   created_at TIMESTAMP NOT NULL,
   modified_at TIMESTAMP,
   commentaire_manager VARCHAR(255) ,
   commentaire VARCHAR(255) ,
   autre_motif VARCHAR(255),
   nb_jours NUMERIC(10,2)   NOT NULL,
   id_employe VARCHAR(50) ,
   id_type_conge VARCHAR(50) ,
   id_manager VARCHAR(50),
   PRIMARY KEY(id),
   FOREIGN KEY(id_employe) REFERENCES Employe(id),
   FOREIGN KEY(id_type_conge) REFERENCES type_conge(id),
   FOREIGN KEY(id_manager) REFERENCES manager(id)
);    

ALTER TABLE Demande_conge 
ADD COLUMN id_manager VARCHAR(50);

ALTER TABLE Demande_conge 
ADD CONSTRAINT fk_demande_conge_manager 
FOREIGN KEY (id_manager) REFERENCES manager(id);

CREATE TABLE mouvement_solde (
    id SERIAL PRIMARY KEY,
    nb_conge_total NUMERIC(10,2) NOT NULL,
    nb_conge_restant NUMERIC(10,2) NOT NULL,
    nb_conge_pris NUMERIC(10,2) NOT NULL,
    annee INTEGER NOT NULL,
    created_at TIMESTAMP NOT NULL,
    modified_at TIMESTAMP,
    mois INTEGER NOT NULL,
    statut INTEGER NOT NULL,
    commentaire VARCHAR(255),
    type_mouvement VARCHAR(50) NOT NULL,
    id_employe VARCHAR(50) NOT NULL,
    FOREIGN KEY(id_employe) REFERENCES employe(id)
);

-- ALTER TABLE mouvement_solde 
-- ALTER COLUMN type_mouvement TYPE VARCHAR(50) USING type_mouvement_enum::text;

-- CREATE TYPE type_mouvement_enum AS ENUM (
--     'ACQUISITION',  -- ajout mensuel de congés
--     'PRISE',        -- congé pris
--     'REPORT',       -- report d'un solde
--     'CORRECTION',    -- correction RH
--     'ANNULATION'
-- );

CREATE TABLE Solde_annuel(
   id VARCHAR(50) ,
   nb_conge_total NUMERIC(10,2)  ,
   nb_conge_restant NUMERIC(10,2)  ,
   nb_conge_pris NUMERIC(10,2)  ,
   annee INTEGER NOT NULL,
   date_cloture DATE,
   statut_cloture INTEGER,
   created_at TIMESTAMP NOT NULL,
   modified_at TIMESTAMP,
   id_employe VARCHAR(50) ,
   PRIMARY KEY(id),
   FOREIGN KEY(id_employe) REFERENCES Employe(id)
);

CREATE TABLE regle_gestion_conges(
   id SERIAL,
   anciennete_requis INTEGER NOT NULL,
   solde_mensuel NUMERIC(10,2)   NOT NULL,
   is_week_end_inclus BOOLEAN,
   created_at TIMESTAMP NOT NULL,
   limite_report_annuel INTEGER,
   allocation_familiale NUMERIC(10,2)  ,
   modified_at TIMESTAMP,
   statut INTEGER NOT NULL,
   PRIMARY KEY(id)
);

alter table regle_gestion_conges add column allocation_familiale NUMERIC(10, 2);

CREATE TABLE regles_annulation_conges(
   id VARCHAR(50) ,
   delai_min_jours INTEGER,
   duree_max_jours INTEGER,
   besoin_validation_manager BOOLEAN,
   besoin_validation_rh BOOLEAN,
   actif BOOLEAN,
   PRIMARY KEY(id)
);

CREATE TABLE reglement_horaire_interieur(
   id SERIAL,
   heure_mat_entree TIME,
   heure_aprem_sortie TIME,
   heure_normale_journaliere NUMERIC(10,2)  ,
   heure_normale_semaine TIME,
   heure_normale_mois TIME,
   created_at TIMESTAMP NOT NULL,
   modified_at TIMESTAMP,
   duree_normale_pause_minutes NUMERIC(10,2)  ,
   statut int,
   PRIMARY KEY(id)
);

alter table reglement_horaire_interieur add column statut int default 0;

CREATE TABLE information_societe(
   id SERIAL,
   nom_company VARCHAR(150) ,
   logo VARCHAR(150) ,
   created_at TIMESTAMP NOT NULL,
   modified_at TIMESTAMP,
   PRIMARY KEY(id)
);

CREATE TABLE mois(
   id SERIAL,
   libelle VARCHAR(20) ,
   created_at TIMESTAMP NOT NULL,
   modified_at TIMESTAMP,
   PRIMARY KEY(id)
);

alter table mois add column num int;

CREATE TABLE paie(
   id VARCHAR(50) ,
   statut INTEGER,
   created_at TIMESTAMP NOT NULL,
   modified_at TIMESTAMP,
   id_info_societte INTEGER NOT NULL,
   id_employe VARCHAR(50) ,
   id_periode varchar(50),
   PRIMARY KEY(id),
   foreign key(id_periode) references periode_paie(id),
   FOREIGN KEY(id_info_societe) REFERENCES information_societe(id),
   FOREIGN KEY(id_employe) REFERENCES Employe(id)
);

alter table paie add column date_cloture date;
alter table paie drop CONSTRAINT FOREIGN KEY paie_id_mois_fkey;
alter table paie drop column id_mois;
alter table paie drop column annee;
alter table paie add column date_debut_periode date;
alter table paie add column date_fin_periode date;
alter table paie add column date_paiement date;
alter table paie add column classification VARCHAR(20);
alter table paie add column departement VARCHAR(50);
alter table paie add column conges_pris NUMERIC(10, 2);
alter table paie add column solde_conges NUMERIC(10, 2);
alter table paie add column mode_paiement VARCHAR(150);

CREATE TABLE paie_fille(
   id VARCHAR(50) ,
   taux NUMERIC(10,2)  ,
   montant NUMERIC(10,2)  ,
   base NUMERIC(12,2)  ,
   nombre NUMERIC(10,2)  ,
   modified_at TIMESTAMP,
   created_at TIMESTAMP NOT NULL,
   id_paie VARCHAR(50)  NOT NULL,
   id_rubrique VARCHAR(50)  NOT NULL,
   PRIMARY KEY(id),
   FOREIGN KEY(id_paie) REFERENCES paie(id),
   FOREIGN KEY(id_rubrique) REFERENCES rubrique_paie(id)
);

CREATE TABLE formule(
   id VARCHAR(50) ,
   base VARCHAR(50) ,
   montant_fixe NUMERIC(10,2)  ,
   nombre NUMERIC(10, 2),
   taux NUMERIC(10,2)  ,
   created_at TIMESTAMP NOT NULL,
   modified_at TIMESTAMP,
   PRIMARY KEY(id)
);

CREATE TABLE base_irsa(
   id VARCHAR(50) ,
   tranche_min NUMERIC(10,2)  ,
   tranche_max NUMERIC(10,2)  ,
   taux NUMERIC(10,2)  ,
   num_tranche INTEGER,
   created_at TIMESTAMP NOT NULL,
   modified_at TIMESTAMP,
   PRIMARY KEY(id)
);

CREATE TABLE paie_fille_irsa(
   id VARCHAR(50) ,
   montant NUMERIC(10,2)  ,
   num_tranche INTEGER,
   created_at TIMESTAMP NOT NULL,
   modified_at TIMESTAMP,
   id_base_irsa VARCHAR(50) ,
   id_paie_fille VARCHAR(50) ,
   PRIMARY KEY(id),
   FOREIGN KEY(id_base_irsa) REFERENCES base_irsa(id),
   FOREIGN KEY(id_paie_fille) REFERENCES paie_fille(id)
);

CREATE TYPE mode_calcul_enum AS ENUM (
    'AUTO',
    'CALCULE',
    'MANUEL'
);

CREATE TABLE rubrique_paie(
   id VARCHAR(50) ,
   code VARCHAR(20) ,
   mode_calcul VARCHAR(50) ,
   libelle VARCHAR(100) ,
   plafond_mensuel VARCHAR(50) ,
   est_imposable BOOLEAN,
   est_soumis_cotisations BOOLEAN,
   compte_comptable INTEGER,
   ordre INTEGER,
   est_actif BOOLEAN,
   plafond_annuel NUMERIC(10,2) ,
   commentaire TEXT,
   modified_at TIMESTAMP,
   created_at TIMESTAMP NOT NULL,
   id_categorie VARCHAR(50) ,
   id_rubrique TEXT,
   id_formule VARCHAR(20),
   PRIMARY KEY(id),
   FOREIGN KEY(id_categorie) REFERENCES categorie_rub(id),
   FOREIGN KEY(id_rubrique) REFERENCES rubrique_types(id),
   FOREIGN KEY(id_formule) REFERENCES formule(id)
);

alter table regle_gestion_conges add column allocation_familiale NUMERIC(10, 2);
alter table rubrique_paie add column est_deductible_irsa BOOLEAN default false;
ALTER TABLE rubrique_paie
ADD COLUMN mode_calcul mode_calcul_enum NOT NULL DEFAULT 'AUTO';

alter table rubrique_paie add column id_formule VARCHAR(50);
ALTER TABLE rubrique_paie
ADD CONSTRAINT formule_id_formule_fkey
FOREIGN KEY (id_formule)
REFERENCES formule(id);

CREATE TABLE categorie_rub(
   id VARCHAR(50) ,
   libelle VARCHAR(150) ,
   created_at TIMESTAMP NOT NULL,
   modified_at TIMESTAMP,
   PRIMARY KEY(id)
);

CREATE TABLE rubrique_types(
   id TEXT,
   libelle VARCHAR(100) ,
   description TEXT,
   created_at TIMESTAMP NOT NULL,
   modified_at TIMESTAMP,
   PRIMARY KEY(id)
);

CREATE TABLE base_irsa(
   id VARCHAR(50) ,
   tranche_min NUMERIC(10,2)  ,
   tranche_max NUMERIC(10,2)  ,
   taux NUMERIC(10,2)  ,
   num_tranche INTEGER,
   created_at TIMESTAMP NOT NULL,
   modified_at TIMESTAMP,
   PRIMARY KEY(id)
);

CREATE TABLE grade_et_grille_salariale(
   grade VARCHAR(2) ,
   code VARCHAR(10) ,
   intitule VARCHAR(50) ,
   salaire_min NUMERIC(10,2)  ,
   salaire_mid NUMERIC(10,2)  ,
   salaire_max NUMERIC(10,2)  ,
   prime_fixe NUMERIC(10,2)  ,
   created_at TIMESTAMP NOT NULL,
   modified_at TIMESTAMP,
   PRIMARY KEY(grade)
);

CREATE TABLE calendrier_operations(
   id VARCHAR(50) ,
   periode_jour_debut INTEGER,
   periode_jour_fin INTEGER,
   echeance VARCHAR(10) ,
   operations VARCHAR(100) ,
   responsable VARCHAR(20) ,
   created_at TIMESTAMP NOT NULL,
   modified_at TIMESTAMP,
   PRIMARY KEY(id)
);

CREATE TABLE journal_parametrage(
   id VARCHAR(50) ,
   valeur NUMERIC(10,2)  ,
   commentaire TEXT,
   responsable VARCHAR(50) ,
   PRIMARY KEY(id)
);

CREATE TABLE abreviation(
   id SERIAL,
   libelle VARCHAR(150) ,
   abreviation VARCHAR(50) Unique,
   description TEXT,
   created_at TIMESTAMP NOT NULL,
   modified_at TIMESTAMP,
   PRIMARY KEY(id)
);

CREATE TABLE type_demande(
   id VARCHAR(50) ,
   type VARCHAR(150) ,
   created_at TIMESTAMP NOT NULL,
   modified_at TIMESTAMP,
   PRIMARY KEY(id)
);

CREATE TABLE demande_absence(
   id VARCHAR(50) ,
   created_at TIMESTAMP NOT NULL,
   modified_at TIMESTAMP,
   statut INTEGER,
   date_heure_absence_debut TIMESTAMP,
   date_heure_absence_fin TIMESTAMP,
   id_employe VARCHAR(50) ,
   id_type_demande VARCHAR(50) ,
   commentaite TEXT,
   PRIMARY KEY(id),
   FOREIGN KEY(id_employe) REFERENCES Employe(id),
   FOREIGN KEY(id_type_demande) REFERENCES type_demande(id)
); 

CREATE TABLE type_paiement(
   id VARCHAR(50) ,
   code VARCHAR(50) ,
   libelle VARCHAR(50) ,
   created_at TIMESTAMP NOT NULL,
   modified_at TIMESTAMP,
   PRIMARY KEY(id)
);

CREATE TABLE mode_paiement(
   id VARCHAR(50) PRIMARY KEY,
   nom_banque VARCHAR(255),
   code_banque VARCHAR(20),
   code_guichet VARCHAR(20),
   numero_compte VARCHAR(50),
   cle_rib VARCHAR(2),
   titulaire_compte VARCHAR(255) NOT NULL,
   telephone_mobile VARCHAR(20),
   domiciliation_agence VARCHAR(255),
   created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
   modified_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
   est_actif BOOLEAN DEFAULT TRUE,
   est_par_defaut BOOLEAN DEFAULT FALSE,
   id_type_paiement VARCHAR(50) NOT NULL,
   id_employe VARCHAR(50) NOT NULL,
   
   FOREIGN KEY(id_type_paiement) REFERENCES type_paiement(id),
   FOREIGN KEY(id_employe) REFERENCES Employe(id)
);

CREATE TABLE mouvement_solde_paie(
   id SERIAL,
   date_edition TIMESTAMP,
   created_at TIMESTAMP NOT NULL,
   modified_at TIMESTAMP,
   id_user VARCHAR(50) ,
   id_mouvement_solde INTEGER NOT NULL,
   id_paie VARCHAR(50)  NOT NULL,
   PRIMARY KEY(id),
   FOREIGN KEY(id_mouvement_solde) REFERENCES mouvement_solde(id),
   FOREIGN KEY(id_paie) REFERENCES paie(id)
);

CREATE TABLE Notifications(
   id SERIAL,
   email_destinateur VARCHAR(250) ,
   date_envoi DATE,
   message TEXT,
   id_manager INTEGER NOT NULL,
   id_utilisateur_conserne VARCHAR(250)  NOT NULL,
   id_utilisateur_destinateur VARCHAR(250)  NOT NULL,
   statut INTEGER,
   created_at TIMESTAMP NOT NULL,
   modified_at TIMESTAMP,
   PRIMARY KEY(id)
);

-- Supprimer l'ancienne table
DROP TABLE IF EXISTS preparation_export_employe;

-- Recréer la table avec des noms en minuscules et underscores
CREATE TABLE preparation_export_employe(
    id SERIAL PRIMARY KEY,
    is_matricule BOOLEAN DEFAULT TRUE,
    is_nom BOOLEAN DEFAULT TRUE,
    is_prenom BOOLEAN DEFAULT TRUE,
    is_date_naissance BOOLEAN DEFAULT FALSE,
    is_email BOOLEAN DEFAULT FALSE,
    is_cin BOOLEAN DEFAULT FALSE,
    is_lieu_naissance BOOLEAN DEFAULT FALSE,
    is_telephone BOOLEAN DEFAULT FALSE,
    is_code_postal BOOLEAN DEFAULT FALSE,
    is_adresse BOOLEAN DEFAULT FALSE,
    is_num_cnaps BOOLEAN DEFAULT FALSE,
    is_num_ostie BOOLEAN DEFAULT FALSE,
    is_nom_complet_mere BOOLEAN DEFAULT FALSE,
    is_nom_complet_pere BOOLEAN DEFAULT FALSE,
    is_nb_enfants BOOLEAN DEFAULT FALSE,
    is_nom_conjoint BOOLEAN DEFAULT FALSE,
    is_mode_paiement_nom_banque BOOLEAN DEFAULT FALSE,
    is_mode_paiement_code_banque BOOLEAN DEFAULT FALSE,
    is_mode_paiement_code_guichet BOOLEAN DEFAULT FALSE,
    is_info_pro_date_embauche BOOLEAN DEFAULT TRUE,
    is_info_pro_salaire_base BOOLEAN DEFAULT TRUE,
    is_info_pro_classification BOOLEAN DEFAULT FALSE,
    is_info_pro_periodicite_paiement BOOLEAN DEFAULT FALSE,
    is_info_pro_categorie BOOLEAN DEFAULT FALSE,
    is_info_pro_poste BOOLEAN DEFAULT TRUE,
    is_info_pro_departement BOOLEAN DEFAULT TRUE,
    is_situation_familiale BOOLEAN DEFAULT FALSE,
    is_nationalite BOOLEAN DEFAULT FALSE,
    is_sexe BOOLEAN DEFAULT FALSE,
    is_emergency_contact_telephone BOOLEAN DEFAULT FALSE,
    is_emergency_contact_nom BOOLEAN DEFAULT FALSE,
    is_emergency_contact_email BOOLEAN DEFAULT FALSE,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    modified_at TIMESTAMP
);

CREATE TABLE mouvement_solde_paie(
   id SERIAL,
   id_mouvement_solde VARCHAR(50) ,
   date_heure_saisie TIMESTAMP,
   id_paie VARCHAR(50) ,
   nb_conge_a_reporter NUMERIC(15,2)  ,
   nb_conge_dans_paie NUMERIC(15,2)  ,
   nb_conge_dans_mouvement_solde NUMERIC(15,2)  ,
   created_at TIMESTAMP NOT NULL,
   PRIMARY KEY(id)
);
