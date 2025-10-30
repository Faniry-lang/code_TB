using System;
using System.Collections.Generic;

namespace banque_compte_depot.Models;

public partial class Utilisateur
{
    public int Id { get; set; }

    public int IdDirection { get; set; }

    public int Role { get; set; }

    public string Matricule { get; set; } = null!;

    public string Password { get; set; } = null!;

    public virtual Direction IdDirectionNavigation { get; set; } = null!;
}
