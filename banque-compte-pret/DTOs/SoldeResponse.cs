using System.Text.Json.Serialization;

namespace banque_compte_pret.DTOs
{
    public class SoldeResponse
    {
        [JsonPropertyName("solde")]
        public decimal Solde { get; set; }

        [JsonPropertyName("message")]
        public string Message { get; set; }

        public SoldeResponse(decimal solde)
        {
            Solde = solde;
            Message = "Opération réussie";
        }
    }
}