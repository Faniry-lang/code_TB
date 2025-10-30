-- Insertion des données de test pour la base banque_berthin

-- 1. Insertion dans la table client
INSERT INTO client (id, matricule, nom, prenom, date_naissance, adresse, email, telephone, date_creation) VALUES
(1, 'CLI001', 'Dupont', 'Jean', '1985-03-15', '123 Avenue des Champs-Élysées, Paris', 'jean.dupont@email.com', '0123456789', '2023-01-15 09:30:00'),
(2, 'CLI002', 'Martin', 'Marie', '1990-07-22', '456 Rue de la République, Lyon', 'marie.martin@email.com', '0234567890', '2023-02-20 10:15:00'),
(3, 'CLI003', 'Bernard', 'Pierre', '1978-11-08', '789 Boulevard Saint-Germain, Marseille', 'pierre.bernard@email.com', '0345678901', '2023-03-10 14:20:00'),
(4, 'CLI004', 'Dubois', 'Sophie', '1988-05-30', '321 Rue du Faubourg Saint-Antoine, Lille', 'sophie.dubois@email.com', '0456789012', '2023-04-05 11:45:00'),
(5, 'CLI005', 'Moreau', 'Luc', '1995-12-12', '654 Avenue de la Libération, Toulouse', 'luc.moreau@email.com', '0567890123', '2023-05-12 16:30:00');

-- 2. Insertion dans la table statut_compte
INSERT INTO statut_compte (id, libelle) VALUES
(1, 'Actif'),
(2, 'Fermé'),
(3, 'Bloqué');

-- 3. Insertion dans la table statut_pret
INSERT INTO statut_pret (id, libelle) VALUES
(1, 'Actif'),
(2, 'Annulé'),
(3, 'Fermé');

-- 4. Insertion dans la table type_mouvement_compte_courant
INSERT INTO type_mouvement_compte_courant (id, libelle) VALUES
(1, 'Crédit'),
(2, 'Débit');

-- 5. Insertion dans la table type_mouvement_compte_depot
INSERT INTO type_mouvement_compte_depot (id, libelle) VALUES
(1, 'Dépôt'),
(2, 'Retrait'),
(3, 'Obtention d''intérêts');

-- 6. Insertion dans la table compte_courant
INSERT INTO compte_courant (id, numero_compte, solde, date_ouverture, date_fermeture, id_statut, id_client) VALUES
(1, 'CC001', 2500.00, '2023-01-15 10:00:00', NULL, 1, 1),
(2, 'CC002', 1800.50, '2023-02-20 11:00:00', NULL, 1, 2),
(3, 'CC003', 3200.75, '2023-03-10 15:00:00', NULL, 1, 3),
(4, 'CC004', 950.25, '2023-04-05 12:00:00', NULL, 1, 4),
(5, 'CC005', 4500.00, '2023-05-12 17:00:00', NULL, 1, 5);

-- 7. Insertion dans la table compte_depot
INSERT INTO compte_depot (id, numero_compte, date_ouverture, date_fermeture, id_statut, id_client) VALUES
(1, 'CD001', '2023-01-20 09:00:00', NULL, 1, 1),
(2, 'CD002', '2023-02-25 10:00:00', NULL, 1, 2),
(3, 'CD003', '2023-03-15 14:00:00', NULL, 1, 3),
(4, 'CD004', '2023-04-10 11:00:00', NULL, 1, 4),
(5, 'CD005', '2023-05-18 16:00:00', NULL, 1, 5);

-- 8. Insertion dans la table pret
INSERT INTO pret (id, montant_pret, taux_interet_annuel, periodicite_remboursement, date_creation, date_fermeture, id_client) VALUES
(1, 15000.00, 3.5, 36, '2023-02-01 10:00:00', NULL, 1),
(2, 25000.00, 4.2, 60, '2023-03-15 11:00:00', NULL, 2),
(3, 10000.00, 3.8, 24, '2023-04-20 14:00:00', NULL, 3),
(4, 5000.00, 5.0, 12, '2023-05-10 09:00:00', NULL, 4),
(5, 30000.00, 3.2, 48, '2023-06-05 16:00:00', NULL, 5);

