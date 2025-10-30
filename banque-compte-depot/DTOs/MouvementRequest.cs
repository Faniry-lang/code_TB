using System.ComponentModel.DataAnnotations;

namespace banque_compte_depot.DTOs
{
    public class MouvementRequest
    {
        private DateTime _dateMouvement;

        [Required]
        [Range(0.01, double.MaxValue, ErrorMessage = "Le montant doit être positif")]
        public decimal Montant { get; set; }

        [Required]
        public DateTime DateMouvement
        {
            get => _dateMouvement;
            set => _dateMouvement = value.Kind == DateTimeKind.Utc ? value : DateTime.SpecifyKind(value, DateTimeKind.Utc);
        }

        [Required]
        [StringLength(500, ErrorMessage = "La description ne peut pas dépasser 500 caractères")]
        public string Description { get; set; } = string.Empty;
    }
}