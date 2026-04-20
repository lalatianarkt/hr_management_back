CREATE VIEW vue_statistiques_employe_demandes AS
SELECT 
    e.id as employe_id,
    e.nom,
    e.prenom,
    e.matricule,
    e.email, 
    -- Statistiques des demandes
    COUNT(dc.id) as nombre_demandes,
    -- Total des jours demandés (somme de nb_jours)
    SUM(dc.nb_jours) as total_jours_demandes,
    -- Moyenne des jours par demande
    ROUND(AVG(dc.nb_jours), 2) as moyenne_jours_demande,
    -- Dernière demande
    MAX(dc.date_demande) as derniere_demande,
    -- Première demande
    MIN(dc.date_demande) as premiere_demande,
    -- Répartition par statut
    COUNT(CASE WHEN dc.statut = 0 THEN 1 END) as demandes_en_attente,
    COUNT(CASE WHEN dc.statut = 1 THEN 1 END) as demandes_approuvees,
    COUNT(CASE WHEN dc.statut = 2 THEN 1 END) as demandes_refusees,
    COUNT(CASE WHEN dc.statut = 3 THEN 1 END) as demandes_annulees,
    -- Pourcentage d'approbation
    CASE 
        WHEN COUNT(CASE WHEN dc.statut IN (1, 2) THEN 1 END) > 0 
        THEN ROUND(
            COUNT(CASE WHEN dc.statut = 1 THEN 1 END) * 100.0 / 
            COUNT(CASE WHEN dc.statut IN (1, 2) THEN 1 END), 
            2
        )
        ELSE 0 
    END as taux_approbation
FROM 
    Employe e
LEFT JOIN 
    Demande_conge dc ON e.id = dc.id_employe
GROUP BY 
    e.id, e.nom, e.prenom, e.matricule, e.email
ORDER BY 
    nombre_demandes DESC; 

CREATE OR REPLACE VIEW v_employe_infos_pro AS
SELECT 
    -- Informations de base de l'employé
    e.id AS employe_id,
    e.nom AS employe_nom,
    e.prenom AS employe_prenom,
    e.date_naissance AS employe_date_naissance,
    e.lieu_naissance AS employe_lieu_naissance,
    e.telephone AS employe_telephone,
    e.email AS employe_email,
    e.statut AS employe_statut,
    e.adresse AS employe_adresse,
    e.created_at AS employe_created_at,
    e.modified_at AS employe_modified_at,
    e.nom_mere AS employe_nom_mere,
    e.nom_pere AS employe_nom_pere,
    e.id_emergency_contact AS employe_id_emergency_contact,
    e.nb_enfants AS employe_nb_enfants,
    e.nom_conjoint AS employe_nom_conjoint,
    e.etat_civil AS employe_etat_civil,
    e.id_sexe AS employe_id_sexe,
    e.id_nationalite AS employe_id_nationalite,
    e.num_cnaps AS employe_num_cnaps,
    e.cin AS employe_cin,
    
    -- Informations professionnelles
    ip.id AS info_pro_id,
    ip.date_embauche AS info_pro_date_embauche,
    ip.date_debauche AS info_pro_date_debauche,
    ip.created_at AS info_pro_created_at,
    ip.modified_at AS info_pro_modified_at,
    ip.date_debut_assignation_poste AS info_pro_date_debut_assignation,
    ip.date_fin_assignation_poste AS info_pro_date_fin_assignation,
    ip.statut AS info_pro_statut,
    ip.id_manager AS info_pro_id_manager,
    ip.id_poste AS info_pro_id_poste,
    ip.id_departement AS info_pro_id_departement,
    ip.id_employe AS info_pro_id_employe,
    ip.salaire_base AS info_pro_salaire_base,
    ip.matricule AS info_pro_matricule,
    ip.motif_depart AS info_pro_motif_depart,
    
    -- Informations liées (via LEFT JOIN pour avoir toutes les infos)
    s.sexe AS sexe_libelle,
    s.code AS sexe_code,
    n.nationalite AS nationalite_libelle,
    ec.nom AS emergency_contact_nom,
    ec.contact AS emergency_contact_telephone,
    p.nom AS poste_nom,
    p.description AS poste_description,
    d.nom AS departement_nom,
    d.description AS departement_description,
    d.statut AS departement_statut,
    m.nom AS manager_nom,
    m.prenom AS manager_prenom,
    
    -- Calculs et métadonnées utiles
    CASE 
        WHEN ip.date_debauche IS NOT NULL THEN 'INACTIF'
        WHEN e.statut = 0 AND ip.statut = 0 THEN 'ACTIF'
        ELSE 'INACTIF'
    END AS statut_global,
    
    -- Ancienneté en années
    EXTRACT(YEAR FROM AGE(CURRENT_DATE, ip.date_embauche)) AS anciennete_annees,
    
    -- Âge de l'employé
    EXTRACT(YEAR FROM AGE(CURRENT_DATE, e.date_naissance)) AS age_employe,
    ip.matricule,
    
    
    