-- 9. Insertion dans la table configuration_compte_depot
INSERT INTO configuration_compte_depot (id, taux_interet_annuel, limite_retrait_mensuel, pourcentage_max_retrait, date_application, id_compte_depot) VALUES
(1, 1.5, 3, 30.0, '2023-01-20 09:00:00', 1),
(2, 1.5, 3, 30.0, '2023-02-25 10:00:00', 2),
(3, 1.5, 3, 30.0, '2023-03-15 14:00:00', 3),
(4, 1.5, 3, 30.0, '2023-04-10 11:00:00', 4),
(5, 1.5, 3, 30.0, '2023-05-18 16:00:00', 5);

-- 10. Insertion dans la table mouvement_compte_courant
INSERT INTO mouvement_compte_courant (id, montant, description, date_mouvement, id_type_mouvement, id_compte_courant, actif) VALUES
(1, 1000.00, 'Dépôt initial', '2023-01-15 10:05:00', 1, 1, TRUE),
(2, 1500.00, 'Virement salaire', '2023-02-01 09:15:00', 1, 1, TRUE),
(3, 200.00, 'Retrait DAB', '2023-02-05 16:30:00', 2, 1, TRUE),
(4, 800.00, 'Dépôt initial', '2023-02-20 11:05:00', 1, 2, TRUE),
(5, 1200.00, 'Virement salaire', '2023-03-01 10:20:00', 1, 2, TRUE),
(6, 150.50, 'Paiement carte', '2023-03-10 14:45:00', 2, 2, TRUE),
(7, 2000.00, 'Dépôt initial', '2023-03-10 15:05:00', 1, 3, TRUE),
(8, 1500.00, 'Virement professionnel', '2023-04-01 11:30:00', 1, 3, TRUE),
(9, 299.25, 'Prélèvement EDF', '2023-04-05 08:15:00', 2, 3, TRUE),
(10, 500.00, 'Dépôt initial', '2023-04-05 12:05:00', 1, 4, TRUE),
(11, 600.00, 'Virement occasionnel', '2023-05-01 13:20:00', 1, 4, TRUE),
(12, 149.75, 'Achat en ligne', '2023-05-15 19:30:00', 2, 4, TRUE),
(13, 3000.00, 'Dépôt initial', '2023-05-12 17:05:00', 1, 5, TRUE),
(14, 2000.00, 'Virement investissement', '2023-06-01 15:45:00', 1, 5, TRUE),
(15, 500.00, 'Transfert épargne', '2023-06-10 10:10:00', 2, 5, TRUE);

-- 11. Insertion dans la table mouvement_compte_depot
INSERT INTO mouvement_compte_depot (id, montant, description, date_mouvement, id_type_mouvement, id_compte_depot) VALUES
(1, 5000.00, 'Dépôt initial', '2023-01-20 09:05:00', 1, 1),
(2, 1000.00, 'Complément épargne', '2023-03-15 14:20:00', 1, 1),
(3, 300.00, 'Retrait partiel', '2023-04-10 11:30:00', 2, 1),
(4, 8000.00, 'Dépôt initial', '2023-02-25 10:05:00', 1, 2),
(5, 2000.00, 'Virement épargne', '2023-04-20 16:15:00', 1, 2),
(6, 12000.00, 'Dépôt initial', '2023-03-15 14:05:00', 1, 3),
(7, 3000.00, 'Dépôt initial', '2023-04-10 11:05:00', 1, 4),
(8, 6000.00, 'Dépôt initial', '2023-05-18 16:05:00', 1, 5),
(9, 25.50, 'Intérêts trimestriels', '2023-06-30 23:59:00', 3, 1),
(10, 40.25, 'Intérêts trimestriels', '2023-06-30 23:59:00', 3, 2);

