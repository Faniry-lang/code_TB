using System;
using System.Collections.Generic;

namespace banque_compte_depot.Models;

public partial class MouvementCompteCourant
{
    public bool? Actif { get; set; }

    public int Id { get; set; }

    public int IdCompteCourant { get; set; }

    public int IdTypeMouvement { get; set; }

    public decimal Montant { get; set; }

    public DateTime DateMouvement { get; set; }

    public string Description { get; set; } = null!;

    public virtual CompteCourant IdCompteCourantNavigation { get; set; } = null!;

    public virtual TypeMouvementCompteCourant IdTypeMouvementNavigation { get; set; } = null!;

    public virtual ICollection<RemboursementPret> RemboursementPrets { get; set; } = new List<RemboursementPret>();
}
