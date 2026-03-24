CREATE TABLE Departement(
   id VARCHAR(50) ,
   nom VARCHAR(50)  NOT NULL,
   description VARCHAR(100) ,
   created_at TIMESTAMP NOT NULL,
   modified_at TIMESTAMP,
   statut INTEGER,
   commentaire TEXT,
   PRIMARY KEY(id)
);

CREATE TABLE Type_document(
   id VARCHAR(50) ,
   intitule VARCHAR(50)  NOT NULL,
   created_at TIMESTAMP NOT NULL,
   date_updated TIMESTAMP,
   is_expired BOOLEAN,
   PRIMARY KEY(id)
);

CREATE TABLE Type_contrat(
   id VARCHAR(50) ,
   intitule VARCHAR(50)  NOT NULL,
   description VARCHAR(255) ,
   created_at TIMESTAMP NOT NULL,
   modified_at TIMESTAMP,
   duree_max_mois INTEGER NOT NULL,
   PRIMARY KEY(id)
);

CREATE TABLE type_conge(
   id VARCHAR(50) ,
   intitule VARCHAR(50)  NOT NULL,
   description VARCHAR(255) ,
   created_at TIMESTAMP NOT NULL,
   modified_at TIMESTAMP,
   PRIMARY KEY(id)
);

CREATE TABLE type_user(
   id SERIAL,
   type VARCHAR(50)  NOT NULL,
   PRIMARY KEY(id)
);

CREATE TABLE nationalite(
   id SERIAL,
   nationalite VARCHAR(150)  NOT NULL,
   created_at TIMESTAMP NOT NULL,
   modified_at TIMESTAMP,
   PRIMARY KEY(id)
);

