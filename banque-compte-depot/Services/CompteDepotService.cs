using Microsoft.EntityFrameworkCore;
using banque_compte_depot.Data;
using banque_compte_depot.Models;
using banque_compte_depot.Services;

namespace banque_compte_depot.Services
{
    public class CompteDepotService : ICompteDepotService
    {
        private readonly BanqueBerthinContext _context;

        private const int STATUT_COMPTE_ACTIF = 1;
        private const int TYPE_MOUVEMENT_DEPOT = 1;
        private const int TYPE_MOUVEMENT_RETRAIT = 2;

        public CompteDepotService(BanqueBerthinContext context)
        {
            _context = context;
        }

        public decimal CalculerSoldeBrut(int idClient)
        {
            var compte = _context.CompteDepots
                .Include(c => c.MouvementCompteDepots)
                .ThenInclude(m => m.IdTypeMouvementNavigation)
                .FirstOrDefault(c => c.IdClient == idClient);

            if (compte == null)
            {
                throw new ArgumentException($"Compte dépôt non trouvé pour le client ID: {idClient}");
            }

            decimal soldeBrut = 0;
            foreach (var mouvement in compte.MouvementCompteDepots)
            {
                if (mouvement.IdTypeMouvementNavigation.Id == TYPE_MOUVEMENT_DEPOT)
                {
                    soldeBrut += mouvement.Montant;
                }
                else if (mouvement.IdTypeMouvementNavigation.Id == TYPE_MOUVEMENT_RETRAIT)
                {
                    soldeBrut -= mouvement.Montant;
                }
                // On ignore les intérêts (type 3) pour le solde brut
            }

            return soldeBrut;
        }

        public decimal CalculerInteretsGagnes(int idClient)
        {
            var compte = _context.CompteDepots
                .Include(c => c.ConfigurationCompteDepots)
                .FirstOrDefault(c => c.IdClient == idClient);

            if (compte == null)
            {
                throw new ArgumentException($"Compte dépôt non trouvé pour le client ID: {idClient}");
            }

            // Récupérer la dernière configuration
            var derniereConfig = compte.ConfigurationCompteDepots
                .OrderByDescending(c => c.DateApplication)
                .FirstOrDefault();

            if (derniereConfig == null)
            {
                throw new InvalidOperationException($"Aucune configuration trouvée pour le compte dépôt ID: {compte.Id}");
            }

            // Récupérer tous les mouvements triés par date (hors intérêts)
            var mouvements = _context.MouvementCompteDepots
                .Where(m => m.IdCompteDepot == compte.Id && m.IdTypeMouvement != 3) // Exclure les intérêts
                .OrderBy(m => m.DateMouvement)
                .ToList();

            // Ajouter un mouvement fictif pour la date de début (ouverture du compte)
            var tousLesMouvements = new List<MouvementCompteDepot>();
            
            // Mouvement d'ouverture fictif
            var mouvementOuverture = new MouvementCompteDepot
            {
                DateMouvement = compte.DateOuverture,
                Montant = 0
            };
            tousLesMouvements.Add(mouvementOuverture);
            tousLesMouvements.AddRange(mouvements);

            // Trier par date
            tousLesMouvements = tousLesMouvements.OrderBy(m => m.DateMouvement).ToList();

            // Calculer les intérêts avec prorata temporis
            decimal interetsTotaux = 0;
            decimal soldeCourant = 0;

            for (int i = 0; i < tousLesMouvements.Count - 1; i++)
            {
                var mouvementActuel = tousLesMouvements[i];
                var mouvementSuivant = tousLesMouvements[i + 1];

                // Mettre à jour le solde courant
                if (mouvementActuel.IdTypeMouvementNavigation != null)
                {
                    if (mouvementActuel.IdTypeMouvementNavigation.Id == TYPE_MOUVEMENT_DEPOT)
                    {
                        soldeCourant += mouvementActuel.Montant;
                    }
                    else if (mouvementActuel.IdTypeMouvementNavigation.Id == TYPE_MOUVEMENT_RETRAIT)
                    {
                        soldeCourant -= mouvementActuel.Montant;
                    }
                }

                // Calculer la durée entre les mouvements en jours
                var jours = (mouvementSuivant.DateMouvement - mouvementActuel.DateMouvement).Days;

                if (jours > 0 && soldeCourant > 0)
                {
                    // Calculer les intérêts pour cette période
                    var interetsPeriodes = CalculerInteretsPourPeriode(
                        soldeCourant, (decimal)derniereConfig.TauxInteretAnnuel, jours);
                    interetsTotaux += interetsPeriodes;
                }
            }

            // Calculer les intérêts pour la dernière période (dernier mouvement à maintenant)
            var dernierMouvement = tousLesMouvements.Last();

            // Mettre à jour le solde courant pour le dernier mouvement
            if (dernierMouvement.IdTypeMouvementNavigation != null)
            {
                if (dernierMouvement.IdTypeMouvementNavigation.Id == TYPE_MOUVEMENT_DEPOT)
                {
                    soldeCourant += dernierMouvement.Montant;
                }
                else if (dernierMouvement.IdTypeMouvementNavigation.Id == TYPE_MOUVEMENT_RETRAIT)
                {
                    soldeCourant -= dernierMouvement.Montant;
                }
            }

            var joursFin = (DateTime.Now - dernierMouvement.DateMouvement).Days;

            if (joursFin > 0 && soldeCourant > 0)
            {
                var interetsFin = CalculerInteretsPourPeriode(
                    soldeCourant, (decimal)derniereConfig.TauxInteretAnnuel, joursFin);
                interetsTotaux += interetsFin;
            }

            return Math.Round(interetsTotaux, 2);
        }

