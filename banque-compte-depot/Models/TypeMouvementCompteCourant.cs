using System;
using System.Collections.Generic;

namespace banque_compte_depot.Models;

public partial class TypeMouvementCompteCourant
{
    public int Id { get; set; }

    public string Libelle { get; set; } = null!;

    public virtual ICollection<MouvementCompteCourant> MouvementCompteCourants { get; set; } = new List<MouvementCompteCourant>();
}
