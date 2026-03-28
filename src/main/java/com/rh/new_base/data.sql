BEGIN;

-- Clean target tables
DELETE FROM users;
DELETE FROM infos_professionnelles;
DELETE FROM manager;
DELETE FROM employe;
DELETE FROM emergency_contact;

-- Données de test pour la table emergency_contact
-- 120 contacts d'urgence avec 90% sans email

-- 120 contacts d'urgence avec 90% des adresses identiques aux employés
-- 108 contacts (90%) ont la même adresse que l'employé
-- 12 contacts (10%) ont une adresse différente

INSERT INTO emergency_contact (id, contact, email, adresse, created_at, modified_at) VALUES
('EC001', '0341234567', NULL, 'Lot IVK 101 Analakely', NOW(), NULL),
('EC002', '0322345678', NULL, 'Lot IIA 45 Antaninarenina', NOW(), NULL),
('EC003', '0383456789', NULL, 'Lot IIIB 78 Isoraka', NOW(), NULL),
('EC004', '0334567890', NULL, 'Lot VIC 12 Mahamasina', NOW(), NULL),
('EC005', '0375678901', NULL, 'Lot IVM 234 Ampefiloha', NOW(), NULL),
('EC006', '0346789012', NULL, 'Lot IJK 56 Ambohijatovo', NOW(), NULL),
('EC007', '0327890123', NULL, 'Lot VIIH 89 Andravoahangy', NOW(), NULL),
('EC008', '0388901234', NULL, 'Lot IIIG 234 Isotry', NOW(), NULL),
('EC009', '0339012345', NULL, 'Lot IIF 67 Ambohijatovo', NOW(), NULL),
('EC010', '0370123456', NULL, 'Lot IVE 12 Antaninarenina', NOW(), NULL),
('EC011', '0341123456', NULL, 'Lot VIC 34 Ankazomanga', NOW(), NULL),
('EC012', '0322234567', NULL, 'Lot IIIA 89 Andravoahangy', NOW(), NULL),
('EC013', '0383345678', 'rahm@contact.mg', 'Lot IJB 56 Anosy', NOW(), NULL),
('EC014', '0334456789', NULL, 'Lot IVC 78 Ankadifotsy', NOW(), NULL),
('EC015', '0375567890', NULL, 'Lot IIK 23 Antsahabe', NOW(), NULL),
('EC016', '0346678901', NULL, 'Lot VIIL 45 Ambohidratrimo', NOW(), NULL),
('EC017', '0327789012', NULL, 'Lot IIIM 67 Ankadilalana', NOW(), NULL),
('EC018', '0388890123', NULL, 'Lot IVN 89 Ambohibao', NOW(), NULL),
('EC019', '0339901234', NULL, 'Lot IJO 12 Ankadivato', NOW(), NULL),
('EC020', '0370012345', NULL, 'Lot IIP 34 Ankaditapaka', NOW(), NULL),
('EC021', '0341101234', NULL, 'Lot IIIQ 56 Ambohimiandra', NOW(), NULL),
('EC022', '0322212345', NULL, 'Lot IVR 78 Antaninarenina', NOW(), NULL),
('EC023', '0383323456', NULL, 'Lot IIS 90 Ambohibao', NOW(), NULL),
('EC024', '0334434567', NULL, 'Lot IIT 23 Andrainarivo', NOW(), NULL),
('EC025', '0375545678', NULL, 'Lot IVU 45 Ankadifotsy', NOW(), NULL),
('EC026', '0346656789', NULL, 'Lot IJV 67 Faravohitra', NOW(), NULL),
('EC027', '0327767890', NULL, 'Lot IIIW 89 Andohalo', NOW(), NULL),
('EC028', '0388878901', 'velo.contact@mg.mg', 'Lot IIX 12 Andohalo', NOW(), NULL),
('EC029', '0339989012', NULL, 'Lot IVY 34 Ambohidratrimo', NOW(), NULL),
('EC030', '0370090123', NULL, 'Lot IZ 56 Ampefiloha', NOW(), NULL),
('EC031', '0341201234', NULL, 'Lot IIIAA 78 Ambohijatovo', NOW(), NULL),
('EC032', '0322312345', NULL, 'Lot IVAB 90 Antanimena', NOW(), NULL),
('EC033', '0383423456', NULL, 'Lot IAC 12 Analakely', NOW(), NULL),
('EC034', '0334534567', NULL, 'Lot IIAD 34 Ankadifotsy', NOW(), NULL),
('EC035', '0375645678', NULL, 'Lot IIIAE 56 Ankadifotsy', NOW(), NULL),
('EC036', '0346756789', NULL, 'Lot IVAF 78 Ambohibao', NOW(), NULL),
('EC037', '0327867890', NULL, 'Lot IAG 90 Isotry', NOW(), NULL),
('EC038', '0388978901', NULL, 'Lot IIAH 23 Ambohimanarina', NOW(), NULL),
('EC039', '0339089012', NULL, 'Lot IIIAI 45 Andravoahangy', NOW(), NULL),
('EC040', '0370190123', NULL, 'Lot IVAJ 67 Ambohitsoa', NOW(), NULL),
('EC041', '0341301234', NULL, 'Lot IAK 89 Mahamasina', NOW(), NULL),
('EC042', '0322412345', 'hery.contact@mg.com', 'Lot IIAL 12 Faravohitra', NOW(), NULL),
('EC043', '0383523456', NULL, 'Lot IIIAM 34 Isoraka', NOW(), NULL),
('EC044', '0334634567', NULL, 'Lot IVAN 56 Antaninarenina', NOW(), NULL),
('EC045', '0375745678', NULL, 'Lot IAO 78 Ampefiloha', NOW(), NULL),
('EC046', '0346856789', NULL, 'Lot IIAP 90 Ankaditapaka', NOW(), NULL),
('EC047', '0327967890', NULL, 'Lot IIIAQ 23 Ankadivato', NOW(), NULL),
('EC048', '0388078901', NULL, 'Lot IVAR 45 Ambohidratrimo', NOW(), NULL),
('EC049', '0339189012', NULL, 'Lot IAS 67 Ambohijatovo', NOW(), NULL),
('EC050', '0370290123', NULL, 'Lot IIAT 89 Andohalo', NOW(), NULL),
('EC051', '0341401234', NULL, 'Lot IIIAU 12 Ankadilalana', NOW(), NULL),
('EC052', '0322512345', NULL, 'Lot IVAV 34 Ambohibao', NOW(), NULL),
('EC053', '0383623456', NULL, 'Lot IAW 56 Ambohimiandra', NOW(), NULL),
('EC054', '0334734567', NULL, 'Lot IIAX 78 Ankadilalana', NOW(), NULL),
('EC055', '0375845678', NULL, 'Lot IIIAY 90 Antsahabe', NOW(), NULL),
('EC056', '0346956789', NULL, 'Lot IVAZ 23 Antanimena', NOW(), NULL),
('EC057', '0327067890', NULL, 'Lot IBA 45 Ambohidratrimo', NOW(), NULL),
('EC058', '0388178901', NULL, 'Lot IIBB 67 Andranomavo', NOW(), NULL),
('EC059', '0339289012', NULL, 'Lot IIIBC 89 Ankazomanga', NOW(), NULL),
('EC060', '0370390123', NULL, 'Lot IVBD 12 Ambohimanarina', NOW(), NULL),
('EC061', '0341501234', NULL, 'Lot IBE 34 Ankadivato', NOW(), NULL),
('EC062', '0322612345', NULL, 'Lot IIBF 56 Ampefiloha', NOW(), NULL),
('EC063', '0383723456', 'mamy.contact@mg.mg', 'Lot IIIBG 78 Isotry', NOW(), NULL),
('EC064', '0334834567', NULL, 'Lot IVBH 90 Ambohidratrimo', NOW(), NULL),
('EC065', '0375945678', NULL, 'Lot IBI 23 Ankaditapaka', NOW(), NULL),
('EC066', '0346056789', NULL, 'Lot IIBJ 45 Ambohimiandra', NOW(), NULL),
('EC067', '0327167890', NULL, 'Lot IIIBK 67 Antsahabe', NOW(), NULL),
('EC068', '0388278901', NULL, 'Lot IVBL 89 Ambohibao', NOW(), NULL),
('EC069', '0339389012', NULL, 'Lot IBM 12 Ankadilalana', NOW(), NULL),
('EC070', '0370490123', NULL, 'Lot IIBN 34 Mahamasina', NOW(), NULL),
('EC071', '0341601234', NULL, 'Lot IIIBO 56 Ambohijatovo', NOW(), NULL),
('EC072', '0322712345', NULL, 'Lot IVBP 78 Anosy', NOW(), NULL),
('EC073', '0383823456', NULL, 'Lot IBQ 90 Andrainarivo', NOW(), NULL),
('EC074', '0334934567', NULL, 'Lot IIBR 23 Ambohidratrimo', NOW(), NULL),
('EC075', '0375045678', NULL, 'Lot IIIBS 45 Ankadifotsy', NOW(), NULL),
('EC076', '0346156789', NULL, 'Lot IVBT 67 Ambohijatovo', NOW(), NULL),
('EC077', '0327267890', NULL, 'Lot IBU 89 Antanimena', NOW(), NULL),
('EC078', '0388378901', NULL, 'Lot IIBV 12 Analakely', NOW(), NULL),
('EC079', '0339489012', NULL, 'Lot IIIBW 34 Andranomavo', NOW(), NULL),
('EC080', '0370590123', NULL, 'Lot IVBX 56 Ankadifotsy', NOW(), NULL),
('EC081', '0341701234', NULL, 'Lot IBY 78 Ambohibao', NOW(), NULL),
('EC082', '0322812345', 'tiana.contact@gmail.com', 'Lot IIBZ 90 Mahamasina', NOW(), NULL),
('EC083', '0383923456', NULL, 'Lot IIIAA1 23 Ambohimanarina', NOW(), NULL),
('EC084', '0334034567', NULL, 'Lot IVAB1 45 Ankadivato', NOW(), NULL),
('EC085', '0375145678', NULL, 'Lot IAC1 67 Ambohitsoa', NOW(), NULL),
('EC086', '0346256789', NULL, 'Lot IIAD1 89 Ampefiloha', NOW(), NULL),
('EC087', '0327367890', NULL, 'Lot IIIAE1 12 Isotry', NOW(), NULL),
('EC088', '0388478901', NULL, 'Lot IVAF1 34 Isoraka', NOW(), NULL),
('EC089', '0339589012', NULL, 'Lot IAG1 56 Antaninarenina', NOW(), NULL),
('EC090', '0370690123', NULL, 'Lot IIAH1 78 Andohalo', NOW(), NULL),
('EC091', '0341801234', NULL, 'Lot IIIAI1 90 Ankaditapaka', NOW(), NULL),
('EC092', '0322912345', NULL, 'Lot IVAJ1 23 Antsahabe', NOW(), NULL),
('EC093', '0383023456', NULL, 'Lot IAK1 45 Ambohidratrimo', NOW(), NULL),
('EC094', '0334134567', NULL, 'Lot IIAL1 67 Ambohijatovo', NOW(), NULL),
('EC095', '0375245678', NULL, 'Lot IIIAM1 89 Andohalo', NOW(), NULL),
('EC096', '0346356789', NULL, 'Lot IVAN1 12 Ankadilalana', NOW(), NULL),
('EC097', '0327467890', NULL, 'Lot IAO1 34 Ambohibao', NOW(), NULL),
('EC098', '0388578901', NULL, 'Lot IIAP1 56 Ankadivato', NOW(), NULL),
('EC099', '0339689012', NULL, 'Lot IIIAQ1 78 Ankadilalana', NOW(), NULL),
('EC100', '0370790123', NULL, 'Lot IVAR1 90 Amboditsiry', NOW(), NULL),
('EC101', '0341901234', NULL, 'Lot IAS1 23 Antanimena', NOW(), NULL),
('EC102', '0322012345', NULL, 'Lot IIAT1 45 Ambohidratrimo', NOW(), NULL),
('EC103', '0383123456', NULL, 'Lot IIIAU1 67 Andranomavo', NOW(), NULL),
('EC104', '0334234567', NULL, 'Lot IVAV1 89 Ankazomanga', NOW(), NULL),
('EC105', '0375345678', NULL, 'Lot IAW1 12 Ambohimanarina', NOW(), NULL),
('EC106', '0346456789', NULL, 'Lot IIAX1 34 Ankadivato', NOW(), NULL),
('EC107', '0327567890', NULL, 'Lot IIIAY1 56 Ampefiloha', NOW(), NULL),
('EC108', '0388678901', NULL, 'Lot IVAZ1 78 Isotry', NOW(), NULL),
('EC109', '0339789012', NULL, 'Lot IBA1 90 Ambohidratrimo', NOW(), NULL),
('EC110', '0370890123', NULL, 'Lot IIBB1 23 Ankaditapaka', NOW(), NULL),
('EC111', '0341001234', NULL, 'Lot IIIBC1 45 Ambohimiandra', NOW(), NULL),
('EC112', '0322112345', NULL, 'Lot IVBD1 67 Antsahabe', NOW(), NULL),
('EC113', '0383223456', 'naina.contact@mg.org', 'Lot IBE1 89 Ambohibao', NOW(), NULL),
('EC114', '0334334567', NULL, 'Lot IIBF1 12 Ankadilalana', NOW(), NULL),
('EC115', '0375445678', NULL, 'Lot IIIBG1 34 Mahamasina', NOW(), NULL),
('EC116', '0346556789', NULL, 'Lot IVBH1 56 Ambohijatovo', NOW(), NULL),
('EC117', '0327667890', NULL, 'Lot IBI1 78 Anosy', NOW(), NULL),
('EC118', '0388778901', NULL, 'Lot IIBJ1 90 Andrainarivo', NOW(), NULL),
('EC119', '0339889012', NULL, 'Lot IIIBK1 23 Ambohidratrimo', NOW(), NULL),
('EC120', '0370990123', 'faniry.urgence@mg.mg', 'Lot IVBL1 45 Ankadifotsy', NOW(), NULL);

-- 120 employés pour la région Analamanga uniquement
-- Tous malgaches, avec emails Gmail, CIN valides (201 pour hommes, 202 pour femmes)

-- 120 employés pour la région Analamanga uniquement
-- CNaPS: 6-8 chiffres, OSTIE: format XXXXX/OST

