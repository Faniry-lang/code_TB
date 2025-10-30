namespace banque_compte_depot.DTOs
{
    public class ErrorResponse
    {
        public string Error { get; set; } = string.Empty;
        public bool Success { get; set; } = false;

        public ErrorResponse() { }

        public ErrorResponse(string error)
        {
            Error = error;
            Success = false;
        }
    }
}