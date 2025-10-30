using Microsoft.AspNetCore.Mvc;
using banque_compte_pret.Services;
using banque_compte_pret.DTOs;

namespace banque_compte_pret.Controllers
{
    [ApiController]
    [Route("api/prets")]
    [Produces("application/json")]
    public class PretController : ControllerBase
    {
        private readonly IPretService _pretService;

        public PretController(IPretService pretService)
        {
            _pretService = pretService;
        }

        [HttpGet("client/{idClient}/somme")]
        public async Task<ActionResult<SoldeResponse>> CalculerSommePrets(int idClient)
        {
            try
            {
                var somme = await _pretService.CalculerSommePretsAsync(idClient);
                return Ok(new SoldeResponse(somme));
            }
            catch (Exception e)
            {
                return NotFound(new ErrorResponse(e.Message));
            }
        }

        [HttpPost]
        public async Task<ActionResult<PretResponse>> CreatePret([FromBody] PretRequest request)
        {
            try
            {
                var pret = await _pretService.CreerPretAsync(
                    request.IdClient,
                    request.MontantPret,
                    request.TauxInteretAnnuel,
                    request.PeriodiciteRemboursement,
                    request.DateCreation
                );
                return StatusCode(201, new PretResponse(pret));
            }
            catch (Exception e)
            {
                return BadRequest(new ErrorResponse(e.Message));
            }
        }
    }
}