INSERT INTO Employe (id, nom, prenom, date_naissance, lieu_naissance, telephone, email, statut, adresse, created_at, modified_at, nom_mere, nom_pere, id_emergency_contact, nb_enfants, nom_conjoint, etat_civil, id_sexe, id_nationalite, num_cnaps, cin, num_ostie, code_postal, id_region) VALUES
('EMP001', 'Rakoto', 'Jean', '1985-03-15', 'Befelatanana', '0321234501', 'jean.rakoto@gmail.com', 0, 'Lot IVK 101 Analakely', NOW(), NULL, 'Rakoto Marie', 'Rakoto Paul', 'EC001', 2, 'Rakoto Solange', 'MARIE', 'M', '1', '12345678', '201123456789', '12345/OST', '101', 'RG01'),
('EMP002', 'Rasoa', 'Marie', '1990-07-22', 'Antanimena', '0341234502', 'marie.rasoa@gmail.com', 0, 'Lot IIA 45 Antaninarenina', NOW(), NULL, 'Rasoa Jeanne', 'Rasoa Pierre', 'EC002', 1, NULL, 'CELIBATAIRE', 'F', '1', '23456789', '202123456788', NULL, '101', 'RG01'),
('EMP003', 'Andrianirina', 'Faly', '1982-11-05', 'Anosy', '0381234503', 'faly.andrianirina@gmail.com', 0, 'Lot IIIB 78 Isoraka', NOW(), NULL, 'Andrianirina Claire', 'Andrianirina Michel', 'EC003', 0, NULL, 'DIVORCE', 'M', '1', '34567890', '201123456787', '23456/OST', '101', 'RG01'),
('EMP004', 'Ravelo', 'Hery', '1978-09-30', 'Mahamasina', '0331234504', 'hery.ravelo@gmail.com', 0, 'Lot VIC 12 Mahamasina', NOW(), NULL, 'Ravelo Louise', 'Ravelo Joseph', 'EC004', 3, 'Ravelo Lala', 'MARIE', 'M', '1', '45678901', '201123456786', '34567/OST', '101', 'RG01'),
('EMP005', 'Randria', 'Voahangy', '1995-02-18', 'Ampefiloha', '0371234505', 'voahangy.randria@gmail.com', 0, 'Lot IVM 234 Ampefiloha', NOW(), NULL, 'Randria Helene', 'Randria Robert', 'EC005', 0, NULL, 'CELIBATAIRE', 'F', '1', '56789012', '202123456785', NULL, '102', 'RG01'),
('EMP006', 'Razafy', 'Tiana', '1987-06-12', 'Ambohijatovo', '0341234506', 'tiana.razafy@gmail.com', 0, 'Lot IJK 56 Ambohijatovo', NOW(), NULL, 'Razafy Eliane', 'Razafy Georges', 'EC006', 1, NULL, 'VEUF', 'F', '1', '67890123', '202123456784', '45678/OST', '101', 'RG01'),
('EMP007', 'Ramanantsoa', 'Nantenaina', '1992-04-25', 'Andravoahangy', '0321234507', 'nantenaina.ramanantsoa@gmail.com', 0, 'Lot VIIH 89 Andravoahangy', NOW(), NULL, 'Ramanantsoa Rose', 'Ramanantsoa Jean', 'EC007', 0, NULL, 'CELIBATAIRE', 'M', '1', '78901234', '201123456783', NULL, '101', 'RG01'),
('EMP008', 'Rakotomalala', 'Mampionona', '1980-08-14', 'Isotry', '0381234508', 'mampionona.rakotomalala@gmail.com', 0, 'Lot IIIG 234 Isotry', NOW(), NULL, 'Rakotomalala Therese', 'Rakotomalala Andre', 'EC008', 2, 'Rakotomalala Christiane', 'MARIE', 'F', '1', '89012345', '202123456782', '56789/OST', '102', 'RG01'),
('EMP009', 'Rakotondrasoa', 'Tojo', '1983-12-01', 'Antananarivo', '0331234509', 'tojo.rakotondrasoa@gmail.com', 0, 'Lot IIF 67 Ambohijatovo', NOW(), NULL, 'Rakotondrasoa Berthe', 'Rakotondrasoa Francis', 'EC009', 0, NULL, 'DIVORCE', 'M', '1', '90123456', '201123456781', NULL, '101', 'RG01'),
('EMP010', 'Randrianarisoa', 'Faniry', '1989-10-19', 'Amboditsiry', '0371234510', 'faniry.randrianarisoa@gmail.com', 0, 'Lot IVE 12 Antaninarenina', NOW(), NULL, 'Randrianarisoa Pauline', 'Randrianarisoa Richard', 'EC010', 1, NULL, 'VEUF', 'F', '1', '01234567', '202123456780', '67890/OST', '101', 'RG01'),
('EMP011', 'Ranaivo', 'Lova', '1993-07-08', 'Ankazomanga', '0341234511', 'lova.ranaivo@gmail.com', 0, 'Lot VIC 34 Ankazomanga', NOW(), NULL, 'Ranaivo Cecile', 'Ranaivo Laurent', 'EC011', 0, NULL, 'CELIBATAIRE', 'M', '1', '123456', '201123456779', NULL, '101', 'RG01'),
('EMP012', 'Rakotoarisoa', 'Haja', '1984-05-21', 'Ambohitsoa', '0321234512', 'haja.rakotoarisoa@gmail.com', 0, 'Lot IIIA 89 Andravoahangy', NOW(), NULL, 'Rakotoarisoa Monique', 'Rakotoarisoa Philippe', 'EC012', 2, 'Rakotoarisoa Solo', 'MARIE', 'M', '1', '234567', '201123456778', '78901/OST', '101', 'RG01'),
('EMP013', 'Rasolofo', 'Miora', '1991-09-14', 'Anosipatrana', '0381234513', 'miora.rasolofo@gmail.com', 0, 'Lot IJB 56 Anosy', NOW(), NULL, 'Rasolofo Denise', 'Rasolofo Patrick', 'EC013', 0, NULL, 'CELIBATAIRE', 'F', '1', '345678', '202123456777', NULL, '101', 'RG01'),
('EMP014', 'Randriamampandry', 'Nirina', '1979-03-27', 'Ankadifotsy', '0331234514', 'nirina.randriamampandry@gmail.com', 0, 'Lot IVC 78 Ankadifotsy', NOW(), NULL, 'Randriamampandry Jacqueline', 'Randriamampandry Henri', 'EC014', 3, 'Randriamampandry Noeline', 'MARIE', 'F', '1', '456789', '202123456776', '89012/OST', '101', 'RG01'),
('EMP015', 'Rakotobe', 'Hasina', '1986-11-09', 'Antsahabe', '0371234515', 'hasina.rakotobe@gmail.com', 0, 'Lot IIK 23 Antsahabe', NOW(), NULL, 'Rakotobe Emilienne', 'Rakotobe Bernard', 'EC015', 1, NULL, 'DIVORCE', 'M', '1', '567890', '201123456775', '90123/OST', '101', 'RG01'),
('EMP016', 'Ramanampisoa', 'Soa', '1994-01-30', 'Ambohidahy', '0341234516', 'soa.ramanampisoa@gmail.com', 0, 'Lot VIIL 45 Ambohidratrimo', NOW(), NULL, 'Ramanampisoa Simone', 'Ramanampisoa Maurice', 'EC016', 0, NULL, 'CELIBATAIRE', 'F', '1', '678901', '202123456774', NULL, '105', 'RG01'),
('EMP017', 'Razafindrakoto', 'Toky', '1981-08-19', 'Ankadilalana', '0321234517', 'toky.razafindrakoto@gmail.com', 0, 'Lot IIIM 67 Ankadilalana', NOW(), NULL, 'Razafindrakoto Alice', 'Razafindrakoto Claude', 'EC017', 2, 'Razafindrakoto Hanta', 'MARIE', 'M', '1', '789012', '201123456773', '01234/OST', '102', 'RG01'),
('EMP018', 'Randrianantenaina', 'Fetra', '1988-12-03', 'Analamahitsy', '0381234518', 'fetra.randrianantenaina@gmail.com', 0, 'Lot IVN 89 Ambohibao', NOW(), NULL, 'Randrianantenaina Lucie', 'Randrianantenaina Albert', 'EC018', 0, NULL, 'CELIBATAIRE', 'M', '1', '890123', '201123456772', NULL, '105', 'RG01'),
('EMP019', 'Rakotondramboa', 'Njaka', '1996-05-17', 'Ankadivato', '0331234519', 'njaka.rakotondramboa@gmail.com', 0, 'Lot IJO 12 Ankadivato', NOW(), NULL, 'Rakotondramboa Marthe', 'Rakotondramboa Serge', 'EC019', 1, NULL, 'VEUF', 'M', '1', '901234', '201123456771', '12345/OST', '101', 'RG01'),
('EMP020', 'Razafiarison', 'Vololona', '1983-10-24', 'Ambohipo', '0371234520', 'vololona.razafiarison@gmail.com', 0, 'Lot IIP 34 Ankaditapaka', NOW(), NULL, 'Razafiarison Christine', 'Razafiarison Daniel', 'EC020', 0, NULL, 'CELIBATAIRE', 'F', '1', '012345', '202123456770', NULL, '102', 'RG01'),
('EMP021', 'Andriamanantena', 'Mahery', '1990-02-11', 'Tsaralalana', '0341234521', 'mahery.andriamanantena@gmail.com', 0, 'Lot IIIQ 56 Ambohimiandra', NOW(), NULL, 'Andriamanantena Suzanne', 'Andriamanantena Alexandre', 'EC021', 0, NULL, 'DIVORCE', 'M', '1', '12345678', '201123456769', NULL, '102', 'RG01'),
('EMP022', 'Rakotovao', 'Hoby', '1985-07-29', 'Antaninarenina', '0321234522', 'hoby.rakotovao@gmail.com', 0, 'Lot IVR 78 Antaninarenina', NOW(), NULL, 'Rakotovoa Francine', 'Rakotovao Vincent', 'EC022', 2, 'Rakotovao Mamy', 'MARIE', 'M', '1', '23456789', '201123456768', '23456/OST', '101', 'RG01'),
('EMP023', 'Rasamimanana', 'Nomena', '1993-04-03', 'Ambatobe', '0381234523', 'nomena.rasamimanana@gmail.com', 0, 'Lot IIS 90 Ambohibao', NOW(), NULL, 'Rasamimanana Irene', 'Rasamimanana Marcel', 'EC023', 0, NULL, 'CELIBATAIRE', 'F', '1', '34567890', '202123456767', NULL, '105', 'RG01'),
('EMP024', 'Rakotondrabe', 'Andry', '1977-12-15', 'Andrainarivo', '0331234524', 'andry.rakotondrabe@gmail.com', 0, 'Lot IIT 23 Andrainarivo', NOW(), NULL, 'Rakotondrabe Bernadette', 'Rakotondrabe Emmanuel', 'EC024', 3, 'Rakotondrabe Olivia', 'MARIE', 'M', '1', '45678901', '201123456766', '34567/OST', '101', 'RG01'),
('EMP025', 'Rakotozafy', 'Sarobidy', '1989-09-08', 'Ankorondrano', '0371234525', 'sarobidy.rakotozafy@gmail.com', 0, 'Lot IVU 45 Ankadifotsy', NOW(), NULL, 'Rakotozafy Marguerite', 'Rakotozafy Thierry', 'EC025', 1, NULL, 'VEUVE', 'F', '1', '56789012', '202123456765', '45678/OST', '101', 'RG01'),
('EMP026', 'Raharison', 'Ny Aina', '1982-06-20', 'Faravohitra', '0341234526', 'nyaina.raharison@gmail.com', 0, 'Lot IJV 67 Faravohitra', NOW(), NULL, 'Raharison Paulette', 'Raharison Etienne', 'EC026', 0, NULL, 'CELIBATAIRE', 'M', '1', '67890123', '201123456764', NULL, '101', 'RG01'),
('EMP027', 'Ranaivoson', 'Mendrika', '1995-11-02', 'Ampandrana', '0321234527', 'mendrika.ranaivoson@gmail.com', 0, 'Lot IIIW 89 Andohalo', NOW(), NULL, 'Ranaivoson Yvette', 'Ranaivoson Gerard', 'EC027', 0, NULL, 'CELIBATAIRE', 'F', '1', '78901234', '202123456763', NULL, '101', 'RG01'),
('EMP028', 'Randrianarivelo', 'Tahiana', '1986-03-09', 'Andohalo', '0381234528', 'tahiana.randrianarivelo@gmail.com', 0, 'Lot IIX 12 Andohalo', NOW(), NULL, 'Randrianarivelo Denise', 'Randrianarivelo Philippe', 'EC028', 2, 'Randrianarivelo Nirina', 'MARIE', 'F', '1', '89012345', '202123456762', '56789/OST', '101', 'RG01'),
('EMP029', 'Razafindrainibe', 'Mamy', '1975-08-28', 'Ankadinandriana', '0331234529', 'mamy.razafindrainibe@gmail.com', 0, 'Lot IVY 34 Ambohidratrimo', NOW(), NULL, 'Razafindrainibe Jeannette', 'Razafindrainibe Francois', 'EC029', 3, 'Razafindrainibe Lydie', 'MARIE', 'M', '1', '90123456', '201123456761', '67890/OST', '105', 'RG01'),
('EMP030', 'Randriamanantena', 'Soanirina', '1991-01-14', 'Ambanidia', '0371234530', 'soanirina.randriamanantena@gmail.com', 0, 'Lot IZ 56 Ampefiloha', NOW(), NULL, 'Randriamanantena Clairette', 'Randriamanantena Michel', 'EC030', 0, NULL, 'CELIBATAIRE', 'F', '1', '01234567', '202123456760', NULL, '102', 'RG01'),
('EMP031', 'Rakotondrazaka', 'Dina', '1984-10-07', 'Ambodivona', '0341234531', 'dina.rakotondrazaka@gmail.com', 0, 'Lot IIIAA 78 Ambohijatovo', NOW(), NULL, 'Rakotondrazaka Sylvie', 'Rakotondrazaka Marc', 'EC031', 1, NULL, 'DIVORCE', 'F', '1', '123456', '202123456759', '78901/OST', '101', 'RG01'),
('EMP032', 'Andriantsilavo', 'Tovo', '1994-06-25', 'Antanimora', '0321234532', 'tovo.andriantsilavo@gmail.com', 0, 'Lot IVAB 90 Antanimena', NOW(), NULL, 'Andriantsilavo Beatrice', 'Andriantsilavo Julien', 'EC032', 0, NULL, 'CELIBATAIRE', 'M', '1', '234567', '201123456758', NULL, '101', 'RG01'),
('EMP033', 'Ramanantenasoa', 'Kanto', '1980-05-12', 'Analakely', '0381234533', 'kanto.ramanantenasoa@gmail.com', 0, 'Lot IAC 12 Analakely', NOW(), NULL, 'Ramanantenasoa Odette', 'Ramanantenasoa Jean-Pierre', 'EC033', 2, 'Ramanantenasoa Sandra', 'MARIE', 'M', '1', '345678', '201123456757', '89012/OST', '101', 'RG01'),
('EMP034', 'Razafindramboa', 'Fidy', '1987-12-19', 'Andohanilalana', '0331234534', 'fidy.razafindramboa@gmail.com', 0, 'Lot IIAD 34 Ankadifotsy', NOW(), NULL, 'Razafindramboa Ginette', 'Razafindramboa Robert', 'EC034', 0, NULL, 'CELIBATAIRE', 'M', '1', '456789', '201123456756', NULL, '101', 'RG01'),
('EMP035', 'Rakotondramanana', 'Tsiky', '1996-08-03', 'Ankadifotsy', '0371234535', 'tsiky.rakotondramanana@gmail.com', 0, 'Lot IIIAE 56 Ankadifotsy', NOW(), NULL, 'Rakotondramanana Colette', 'Rakotondramanana Andre', 'EC035', 1, NULL, 'VEUF', 'F', '1', '567890', '202123456755', '90123/OST', '101', 'RG01'),
('EMP036', 'Randrianirina', 'Rinah', '1992-02-22', 'Ambatobe', '0341234536', 'rinah.randrianirina@gmail.com', 0, 'Lot IVAF 78 Ambohibao', NOW(), NULL, 'Randrianirina Felicite', 'Randrianirina Henri', 'EC036', 0, NULL, 'CELIBATAIRE', 'F', '1', '678901', '202123456754', NULL, '105', 'RG01'),
('EMP037', 'Razafindramisa', 'Nivo', '1983-09-16', 'Ampandrana', '0321234537', 'nivo.razafindramisa@gmail.com', 0, 'Lot IAG 90 Isotry', NOW(), NULL, 'Razafindramisa Germaine', 'Razafindramisa Georges', 'EC037', 2, 'Razafindramisa Tantely', 'MARIE', 'M', '1', '789012', '201123456753', '01234/OST', '102', 'RG01'),
('EMP038', 'Rakotondrajao', 'Tanjona', '1989-04-11', 'Ambohimanambola', '0381234538', 'tanjona.rakotondrajao@gmail.com', 0, 'Lot IIAH 23 Ambohimanarina', NOW(), NULL, 'Rakotondrajao Juliette', 'Rakotondrajao Jean', 'EC038', 0, NULL, 'CELIBATAIRE', 'M', '1', '890123', '201123456752', NULL, '101', 'RG01'),
('EMP039', 'Andriamihaja', 'Riantsoa', '1985-01-28', 'Andranomena', '0331234539', 'riantsoa.andriamihaja@gmail.com', 0, 'Lot IIIAI 45 Andravoahangy', NOW(), NULL, 'Andriamihaja Christiane', 'Andriamihaja Raymond', 'EC039', 1, NULL, 'DIVORCE', 'F', '1', '901234', '202123456751', '12345/OST', '101', 'RG01'),
('EMP040', 'Rakotovelo', 'Fenitra', '1993-07-30', 'Ambohitsoa', '0371234540', 'fenitra.rakotovelo@gmail.com', 0, 'Lot IVAJ 67 Ambohitsoa', NOW(), NULL, 'Rakotovelo Micheline', 'Rakotovelo Auguste', 'EC040', 0, NULL, 'CELIBATAIRE', 'M', '1', '012345', '201123456750', NULL, '102', 'RG01'),
('EMP041', 'Rasendra', 'Heriniaina', '1978-06-06', 'Ankorondrano', '0341234541', 'heriniaina.rasendra@gmail.com', 0, 'Lot IAK 89 Mahamasina', NOW(), NULL, 'Rasendra Therese', 'Rasendra Gerard', 'EC041', 3, 'Rasendra Solange', 'MARIE', 'M', '1', '12345678', '201123456749', '23456/OST', '101', 'RG01'),
('EMP042', 'Ravelomanantsoa', 'Mino', '1990-10-17', 'Faravohitra', '0321234542', 'mino.ravelomanantsoa@gmail.com', 0, 'Lot IIAL 12 Faravohitra', NOW(), NULL, 'Ravelomanantsoa Helene', 'Ravelomanantsoa Laurent', 'EC042', 0, NULL, 'CELIBATAIRE', 'F', '1', '23456789', '202123456748', NULL, '101', 'RG01'),
('EMP043', 'Randrianasolo', 'Nandrianina', '1981-12-23', 'Tsaralalana', '0381234543', 'nandrianina.randrianasolo@gmail.com', 0, 'Lot IIIAM 34 Isoraka', NOW(), NULL, 'Randrianasolo Monique', 'Randrianasolo Albert', 'EC043', 2, 'Randrianasolo Tiana', 'MARIE', 'M', '1', '34567890', '201123456747', '34567/OST', '101', 'RG01'),
('EMP044', 'Razakamanana', 'Mirana', '1995-03-04', 'Antaninarenina', '0331234544', 'mirana.razakamanana@gmail.com', 0, 'Lot IVAN 56 Antaninarenina', NOW(), NULL, 'Razakamanana Pauline', 'Razakamanana Marcel', 'EC044', 0, NULL, 'CELIBATAIRE', 'F', '1', '45678901', '202123456746', NULL, '101', 'RG01'),
('EMP045', 'Rakotoniaina', 'Iarivo', '1986-08-29', 'Ampasampito', '0371234545', 'iarivo.rakotoniaina@gmail.com', 0, 'Lot IAO 78 Ampefiloha', NOW(), NULL, 'Rakotoniaina Bernadette', 'Rakotoniaina Serge', 'EC045', 1, NULL, 'VEUF', 'M', '1', '56789012', '201123456745', '45678/OST', '102', 'RG01'),
('EMP046', 'Andriamanjato', 'Mamina', '1991-11-12', 'Andrefanambohitra', '0341234546', 'mamina.andriamanjato@gmail.com', 0, 'Lot IIAP 90 Ankaditapaka', NOW(), NULL, 'Andriamanjato Claire', 'Andriamanjato Francois', 'EC046', 0, NULL, 'CELIBATAIRE', 'F', '1', '67890123', '202123456744', NULL, '102', 'RG01'),
('EMP047', 'Razafindralambo', 'Rija', '1984-05-18', 'Ambohitsararavina', '0321234547', 'rija.razafindralambo@gmail.com', 0, 'Lot IIIAQ 23 Ankadivato', NOW(), NULL, 'Razafindralambo Suzanne', 'Razafindralambo Henri', 'EC047', 2, 'Razafindralambo Lanto', 'MARIE', 'M', '1', '78901234', '201123456743', '56789/OST', '101', 'RG01'),
('EMP048', 'Rakotovololona', 'Hary', '1988-02-09', 'Ambohipo', '0381234548', 'hary.rakotovololona@gmail.com', 0, 'Lot IVAR 45 Ambohidratrimo', NOW(), NULL, 'Rakotovololona Lucie', 'Rakotovololona Daniel', 'EC048', 0, NULL, 'CELIBATAIRE', 'M', '1', '89012345', '201123456742', NULL, '105', 'RG01'),
('EMP049', 'Randriamboavonjy', 'Tsiry', '1994-09-26', 'Ambohijatovo', '0331234549', 'tsiry.randriamboavonjy@gmail.com', 0, 'Lot IAS 67 Ambohijatovo', NOW(), NULL, 'Randriamboavonjy Emilienne', 'Randriamboavonjy Joseph', 'EC049', 1, NULL, 'DIVORCE', 'F', '1', '90123456', '202123456741', '67890/OST', '101', 'RG01'),
('EMP050', 'Razanadravao', 'Aina', '1979-07-01', 'Andohalo', '0371234550', 'aina.razanadravao@gmail.com', 0, 'Lot IIAT 89 Andohalo', NOW(), NULL, 'Razanadravao Jacqueline', 'Razanadravao Philippe', 'EC050', 3, 'Razanadravao Helene', 'MARIE', 'F', '1', '01234567', '202123456740', '78901/OST', '101', 'RG01'),
('EMP051', 'Razafimandimby', 'Tefy', '1982-03-15', 'Analamahitsy', '0341234551', 'tefy.razafimandimby@gmail.com', 0, 'Lot IIIAU 12 Ankadilalana', NOW(), NULL, 'Razafimandimby Marie', 'Razafimandimby Pierre', 'EC051', 0, NULL, 'CELIBATAIRE', 'M', '1', '123456', '201123456739', NULL, '102', 'RG01'),
('EMP052', 'Ramanandraibe', 'Sitraka', '1987-10-09', 'Ankadinandriana', '0321234552', 'sitraka.ramanandraibe@gmail.com', 0, 'Lot IVAV 34 Ambohibao', NOW(), NULL, 'Ramanandraibe Berthe', 'Ramanandraibe Maurice', 'EC052', 0, NULL, 'CELIBATAIRE', 'M', '1', '234567', '201123456738', NULL, '105', 'RG01'),
('EMP053', 'Rakotomena', 'Ny Hasina', '1992-01-24', 'Ampasika', '0381234553', 'nyhasina.rakotomena@gmail.com', 0, 'Lot IAW 56 Ambohimiandra', NOW(), NULL, 'Rakotomena Yolande', 'Rakotomena Jean', 'EC053', 2, 'Rakotomena Fara', 'MARIE', 'F', '1', '345678', '202123456737', '89012/OST', '102', 'RG01'),
('EMP054', 'Randriatahina', 'Fano', '1996-06-19', 'Ankadilalana', '0331234554', 'fano.randriatahina@gmail.com', 0, 'Lot IIAX 78 Ankadilalana', NOW(), NULL, 'Randriatahina Cecile', 'Randriatahina Robert', 'EC054', 0, NULL, 'CELIBATAIRE', 'M', '1', '456789', '201123456736', NULL, '102', 'RG01'),
('EMP055', 'Ravelonarivo', 'Faneva', '1985-04-02', 'Amboditsiry', '0371234555', 'faneva.ravelonarivo@gmail.com', 0, 'Lot IIIAY 90 Antsahabe', NOW(), NULL, 'Ravelonarivo Simone', 'Ravelonarivo Andre', 'EC055', 1, NULL, 'VEUF', 'M', '1', '567890', '201123456735', '90123/OST', '101', 'RG01'),
('EMP056', 'Andriatiana', 'Anjara', '1993-12-07', 'Antanimena', '0341234556', 'anjara.andriatiana@gmail.com', 0, 'Lot IVAZ 23 Antanimena', NOW(), NULL, 'Andriatiana Odette', 'Andriatiana Georges', 'EC056', 0, NULL, 'CELIBATAIRE', 'F', '1', '678901', '202123456734', NULL, '101', 'RG01'),
('EMP057', 'Rakotondramanan', 'Tsinjo', '1980-09-13', 'Ambohidahy', '0321234557', 'tsinjo.rakotondramanan@gmail.com', 0, 'Lot IBA 45 Ambohidratrimo', NOW(), NULL, 'Rakotondramanan Christine', 'Rakotondramanan Julien', 'EC057', 3, 'Rakotondramanan Mamy', 'MARIE', 'F', '1', '789012', '202123456733', '01234/OST', '105', 'RG01'),
('EMP058', 'Razafindrasoa', 'Tina', '1990-11-20', 'Andranomavo', '0381234558', 'tina.razafindrasoa@gmail.com', 0, 'Lot IIBB 67 Andranomavo', NOW(), NULL, 'Razafindrasoa Francoise', 'Razafindrasoa Marc', 'EC058', 0, NULL, 'CELIBATAIRE', 'F', '1', '890123', '202123456732', NULL, '101', 'RG01'),
('EMP059', 'Randrianjafy', 'Lalaina', '1984-08-05', 'Ankazomanga', '0331234559', 'lalaina.randrianjafy@gmail.com', 0, 'Lot IIIBC 89 Ankazomanga', NOW(), NULL, 'Randrianjafy Therese', 'Randrianjafy Henri', 'EC059', 2, 'Randrianjafy Hanta', 'MARIE', 'M', '1', '901234', '201123456731', '12345/OST', '101', 'RG01'),
('EMP060', 'Rakotovao', 'Tahiry', '1977-02-18', 'Ambohimanarina', '0371234560', 'tahiry.rakotovao@gmail.com', 0, 'Lot IVBD 12 Ambohimanarina', NOW(), NULL, 'Rakotovao Marie', 'Rakotovao Raymond', 'EC060', 3, 'Rakotovao Nirina', 'MARIE', 'M', '1', '012345', '201123456730', '23456/OST', '101', 'RG01'),
('EMP061', 'Ravelojaona', 'Jery', '1989-03-22', 'Ankadivato', '0341234561', 'jery.ravelojaona@gmail.com', 0, 'Lot IBE 34 Ankadivato', NOW(), NULL, 'Ravelojaona Jeannette', 'Ravelojaona Jean', 'EC061', 0, NULL, 'CELIBATAIRE', 'M', '1', '12345678', '201123456729', NULL, '101', 'RG01'),
('EMP062', 'Rakotondrasoa', 'Mampionona', '1986-07-07', 'Ampefiloha', '0321234562', 'mampionona.rakotondrasoa@gmail.com', 0, 'Lot IIBF 56 Ampefiloha', NOW(), NULL, 'Rakotondrasoa Paulette', 'Rakotondrasoa Claude', 'EC062', 1, NULL, 'DIVORCE', 'F', '1', '23456789', '202123456728', '34567/OST', '102', 'RG01'),
('EMP063', 'Razafimanantsoa', 'Hery', '1991-09-15', 'Isotry', '0381234563', 'hery.razafimanantsoa@gmail.com', 0, 'Lot IIIBG 78 Isotry', NOW(), NULL, 'Razafimanantsoa Lucie', 'Razafimanantsoa Philippe', 'EC063', 0, NULL, 'CELIBATAIRE', 'M', '1', '34567890', '201123456727', NULL, '102', 'RG01'),
('EMP064', 'Randriamahazo', 'Miaro', '1995-05-28', 'Ambohidratrimo', '0331234564', 'miaro.randriamahazo@gmail.com', 0, 'Lot IVBH 90 Ambohidratrimo', NOW(), NULL, 'Randriamahazo Emilienne', 'Randriamahazo Gerard', 'EC064', 0, NULL, 'CELIBATAIRE', 'M', '1', '45678901', '201123456726', NULL, '105', 'RG01'),
('EMP065', 'Rakotomalala', 'Nandrasana', '1983-12-11', 'Ankaditapaka', '0371234565', 'nandrasana.rakotomalala@gmail.com', 0, 'Lot IBI 23 Ankaditapaka', NOW(), NULL, 'Rakotomalala Bernadette', 'Rakotomalala Francois', 'EC065', 2, 'Rakotomalala Solo', 'MARIE', 'F', '1', '56789012', '202123456725', '45678/OST', '102', 'RG01'),
('EMP066', 'Rakotozandry', 'Tiavina', '1988-04-14', 'Ambohimiandra', '0341234566', 'tiavina.rakotozandry@gmail.com', 0, 'Lot IIBJ 45 Ambohimiandra', NOW(), NULL, 'Rakotozandry Monique', 'Rakotozandry Andre', 'EC066', 1, NULL, 'VEUF', 'F', '1', '67890123', '202123456724', '56789/OST', '102', 'RG01'),
('EMP067', 'Randrianantenaina', 'Manoa', '1994-10-01', 'Antsahabe', '0321234567', 'manoa.randrianantenaina@gmail.com', 0, 'Lot IIIBK 67 Antsahabe', NOW(), NULL, 'Randrianantenaina Simone', 'Randrianantenaina Albert', 'EC067', 0, NULL, 'CELIBATAIRE', 'M', '1', '78901234', '201123456723', NULL, '101', 'RG01'),
('EMP068', 'Razafindrazaka', 'Fanilo', '1981-06-26', 'Ambohibao', '0381234568', 'fanilo.razafindrazaka@gmail.com', 0, 'Lot IVBL 89 Ambohibao', NOW(), NULL, 'Razafindrazaka Helene', 'Razafindrazaka Marcel', 'EC068', 2, 'Razafindrazaka Hoby', 'MARIE', 'M', '1', '89012345', '201123456722', '67890/OST', '105', 'RG01'),
('EMP069', 'Rakotondratsimba', 'Ny Avo', '1992-08-08', 'Ankadilalana', '0331234569', 'nyavo.rakotondratsimba@gmail.com', 0, 'Lot IBM 12 Ankadilalana', NOW(), NULL, 'Rakotondratsimba Jacqueline', 'Rakotondratsimba Julien', 'EC069', 0, NULL, 'CELIBATAIRE', 'M', '1', '90123456', '201123456721', NULL, '102', 'RG01'),
('EMP070', 'Ranaivomanana', 'Miora', '1987-11-29', 'Mahamasina', '0371234570', 'miora.ranaivomanana@gmail.com', 0, 'Lot IIBN 34 Mahamasina', NOW(), NULL, 'Ranaivomanana Christine', 'Ranaivomanana Georges', 'EC070', 1, NULL, 'DIVORCE', 'F', '1', '01234567', '202123456720', '78901/OST', '101', 'RG01'),
('EMP071', 'Andriamananony', 'Tiana', '1984-02-19', 'Ambohijatovo', '0341234571', 'tiana.andriamananony@gmail.com', 0, 'Lot IIIBO 56 Ambohijatovo', NOW(), NULL, 'Andriamananony Yvette', 'Andriamananony Richard', 'EC071', 0, NULL, 'CELIBATAIRE', 'F', '1', '123456', '202123456719', NULL, '101', 'RG01'),
('EMP072', 'Rakotondramaro', 'Ando', '1993-07-05', 'Anosy', '0321234572', 'ando.rakotondramaro@gmail.com', 0, 'Lot IVBP 78 Anosy', NOW(), NULL, 'Rakotondramaro Denise', 'Rakotondramaro Daniel', 'EC072', 2, 'Rakotondramaro Lova', 'MARIE', 'M', '1', '234567', '201123456718', '89012/OST', '101', 'RG01'),
('EMP073', 'Razafitsalama', 'Nantenaina', '1990-12-13', 'Andrainarivo', '0381234573', 'nantenaina.razafitsalama@gmail.com', 0, 'Lot IBQ 90 Andrainarivo', NOW(), NULL, 'Razafitsalama Pauline', 'Razafitsalama Serge', 'EC073', 0, NULL, 'CELIBATAIRE', 'M', '1', '345678', '201123456717', NULL, '101', 'RG01'),
('EMP074', 'Rakotobe', 'Liantsoa', '1985-09-03', 'Ambohidahy', '0331234574', 'liantsoa.rakotobe@gmail.com', 0, 'Lot IIBR 23 Ambohidratrimo', NOW(), NULL, 'Rakotobe Marthe', 'Rakotobe Henri', 'EC074', 1, NULL, 'VEUF', 'F', '1', '456789', '202123456716', '90123/OST', '105', 'RG01'),
('EMP075', 'Randrianarison', 'Finaritra', '1996-01-21', 'Ambanidia', '0371234575', 'finaritra.randrianarison@gmail.com', 0, 'Lot IIIBS 45 Ankadifotsy', NOW(), NULL, 'Randrianarison Francine', 'Randrianarison Albert', 'EC075', 0, NULL, 'CELIBATAIRE', 'M', '1', '567890', '201123456715', NULL, '101', 'RG01'),
('EMP076', 'Razafindrafara', 'Tahina', '1982-04-27', 'Ambodivona', '0341234576', 'tahina.razafindrafara@gmail.com', 0, 'Lot IVBT 67 Ambohijatovo', NOW(), NULL, 'Razafindrafara Cecile', 'Razafindrafara Pierre', 'EC076', 2, 'Razafindrafara Mahefa', 'MARIE', 'M', '1', '678901', '201123456714', '01234/OST', '101', 'RG01'),
('EMP077', 'Rakotondravao', 'Harinirina', '1989-08-16', 'Antanimora', '0321234577', 'harinirina.rakotondravao@gmail.com', 0, 'Lot IBU 89 Antanimena', NOW(), NULL, 'Rakotondravao Claire', 'Rakotondravao Laurent', 'EC077', 0, NULL, 'CELIBATAIRE', 'F', '1', '789012', '202123456713', NULL, '101', 'RG01'),
('EMP078', 'Andrianarivelo', 'Nirina', '1976-10-31', 'Analakely', '0381234578', 'nirina.andrianarivelo@gmail.com', 0, 'Lot IIBV 12 Analakely', NOW(), NULL, 'Andrianarivelo Jeanne', 'Andrianarivelo Robert', 'EC078', 3, 'Andrianarivelo Soa', 'MARIE', 'F', '1', '890123', '202123456712', '12345/OST', '101', 'RG01'),
('EMP079', 'Raveloson', 'Tokiniaina', '1983-05-23', 'Andohanilalana', '0331234579', 'tokiniaina.raveloson@gmail.com', 0, 'Lot IIIBW 34 Andranomavo', NOW(), NULL, 'Raveloson Marie', 'Raveloson Andre', 'EC079', 1, NULL, 'DIVORCE', 'M', '1', '901234', '201123456711', '23456/OST', '101', 'RG01'),
('EMP080', 'Rakotonirainy', 'Fenomanana', '1995-02-14', 'Ankadifotsy', '0371234580', 'fenomanana.rakotonirainy@gmail.com', 0, 'Lot IVBX 56 Ankadifotsy', NOW(), NULL, 'Rakotonirainy Therese', 'Rakotonirainy Maurice', 'EC080', 0, NULL, 'CELIBATAIRE', 'M', '1', '012345', '201123456710', NULL, '101', 'RG01'),
('EMP081', 'Randrianaivo', 'Mendrika', '1986-12-06', 'Ambatobe', '0341234581', 'mendrika.randrianaivo@gmail.com', 0, 'Lot IBY 78 Ambohibao', NOW(), NULL, 'Randrianaivo Odette', 'Randrianaivo Jean', 'EC081', 0, NULL, 'CELIBATAIRE', 'F', '1', '12345678', '202123456709', NULL, '105', 'RG01'),
('EMP082', 'Razafinjanahary', 'Hery', '1991-03-11', 'Ampandrana', '0321234582', 'hery.razafinjanahary@gmail.com', 0, 'Lot IIBZ 90 Mahamasina', NOW(), NULL, 'Razafinjanahary Berthe', 'Razafinjanahary Georges', 'EC082', 2, 'Razafinjanahary Ny Aina', 'MARIE', 'M', '1', '23456789', '201123456708', '34567/OST', '101', 'RG01'),
('EMP083', 'Rakotovahiny', 'Tafita', '1994-04-18', 'Ambohimanambola', '0381234583', 'tafita.rakotovahiny@gmail.com', 0, 'Lot IIIAA1 23 Ambohimanarina', NOW(), NULL, 'Rakotovahiny Suzanne', 'Rakotovahiny Marcel', 'EC083', 0, NULL, 'CELIBATAIRE', 'M', '1', '34567890', '201123456707', NULL, '101', 'RG01'),
('EMP084', 'Ratsimbazafy', 'Mamy', '1980-07-24', 'Andranomena', '0331234584', 'mamy.ratsimbazafy@gmail.com', 0, 'Lot IVAB1 45 Ankadivato', NOW(), NULL, 'Ratsimbazafy Christiane', 'Ratsimbazafy Philippe', 'EC084', 3, 'Ratsimbazafy Tiana', 'MARIE', 'F', '1', '45678901', '202123456706', '45678/OST', '101', 'RG01'),
('EMP085', 'Randrianatoandro', 'Fidy', '1987-09-02', 'Ambohitsoa', '0371234585', 'fidy.randrianatoandro@gmail.com', 0, 'Lot IAC1 67 Ambohitsoa', NOW(), NULL, 'Randrianatoandro Lucie', 'Randrianatoandro Julien', 'EC085', 1, NULL, 'VEUF', 'M', '1', '56789012', '201123456705', '56789/OST', '102', 'RG01'),
('EMP086', 'Rakotomalala', 'Soa', '1992-11-27', 'Ankorondrano', '0341234586', 'soa.rakotomalala@gmail.com', 0, 'Lot IIAD1 89 Ampefiloha', NOW(), NULL, 'Rakotomalala Monique', 'Rakotomalala Francois', 'EC086', 0, NULL, 'CELIBATAIRE', 'F', '1', '67890123', '202123456704', NULL, '102', 'RG01'),
('EMP087', 'Andriamihaja', 'Mahefa', '1985-06-10', 'Faravohitra', '0321234587', 'mahefa.andriamihaja@gmail.com', 0, 'Lot IIIAE1 12 Isotry', NOW(), NULL, 'Andriamihala Ginette', 'Andriamihaja Raymond', 'EC087', 2, 'Andriamihaja Fenitra', 'MARIE', 'M', '1', '78901234', '201123456703', '67890/OST', '102', 'RG01'),
('EMP088', 'Rakotondrabe', 'Aina', '1996-08-01', 'Tsaralalana', '0381234588', 'aina.rakotondrabe@gmail.com', 0, 'Lot IVAF1 34 Isoraka', NOW(), NULL, 'Rakotondrabe Helene', 'Rakotondrabe Claude', 'EC088', 0, NULL, 'CELIBATAIRE', 'F', '1', '89012345', '202123456702', NULL, '101', 'RG01'),
('EMP089', 'Ravelomanana', 'Njaka', '1983-01-15', 'Antaninarenina', '0331234589', 'njaka.ravelomanana@gmail.com', 0, 'Lot IAG1 56 Antaninarenina', NOW(), NULL, 'Ravelomanana Yvette', 'Ravelomanana Richard', 'EC089', 1, NULL, 'DIVORCE', 'M', '1', '90123456', '201123456701', '78901/OST', '101', 'RG01'),
('EMP090', 'Randrianasolo', 'Tovo', '1990-05-19', 'Ampasampito', '0371234590', 'tovo.randrianasolo@gmail.com', 0, 'Lot IIAH1 78 Andohalo', NOW(), NULL, 'Randrianasolo Jeanne', 'Randrianasolo Henri', 'EC090', 0, NULL, 'CELIBATAIRE', 'M', '1', '01234567', '201123456700', NULL, '101', 'RG01'),
('EMP091', 'Rakotozafy', 'Miora', '1988-12-02', 'Andrefanambohitra', '0341234591', 'miora.rakotozafy@gmail.com', 0, 'Lot IIIAI1 90 Ankaditapaka', NOW(), NULL, 'Rakotozafy Francine', 'Rakotozafy Pierre', 'EC091', 0, NULL, 'CELIBATAIRE', 'F', '1', '123456', '202123456699', NULL, '102', 'RG01'),
('EMP092', 'Ramanantenasoa', 'Hary', '1982-10-12', 'Ambohitsararavina', '0321234592', 'hary.ramanantenasoa@gmail.com', 0, 'Lot IVAJ1 23 Antsahabe', NOW(), NULL, 'Ramanantenasoa Bernadette', 'Ramanantenasoa Andre', 'EC092', 2, 'Ramanantenasoa Vola', 'MARIE', 'M', '1', '234567', '201123456698', '89012/OST', '101', 'RG01'),
('EMP093', 'Razafindrainibe', 'Lala', '1993-07-28', 'Ambohipo', '0381234593', 'lala.razafindrainibe@gmail.com', 0, 'Lot IAK1 45 Ambohidratrimo', NOW(), NULL, 'Razafindrainibe Marthe', 'Razafindrainibe Marcel', 'EC093', 0, NULL, 'CELIBATAIRE', 'F', '1', '345678', '202123456697', NULL, '105', 'RG01'),
('EMP094', 'Rakotonirina', 'Tiana', '1984-03-06', 'Ambohijatovo', '0331234594', 'tiana.rakotonirina@gmail.com', 0, 'Lot IIAL1 67 Ambohijatovo', NOW(), NULL, 'Rakotonirina Colette', 'Rakotonirina Georges', 'EC094', 1, NULL, 'VEUF', 'F', '1', '456789', '202123456696', '90123/OST', '101', 'RG01'),
('EMP095', 'Randriamihaja', 'Faneva', '1995-11-09', 'Andohalo', '0371234595', 'faneva.randriamihaja@gmail.com', 0, 'Lot IIIAM1 89 Andohalo', NOW(), NULL, 'Randriamihaja Juliette', 'Randriamihaja Jean', 'EC095', 0, NULL, 'CELIBATAIRE', 'M', '1', '567890', '201123456695', NULL, '101', 'RG01'),
('EMP096', 'Rakotondrasoa', 'Sitraka', '1987-04-23', 'Analamahitsy', '0341234596', 'sitraka.rakotondrasoa@gmail.com', 0, 'Lot IVAN1 12 Ankadilalana', NOW(), NULL, 'Rakotondrasoa Paulette', 'Rakotondrasoa Robert', 'EC096', 2, 'Rakotondrasoa Nirina', 'MARIE', 'M', '1', '678901', '201123456694', '01234/OST', '102', 'RG01'),
('EMP097', 'Ravelohery', 'Ando', '1991-09-17', 'Ankadinandriana', '0321234597', 'ando.ravelohery@gmail.com', 0, 'Lot IAO1 34 Ambohibao', NOW(), NULL, 'Ravelohery Therese', 'Ravelohery Albert', 'EC097', 0, NULL, 'CELIBATAIRE', 'M', '1', '789012', '201123456693', NULL, '105', 'RG01'),
('EMP098', 'Randriambololona', 'Lova', '1986-01-30', 'Ampasika', '0381234598', 'lova.randriambololona@gmail.com', 0, 'Lot IIAP1 56 Ankadivato', NOW(), NULL, 'Randriambololona Simone', 'Randriambololona Daniel', 'EC098', 1, NULL, 'DIVORCE', 'F', '1', '890123', '202123456692', '12345/OST', '101', 'RG01'),
('EMP099', 'Rakotomavo', 'Hasina', '1980-08-14', 'Ankadilalana', '0331234599', 'hasina.rakotomavo@gmail.com', 0, 'Lot IIIAQ1 78 Ankadilalana', NOW(), NULL, 'Rakotomavo Lucie', 'Rakotomavo Philippe', 'EC099', 3, 'Rakotomavo Tiana', 'MARIE', 'M', '1', '901234', '201123456691', '23456/OST', '102', 'RG01'),
('EMP100', 'Razafindrabe', 'Mamina', '1994-05-25', 'Amboditsiry', '0371234600', 'mamina.razafindrabe@gmail.com', 0, 'Lot IVAR1 90 Amboditsiry', NOW(), NULL, 'Razafindrabe Jacqueline', 'Razafindrabe Serge', 'EC100', 0, NULL, 'CELIBATAIRE', 'F', '1', '012345', '202123456690', NULL, '101', 'RG01'),
('EMP101', 'Rakotoarimanana', 'Tahina', '1989-02-20', 'Antanimena', '0341234601', 'tahina.rakotoarimanana@gmail.com', 0, 'Lot IAS1 23 Antanimena', NOW(), NULL, 'Rakotoarimanana Emilienne', 'Rakotoarimanana Henri', 'EC101', 0, NULL, 'CELIBATAIRE', 'M', '1', '12345678', '201123456689', NULL, '101', 'RG01'),
('EMP102', 'Randriamiandrisoa', 'Fetra', '1983-06-08', 'Ambohidahy', '0321234602', 'fetra.randriamiandrisoa@gmail.com', 0, 'Lot IIAT1 45 Ambohidratrimo', NOW(), NULL, 'Randriamiandrisoa Marie', 'Randriamiandrisoa Julien', 'EC102', 2, 'Randriamiandrisoa Lanto', 'MARIE', 'M', '1', '23456789', '201123456688', '34567/OST', '105', 'RG01'),
('EMP103', 'Rakotovelo', 'Soanirina', '1992-12-04', 'Andranomavo', '0381234603', 'soanirina.rakotovelo@gmail.com', 0, 'Lot IIIAU1 67 Andranomavo', NOW(), NULL, 'Rakotovelo Bernadette', 'Rakotovelo Andre', 'EC103', 0, NULL, 'CELIBATAIRE', 'F', '1', '34567890', '202123456687', NULL, '101', 'RG01'),
('EMP104', 'Razafindramiadana', 'Mampionona', '1985-10-11', 'Ankazomanga', '0331234604', 'mampionona.razafindramiadana@gmail.com', 0, 'Lot IVAV1 89 Ankazomanga', NOW(), NULL, 'Razafindramiadana Monique', 'Razafindramiadana Pierre', 'EC104', 1, NULL, 'VEUF', 'F', '1', '45678901', '202123456686', '45678/OST', '101', 'RG01'),
('EMP105', 'Randrianantoandro', 'Niry', '1981-07-19', 'Ambohimanarina', '0371234605', 'niry.randrianantoandro@gmail.com', 0, 'Lot IAW1 12 Ambohimanarina', NOW(), NULL, 'Randrianantoandro Claire', 'Randrianantoandro Maurice', 'EC105', 0, NULL, 'CELIBATAIRE', 'M', '1', '56789012', '201123456685', NULL, '101', 'RG01'),
('EMP106', 'Rakotoniaina', 'Nantenaina', '1996-03-16', 'Ankadivato', '0341234606', 'nantenaina.rakotoniaina@gmail.com', 0, 'Lot IIAX1 34 Ankadivato', NOW(), NULL, 'Rakotoniaina Berthe', 'Rakotoniaina Robert', 'EC106', 0, NULL, 'CELIBATAIRE', 'M', '1', '67890123', '201123456684', NULL, '101', 'RG01'),
('EMP107', 'Ravelontsalama', 'Faly', '1988-08-27', 'Ampefiloha', '0321234607', 'faly.ravelontsalama@gmail.com', 0, 'Lot IIIAY1 56 Ampefiloha', NOW(), NULL, 'Ravelontsalama Yolande', 'Ravelontsalama Jean', 'EC107', 2, 'Ravelontsalama Mamy', 'MARIE', 'M', '1', '78901234', '201123456683', '56789/OST', '102', 'RG01'),
('EMP108', 'Randrianantenaina', 'Soa', '1990-04-09', 'Isotry', '0381234608', 'soa.randrianantenaina@gmail.com', 0, 'Lot IVAZ1 78 Isotry', NOW(), NULL, 'Randrianantenaina Odette', 'Randrianantenaina Richard', 'EC108', 0, NULL, 'CELIBATAIRE', 'F', '1', '89012345', '202123456682', NULL, '102', 'RG01'),
('EMP109', 'Rakotondramasy', 'Tsinjo', '1984-11-03', 'Ambohidratrimo', '0331234609', 'tsinjo.rakotondramasy@gmail.com', 0, 'Lot IBA1 90 Ambohidratrimo', NOW(), NULL, 'Rakotondramasy Francine', 'Rakotondramasy Georges', 'EC109', 1, NULL, 'DIVORCE', 'F', '1', '90123456', '202123456681', '67890/OST', '105', 'RG01'),
('EMP110', 'Razafitsalama', 'Hery', '1982-09-21', 'Ankaditapaka', '0371234610', 'hery.razafitsalama@gmail.com', 0, 'Lot IIBB1 23 Ankaditapaka', NOW(), NULL, 'Razafitsalama Cecile', 'Razafitsalama Albert', 'EC110', 0, NULL, 'CELIBATAIRE', 'M', '1', '01234567', '201123456680', NULL, '102', 'RG01'),
('EMP111', 'Randriamananjara', 'Tahiry', '1993-06-13', 'Ambohimiandra', '0341234611', 'tahiry.randriamananjara@gmail.com', 0, 'Lot IIIBC1 45 Ambohimiandra', NOW(), NULL, 'Randriamananjara Simone', 'Randriamananjara Philippe', 'EC111', 2, 'Randriamananjara Njaka', 'MARIE', 'M', '1', '123456', '201123456679', '78901/OST', '102', 'RG01'),
('EMP112', 'Rakotomalala', 'Miora', '1995-01-28', 'Antsahabe', '0321234612', 'miora.rakotomalala@gmail.com', 0, 'Lot IVBD1 67 Antsahabe', NOW(), NULL, 'Rakotomalala Jacqueline', 'Rakotomalala Marcel', 'EC112', 0, NULL, 'CELIBATAIRE', 'F', '1', '234567', '202123456678', NULL, '101', 'RG01'),
('EMP113', 'Andriamampionona', 'Lalaina', '1987-12-15', 'Ambohibao', '0381234613', 'lalaina.andriamampionona@gmail.com', 0, 'Lot IBE1 89 Ambohibao', NOW(), NULL, 'Andriamampionona Marie', 'Andriamampionona Andre', 'EC113', 0, NULL, 'CELIBATAIRE', 'M', '1', '345678', '201123456677', NULL, '105', 'RG01'),
('EMP114', 'Razafimahatratra', 'Nandrianina', '1991-05-07', 'Ankadilalana', '0331234614', 'nandrianina.razafimahatratra@gmail.com', 0, 'Lot IIBF1 12 Ankadilalana', NOW(), NULL, 'Razafimahatratra Helene', 'Razafimahatratra Jean', 'EC114', 1, NULL, 'VEUF', 'F', '1', '456789', '202123456676', '89012/OST', '102', 'RG01'),
('EMP115', 'Rakotondramanana', 'Toky', '1986-10-18', 'Mahamasina', '0371234615', 'toky.rakotondramanana@gmail.com', 0, 'Lot IIIBG1 34 Mahamasina', NOW(), NULL, 'Rakotondramanana Paulette', 'Rakotondramanana Robert', 'EC115', 2, 'Rakotondramanana Aina', 'MARIE', 'M', '1', '567890', '201123456675', '90123/OST', '101', 'RG01'),
('EMP116', 'Randrianarivelo', 'Faniry', '1989-07-22', 'Ambohijatovo', '0341234616', 'faniry.randrianarivelo@gmail.com', 0, 'Lot IVBH1 56 Ambohijatovo', NOW(), NULL, 'Randrianarivelo Lucie', 'Randrianarivelo Georges', 'EC116', 0, NULL, 'CELIBATAIRE', 'F', '1', '678901', '202123456674', NULL, '101', 'RG01'),
('EMP117', 'Rakotondrazafy', 'Nivo', '1980-04-04', 'Anosy', '0321234617', 'nivo.rakotondrazafy@gmail.com', 0, 'Lot IBI1 78 Anosy', NOW(), NULL, 'Rakotondrazafy Bernadette', 'Rakotondrazafy Julien', 'EC117', 3, 'Rakotondrazafy Tiana', 'MARIE', 'F', '1', '789012', '202123456673', '01234/OST', '101', 'RG01'),
('EMP118', 'Ramanantenasoa', 'Mahefa', '1994-09-30', 'Andrainarivo', '0381234618', 'mahefa.ramanantenasoa@gmail.com', 0, 'Lot IIBJ1 90 Andrainarivo', NOW(), NULL, 'Ramanantenasoa Yvette', 'Ramanantenasoa Henri', 'EC118', 0, NULL, 'CELIBATAIRE', 'M', '1', '890123', '201123456672', NULL, '101', 'RG01'),
('EMP119', 'Ravelojaona', 'Fenitra', '1985-02-11', 'Ambohidahy', '0331234619', 'fenitra.ravelojaona@gmail.com', 0, 'Lot IIIBK1 23 Ambohidratrimo', NOW(), NULL, 'Ravelojaona Therese', 'Ravelojaona Philippe', 'EC119', 0, NULL, 'CELIBATAIRE', 'M', '1', '901234', '201123456671', NULL, '105', 'RG01'),
('EMP120', 'Randriamanantsoa', 'Aina', '1992-08-23', 'Ambanidia', '0371234620', 'aina.randriamanantsoa@gmail.com', 0, 'Lot IVBL1 45 Ankadifotsy', NOW(), NULL, 'Randriamanantsoa Monique', 'Randriamanantsoa Richard', 'EC120', 1, NULL, 'DIVORCE', 'F', '1', '012345', '202123456670', '12345/OST', '101', 'RG01');

