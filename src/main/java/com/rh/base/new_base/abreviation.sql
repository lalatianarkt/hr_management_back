insert into abreviation(libelle, abreviation, description, created_at, modified_at) VALUES
('Salaire horaire', 'SH', 'Equivalent de salaire en heures', now(), now());
insert into abreviation(libelle, abreviation, description, created_at, modified_at) VALUES
('nombre', 'nb', 'quantité', now(), now());
insert into abreviation(libelle, abreviation, description, created_at, modified_at) VALUES
('taux', 'taux', 'taux de majoration', now(), now());
insert into abreviation(libelle, abreviation, description, created_at, modified_at) VALUES
('Salaire journalier', 'SJ', 'Equivalent de salaire par jour', now(), now());
insert into abreviation(libelle, abreviation, description, created_at, modified_at) VALUES
('base irsa', 'base_irsa', NULL, now(), now());

INSERT INTO abreviation (libelle, abreviation, description, created_at, modified_at)
VALUES
('HS50', 'HS50', NULL, NOW(), NOW()),
('HS100', 'HS100', NULL, NOW(), NOW()),
('PR_ANC', 'PR_ANC', NULL, NOW(), NOW()),
('PR_RISQ', 'PR_RISQ', NULL, NOW(), NOW()),
('COT_SYN', 'COT_SYN', NULL, NOW(), NOW()),
('ABS_NP', 'ABS_NP', NULL, NOW(), NOW()),
('OSTIE-EMP', 'OSTIE-EMP', NULL, NOW(), NOW()),
('13EME', '13EME', NULL, NOW(), NOW()),
('RAPPEL', 'RAPPEL', NULL, NOW(), NOW()),
('T_BRUT', 'T_BRUT', NULL, NOW(), NOW()),
('SBM', 'SBM', NULL, NOW(), NOW()),
('HS25', 'HS25', NULL, NOW(), NOW()),
('IND_LOG', 'IND_LOG', NULL, NOW(), NOW()),
('IND_TR', 'IND_TR', NULL, NOW(), NOW()),
('PR_PAN', 'PR_PAN', NULL, NOW(), NOW()),
('PR_PERF', 'PR_PERF', NULL, NOW(), NOW()),
('IND_CONG', 'IND_CONG', NULL, NOW(), NOW()),
('CNPS_SAL', 'CNPS_SAL', NULL, NOW(), NOW()),
('IRSA', 'IRSA', NULL, NOW(), NOW()),
('AVANCE', 'AVANCE', NULL, NOW(), NOW()),
('SB', 'SB', NULL, NOW(), NOW()),
('HS130', 'HS130', NULL, NOW(), NOW()),
('HS 150', 'HS150', NULL, NOW(), NOW()),
('HS130 EXO', 'HS130_EXO', NULL, NOW(), NOW()),
('HS150 EXO', 'HS150_EXO', NULL, NOW(), NOW()),
('PA', 'PA', NULL, NOW(), NOW()),
('MN30', 'MN30', NULL, NOW(), NOW()),
('AFAM', 'AFAM', NULL, NOW(), NOW()),
('AMENDE', 'AMENDE', NULL, NOW(), NOW()),
('OSTIE-SAL', 'OSTIE-SAL', NULL, NOW(), NOW()),
('tiana', 'TIANA', NULL, NOW(), NOW()),
('CNPS_PAT', 'CNPS_PAT', NULL, NOW(), NOW()),
('T_COT', 'T_COT', NULL, NOW(), NOW()),
('NET_IMP', 'NET_IMP', NULL, NOW(), NOW()),
('T_RET', 'T_RET', NULL, NOW(), NOW()),
('NET_PAYER', 'NET_PAYER', NULL, NOW(), NOW());