        private decimal CalculerInteretsPourPeriode(decimal solde, decimal tauxAnnuel, long jours)
        {
            return solde * tauxAnnuel * jours / 36500; // ÷ 365 et ÷ 100 pour le pourcentage
        }

        public decimal CalculerSoldeReel(int idClient)
        {
            var soldeBrut = CalculerSoldeBrut(idClient);
            var interets = CalculerInteretsGagnes(idClient);

            return soldeBrut + interets;
        }

        public async Task CrediterCompteDepot(int idClient, decimal montant, DateTime dateMouvement, string description)
        {
            // Utilisez directement DateMouvementUtc depuis le DTO
            // Cette méthode sera appelée avec le DateTime déjà converti
            var compte = _context.CompteDepots
                .Include(c => c.IdStatutNavigation)
                .FirstOrDefault(c => c.IdClient == idClient);

            if (compte == null)
            {
                throw new ArgumentException($"Compte dépôt non trouvé pour le client ID: {idClient}");
            }

            // Vérifier si le compte est actif
            if (compte.IdStatut != STATUT_COMPTE_ACTIF)
            {
                throw new InvalidOperationException("Impossible d'effectuer l'opération : le compte n'est pas actif");
            }

            // Validation du montant
            if (montant <= 0)
            {
                throw new ArgumentException("Le montant doit être positif");
            }

            var typeMouvement = await _context.TypeMouvementCompteDepots.FindAsync(TYPE_MOUVEMENT_DEPOT);

            // Créer le mouvement avec la date déjà en UTC
            var mouvement = new MouvementCompteDepot
            {
                Montant = montant,
                Description = description,
                DateMouvement = dateMouvement, // Doit déjà être en UTC
                IdTypeMouvement = typeMouvement.Id,
                IdCompteDepot = compte.Id
            };

            _context.MouvementCompteDepots.Add(mouvement);
            await _context.SaveChangesAsync();
        }

