using System;
using System.Collections.Generic;
using banque_compte_pret.Models;
using Microsoft.EntityFrameworkCore;

namespace banque_compte_pret.Data;

public partial class BanqueBerthinContext : DbContext
{
    public BanqueBerthinContext()
    {
    }

    public BanqueBerthinContext(DbContextOptions<BanqueBerthinContext> options)
        : base(options)
    {
    }

    public virtual DbSet<ActionRole> ActionRoles { get; set; }

    public virtual DbSet<Client> Clients { get; set; }

    public virtual DbSet<CompteCourant> CompteCourants { get; set; }

    public virtual DbSet<CompteDepot> CompteDepots { get; set; }

    public virtual DbSet<ConfigurationCompteDepot> ConfigurationCompteDepots { get; set; }

    public virtual DbSet<Direction> Directions { get; set; }

    public virtual DbSet<HistoriqueStatutPret> HistoriqueStatutPrets { get; set; }

    public virtual DbSet<MouvementCompteCourant> MouvementCompteCourants { get; set; }

    public virtual DbSet<MouvementCompteDepot> MouvementCompteDepots { get; set; }

    public virtual DbSet<Pret> Prets { get; set; }

    public virtual DbSet<RemboursementPret> RemboursementPrets { get; set; }

    public virtual DbSet<StatutCompte> StatutComptes { get; set; }

    public virtual DbSet<StatutPret> StatutPrets { get; set; }

    public virtual DbSet<TypeMouvementCompteCourant> TypeMouvementCompteCourants { get; set; }

    public virtual DbSet<TypeMouvementCompteDepot> TypeMouvementCompteDepots { get; set; }

    public virtual DbSet<Utilisateur> Utilisateurs { get; set; }

    protected override void OnConfiguring(DbContextOptionsBuilder optionsBuilder)
#warning To protect potentially sensitive information in your connection string, you should move it out of source code. You can avoid scaffolding the connection string by using the Name= syntax to read it from configuration - see https://go.microsoft.com/fwlink/?linkid=2131148. For more guidance on storing connection strings, see https://go.microsoft.com/fwlink/?LinkId=723263.
        => optionsBuilder.UseNpgsql("Host=localhost;Database=banque_berthin;Username=postgres;Password=berthin");

