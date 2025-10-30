using banque_compte_pret.Data;
using banque_compte_pret.Models;
using Microsoft.EntityFrameworkCore;

namespace banque_compte_pret.Services.IRepositories
{
    public interface IPretRepository
    {
        Task<decimal> FindSommePretsByClientIdAsync(int idClient);
        Task<Pret> SaveAsync(Pret pret);
        Task<Pret?> FindByIdAsync(int id);
    }

    public class PretRepository : IPretRepository
    {
        private readonly BanqueBerthinContext _context;

        public PretRepository(BanqueBerthinContext context)
        {
            _context = context;
        }

        public async Task<decimal> FindSommePretsByClientIdAsync(int idClient)
        {
            return await _context.Prets
                .Where(p => p.IdClient == idClient && p.DateFermeture == null)
                .SumAsync(p => p.MontantPret);
        }

        public async Task<Pret> SaveAsync(Pret pret)
        {
            _context.Prets.Add(pret);
            await _context.SaveChangesAsync();
            return pret;
        }

        public async Task<Pret?> FindByIdAsync(int id)
        {
            return await _context.Prets.FindAsync(id);
        }
    }
}