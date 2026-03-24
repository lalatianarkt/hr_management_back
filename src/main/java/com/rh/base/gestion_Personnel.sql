CREATE TABLE Poste(
   id VARCHAR(50) ,
   nom VARCHAR(100)  NOT NULL,
   description VARCHAR(255) ,
   created_at TIMESTAMP,
   modified_at TIMESTAMP,
   PRIMARY KEY(id)
);

CREATE TABLE Type_document(
   id VARCHAR(50) ,
   intitule VARCHAR(50)  NOT NULL,
   PRIMARY KEY(id)
);

CREATE TABLE Type_contrat(
   id VARCHAR(50) ,
   intitule VARCHAR(50)  NOT NULL,
   description VARCHAR(255) ,
   PRIMARY KEY(id)
);

CREATE TABLE Historique_conge(
   id VARCHAR(50) ,
   id_employe VARCHAR(50) ,
   id_demande_conge VARCHAR(50) ,
   date_debut VARCHAR(50) ,
   date_fin VARCHAR(50) ,
   nb_jours VARCHAR(50) ,
   type_conge VARCHAR(50) ,
   PRIMARY KEY(id)
);

CREATE TABLE Historique_absence(
   id VARCHAR(50) ,
   id_employe VARCHAR(50) ,
   duree_heures VARCHAR(50) ,
   motif VARCHAR(255) ,
   justifiee VARCHAR(1) ,
   remarque VARCHAR(255) ,
   date_absence VARCHAR(50) ,
   PRIMARY KEY(id)
);

CREATE TABLE type_conge(
   id VARCHAR(50) ,
   intitule VARCHAR(50)  NOT NULL,
   description VARCHAR(255) ,
   PRIMARY KEY(id)
);

CREATE TABLE emergency_contact(
   id VARCHAR(50) ,
   contact VARCHAR(50) ,
   email VARCHAR(50) ,
   adresse VARCHAR(50) ,
   nom VARCHAR(250) ,
   PRIMARY KEY(id)
);

CREATE TABLE Historique_poste(
   id SERIAL,
   id_poste VARCHAR(150) ,
   id_employe VARCHAR(255) ,
   date_debut DATE,
   date_fin DATE,
   PRIMARY KEY(id)
);

CREATE TABLE Type_sanction(
   id VARCHAR(50) ,
   intitule VARCHAR(150)  NOT NULL,
   created_at TIMESTAMP,
   modified_at TIMESTAMP,
   PRIMARY KEY(id)
);

CREATE TABLE Type_departement(
   id VARCHAR(50) ,
   intitule VARCHAR(150)  NOT NULL,
   created_at DATE NOT NULL,
   modified_at DATE,
   PRIMARY KEY(id)
);

CREATE TABLE Type_avertissment(
   id VARCHAR(50) ,
   intitule VARCHAR(150)  NOT NULL,
   created_at TIMESTAMP NOT NULL,
   modified_at TIMESTAMP,
   PRIMARY KEY(id)
);

CREATE TABLE Historique_avertissement(
   id SERIAL,
   type_avertissement VARCHAR(150) ,
   idEmploye VARCHAR(50) ,
   date_averti VARCHAR(50) ,
   PRIMARY KEY(id)
);

CREATE TABLE Solde_conge(
   id SERIAL,
   date_debut DATE,
   date_fin VARCHAR(50) ,
   PRIMARY KEY(id)
);

CREATE TABLE Historique_sanction(
   id SERIAL,
   type_sanction VARCHAR(50) ,
   nomEmploye VARCHAR(150) ,
   date_sanction VARCHAR(50) ,
   PRIMARY KEY(id)
);

CREATE TABLE Parametre_solde_sanction(
   id SERIAL,
   PRIMARY KEY(id)
);

CREATE TABLE type_user(
   id SERIAL,
   type VARCHAR(50)  NOT NULL,
   PRIMARY KEY(id)
);

CREATE TABLE situation_familiale(
   id SERIAL,
   type VARCHAR(50)  NOT NULL,
   PRIMARY KEY(id)
);

CREATE TABLE nationalite(
   id SERIAL,
   nationalite VARCHAR(150)  NOT NULL,
   PRIMARY KEY(id)
);

CREATE TABLE sexe(
   id SERIAL,
   sexe VARCHAR(50)  NOT NULL,
   code VARCHAR(1) ,
   PRIMARY KEY(id)
);

