using System;
using System.Collections.Generic;

namespace banque_compte_depot.Models;

public partial class Client
{
    public DateOnly DateNaissance { get; set; }

    public int Id { get; set; }

    public DateTime DateCreation { get; set; }

    public string Matricule { get; set; } = null!;

    public string Telephone { get; set; } = null!;

    public string Email { get; set; } = null!;

    public string Adresse { get; set; } = null!;

    public string Nom { get; set; } = null!;

    public string Prenom { get; set; } = null!;

    public virtual CompteCourant? CompteCourant { get; set; }

    public virtual CompteDepot? CompteDepot { get; set; }

    public virtual ICollection<Pret> Prets { get; set; } = new List<Pret>();
}
