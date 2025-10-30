using System;
using System.Collections.Generic;

namespace banque_compte_depot.Models;

public partial class CompteCourant
{
    public int Id { get; set; }

    public int IdClient { get; set; }

    public int IdStatut { get; set; }

    public decimal Solde { get; set; }

    public DateTime? DateFermeture { get; set; }

    public DateTime DateOuverture { get; set; }

    public string NumeroCompte { get; set; } = null!;

    public virtual Client IdClientNavigation { get; set; } = null!;

    public virtual StatutCompte IdStatutNavigation { get; set; } = null!;

    public virtual ICollection<MouvementCompteCourant> MouvementCompteCourants { get; set; } = new List<MouvementCompteCourant>();
}
