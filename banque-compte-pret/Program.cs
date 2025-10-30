using banque_compte_pret.Services;
using banque_compte_pret.Services.IRepositories;
using banque_compte_pret.Data;
using Microsoft.EntityFrameworkCore;

var builder = WebApplication.CreateBuilder(args);

// Add services to the container.
builder.Services.AddControllers()
    .AddJsonOptions(options =>
    {
        options.JsonSerializerOptions.PropertyNamingPolicy = null; // Garder le nommage exact des propriétés
    });

// Configuration de la base de données
builder.Services.AddDbContext<BanqueBerthinContext>(options =>
    options.UseNpgsql(builder.Configuration.GetConnectionString("DefaultConnection")));

// Configuration des repositories
builder.Services.AddScoped<IPretRepository, PretRepository>();
builder.Services.AddScoped<IClientRepository, ClientRepository>();
builder.Services.AddScoped<IStatutPretRepository, StatutPretRepository>();
builder.Services.AddScoped<IHistoriqueStatutPretRepository, HistoriqueStatutPretRepository>();

// Configuration des services
builder.Services.AddScoped<IPretService, PretService>();

// Swagger/OpenAPI
builder.Services.AddEndpointsApiExplorer();
builder.Services.AddSwaggerGen();

var app = builder.Build();

// Configure the HTTP request pipeline.
if (app.Environment.IsDevelopment())
{
    app.UseSwagger();
    app.UseSwaggerUI();
}

app.UseRouting();
app.MapControllers();

app.Run();