        public async Task DebiterCompteDepot(int idClient, decimal montant, DateTime dateMouvement, string description)
        {
            var compte = _context.CompteDepots
                .Include(c => c.IdStatutNavigation)
                .FirstOrDefault(c => c.IdClient == idClient);

            if (compte == null)
            {
                throw new ArgumentException($"Compte dépôt non trouvé pour le client ID: {idClient}");
            }

            // Vérifier si le compte est actif
            if (compte.IdStatut != STATUT_COMPTE_ACTIF)
            {
                throw new InvalidOperationException("Impossible d'effectuer l'opération : le compte n'est pas actif");
            }

            // Validation du montant
            if (montant <= 0)
            {
                throw new ArgumentException("Le montant doit être positif");
            }

            // Récupérer la dernière configuration
            var config = _context.ConfigurationCompteDepots
                .Where(c => c.IdCompteDepot == compte.Id)
                .OrderByDescending(c => c.DateApplication)
                .FirstOrDefault();

            if (config == null)
            {
                throw new InvalidOperationException("Aucune configuration trouvée pour le compte dépôt");
            }

            // CORRECTION : Créer debutMois en UTC
            var debutMois = new DateTime(DateTime.UtcNow.Year, DateTime.UtcNow.Month, 1, 0, 0, 0, DateTimeKind.Utc);

            // Vérifier le nombre maximal de retraits ce mois-ci
            var retraitsCeMois = _context.MouvementCompteDepots
                .Count(m => m.IdCompteDepot == compte.Id && 
                           m.IdTypeMouvement == TYPE_MOUVEMENT_RETRAIT &&
                           m.DateMouvement >= debutMois);

            if (retraitsCeMois >= config.LimiteRetraitMensuel)
            {
                throw new InvalidOperationException($"Limite de retraits mensuels atteinte: {config.LimiteRetraitMensuel}");
            }

            // Vérifier le pourcentage maximum de retrait par rapport au solde réel
            var soldeReel = CalculerSoldeReel(idClient);
            var pourcentageRetrait = (montant * 100) / soldeReel;

            if (pourcentageRetrait > (decimal)config.PourcentageMaxRetrait)
            {
                throw new InvalidOperationException(
                    $"Le retrait dépasse le pourcentage maximum autorisé: {pourcentageRetrait:F2}% > {config.PourcentageMaxRetrait:F2}%");
            }

            // Vérifier que le solde brut est suffisant
            var soldeBrut = CalculerSoldeBrut(idClient);
            if (soldeBrut < montant)
            {
                throw new InvalidOperationException("Solde brut insuffisant pour effectuer le retrait");
            }

            var typeMouvement = await _context.TypeMouvementCompteDepots.FindAsync(TYPE_MOUVEMENT_RETRAIT);

            // Créer le mouvement avec la date en UTC
            var mouvement = new MouvementCompteDepot
            {
                Montant = montant,
                Description = description,
                DateMouvement = dateMouvement, // Doit déjà être en UTC
                IdTypeMouvement = typeMouvement.Id,
                IdCompteDepot = compte.Id
            };

            _context.MouvementCompteDepots.Add(mouvement);
            await _context.SaveChangesAsync();
        }

        public List<MouvementCompteDepot> HistoriqueMouvementCompteDepot(int idClient)
        {
            var compte = _context.CompteDepots
                .Include(c => c.IdStatutNavigation)
                .FirstOrDefault(c => c.IdClient == idClient);

            if (compte == null)
            {
                throw new ArgumentException($"Compte dépôt non trouvé pour le client ID: {idClient}");
            }

            // Vérifier si le compte est actif
            if (compte.IdStatut != STATUT_COMPTE_ACTIF)
            {
                throw new InvalidOperationException("Impossible d'effectuer l'opération : le compte n'est pas actif");
            }

            // Obtenir les mouvements du compte dépôt
            return _context.MouvementCompteDepots
                .Include(m => m.IdTypeMouvementNavigation)
                .Where(m => m.IdCompteDepot == compte.Id)
                .OrderByDescending(m => m.DateMouvement)
                .ToList();
        }

        public CompteDepot GetCompteDepotByClientId(int idClient)
        {
            var compte = _context.CompteDepots
                .Include(c => c.IdStatutNavigation)
                .FirstOrDefault(c => c.IdClient == idClient);

            if (compte == null)
            {
                throw new ArgumentException($"Compte dépôt non trouvé pour le client ID: {idClient}");
            }
            return compte;
        }
    }
}