-- Données pour la table manager
-- Un manager par département (7 départements)
-- Les managers sont choisis parmi les employés avec des postes de direction ou supervision

INSERT INTO manager (id, date_debut, date_fin, created_at, modified_at, statut, id_departement, id_employe, commentaire) VALUES
('MGR001', '2023-01-01', NULL, NOW(), NULL, 0, 'DEP1', 'EMP005', 'Manager du département Ressources Humaines, en poste depuis janvier 2023'),
('MGR002', '2023-01-01', NULL, NOW(), NULL, 0, 'DEP2', 'EMP015', 'Manager du département Ventes et Marketing, expérimenté dans le domaine commercial'),
('MGR003', '2023-01-01', NULL, NOW(), NULL, 0, 'DEP3', 'EMP012', 'Manager du département Developpement Informatique, expert en solutions digitales'),
('MGR004', '2023-01-01', NULL, NOW(), NULL, 0, 'DEP4', 'EMP041', 'Manager du département Comptabilite Generale, 15 ans d expérience en finance'),
('MGR005', '2023-01-01', NULL, NOW(), NULL, 0, 'DEP5', 'EMP022', 'Manager du département Maintenance et Logistique, garant de la performance opérationnelle'),
('MGR006', '2023-01-01', NULL, NOW(), NULL, 0, 'DEP338BD413', 'EMP001', 'Directeur General, responsable de la stratégie globale de l entreprise'),
('MGR007', '2023-03-15', NULL, NOW(), NULL, 0, 'DEP7C3861D7', 'EMP029', 'Manager du département Intendance, supervise les équipes de nettoyage et sécurité');