CREATE TABLE departement_employe(
   id VARCHAR(50) ,
   created_at TIMESTAMP NOT NULL,
   modified_at TIMESTAMP,
   PRIMARY KEY(id)
);

CREATE TABLE Departement(
   id VARCHAR(50) ,
   nom VARCHAR(50)  NOT NULL,
   description VARCHAR(100) ,
   created_at TIMESTAMP NOT NULL,
   modified_at TIMESTAMP,
   nb_employe INTEGER NOT NULL,
   id_1 VARCHAR(50)  NOT NULL,
   PRIMARY KEY(id),
   FOREIGN KEY(id_1) REFERENCES Type_departement(id)
);

CREATE TABLE infos_Administratives(
   id VARCHAR(50) ,
   num_cnaps VARCHAR(50)  NOT NULL,
   cin VARCHAR(20)  NOT NULL,
   nombre_enfants INTEGER,
   id_1 INTEGER,
   PRIMARY KEY(id),
   UNIQUE(cin),
   FOREIGN KEY(id_1) REFERENCES situation_familiale(id)
);

CREATE TABLE Employe(
   id VARCHAR(50) ,
   nom VARCHAR(100)  NOT NULL,
   prenom VARCHAR(250)  NOT NULL,
   date_naissance DATE NOT NULL,
   telephone VARCHAR(12)  NOT NULL,
   email VARCHAR(100)  NOT NULL,
   adresse VARCHAR(255)  NOT NULL,
   created_at TIMESTAMP NOT NULL,
   modified_at TIMESTAMP,
   nom_mere VARCHAR(255) ,
   nom_pere VARCHAR(255) ,
   lieu_naissance VARCHAR(250)  NOT NULL,
   id_1 INTEGER NOT NULL,
   id_2 INTEGER NOT NULL,
   id_3 VARCHAR(50)  NOT NULL,
   id_4 VARCHAR(50)  NOT NULL,
   PRIMARY KEY(id),
   FOREIGN KEY(id_1) REFERENCES sexe(id),
   FOREIGN KEY(id_2) REFERENCES nationalite(id),
   FOREIGN KEY(id_3) REFERENCES emergency_contact(id),
   FOREIGN KEY(id_4) REFERENCES infos_Administratives(id)
);

CREATE TABLE Contrat(
   id VARCHAR(50) ,
   date_debut DATE NOT NULL,
   date_fin DATE,
   duree INTEGER,
   statut VARCHAR(50) ,
   id_1 VARCHAR(50)  NOT NULL,
   id_2 VARCHAR(50)  NOT NULL,
   id_3 VARCHAR(50)  NOT NULL,
   PRIMARY KEY(id),
   FOREIGN KEY(id_1) REFERENCES Poste(id),
   FOREIGN KEY(id_2) REFERENCES Type_contrat(id),
   FOREIGN KEY(id_3) REFERENCES Employe(id)
);

CREATE TABLE Document_employe(
   id VARCHAR(50) ,
   nom_fichier VARCHAR(100)  NOT NULL,
   chemin_fichier VARCHAR(150)  NOT NULL,
   date_upload TIMESTAMP NOT NULL,
   date_modified TIMESTAMP,
   id_1 VARCHAR(50) ,
   id_2 VARCHAR(50)  NOT NULL,
   PRIMARY KEY(id),
   FOREIGN KEY(id_1) REFERENCES Type_document(id),
   FOREIGN KEY(id_2) REFERENCES Employe(id)
);

CREATE TABLE Manager(
   id VARCHAR(50) ,
   description VARCHAR(250) ,
   id_1 VARCHAR(50)  NOT NULL,
   id_2 VARCHAR(50)  NOT NULL,
   PRIMARY KEY(id),
   FOREIGN KEY(id_1) REFERENCES Departement(id),
   FOREIGN KEY(id_2) REFERENCES Employe(id)
);

