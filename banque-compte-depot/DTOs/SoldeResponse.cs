namespace banque_compte_depot.DTOs
{
    public class SoldeResponse
    {
        public decimal Solde { get; set; }
        public string Message { get; set; } = string.Empty;

        public SoldeResponse() { }

        public SoldeResponse(decimal solde)
        {
            Solde = solde;
            Message = "Opération réussie";
        }
    }
}