using System;
using System.Collections.Generic;

namespace banque_compte_pret.Models;

public partial class RemboursementPret
{
    public int Id { get; set; }

    public int IdMouvementCompteCourant { get; set; }

    public int IdPret { get; set; }

    public decimal InteretPaye { get; set; }

    public decimal MontantRembourse { get; set; }

    public DateTime DatePaiement { get; set; }

    public virtual MouvementCompteCourant IdMouvementCompteCourantNavigation { get; set; } = null!;

    public virtual Pret IdPretNavigation { get; set; } = null!;
}