-- =====================================================
-- INFOS_PROFESSIONNELLES - 120 EMPLOYES (EMP001 à EMP120)
-- CDI: période d'essai + contrat définitif (2 lignes)
-- CDD: 1 ligne
-- Inactifs: 10 (statut=1)
-- RH = 4 employés UNIQUEMENT (EMP005, EMP002, EMP013, EMP076)
-- =====================================================

-- ==================== PERIODES D'ESSAI (CDI) - 70 lignes ====================

INSERT INTO infos_professionnelles (id, date_embauche, created_at, modified_at, id_poste, id_type_contrat, id_employe, id_manager, statut, date_debauche, date_debut_assignation_poste, date_fin_assignation_poste, salaire_base, matricule, motif_depart, id_departement, id_type_entree, id_categorie, classification, id_temps_travail) VALUES
-- DEPARTEMENT RH (DEP1) - 4 employés
('INF001', '2023-01-01', NOW(), NULL, 'POST005', 'CONT009', 'EMP005', 'MGR001', 0, NULL, '2023-01-01', '2023-06-30', 1500000.00, '2301', NULL, 'DEP1', 'TE01', 'CPG05', 'B1', 1),
('INF002', '2023-02-15', NOW(), NULL, 'POST002', 'CONT009', 'EMP002', 'MGR001', 0, NULL, '2023-02-15', '2023-08-14', 800000.00, '2302', NULL, 'DEP1', 'TE01', 'CPG03', 'C2', 1),
('INF003', '2023-03-01', NOW(), NULL, 'POST004', 'CONT009', 'EMP013', 'MGR001', 0, NULL, '2023-03-01', '2023-08-31', 1100000.00, '2303', NULL, 'DEP1', 'TE01', 'CPG04', 'D1', 1),
('INF004', '2023-06-01', NOW(), NULL, 'POST001', 'CONT009', 'EMP076', 'MGR001', 0, NULL, '2023-06-01', '2023-11-30', 550000.00, '2304', NULL, 'DEP1', 'TE01', 'CPG02', 'C1', 1),

-- DEPARTEMENT MARKETING (DEP2) - 16 CDI
('INF005', '2023-01-10', NOW(), NULL, 'POST015', 'CONT009', 'EMP015', 'MGR002', 0, NULL, '2023-01-10', '2023-07-09', 1550000.00, '2305', NULL, 'DEP2', 'TE01', 'CPG05', 'B1', 1),
('INF006', '2023-02-20', NOW(), NULL, 'POST012', 'CONT009', 'EMP003', 'MGR002', 0, NULL, '2023-02-20', '2023-08-19', 850000.00, '2306', NULL, 'DEP2', 'TE01', 'CPG03', 'C2', 1),
('INF007', '2023-03-15', NOW(), NULL, 'POST014', 'CONT009', 'EMP008', 'MGR002', 0, NULL, '2023-03-15', '2023-09-14', 1200000.00, '2307', NULL, 'DEP2', 'TE01', 'CPG04', 'D1', 1),
('INF008', '2023-04-01', NOW(), NULL, 'POST011', 'CONT009', 'EMP025', 'MGR002', 0, NULL, '2023-04-01', '2023-09-30', 600000.00, '2308', NULL, 'DEP2', 'TE01', 'CPG02', 'C1', 1),
('INF009', '2023-05-10', NOW(), NULL, 'POST013', 'CONT009', 'EMP028', 'MGR002', 0, NULL, '2023-05-10', '2023-11-09', 1400000.00, '2309', NULL, 'DEP2', 'TE01', 'CPG05', 'B1', 1),
('INF010', '2023-06-15', NOW(), NULL, 'POST011', 'CONT009', 'EMP030', 'MGR002', 0, NULL, '2023-06-15', '2023-12-14', 600000.00, '2310', NULL, 'DEP2', 'TE01', 'CPG02', 'C1', 1),
('INF011', '2023-07-01', NOW(), NULL, 'POST012', 'CONT009', 'EMP038', 'MGR002', 0, NULL, '2023-07-01', '2023-12-31', 800000.00, '2311', NULL, 'DEP2', 'TE01', 'CPG03', 'C2', 1),
('INF012', '2023-08-01', NOW(), NULL, 'POST014', 'CONT009', 'EMP039', 'MGR002', 0, NULL, '2023-08-01', '2024-01-31', 1150000.00, '2312', NULL, 'DEP2', 'TE01', 'CPG04', 'D1', 1),
('INF013', '2023-09-01', NOW(), NULL, 'POST015', 'CONT009', 'EMP045', 'MGR002', 0, NULL, '2023-09-01', '2024-02-29', 1500000.00, '2313', NULL, 'DEP2', 'TE01', 'CPG05', 'B1', 1),
('INF014', '2023-10-01', NOW(), NULL, 'POST011', 'CONT009', 'EMP049', 'MGR002', 0, NULL, '2023-10-01', '2024-03-31', 600000.00, '2314', NULL, 'DEP2', 'TE01', 'CPG02', 'C1', 1),
('INF015', '2023-11-01', NOW(), NULL, 'POST012', 'CONT009', 'EMP052', 'MGR002', 0, NULL, '2023-11-01', '2024-04-30', 800000.00, '2315', NULL, 'DEP2', 'TE01', 'CPG03', 'C2', 1),
('INF016', '2023-12-01', NOW(), NULL, 'POST013', 'CONT009', 'EMP058', 'MGR002', 0, NULL, '2023-12-01', '2024-05-31', 1350000.00, '2316', NULL, 'DEP2', 'TE01', 'CPG05', 'B1', 1),
('INF017', '2024-01-15', NOW(), NULL, 'POST011', 'CONT009', 'EMP063', 'MGR002', 0, NULL, '2024-01-15', '2024-07-14', 620000.00, '2317', NULL, 'DEP2', 'TE01', 'CPG02', 'C1', 1),
('INF018', '2024-02-01', NOW(), NULL, 'POST012', 'CONT009', 'EMP067', 'MGR002', 0, NULL, '2024-02-01', '2024-07-31', 820000.00, '2318', NULL, 'DEP2', 'TE01', 'CPG03', 'C2', 1),
('INF019', '2024-03-01', NOW(), NULL, 'POST014', 'CONT009', 'EMP069', 'MGR002', 0, NULL, '2024-03-01', '2024-08-31', 1180000.00, '2319', NULL, 'DEP2', 'TE01', 'CPG04', 'D1', 1),
('INF020', '2024-04-01', NOW(), NULL, 'POST015', 'CONT009', 'EMP075', 'MGR002', 0, NULL, '2024-04-01', '2024-09-30', 1520000.00, '2320', NULL, 'DEP2', 'TE01', 'CPG05', 'B1', 1),