FROM Employe e
-- Jointure avec infos_professionnelles (ONLY ACTIVE)
INNER JOIN infos_professionnelles ip ON e.id = ip.id_employe 
    AND ip.statut = 0
    AND (ip.date_debauche IS NULL OR ip.date_debauche > CURRENT_DATE)
    
-- Jointures avec les tables de référence
LEFT JOIN sexe s ON e.id_sexe = s.id
LEFT JOIN nationalite n ON e.id_nationalite = n.id
LEFT JOIN emergency_contact ec ON e.id_emergency_contact = ec.id
LEFT JOIN type_entree te ON ip.id_type_entree = te.id
LEFT JOIN Poste p ON ip.id_poste = p.id
LEFT JOIN Departement d ON ip.id_departement = d.id
LEFT JOIN Employe m ON ip.id_manager = m.id AND m.statut = 0

-- Filtre: seulement les employés actifs
WHERE e.statut = 0

-- Tri par défaut
ORDER BY e.nom, e.prenom, ip.date_embauche DESC;

SELECT 
    -- Informations de l'employé (depuis la vue existante)
    ep.employe_id AS employe_id,
    ep.employe_nom AS employe_nom,
    ep.employe_prenom AS employe_prenom,
    ep.info_pro_matricule,
    ep.nom_complet AS employe_nom_complet,
    -- ep.affichage_complet AS employe_affichage_complet,
    ep.departement,
    ep.poste,
    ep.employe_statut,
    ep.info_pro_statut,
    ep.date_naissance AS employe_date_naissance,
    ep.telephone AS employe_telephone,
    ep.email AS employe_email,
    ep.date_embauche AS employe_date_embauche,
    ep.salaire_base,
    
    -- Informations du pointage
    p.id AS pointage_id,
    p.date_pointage,
    p.duree_heure_travaillee_minute,
    p.duree_retard_minute,
    p.created_at AS pointage_created_at,
    p.modified_at AS pointage_modified_at,
    p.duree_heure_supplementaire,
    p.is_shift_jour,
    p.commentaire,
    p.is_week_end,
    p.statut_pointage,
    p.is_ferie,
    p.date_heure_arrivee,
    p.date_heure_depart,
    p.id_employe,
    
    -- Calculs supplémentaires
    EXTRACT(YEAR FROM p.date_pointage) AS pointage_annee,
    EXTRACT(MONTH FROM p.date_pointage) AS pointage_mois,
    EXTRACT(DAY FROM p.date_pointage) AS pointage_jour,
    EXTRACT(DOW FROM p.date_pointage) AS pointage_jour_semaine,
    TO_CHAR(p.date_pointage, 'YYYY-MM') AS pointage_periode,
    
    -- Heures formatées
    TO_CHAR(p.date_heure_arrivee, 'HH24:MI') AS heure_arrivee,
    TO_CHAR(p.date_heure_depart, 'HH24:MI') AS heure_depart,
    
    -- Durées formatées
    CASE 
        WHEN p.duree_heure_travaillee_minute IS NOT NULL THEN 
            FLOOR(p.duree_heure_travaillee_minute / 60) || 'h ' || 
            MOD(p.duree_heure_travaillee_minute, 60) || 'min'
        ELSE NULL 
    END AS duree_travail_format,
    
    CASE 
        WHEN p.duree_heure_supplementaire IS NOT NULL THEN 
            FLOOR(p.duree_heure_supplementaire / 60) || 'h ' || 
            MOD(p.duree_heure_supplementaire, 60) || 'min'
        ELSE NULL 
    END AS duree_supp_format,
    
    -- Statut du pointage en texte
    CASE p.statut_pointage
        WHEN 0 THEN 'PRESENT'
        WHEN 1 THEN 'ABSENT'
        WHEN 2 THEN 'CONGE'
        WHEN 3 THEN 'MALADIE'
        WHEN 4 THEN 'FORMATION'
        WHEN 5 THEN 'DEPLACEMENT'
        ELSE 'INCONNU'
    END AS statut_pointage_libelle,
    
    -- Couleur du statut (pour affichage)
    CASE p.statut_pointage
        WHEN 0 THEN 'success'   -- Présent
        WHEN 1 THEN 'danger'    -- Absent
        WHEN 2 THEN 'info'      -- Congé
        WHEN 3 THEN 'warning'   -- Maladie
        WHEN 4 THEN 'primary'   -- Formation
        WHEN 5 THEN 'secondary' -- Déplacement
        ELSE 'dark'
    END AS statut_pointage_couleur,
    
    -- Indicateurs de retard
    CASE 
        WHEN p.duree_retard_minute > 0 THEN TRUE
        ELSE FALSE
    END AS a_retard,
    
    -- Indicateur d'heures supplémentaires
    CASE 
        WHEN p.duree_heure_supplementaire > 0 THEN TRUE
        ELSE FALSE
    END AS a_heures_supp,
    
    -- Calcul de l'heure d'arrivée théorique (exemple: 8h00)
    (DATE_TRUNC('day', p.date_heure_arrivee) + INTERVAL '8 hours') AS heure_arrivee_theorique,
    
    -- Jours entre l'embauche et le pointage
    EXTRACT(DAY FROM (p.date_pointage - ep.date_embauche)) AS jours_depuis_embauche,
    
    -- Âge de l'employé au moment du pointage
    EXTRACT(YEAR FROM AGE(p.date_pointage, ep.date_naissance)) AS age_au_pointage,
    
    -- Ancienneté au moment du pointage
    EXTRACT(YEAR FROM AGE(p.date_pointage, ep.date_embauche)) AS anciennete_au_pointage,
    
    -- Est-ce un jour ouvrable? (lundi-vendredi)
    CASE 
        WHEN EXTRACT(DOW FROM p.date_pointage) BETWEEN 1 AND 5 THEN TRUE
        ELSE FALSE
    END AS est_jour_ouvrable,
    
    -- Est-ce un pointage du jour courant?
    CASE 
        WHEN p.date_pointage = CURRENT_DATE THEN TRUE
        ELSE FALSE
    END AS est_aujourdhui,
    
    -- Est-ce un pointage du mois courant?
    CASE 
        WHEN EXTRACT(YEAR FROM p.date_pointage) = EXTRACT(YEAR FROM CURRENT_DATE)
         AND EXTRACT(MONTH FROM p.date_pointage) = EXTRACT(MONTH FROM CURRENT_DATE) 
        THEN TRUE
        ELSE FALSE
    END AS est_mois_courant
    
