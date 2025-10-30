namespace banque_compte_depot.Utils;

// Convertisseur personnalisé pour les DateTime
public class JsonDateTimeConverter : System.Text.Json.Serialization.JsonConverter<DateTime>
{
    public override DateTime Read(ref System.Text.Json.Utf8JsonReader reader, Type typeToConvert, System.Text.Json.JsonSerializerOptions options)
    {
        var dateTime = reader.GetDateTime();
        // Convertir en UTC si ce n'est pas déjà le cas
        if (dateTime.Kind == DateTimeKind.Unspecified)
            return DateTime.SpecifyKind(dateTime, DateTimeKind.Utc);
        else if (dateTime.Kind == DateTimeKind.Local)
            return dateTime.ToUniversalTime();
        else
            return dateTime;
    }

    public override void Write(System.Text.Json.Utf8JsonWriter writer, DateTime value, System.Text.Json.JsonSerializerOptions options)
    {
        writer.WriteStringValue(value.ToUniversalTime());
    }
}