-- DEPARTEMENT INFORMATIQUE (DEP3) - 14 CDI
('INF021', '2023-01-20', NOW(), NULL, 'POST010', 'CONT009', 'EMP012', 'MGR003', 0, NULL, '2023-01-20', '2023-07-19', 1600000.00, '2321', NULL, 'DEP3', 'TE01', 'CPG05', 'B1', 1),
('INF022', '2023-02-01', NOW(), NULL, 'POST007', 'CONT009', 'EMP006', 'MGR003', 0, NULL, '2023-02-01', '2023-07-31', 1000000.00, '2322', NULL, 'DEP3', 'TE01', 'CPG03', 'C2', 1),
('INF023', '2023-03-10', NOW(), NULL, 'POST009', 'CONT009', 'EMP009', 'MGR003', 0, NULL, '2023-03-10', '2023-09-09', 1300000.00, '2323', NULL, 'DEP3', 'TE01', 'CPG04', 'D1', 1),
('INF024', '2023-04-15', NOW(), NULL, 'POST006', 'CONT009', 'EMP019', 'MGR003', 0, NULL, '2023-04-15', '2023-10-14', 650000.00, '2324', NULL, 'DEP3', 'TE01', 'CPG02', 'C1', 1),
('INF025', '2023-05-20', NOW(), NULL, 'POST008', 'CONT009', 'EMP036', 'MGR003', 0, NULL, '2023-05-20', '2023-11-19', 1450000.00, '2325', NULL, 'DEP3', 'TE01', 'CPG05', 'B1', 1),
('INF026', '2023-06-20', NOW(), NULL, 'POST006', 'CONT009', 'EMP037', 'MGR003', 0, NULL, '2023-06-20', '2023-12-19', 650000.00, '2326', NULL, 'DEP3', 'TE01', 'CPG02', 'C1', 1),
('INF027', '2023-07-10', NOW(), NULL, 'POST007', 'CONT009', 'EMP040', 'MGR003', 0, NULL, '2023-07-10', '2024-01-09', 950000.00, '2327', NULL, 'DEP3', 'TE01', 'CPG03', 'C2', 1),
('INF028', '2023-08-15', NOW(), NULL, 'POST009', 'CONT009', 'EMP044', 'MGR003', 0, NULL, '2023-08-15', '2024-02-14', 1250000.00, '2328', NULL, 'DEP3', 'TE01', 'CPG04', 'D1', 1),
('INF029', '2023-09-10', NOW(), NULL, 'POST010', 'CONT009', 'EMP048', 'MGR003', 0, NULL, '2023-09-10', '2024-03-09', 1550000.00, '2329', NULL, 'DEP3', 'TE01', 'CPG05', 'B1', 1),
('INF030', '2023-10-05', NOW(), NULL, 'POST006', 'CONT009', 'EMP053', 'MGR003', 0, NULL, '2023-10-05', '2024-04-04', 620000.00, '2330', NULL, 'DEP3', 'TE01', 'CPG02', 'C1', 1),
('INF031', '2023-11-15', NOW(), NULL, 'POST007', 'CONT009', 'EMP059', 'MGR003', 0, NULL, '2023-11-15', '2024-05-14', 920000.00, '2331', NULL, 'DEP3', 'TE01', 'CPG03', 'C2', 1),
('INF032', '2024-01-10', NOW(), NULL, 'POST008', 'CONT009', 'EMP062', 'MGR003', 0, NULL, '2024-01-10', '2024-07-09', 1400000.00, '2332', NULL, 'DEP3', 'TE01', 'CPG05', 'B1', 1),
('INF033', '2024-02-15', NOW(), NULL, 'POST006', 'CONT009', 'EMP066', 'MGR003', 0, NULL, '2024-02-15', '2024-08-14', 630000.00, '2333', NULL, 'DEP3', 'TE01', 'CPG02', 'C1', 1),
('INF034', '2024-03-20', NOW(), NULL, 'POST007', 'CONT009', 'EMP072', 'MGR003', 0, NULL, '2024-03-20', '2024-09-19', 980000.00, '2334', NULL, 'DEP3', 'TE01', 'CPG03', 'C2', 1),

-- DEPARTEMENT FINANCE (DEP4) - 14 CDI
('INF035', '2023-01-05', NOW(), NULL, 'POST020', 'CONT009', 'EMP041', 'MGR004', 0, NULL, '2023-01-05', '2023-07-04', 1500000.00, '2335', NULL, 'DEP4', 'TE01', 'CPG05', 'B1', 1),
('INF036', '2023-02-10', NOW(), NULL, 'POST017', 'CONT009', 'EMP007', 'MGR004', 0, NULL, '2023-02-10', '2023-08-09', 800000.00, '2336', NULL, 'DEP4', 'TE01', 'CPG03', 'C2', 1),
('INF037', '2023-03-05', NOW(), NULL, 'POST019', 'CONT009', 'EMP014', 'MGR004', 0, NULL, '2023-03-05', '2023-09-04', 1200000.00, '2337', NULL, 'DEP4', 'TE01', 'CPG04', 'D1', 1),
('INF038', '2023-04-20', NOW(), NULL, 'POST016', 'CONT009', 'EMP023', 'MGR004', 0, NULL, '2023-04-20', '2023-10-19', 650000.00, '2338', NULL, 'DEP4', 'TE01', 'CPG02', 'C1', 1),
('INF039', '2023-05-15', NOW(), NULL, 'POST018', 'CONT009', 'EMP027', 'MGR004', 0, NULL, '2023-05-15', '2023-11-14', 1400000.00, '2339', NULL, 'DEP4', 'TE01', 'CPG05', 'B1', 1),
('INF040', '2023-06-10', NOW(), NULL, 'POST016', 'CONT009', 'EMP031', 'MGR004', 0, NULL, '2023-06-10', '2023-12-09', 650000.00, '2340', NULL, 'DEP4', 'TE01', 'CPG02', 'C1', 1),
('INF041', '2023-07-05', NOW(), NULL, 'POST017', 'CONT009', 'EMP042', 'MGR004', 0, NULL, '2023-07-05', '2024-01-04', 800000.00, '2341', NULL, 'DEP4', 'TE01', 'CPG03', 'C2', 1),
('INF042', '2023-08-10', NOW(), NULL, 'POST019', 'CONT009', 'EMP046', 'MGR004', 0, NULL, '2023-08-10', '2024-02-09', 1180000.00, '2342', NULL, 'DEP4', 'TE01', 'CPG04', 'D1', 1),
('INF043', '2023-09-05', NOW(), NULL, 'POST020', 'CONT009', 'EMP050', 'MGR004', 0, NULL, '2023-09-05', '2024-03-04', 1480000.00, '2343', NULL, 'DEP4', 'TE01', 'CPG05', 'B1', 1),
('INF044', '2023-10-15', NOW(), NULL, 'POST016', 'CONT009', 'EMP054', 'MGR004', 0, NULL, '2023-10-15', '2024-04-14', 620000.00, '2344', NULL, 'DEP4', 'TE01', 'CPG02', 'C1', 1),
('INF045', '2023-11-20', NOW(), NULL, 'POST017', 'CONT009', 'EMP060', 'MGR004', 0, NULL, '2023-11-20', '2024-05-19', 820000.00, '2345', NULL, 'DEP4', 'TE01', 'CPG03', 'C2', 1),
('INF046', '2024-01-05', NOW(), NULL, 'POST018', 'CONT009', 'EMP064', 'MGR004', 0, NULL, '2024-01-05', '2024-07-04', 1380000.00, '2346', NULL, 'DEP4', 'TE01', 'CPG05', 'B1', 1),
('INF047', '2024-02-20', NOW(), NULL, 'POST016', 'CONT009', 'EMP071', 'MGR004', 0, NULL, '2024-02-20', '2024-08-19', 630000.00, '2347', NULL, 'DEP4', 'TE01', 'CPG02', 'C1', 1),
('INF048', '2024-03-10', NOW(), NULL, 'POST017', 'CONT009', 'EMP073', 'MGR004', 0, NULL, '2024-03-10', '2024-09-09', 820000.00, '2348', NULL, 'DEP4', 'TE01', 'CPG03', 'C2', 1),

-- DEPARTEMENT OPERATIONS (DEP5) - 14 CDI
('INF049', '2023-01-25', NOW(), NULL, 'POST025', 'CONT009', 'EMP022', 'MGR005', 0, NULL, '2023-01-25', '2023-07-24', 1450000.00, '2349', NULL, 'DEP5', 'TE01', 'CPG05', 'B1', 1),
('INF050', '2023-02-15', NOW(), NULL, 'POST022', 'CONT009', 'EMP004', 'MGR005', 0, NULL, '2023-02-15', '2023-08-14', 900000.00, '2350', NULL, 'DEP5', 'TE01', 'CPG03', 'C2', 1),
('INF051', '2023-03-20', NOW(), NULL, 'POST024', 'CONT009', 'EMP011', 'MGR005', 0, NULL, '2023-03-20', '2023-09-19', 1250000.00, '2351', NULL, 'DEP5', 'TE01', 'CPG04', 'D1', 1),
('INF052', '2023-04-10', NOW(), NULL, 'POST021', 'CONT009', 'EMP017', 'MGR005', 0, NULL, '2023-04-10', '2023-10-09', 650000.00, '2352', NULL, 'DEP5', 'TE01', 'CPG02', 'C1', 1),
('INF053', '2023-05-25', NOW(), NULL, 'POST023', 'CONT009', 'EMP024', 'MGR005', 0, NULL, '2023-05-25', '2023-11-24', 1350000.00, '2353', NULL, 'DEP5', 'TE01', 'CPG05', 'B1', 1),
('INF054', '2023-06-05', NOW(), NULL, 'POST021', 'CONT009', 'EMP032', 'MGR005', 0, NULL, '2023-06-05', '2023-12-04', 650000.00, '2354', NULL, 'DEP5', 'TE01', 'CPG02', 'C1', 1),
('INF055', '2023-07-15', NOW(), NULL, 'POST022', 'CONT009', 'EMP033', 'MGR005', 0, NULL, '2023-07-15', '2024-01-14', 850000.00, '2355', NULL, 'DEP5', 'TE01', 'CPG03', 'C2', 1),
('INF056', '2023-08-20', NOW(), NULL, 'POST024', 'CONT009', 'EMP035', 'MGR005', 0, NULL, '2023-08-20', '2024-02-19', 1220000.00, '2356', NULL, 'DEP5', 'TE01', 'CPG04', 'D1', 1),
('INF057', '2023-09-25', NOW(), NULL, 'POST025', 'CONT009', 'EMP043', 'MGR005', 0, NULL, '2023-09-25', '2024-03-24', 1420000.00, '2357', NULL, 'DEP5', 'TE01', 'CPG05', 'B1', 1),
('INF058', '2023-10-30', NOW(), NULL, 'POST021', 'CONT009', 'EMP047', 'MGR005', 0, NULL, '2023-10-30', '2024-04-29', 640000.00, '2358', NULL, 'DEP5', 'TE01', 'CPG02', 'C1', 1),
('INF059', '2023-11-15', NOW(), NULL, 'POST022', 'CONT009', 'EMP051', 'MGR005', 0, NULL, '2023-11-15', '2024-05-14', 880000.00, '2359', NULL, 'DEP5', 'TE01', 'CPG03', 'C2', 1),
('INF060', '2024-01-20', NOW(), NULL, 'POST023', 'CONT009', 'EMP055', 'MGR005', 0, NULL, '2024-01-20', '2024-07-19', 1320000.00, '2360', NULL, 'DEP5', 'TE01', 'CPG05', 'B1', 1),
('INF061', '2024-02-25', NOW(), NULL, 'POST021', 'CONT009', 'EMP061', 'MGR005', 0, NULL, '2024-02-25', '2024-08-24', 640000.00, '2361', NULL, 'DEP5', 'TE01', 'CPG02', 'C1', 1),
('INF062', '2024-03-30', NOW(), NULL, 'POST022', 'CONT009', 'EMP068', 'MGR005', 0, NULL, '2024-03-30', '2024-09-29', 860000.00, '2362', NULL, 'DEP5', 'TE01', 'CPG03', 'C2', 1),

-- DEPARTEMENT DIRECTION GENERALE (DEP338BD413) - 8 CDI
('INF063', '2023-01-01', NOW(), NULL, 'POST026', 'CONT009', 'EMP001', 'MGR006', 0, NULL, '2023-01-01', '2023-06-30', 3500000.00, '2363', NULL, 'DEP338BD413', 'TE01', 'CPG05', 'A1', 1),
('INF064', '2023-02-01', NOW(), NULL, 'POST027', 'CONT009', 'EMP010', 'MGR006', 0, NULL, '2023-02-01', '2023-07-31', 600000.00, '2364', NULL, 'DEP338BD413', 'TE01', 'CPG02', 'C1', 1),
('INF065', '2023-03-01', NOW(), NULL, 'POST028', 'CONT009', 'EMP016', 'MGR006', 0, NULL, '2023-03-01', '2023-08-31', 650000.00, '2365', NULL, 'DEP338BD413', 'TE01', 'CPG02', 'C1', 1),
('INF066', '2023-04-01', NOW(), NULL, 'POST033', 'CONT009', 'EMP018', 'MGR006', 0, NULL, '2023-04-01', '2023-09-30', 1800000.00, '2366', NULL, 'DEP338BD413', 'TE01', 'CPG05', 'B1', 1),
('INF067', '2023-05-15', NOW(), NULL, 'POST028', 'CONT009', 'EMP020', 'MGR006', 0, NULL, '2023-05-15', '2023-11-14', 600000.00, '2367', NULL, 'DEP338BD413', 'TE01', 'CPG02', 'C1', 1),
('INF068', '2023-07-01', NOW(), NULL, 'POST027', 'CONT009', 'EMP021', 'MGR006', 0, NULL, '2023-07-01', '2023-12-31', 620000.00, '2368', NULL, 'DEP338BD413', 'TE01', 'CPG02', 'C1', 1),
('INF069', '2023-09-01', NOW(), NULL, 'POST033', 'CONT009', 'EMP026', 'MGR006', 0, NULL, '2023-09-01', '2024-02-29', 1750000.00, '2369', NULL, 'DEP338BD413', 'TE01', 'CPG05', 'B1', 1),
('INF070', '2024-01-10', NOW(), NULL, 'POST028', 'CONT009', 'EMP029', 'MGR006', 0, NULL, '2024-01-10', '2024-07-09', 620000.00, '2370', NULL, 'DEP338BD413', 'TE01', 'CPG02', 'C1', 1);

-- ==================== CONTRATS CDI (définitifs) - 70 lignes ====================

INSERT INTO infos_professionnelles (id, date_embauche, created_at, modified_at, id_poste, id_type_contrat, id_employe, id_manager, statut, date_debauche, date_debut_assignation_poste, date_fin_assignation_poste, salaire_base, matricule, motif_depart, id_departement, id_type_entree, id_categorie, classification, id_temps_travail) VALUES
-- DEPARTEMENT RH (DEP1) - 4 CDI
('INF101', '2023-01-01', NOW(), NULL, 'POST005', 'CONT001', 'EMP005', 'MGR001', 0, NULL, '2023-07-01', NULL, 1600000.00, '2301', NULL, 'DEP1', 'TE01', 'CPG05', 'B1', 1),
('INF102', '2023-02-15', NOW(), NULL, 'POST002', 'CONT001', 'EMP002', 'MGR001', 0, NULL, '2023-08-15', NULL, 850000.00, '2302', NULL, 'DEP1', 'TE01', 'CPG03', 'C2', 1),
('INF103', '2023-03-01', NOW(), NULL, 'POST004', 'CONT001', 'EMP013', 'MGR001', 0, NULL, '2023-09-01', NULL, 1150000.00, '2303', NULL, 'DEP1', 'TE01', 'CPG04', 'D1', 1),
('INF104', '2023-06-01', NOW(), NULL, 'POST001', 'CONT001', 'EMP076', 'MGR001', 0, NULL, '2023-12-01', NULL, 600000.00, '2304', NULL, 'DEP1', 'TE01', 'CPG02', 'C1', 1),

-- DEPARTEMENT MARKETING (DEP2) - 16 CDI
('INF105', '2023-01-10', NOW(), NULL, 'POST015', 'CONT001', 'EMP015', 'MGR002', 0, NULL, '2023-07-10', NULL, 1650000.00, '2305', NULL, 'DEP2', 'TE01', 'CPG05', 'B1', 1),
('INF106', '2023-02-20', NOW(), NULL, 'POST012', 'CONT001', 'EMP003', 'MGR002', 0, NULL, '2023-08-20', NULL, 900000.00, '2306', NULL, 'DEP2', 'TE01', 'CPG03', 'C2', 1),
('INF107', '2023-03-15', NOW(), NULL, 'POST014', 'CONT001', 'EMP008', 'MGR002', 0, NULL, '2023-09-15', NULL, 1250000.00, '2307', NULL, 'DEP2', 'TE01', 'CPG04', 'D1', 1),
('INF108', '2023-04-01', NOW(), NULL, 'POST011', 'CONT001', 'EMP025', 'MGR002', 0, NULL, '2023-10-01', NULL, 650000.00, '2308', NULL, 'DEP2', 'TE01', 'CPG02', 'C1', 1),
('INF109', '2023-05-10', NOW(), NULL, 'POST013', 'CONT001', 'EMP028', 'MGR002', 0, NULL, '2023-11-10', NULL, 1450000.00, '2309', NULL, 'DEP2', 'TE01', 'CPG05', 'B1', 1),
('INF110', '2023-06-15', NOW(), NULL, 'POST011', 'CONT001', 'EMP030', 'MGR002', 0, NULL, '2023-12-15', NULL, 650000.00, '2310', NULL, 'DEP2', 'TE01', 'CPG02', 'C1', 1),
('INF111', '2023-07-01', NOW(), NULL, 'POST012', 'CONT001', 'EMP038', 'MGR002', 0, NULL, '2024-01-01', NULL, 850000.00, '2311', NULL, 'DEP2', 'TE01', 'CPG03', 'C2', 1),
('INF112', '2023-08-01', NOW(), NULL, 'POST014', 'CONT001', 'EMP039', 'MGR002', 0, NULL, '2024-02-01', NULL, 1200000.00, '2312', NULL, 'DEP2', 'TE01', 'CPG04', 'D1', 1),
('INF113', '2023-09-01', NOW(), NULL, 'POST015', 'CONT001', 'EMP045', 'MGR002', 0, NULL, '2024-03-01', NULL, 1580000.00, '2313', NULL, 'DEP2', 'TE01', 'CPG05', 'B1', 1),
('INF114', '2023-10-01', NOW(), NULL, 'POST011', 'CONT001', 'EMP049', 'MGR002', 0, NULL, '2024-04-01', NULL, 650000.00, '2314', NULL, 'DEP2', 'TE01', 'CPG02', 'C1', 1),
('INF115', '2023-11-01', NOW(), NULL, 'POST012', 'CONT001', 'EMP052', 'MGR002', 0, NULL, '2024-05-01', NULL, 850000.00, '2315', NULL, 'DEP2', 'TE01', 'CPG03', 'C2', 1),
('INF116', '2023-12-01', NOW(), NULL, 'POST013', 'CONT001', 'EMP058', 'MGR002', 0, NULL, '2024-06-01', NULL, 1400000.00, '2316', NULL, 'DEP2', 'TE01', 'CPG05', 'B1', 1),
('INF117', '2024-01-15', NOW(), NULL, 'POST011', 'CONT001', 'EMP063', 'MGR002', 0, NULL, '2024-07-15', NULL, 670000.00, '2317', NULL, 'DEP2', 'TE02', 'CPG02', 'C1', 1),
('INF118', '2024-02-01', NOW(), NULL, 'POST012', 'CONT001', 'EMP067', 'MGR002', 0, NULL, '2024-08-01', NULL, 870000.00, '2318', NULL, 'DEP2', 'TE03', 'CPG03', 'C2', 1),
('INF119', '2024-03-01', NOW(), NULL, 'POST014', 'CONT001', 'EMP069', 'MGR002', 0, NULL, '2024-09-01', NULL, 1230000.00, '2319', NULL, 'DEP2', 'TE04', 'CPG04', 'D1', 1),
('INF120', '2024-04-01', NOW(), NULL, 'POST015', 'CONT001', 'EMP075', 'MGR002', 0, NULL, '2024-10-01', NULL, 1600000.00, '2320', NULL, 'DEP2', 'TE05', 'CPG05', 'B1', 1),

