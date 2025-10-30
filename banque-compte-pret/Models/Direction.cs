using System;
using System.Collections.Generic;

namespace banque_compte_pret.Models;

public partial class Direction
{
    public int Id { get; set; }

    public int Niveau { get; set; }

    public string Libelle { get; set; } = null!;

    public virtual ICollection<Utilisateur> Utilisateurs { get; set; } = new List<Utilisateur>();
}
