-- sexe test data
BEGIN;
INSERT INTO sexe (sexe, code) VALUES 
('Masculin', 'M'),
('Féminin', 'F'),
('Non binaire', 'N'),
('Autre', 'A'),
('Non spécifié', 'X');
COMMIT;