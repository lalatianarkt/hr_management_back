-- sexe test data
BEGIN;
INSERT INTO sexe (id, sexe, code) VALUES 
(1, 'Masculin', 'M'),
(2, 'Féminin', 'F'),
(3, 'Non binaire', 'N'),
(4, 'Autre', 'A'),
(5, 'Non spécifié', 'X');
COMMIT;

