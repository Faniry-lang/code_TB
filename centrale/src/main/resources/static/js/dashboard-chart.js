/**
 * Initialise le graphique des soldes
 * @param {string} canvasId - L'ID de l'élément canvas
 * @param {number} courants - Montant des comptes courants
 * @param {number} depot - Montant des comptes dépôt
 * @param {number} interets - Montant des intérêts
 * @returns {Chart} - L'instance du graphique Chart.js
 */
function initSoldesChart(canvasId, courants, depot, interets) {
    const ctx = document.getElementById(canvasId).getContext('2d');

    return new Chart(ctx, {
        type: 'doughnut',
        data: {
            labels: ['Comptes Courants', 'Comptes Dépôt', 'Intérêts'],
            datasets: [{
                data: [courants, depot, interets],
                backgroundColor: ['#4e73df', '#1cc88a', '#36b9cc'],
                hoverBackgroundColor: ['#2e59d9', '#17a673', '#2c9faf'],
                hoverBorderColor: "rgba(234, 236, 244, 1)",
            }],
        },
        options: {
            maintainAspectRatio: false,
            tooltips: {
                backgroundColor: "rgb(255,255,255)",
                bodyFontColor: "#858796",
                borderColor: '#dddfeb',
                borderWidth: 1,
                xPadding: 15,
                yPadding: 15,
                displayColors: false,
                caretPadding: 10,
                callbacks: {
                    label: function(tooltipItem, data) {
                        const label = data.labels[tooltipItem.index];
                        const value = data.datasets[0].data[tooltipItem.index];
                        return label + ': ' + value.toLocaleString('fr-FR', {
                            style: 'currency',
                            currency: 'EUR'
                        });
                    }
                }
            },
            legend: {
                display: true,
                position: 'bottom',
                labels: {
                    fontColor: '#858796',
                    usePointStyle: true,
                    padding: 20
                }
            },
            cutoutPercentage: 80,
        },
    });
}

/**
 * Initialise le dashboard une fois que le DOM est chargé
 */
document.addEventListener('DOMContentLoaded', function() {
    // Vérifier si l'élément canvas existe
    const canvas = document.getElementById('soldesChart');
    if (!canvas) {
        console.warn('Élément canvas "soldesChart" non trouvé');
        return;
    }

    // Vérifier si les données sont disponibles
    if (typeof window.dashboardData === 'undefined') {
        console.error('Les données du dashboard ne sont pas disponibles');
        return;
    }

    // Récupérer les données depuis l'objet global
    const { totalComptesCourants, totalComptesDepot, totalInterets } = window.dashboardData;

    // Convertir les valeurs en nombres et gérer les cas où elles seraient null/undefined
    const courants = parseFloat(totalComptesCourants) || 0;
    const depot = parseFloat(totalComptesDepot) || 0;
    const interets = parseFloat(totalInterets) || 0;

    // Vérifier qu'il y a des données à afficher
    if (courants === 0 && depot === 0 && interets === 0) {
        // Afficher un message dans le canvas si aucune donnée
        const ctx = canvas.getContext('2d');
        ctx.fillStyle = '#858796';
        ctx.font = '16px Arial';
        ctx.textAlign = 'center';
        ctx.fillText('Aucune donnée à afficher', canvas.width / 2, canvas.height / 2);
        return;
    }

    // Initialiser le graphique
    try {
        const chart = initSoldesChart('soldesChart', courants, depot, interets);
        console.log('Graphique initialisé avec succès', { courants, depot, interets });
    } catch (error) {
        console.error('Erreur lors de l\'initialisation du graphique:', error);
    }
});