-- DEPARTEMENT INFORMATIQUE (DEP3) - 14 CDI
('INF121', '2023-01-20', NOW(), NULL, 'POST010', 'CONT001', 'EMP012', 'MGR003', 0, NULL, '2023-07-20', NULL, 1700000.00, '2321', NULL, 'DEP3', 'TE01', 'CPG05', 'B1', 1),
('INF122', '2023-02-01', NOW(), NULL, 'POST007', 'CONT001', 'EMP006', 'MGR003', 0, NULL, '2023-08-01', NULL, 1100000.00, '2322', NULL, 'DEP3', 'TE01', 'CPG03', 'C2', 1),
('INF123', '2023-03-10', NOW(), NULL, 'POST009', 'CONT001', 'EMP009', 'MGR003', 0, NULL, '2023-09-10', NULL, 1350000.00, '2323', NULL, 'DEP3', 'TE01', 'CPG04', 'D1', 1),
('INF124', '2023-04-15', NOW(), NULL, 'POST006', 'CONT001', 'EMP019', 'MGR003', 0, NULL, '2023-10-15', NULL, 700000.00, '2324', NULL, 'DEP3', 'TE01', 'CPG02', 'C1', 1),
('INF125', '2023-05-20', NOW(), NULL, 'POST008', 'CONT001', 'EMP036', 'MGR003', 0, NULL, '2023-11-20', NULL, 1500000.00, '2325', NULL, 'DEP3', 'TE01', 'CPG05', 'B1', 1),
('INF126', '2023-06-20', NOW(), NULL, 'POST006', 'CONT001', 'EMP037', 'MGR003', 0, NULL, '2023-12-20', NULL, 700000.00, '2326', NULL, 'DEP3', 'TE01', 'CPG02', 'C1', 1),
('INF127', '2023-07-10', NOW(), NULL, 'POST007', 'CONT001', 'EMP040', 'MGR003', 0, NULL, '2024-01-10', NULL, 1000000.00, '2327', NULL, 'DEP3', 'TE01', 'CPG03', 'C2', 1),
('INF128', '2023-08-15', NOW(), NULL, 'POST009', 'CONT001', 'EMP044', 'MGR003', 0, NULL, '2024-02-15', NULL, 1300000.00, '2328', NULL, 'DEP3', 'TE01', 'CPG04', 'D1', 1),
('INF129', '2023-09-10', NOW(), NULL, 'POST010', 'CONT001', 'EMP048', 'MGR003', 0, NULL, '2024-03-10', NULL, 1650000.00, '2329', NULL, 'DEP3', 'TE01', 'CPG05', 'B1', 1),
('INF130', '2023-10-05', NOW(), NULL, 'POST006', 'CONT001', 'EMP053', 'MGR003', 0, NULL, '2024-04-05', NULL, 670000.00, '2330', NULL, 'DEP3', 'TE01', 'CPG02', 'C1', 1),
('INF131', '2023-11-15', NOW(), NULL, 'POST007', 'CONT001', 'EMP059', 'MGR003', 0, NULL, '2024-05-15', NULL, 970000.00, '2331', NULL, 'DEP3', 'TE01', 'CPG03', 'C2', 1),
('INF132', '2024-01-10', NOW(), NULL, 'POST008', 'CONT001', 'EMP062', 'MGR003', 0, NULL, '2024-07-10', NULL, 1450000.00, '2332', NULL, 'DEP3', 'TE06', 'CPG05', 'B1', 1),
('INF133', '2024-02-15', NOW(), NULL, 'POST006', 'CONT001', 'EMP066', 'MGR003', 0, NULL, '2024-08-15', NULL, 680000.00, '2333', NULL, 'DEP3', 'TE07', 'CPG02', 'C1', 1),
('INF134', '2024-03-20', NOW(), NULL, 'POST007', 'CONT001', 'EMP072', 'MGR003', 0, NULL, '2024-09-20', NULL, 1030000.00, '2334', NULL, 'DEP3', 'TE08', 'CPG03', 'C2', 1),

-- DEPARTEMENT FINANCE (DEP4) - 14 CDI
('INF135', '2023-01-05', NOW(), NULL, 'POST020', 'CONT001', 'EMP041', 'MGR004', 0, NULL, '2023-07-05', NULL, 1600000.00, '2335', NULL, 'DEP4', 'TE01', 'CPG05', 'B1', 1),
('INF136', '2023-02-10', NOW(), NULL, 'POST017', 'CONT001', 'EMP007', 'MGR004', 0, NULL, '2023-08-10', NULL, 850000.00, '2336', NULL, 'DEP4', 'TE01', 'CPG03', 'C2', 1),
('INF137', '2023-03-05', NOW(), NULL, 'POST019', 'CONT001', 'EMP014', 'MGR004', 0, NULL, '2023-09-05', NULL, 1250000.00, '2337', NULL, 'DEP4', 'TE01', 'CPG04', 'D1', 1),
('INF138', '2023-04-20', NOW(), NULL, 'POST016', 'CONT001', 'EMP023', 'MGR004', 0, NULL, '2023-10-20', NULL, 700000.00, '2338', NULL, 'DEP4', 'TE01', 'CPG02', 'C1', 1),
('INF139', '2023-05-15', NOW(), NULL, 'POST018', 'CONT001', 'EMP027', 'MGR004', 0, NULL, '2023-11-15', NULL, 1450000.00, '2339', NULL, 'DEP4', 'TE01', 'CPG05', 'B1', 1),
('INF140', '2023-06-10', NOW(), NULL, 'POST016', 'CONT001', 'EMP031', 'MGR004', 0, NULL, '2023-12-10', NULL, 700000.00, '2340', NULL, 'DEP4', 'TE01', 'CPG02', 'C1', 1),
('INF141', '2023-07-05', NOW(), NULL, 'POST017', 'CONT001', 'EMP042', 'MGR004', 0, NULL, '2024-01-05', NULL, 850000.00, '2341', NULL, 'DEP4', 'TE01', 'CPG03', 'C2', 1),
('INF142', '2023-08-10', NOW(), NULL, 'POST019', 'CONT001', 'EMP046', 'MGR004', 0, NULL, '2024-02-10', NULL, 1230000.00, '2342', NULL, 'DEP4', 'TE01', 'CPG04', 'D1', 1),
('INF143', '2023-09-05', NOW(), NULL, 'POST020', 'CONT001', 'EMP050', 'MGR004', 0, NULL, '2024-03-05', NULL, 1550000.00, '2343', NULL, 'DEP4', 'TE01', 'CPG05', 'B1', 1),
('INF144', '2023-10-15', NOW(), NULL, 'POST016', 'CONT001', 'EMP054', 'MGR004', 0, NULL, '2024-04-15', NULL, 670000.00, '2344', NULL, 'DEP4', 'TE01', 'CPG02', 'C1', 1),
('INF145', '2023-11-20', NOW(), NULL, 'POST017', 'CONT001', 'EMP060', 'MGR004', 0, NULL, '2024-05-20', NULL, 870000.00, '2345', NULL, 'DEP4', 'TE01', 'CPG03', 'C2', 1),
('INF146', '2024-01-05', NOW(), NULL, 'POST018', 'CONT001', 'EMP064', 'MGR004', 0, NULL, '2024-07-05', NULL, 1430000.00, '2346', NULL, 'DEP4', 'TE09', 'CPG05', 'B1', 1),
('INF147', '2024-02-20', NOW(), NULL, 'POST016', 'CONT001', 'EMP071', 'MGR004', 0, NULL, '2024-08-20', NULL, 680000.00, '2347', NULL, 'DEP4', 'TE01', 'CPG02', 'C1', 1),
('INF148', '2024-03-10', NOW(), NULL, 'POST017', 'CONT001', 'EMP073', 'MGR004', 0, NULL, '2024-09-10', NULL, 870000.00, '2348', NULL, 'DEP4', 'TE01', 'CPG03', 'C2', 1),

-- DEPARTEMENT OPERATIONS (DEP5) - 14 CDI
('INF149', '2023-01-25', NOW(), NULL, 'POST025', 'CONT001', 'EMP022', 'MGR005', 0, NULL, '2023-07-25', NULL, 1550000.00, '2349', NULL, 'DEP5', 'TE01', 'CPG05', 'B1', 1),
('INF150', '2023-02-15', NOW(), NULL, 'POST022', 'CONT001', 'EMP004', 'MGR005', 0, NULL, '2023-08-15', NULL, 950000.00, '2350', NULL, 'DEP5', 'TE01', 'CPG03', 'C2', 1),
('INF151', '2023-03-20', NOW(), NULL, 'POST024', 'CONT001', 'EMP011', 'MGR005', 0, NULL, '2023-09-20', NULL, 1300000.00, '2351', NULL, 'DEP5', 'TE01', 'CPG04', 'D1', 1),
('INF152', '2023-04-10', NOW(), NULL, 'POST021', 'CONT001', 'EMP017', 'MGR005', 0, NULL, '2023-10-10', NULL, 700000.00, '2352', NULL, 'DEP5', 'TE01', 'CPG02', 'C1', 1),
('INF153', '2023-05-25', NOW(), NULL, 'POST023', 'CONT001', 'EMP024', 'MGR005', 0, NULL, '2023-11-25', NULL, 1400000.00, '2353', NULL, 'DEP5', 'TE01', 'CPG05', 'B1', 1),
('INF154', '2023-06-05', NOW(), NULL, 'POST021', 'CONT001', 'EMP032', 'MGR005', 0, NULL, '2023-12-05', NULL, 700000.00, '2354', NULL, 'DEP5', 'TE01', 'CPG02', 'C1', 1),
('INF155', '2023-07-15', NOW(), NULL, 'POST022', 'CONT001', 'EMP033', 'MGR005', 0, NULL, '2024-01-15', NULL, 900000.00, '2355', NULL, 'DEP5', 'TE01', 'CPG03', 'C2', 1),
('INF156', '2023-08-20', NOW(), NULL, 'POST024', 'CONT001', 'EMP035', 'MGR005', 0, NULL, '2024-02-20', NULL, 1270000.00, '2356', NULL, 'DEP5', 'TE01', 'CPG04', 'D1', 1),
('INF157', '2023-09-25', NOW(), NULL, 'POST025', 'CONT001', 'EMP043', 'MGR005', 0, NULL, '2024-03-25', NULL, 1500000.00, '2357', NULL, 'DEP5', 'TE01', 'CPG05', 'B1', 1),
('INF158', '2023-10-30', NOW(), NULL, 'POST021', 'CONT001', 'EMP047', 'MGR005', 0, NULL, '2024-04-30', NULL, 690000.00, '2358', NULL, 'DEP5', 'TE01', 'CPG02', 'C1', 1),
('INF159', '2023-11-15', NOW(), NULL, 'POST022', 'CONT001', 'EMP051', 'MGR005', 0, NULL, '2024-05-15', NULL, 930000.00, '2359', NULL, 'DEP5', 'TE01', 'CPG03', 'C2', 1),
('INF160', '2024-01-20', NOW(), NULL, 'POST023', 'CONT001', 'EMP055', 'MGR005', 0, NULL, '2024-07-20', NULL, 1370000.00, '2360', NULL, 'DEP5', 'TE01', 'CPG05', 'B1', 1),
('INF161', '2024-02-25', NOW(), NULL, 'POST021', 'CONT001', 'EMP061', 'MGR005', 0, NULL, '2024-08-25', NULL, 690000.00, '2361', NULL, 'DEP5', 'TE01', 'CPG02', 'C1', 1),
('INF162', '2024-03-30', NOW(), NULL, 'POST022', 'CONT001', 'EMP068', 'MGR005', 0, NULL, '2024-09-30', NULL, 910000.00, '2362', NULL, 'DEP5', 'TE01', 'CPG03', 'C2', 1),

-- DEPARTEMENT DIRECTION GENERALE (DEP338BD413) - 8 CDI
('INF163', '2023-01-01', NOW(), NULL, 'POST026', 'CONT001', 'EMP001', 'MGR006', 0, NULL, '2023-07-01', NULL, 3800000.00, '2363', NULL, 'DEP338BD413', 'TE01', 'CPG05', 'A1', 1),
('INF164', '2023-02-01', NOW(), NULL, 'POST027', 'CONT001', 'EMP010', 'MGR006', 0, NULL, '2023-08-01', NULL, 650000.00, '2364', NULL, 'DEP338BD413', 'TE01', 'CPG02', 'C1', 1),
('INF165', '2023-03-01', NOW(), NULL, 'POST028', 'CONT001', 'EMP016', 'MGR006', 0, NULL, '2023-09-01', NULL, 700000.00, '2365', NULL, 'DEP338BD413', 'TE01', 'CPG02', 'C1', 1),
('INF166', '2023-04-01', NOW(), NULL, 'POST033', 'CONT001', 'EMP018', 'MGR006', 0, NULL, '2023-10-01', NULL, 1900000.00, '2366', NULL, 'DEP338BD413', 'TE01', 'CPG05', 'B1', 1),
('INF167', '2023-05-15', NOW(), NULL, 'POST028', 'CONT001', 'EMP020', 'MGR006', 0, NULL, '2023-11-15', NULL, 650000.00, '2367', NULL, 'DEP338BD413', 'TE01', 'CPG02', 'C1', 1),
('INF168', '2023-07-01', NOW(), NULL, 'POST027', 'CONT001', 'EMP021', 'MGR006', 0, NULL, '2024-01-01', NULL, 670000.00, '2368', NULL, 'DEP338BD413', 'TE01', 'CPG02', 'C1', 1),
('INF169', '2023-09-01', NOW(), NULL, 'POST033', 'CONT001', 'EMP026', 'MGR006', 0, NULL, '2024-03-01', NULL, 1850000.00, '2369', NULL, 'DEP338BD413', 'TE01', 'CPG05', 'B1', 1),
('INF170', '2024-01-10', NOW(), NULL, 'POST028', 'CONT001', 'EMP029', 'MGR006', 0, NULL, '2024-07-10', NULL, 670000.00, '2370', NULL, 'DEP338BD413', 'TE01', 'CPG02', 'C1', 1);

-- ==================== EMPLOYES EN CDD (30 lignes) ====================

INSERT INTO infos_professionnelles (id, date_embauche, created_at, modified_at, id_poste, id_type_contrat, id_employe, id_manager, statut, date_debauche, date_debut_assignation_poste, date_fin_assignation_poste, salaire_base, matricule, motif_depart, id_departement, id_type_entree, id_categorie, classification, id_temps_travail) VALUES
-- DEPARTEMENT MARKETING (DEP2) - 6 CDD
('INF201', '2024-06-01', NOW(), NULL, 'POST011', 'CONT002', 'EMP034', 'MGR002', 0, NULL, '2024-06-01', '2024-11-30', 600000.00, '2371', NULL, 'DEP2', 'TE01', 'CPG02', 'C1', 1),
('INF202', '2024-07-01', NOW(), NULL, 'POST012', 'CONT002', 'EMP056', 'MGR002', 0, NULL, '2024-07-01', '2024-12-31', 800000.00, '2372', NULL, 'DEP2', 'TE01', 'CPG03', 'C2', 1),
('INF203', '2024-08-01', NOW(), NULL, 'POST011', 'CONT002', 'EMP065', 'MGR002', 0, NULL, '2024-08-01', '2025-01-31', 620000.00, '2373', NULL, 'DEP2', 'TE01', 'CPG02', 'C1', 1),
('INF204', '2024-09-01', NOW(), NULL, 'POST012', 'CONT002', 'EMP074', 'MGR002', 0, NULL, '2024-09-01', '2025-02-28', 820000.00, '2374', NULL, 'DEP2', 'TE01', 'CPG03', 'C2', 1),
('INF205', '2024-10-01', NOW(), NULL, 'POST011', 'CONT002', 'EMP077', 'MGR002', 0, NULL, '2024-10-01', '2025-03-31', 630000.00, '2375', NULL, 'DEP2', 'TE01', 'CPG02', 'C1', 1),
('INF206', '2024-11-01', NOW(), NULL, 'POST012', 'CONT002', 'EMP078', 'MGR002', 0, NULL, '2024-11-01', '2025-04-30', 830000.00, '2376', NULL, 'DEP2', 'TE01', 'CPG03', 'C2', 1),

-- DEPARTEMENT INFORMATIQUE (DEP3) - 6 CDD
('INF207', '2024-06-15', NOW(), NULL, 'POST006', 'CONT002', 'EMP080', 'MGR003', 0, NULL, '2024-06-15', '2024-12-14', 620000.00, '2377', NULL, 'DEP3', 'TE01', 'CPG02', 'C1', 1),
('INF208', '2024-07-15', NOW(), NULL, 'POST007', 'CONT002', 'EMP083', 'MGR003', 0, NULL, '2024-07-15', '2025-01-14', 920000.00, '2378', NULL, 'DEP3', 'TE01', 'CPG03', 'C2', 1),
('INF209', '2024-08-15', NOW(), NULL, 'POST006', 'CONT002', 'EMP084', 'MGR003', 0, NULL, '2024-08-15', '2025-02-14', 630000.00, '2379', NULL, 'DEP3', 'TE01', 'CPG02', 'C1', 1),
('INF210', '2024-09-15', NOW(), NULL, 'POST007', 'CONT002', 'EMP089', 'MGR003', 0, NULL, '2024-09-15', '2025-03-14', 930000.00, '2380', NULL, 'DEP3', 'TE01', 'CPG03', 'C2', 1),
('INF211', '2024-10-15', NOW(), NULL, 'POST006', 'CONT002', 'EMP090', 'MGR003', 0, NULL, '2024-10-15', '2025-04-14', 640000.00, '2381', NULL, 'DEP3', 'TE01', 'CPG02', 'C1', 1),
('INF212', '2024-11-15', NOW(), NULL, 'POST007', 'CONT002', 'EMP091', 'MGR003', 0, NULL, '2024-11-15', '2025-05-14', 940000.00, '2382', NULL, 'DEP3', 'TE01', 'CPG03', 'C2', 1),

-- DEPARTEMENT FINANCE (DEP4) - 6 CDD
('INF213', '2024-06-01', NOW(), NULL, 'POST016', 'CONT002', 'EMP092', 'MGR004', 0, NULL, '2024-06-01', '2024-11-30', 620000.00, '2383', NULL, 'DEP4', 'TE01', 'CPG02', 'C1', 1),
('INF214', '2024-07-01', NOW(), NULL, 'POST017', 'CONT002', 'EMP093', 'MGR004', 0, NULL, '2024-07-01', '2024-12-31', 820000.00, '2384', NULL, 'DEP4', 'TE01', 'CPG03', 'C2', 1),
('INF215', '2024-08-01', NOW(), NULL, 'POST016', 'CONT002', 'EMP094', 'MGR004', 0, NULL, '2024-08-01', '2025-01-31', 630000.00, '2385', NULL, 'DEP4', 'TE01', 'CPG02', 'C1', 1),
('INF216', '2024-09-01', NOW(), NULL, 'POST017', 'CONT002', 'EMP095', 'MGR004', 0, NULL, '2024-09-01', '2025-02-28', 830000.00, '2386', NULL, 'DEP4', 'TE01', 'CPG03', 'C2', 1),
('INF217', '2024-10-01', NOW(), NULL, 'POST016', 'CONT002', 'EMP096', 'MGR004', 0, NULL, '2024-10-01', '2025-03-31', 640000.00, '2387', NULL, 'DEP4', 'TE01', 'CPG02', 'C1', 1),
('INF218', '2024-11-01', NOW(), NULL, 'POST017', 'CONT002', 'EMP097', 'MGR004', 0, NULL, '2024-11-01', '2025-04-30', 840000.00, '2388', NULL, 'DEP4', 'TE01', 'CPG03', 'C2', 1),

