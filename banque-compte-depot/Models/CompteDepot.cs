using System;
using System.Collections.Generic;

namespace banque_compte_depot.Models;

public partial class CompteDepot
{
    public int Id { get; set; }

    public int IdClient { get; set; }

    public int IdStatut { get; set; }

    public DateTime? DateFermeture { get; set; }

    public DateTime DateOuverture { get; set; }

    public string NumeroCompte { get; set; } = null!;

    public virtual ICollection<ConfigurationCompteDepot> ConfigurationCompteDepots { get; set; } = new List<ConfigurationCompteDepot>();

    public virtual Client IdClientNavigation { get; set; } = null!;

    public virtual StatutCompte IdStatutNavigation { get; set; } = null!;

    public virtual ICollection<MouvementCompteDepot> MouvementCompteDepots { get; set; } = new List<MouvementCompteDepot>();
}
