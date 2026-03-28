-- manager test data
BEGIN;
INSERT INTO manager (id, date_debut, date_fin, created_at, modified_at, statut, id_departement, id_employe, commentaire) VALUES ('MGR-0001', '2024-01-01', NULL, 'NOW()', 'NOW()', '0', 'DEP1', 'EMP-TEST-0001', 'Manager DEP1');
INSERT INTO manager (id, date_debut, date_fin, created_at, modified_at, statut, id_departement, id_employe, commentaire) VALUES ('MGR-0002', '2024-01-01', NULL, 'NOW()', 'NOW()', '0', 'DEP3', 'EMP-TEST-0002', 'Manager DEP3');
INSERT INTO manager (id, date_debut, date_fin, created_at, modified_at, statut, id_departement, id_employe, commentaire) VALUES ('MGR-0003', '2024-01-01', NULL, 'NOW()', 'NOW()', '0', 'DEP2', 'EMP-TEST-0003', 'Manager DEP2');
INSERT INTO manager (id, date_debut, date_fin, created_at, modified_at, statut, id_departement, id_employe, commentaire) VALUES ('MGR-0004', '2024-01-01', NULL, 'NOW()', 'NOW()', '0', 'DEP4', 'EMP-TEST-0004', 'Manager DEP4');
INSERT INTO manager (id, date_debut, date_fin, created_at, modified_at, statut, id_departement, id_employe, commentaire) VALUES ('MGR-0005', '2024-01-01', NULL, 'NOW()', 'NOW()', '0', 'DEP5', 'EMP-TEST-0005', 'Manager DEP5');
COMMIT;

