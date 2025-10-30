using banque_compte_pret.Models;
using banque_compte_pret.Services.IRepositories;

namespace banque_compte_pret.Services
{
    public interface IPretService
    {
        Task<decimal> CalculerSommePretsAsync(int idClient);
        Task<Pret> CreerPretAsync(
            int idClient,
            decimal montantPret,
            decimal tauxInteretAnnuel,
            int periodiciteRemboursement,
            DateTime dateCreation);
    }

    public class PretService : IPretService
    {
        private const int STATUT_PRET_ACTIF = 1;

        private readonly IPretRepository _pretRepository;
        private readonly IClientRepository _clientRepository;
        private readonly IStatutPretRepository _statutPretRepository;
        private readonly IHistoriqueStatutPretRepository _historiqueStatutPretRepository;

        public PretService(
            IPretRepository pretRepository,
            IClientRepository clientRepository,
            IStatutPretRepository statutPretRepository,
            IHistoriqueStatutPretRepository historiqueStatutPretRepository)
        {
            _pretRepository = pretRepository;
            _clientRepository = clientRepository;
            _statutPretRepository = statutPretRepository;
            _historiqueStatutPretRepository = historiqueStatutPretRepository;
        }

        /// <summary>
        /// (5) Calcule la somme des prêts à l'instant T pour un client
        /// </summary>
        /// <param name="idClient">ID du client</param>
        /// <returns>Somme des montants des prêts non fermés</returns>
        public async Task<decimal> CalculerSommePretsAsync(int idClient)
        {
            return await _pretRepository.FindSommePretsByClientIdAsync(idClient);
        }

        public async Task<Pret> CreerPretAsync(
            int idClient,
            decimal montantPret,
            decimal tauxInteretAnnuel,
            int periodiciteRemboursement,
            DateTime dateCreation)
        {
            // Récupérer le client
            var client = await _clientRepository.FindByIdAsync(idClient);
            if (client == null)
            {
                throw new InvalidOperationException("Client non trouvé dans la base de données");
            }

            // Création de l'entité prêt
            var pret = new Pret
            {
                IdClient = client.Id,
                MontantPret = montantPret,
                TauxInteretAnnuel = tauxInteretAnnuel,
                PeriodiciteRemboursement = periodiciteRemboursement,
                DateCreation = dateCreation,
                DateFermeture = null,
                IdClientNavigation = client
            };

            pret = await _pretRepository.SaveAsync(pret);

            // Récupérer le statut prêt
            var statutActif = await _statutPretRepository.FindByIdAsync(STATUT_PRET_ACTIF);
            if (statutActif == null)
            {
                throw new InvalidOperationException("Statut 'Actif' non trouvé dans la base de données");
            }

            // Créer le mouvement historique statut pret
            var historiqueStatutPret = new HistoriqueStatutPret
            {
                IdPret = pret.Id,
                IdStatut = statutActif.Id,
                DateModification = DateTime.UtcNow,
                IdPretNavigation = pret,
                IdStatutNavigation = statutActif
            };

            await _historiqueStatutPretRepository.SaveAsync(historiqueStatutPret);

            return pret;
        }
    }
}