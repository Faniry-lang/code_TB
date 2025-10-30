using System;
using System.Collections.Generic;

namespace banque_compte_pret.Models;

public partial class MouvementCompteDepot
{
    public int Id { get; set; }

    public int IdCompteDepot { get; set; }

    public int IdTypeMouvement { get; set; }

    public decimal Montant { get; set; }

    public DateTime DateMouvement { get; set; }

    public string Description { get; set; } = null!;

    public virtual CompteDepot IdCompteDepotNavigation { get; set; } = null!;

    public virtual TypeMouvementCompteDepot IdTypeMouvementNavigation { get; set; } = null!;
}
