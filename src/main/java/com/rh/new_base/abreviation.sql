insert into abreviation(id, libelle, abreviation, description, created_at, modified_at) VALUES
(1, 'Salaire horaire', 'SH', 'Equivalent de salaire en heures', now(), now());
insert into abreviation(id, libelle, abreviation, description, created_at, modified_at) VALUES
(2, 'nombre', 'nb', 'quantité', now(), now());
insert into abreviation(id, libelle, abreviation, description, created_at, modified_at) VALUES
(3, 'taux', 'taux', 'taux de majoration', now(), now());
insert into abreviation(id, libelle, abreviation, description, created_at, modified_at) VALUES
(4, 'Salaire journalier', 'SJ', 'Equivalent de salaire par jour', now(), now());
insert into abreviation(id, libelle, abreviation, description, created_at, modified_at) VALUES
(5, 'base irsa', 'base_irsa', NULL, now(), now());

INSERT INTO abreviation (id, libelle, abreviation, description, created_at, modified_at)
VALUES
(6, 'HS50', 'HS50', NULL, NOW(), NOW()),
(7, 'HS100', 'HS100', NULL, NOW(), NOW()),
(8, 'PR_ANC', 'PR_ANC', NULL, NOW(), NOW()),
(9, 'PR_RISQ', 'PR_RISQ', NULL, NOW(), NOW()),
(10, 'COT_SYN', 'COT_SYN', NULL, NOW(), NOW()),
(11, 'ABS_NP', 'ABS_NP', NULL, NOW(), NOW()),
(12, 'OSTIE-EMP', 'OSTIE-EMP', NULL, NOW(), NOW()),
(13, '13EME', '13EME', NULL, NOW(), NOW()),
(14, 'RAPPEL', 'RAPPEL', NULL, NOW(), NOW()),
(15, 'T_BRUT', 'T_BRUT', NULL, NOW(), NOW()),
(16, 'SBM', 'SBM', NULL, NOW(), NOW()),
(17, 'HS25', 'HS25', NULL, NOW(), NOW()),
(18, 'IND_LOG', 'IND_LOG', NULL, NOW(), NOW()),
(19, 'IND_TR', 'IND_TR', NULL, NOW(), NOW()),
(20, 'PR_PAN', 'PR_PAN', NULL, NOW(), NOW()),
(21, 'PR_PERF', 'PR_PERF', NULL, NOW(), NOW()),
(22, 'IND_CONG', 'IND_CONG', NULL, NOW(), NOW()),
(23, 'CNPS_SAL', 'CNPS_SAL', NULL, NOW(), NOW()),
(24, 'IRSA', 'IRSA', NULL, NOW(), NOW()),
(25, 'AVANCE', 'AVANCE', NULL, NOW(), NOW()),
(26, 'SB', 'SB', NULL, NOW(), NOW()),
(27, 'HS130', 'HS130', NULL, NOW(), NOW()),
(28, 'HS 150', 'HS150', NULL, NOW(), NOW()),
(29, 'HS130 EXO', 'HS130_EXO', NULL, NOW(), NOW()),
(30, 'HS150 EXO', 'HS150_EXO', NULL, NOW(), NOW()),
(31, 'PA', 'PA', NULL, NOW(), NOW()),
(32, 'MN30', 'MN30', NULL, NOW(), NOW()),
(33, 'AFAM', 'AFAM', NULL, NOW(), NOW()),
(34, 'AMENDE', 'AMENDE', NULL, NOW(), NOW()),
(35, 'OSTIE-SAL', 'OSTIE-SAL', NULL, NOW(), NOW()),
(36, 'tiana', 'TIANA', NULL, NOW(), NOW()),
(37, 'CNPS_PAT', 'CNPS_PAT', NULL, NOW(), NOW()),
(38, 'T_COT', 'T_COT', NULL, NOW(), NOW()),
(39, 'NET_IMP', 'NET_IMP', NULL, NOW(), NOW()),
(40, 'T_RET', 'T_RET', NULL, NOW(), NOW()),
(41, 'NET_PAYER', 'NET_PAYER', NULL, NOW(), NOW());


