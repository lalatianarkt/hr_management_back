-- ========================================
-- 1️⃣ SEQUENCES
-- ========================================

CREATE SEQUENCE seq_emergency_contact START 1;
CREATE SEQUENCE seq_infos_professionnelles START 1;
CREATE SEQUENCE seq_infos_administratives START 1;
CREATE SEQUENCE seq_employe START 1;
CREATE SEQUENCE seq_users START 1;
CREATE SEQUENCE seq_token START 1;

-- type_user a déjà SERIAL, donc pas besoin de séquence externe 

-- Création de la séquence pour type_document
CREATE SEQUENCE seq_type_document_id START WITH 1 INCREMENT BY 1;

-- Création de la séquence pour document_employe
CREATE SEQUENCE seq_document_employe_id START WITH 1 INCREMENT BY 1;

-- Séquence pour Poste
CREATE SEQUENCE seq_poste_id START WITH 1 INCREMENT BY 1;

-- Séquence pour Type_contrat
CREATE SEQUENCE seq_type_contrat_id START WITH 1 INCREMENT BY 1;

CREATE SEQUENCE seq_departement_id
    START WITH 1
    INCREMENT BY 1;

CREATE SEQUENCE seq_type_departement_id
    START WITH 1
    INCREMENT BY 1;

CREATE SEQUENCE seq_manager_id
    START WITH 1
    INCREMENT BY 1;

-- Créer une séquence pour les IDs de catégorie
CREATE SEQUENCE categorie_id_seq
    START WITH 1
    INCREMENT BY 1;  