CREATE TABLE Pointage(
   id VARCHAR(50) ,
   heure_arrivee TIME NOT NULL,
   heure_depart TIME NOT NULL,
   heure_journaliere TIME,
   date_du_jour DATE NOT NULL,
   created_at TIMESTAMP NOT NULL,
   modified_at TIMESTAMP,
   id_1 VARCHAR(50)  NOT NULL,
   PRIMARY KEY(id),
   FOREIGN KEY(id_1) REFERENCES Employe(id)
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

CREATE TABLE Compte_conge(
   id SERIAL,
   nb_conge_total INTEGER NOT NULL,
   nb_conge_restant INTEGER NOT NULL,
   nb_conge_pris INTEGER NOT NULL,
   annee INTEGER NOT NULL,
   id_1 VARCHAR(50)  NOT NULL,
   PRIMARY KEY(id),
   FOREIGN KEY(id_1) REFERENCES Employe(id)
);

CREATE TABLE Demande_absence(
   id SERIAL,
   heure_debut VARCHAR(50)  NOT NULL,
   heure_fin VARCHAR(50)  NOT NULL,
   motif VARCHAR(150)  NOT NULL,
   statut INTEGER NOT NULL,
   date_demande DATE NOT NULL,
   date_absence DATE NOT NULL,
   id_1 VARCHAR(50)  NOT NULL,
   PRIMARY KEY(id),
   FOREIGN KEY(id_1) REFERENCES Employe(id)
);

CREATE TABLE Demande_conge(
   id VARCHAR(50) ,
   date_debut DATE NOT NULL,
   date_fin DATE NOT NULL,
   motif VARCHAR(255)  NOT NULL,
   statut INTEGER NOT NULL,
   date_demande DATE NOT NULL,
   decision_manager VARCHAR(150) ,
   id_1 VARCHAR(50) ,
   id_2 VARCHAR(50)  NOT NULL,
   PRIMARY KEY(id),
   FOREIGN KEY(id_1) REFERENCES Employe(id),
   FOREIGN KEY(id_2) REFERENCES type_conge(id)
);

CREATE TABLE Sanctions(
   id VARCHAR(50) ,
   id_1 VARCHAR(50) ,
   id_2 VARCHAR(50)  NOT NULL,
   PRIMARY KEY(id),
   FOREIGN KEY(id_1) REFERENCES Employe(id),
   FOREIGN KEY(id_2) REFERENCES Type_sanction(id)
);

CREATE TABLE Avertissement(
   id VARCHAR(50) ,
   date_averti DATE NOT NULL,
   motif VARCHAR(250) ,
   id_1 VARCHAR(50) ,
   id_2 VARCHAR(50)  NOT NULL,
   PRIMARY KEY(id),
   FOREIGN KEY(id_1) REFERENCES Employe(id),
   FOREIGN KEY(id_2) REFERENCES Type_avertissment(id)
);

CREATE TABLE Poste_employe(
   id VARCHAR(50) ,
   date_debut DATE NOT NULL,
   date_fin DATE NOT NULL,
   id_1 VARCHAR(50)  NOT NULL,
   id_2 VARCHAR(50)  NOT NULL,
   PRIMARY KEY(id),
   FOREIGN KEY(id_1) REFERENCES Poste(id),
   FOREIGN KEY(id_2) REFERENCES Employe(id)
);

CREATE TABLE manager_employe(
   id SERIAL,
   date_fin DATE,
   date_debut DATE,
   id_1 VARCHAR(50)  NOT NULL,
   PRIMARY KEY(id),
   FOREIGN KEY(id_1) REFERENCES Manager(id)
);

CREATE TABLE infos_professionnelles(
   id VARCHAR(50) ,
   matricule NUMERIC(15,2)   NOT NULL,
   date_embauche DATE NOT NULL,
   date_debut DATE NOT NULL,
   date_fin DATE,
   created_at TIMESTAMP NOT NULL,
   modified_at TIMESTAMP,
   id_1 VARCHAR(50)  NOT NULL,
   id_2 VARCHAR(50)  NOT NULL,
   id_3 INTEGER NOT NULL,
   id_4 VARCHAR(50)  NOT NULL,
   PRIMARY KEY(id),
   FOREIGN KEY(id_1) REFERENCES departement_employe(id),
   FOREIGN KEY(id_2) REFERENCES Poste_employe(id),
   FOREIGN KEY(id_3) REFERENCES manager_employe(id),
   FOREIGN KEY(id_4) REFERENCES Manager(id)
);

CREATE TABLE info_pro_employe(
   id VARCHAR(50) ,
   id_1 VARCHAR(50)  NOT NULL,
   id_2 VARCHAR(50)  NOT NULL,
   PRIMARY KEY(id),
   FOREIGN KEY(id_1) REFERENCES infos_professionnelles(id),
   FOREIGN KEY(id_2) REFERENCES Employe(id)
);

CREATE TABLE Asso_44(
   id VARCHAR(50) ,
   id_1 VARCHAR(50) ,
   PRIMARY KEY(id, id_1),
   FOREIGN KEY(id) REFERENCES Departement(id),
   FOREIGN KEY(id_1) REFERENCES departement_employe(id)
);
