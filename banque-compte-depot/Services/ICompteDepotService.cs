using banque_compte_depot.Models;

namespace banque_compte_depot.Services
{
    public interface ICompteDepotService
    {
        decimal CalculerSoldeBrut(int idClient);
        decimal CalculerInteretsGagnes(int idClient);
        decimal CalculerSoldeReel(int idClient);
        Task CrediterCompteDepot(int idClient, decimal montant, DateTime dateMouvement, string description);
        Task DebiterCompteDepot(int idClient, decimal montant, DateTime dateMouvement, string description);
        List<MouvementCompteDepot> HistoriqueMouvementCompteDepot(int idClient);
        CompteDepot GetCompteDepotByClientId(int idClient);
    }
}