INSERT INTO abreviation (id, libelle, abreviation, description, created_at, modified_at)
VALUES
(41, 'Salaire horaire', 'SH', 'Salaire mensuel de base', NOW(), NOW()),
(42, 'Salaire journalier', 'SJ', 'Salaire utilisé comme référence pour les calculs', NOW(), NOW()),
(43, 'Salaire brut', 'SBRUT', 'Salaire total avant déductions', NOW(), NOW()),
(44, 'Salaire net', 'SNET', 'Salaire après déductions sociales et fiscales', NOW(), NOW()),
(45, 'Salaire brut imposable', 'SBRUT_IMP', 'Salaire brut soumis à l''impôt', NOW(), NOW()),
(46, 'Salaire net imposable', 'SNET_IMP', 'Salaire net soumis à l''impôt', NOW(), NOW());


(47, 'Salaire imposable', 'SIMPO', 'Salaire soumis à l''impôt', NOW()),
(48, 'Heures normales', 'HN', 'Heures de travail normales', NOW()),
(49, 'Heures supplémentaires', 'HS', 'Heures supplémentaires travaillées', NOW()),
(50, 'Heures supplémentaires 25%', 'HS25', 'Heures supplémentaires majorées à 25%', NOW()),
(51, 'Heures supplémentaires 50%', 'HS50', 'Heures supplémentaires majorées à 50%', NOW()),
(52, 'Heures supplémentaires 100%', 'HS100', 'Heures supplémentaires majorées à 100%', NOW()),
(53, 'Heures de nuit', 'HNuit', 'Heures travaillées de nuit', NOW()),
(54, 'Heures de dimanche', 'HDim', 'Heures travaillées le dimanche', NOW()),
(55, 'Heures de jour férié', 'HFer', 'Heures travaillées les jours fériés', NOW());

-- Cotisations sociales (CNaPS, OSTIE, etc.)
INSERT INTO abreviation (id, libelle, abreviation, description, created_at) VALUES
(56, 'Caisse Nationale de Prévoyance Sociale Salarié', 'CNAPS_S', 'Cotisation salariale CNaPS (1%)', NOW()),
(57, 'Caisse Nationale de Prévoyance Sociale Employeur', 'CNAPS_E', 'Cotisation employeur CNaPS (13%)', NOW()),
(58, 'Organisme de Santé au Travail et d''Inspection du Travail Salarié', 'OSTIE_S', 'Cotisation salariale OSTIE (1%)', NOW()),
(59, 'Organisme de Santé au Travail et d''Inspection du Travail Employeur', 'OSTIE_E', 'Cotisation employeur OSTIE (5%)', NOW()),
(60, 'Assurance Vieillesse', 'AV', 'Assurance vieillesse (retraite)', NOW()),
(61, 'Assurance Maladie', 'AM', 'Assurance maladie', NOW()),
(62, 'Assurance Maternité', 'AMAT', 'Assurance maternité', NOW()),
(63, 'Assurance Invalidité Décès', 'AID', 'Assurance invalidité décès', NOW()),
(64, 'Prestations Familiales', 'PF', 'Allocations familiales', NOW());

-- Primes et avantages 
INSERT INTO abreviation (id, libelle, abreviation, description, created_at) VALUES
(65, 'Prime d''ancienneté', 'PA', 'Prime basée sur l''ancienneté', NOW()),
(66, 'Prime de rendement', 'PR', 'Prime liée à la performance', NOW()),
(67, 'Prime de transport', 'PT', 'Indemnité de transport', NOW()),
(68, 'Prime de logement', 'PL', 'Indemnité de logement', NOW()),
(69, 'Prime de panier', 'PP', 'Indemnité repas/panier', NOW()),
(70, 'Prime de risque', 'PRISQ', 'Prime pour travaux dangereux', NOW()),
(71, 'Prime de fonction', 'PFCT', 'Prime liée à la fonction occupée', NOW()),
(72, 'Prime de technicité', 'PTECH', 'Prime pour compétences techniques', NOW()),
(73, 'Prime de treizième mois', 'P13', 'Prime de fin d''année', NOW()),
(74, 'Prime de vacances', 'PVAC', 'Prime de congés payés', NOW()),
(75, 'Indemnité de congé', 'IC', 'Indemnité pour congés pris', NOW()),
(76, 'Indemnité de licenciement', 'IL', 'Indemnité de rupture de contrat', NOW()),
(77, 'Indemnité de préavis', 'IP', 'Indemnité de préavis', NOW());

