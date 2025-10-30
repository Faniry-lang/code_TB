using System.Text.Json.Serialization;

namespace banque_compte_pret.DTOs
{
    public class ErrorResponse
    {
        [JsonPropertyName("error")]
        public string Error { get; set; }

        [JsonPropertyName("success")]
        public bool Success { get; set; } = false;

        public ErrorResponse(string error)
        {
            Error = error;
        }
    }
}