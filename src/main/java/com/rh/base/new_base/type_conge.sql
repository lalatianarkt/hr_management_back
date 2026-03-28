INSERT INTO type_conge (id, intitule, description, obligatoire_doc, created_at, modified_at, is_cumulable) VALUES
('TC001', 'Conge annuel', 'Conge paye annuel pour tous les employes', FALSE, CURRENT_TIMESTAMP, NULL, false),
('TC002', 'Conge maladie', 'Conge pour raison de sante sur presentation d un certificat medical', false, CURRENT_TIMESTAMP, NULL, FALSE),
('TC003', 'Conge maternite', 'Conge pour maternite selon la legislation en vigueur', false, CURRENT_TIMESTAMP, NULL, FALSE),
('TC004', 'Conge paternite', 'Conge pour paternite selon la legislation', false, CURRENT_TIMESTAMP, NULL, FALSE),
('TC005', 'RTT', 'Jours de reduction du temps de travail', FALSE, CURRENT_TIMESTAMP, NULL, false),
('TC006', 'Conge sans solde', 'Conge autorise mais non paye', FALSE, CURRENT_TIMESTAMP, NULL, FALSE),
('TC007', 'Conge pour mariage', 'Conge accorde pour mariage ou PACS', FALSE, CURRENT_TIMESTAMP, NULL, FALSE),
('TC008', 'Conge pour deces', 'Conge en cas de deces d un proche', FALSE, CURRENT_TIMESTAMP, NULL, FALSE);