FROM v_employe_infos_pro ep
LEFT JOIN Pointage p ON ep.employe_id = p.id_employe
WHERE ep.employe_statut = 0 
  AND ep.info_pro_statut = 0
ORDER BY ep.matricule, p.date_pointage DESC, p.date_heure_arrivee DESC;

-- Vue bulletin de paie ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
CREATE OR REPLACE VIEW vue_info_actif AS
SELECT 
    ip.id_employe,
    ip.matricule,
    ip.date_embauche,
    ip.salaire_base,
    ip.statut,
    ip.id_poste,
    ip.id_departement,
    mp.id_type_paiement,
    tp.libelle AS mode_paiement_libelle,
    mp.code_banque,
    mp.code_guichet,
    mp.nom_banque
FROM infos_professionnelles ip
LEFT JOIN mode_paiement mp ON ip.id_employe = mp.id_employe AND mp.est_actif = true AND mp.est_par_defaut = true
LEFT JOIN type_paiement tp ON mp.id_type_paiement = tp.id
WHERE ip.statut = 0;

CREATE OR REPLACE VIEW employe_info as
SELECT 
    ip.id_employe,
    ip.matricule,
    ip.date_embauche,
    ip.salaire_base,
    ip.statut,
    -- Correction: Extraction d'année et calcul d'ancienneté
    EXTRACT(YEAR FROM AGE(CURRENT_DATE, ip.date_embauche)) AS anciennete_ans,
    EXTRACT(MONTH FROM AGE(CURRENT_DATE, ip.date_embauche)) AS anciennete_mois,
    -- Formatage de l'ancienneté en "X ans Y mois"
    CONCAT(
        EXTRACT(YEAR FROM AGE(CURRENT_DATE, ip.date_embauche)), ' ans ',
        EXTRACT(MONTH FROM AGE(CURRENT_DATE, ip.date_embauche)), ' mois'
    ) AS anciennete_formatee,
    p.nom AS fonction,
    e.nom,
    e.prenom,
    e.date_naissance,
    e.num_cnaps,
    d.nom AS departement,
    e.num_ostie,
    d.id as id_departement,
    -- Gestion du mode de paiement: si compte bancaire, afficher "Virement bancaire"
    CASE 
        WHEN ip.mode_paiement_libelle IS NOT NULL AND ip.mode_paiement_libelle != '' 
        THEN 'Virement bancaire'
    END AS mode_paiement
FROM vue_info_actif ip  
JOIN employe e ON ip.id_employe = e.id
JOIN poste p ON ip.id_poste = p.id 
JOIN departement d ON ip.id_departement = d.id
WHERE ip.statut = 0 
ORDER BY ip.matricule;

CREATE OR REPLACE VIEW vue_details_paie AS
SELECT 
    p.id_employe,
    p.id,
    p.statut_cloture,
    pp.date_debut as date_debut_periode, 
    pp.date_fin as date_fin_periode,
    i.nom_company,
    i.logo,
    pp.statut as statut_periode,
    pp.id_mois,
    pp.annee,
    m.libelle,
    pp.date_cloture,
    m.num
