using System.Text.Json.Serialization;
using banque_compte_pret.Models;

namespace banque_compte_pret.DTOs
{
    public class PretResponse
    {
        [JsonPropertyName("idPret")]
        public int IdPret { get; set; }

        [JsonPropertyName("montantPret")]
        public decimal MontantPret { get; set; }

        [JsonPropertyName("tauxInteretAnnuel")]
        public decimal TauxInteretAnnuel { get; set; }

        [JsonPropertyName("periodiciteRemboursement")]
        public int PeriodiciteRemboursement { get; set; }

        [JsonPropertyName("dateCreation")]
        public DateTime DateCreation { get; set; }

        [JsonPropertyName("dateFermeture")]
        public DateTime? DateFermeture { get; set; }

        public PretResponse(Pret pret)
        {
            IdPret = pret.Id;
            MontantPret = pret.MontantPret;
            TauxInteretAnnuel = pret.TauxInteretAnnuel;
            PeriodiciteRemboursement = pret.PeriodiciteRemboursement;
            DateCreation = pret.DateCreation;
            DateFermeture = pret.DateFermeture;
        }
    }
}