-- Déductions et retenues 
INSERT INTO abreviation (id, libelle, abreviation, description, created_at) VALUES
(78, 'Impôt sur le revenu des salaires', 'IRSA', 'Impôt retenu à la source', NOW()),
(79, 'Retenue avance sur salaire', 'RAS', 'Avance sur salaire remboursée', NOW()),
(80, 'Retenue prêt personnel', 'RPP', 'Remboursement de prêt personnel', NOW()),
(81, 'Retenue coopérative', 'RCOOP', 'Cotisation coopérative', NOW()),
(82, 'Retenue mutuelle', 'RMUT', 'Cotisation mutuelle santé', NOW()),
(83, 'Retenue syndicale', 'RSYN', 'Cotisation syndicale', NOW()),
(84, 'Retenue assurance groupe', 'RAG', 'Assurance groupe déduite', NOW());

-- Congés et absences 
INSERT INTO abreviation (id, libelle, abreviation, description, created_at) VALUES
(85, 'Congés payés', 'CP', 'Jours de congés payés', NOW()),
(86, 'Congés sans solde', 'CSS', 'Congés non rémunérés', NOW()),
(87, 'Congés maladie', 'CMAL', 'Congés pour maladie', NOW()),
(88, 'Congés maternité', 'CMAT', 'Congés de maternité', NOW()),
(89, 'Congés paternité', 'CPAT', 'Congés de paternité', NOW()),
(90, 'Absences non justifiées', 'ANJ', 'Absences non rémunérées', NOW()),
(91, 'Absences justifiées', 'AJ', 'Absences justifiées payées', NOW()),
(92, 'Jours fériés', 'JF', 'Jours fériés payés', NOW()),
(93, 'RTT', 'RTT', 'Réduction du temps de travail', NOW());

-- Autres éléments de paie 
INSERT INTO abreviation (id, libelle, abreviation, description, created_at) VALUES
(94, 'Taux horaire', 'TH', 'Taux de rémunération horaire', NOW()),
(95, 'Taux journalier', 'TJ', 'Taux de rémunération journalier', NOW()),
(96, 'Nombre de jours travaillés', 'NJT', 'Nombre de jours effectivement travaillés', NOW()),
(97, 'Nombre de jours ouvrables', 'NJO', 'Nombre de jours ouvrables dans le mois', NOW()),
(98, 'Nombre de jours calendaires', 'NJC', 'Nombre de jours du mois', NOW()),
(99, 'Majoration', 'MAJ', 'Pourcentage de majoration', NOW()),
(100, 'Part patronale', 'PPAT', 'Part prise en charge par l''employeur', NOW()),
(101, 'Part salariale', 'PSAL', 'Part déduite du salarié', NOW()),
(102, 'Forfait jour', 'FJ', 'Rémunération en forfait jours', NOW()),
(103, 'Forfait heure', 'FH', 'Rémunération en forfait heures', NOW()),
(104,'Salaire minimum interprofessionnel garanti', 'SMIG', 'Salaire minimum légal', NOW()),
(105, 'Salaire minimum d''embauche', 'SME', 'Salaire minimum d''embauche', NOW());

-- Formules et calculs spécifiques
INSERT INTO abreviation (id, libelle, abreviation, description, created_at) VALUES
(106, 'Base de calcul CNaPS', 'BC_CNAPS', 'Base utilisée pour calculer les cotisations CNaPS', NOW()),
(107, 'Base de calcul OSTIE', 'BC_OSTIE', 'Base utilisée pour calculer les cotisations OSTIE', NOW()),
(108, 'Base de calcul IRS', 'BC_IRS', 'Base imposable pour l''IRS', NOW()),
(109, 'Plafond de sécurité sociale', 'PSS', 'Plafond mensuel de sécurité sociale', NOW()),
(110, 'Tranche IRS', 'TIRS', 'Tranche d''imposition pour l''IRS', NOW()),
(111, 'Abattement', 'ABATT', 'Abattement fiscal', NOW()),
(112, 'Déduction forfaitaire', 'DF', 'Déduction forfaitaire sur salaire', NOW()),
(113, 'Frais professionnels', 'FPRO', 'Frais professionnels déductibles', NOW());
