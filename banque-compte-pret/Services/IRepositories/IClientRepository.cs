using banque_compte_pret.Data;
using banque_compte_pret.Models;

namespace banque_compte_pret.Services.IRepositories
{
    public interface IClientRepository
    {
        Task<Client?> FindByIdAsync(int id);
    }

    public class ClientRepository : IClientRepository
    {
        private readonly BanqueBerthinContext _context;

        public ClientRepository(BanqueBerthinContext context)
        {
            _context = context;
        }

        public async Task<Client?> FindByIdAsync(int id)
        {
            return await _context.Clients.FindAsync(id);
        }
    }
}