    protected override void OnModelCreating(ModelBuilder modelBuilder)
    {
        modelBuilder.Entity<ActionRole>(entity =>
        {
            entity.HasKey(e => e.Id).HasName("action_role_pkey");

            entity.ToTable("action_role");

            entity.Property(e => e.Id)
                .ValueGeneratedNever()
                .HasColumnName("id");
            entity.Property(e => e.Action)
                .HasMaxLength(100)
                .HasColumnName("action");
            entity.Property(e => e.Role).HasColumnName("role");
        });

        modelBuilder.Entity<Client>(entity =>
        {
            entity.HasKey(e => e.Id).HasName("client_pkey");

            entity.ToTable("client");

            entity.HasIndex(e => e.Matricule, "client_matricule_key").IsUnique();

            entity.Property(e => e.Id).HasColumnName("id");
            entity.Property(e => e.Adresse)
                .HasMaxLength(255)
                .HasColumnName("adresse");
            entity.Property(e => e.DateCreation)
                .HasPrecision(6)
                .HasColumnName("date_creation");
            entity.Property(e => e.DateNaissance).HasColumnName("date_naissance");
            entity.Property(e => e.Email)
                .HasMaxLength(100)
                .HasColumnName("email");
            entity.Property(e => e.Matricule)
                .HasMaxLength(50)
                .HasColumnName("matricule");
            entity.Property(e => e.Nom)
                .HasMaxLength(255)
                .HasColumnName("nom");
            entity.Property(e => e.Prenom)
                .HasMaxLength(255)
                .HasColumnName("prenom");
            entity.Property(e => e.Telephone)
                .HasMaxLength(50)
                .HasColumnName("telephone");
        });

        modelBuilder.Entity<CompteCourant>(entity =>
        {
            entity.HasKey(e => e.Id).HasName("compte_courant_pkey");

            entity.ToTable("compte_courant");

            entity.HasIndex(e => e.IdClient, "compte_courant_id_client_key").IsUnique();

            entity.HasIndex(e => e.NumeroCompte, "compte_courant_numero_compte_key").IsUnique();

            entity.Property(e => e.Id).HasColumnName("id");
            entity.Property(e => e.DateFermeture)
                .HasPrecision(6)
                .HasColumnName("date_fermeture");
            entity.Property(e => e.DateOuverture)
                .HasPrecision(6)
                .HasColumnName("date_ouverture");
            entity.Property(e => e.IdClient).HasColumnName("id_client");
            entity.Property(e => e.IdStatut).HasColumnName("id_statut");
            entity.Property(e => e.NumeroCompte)
                .HasMaxLength(50)
                .HasColumnName("numero_compte");
            entity.Property(e => e.Solde)
                .HasPrecision(15, 2)
                .HasColumnName("solde");

            entity.HasOne(d => d.IdClientNavigation).WithOne(p => p.CompteCourant)
                .HasForeignKey<CompteCourant>(d => d.IdClient)
                .OnDelete(DeleteBehavior.ClientSetNull)
                .HasConstraintName("fk7tq4t7yku4rvo542b61375f5u");

            entity.HasOne(d => d.IdStatutNavigation).WithMany(p => p.CompteCourants)
                .HasForeignKey(d => d.IdStatut)
                .OnDelete(DeleteBehavior.ClientSetNull)
                .HasConstraintName("fkmp737pro3lg3qu5len7px7nkr");
        });

        modelBuilder.Entity<CompteDepot>(entity =>
        {
            entity.HasKey(e => e.Id).HasName("compte_depot_pkey");

            entity.ToTable("compte_depot");

            entity.HasIndex(e => e.IdClient, "compte_depot_id_client_key").IsUnique();

            entity.HasIndex(e => e.NumeroCompte, "compte_depot_numero_compte_key").IsUnique();

            entity.Property(e => e.Id).HasColumnName("id");
            entity.Property(e => e.DateFermeture)
                .HasPrecision(6)
                .HasColumnName("date_fermeture");
            entity.Property(e => e.DateOuverture)
                .HasPrecision(6)
                .HasColumnName("date_ouverture");
            entity.Property(e => e.IdClient).HasColumnName("id_client");
            entity.Property(e => e.IdStatut).HasColumnName("id_statut");
            entity.Property(e => e.NumeroCompte)
                .HasMaxLength(50)
                .HasColumnName("numero_compte");

            entity.HasOne(d => d.IdClientNavigation).WithOne(p => p.CompteDepot)
                .HasForeignKey<CompteDepot>(d => d.IdClient)
                .OnDelete(DeleteBehavior.ClientSetNull)
                .HasConstraintName("fko74iuk1yvmn8dpdfwv2pcfs5r");

            entity.HasOne(d => d.IdStatutNavigation).WithMany(p => p.CompteDepots)
                .HasForeignKey(d => d.IdStatut)
                .OnDelete(DeleteBehavior.ClientSetNull)
                .HasConstraintName("fk2uf9rod20osvh1sga826rolbo");
        });

        modelBuilder.Entity<ConfigurationCompteDepot>(entity =>
        {
            entity.HasKey(e => e.Id).HasName("configuration_compte_depot_pkey");

            entity.ToTable("configuration_compte_depot");

            entity.Property(e => e.Id).HasColumnName("id");
            entity.Property(e => e.DateApplication)
                .HasPrecision(6)
                .HasColumnName("date_application");
            entity.Property(e => e.IdCompteDepot).HasColumnName("id_compte_depot");
            entity.Property(e => e.LimiteRetraitMensuel).HasColumnName("limite_retrait_mensuel");
            entity.Property(e => e.PourcentageMaxRetrait).HasColumnName("pourcentage_max_retrait");
            entity.Property(e => e.TauxInteretAnnuel).HasColumnName("taux_interet_annuel");

            entity.HasOne(d => d.IdCompteDepotNavigation).WithMany(p => p.ConfigurationCompteDepots)
                .HasForeignKey(d => d.IdCompteDepot)
                .OnDelete(DeleteBehavior.ClientSetNull)
                .HasConstraintName("fkpoi73r6qh2eexxrp4uo7v21n1");
        });

        modelBuilder.Entity<Direction>(entity =>
        {
            entity.HasKey(e => e.Id).HasName("direction_pkey");

            entity.ToTable("direction");

            entity.HasIndex(e => e.Libelle, "direction_libelle_key").IsUnique();

            entity.Property(e => e.Id)
                .ValueGeneratedNever()
                .HasColumnName("id");
            entity.Property(e => e.Libelle)
                .HasMaxLength(100)
                .HasColumnName("libelle");
            entity.Property(e => e.Niveau).HasColumnName("niveau");
        });

        modelBuilder.Entity<HistoriqueStatutPret>(entity =>
        {
            entity.HasKey(e => e.Id).HasName("historique_statut_pret_pkey");

            entity.ToTable("historique_statut_pret");

            entity.Property(e => e.Id).HasColumnName("id");
            entity.Property(e => e.DateModification)
                .HasPrecision(6)
                .HasColumnName("date_modification");
            entity.Property(e => e.IdPret).HasColumnName("id_pret");
            entity.Property(e => e.IdStatut).HasColumnName("id_statut");

            entity.HasOne(d => d.IdPretNavigation).WithMany(p => p.HistoriqueStatutPrets)
                .HasForeignKey(d => d.IdPret)
                .OnDelete(DeleteBehavior.ClientSetNull)
                .HasConstraintName("fke3gwuku1nknbo0l5oocx80cw0");

            entity.HasOne(d => d.IdStatutNavigation).WithMany(p => p.HistoriqueStatutPrets)
                .HasForeignKey(d => d.IdStatut)
                .OnDelete(DeleteBehavior.ClientSetNull)
                .HasConstraintName("fk6hrcrb61y1smjlr4oqk78a0cq");
        });

        modelBuilder.Entity<MouvementCompteCourant>(entity =>
        {
            entity.HasKey(e => e.Id).HasName("mouvement_compte_courant_pkey");

            entity.ToTable("mouvement_compte_courant");

            entity.Property(e => e.Id).HasColumnName("id");
            entity.Property(e => e.Actif).HasColumnName("actif");
            entity.Property(e => e.DateMouvement)
                .HasPrecision(6)
                .HasColumnName("date_mouvement");
            entity.Property(e => e.Description).HasColumnName("description");
            entity.Property(e => e.IdCompteCourant).HasColumnName("id_compte_courant");
            entity.Property(e => e.IdTypeMouvement).HasColumnName("id_type_mouvement");
            entity.Property(e => e.Montant)
                .HasPrecision(15, 2)
                .HasColumnName("montant");

            entity.HasOne(d => d.IdCompteCourantNavigation).WithMany(p => p.MouvementCompteCourants)
                .HasForeignKey(d => d.IdCompteCourant)
                .OnDelete(DeleteBehavior.ClientSetNull)
                .HasConstraintName("fkh62f7isjxbskgl9cfk6npn34m");

            entity.HasOne(d => d.IdTypeMouvementNavigation).WithMany(p => p.MouvementCompteCourants)
                .HasForeignKey(d => d.IdTypeMouvement)
                .OnDelete(DeleteBehavior.ClientSetNull)
                .HasConstraintName("fk3fmr4rd2fsy2dnbyeebkh54qo");
        });

        modelBuilder.Entity<MouvementCompteDepot>(entity =>
        {
            entity.HasKey(e => e.Id).HasName("mouvement_compte_depot_pkey");

            entity.ToTable("mouvement_compte_depot");

            entity.Property(e => e.Id).HasColumnName("id");
            entity.Property(e => e.DateMouvement)
                .HasPrecision(6)
                .HasColumnName("date_mouvement");
            entity.Property(e => e.Description).HasColumnName("description");
            entity.Property(e => e.IdCompteDepot).HasColumnName("id_compte_depot");
            entity.Property(e => e.IdTypeMouvement).HasColumnName("id_type_mouvement");
            entity.Property(e => e.Montant)
                .HasPrecision(15, 2)
                .HasColumnName("montant");

            entity.HasOne(d => d.IdCompteDepotNavigation).WithMany(p => p.MouvementCompteDepots)
                .HasForeignKey(d => d.IdCompteDepot)
                .OnDelete(DeleteBehavior.ClientSetNull)
                .HasConstraintName("fk5doq0w14fjf0ink6a02dhut28");

            entity.HasOne(d => d.IdTypeMouvementNavigation).WithMany(p => p.MouvementCompteDepots)
                .HasForeignKey(d => d.IdTypeMouvement)
                .OnDelete(DeleteBehavior.ClientSetNull)
                .HasConstraintName("fk9j39eou16wmf0bnxv7rold8a7");
        });

        modelBuilder.Entity<Pret>(entity =>
        {
            entity.HasKey(e => e.Id).HasName("pret_pkey");

            entity.ToTable("pret");

            entity.Property(e => e.Id).HasColumnName("id");
            entity.Property(e => e.DateCreation)
                .HasPrecision(6)
                .HasColumnName("date_creation");
            entity.Property(e => e.DateFermeture)
                .HasPrecision(6)
                .HasColumnName("date_fermeture");
            entity.Property(e => e.IdClient).HasColumnName("id_client");
            entity.Property(e => e.MontantPret)
                .HasPrecision(15, 2)
                .HasColumnName("montant_pret");
            entity.Property(e => e.PeriodiciteRemboursement).HasColumnName("periodicite_remboursement");
            entity.Property(e => e.TauxInteretAnnuel)
                .HasPrecision(15, 2)
                .HasColumnName("taux_interet_annuel");

            entity.HasOne(d => d.IdClientNavigation).WithMany(p => p.Prets)
                .HasForeignKey(d => d.IdClient)
                .OnDelete(DeleteBehavior.ClientSetNull)
                .HasConstraintName("fkkxf9aifgtf8tc9dtu9kjsxk0y");
        });

        modelBuilder.Entity<RemboursementPret>(entity =>
        {
            entity.HasKey(e => e.Id).HasName("remboursement_pret_pkey");

            entity.ToTable("remboursement_pret");

            entity.Property(e => e.Id).HasColumnName("id");
            entity.Property(e => e.DatePaiement)
                .HasPrecision(6)
                .HasColumnName("date_paiement");
            entity.Property(e => e.IdMouvementCompteCourant).HasColumnName("id_mouvement_compte_courant");
            entity.Property(e => e.IdPret).HasColumnName("id_pret");
            entity.Property(e => e.InteretPaye)
                .HasPrecision(15, 2)
                .HasColumnName("interet_paye");
            entity.Property(e => e.MontantRembourse)
                .HasPrecision(15, 2)
                .HasColumnName("montant_rembourse");

            entity.HasOne(d => d.IdMouvementCompteCourantNavigation).WithMany(p => p.RemboursementPrets)
                .HasForeignKey(d => d.IdMouvementCompteCourant)
                .OnDelete(DeleteBehavior.ClientSetNull)
                .HasConstraintName("fkidq08eg9pnjexy6q9d9v945l0");

            entity.HasOne(d => d.IdPretNavigation).WithMany(p => p.RemboursementPrets)
                .HasForeignKey(d => d.IdPret)
                .OnDelete(DeleteBehavior.ClientSetNull)
                .HasConstraintName("fkl98ktgbirg9r77n7xfjbnc7nl");
        });

        modelBuilder.Entity<StatutCompte>(entity =>
        {
            entity.HasKey(e => e.Id).HasName("statut_compte_pkey");

            entity.ToTable("statut_compte");

            entity.Property(e => e.Id).HasColumnName("id");
            entity.Property(e => e.Libelle)
                .HasMaxLength(50)
                .HasColumnName("libelle");
        });

        modelBuilder.Entity<StatutPret>(entity =>
        {
            entity.HasKey(e => e.Id).HasName("statut_pret_pkey");

            entity.ToTable("statut_pret");

            entity.Property(e => e.Id).HasColumnName("id");
            entity.Property(e => e.Libelle)
                .HasMaxLength(50)
                .HasColumnName("libelle");
        });

        modelBuilder.Entity<TypeMouvementCompteCourant>(entity =>
        {
            entity.HasKey(e => e.Id).HasName("type_mouvement_compte_courant_pkey");

            entity.ToTable("type_mouvement_compte_courant");

            entity.HasIndex(e => e.Libelle, "type_mouvement_compte_courant_libelle_key").IsUnique();

            entity.Property(e => e.Id).HasColumnName("id");
            entity.Property(e => e.Libelle)
                .HasMaxLength(50)
                .HasColumnName("libelle");
        });

        modelBuilder.Entity<TypeMouvementCompteDepot>(entity =>
        {
            entity.HasKey(e => e.Id).HasName("type_mouvement_compte_depot_pkey");

            entity.ToTable("type_mouvement_compte_depot");

            entity.HasIndex(e => e.Libelle, "type_mouvement_compte_depot_libelle_key").IsUnique();

            entity.Property(e => e.Id).HasColumnName("id");
            entity.Property(e => e.Libelle)
                .HasMaxLength(50)
                .HasColumnName("libelle");
        });

        modelBuilder.Entity<Utilisateur>(entity =>
        {
            entity.HasKey(e => e.Id).HasName("utilisateur_pkey");

            entity.ToTable("utilisateur");

            entity.HasIndex(e => e.Matricule, "utilisateur_matricule_key").IsUnique();

            entity.Property(e => e.Id).HasColumnName("id");
            entity.Property(e => e.IdDirection).HasColumnName("id_direction");
            entity.Property(e => e.Matricule)
                .HasMaxLength(50)
                .HasColumnName("matricule");
            entity.Property(e => e.Password)
                .HasMaxLength(255)
                .HasColumnName("password");
            entity.Property(e => e.Role).HasColumnName("role");

            entity.HasOne(d => d.IdDirectionNavigation).WithMany(p => p.Utilisateurs)
                .HasForeignKey(d => d.IdDirection)
                .OnDelete(DeleteBehavior.ClientSetNull)
                .HasConstraintName("fki5c1kua7cyk508wt3ex1nwq3q");
        });

        OnModelCreatingPartial(modelBuilder);
    }

    partial void OnModelCreatingPartial(ModelBuilder modelBuilder);
}