FROM paie p 
join periode_paie pp on p.id_periode = pp.id
join mois m on pp.id_mois = m.id
JOIN information_societe i ON p.id_info_societe = i.id;

CREATE OR REPLACE VIEW vue_salaire_brut as 
select 
    pf.id_paie as paieId,
    sum(pf.montant) as salaire_brut, 
    sum(pf.taux) as total_taux,
    sum(pf.base) as total_base
    from paie_fille pf
    join rubrique_paie rp on 
    pf.id_rubrique = rp.id 
    where rp.id_type = 'GAIN'  
    GROUP by pf.id_paie; 

create or replace view vue_charge_patronale as
select 
    pf.id_paie as paieId,
    sum(pf.montant) as charge, 
    sum(pf.taux) as total_taux,
    sum(pf.base) as total_base
    from paie_fille pf
    join rubrique_paie rp on 
    pf.id_rubrique = rp.id 
    where rp.id_type = 'CHARGE'  
    GROUP by pf.id_paie; 

CREATE OR REPLACE VIEW vue_total_retenue as 
select 
    pf.id_paie as paieId,
    sum(pf.montant) as total_retenue
    from paie_fille pf
    join rubrique_paie rp on 
    pf.id_rubrique = rp.id 
    where rp.id_type = 'RETENUE'  
    GROUP by pf.id_paie; 


CREATE OR REPLACE VIEW vue_total_cotisation as 
select 
    pf.id_paie as paieId,
    sum(pf.montant) as total_cotisations
    from paie_fille pf
    join rubrique_paie rp on 
    pf.id_rubrique = rp.id 
    where rp.id_type = 'RETENUE'
    and 
    rp.id_categorie = 'COTISATION'  
    GROUP by pf.id_paie; 

CREATE OR REPLACE VIEW employe_conge as 
select 
    ms.id_employe,
    ms.nb_conge_total,
    ms.nb_conge_restant,
    ms.nb_conge_pris,
    ms.mois,
    ms.annee,
    ms.statut
    from mouvement_solde ms 
    join employe e  on
    ms.id_employe = e.id;

-- CREATE OR REPLACE VIEW vue_paie_complete AS
-- SELECT 
--     -- Informations employe
--     ei.id_employe,
--     ei.matricule,
--     ei.nom,
--     ei.prenom,
--     ei.nom || ' ' || ei.prenom AS nom_complet,
--     ei.fonction,
--     ei.departement,
--     ei.date_embauche,
--     ei.anciennete_ans,
--     ei.anciennete_mois,
--     ei.anciennete_formatee,
--     ei.salaire_base,
--     ei.date_naissance,
--     ei.num_cnaps,
    
--     -- Informations paie
--     vdp.id AS paie_id,
--     vdp.statut_periode,
--     vdp.date_debut_periode,
--     vdp.date_fin_periode,
--     vdp.nom_company,
--     vdp.logo,
    
--     -- Calculs salariaux
--     COALESCE(vsb.salaire_brut, 0) AS salaire_brut,
--     COALESCE(vsb.total_taux, 0) AS total_taux,
--     COALESCE(vsb.total_base, 0) AS total_base,
--     COALESCE(vtr.total_retenue, 0) AS total_retenue,
--     COALESCE(vtc.total_cotisations, 0) AS total_cotisations,
    
--     -- Calculs derives
--     -- Salaire net = Salaire brut - Total retenue
--     COALESCE(vsb.salaire_brut, 0) - COALESCE(vtr.total_retenue, 0) AS salaire_net,
    
--     -- Taux de prelevement
--     CASE 
--         WHEN COALESCE(vsb.salaire_brut, 0) > 0 
--         THEN ROUND((COALESCE(vtr.total_retenue, 0) / vsb.salaire_brut) * 100, 2)
--         ELSE 0
--     END AS taux_prelevement_percent,
    
--     -- Pourcentage cotisations dans retenues
--     CASE 
--         WHEN COALESCE(vtr.total_retenue, 0) > 0 
--         THEN ROUND((COALESCE(vtc.total_cotisations, 0) / vtr.total_retenue) * 100, 2)
--         ELSE 0
--     END AS taux_cotisation_percent,
    
--     -- Categorie de salaire
--     CASE 
--         WHEN COALESCE(vsb.salaire_brut, 0) < 400000 THEN 'Bas'
--         WHEN COALESCE(vsb.salaire_brut, 0) BETWEEN 400000 AND 800000 THEN 'Moyen'
--         WHEN COALESCE(vsb.salaire_brut, 0) > 800000 THEN 'Eleve'
--         ELSE 'Non defini'
--     END AS categorie_salaire,
    
