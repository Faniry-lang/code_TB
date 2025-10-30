using System;
using System.Collections.Generic;

namespace banque_compte_depot.Models;

public partial class TypeMouvementCompteDepot
{
    public int Id { get; set; }

    public string Libelle { get; set; } = null!;

    public virtual ICollection<MouvementCompteDepot> MouvementCompteDepots { get; set; } = new List<MouvementCompteDepot>();
}