CREATE TABLE sexe(
   id SERIAL,
   sexe VARCHAR(50)  NOT NULL,
   code VARCHAR(1) ,
   PRIMARY KEY(id)
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

CREATE TABLE Niveau_hierarchique(
   id VARCHAR(50) ,
   nom VARCHAR(150)  NOT NULL,
   rang INTEGER NOT NULL,
   description VARCHAR(255) ,
   created_at TIMESTAMP NOT NULL,
   modified_at TIMESTAMP,
   PRIMARY KEY(id)
);

CREATE TABLE regle_gestion_conges(
   id SERIAL,
   anciennete_requis INTEGER NOT NULL,
   solde_mensuel NUMERIC(10,2)   NOT NULL,
   is_week_end_inclus BOOLEAN,
   created_at TIMESTAMP NOT NULL,
   limite_report_annuel INTEGER,
   prime_anciennete NUMERIC(10,2)  ,
   modified_at TIMESTAMP,
   statut INTEGER NOT NULL,
   anciennete_requis_prime NUMERIC(10,2)  ,
   allocation_familiale NUMERIC(10,2)  ,
   jour_paiement_max INTEGER,
   jour_paiement_min INTEGER,
   PRIMARY KEY(id)
);

CREATE TABLE regles_annulation_conges(
   id VARCHAR(50) ,
   delai_min_jours INTEGER,
   duree_max_jours INTEGER,
   besoin_validation_manager BOOLEAN,
   besoin_validation_rh BOOLEAN,
   actif BOOLEAN,
   PRIMARY KEY(id)
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

CREATE TABLE region(
   id VARCHAR(50) ,
   nom VARCHAR(255) ,
   created_at TIMESTAMP NOT NULL,
   modified_at TIMESTAMP,
   PRIMARY KEY(id)
);

CREATE TABLE type_paiement(
   id VARCHAR(50) ,
   code VARCHAR(50) ,
   libelle VARCHAR(50) ,
   created_at TIMESTAMP NOT NULL,
   modified_at TIMESTAMP,
   PRIMARY KEY(id)
);

CREATE TABLE situation_familiale(
   id VARCHAR(50) ,
   created_at TIMESTAMP NOT NULL,
   modified_at TIMESTAMP,
   intitule VARCHAR(255) ,
   PRIMARY KEY(id)
);

CREATE TABLE type_entree(
   id VARCHAR(50) ,
   nom VARCHAR(255) ,
   created_at TIMESTAMP NOT NULL,
   modified_at TIMESTAMP,
   PRIMARY KEY(id)
);

CREATE TABLE historique_solde_conge(
   id SERIAL,
   idEmploye VARCHAR(50) ,
   date_conge_acquis DATE,
   nb_conge_total NUMERIC(10,2)  ,
   nb_conge_restant NUMERIC(10,2)  ,
   nb_conge_pris NUMERIC(10,2)  ,
   commentaire TEXT,
   idDemandeConge VARCHAR(50) ,
   date_heure_mvt TIMESTAMP,
   created_at TIMESTAMP NOT NULL,
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
   statut INTEGER,
   PRIMARY KEY(id)
);

CREATE TABLE calendrier_ferie(
   id SERIAL,
   date_ferie DATE NOT NULL,
   libelle VARCHAR(50) ,
   created_at TIMESTAMP NOT NULL,
   modified_at TIMESTAMP,
   est_actif BOOLEAN,
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

CREATE TABLE journal_parametrage(
   id VARCHAR(50) ,
   valeur NUMERIC(10,2)  ,
   commentaire TEXT,
   responsable VARCHAR(50) ,
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

CREATE TABLE categorie_rub(
   id VARCHAR(50) ,
   libelle VARCHAR(150) ,
   created_at TIMESTAMP NOT NULL,
   modified_at TIMESTAMP,
   PRIMARY KEY(id)
);

CREATE TABLE information_societe(
   id SERIAL,
   nom_company VARCHAR(150) ,
   logo VARCHAR(150) ,
   created_at TIMESTAMP NOT NULL,
   modified_at TIMESTAMP,
   date_fondation DATE,
   nif VARCHAR(50) ,
   stat VARCHAR(50) ,
   adresse VARCHAR(150) ,
   PRIMARY KEY(id)
);

CREATE TABLE abreviation(
   id SERIAL,
   libelle VARCHAR(150) ,
   abreviation VARCHAR(50) ,
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

CREATE TABLE formule(
   id VARCHAR(50) ,
   base VARCHAR(50) ,
   nombre NUMERIC(10,2)  ,
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

CREATE TABLE type_temps_travail(
   id SERIAL,
   temps_travail VARCHAR(50) ,
   created_at TIMESTAMP NOT NULL,
   modified_at TIMESTAMP,
   PRIMARY KEY(id)
);

CREATE TABLE Notifications(
   id SERIAL,
   email_envoye VARCHAR(50) ,
   date_envoi DATE,
   created_at TIMESTAMP NOT NULL,
   modified_at TIMESTAMP,
   message_notif TEXT,
   statut INTEGER,
   PRIMARY KEY(id)
);

CREATE TABLE mois(
   id SERIAL,
   libelle VARCHAR(30) ,
   num INTEGER NOT NULL,
   created_at TIMESTAMP NOT NULL,
   modified_at TIMESTAMP,
   PRIMARY KEY(id)
);

CREATE TABLE preparation_export_employe(
   id SERIAL,
   is_Matricule BOOLEAN,
   is_Nom BOOLEAN,
   is_Prenom BOOLEAN,
   is_DateNaissance BOOLEAN,
   is_Email BOOLEAN,
   is_Cin BOOLEAN,
   is_LieuNaissance BOOLEAN,
   is_Telephone BOOLEAN,
   is_CodePostal BOOLEAN,
   is_Adresse BOOLEAN,
   is_NumCnaps BOOLEAN,
   is_NumOstie BOOLEAN,
   is_NomCompletMere BOOLEAN,
   is_NomCompletPere BOOLEAN,
   is_NbEnfants BOOLEAN,
   is_NomConjoint BOOLEAN,
   is_ModePaiementNomBanque BOOLEAN,
   is_ModePaiementCodeBanque BOOLEAN,
   is_ModePaiementCodeGuichet BOOLEAN,
   is_InfoProDateEmbauche BOOLEAN,
   is_InfoProSalaireBase BOOLEAN,
   is_InfoProClassification BOOLEAN,
   is_InfoProPeriodicitePaiement BOOLEAN,
   is_InfoProCategorie BOOLEAN,
   is_InfoProPoste BOOLEAN,
   is_InfoProDepartement BOOLEAN,
   is_SituationFamiliale BOOLEAN,
   is_Nationalite BOOLEAN,
   is_Sexe BOOLEAN,
   is_EmergencyContactTelephone BOOLEAN,
   is_EmergencyContactNom BOOLEAN,
   is_EmergencyContactEmail BOOLEAN,
   modified_at TIMESTAMP,
   created_at TIMESTAMP NOT NULL,
   PRIMARY KEY(id)
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

CREATE TABLE Employe(
   id VARCHAR(50) ,
   matricule INTEGER,
   nom VARCHAR(100)  NOT NULL,
   prenom VARCHAR(250)  NOT NULL,
   date_naissance DATE NOT NULL,
   email VARCHAR(100)  NOT NULL,
   cin VARCHAR(12)  NOT NULL,
   lieu_naissance VARCHAR(250)  NOT NULL,
   telephone VARCHAR(12)  NOT NULL,
   code_postal INTEGER,
   adresse VARCHAR(255)  NOT NULL,
   photo VARCHAR(255) ,
   numero_affiliation_cnaps VARCHAR(50) ,
   numero_affiliation_ostie VARCHAR(50) ,
   nom_complet_mere VARCHAR(255) ,
   nom_complet_pere VARCHAR(255) ,
   nb_enfants INTEGER,
   nom_conjoint VARCHAR(255) ,
   created_at TIMESTAMP NOT NULL,
   modified_at TIMESTAMP,
   statut INTEGER NOT NULL,
   commentaire TEXT,
   id_1 VARCHAR(50) ,
   id_2 VARCHAR(50)  NOT NULL,
   id_3 INTEGER NOT NULL,
   id_4 INTEGER NOT NULL,
   PRIMARY KEY(id),
   UNIQUE(cin),
   FOREIGN KEY(id_1) REFERENCES region(id),
   FOREIGN KEY(id_2) REFERENCES situation_familiale(id),
   FOREIGN KEY(id_3) REFERENCES sexe(id),
   FOREIGN KEY(id_4) REFERENCES nationalite(id)
);

CREATE TABLE Poste(
   id VARCHAR(50) ,
   nom VARCHAR(100)  NOT NULL,
   description VARCHAR(255) ,
   created_at TIMESTAMP,
   modified_at TIMESTAMP,
   id_1 VARCHAR(50) ,
   id_2 VARCHAR(50) ,
   PRIMARY KEY(id),
   FOREIGN KEY(id_1) REFERENCES Niveau_hierarchique(id),
   FOREIGN KEY(id_2) REFERENCES Departement(id)
);

CREATE TABLE Document_employe(
   id VARCHAR(50) ,
   nom_fichier VARCHAR(100)  NOT NULL,
   chemin_fichier VARCHAR(150)  NOT NULL,
   date_upload TIMESTAMP NOT NULL,
   date_modified TIMESTAMP,
   content_type VARCHAR(150) ,
   file_size INTEGER,
   id_1 VARCHAR(50) ,
   id_2 VARCHAR(50)  NOT NULL,
   PRIMARY KEY(id),
   FOREIGN KEY(id_1) REFERENCES Type_document(id),
   FOREIGN KEY(id_2) REFERENCES Employe(id)
);

CREATE TABLE Users(
   id VARCHAR(50) ,
   email VARCHAR(150)  NOT NULL,
   password VARCHAR(150)  NOT NULL,
   created_at TIMESTAMP NOT NULL,
   modified_at TIMESTAMP,
   id_1 INTEGER NOT NULL,
   id_2 VARCHAR(50)  NOT NULL,
   PRIMARY KEY(id),
   UNIQUE(email),
   FOREIGN KEY(id_1) REFERENCES type_user(id),
   FOREIGN KEY(id_2) REFERENCES Employe(id)
);

CREATE TABLE Token(
   id SERIAL,
   token_genere VARCHAR(255)  NOT NULL,
   duree_expiration VARCHAR(50) ,
   type VARCHAR(150) ,
   created_at TIMESTAMP NOT NULL,
   expires_at TIMESTAMP,
   is_active INTEGER,
   id_1 VARCHAR(50)  NOT NULL,
   PRIMARY KEY(id),
   FOREIGN KEY(id_1) REFERENCES Users(id)
);

CREATE TABLE mouvement_solde(
   id SERIAL,
   nb_conge_total NUMERIC(10,2)   NOT NULL,
   nb_conge_restant NUMERIC(10,2)   NOT NULL,
   nb_conge_pris NUMERIC(10,2)   NOT NULL,
   mois INTEGER NOT NULL,
   annee INTEGER NOT NULL,
   created_at TIMESTAMP NOT NULL,
   modified_at TIMESTAMP,
   statut INTEGER NOT NULL,
   commentaire VARCHAR(255) ,
   type_mouvement VARCHAR(50) ,
   date_cloture DATE,
   id_1 VARCHAR(50)  NOT NULL,
   PRIMARY KEY(id),
   FOREIGN KEY(id_1) REFERENCES Employe(id)
);

CREATE TABLE emergency_contact(
   id VARCHAR(50) ,
   contact VARCHAR(50) ,
   email VARCHAR(50) ,
   adresse VARCHAR(50) ,
   nom VARCHAR(250) ,
   created_at TIMESTAMP NOT NULL,
   modified_at DATE,
   lien_parente VARCHAR(50) ,
   statut INTEGER,
   id_1 VARCHAR(50)  NOT NULL,
   PRIMARY KEY(id),
   FOREIGN KEY(id_1) REFERENCES Employe(id)
);

CREATE TABLE manager(
   id VARCHAR(50) ,
   date_debut DATE NOT NULL,
   date_fin DATE,
   created_at TIMESTAMP NOT NULL,
   modified_at VARCHAR(50) ,
   statut INTEGER NOT NULL,
   commentaire TEXT,
   id_1 VARCHAR(50) ,
   id_2 VARCHAR(50)  NOT NULL,
   PRIMARY KEY(id),
   FOREIGN KEY(id_1) REFERENCES Departement(id),
   FOREIGN KEY(id_2) REFERENCES Employe(id)
);

CREATE TABLE Solde_annuel(
   id VARCHAR(50) ,
   solde_reporte NUMERIC(10,2)  ,
   nb_conge_total NUMERIC(10,2)  ,
   nb_conge_restant NUMERIC(10,2)  ,
   nb_conge_pris NUMERIC(10,2)  ,
   annee_exercice INTEGER NOT NULL,
   date_cloture DATE,
   statut_cloture INTEGER,
   created_at TIMESTAMP NOT NULL,
   modified_at TIMESTAMP,
   id_1 VARCHAR(50) ,
   PRIMARY KEY(id),
   FOREIGN KEY(id_1) REFERENCES Employe(id)
);

CREATE TABLE Pointage(
   id VARCHAR(50) ,
   date_pointage DATE NOT NULL,
   duree_heure_travaillee_minute NUMERIC(10,2)  ,
   duree_retard_minute NUMERIC(10,2)  ,
   is_shift_jour BOOLEAN,
   commentaire TEXT,
   is_week_end BOOLEAN,
   statut INTEGER NOT NULL,
   is_ferie BOOLEAN,
   date_heure_arrivee TIMESTAMP,
   date_heure_depart TIMESTAMP,
   created_at TIMESTAMP,
   modified_at TIMESTAMP,
   id_1 VARCHAR(50)  NOT NULL,
   PRIMARY KEY(id),
   FOREIGN KEY(id_1) REFERENCES Employe(id)
);

CREATE TABLE rubrique_paie(
   id VARCHAR(50) ,
   code VARCHAR(20) ,
   mode_calcul VARCHAR(50) ,
   libelle VARCHAR(100) ,
   plafond_mensuel NUMERIC(10,2)  ,
   est_imposable BOOLEAN,
   est_soumis_cotisations BOOLEAN,
   est_deductible_irsa BOOLEAN,
   compte_comptable INTEGER,
   ordre INTEGER,
   est_actif BOOLEAN,
   plafond_annuel NUMERIC(10,2)  ,
   commentaire TEXT,
   modified_at TIMESTAMP,
   created_at TIMESTAMP NOT NULL,
   statut INTEGER,
   id_1 VARCHAR(50) ,
   id_2 VARCHAR(50) ,
   id_3 TEXT,
   PRIMARY KEY(id),
   FOREIGN KEY(id_1) REFERENCES formule(id),
   FOREIGN KEY(id_2) REFERENCES categorie_rub(id),
   FOREIGN KEY(id_3) REFERENCES rubrique_types(id)
);

CREATE TABLE Mode_paiement(
   id VARCHAR(50) ,
   nom_banque VARCHAR(255) ,
   code_banque VARCHAR(20) ,
   code_guichet VARCHAR(20) ,
   numero_compte VARCHAR(50) ,
   cle_rib VARCHAR(2) ,
   titulaire_compte VARCHAR(255) ,
   domiciliation_agence VARCHAR(255) ,
   created_at TIMESTAMP NOT NULL,
   modified_at TIMESTAMP,
   statut INTEGER,
   id_1 VARCHAR(50)  NOT NULL,
   id_2 VARCHAR(50) ,
   PRIMARY KEY(id),
   FOREIGN KEY(id_1) REFERENCES type_paiement(id),
   FOREIGN KEY(id_2) REFERENCES Employe(id)
);

CREATE TABLE demande_absence(
   id VARCHAR(50) ,
   created_at TIMESTAMP NOT NULL,
   modified_at TIMESTAMP,
   statut INTEGER,
   date_heure_absence_debut TIMESTAMP,
   date_heure_bsence_fin TIMESTAMP,
   commentaite TEXT,
   id_1 VARCHAR(50) ,
   id_2 VARCHAR(50) ,
   PRIMARY KEY(id),
   FOREIGN KEY(id_1) REFERENCES Employe(id),
   FOREIGN KEY(id_2) REFERENCES type_demande(id)
);

CREATE TABLE doc_justificatif(
   id VARCHAR(50) ,
   url_piece_justificatif VARCHAR(150) ,
   created_at TIMESTAMP NOT NULL,
   commentaire VARCHAR(150) ,
   id_1 VARCHAR(50) ,
   PRIMARY KEY(id),
   FOREIGN KEY(id_1) REFERENCES demande_absence(id)
);

CREATE TABLE periode_paie(
   id VARCHAR(50) ,
   date_debut DATE,
   date_fin DATE,
   statut INTEGER,
   created_at TIMESTAMP NOT NULL,
   modified_at TIMESTAMP,
   date_cloture DATE,
   annee INTEGER,
   id_1 INTEGER NOT NULL,
   PRIMARY KEY(id),
   FOREIGN KEY(id_1) REFERENCES mois(id)
);

CREATE TABLE Pointage_fille(
   id VARCHAR(50) ,
   date_heure_pointage TIMESTAMP NOT NULL,
   created_at TIMESTAMP NOT NULL,
   modified_at TIMESTAMP,
   type_action VARCHAR(3) ,
   source VARCHAR(50) ,
   id_1 VARCHAR(50)  NOT NULL,
   PRIMARY KEY(id),
   FOREIGN KEY(id_1) REFERENCES Pointage(id)
);

CREATE TABLE Demande_conge(
   id VARCHAR(50) ,
   date_debut DATE NOT NULL,
   date_fin DATE NOT NULL,
   nb_jours NUMERIC(10,2)   NOT NULL,
   autre_motif VARCHAR(255) ,
   date_demande DATE NOT NULL,
   date_validation DATE,
   statut INTEGER,
   commentaire_manager TEXT,
   commentaire_rh TEXT,
   modified_at TIMESTAMP,
   created_at TIMESTAMP NOT NULL,
   commentaire_annulation TEXT,
   id_1 VARCHAR(50) ,
   id_2 VARCHAR(50) ,
   id_3 VARCHAR(50) ,
   PRIMARY KEY(id),
   FOREIGN KEY(id_1) REFERENCES manager(id),
   FOREIGN KEY(id_2) REFERENCES Employe(id),
   FOREIGN KEY(id_3) REFERENCES type_conge(id)
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
   periodicite_paiement VARCHAR(50) ,
   id_1 INTEGER,
   id_2 VARCHAR(50) ,
   id_3 VARCHAR(50)  NOT NULL,
   id_4 VARCHAR(50)  NOT NULL,
   id_5 VARCHAR(50)  NOT NULL,
   id_6 VARCHAR(50)  NOT NULL,
   id_7 VARCHAR(50)  NOT NULL,
   id_8 VARCHAR(50)  NOT NULL,
   PRIMARY KEY(id),
   FOREIGN KEY(id_1) REFERENCES type_temps_travail(id),
   FOREIGN KEY(id_2) REFERENCES categorie_professionnelle(id),
   FOREIGN KEY(id_3) REFERENCES Departement(id),
   FOREIGN KEY(id_4) REFERENCES type_entree(id),
   FOREIGN KEY(id_5) REFERENCES manager(id),
   FOREIGN KEY(id_6) REFERENCES Poste(id),
   FOREIGN KEY(id_7) REFERENCES Type_contrat(id),
   FOREIGN KEY(id_8) REFERENCES Employe(id)
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
   id_1 VARCHAR(50) ,
   id_2 VARCHAR(50) ,
   id_3 VARCHAR(50)  NOT NULL,
   id_4 VARCHAR(50) ,
   id_5 VARCHAR(50) ,
   id_6 VARCHAR(50) ,
   PRIMARY KEY(id),
   FOREIGN KEY(id_1) REFERENCES Employe(id),
   FOREIGN KEY(id_2) REFERENCES Employe(id),
   FOREIGN KEY(id_3) REFERENCES infos_professionnelles(id),
   FOREIGN KEY(id_4) REFERENCES infos_professionnelles(id),
   FOREIGN KEY(id_5) REFERENCES Employe(id),
   FOREIGN KEY(id_6) REFERENCES type_mouvement(id)
);

CREATE TABLE paie(
   id VARCHAR(50) ,
   statut INTEGER,
   created_at TIMESTAMP NOT NULL,
   modified_at TIMESTAMP,
   id_1 VARCHAR(50) ,
   id_2 INTEGER NOT NULL,
   id_3 VARCHAR(50) ,
   PRIMARY KEY(id),
   FOREIGN KEY(id_1) REFERENCES periode_paie(id),
   FOREIGN KEY(id_2) REFERENCES information_societe(id),
   FOREIGN KEY(id_3) REFERENCES Employe(id)
);

CREATE TABLE paie_fille(
   id VARCHAR(50) ,
   montant NUMERIC(10,2)  ,
   taux NUMERIC(10,2)  ,
   base NUMERIC(12,2)  ,
   nombre NUMERIC(10,2)  ,
   modified_at TIMESTAMP,
   created_at TIMESTAMP NOT NULL,
   id_1 VARCHAR(50)  NOT NULL,
   id_2 VARCHAR(50)  NOT NULL,
   PRIMARY KEY(id),
   FOREIGN KEY(id_1) REFERENCES paie(id),
   FOREIGN KEY(id_2) REFERENCES rubrique_paie(id)
);