-- DEPARTEMENT OPERATIONS (DEP5) - 6 CDD
('INF219', '2024-06-10', NOW(), NULL, 'POST021', 'CONT002', 'EMP098', 'MGR005', 0, NULL, '2024-06-10', '2024-12-09', 620000.00, '2389', NULL, 'DEP5', 'TE01', 'CPG02', 'C1', 1),
('INF220', '2024-07-10', NOW(), NULL, 'POST022', 'CONT002', 'EMP099', 'MGR005', 0, NULL, '2024-07-10', '2025-01-09', 850000.00, '2390', NULL, 'DEP5', 'TE01', 'CPG03', 'C2', 1),
('INF221', '2024-08-10', NOW(), NULL, 'POST021', 'CONT002', 'EMP100', 'MGR005', 0, NULL, '2024-08-10', '2025-02-09', 630000.00, '2391', NULL, 'DEP5', 'TE01', 'CPG02', 'C1', 1),
('INF222', '2024-09-10', NOW(), NULL, 'POST022', 'CONT002', 'EMP101', 'MGR005', 0, NULL, '2024-09-10', '2025-03-09', 860000.00, '2392', NULL, 'DEP5', 'TE01', 'CPG03', 'C2', 1),
('INF223', '2024-10-10', NOW(), NULL, 'POST021', 'CONT002', 'EMP102', 'MGR005', 0, NULL, '2024-10-10', '2025-04-09', 640000.00, '2393', NULL, 'DEP5', 'TE01', 'CPG02', 'C1', 1),
('INF224', '2024-11-10', NOW(), NULL, 'POST022', 'CONT002', 'EMP103', 'MGR005', 0, NULL, '2024-11-10', '2025-05-09', 870000.00, '2394', NULL, 'DEP5', 'TE01', 'CPG03', 'C2', 1),

-- DEPARTEMENT INTENDANCE (DEP7C3861D7) - 6 CDD
('INF225', '2024-06-01', NOW(), NULL, 'POST030', 'CONT002', 'EMP104', 'MGR007', 0, NULL, '2024-06-01', '2024-11-30', 400000.00, '2395', NULL, 'DEP7C3861D7', 'TE01', 'CPG01', 'E1', 1),
('INF226', '2024-07-01', NOW(), NULL, 'POST031', 'CONT002', 'EMP105', 'MGR007', 0, NULL, '2024-07-01', '2024-12-31', 350000.00, '2396', NULL, 'DEP7C3861D7', 'TE01', 'CPG01', 'E1', 1),
('INF227', '2024-08-01', NOW(), NULL, 'POST032', 'CONT002', 'EMP106', 'MGR007', 0, NULL, '2024-08-01', '2025-01-31', 350000.00, '2397', NULL, 'DEP7C3861D7', 'TE01', 'CPG01', 'E1', 1),
('INF228', '2024-09-01', NOW(), NULL, 'POST030', 'CONT002', 'EMP107', 'MGR007', 0, NULL, '2024-09-01', '2025-02-28', 400000.00, '2398', NULL, 'DEP7C3861D7', 'TE01', 'CPG01', 'E1', 1),
('INF229', '2024-10-01', NOW(), NULL, 'POST031', 'CONT002', 'EMP108', 'MGR007', 0, NULL, '2024-10-01', '2025-03-31', 380000.00, '2399', NULL, 'DEP7C3861D7', 'TE01', 'CPG01', 'E1', 1),
('INF230', '2024-11-01', NOW(), NULL, 'POST032', 'CONT002', 'EMP109', 'MGR007', 0, NULL, '2024-11-01', '2025-04-30', 360000.00, '2400', NULL, 'DEP7C3861D7', 'TE01', 'CPG01', 'E1', 1);

-- ==================== EMPLOYES INACTIFS (10) ====================

INSERT INTO infos_professionnelles (id, date_embauche, created_at, modified_at, id_poste, id_type_contrat, id_employe, id_manager, statut, date_debauche, date_debut_assignation_poste, date_fin_assignation_poste, salaire_base, matricule, motif_depart, id_departement, id_type_entree, id_categorie, classification, id_temps_travail) VALUES
('INF301', '2022-03-01', NOW(), NULL, 'POST012', 'CONT001', 'EMP110', 'MGR002', 1, '2023-12-31', '2022-03-01', '2023-12-31', 800000.00, '2401', 'Démission volontaire', 'DEP2', 'TE01', 'CPG03', 'C2', 1),
('INF302', '2022-05-15', NOW(), NULL, 'POST007', 'CONT001', 'EMP111', 'MGR003', 1, '2024-01-15', '2022-05-15', '2024-01-15', 950000.00, '2402', 'Fin de contrat CDD non renouvelé', 'DEP3', 'TE01', 'CPG03', 'C2', 1),
('INF303', '2022-07-01', NOW(), NULL, 'POST017', 'CONT001', 'EMP112', 'MGR004', 1, '2024-02-28', '2022-07-01', '2024-02-28', 820000.00, '2403', 'Rupture conventionnelle', 'DEP4', 'TE01', 'CPG03', 'C2', 1),
('INF304', '2022-09-10', NOW(), NULL, 'POST022', 'CONT001', 'EMP113', 'MGR005', 1, '2024-03-15', '2022-09-10', '2024-03-15', 880000.00, '2404', 'Licenciement économique', 'DEP5', 'TE01', 'CPG03', 'C2', 1),
('INF305', '2022-11-20', NOW(), NULL, 'POST026', 'CONT001', 'EMP114', 'MGR006', 1, '2024-04-30', '2022-11-20', '2024-04-30', 3500000.00, '2405', 'Départ à la retraite', 'DEP338BD413', 'TE01', 'CPG05', 'A1', 1),
('INF306', '2023-01-05', NOW(), NULL, 'POST030', 'CONT001', 'EMP115', 'MGR007', 1, '2024-05-20', '2023-01-05', '2024-05-20', 400000.00, '2406', 'Démission volontaire', 'DEP7C3861D7', 'TE01', 'CPG01', 'E1', 1),
('INF307', '2023-02-10', NOW(), NULL, 'POST011', 'CONT001', 'EMP116', 'MGR002', 1, '2024-06-10', '2023-02-10', '2024-06-10', 600000.00, '2407', 'Fin de période d''essai non validée', 'DEP2', 'TE01', 'CPG02', 'C1', 1),
('INF308', '2023-03-15', NOW(), NULL, 'POST006', 'CONT001', 'EMP117', 'MGR003', 1, '2024-07-15', '2023-03-15', '2024-07-15', 650000.00, '2408', 'Mutation externe', 'DEP3', 'TE01', 'CPG02', 'C1', 1),
('INF309', '2023-04-20', NOW(), NULL, 'POST016', 'CONT001', 'EMP118', 'MGR004', 1, '2024-08-20', '2023-04-20', '2024-08-20', 650000.00, '2409', 'Rupture conventionnelle', 'DEP4', 'TE01', 'CPG02', 'C1', 1),
('INF310', '2023-05-25', NOW(), NULL, 'POST021', 'CONT001', 'EMP119', 'MGR005', 1, '2024-09-25', '2023-05-25', '2024-09-25', 650000.00, '2410', 'Démission volontaire', 'DEP5', 'TE01', 'CPG02', 'C1', 1);

