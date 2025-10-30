using System;
using System.Collections.Generic;

namespace banque_compte_pret.Models;

public partial class Pret
{
    public int Id { get; set; }

    public int IdClient { get; set; }

    public decimal MontantPret { get; set; }

    public int PeriodiciteRemboursement { get; set; }

    public decimal TauxInteretAnnuel { get; set; }

    public DateTime DateCreation { get; set; }

    public DateTime? DateFermeture { get; set; }

    public virtual ICollection<HistoriqueStatutPret> HistoriqueStatutPrets { get; set; } = new List<HistoriqueStatutPret>();

    public virtual Client IdClientNavigation { get; set; } = null!;

    public virtual ICollection<RemboursementPret> RemboursementPrets { get; set; } = new List<RemboursementPret>();
}