INSERT INTO abreviation (libelle, abreviation, description, created_at, modified_at)
VALUES
('Salaire horaire', 'SH', 'Salaire mensuel de base', NOW(), NOW()),
('Salaire journalier', 'SJ', 'Salaire utilisé comme référence pour les calculs', NOW(), NOW()),
('Salaire brut', 'SBRUT', 'Salaire total avant déductions', NOW(), NOW()),
('Salaire net', 'SNET', 'Salaire après déductions sociales et fiscales', NOW(), NOW()),
('Salaire brut imposable', 'SBRUT_IMP', 'Salaire brut soumis à l''impôt', NOW(), NOW()),
('Salaire net imposable', 'SNET_IMP', 'Salaire net soumis à l''impôt', NOW(), NOW());


('Salaire imposable', 'SIMPO', 'Salaire soumis à l''impôt', NOW()),
('Heures normales', 'HN', 'Heures de travail normales', NOW()),
('Heures supplémentaires', 'HS', 'Heures supplémentaires travaillées', NOW()),
('Heures supplémentaires 25%', 'HS25', 'Heures supplémentaires majorées à 25%', NOW()),
('Heures supplémentaires 50%', 'HS50', 'Heures supplémentaires majorées à 50%', NOW()),
('Heures supplémentaires 100%', 'HS100', 'Heures supplémentaires majorées à 100%', NOW()),
('Heures de nuit', 'HNuit', 'Heures travaillées de nuit', NOW()),
('Heures de dimanche', 'HDim', 'Heures travaillées le dimanche', NOW()),
('Heures de jour férié', 'HFer', 'Heures travaillées les jours fériés', NOW());

-- Cotisations sociales (CNaPS, OSTIE, etc.)
INSERT INTO abreviation (libelle, abreviation, description, created_at) VALUES
('Caisse Nationale de Prévoyance Sociale Salarié', 'CNAPS_S', 'Cotisation salariale CNaPS (1%)', NOW()),
('Caisse Nationale de Prévoyance Sociale Employeur', 'CNAPS_E', 'Cotisation employeur CNaPS (13%)', NOW()),
('Organisme de Santé au Travail et d''Inspection du Travail Salarié', 'OSTIE_S', 'Cotisation salariale OSTIE (1%)', NOW()),
('Organisme de Santé au Travail et d''Inspection du Travail Employeur', 'OSTIE_E', 'Cotisation employeur OSTIE (5%)', NOW()),
('Assurance Vieillesse', 'AV', 'Assurance vieillesse (retraite)', NOW()),
('Assurance Maladie', 'AM', 'Assurance maladie', NOW()),
('Assurance Maternité', 'AMAT', 'Assurance maternité', NOW()),
('Assurance Invalidité Décès', 'AID', 'Assurance invalidité décès', NOW()),
('Prestations Familiales', 'PF', 'Allocations familiales', NOW());

-- Primes et avantages 
INSERT INTO abreviation (libelle, abreviation, description, created_at) VALUES
('Prime d''ancienneté', 'PA', 'Prime basée sur l''ancienneté', NOW()),
('Prime de rendement', 'PR', 'Prime liée à la performance', NOW()),
('Prime de transport', 'PT', 'Indemnité de transport', NOW()),
('Prime de logement', 'PL', 'Indemnité de logement', NOW()),
('Prime de panier', 'PP', 'Indemnité repas/panier', NOW()),
('Prime de risque', 'PRISQ', 'Prime pour travaux dangereux', NOW()),
('Prime de fonction', 'PFCT', 'Prime liée à la fonction occupée', NOW()),
('Prime de technicité', 'PTECH', 'Prime pour compétences techniques', NOW()),
('Prime de treizième mois', 'P13', 'Prime de fin d''année', NOW()),
('Prime de vacances', 'PVAC', 'Prime de congés payés', NOW()),
('Indemnité de congé', 'IC', 'Indemnité pour congés pris', NOW()),
('Indemnité de licenciement', 'IL', 'Indemnité de rupture de contrat', NOW()),
('Indemnité de préavis', 'IP', 'Indemnité de préavis', NOW());

