using System.Text.Json.Serialization;

namespace banque_compte_pret.DTOs
{
    public class PretRequest
    {
        [JsonPropertyName("idClient")]
        public int IdClient { get; set; }

        [JsonPropertyName("montantPret")]
        public decimal MontantPret { get; set; }

        [JsonPropertyName("tauxInteretAnnuel")]
        public decimal TauxInteretAnnuel { get; set; }

        [JsonPropertyName("periodiciteRemboursement")]
        public int PeriodiciteRemboursement { get; set; }

        [JsonPropertyName("dateCreation")]
        public DateTime DateCreation { get; set; }
    }
}