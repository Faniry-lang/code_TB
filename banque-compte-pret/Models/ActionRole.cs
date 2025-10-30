using System;
using System.Collections.Generic;

namespace banque_compte_pret.Models;

public partial class ActionRole
{
    public int Id { get; set; }

    public int Role { get; set; }

    public string Action { get; set; } = null!;
}
