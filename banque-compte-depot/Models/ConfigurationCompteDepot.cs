using System;
using System.Collections.Generic;

namespace banque_compte_depot.Models;

public partial class ConfigurationCompteDepot
{
    public int Id { get; set; }

    public int IdCompteDepot { get; set; }

    public int LimiteRetraitMensuel { get; set; }

    public double PourcentageMaxRetrait { get; set; }

    public double TauxInteretAnnuel { get; set; }

    public DateTime DateApplication { get; set; }

    public virtual CompteDepot IdCompteDepotNavigation { get; set; } = null!;
}
