INSERT INTO base_irsa (id, tranche_min, tranche_max, taux, num_tranche, created_at, modified_at)
VALUES
('IRSA_T1', 0, 350000, 0, 1, NOW(), NOW()),
('IRSA_T2', 350001, 400000, 5, 2, NOW(), NOW()),
('IRSA_T3', 400001, 500000, 10, 3, NOW(), NOW()),
('IRSA_T4', 500001, 600000, 15, 4, NOW(), NOW()),
('IRSA_T5', 600001, 4000000, 20, 5, NOW(), NOW()),
('IRSA_T6', 4000001, NULL, 25, 6, NOW(), NOW());