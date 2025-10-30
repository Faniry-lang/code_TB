namespace banque_compte_depot.DTOs
{
    public class HistoriqueMouvementsDepotResponse
    {
        public int IdClient { get; set; }
        public string NumeroCompte { get; set; } = string.Empty;
        public decimal SoldeBrut { get; set; }
        public decimal SoldeReel { get; set; }
        public decimal InteretsCumules { get; set; }
        public List<MouvementDetail> Mouvements { get; set; } = new List<MouvementDetail>();

        public HistoriqueMouvementsDepotResponse() { }

        public HistoriqueMouvementsDepotResponse(int idClient, string numeroCompte, decimal soldeBrut,
            decimal soldeReel, decimal interetsCumules,
            List<MouvementDetail> mouvements)
        {
            IdClient = idClient;
            NumeroCompte = numeroCompte;
            SoldeBrut = soldeBrut;
            SoldeReel = soldeReel;
            InteretsCumules = interetsCumules;
            Mouvements = mouvements;
        }

        public class MouvementDetail
        {
            public int IdMouvement { get; set; }
            public decimal Montant { get; set; }
            public string Description { get; set; } = string.Empty;
            public DateTime DateMouvement { get; set; }
            public string TypeMouvement { get; set; } = string.Empty;

            public MouvementDetail() { }

            public MouvementDetail(int idMouvement, decimal montant, string description,
                DateTime dateMouvement, string typeMouvement)
            {
                IdMouvement = idMouvement;
                Montant = montant;
                Description = description;
                DateMouvement = dateMouvement;
                TypeMouvement = typeMouvement;
            }
        }
    }
}