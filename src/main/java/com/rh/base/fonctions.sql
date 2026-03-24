-- -----------------------------
-- Employe
-- -----------------------------
CREATE OR REPLACE FUNCTION generate_employe_id()
RETURNS TRIGGER AS $$
DECLARE
    seq_value INT;
BEGIN
    seq_value := nextval('seq_employe');
    NEW.id := 'EMP' || LPAD(seq_value::TEXT, 3, '0');
    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

DROP TRIGGER IF EXISTS before_insert_employe ON Employe;
CREATE TRIGGER before_insert_employe
BEFORE INSERT ON Employe
FOR EACH ROW
WHEN (NEW.id IS NULL)
EXECUTE PROCEDURE generate_employe_id();

-- -----------------------------
-- InfosAdministratives
-- -----------------------------
CREATE OR REPLACE FUNCTION generate_infos_admin_id()
RETURNS TRIGGER AS $$
DECLARE
    seq_value INT;
BEGIN
    seq_value := nextval('seq_infos_administratives');
    NEW.id := 'IA' || LPAD(seq_value::TEXT, 3, '0');
    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

DROP TRIGGER IF EXISTS before_insert_infos_admin ON infos_Administratives;
CREATE TRIGGER before_insert_infos_admin
BEFORE INSERT ON infos_Administratives
FOR EACH ROW
WHEN (NEW.id IS NULL)
EXECUTE PROCEDURE generate_infos_admin_id();

-- -----------------------------
-- InfosProfessionnelles
-- -----------------------------
CREATE OR REPLACE FUNCTION generate_infos_pro_id()
RETURNS TRIGGER AS $$
DECLARE
    seq_value INT;
BEGIN
    seq_value := nextval('seq_infos_professionnelles');
    NEW.id := 'IP' || LPAD(seq_value::TEXT, 3, '0');
    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

DROP TRIGGER IF EXISTS before_insert_infos_pro ON infos_Professionnelles;
CREATE TRIGGER before_insert_infos_pro
BEFORE INSERT ON infos_Professionnelles
FOR EACH ROW
WHEN (NEW.id IS NULL)
EXECUTE PROCEDURE generate_infos_pro_id();

-- -----------------------------
-- EmergencyContact
-- -----------------------------
CREATE OR REPLACE FUNCTION generate_emergency_id()
RETURNS TRIGGER AS $$
DECLARE
    seq_value INT;
BEGIN
    seq_value := nextval('seq_emergency_contact');
    NEW.id := 'EC' || LPAD(seq_value::TEXT, 3, '0');
    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

DROP TRIGGER IF EXISTS before_insert_emergency ON emergency_contact;
CREATE TRIGGER before_insert_emergency
BEFORE INSERT ON emergency_contact
FOR EACH ROW
WHEN (NEW.id IS NULL)
EXECUTE PROCEDURE generate_emergency_id();

-- -----------------------------
-- Users
-- -----------------------------
CREATE OR REPLACE FUNCTION generate_user_id()
RETURNS TRIGGER AS $$
DECLARE
    seq_value INT;
BEGIN
    seq_value := nextval('seq_users');
    NEW.id := 'USR' || LPAD(seq_value::TEXT, 3, '0');
    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

DROP TRIGGER IF EXISTS before_insert_user ON Users;
CREATE TRIGGER before_insert_user
BEFORE INSERT ON Users
FOR EACH ROW
WHEN (NEW.id IS NULL)
EXECUTE PROCEDURE generate_user_id();

-- Corriger la fonction pour type_document
CREATE OR REPLACE FUNCTION generate_type_document_id()
RETURNS TRIGGER AS $$
DECLARE
    seq_value INT;
BEGIN
    seq_value := nextval('seq_type_document_id');  -- ← Ajouter '_id'
    NEW.id := 'TYP' || LPAD(seq_value::TEXT, 3, '0');
    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

-- Corriger la fonction pour document_employe
CREATE OR REPLACE FUNCTION generate_document_employe_id()
RETURNS TRIGGER AS $$
DECLARE
    seq_value INT;
BEGIN
    seq_value := nextval('seq_document_employe_id');  -- ← Ajouter '_id'
    NEW.id := 'DOC' || LPAD(seq_value::TEXT, 3, '0');
    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