--     -- Mois et annee de paie
--     EXTRACT(MONTH FROM vdp.date_debut_periode) AS mois_paie,
--     EXTRACT(YEAR FROM vdp.date_debut_periode) AS annee_paie,
    
--     -- Statut paie lisible
--     CASE 
--         WHEN vdp.statut_periode = 0 THEN 'Non cloturee'
--         WHEN vdp.statut_periode = 1 THEN 'Cloturee'
--         WHEN vdp.statut_periode = 2 THEN 'Payee'
--         ELSE 'Statut inconnu'
--     END AS statut_paie_libelle
    
-- FROM employe_info ei
-- JOIN vue_details_paie vdp ON ei.id_employe = vdp.id_employe
-- LEFT JOIN vue_salaire_brut vsb ON vdp.id = vsb.paieId
-- LEFT JOIN vue_total_retenue vtr ON vdp.id = vtr.paieId
-- LEFT JOIN vue_total_cotisation vtc ON vdp.id = vtc.paieId

-- -- Filtrer seulement les employes actifs
-- WHERE ei.statut = 0
-- ORDER BY ei.matricule, vdp.date_debut_periode DESC;

-- 
CREATE OR REPLACE VIEW vue_paie_complete AS
SELECT 
    -- Informations employe
    ei.id_employe,
    ei.matricule,
    ei.nom,
    ei.prenom,
    ei.nom || ' ' || ei.prenom AS nom_complet,
    ei.fonction,
    ei.departement,
    ei.date_embauche,
    ei.anciennete_ans,
    ei.anciennete_mois,
    ei.anciennete_formatee,
    ei.salaire_base,
    ei.date_naissance,
    ei.num_cnaps,
    
    -- Informations paie (depuis vue_details_paie)
    vdp.id AS paie_id,
    vdp.date_cloture,
    vdp.date_debut_periode,
    vdp.date_fin_periode,
    vdp.nom_company,
    vdp.logo,
    vdp.statut_periode,
    
    -- Mois et année (directement depuis vue_details_paie)
    vdp.annee AS annee_paie,
    vdp.id_mois AS mois_paie_id,
    vdp.num as mois_paie,
    vdp.libelle AS mois_paie_nom,
    
    -- Calculs salariaux
    COALESCE(vsb.salaire_brut, 0) AS salaire_brut,
    COALESCE(vsb.total_taux, 0) AS total_taux,
    COALESCE(vsb.total_base, 0) AS total_base,
    COALESCE(vtr.total_retenue, 0) AS total_retenue,
    COALESCE(vtc.total_cotisations, 0) AS total_cotisations,
    coalesce(vc.charge, 0) as total_charges,
    
    -- Calculs derives
    -- Salaire net = Salaire brut - Total retenue
    COALESCE(vsb.salaire_brut, 0) - COALESCE(vtr.total_retenue, 0) AS salaire_net,
    
    -- Taux de prelevement
    CASE 
        WHEN COALESCE(vsb.salaire_brut, 0) > 0 
        THEN ROUND((COALESCE(vtr.total_retenue, 0) / vsb.salaire_brut) * 100, 2)
        ELSE 0
    END AS taux_prelevement_percent,
    
    -- Pourcentage cotisations dans retenues
    CASE 
        WHEN COALESCE(vtr.total_retenue, 0) > 0 
        THEN ROUND((COALESCE(vtc.total_cotisations, 0) / vtr.total_retenue) * 100, 2)
        ELSE 0
    END AS taux_cotisation_percent,
    
    -- Categorie de salaire
    CASE 
        WHEN COALESCE(vsb.salaire_brut, 0) < 400000 THEN 'Bas'
        WHEN COALESCE(vsb.salaire_brut, 0) BETWEEN 400000 AND 800000 THEN 'Moyen'
        WHEN COALESCE(vsb.salaire_brut, 0) > 800000 THEN 'Eleve'
        ELSE 'Non defini'
    END AS categorie_salaire,
    
    -- Statut paie lisible
    CASE 
        WHEN vdp.statut_periode = 0 THEN 'Non cloturee'
        WHEN vdp.statut_periode = 1 THEN 'Cloturee'
        WHEN vdp.statut_periode = 2 THEN 'Payee'
        ELSE 'Statut inconnu'
    END AS statut_paie_libelle,
    ei.mode_paiement
    
FROM employe_info ei
JOIN vue_details_paie vdp ON ei.id_employe = vdp.id_employe
LEFT JOIN vue_salaire_brut vsb ON vdp.id = vsb.paieId
LEFT JOIN vue_total_retenue vtr ON vdp.id = vtr.paieId
LEFT JOIN vue_total_cotisation vtc ON vdp.id = vtc.paieId
left join vue_charge_patronale vc on vdp.id = vc.paieId

-- Filtrer seulement les employes actifs
WHERE ei.statut = 0
ORDER BY ei.matricule, vdp.date_debut_periode DESC;  

