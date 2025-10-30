namespace banque_compte_depot.DTOs
{
    public class CompteDepotDto
    {
        public int Id { get; set; }
        public int IdClient { get; set; }
        public int IdStatut { get; set; }
        public DateTime? DateFermeture { get; set; }
        public DateTime DateOuverture { get; set; }
        public string NumeroCompte { get; set; } = string.Empty;
    }
}