-- Déductions et retenues 
INSERT INTO abreviation (libelle, abreviation, description, created_at) VALUES
('Impôt sur le revenu des salaires', 'IRSA', 'Impôt retenu à la source', NOW()),
('Retenue avance sur salaire', 'RAS', 'Avance sur salaire remboursée', NOW()),
('Retenue prêt personnel', 'RPP', 'Remboursement de prêt personnel', NOW()),
('Retenue coopérative', 'RCOOP', 'Cotisation coopérative', NOW()),
('Retenue mutuelle', 'RMUT', 'Cotisation mutuelle santé', NOW()),
('Retenue syndicale', 'RSYN', 'Cotisation syndicale', NOW()),
('Retenue assurance groupe', 'RAG', 'Assurance groupe déduite', NOW());

-- Congés et absences 
INSERT INTO abreviation (libelle, abreviation, description, created_at) VALUES
('Congés payés', 'CP', 'Jours de congés payés', NOW()),
('Congés sans solde', 'CSS', 'Congés non rémunérés', NOW()),
('Congés maladie', 'CMAL', 'Congés pour maladie', NOW()),
('Congés maternité', 'CMAT', 'Congés de maternité', NOW()),
('Congés paternité', 'CPAT', 'Congés de paternité', NOW()),
('Absences non justifiées', 'ANJ', 'Absences non rémunérées', NOW()),
('Absences justifiées', 'AJ', 'Absences justifiées payées', NOW()),
('Jours fériés', 'JF', 'Jours fériés payés', NOW()),
('RTT', 'RTT', 'Réduction du temps de travail', NOW());

-- Autres éléments de paie 
INSERT INTO abreviation (libelle, abreviation, description, created_at) VALUES
('Taux horaire', 'TH', 'Taux de rémunération horaire', NOW()),
('Taux journalier', 'TJ', 'Taux de rémunération journalier', NOW()),
('Nombre de jours travaillés', 'NJT', 'Nombre de jours effectivement travaillés', NOW()),
('Nombre de jours ouvrables', 'NJO', 'Nombre de jours ouvrables dans le mois', NOW()),
('Nombre de jours calendaires', 'NJC', 'Nombre de jours du mois', NOW()),
('Majoration', 'MAJ', 'Pourcentage de majoration', NOW()),
('Part patronale', 'PPAT', 'Part prise en charge par l''employeur', NOW()),
('Part salariale', 'PSAL', 'Part déduite du salarié', NOW()),
('Forfait jour', 'FJ', 'Rémunération en forfait jours', NOW()),
('Forfait heure', 'FH', 'Rémunération en forfait heures', NOW()),
('Salaire minimum interprofessionnel garanti', 'SMIG', 'Salaire minimum légal', NOW()),
('Salaire minimum d''embauche', 'SME', 'Salaire minimum d''embauche', NOW());

-- Formules et calculs spécifiques
INSERT INTO abreviation (libelle, abreviation, description, created_at) VALUES
('Base de calcul CNaPS', 'BC_CNAPS', 'Base utilisée pour calculer les cotisations CNaPS', NOW()),
('Base de calcul OSTIE', 'BC_OSTIE', 'Base utilisée pour calculer les cotisations OSTIE', NOW()),
('Base de calcul IRS', 'BC_IRS', 'Base imposable pour l''IRS', NOW()),
('Plafond de sécurité sociale', 'PSS', 'Plafond mensuel de sécurité sociale', NOW()),
('Tranche IRS', 'TIRS', 'Tranche d''imposition pour l''IRS', NOW()),
('Abattement', 'ABATT', 'Abattement fiscal', NOW()),
('Déduction forfaitaire', 'DF', 'Déduction forfaitaire sur salaire', NOW()),
('Frais professionnels', 'FPRO', 'Frais professionnels déductibles', NOW());