-- Trigger pour type_document
DROP TRIGGER IF EXISTS before_insert_type_document ON type_document;
CREATE TRIGGER before_insert_type_document
BEFORE INSERT ON type_document
FOR EACH ROW
WHEN (NEW.id IS NULL)
EXECUTE PROCEDURE generate_type_document_id(); 

-- Trigger pour document_employe
DROP TRIGGER IF EXISTS before_insert_document_employe ON document_employe;
CREATE TRIGGER before_insert_document_employe
BEFORE INSERT ON document_employe
FOR EACH ROW
WHEN (NEW.id IS NULL)
EXECUTE PROCEDURE generate_document_employe_id();

-- Fonction pour générer l'ID de Poste
CREATE OR REPLACE FUNCTION generate_poste_id()
RETURNS TRIGGER AS $$
DECLARE
    seq_value INT;
BEGIN
    seq_value := nextval('seq_poste_id');
    NEW.id := 'POST' || LPAD(seq_value::TEXT, 3, '0');
    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

-- Fonction pour générer l'ID de Type_contrat
CREATE OR REPLACE FUNCTION generate_type_contrat_id()
RETURNS TRIGGER AS $$
DECLARE
    seq_value INT;
BEGIN
    seq_value := nextval('seq_type_contrat_id');
    NEW.id := 'CONT' || LPAD(seq_value::TEXT, 3, '0');
    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

-- Trigger pour Poste
DROP TRIGGER IF EXISTS before_insert_poste ON poste;
CREATE TRIGGER before_insert_poste
BEFORE INSERT ON poste
FOR EACH ROW
WHEN (NEW.id IS NULL)
EXECUTE PROCEDURE generate_poste_id();

-- Trigger pour Type_contrat
DROP TRIGGER IF EXISTS before_insert_type_contrat ON type_contrat;
CREATE TRIGGER before_insert_type_contrat
BEFORE INSERT ON type_contrat
FOR EACH ROW
WHEN (NEW.id IS NULL)
EXECUTE PROCEDURE generate_type_contrat_id(); 

-- Fonction pour générer un ID Type Département
CREATE OR REPLACE FUNCTION generate_type_departement_id()
RETURNS TRIGGER AS $$
BEGIN
    IF NEW.id IS NULL THEN
        NEW.id := 'TDEP' || nextval('seq_type_departement_id');
    END IF;
    RETURN NEW;
END;
$$ LANGUAGE plpgsql;


-- Fonction pour générer un ID Département
CREATE OR REPLACE FUNCTION generate_departement_id()
RETURNS TRIGGER AS $$
BEGIN
    IF NEW.id IS NULL THEN
        NEW.id := 'DEP' || nextval('seq_departement_id');
    END IF;
    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

-- Trigger pour Type Département
DROP TRIGGER IF EXISTS before_insert_type_departement ON type_departement;
CREATE TRIGGER before_insert_type_departement
BEFORE INSERT ON type_departement
FOR EACH ROW
WHEN (NEW.id IS NULL)
EXECUTE PROCEDURE generate_type_departement_id();


-- Trigger pour Département
DROP TRIGGER IF EXISTS before_insert_departement ON departement;
CREATE TRIGGER before_insert_departement
BEFORE INSERT ON departement
FOR EACH ROW
WHEN (NEW.id IS NULL)
EXECUTE PROCEDURE generate_departement_id();

-- Fonction pour générer l'ID de catégorie
-- Fonction pour générer l'ID de catégorie et la date de création
CREATE OR REPLACE FUNCTION generate_categorie_id()
RETURNS TRIGGER AS $$
BEGIN
    -- Générer l'ID si null
    IF NEW.id IS NULL THEN
        NEW.id := 'CAT' || nextval('categorie_id_seq');
    END IF;
    
    -- Définir la date de création si null
    IF NEW.created_at IS NULL THEN
        NEW.created_at := CURRENT_TIMESTAMP;
    END IF;
    
    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

-- Trigger pour la table categorie
-- Utiliser EXECUTE PROCEDURE au lieu de EXECUTE FUNCTION
DROP TRIGGER IF EXISTS before_insert_categorie ON categorie;
CREATE TRIGGER before_insert_categorie
BEFORE INSERT ON categorie
FOR EACH ROW
WHEN (NEW.id IS NULL)
EXECUTE PROCEDURE generate_categorie_id();

