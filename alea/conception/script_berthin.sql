-- DATABASE = banque_berthin

CREATE TABLE client(
   id INTEGER GENERATED ALWAYS AS IDENTITY,
   matricule VARCHAR(50) NOT NULL,
   nom VARCHAR(255) NOT NULL,
   prenom VARCHAR(255) NOT NULL,
   date_naissance DATE NOT NULL,
   adresse VARCHAR(255) NOT NULL,
   email VARCHAR(100) NOT NULL,
   telephone VARCHAR(50) NOT NULL,
   date_creation TIMESTAMP NOT NULL,
   PRIMARY KEY(id),
   UNIQUE(matricule)
);

CREATE TABLE statut_compte(
   id INTEGER GENERATED ALWAYS AS IDENTITY,
   libelle VARCHAR(50) NOT NULL, -- Actif, Fermé, Bloqué
   PRIMARY KEY(id)
);

CREATE TABLE compte_depot(
   id INTEGER GENERATED ALWAYS AS IDENTITY,
   numero_compte VARCHAR(50) NOT NULL,
   date_ouverture TIMESTAMP NOT NULL,
   date_fermeture TIMESTAMP,
   id_statut INTEGER NOT NULL,
   id_client INTEGER NOT NULL,
   PRIMARY KEY(id),
   UNIQUE(id_client),
   UNIQUE(numero_compte),
   FOREIGN KEY(id_statut) REFERENCES statut_compte(id),
   FOREIGN KEY(id_client) REFERENCES client(id)
);

CREATE TABLE pret(
   id INTEGER GENERATED ALWAYS AS IDENTITY,
   montant_pret NUMERIC(15,2) NOT NULL,
   taux_interet_annuel NUMERIC(15,2) NOT NULL,
   periodicite_remboursement INTEGER NOT NULL,
   date_creation TIMESTAMP NOT NULL,
   date_fermeture TIMESTAMP,
   id_client INTEGER NOT NULL,
   PRIMARY KEY(id),
   FOREIGN KEY(id_client) REFERENCES client(id)
);

CREATE TABLE statut_pret(
   id INTEGER GENERATED ALWAYS AS IDENTITY,
   libelle VARCHAR(50), -- Actif, Annulé, Fermé
   PRIMARY KEY(id)
);

CREATE TABLE configuration_compte_depot(
   id INTEGER GENERATED ALWAYS AS IDENTITY,
   taux_interet_annuel DOUBLE PRECISION NOT NULL,
   limite_retrait_mensuel INTEGER NOT NULL,
   pourcentage_max_retrait DOUBLE PRECISION NOT NULL,
   date_application TIMESTAMP NOT NULL,
   id_compte_depot INTEGER NOT NULL,
   PRIMARY KEY(id),
   FOREIGN KEY(id_compte_depot) REFERENCES compte_depot(id)
);

CREATE TABLE type_mouvement_compte_depot(
   id INTEGER GENERATED ALWAYS AS IDENTITY,
   libelle VARCHAR(50) NOT NULL, -- Dépôt, Retrait, Obtention d'intérêts
   PRIMARY KEY(id),
   UNIQUE(libelle)
);

CREATE TABLE type_mouvement_compte_courant(
   id INTEGER GENERATED ALWAYS AS IDENTITY,
   libelle VARCHAR(50) NOT NULL, -- Débit, Crédit
   PRIMARY KEY(id),
   UNIQUE(libelle)
);

CREATE TABLE historique_statut_pret(
   id INTEGER GENERATED ALWAYS AS IDENTITY,
   date_modification TIMESTAMP NOT NULL,
   id_statut INTEGER NOT NULL,
   id_pret INTEGER NOT NULL,
   PRIMARY KEY(id),
   FOREIGN KEY(id_statut) REFERENCES statut_pret(id),
   FOREIGN KEY(id_pret) REFERENCES pret(id)
);

CREATE TABLE compte_courant(
   id INTEGER GENERATED ALWAYS AS IDENTITY,
   numero_compte VARCHAR(50)  NOT NULL,
   solde NUMERIC(15,2) NOT NULL,
   date_ouverture TIMESTAMP NOT NULL,
   date_fermeture TIMESTAMP,
   id_statut INTEGER NOT NULL,
   id_client INTEGER NOT NULL,
   PRIMARY KEY(id),
   UNIQUE(id_client),
   UNIQUE(numero_compte),
   FOREIGN KEY(id_statut) REFERENCES statut_compte(id),
   FOREIGN KEY(id_client) REFERENCES client(id)
);

CREATE TABLE mouvement_compte_courant(
   id INTEGER GENERATED ALWAYS AS IDENTITY,
   montant NUMERIC(15,2) NOT NULL,
   description TEXT NOT NULL,
   date_mouvement TIMESTAMP NOT NULL,
   id_type_mouvement INTEGER NOT NULL,
   id_compte_courant INTEGER NOT NULL,
   PRIMARY KEY(id),
   FOREIGN KEY(id_type_mouvement) REFERENCES type_mouvement_compte_courant(id),
   FOREIGN KEY(id_compte_courant) REFERENCES compte_courant(id)
);

CREATE TABLE mouvement_compte_depot(
   id INTEGER GENERATED ALWAYS AS IDENTITY,
   montant NUMERIC(15,2) NOT NULL,
   description TEXT NOT NULL,
   date_mouvement TIMESTAMP NOT NULL,
   id_type_mouvement INTEGER NOT NULL,
   id_compte_depot INTEGER NOT NULL,
   PRIMARY KEY(id),
   FOREIGN KEY(id_type_mouvement) REFERENCES type_mouvement_compte_depot(id),
   FOREIGN KEY(id_compte_depot) REFERENCES compte_depot(id)
);

CREATE TABLE remboursement_pret(
   id INTEGER GENERATED ALWAYS AS IDENTITY,
   montant_rembourse NUMERIC(15,2) NOT NULL,
   interet_paye NUMERIC(15,2) NOT NULL,
   date_paiement TIMESTAMP NOT NULL,
   id_mouvement_compte_courant INTEGER NOT NULL,
   id_pret INTEGER NOT NULL,
   PRIMARY KEY(id),
   FOREIGN KEY(id_mouvement_compte_courant) REFERENCES mouvement_compte_courant(id),
   FOREIGN KEY(id_pret) REFERENCES pret(id)
);

CREATE TABLE direction(
   id INTEGER GENERATED ALWAYS AS IDENTITY,
   niveau INTEGER NOT NULL,
   libelle VARCHAR(100) NOT NULL,
   PRIMARY KEY(id),
   UNIQUE(libelle)
);

CREATE TABLE action_role(
   id INTEGER GENERATED ALWAYS AS IDENTITY,
   role INTEGER NOT NULL,
   action VARCHAR(100) NOT NULL,
   PRIMARY KEY(id)
);

CREATE TABLE utilisateur(
   id INTEGER GENERATED ALWAYS AS IDENTITY,
   matricule VARCHAR(50) NOT NULL,
   password VARCHAR(255) NOT NULL,
   role INTEGER NOT NULL,
   id_direction INTEGER NOT NULL,
   PRIMARY KEY(id),
   FOREIGN KEY(id_direction) REFERENCES direction(id),
   UNIQUE(matricule)
); 



