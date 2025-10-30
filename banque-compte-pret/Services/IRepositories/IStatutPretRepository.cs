using banque_compte_pret.Data;
using banque_compte_pret.Models;

namespace banque_compte_pret.Services.IRepositories
{
    public interface IStatutPretRepository
    {
        Task<StatutPret?> FindByIdAsync(int id);
    }

    public class StatutPretRepository : IStatutPretRepository
    {
        private readonly BanqueBerthinContext _context;

        public StatutPretRepository(BanqueBerthinContext context)
        {
            _context = context;
        }

        public async Task<StatutPret?> FindByIdAsync(int id)
        {
            return await _context.StatutPrets.FindAsync(id);
        }
    }
}