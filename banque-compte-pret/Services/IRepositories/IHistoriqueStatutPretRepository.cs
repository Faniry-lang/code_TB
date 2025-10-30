using banque_compte_pret.Data;
using banque_compte_pret.Models;

namespace banque_compte_pret.Services.IRepositories
{
    public interface IHistoriqueStatutPretRepository
    {
        Task<HistoriqueStatutPret> SaveAsync(HistoriqueStatutPret historique);
    }

    public class HistoriqueStatutPretRepository : IHistoriqueStatutPretRepository
    {
        private readonly BanqueBerthinContext _context;

        public HistoriqueStatutPretRepository(BanqueBerthinContext context)
        {
            _context = context;
        }

        public async Task<HistoriqueStatutPret> SaveAsync(HistoriqueStatutPret historique)
        {
            _context.HistoriqueStatutPrets.Add(historique);
            await _context.SaveChangesAsync();
            return historique;
        }
    }
}