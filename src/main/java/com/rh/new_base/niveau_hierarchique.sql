INSERT INTO Niveau_hierarchique (id, nom, rang, description, created_at, modified_at) VALUES
('NIV1', 'Employe', 1, 'Personnel d execution', NOW(), NULL),
('NIV2', 'Assistant', 2, 'Personnel d appui administratif ou technique', NOW(), NULL),
('NIV3', 'Agent Principal', 3, 'Employe experimente avec taches specifiques', NOW(), NULL),
('NIV4', 'Superviseur', 4, 'Encadrement operationnel d equipe', NOW(), NULL),
('NIV5', 'Chef de Service', 5, 'Responsable d un service ou departement', NOW(), NULL),
('NIV6', 'Directeur', 6, 'Membre de la direction, responsable d une division', NOW(), NULL),
('NIV7', 'Direction Generale', 7, 'Plus haut niveau hierarchique', NOW(), NULL);