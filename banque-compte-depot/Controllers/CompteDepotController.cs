using Microsoft.AspNetCore.Mvc;
using banque_compte_depot.Services;
using banque_compte_depot.DTOs;
using banque_compte_depot.Models;

namespace banque_compte_depot.Controllers
{
    [ApiController]
    [Route("api/comptes-depot")]
    public class CompteDepotController : ControllerBase
    {
        private readonly ICompteDepotService _compteDepotService;

        public CompteDepotController(ICompteDepotService compteDepotService)
        {
            _compteDepotService = compteDepotService;
        }

        /// <summary>
        /// (2) Calcule le solde brut du compte dépôt
        /// </summary>
        /// <param name="idClient">ID du client</param>
        /// <returns>Solde brut du compte</returns>
        [HttpGet("solde-brut/{idClient}")]
        [ProducesResponseType(typeof(SoldeResponse), 200)]
        [ProducesResponseType(typeof(ErrorResponse), 400)]
        [ProducesResponseType(typeof(ErrorResponse), 404)]
        public IActionResult GetSoldeBrut(int idClient)
        {
            try
            {
                var soldeBrut = _compteDepotService.CalculerSoldeBrut(idClient);
                return Ok(new SoldeResponse(soldeBrut));
            }
            catch (ArgumentException ex)
            {
                return NotFound(new ErrorResponse(ex.Message));
            }
            catch (Exception ex)
            {
                return BadRequest(new ErrorResponse($"Erreur lors du calcul du solde brut: {ex.Message}"));
            }
        }

        /// <summary>
        /// (3) Calcule les intérêts gagnés du compte dépôt
        /// </summary>
        /// <param name="idClient">ID du client</param>
        /// <returns>Montant des intérêts gagnés</returns>
        [HttpGet("interets/{idClient}")]
        [ProducesResponseType(typeof(SoldeResponse), 200)]
        [ProducesResponseType(typeof(ErrorResponse), 400)]
        [ProducesResponseType(typeof(ErrorResponse), 404)]
        public IActionResult GetInteretsGagnes(int idClient)
        {
            try
            {
                var soldeBrut = _compteDepotService.CalculerSoldeBrut(idClient);
                var soldeReel = _compteDepotService.CalculerSoldeReel(idClient);
                return Ok(new SoldeResponse(soldeReel-soldeBrut));
            }
            catch (ArgumentException ex)
            {
                return NotFound(new ErrorResponse(ex.Message));
            }
            catch (InvalidOperationException ex)
            {
                return BadRequest(new ErrorResponse(ex.Message));
            }
            catch (Exception ex)
            {
                return BadRequest(new ErrorResponse($"Erreur lors du calcul des intérêts: {ex.Message}"));
            }
        }

        /// <summary>
        /// (10) Calcule le solde réel du compte dépôt (solde brut + intérêts)
        /// </summary>
        /// <param name="idClient">ID du client</param>
        /// <returns>Solde réel du compte</returns>
        [HttpGet("solde-reel/{idClient}")]
        [ProducesResponseType(typeof(SoldeResponse), 200)]
        [ProducesResponseType(typeof(ErrorResponse), 400)]
        [ProducesResponseType(typeof(ErrorResponse), 404)]
        public IActionResult GetSoldeReel(int idClient)
        {
            try
            {
                var soldeReel = _compteDepotService.CalculerSoldeReel(idClient);
                return Ok(new SoldeResponse(soldeReel));
            }
            catch (ArgumentException ex)
            {
                return NotFound(new ErrorResponse(ex.Message));
            }
            catch (Exception ex)
            {
                return BadRequest(new ErrorResponse($"Erreur lors du calcul du solde réel: {ex.Message}"));
            }
        }

        /// <summary>
        /// (11) Crédite le compte dépôt d'un client
        /// </summary>
        /// <param name="idClient">ID du client</param>
        /// <param name="request">Requête de mouvement</param>
        /// <returns>Résultat de l'opération</returns>
        [HttpPost("crediter/{idClient}")]
        [ProducesResponseType(typeof(SuccessResponse), 200)]
        [ProducesResponseType(typeof(ErrorResponse), 400)]
        [ProducesResponseType(typeof(ErrorResponse), 404)]
        public async Task<IActionResult> CrediterCompteDepot(int idClient, [FromBody] MouvementRequest request)
        {
            try
            {
                await _compteDepotService.CrediterCompteDepot(
                    idClient, 
                    request.Montant, 
                    request.DateMouvement, 
                    request.Description
                );

                return Ok(new SuccessResponse($"Compte crédité avec succès de {request.Montant:C}"));
            }
            catch (ArgumentException ex)
            {
                return NotFound(new ErrorResponse(ex.Message));
            }
            catch (InvalidOperationException ex)
            {
                return BadRequest(new ErrorResponse(ex.Message));
            }
            catch (UnauthorizedAccessException ex)
            {
                return Unauthorized(new ErrorResponse(ex.Message));
            }
            catch (Exception ex)
            {
                return BadRequest(new ErrorResponse($"Erreur lors du crédit: {ex.Message}"));
            }
        }