CREATE OR REPLACE VIEW vue_paie_fille AS
SELECT 
    p.id_paie,
    r.code,
    r.plafond_mensuel,
    r.plafond_annuel,
    p.montant,
    p.taux,
    p.base,  
    t.libelle AS type_rubrique,
    c.libelle AS categorie_rubrique,
    r.ordre,
    r.libelle as rubrique_nom,
    v.nom_company,
    v.logo 
FROM paie_fille p 
JOIN rubrique_paie r ON p.id_rubrique = r.id
JOIN rubrique_types t ON r.id_type = t.id  
join vue_paie_complete v on p.id_paie = v.paie_id
JOIN categorie_rub c ON r.id_categorie = c.id;

create or replace view vue_mouvementSolde_mensuel as 
select 
    id_employe,
    nb_conge_total,
    nb_conge_pris,
    nb_conge_restant,
    mois,
    annee,
    created_at 
    from mouvement_solde where type_mouvement = 'REPORT';

create or replace view vue_info_manager as
SELECT
    e.nom,
    e.prenom,
    e.nom || ' ' || e.prenom AS nom_complet,
    m.date_debut,
    m.date_fin,
    d.nom AS nom_departement,
    d.description,
    m.id_departement,
    m.id,
    e.id as id_employe
FROM manager m
JOIN employe e ON m.id_employe = e.id
JOIN departement d ON m.id_departement = d.id
WHERE m.statut = 0;

-- create or replace view vue_demande_conge as 
CREATE OR REPLACE VIEW vue_demande_conge AS 
SELECT DISTINCT ON (dc.id)
    dc.date_debut,
    dc.date_fin,
    dc.nb_jours,
    dc.statut,
    CASE dc.statut
        WHEN 0 THEN 'en attente'
        WHEN 1 THEN 'validé par le manager'
        WHEN 2 THEN 'refusé par le manager'
        WHEN 3 THEN 'annulé par le demandeur'
        WHEN 4 THEN 'annulé par le responsable'
        WHEN 5 THEN 'acquis'
        WHEN 6 THEN 'validé par le RH'
        WHEN 7 THEN 'refusé par le RH'
        ELSE 'inconnu'
    END AS decision_manager_libelle,
    dc.date_demande,
    dc.date_validation,
    dc.commentaire_manager,
    dc.commentaire,
    dc.commentaire_annulation,
    emp.nom || ' ' || emp.prenom AS nom_complet_employe,
    emp.nom AS nom_employe,
    emp.prenom AS prenom_employe,
    m.nom AS nom_manager,
    m.prenom AS prenom_manager,
    m.nom_complet AS nom_complet_manager,
    m.nom_departement,
    m.id_departement,
    emp.id as id_employe, 
    vi.matricule,
    m.id as id_manager,
    dc.id
FROM demande_conge dc
JOIN employe emp ON dc.id_employe = emp.id
JOIN v_employe_infos_pro vi ON dc.id_employe = vi.employe_id
JOIN vue_info_manager m ON dc.id_manager = m.id
ORDER BY dc.id, vi.info_pro_date_debut_assignation DESC;


create or replace view poste_niveau as 
select 
    p.id,
    p.nom as nom_poste,
    n.nom as nom_niveau,
    n.rang,
    n.description
    from poste p 
    join niveau_hierarchique n 
    on p.id_niveau = n.id; 

create or replace view vue_employe_manager as
select
    v.employe_nom,
    v.employe_prenom,
    v.employe_nom  || ' ' ||  v.employe_prenom as nom_complet,
    v.info_pro_matricule as matricule,
    p.id as id_poste,
    p.nom_poste,
    p.nom_niveau,
    p.rang,
    v.departement_nom,
    m.nom || ' ' || m.prenom as nom_complet_manager,
    m.id as id_manager,
    v.employe_id,
    v.info_pro_id_departement as id_departement
    from v_employe_infos_pro v 
    join poste_niveau p on v.info_pro_id_poste = p.id
    left join vue_info_manager m 
    on v.info_pro_id_manager = m.id;

create or replace view vue_employe_manager_complet as
select 
    em.matricule as employe_matricule,
    em.employe_nom,
    em.employe_prenom,
    em.nom_complet,
    em.id_poste,
    em.nom_poste,
    em.nom_niveau,
    em.rang, 
    em.departement_nom,
    em.nom_complet_manager,
    max(p.nom) as nom_poste_manager,
    max(n.nom) as nom_niveau_manager,
    max(n.rang) as rang_poste_manager,
    em.employe_id as id_employe
    from vue_employe_manager em left join 
    infos_professionnelles i on em.id_manager = i.id_manager
    join poste p on i.id_poste = p.id
    join niveau_hierarchique n 
    on p.id_niveau = n.id
    where i.statut = 0 group by em.matricule, em.employe_nom, em.employe_prenom,
    em.nom_complet, em.id_poste, em.nom_poste, em.nom_niveau, em.rang, em.departement_nom,
    em.nom_complet_manager, em.employe_id; 

