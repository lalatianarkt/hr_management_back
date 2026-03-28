-- Catégories professionnelles selon la pratique du droit du travail malgache
INSERT INTO categorie_professionnelle (id, code, libelle, description, created_at, modified_at) VALUES
('CPG01', 'G1', 'Groupe 1', 'Manœuvres, ouvriers non qualifiés, agents d''exécution sans qualification', NOW(), NULL),
('CPG02', 'G2', 'Groupe 2', 'Ouvriers qualifiés, employés d''exécution avec expérience ou formation de base', NOW(), NULL),
('CPG03', 'G3', 'Groupe 3', 'Employés qualifiés, techniciens, agents de maîtrise', NOW(), NULL),
('CPG04', 'G4', 'Groupe 4', 'Cadres moyens, chefs d''équipe, responsables de service', NOW(), NULL),
('CPG05', 'G5', 'Groupe 5', 'Cadres supérieurs, direction, management stratégique', NOW(), NULL);