        /// <summary>
        /// (12) Débite le compte dépôt d'un client
        /// </summary>
        /// <param name="idClient">ID du client</param>
        /// <param name="request">Requête de mouvement</param>
        /// <returns>Résultat de l'opération</returns>
        [HttpPost("debiter/{idClient}")]
        [ProducesResponseType(typeof(SuccessResponse), 200)]
        [ProducesResponseType(typeof(ErrorResponse), 400)]
        [ProducesResponseType(typeof(ErrorResponse), 404)]
        public async Task<IActionResult> DebiterCompteDepot(int idClient, [FromBody] MouvementRequest request)
        {
            try
            {
                await _compteDepotService.DebiterCompteDepot(
                    idClient, 
                    request.Montant, 
                    request.DateMouvement, 
                    request.Description
                );

                return Ok(new SuccessResponse($"Compte débité avec succès de {request.Montant:C}"));
            }
            catch (ArgumentException ex)
            {
                return NotFound(new ErrorResponse(ex.Message));
            }
            catch (InvalidOperationException ex)
            {
                return BadRequest(new ErrorResponse(ex.Message));
            }
            catch (UnauthorizedAccessException ex)
            {
                return Unauthorized(new ErrorResponse(ex.Message));
            }
            catch (Exception ex)
            {
                return BadRequest(new ErrorResponse($"Erreur lors du débit: {ex.Message}"));
            }
        }

        /// <summary>
        /// (15) Historique des mouvements du compte dépôt d'un client
        /// </summary>
        /// <param name="idClient">ID du client</param>
        /// <returns>Historique complet des mouvements</returns>
        [HttpGet("historique/{idClient}")]
        [ProducesResponseType(typeof(HistoriqueMouvementsDepotResponse), 200)]
        [ProducesResponseType(typeof(ErrorResponse), 400)]
        [ProducesResponseType(typeof(ErrorResponse), 404)]
        public IActionResult GetHistoriqueMouvements(int idClient)
        {
            try
            {
                var compte = _compteDepotService.GetCompteDepotByClientId(idClient);
                var mouvements = _compteDepotService.HistoriqueMouvementCompteDepot(idClient);
                var soldeBrut = _compteDepotService.CalculerSoldeBrut(idClient);
                var interetsCumules = _compteDepotService.CalculerInteretsGagnes(idClient);
                var soldeReel = soldeBrut + interetsCumules;

                var mouvementsDetails = mouvements.Select(m => new HistoriqueMouvementsDepotResponse.MouvementDetail
                {
                    IdMouvement = m.Id,
                    Montant = m.Montant,
                    Description = m.Description,
                    DateMouvement = m.DateMouvement,
                    TypeMouvement = m.IdTypeMouvementNavigation?.Libelle ?? "Inconnu"
                }).ToList();

                var response = new HistoriqueMouvementsDepotResponse
                {
                    IdClient = idClient,
                    NumeroCompte = compte.NumeroCompte,
                    SoldeBrut = soldeBrut,
                    SoldeReel = soldeReel,
                    InteretsCumules = interetsCumules,
                    Mouvements = mouvementsDetails
                };

                return Ok(response);
            }
            catch (ArgumentException ex)
            {
                return NotFound(new ErrorResponse(ex.Message));
            }
            catch (InvalidOperationException ex)
            {
                return BadRequest(new ErrorResponse(ex.Message));
            }
            catch (Exception ex)
            {
                return BadRequest(new ErrorResponse($"Erreur lors de la récupération de l'historique: {ex.Message}"));
            }
        }

        /// <summary>
        /// Récupère les informations du compte dépôt d'un client
        /// </summary>
        /// <param name="idClient">ID du client</param>
        /// <returns>Informations du compte dépôt</returns>
        [HttpGet("compte/{idClient}")]
        [ProducesResponseType(typeof(CompteDepotDto), 200)]
        [ProducesResponseType(typeof(ErrorResponse), 404)]
        public IActionResult GetCompteDepot(int idClient)
        {
            try
            {
                var compte = _compteDepotService.GetCompteDepotByClientId(idClient);
                var compteDto = new CompteDepotDto
                {
                    Id = compte.Id,
                    IdClient = compte.IdClient,
                    IdStatut = compte.IdStatut,
                    DateFermeture = compte.DateFermeture,
                    DateOuverture = compte.DateOuverture,
                    NumeroCompte = compte.NumeroCompte
                };
                return Ok(compteDto);
            }
            catch (ArgumentException ex)
            {
                return NotFound(new ErrorResponse(ex.Message));
            }
        }
    }
}