create or replace view vue_pointage_employe as
select 
    e.id as id_employe,
    i.matricule,
    e.nom || ' ' || e.prenom as nom_complet,
    sum(p.duree_heure_travaillee_minute) as total_heure_travaillee,
    sum(p.duree_retard_minute) as total_retard,
    sum(p.duree_heure_supplementaire) as total_heure_sup,
    i.id_departement,
    i.departement as departement_nom
    from pointage p
    join employe e on p.id_employe = e.id
    left join employe_info i on  e.id = i.id_employe
    -- where p.date_debut < '01/12/2025' and p.date_debut > '31/12/2025'
    group by e.id, i.matricule, i.id_departement, i.departement;

-- Version 1 --
create or replace view vue_pointage_employe as
select 
    e.id as id_employe,
    i.matricule,
    e.nom || ' ' || e.prenom as nom_complet,
    sum(p.duree_heure_travaillee_minute) as total_heure_travaillee,
    sum(p.duree_retard_minute) as total_retard,
    sum(p.duree_heure_supplementaire) as total_heure_sup,
    i.id_departement,
    i.departement as departement_nom
from pointage p
join employe e on p.id_employe = e.id
left join employe_info i on e.id = i.id_employe
-- Pour le mois précédent
where p.date_pointage >= date_trunc('month', current_date - interval '3 month')
  and p.date_pointage < date_trunc('month', current_date)
group by e.id, i.matricule, i.id_departement, i.departement;

-- Version 2 --
-- create or replace view vue_pointage_employe_v2 as
-- select 
--     e.id as id_employe,
--     i.matricule,
--     e.nom || ' ' || e.prenom as nom_complet,
--     p.date_pointage,
--     p.duree_heure_travaillee_minute,
--     p.duree_retard_minute,
--     p.duree_heure_supplementaire,
--     i.id_departement,
--     i.departement as departement_nom
-- from pointage p
-- join employe e on p.id_employe = e.id
-- left join employe_info i on e.id = i.id_employe;

CREATE OR REPLACE VIEW vue_pointage_employe_v2 AS
SELECT 
    e.id AS id_employe,
    i.matricule,
    e.nom || ' ' || e.prenom AS nom_complet,
    p.date_pointage,
    p.duree_heure_travaillee_minute,
    p.duree_retard_minute,
    p.duree_heure_supplementaire,
    i.id_departement,
    i.departement AS departement_nom
FROM pointage p
JOIN employe e ON p.id_employe = e.id
LEFT JOIN (
    SELECT DISTINCT ON (id_employe) *
    FROM employe_info
    ORDER BY id_employe, date_embauche DESC
) i ON e.id = i.id_employe;


-- Pour le mois en cours
where p.date_pointage >= date_trunc('month', current_date)
  and p.date_pointage < date_trunc('month', current_date) + interval '1 month'

-- Pour le mois précédent
where p.date_pointage >= date_trunc('month', current_date - interval '2 month')
  and p.date_pointage < date_trunc('month', current_date)

CREATE OR REPLACE VIEW vue_absence_employe AS 
SELECT 
    p.date_pointage,
    e.nom_complet,
    e.employe_matricule,
    e.nom_departement,
    e.nom_poste,
    e.id_employe
FROM vue_employe_manager_complet e
right JOIN pointage p ON e.id_employe = p.id_employe;

-- Les employés absents le 01/12/2025 sont ceux dont l'ID n'est PAS dans cette liste
SELECT 
    '2025-12-01'::DATE AS date_absence,
    e.id_employe AS employe_id,
    e.nom_complet,
    e.employe_matricule,
    e.nom_departement,
    e.nom_poste
FROM vue_employe_manager_complet e
WHERE e.id_employe NOT IN (
    'EMP-20251124-F7D44A',
    'EMP-20260126-73C632', 
    'EMP-20260103-AC40DA',
    'EMP-20260122-56A714',
    'EMP-20251124-62B184',
    'EMP-20260122-C4C545',
    'EMP-20260122-5FB6DF',
    'EMP-20251125-5D3FB5',
    'EMP-20260108-42C610',
    'EMP-20260108-E4BBBF',
    'EMP-20260129-035803',
    'EMP-20260126-985643',
    'EMP-20260126-533610',
    'EMP-20251124-2275AF'
)
ORDER BY e.nom_complet;

-- Vue unique avec période max de 100 jours
CREATE OR REPLACE VIEW vue_absence_employe AS 
SELECT 
    dates.date_pointage AS date_absence,
    e.employe_id AS employe_id,
    e.nom_complet,
    e.matricule,
    e.departement_nom,
    e.nom_poste,
    e.id_departement