-- Users (types mis a jour)
INSERT INTO users (id, email, password, created_at, modified_at, statut, id_type_user, id_employe) VALUES ('USR-TEST-0001', 'fanja.raharinja1@gmail.com', 'password123', 'NOW()', 'NOW()', '1', '1', 'EMP-TEST-0001');
INSERT INTO users (id, email, password, created_at, modified_at, statut, id_type_user, id_employe) VALUES ('USR-TEST-0002', 'rado.rasolo2@gmail.com', 'password123', 'NOW()', 'NOW()', '1', '1', 'EMP-TEST-0002');
INSERT INTO users (id, email, password, created_at, modified_at, statut, id_type_user, id_employe) VALUES ('USR-TEST-0003', 'kanto.randria3@gmail.com', 'password123', 'NOW()', 'NOW()', '1', '1', 'EMP-TEST-0003');
INSERT INTO users (id, email, password, created_at, modified_at, statut, id_type_user, id_employe) VALUES ('USR-TEST-0004', 'fetra.ravelomanana4@gmail.com', 'password123', 'NOW()', 'NOW()', '1', '2', 'EMP-TEST-0004');
INSERT INTO users (id, email, password, created_at, modified_at, statut, id_type_user, id_employe) VALUES ('USR-TEST-0005', 'faneva.rasolo5@gmail.com', 'password123', 'NOW()', 'NOW()', '1', '3', 'EMP-TEST-0005');
INSERT INTO users (id, email, password, created_at, modified_at, statut, id_type_user, id_employe) VALUES ('USR-TEST-0006', 'mamy.randrianarisoa6@gmail.com', 'password123', 'NOW()', 'NOW()', '1', '3', 'EMP-TEST-0006');
INSERT INTO users (id, email, password, created_at, modified_at, statut, id_type_user, id_employe) VALUES ('USR-TEST-0007', 'mina.ranaivo7@gmail.com', 'password123', 'NOW()', 'NOW()', '1', '3', 'EMP-TEST-0007');
INSERT INTO users (id, email, password, created_at, modified_at, statut, id_type_user, id_employe) VALUES ('USR-TEST-0008', 'tiana.razanakoto8@gmail.com', 'password123', 'NOW()', 'NOW()', '1', '3', 'EMP-TEST-0008');
INSERT INTO users (id, email, password, created_at, modified_at, statut, id_type_user, id_employe) VALUES ('USR-TEST-0009', 'lala.andrianina9@gmail.com', 'password123', 'NOW()', 'NOW()', '1', '3', 'EMP-TEST-0009');
INSERT INTO users (id, email, password, created_at, modified_at, statut, id_type_user, id_employe) VALUES ('USR-TEST-0010', 'hery.razafindraibe10@gmail.com', 'password123', 'NOW()', 'NOW()', '1', '3', 'EMP-TEST-0010');
INSERT INTO users (id, email, password, created_at, modified_at, statut, id_type_user, id_employe) VALUES ('USR-TEST-0011', 'mina.randria11@gmail.com', 'password123', 'NOW()', 'NOW()', '1', '4', 'EMP-TEST-0011');
INSERT INTO users (id, email, password, created_at, modified_at, statut, id_type_user, id_employe) VALUES ('USR-TEST-0012', 'rado.ramaroson12@gmail.com', 'password123', 'NOW()', 'NOW()', '1', '3', 'EMP-TEST-0012');
INSERT INTO users (id, email, password, created_at, modified_at, statut, id_type_user, id_employe) VALUES ('USR-TEST-0013', 'faneva.andrianina13@gmail.com', 'password123', 'NOW()', 'NOW()', '1', '3', 'EMP-TEST-0013');
INSERT INTO users (id, email, password, created_at, modified_at, statut, id_type_user, id_employe) VALUES ('USR-TEST-0014', 'mina.rakoto14@gmail.com', 'password123', 'NOW()', 'NOW()', '1', '3', 'EMP-TEST-0014');
INSERT INTO users (id, email, password, created_at, modified_at, statut, id_type_user, id_employe) VALUES ('USR-TEST-0015', 'rado.ravelomanana15@gmail.com', 'password123', 'NOW()', 'NOW()', '1', '3', 'EMP-TEST-0015');
INSERT INTO users (id, email, password, created_at, modified_at, statut, id_type_user, id_employe) VALUES ('USR-TEST-0016', 'lala.rasoanaivo16@gmail.com', 'password123', 'NOW()', 'NOW()', '1', '3', 'EMP-TEST-0016');
INSERT INTO users (id, email, password, created_at, modified_at, statut, id_type_user, id_employe) VALUES ('USR-TEST-0017', 'mamy.rabe17@gmail.com', 'password123', 'NOW()', 'NOW()', '1', '3', 'EMP-TEST-0017');
INSERT INTO users (id, email, password, created_at, modified_at, statut, id_type_user, id_employe) VALUES ('USR-TEST-0018', 'soa.razafy18@gmail.com', 'password123', 'NOW()', 'NOW()', '1', '3', 'EMP-TEST-0018');
INSERT INTO users (id, email, password, created_at, modified_at, statut, id_type_user, id_employe) VALUES ('USR-TEST-0019', 'mina.rakoto19@gmail.com', 'password123', 'NOW()', 'NOW()', '1', '3', 'EMP-TEST-0019');
INSERT INTO users (id, email, password, created_at, modified_at, statut, id_type_user, id_employe) VALUES ('USR-TEST-0020', 'hery.rasolo20@gmail.com', 'password123', 'NOW()', 'NOW()', '1', '3', 'EMP-TEST-0020');
INSERT INTO users (id, email, password, created_at, modified_at, statut, id_type_user, id_employe) VALUES ('USR-TEST-0021', 'niry.razafy21@gmail.com', 'password123', 'NOW()', 'NOW()', '1', '3', 'EMP-TEST-0021');
INSERT INTO users (id, email, password, created_at, modified_at, statut, id_type_user, id_employe) VALUES ('USR-TEST-0022', 'rindra.razafindraibe22@gmail.com', 'password123', 'NOW()', 'NOW()', '1', '3', 'EMP-TEST-0022');
INSERT INTO users (id, email, password, created_at, modified_at, statut, id_type_user, id_employe) VALUES ('USR-TEST-0023', 'mamy.ravelomanana23@gmail.com', 'password123', 'NOW()', 'NOW()', '1', '3', 'EMP-TEST-0023');
INSERT INTO users (id, email, password, created_at, modified_at, statut, id_type_user, id_employe) VALUES ('USR-TEST-0024', 'rindra.randrianarisoa24@gmail.com', 'password123', 'NOW()', 'NOW()', '1', '3', 'EMP-TEST-0024');
INSERT INTO users (id, email, password, created_at, modified_at, statut, id_type_user, id_employe) VALUES ('USR-TEST-0025', 'tiana.ravelo25@gmail.com', 'password123', 'NOW()', 'NOW()', '1', '3', 'EMP-TEST-0025');
INSERT INTO users (id, email, password, created_at, modified_at, statut, id_type_user, id_employe) VALUES ('USR-TEST-0026', 'rado.andrianina26@gmail.com', 'password123', 'NOW()', 'NOW()', '1', '3', 'EMP-TEST-0026');
INSERT INTO users (id, email, password, created_at, modified_at, statut, id_type_user, id_employe) VALUES ('USR-TEST-0027', 'sitraka.rakotovao27@gmail.com', 'password123', 'NOW()', 'NOW()', '1', '3', 'EMP-TEST-0027');
INSERT INTO users (id, email, password, created_at, modified_at, statut, id_type_user, id_employe) VALUES ('USR-TEST-0028', 'nomena.andriam28@gmail.com', 'password123', 'NOW()', 'NOW()', '1', '3', 'EMP-TEST-0028');
INSERT INTO users (id, email, password, created_at, modified_at, statut, id_type_user, id_employe) VALUES ('USR-TEST-0029', 'rado.razafindraibe29@gmail.com', 'password123', 'NOW()', 'NOW()', '1', '3', 'EMP-TEST-0029');
INSERT INTO users (id, email, password, created_at, modified_at, statut, id_type_user, id_employe) VALUES ('USR-TEST-0030', 'aina.razanakoto30@gmail.com', 'password123', 'NOW()', 'NOW()', '1', '3', 'EMP-TEST-0030');
INSERT INTO users (id, email, password, created_at, modified_at, statut, id_type_user, id_employe) VALUES ('USR-TEST-0031', 'fanja.raharinja31@gmail.com', 'password123', 'NOW()', 'NOW()', '1', '3', 'EMP-TEST-0031');
INSERT INTO users (id, email, password, created_at, modified_at, statut, id_type_user, id_employe) VALUES ('USR-TEST-0032', 'lova.rasoanaivo32@gmail.com', 'password123', 'NOW()', 'NOW()', '1', '3', 'EMP-TEST-0032');
INSERT INTO users (id, email, password, created_at, modified_at, statut, id_type_user, id_employe) VALUES ('USR-TEST-0033', 'soa.ramaroson33@gmail.com', 'password123', 'NOW()', 'NOW()', '1', '3', 'EMP-TEST-0033');
INSERT INTO users (id, email, password, created_at, modified_at, statut, id_type_user, id_employe) VALUES ('USR-TEST-0034', 'tahina.rabe34@gmail.com', 'password123', 'NOW()', 'NOW()', '1', '3', 'EMP-TEST-0034');
INSERT INTO users (id, email, password, created_at, modified_at, statut, id_type_user, id_employe) VALUES ('USR-TEST-0035', 'mamy.rasoanaivo35@gmail.com', 'password123', 'NOW()', 'NOW()', '1', '3', 'EMP-TEST-0035');
INSERT INTO users (id, email, password, created_at, modified_at, statut, id_type_user, id_employe) VALUES ('USR-TEST-0036', 'lala.rakoto36@gmail.com', 'password123', 'NOW()', 'NOW()', '1', '3', 'EMP-TEST-0036');
INSERT INTO users (id, email, password, created_at, modified_at, statut, id_type_user, id_employe) VALUES ('USR-TEST-0037', 'rindra.ranaivo37@gmail.com', 'password123', 'NOW()', 'NOW()', '1', '3', 'EMP-TEST-0037');
INSERT INTO users (id, email, password, created_at, modified_at, statut, id_type_user, id_employe) VALUES ('USR-TEST-0038', 'nomena.andrianina38@gmail.com', 'password123', 'NOW()', 'NOW()', '1', '3', 'EMP-TEST-0038');
INSERT INTO users (id, email, password, created_at, modified_at, statut, id_type_user, id_employe) VALUES ('USR-TEST-0039', 'tiana.ranaivo39@gmail.com', 'password123', 'NOW()', 'NOW()', '1', '3', 'EMP-TEST-0039');
INSERT INTO users (id, email, password, created_at, modified_at, statut, id_type_user, id_employe) VALUES ('USR-TEST-0040', 'soa.rasoanaivo40@gmail.com', 'password123', 'NOW()', 'NOW()', '1', '3', 'EMP-TEST-0040');
INSERT INTO users (id, email, password, created_at, modified_at, statut, id_type_user, id_employe) VALUES ('USR-TEST-0041', 'sitraka.rakoto41@gmail.com', 'password123', 'NOW()', 'NOW()', '1', '3', 'EMP-TEST-0041');
INSERT INTO users (id, email, password, created_at, modified_at, statut, id_type_user, id_employe) VALUES ('USR-TEST-0042', 'niry.ravelo42@gmail.com', 'password123', 'NOW()', 'NOW()', '1', '3', 'EMP-TEST-0042');
INSERT INTO users (id, email, password, created_at, modified_at, statut, id_type_user, id_employe) VALUES ('USR-TEST-0043', 'lova.rajaonarison43@gmail.com', 'password123', 'NOW()', 'NOW()', '1', '3', 'EMP-TEST-0043');
INSERT INTO users (id, email, password, created_at, modified_at, statut, id_type_user, id_employe) VALUES ('USR-TEST-0044', 'niry.randrianarisoa44@gmail.com', 'password123', 'NOW()', 'NOW()', '1', '3', 'EMP-TEST-0044');
INSERT INTO users (id, email, password, created_at, modified_at, statut, id_type_user, id_employe) VALUES ('USR-TEST-0045', 'lova.ratsimbazafy45@gmail.com', 'password123', 'NOW()', 'NOW()', '1', '3', 'EMP-TEST-0045');
INSERT INTO users (id, email, password, created_at, modified_at, statut, id_type_user, id_employe) VALUES ('USR-TEST-0046', 'kanto.rakotovao46@gmail.com', 'password123', 'NOW()', 'NOW()', '1', '2', 'EMP-TEST-0046');
INSERT INTO users (id, email, password, created_at, modified_at, statut, id_type_user, id_employe) VALUES ('USR-TEST-0047', 'tahina.ramaroson47@gmail.com', 'password123', 'NOW()', 'NOW()', '1', '3', 'EMP-TEST-0047');
INSERT INTO users (id, email, password, created_at, modified_at, statut, id_type_user, id_employe) VALUES ('USR-TEST-0048', 'mina.ravelo48@gmail.com', 'password123', 'NOW()', 'NOW()', '1', '3', 'EMP-TEST-0048');
INSERT INTO users (id, email, password, created_at, modified_at, statut, id_type_user, id_employe) VALUES ('USR-TEST-0049', 'niry.razafy49@gmail.com', 'password123', 'NOW()', 'NOW()', '1', '3', 'EMP-TEST-0049');
INSERT INTO users (id, email, password, created_at, modified_at, statut, id_type_user, id_employe) VALUES ('USR-TEST-0050', 'mamy.ratsimbazafy50@gmail.com', 'password123', 'NOW()', 'NOW()', '1', '3', 'EMP-TEST-0050');
INSERT INTO users (id, email, password, created_at, modified_at, statut, id_type_user, id_employe) VALUES ('USR-TEST-0051', 'mamy.rajaonarison51@gmail.com', 'password123', 'NOW()', 'NOW()', '1', '3', 'EMP-TEST-0051');
INSERT INTO users (id, email, password, created_at, modified_at, statut, id_type_user, id_employe) VALUES ('USR-TEST-0052', 'mamy.rakoto52@gmail.com', 'password123', 'NOW()', 'NOW()', '1', '3', 'EMP-TEST-0052');
INSERT INTO users (id, email, password, created_at, modified_at, statut, id_type_user, id_employe) VALUES ('USR-TEST-0053', 'hery.rakotovao53@gmail.com', 'password123', 'NOW()', 'NOW()', '1', '3', 'EMP-TEST-0053');
INSERT INTO users (id, email, password, created_at, modified_at, statut, id_type_user, id_employe) VALUES ('USR-TEST-0054', 'fetra.ramaroson54@gmail.com', 'password123', 'NOW()', 'NOW()', '1', '3', 'EMP-TEST-0054');
INSERT INTO users (id, email, password, created_at, modified_at, statut, id_type_user, id_employe) VALUES ('USR-TEST-0055', 'tahina.randria55@gmail.com', 'password123', 'NOW()', 'NOW()', '1', '3', 'EMP-TEST-0055');
INSERT INTO users (id, email, password, created_at, modified_at, statut, id_type_user, id_employe) VALUES ('USR-TEST-0056', 'niry.ravelomanana56@gmail.com', 'password123', 'NOW()', 'NOW()', '1', '3', 'EMP-TEST-0056');
INSERT INTO users (id, email, password, created_at, modified_at, statut, id_type_user, id_employe) VALUES ('USR-TEST-0057', 'rado.ravelo57@gmail.com', 'password123', 'NOW()', 'NOW()', '1', '3', 'EMP-TEST-0057');
INSERT INTO users (id, email, password, created_at, modified_at, statut, id_type_user, id_employe) VALUES ('USR-TEST-0058', 'sitraka.rakoto58@gmail.com', 'password123', 'NOW()', 'NOW()', '1', '3', 'EMP-TEST-0058');
INSERT INTO users (id, email, password, created_at, modified_at, statut, id_type_user, id_employe) VALUES ('USR-TEST-0059', 'sitraka.andriam59@gmail.com', 'password123', 'NOW()', 'NOW()', '1', '3', 'EMP-TEST-0059');
INSERT INTO users (id, email, password, created_at, modified_at, statut, id_type_user, id_employe) VALUES ('USR-TEST-0060', 'mina.raharinja60@gmail.com', 'password123', 'NOW()', 'NOW()', '1', '3', 'EMP-TEST-0060');
INSERT INTO users (id, email, password, created_at, modified_at, statut, id_type_user, id_employe) VALUES ('USR-TEST-0061', 'soa.razafy61@gmail.com', 'password123', 'NOW()', 'NOW()', '1', '3', 'EMP-TEST-0061');
INSERT INTO users (id, email, password, created_at, modified_at, statut, id_type_user, id_employe) VALUES ('USR-TEST-0062', 'rado.randrianarisoa62@gmail.com', 'password123', 'NOW()', 'NOW()', '1', '3', 'EMP-TEST-0062');
INSERT INTO users (id, email, password, created_at, modified_at, statut, id_type_user, id_employe) VALUES ('USR-TEST-0063', 'kanto.randrianarisoa63@gmail.com', 'password123', 'NOW()', 'NOW()', '1', '3', 'EMP-TEST-0063');
INSERT INTO users (id, email, password, created_at, modified_at, statut, id_type_user, id_employe) VALUES ('USR-TEST-0064', 'rado.raharimalala64@gmail.com', 'password123', 'NOW()', 'NOW()', '1', '3', 'EMP-TEST-0064');
INSERT INTO users (id, email, password, created_at, modified_at, statut, id_type_user, id_employe) VALUES ('USR-TEST-0065', 'tahina.ratsimbazafy65@gmail.com', 'password123', 'NOW()', 'NOW()', '1', '3', 'EMP-TEST-0065');
INSERT INTO users (id, email, password, created_at, modified_at, statut, id_type_user, id_employe) VALUES ('USR-TEST-0066', 'lova.rajaonarison66@gmail.com', 'password123', 'NOW()', 'NOW()', '1', '2', 'EMP-TEST-0066');
INSERT INTO users (id, email, password, created_at, modified_at, statut, id_type_user, id_employe) VALUES ('USR-TEST-0067', 'hasina.raharimalala67@gmail.com', 'password123', 'NOW()', 'NOW()', '1', '3', 'EMP-TEST-0067');
INSERT INTO users (id, email, password, created_at, modified_at, statut, id_type_user, id_employe) VALUES ('USR-TEST-0068', 'nomena.andriam68@gmail.com', 'password123', 'NOW()', 'NOW()', '1', '3', 'EMP-TEST-0068');
INSERT INTO users (id, email, password, created_at, modified_at, statut, id_type_user, id_employe) VALUES ('USR-TEST-0069', 'lova.rajaonarison69@gmail.com', 'password123', 'NOW()', 'NOW()', '1', '3', 'EMP-TEST-0069');
INSERT INTO users (id, email, password, created_at, modified_at, statut, id_type_user, id_employe) VALUES ('USR-TEST-0070', 'toky.rajaonarison70@gmail.com', 'password123', 'NOW()', 'NOW()', '1', '3', 'EMP-TEST-0070');
INSERT INTO users (id, email, password, created_at, modified_at, statut, id_type_user, id_employe) VALUES ('USR-TEST-0071', 'tiana.ravelo71@gmail.com', 'password123', 'NOW()', 'NOW()', '1', '3', 'EMP-TEST-0071');
INSERT INTO users (id, email, password, created_at, modified_at, statut, id_type_user, id_employe) VALUES ('USR-TEST-0072', 'fetra.randrianarisoa72@gmail.com', 'password123', 'NOW()', 'NOW()', '1', '3', 'EMP-TEST-0072');
INSERT INTO users (id, email, password, created_at, modified_at, statut, id_type_user, id_employe) VALUES ('USR-TEST-0073', 'lova.ranaivo73@gmail.com', 'password123', 'NOW()', 'NOW()', '1', '3', 'EMP-TEST-0073');
INSERT INTO users (id, email, password, created_at, modified_at, statut, id_type_user, id_employe) VALUES ('USR-TEST-0074', 'fetra.ranaivo74@gmail.com', 'password123', 'NOW()', 'NOW()', '1', '3', 'EMP-TEST-0074');
INSERT INTO users (id, email, password, created_at, modified_at, statut, id_type_user, id_employe) VALUES ('USR-TEST-0075', 'aina.ravelomanana75@gmail.com', 'password123', 'NOW()', 'NOW()', '1', '3', 'EMP-TEST-0075');
INSERT INTO users (id, email, password, created_at, modified_at, statut, id_type_user, id_employe) VALUES ('USR-TEST-0076', 'sitraka.rajaonarison76@gmail.com', 'password123', 'NOW()', 'NOW()', '1', '3', 'EMP-TEST-0076');
INSERT INTO users (id, email, password, created_at, modified_at, statut, id_type_user, id_employe) VALUES ('USR-TEST-0077', 'lova.rakotovao77@gmail.com', 'password123', 'NOW()', 'NOW()', '1', '3', 'EMP-TEST-0077');
INSERT INTO users (id, email, password, created_at, modified_at, statut, id_type_user, id_employe) VALUES ('USR-TEST-0078', 'fanja.ranaivo78@gmail.com', 'password123', 'NOW()', 'NOW()', '1', '3', 'EMP-TEST-0078');
INSERT INTO users (id, email, password, created_at, modified_at, statut, id_type_user, id_employe) VALUES ('USR-TEST-0079', 'fetra.ranaivo79@gmail.com', 'password123', 'NOW()', 'NOW()', '1', '3', 'EMP-TEST-0079');
INSERT INTO users (id, email, password, created_at, modified_at, statut, id_type_user, id_employe) VALUES ('USR-TEST-0080', 'hery.randrianarisoa80@gmail.com', 'password123', 'NOW()', 'NOW()', '1', '3', 'EMP-TEST-0080');
INSERT INTO users (id, email, password, created_at, modified_at, statut, id_type_user, id_employe) VALUES ('USR-TEST-0081', 'aina.razanakoto81@gmail.com', 'password123', 'NOW()', 'NOW()', '1', '2', 'EMP-TEST-0081');
INSERT INTO users (id, email, password, created_at, modified_at, statut, id_type_user, id_employe) VALUES ('USR-TEST-0082', 'aina.andriam82@gmail.com', 'password123', 'NOW()', 'NOW()', '1', '3', 'EMP-TEST-0082');
INSERT INTO users (id, email, password, created_at, modified_at, statut, id_type_user, id_employe) VALUES ('USR-TEST-0083', 'rado.raharimalala83@gmail.com', 'password123', 'NOW()', 'NOW()', '1', '3', 'EMP-TEST-0083');
INSERT INTO users (id, email, password, created_at, modified_at, statut, id_type_user, id_employe) VALUES ('USR-TEST-0084', 'rindra.rasoanaivo84@gmail.com', 'password123', 'NOW()', 'NOW()', '1', '3', 'EMP-TEST-0084');
INSERT INTO users (id, email, password, created_at, modified_at, statut, id_type_user, id_employe) VALUES ('USR-TEST-0085', 'faneva.rajaonarison85@gmail.com', 'password123', 'NOW()', 'NOW()', '1', '3', 'EMP-TEST-0085');
INSERT INTO users (id, email, password, created_at, modified_at, statut, id_type_user, id_employe) VALUES ('USR-TEST-0086', 'nomena.rakoto86@gmail.com', 'password123', 'NOW()', 'NOW()', '1', '3', 'EMP-TEST-0086');
INSERT INTO users (id, email, password, created_at, modified_at, statut, id_type_user, id_employe) VALUES ('USR-TEST-0087', 'rindra.rajaonarison87@gmail.com', 'password123', 'NOW()', 'NOW()', '1', '3', 'EMP-TEST-0087');
INSERT INTO users (id, email, password, created_at, modified_at, statut, id_type_user, id_employe) VALUES ('USR-TEST-0088', 'toky.andrianina88@gmail.com', 'password123', 'NOW()', 'NOW()', '1', '3', 'EMP-TEST-0088');
INSERT INTO users (id, email, password, created_at, modified_at, statut, id_type_user, id_employe) VALUES ('USR-TEST-0089', 'fanja.rajaonarison89@gmail.com', 'password123', 'NOW()', 'NOW()', '1', '3', 'EMP-TEST-0089');
INSERT INTO users (id, email, password, created_at, modified_at, statut, id_type_user, id_employe) VALUES ('USR-TEST-0090', 'nomena.rakotovao90@gmail.com', 'password123', 'NOW()', 'NOW()', '1', '3', 'EMP-TEST-0090');
INSERT INTO users (id, email, password, created_at, modified_at, statut, id_type_user, id_employe) VALUES ('USR-TEST-0091', 'tiana.rabe91@gmail.com', 'password123', 'NOW()', 'NOW()', '1', '3', 'EMP-TEST-0091');
INSERT INTO users (id, email, password, created_at, modified_at, statut, id_type_user, id_employe) VALUES ('USR-TEST-0092', 'rindra.razafy92@gmail.com', 'password123', 'NOW()', 'NOW()', '1', '3', 'EMP-TEST-0092');
INSERT INTO users (id, email, password, created_at, modified_at, statut, id_type_user, id_employe) VALUES ('USR-TEST-0093', 'toky.ratsimbazafy93@gmail.com', 'password123', 'NOW()', 'NOW()', '1', '3', 'EMP-TEST-0093');
INSERT INTO users (id, email, password, created_at, modified_at, statut, id_type_user, id_employe) VALUES ('USR-TEST-0094', 'tahina.raharimalala94@gmail.com', 'password123', 'NOW()', 'NOW()', '1', '3', 'EMP-TEST-0094');
INSERT INTO users (id, email, password, created_at, modified_at, statut, id_type_user, id_employe) VALUES ('USR-TEST-0095', 'lala.andriam95@gmail.com', 'password123', 'NOW()', 'NOW()', '1', '3', 'EMP-TEST-0095');
INSERT INTO users (id, email, password, created_at, modified_at, statut, id_type_user, id_employe) VALUES ('USR-TEST-0096', 'hasina.raharinja96@gmail.com', 'password123', 'NOW()', 'NOW()', '1', '3', 'EMP-TEST-0096');
INSERT INTO users (id, email, password, created_at, modified_at, statut, id_type_user, id_employe) VALUES ('USR-TEST-0097', 'kanto.rasolo97@gmail.com', 'password123', 'NOW()', 'NOW()', '1', '3', 'EMP-TEST-0097');
INSERT INTO users (id, email, password, created_at, modified_at, statut, id_type_user, id_employe) VALUES ('USR-TEST-0098', 'hery.razanakoto98@gmail.com', 'password123', 'NOW()', 'NOW()', '1', '3', 'EMP-TEST-0098');
INSERT INTO users (id, email, password, created_at, modified_at, statut, id_type_user, id_employe) VALUES ('USR-TEST-0099', 'soa.rajaonarison99@gmail.com', 'password123', 'NOW()', 'NOW()', '1', '3', 'EMP-TEST-0099');
INSERT INTO users (id, email, password, created_at, modified_at, statut, id_type_user, id_employe) VALUES ('USR-TEST-0100', 'fanja.raharinja100@gmail.com', 'password123', 'NOW()', 'NOW()', '1', '3', 'EMP-TEST-0100');
INSERT INTO users (id, email, password, created_at, modified_at, statut, id_type_user, id_employe) VALUES ('USR-TEST-0101', 'tahina.ranaivo101@gmail.com', 'password123', 'NOW()', 'NOW()', '1', '2', 'EMP-TEST-0101');
INSERT INTO users (id, email, password, created_at, modified_at, statut, id_type_user, id_employe) VALUES ('USR-TEST-0102', 'toky.razanakoto102@gmail.com', 'password123', 'NOW()', 'NOW()', '1', '3', 'EMP-TEST-0102');
INSERT INTO users (id, email, password, created_at, modified_at, statut, id_type_user, id_employe) VALUES ('USR-TEST-0103', 'soa.raharimalala103@gmail.com', 'password123', 'NOW()', 'NOW()', '1', '3', 'EMP-TEST-0103');
INSERT INTO users (id, email, password, created_at, modified_at, statut, id_type_user, id_employe) VALUES ('USR-TEST-0104', 'sitraka.razafindraibe104@gmail.com', 'password123', 'NOW()', 'NOW()', '1', '3', 'EMP-TEST-0104');
INSERT INTO users (id, email, password, created_at, modified_at, statut, id_type_user, id_employe) VALUES ('USR-TEST-0105', 'mina.raharinja105@gmail.com', 'password123', 'NOW()', 'NOW()', '1', '3', 'EMP-TEST-0105');
INSERT INTO users (id, email, password, created_at, modified_at, statut, id_type_user, id_employe) VALUES ('USR-TEST-0106', 'tahina.ravelomanana106@gmail.com', 'password123', 'NOW()', 'NOW()', '1', '3', 'EMP-TEST-0106');
INSERT INTO users (id, email, password, created_at, modified_at, statut, id_type_user, id_employe) VALUES ('USR-TEST-0107', 'soa.rajaonarison107@gmail.com', 'password123', 'NOW()', 'NOW()', '1', '3', 'EMP-TEST-0107');
INSERT INTO users (id, email, password, created_at, modified_at, statut, id_type_user, id_employe) VALUES ('USR-TEST-0108', 'toky.rakotovao108@gmail.com', 'password123', 'NOW()', 'NOW()', '1', '3', 'EMP-TEST-0108');
INSERT INTO users (id, email, password, created_at, modified_at, statut, id_type_user, id_employe) VALUES ('USR-TEST-0109', 'kanto.razafindraibe109@gmail.com', 'password123', 'NOW()', 'NOW()', '1', '3', 'EMP-TEST-0109');
INSERT INTO users (id, email, password, created_at, modified_at, statut, id_type_user, id_employe) VALUES ('USR-TEST-0110', 'lala.raharinja110@gmail.com', 'password123', 'NOW()', 'NOW()', '1', '3', 'EMP-TEST-0110');
INSERT INTO users (id, email, password, created_at, modified_at, statut, id_type_user, id_employe) VALUES ('USR-TEST-0111', 'faneva.andriam111@gmail.com', 'password123', 'NOW()', 'NOW()', '1', '3', 'EMP-TEST-0111');
INSERT INTO users (id, email, password, created_at, modified_at, statut, id_type_user, id_employe) VALUES ('USR-TEST-0112', 'mina.rasoanaivo112@gmail.com', 'password123', 'NOW()', 'NOW()', '1', '3', 'EMP-TEST-0112');
INSERT INTO users (id, email, password, created_at, modified_at, statut, id_type_user, id_employe) VALUES ('USR-TEST-0113', 'toky.razanakoto113@gmail.com', 'password123', 'NOW()', 'NOW()', '1', '3', 'EMP-TEST-0113');
INSERT INTO users (id, email, password, created_at, modified_at, statut, id_type_user, id_employe) VALUES ('USR-TEST-0114', 'lova.raharinja114@gmail.com', 'password123', 'NOW()', 'NOW()', '1', '3', 'EMP-TEST-0114');
INSERT INTO users (id, email, password, created_at, modified_at, statut, id_type_user, id_employe) VALUES ('USR-TEST-0115', 'hasina.ravelo115@gmail.com', 'password123', 'NOW()', 'NOW()', '1', '3', 'EMP-TEST-0115');
INSERT INTO users (id, email, password, created_at, modified_at, statut, id_type_user, id_employe) VALUES ('USR-TEST-0116', 'hery.ravelo116@gmail.com', 'password123', 'NOW()', 'NOW()', '1', '3', 'EMP-TEST-0116');
INSERT INTO users (id, email, password, created_at, modified_at, statut, id_type_user, id_employe) VALUES ('USR-TEST-0117', 'tiana.randria117@gmail.com', 'password123', 'NOW()', 'NOW()', '1', '3', 'EMP-TEST-0117');
INSERT INTO users (id, email, password, created_at, modified_at, statut, id_type_user, id_employe) VALUES ('USR-TEST-0118', 'sitraka.andrianina118@gmail.com', 'password123', 'NOW()', 'NOW()', '1', '3', 'EMP-TEST-0118');
INSERT INTO users (id, email, password, created_at, modified_at, statut, id_type_user, id_employe) VALUES ('USR-TEST-0119', 'soa.ravelomanana119@gmail.com', 'password123', 'NOW()', 'NOW()', '1', '3', 'EMP-TEST-0119');
INSERT INTO users (id, email, password, created_at, modified_at, statut, id_type_user, id_employe) VALUES ('USR-TEST-0120', 'hasina.razanakoto120@gmail.com', 'password123', 'NOW()', 'NOW()', '1', '3', 'EMP-TEST-0120');

COMMIT;