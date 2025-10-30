namespace banque_compte_depot.DTOs
{
    public class SuccessResponse
    {
        public string Message { get; set; } = string.Empty;
        public bool Success { get; set; } = true;

        public SuccessResponse() { }

        public SuccessResponse(string message)
        {
            Message = message;
            Success = true;
        }
    }
}