-- 12. Insertion dans la table historique_statut_pret
INSERT INTO historique_statut_pret (id, date_modification, id_statut, id_pret) VALUES
(1, '2023-02-01 10:00:00', 1, 1),
(2, '2023-03-15 11:00:00', 1, 2),
(3, '2023-04-20 14:00:00', 1, 3),
(4, '2023-05-10 09:00:00', 1, 4),
(5, '2023-06-05 16:00:00', 1, 5);

-- 13. Insertion dans la table remboursement_pret
INSERT INTO remboursement_pret (id, montant_rembourse, interet_paye, date_paiement, id_mouvement_compte_courant, id_pret) VALUES
(1, 450.00, 43.75, '2023-03-01 09:00:00', 2, 1),
(2, 450.00, 42.50, '2023-04-01 09:00:00', 5, 1),
(3, 480.00, 87.50, '2023-04-01 10:00:00', 5, 2),
(4, 480.00, 85.00, '2023-05-01 10:00:00', 8, 2),
(5, 420.00, 31.67, '2023-05-01 14:00:00', 8, 3);

-- Vérification des séquences (si vous utilisez des séquences pour les IDs auto-générés)
-- Note: Si vos IDs sont auto-incrémentés, vous devrez peut-être réinitialiser les séquences
SELECT SETVAL('client_id_seq', (SELECT MAX(id) FROM client));
SELECT SETVAL('statut_compte_id_seq', (SELECT MAX(id) FROM statut_compte));
SELECT SETVAL('statut_pret_id_seq', (SELECT MAX(id) FROM statut_pret));
SELECT SETVAL('type_mouvement_compte_courant_id_seq', (SELECT MAX(id) FROM type_mouvement_compte_courant));
SELECT SETVAL('type_mouvement_compte_depot_id_seq', (SELECT MAX(id) FROM type_mouvement_compte_depot));
SELECT SETVAL('compte_courant_id_seq', (SELECT MAX(id) FROM compte_courant));
SELECT SETVAL('compte_depot_id_seq', (SELECT MAX(id) FROM compte_depot));
SELECT SETVAL('pret_id_seq', (SELECT MAX(id) FROM pret));
SELECT SETVAL('configuration_compte_depot_id_seq', (SELECT MAX(id) FROM configuration_compte_depot));
SELECT SETVAL('mouvement_compte_courant_id_seq', (SELECT MAX(id) FROM mouvement_compte_courant));
SELECT SETVAL('mouvement_compte_depot_id_seq', (SELECT MAX(id) FROM mouvement_compte_depot));
SELECT SETVAL('historique_statut_pret_id_seq', (SELECT MAX(id) FROM historique_statut_pret));
SELECT SETVAL('remboursement_pret_id_seq', (SELECT MAX(id) FROM remboursement_pret));

-- Directions
INSERT INTO direction (id, niveau, libelle) VALUES
(1, 1, 'Compte Courant'),  -- 1
(2, 1, 'Compte Dépot'),    -- 2
(3, 1, 'Pret');            -- 3

-- ActionRole
INSERT INTO action_role (id, role, action) VALUES
(1, 2, 'SAISIE'),          -- 1
(2, 4, 'VALIDATION');      -- 2

-- Utilisateurs
INSERT INTO utilisateur (id, matricule, password, role, id_direction) VALUES
(1, 'courant1', 'berthin', 1, 1),  -- 1
(2, 'courant2', 'berthin', 4, 1),  -- 2
(3, 'depot1', 'berthin', 1, 2),    -- 3
(4, 'depot2', 'berthin', 4, 2),    -- 4
(5, 'pret1', 'berthin', 1, 3),     -- 5
(6, 'pret2', 'berthin', 4, 3);     -- 6