FROM vue_employe_manager e
CROSS JOIN (
    -- Toutes les dates où il y a eu au moins un pointage
    SELECT DISTINCT date_pointage
    FROM pointage
    WHERE date_pointage >= CURRENT_DATE - INTERVAL '100 days'
) dates
WHERE NOT EXISTS (
    -- Cet employé n'a pas pointé à cette date
    SELECT 1
    FROM pointage p
    WHERE p.id_employe = e.employe_id
    AND p.date_pointage = dates.date_pointage
)
ORDER BY dates.date_pointage DESC, e.nom_complet;

SELECT 
    dates.date_pointage AS date_absence,
    e.employe_id AS employe_id,
    e.nom_complet,
    e.matricule,
    e.departement_nom,
    e.nom_poste,
    e.id_departement
FROM vue_employe_manager e
CROSS JOIN (
    -- Toutes les dates où il y a eu au moins un pointage
    SELECT DISTINCT date_pointage
    FROM pointage
) dates
WHERE NOT EXISTS (
    -- Cet employé n'a pas pointé à cette date
    SELECT 1
    FROM pointage p
    WHERE p.id_employe = e.employe_id
    AND p.date_pointage = dates.date_pointage
)
ORDER BY dates.date_pointage DESC, e.nom_complet;

-- create or replace view vue_absence_conge as
-- SELECT 
--     dates.date_pointage AS date_absence,
--     e.employe_id AS employe_id,
--     e.nom_complet,
--     e.matricule,
--     e.departement_nom,
--     e.nom_poste,
--     e.id_departement,
--     CASE 
--         WHEN EXISTS (
--             SELECT 1
--             FROM demande_conge dc
--             WHERE dc.id_employe = e.employe_id
--             AND dc.date_debut <= dates.date_pointage
--             AND dc.date_fin >= dates.date_pointage
--             AND dc.decision_manager IN (1, 5) -- Congés approuvés
--         ) THEN 'conge'
--         ELSE 'absence'
--     END AS type_absence
-- FROM vue_employe_manager e
-- CROSS JOIN (
--     -- Toutes les dates où il y a eu au moins un pointage
--     SELECT DISTINCT date_pointage
--     FROM pointage
--     WHERE date_pointage >= CURRENT_DATE - INTERVAL '100 days'
-- ) dates
-- WHERE NOT EXISTS (
--     -- Cet employé n'a pas pointé à cette date
--     SELECT 1
--     FROM pointage p
--     WHERE p.id_employe = e.employe_id
--     AND p.date_pointage = dates.date_pointage
-- )
-- ORDER BY dates.date_pointage DESC, e.nom_complet;

CREATE OR REPLACE VIEW vue_absence_conge AS
SELECT 
    dates.date_pointage AS date_absence,
    e.employe_id,
    e.nom_complet,
    e.matricule,
    e.departement_nom,
    e.nom_poste,
    e.id_departement,
    CASE 
        WHEN EXISTS (
            SELECT 1
            FROM demande_conge dc
            WHERE dc.id_employe = e.employe_id
              AND dc.date_debut <= dates.date_pointage
              AND dc.date_fin >= dates.date_pointage
              AND dc.statut = 5
        ) THEN 'conge'
        ELSE 'absence'
    END AS type_absence
FROM (
    SELECT DISTINCT ON (employe_id) *
    FROM vue_employe_manager
    ORDER BY employe_id
) e
CROSS JOIN (
    SELECT DISTINCT date_pointage
    FROM pointage
) dates
WHERE NOT EXISTS (
    SELECT 1
    FROM pointage p
    WHERE p.id_employe = e.employe_id
      AND p.date_pointage = dates.date_pointage
)
ORDER BY dates.date_pointage DESC, e.nom_complet;


create or replace view reporting_presence as
SELECT 
    vpe.*,
    COALESCE(dc.nombre_conges_termines, 0) AS nombre_conges_termines,
    COALESCE(dc.total_jours_conges_termines, 0) AS total_jours_conges_termines,
    COALESCE(dc.dernier_conge_termine, NULL) AS date_dernier_conge_termine,
    COALESCE(dc.mois_dernier_conge, NULL) AS mois_dernier_conge
FROM vue_pointage_employe vpe
LEFT JOIN (
    SELECT 
        id_employe,
        COUNT(*) AS nombre_conges_termines,
        SUM(nb_jours) AS total_jours_conges_termines,
        MAX(date_fin) AS dernier_conge_termine,
        TO_CHAR(MAX(date_fin), 'YYYY-MM') AS mois_dernier_conge
    FROM demande_conge 
    WHERE decision_manager = 5  -- SEULEMENT les terminés
    AND date_fin < CURRENT_DATE -- Assure que c'est terminé
    GROUP BY id_employe
) dc ON dc.id_employe = vpe.id_employe
ORDER BY vpe.nom_complet;
