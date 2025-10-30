/**
 * Gestion du formulaire client
 * Validation, formatage et amélioration de l'expérience utilisateur
 */

class ClientFormManager {
    constructor() {
        this.config = window.clientFormConfig || {};
        this.messages = this.config.messages || {};
        this.isEditMode = this.config.isEditMode || false;

        this.init();
    }

    /**
     * Initialise le gestionnaire de formulaire
     */
    init() {
        this.setupFormValidation();
        this.setupPhoneFormatting();
        this.setupMatriculeFormatting();
        this.setupDeleteConfirmation();
        this.setupRealTimeValidation();
    }

    /**
     * Configure la validation des formulaires
     */
    setupFormValidation() {
        const forms = document.querySelectorAll('#clientForm');

        forms.forEach(form => {
            form.addEventListener('submit', (e) => {
                if (!this.validateForm(form)) {
                    e.preventDefault();
                    this.showValidationError();
                }
            });
        });
    }

    /**
     * Valide un formulaire
     * @param {HTMLFormElement} form - Le formulaire à valider
     * @returns {boolean} - True si le formulaire est valide
     */
    validateForm(form) {
        const requiredFields = form.querySelectorAll('[required]');
        let isValid = true;

        requiredFields.forEach(field => {
            const value = field.value.trim();

            if (!value) {
                this.markFieldAsInvalid(field);
                isValid = false;
            } else if (this.validateSpecificField(field, value)) {
                this.markFieldAsValid(field);
            } else {
                this.markFieldAsInvalid(field);
                isValid = false;
            }
        });

        return isValid;
    }

    /**
     * Valide un champ spécifique selon son type
     * @param {HTMLElement} field - Le champ à valider
     * @param {string} value - La valeur du champ
     * @returns {boolean} - True si le champ est valide
     */
    validateSpecificField(field, value) {
        switch (field.type) {
            case 'email':
                return this.validateEmail(value);
            case 'tel':
                return this.validatePhone(value);
            case 'date':
                return this.validateDate(value);
            default:
                return value.length > 0;
        }
    }

    /**
     * Valide une adresse email
     * @param {string} email - L'email à valider
     * @returns {boolean} - True si l'email est valide
     */
    validateEmail(email) {
        const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
        return emailRegex.test(email);
    }

    /**
     * Valide un numéro de téléphone français
     * @param {string} phone - Le téléphone à valider
     * @returns {boolean} - True si le téléphone est valide
     */
    validatePhone(phone) {
        // Accepte les formats: 0123456789, 01 23 45 67 89, 01.23.45.67.89, 01-23-45-67-89
        const phoneRegex = /^(?:(?:\+|00)33|0)\s*[1-9](?:[\s.-]*\d{2}){4}$/;
        return phoneRegex.test(phone.replace(/\s/g, ''));
    }

    /**
     * Valide une date
     * @param {string} date - La date à valider
     * @returns {boolean} - True si la date est valide
     */
    validateDate(date) {
        if (!date) return false;

        const selectedDate = new Date(date);
        const today = new Date();
        const minDate = new Date('1900-01-01');

        return selectedDate >= minDate && selectedDate <= today;
    }

    /**
     * Marque un champ comme invalide
     * @param {HTMLElement} field - Le champ à marquer
     */
    markFieldAsInvalid(field) {
        field.classList.remove('is-valid');
        field.classList.add('is-invalid');

        // Ajouter un message d'erreur spécifique si nécessaire
        this.showFieldError(field);
    }

    /**
     * Marque un champ comme valide
     * @param {HTMLElement} field - Le champ à marquer
     */
    markFieldAsValid(field) {
        field.classList.remove('is-invalid');
        field.classList.add('is-valid');

        // Supprimer le message d'erreur
        this.hideFieldError(field);
    }

    /**
     * Affiche une erreur spécifique pour un champ
     * @param {HTMLElement} field - Le champ concerné
     */
    showFieldError(field) {
        let errorMsg = '';

        switch (field.type) {
            case 'email':
                errorMsg = 'Veuillez saisir une adresse email valide.';
                break;
            case 'tel':
                errorMsg = 'Veuillez saisir un numéro de téléphone valide (10 chiffres).';
                break;
            case 'date':
                errorMsg = 'Veuillez saisir une date de naissance valide.';
                break;
            default:
                errorMsg = 'Ce champ est obligatoire.';
        }

        // Supprimer l'ancien message d'erreur s'il existe
        this.hideFieldError(field);

        // Créer et ajouter le nouveau message
        const errorElement = document.createElement('div');
        errorElement.className = 'invalid-feedback';
        errorElement.textContent = errorMsg;
        errorElement.setAttribute('data-field-error', field.id);

        field.parentNode.appendChild(errorElement);
    }

    /**
     * Masque l'erreur d'un champ
     * @param {HTMLElement} field - Le champ concerné
     */
    hideFieldError(field) {
        const existingError = field.parentNode.querySelector(`[data-field-error="${field.id}"]`);
        if (existingError) {
            existingError.remove();
        }
    }

    /**
     * Affiche une erreur générale de validation
     */
    showValidationError() {
        const message = this.messages.requiredFields || 'Veuillez corriger les erreurs dans le formulaire.';

        // Créer une alerte temporaire
        const alert = document.createElement('div');
        alert.className = 'alert alert-danger alert-dismissible fade show mt-3';
        alert.innerHTML = `
            <i class="fas fa-exclamation-triangle me-2"></i>
            ${message}
            <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
        `;

        // Insérer l'alerte avant le premier formulaire
        const form = document.querySelector('#clientForm');
        if (form) {
            form.parentNode.insertBefore(alert, form);

            // Supprimer automatiquement après 5 secondes
            setTimeout(() => {
                if (alert.parentNode) {
                    alert.remove();
                }
            }, 5000);
        }
    }

    /**
     * Configure le formatage automatique du téléphone
     */
    setupPhoneFormatting() {
        const phoneInput = document.getElementById('telephone');
        if (!phoneInput) return;

        phoneInput.addEventListener('input', (e) => {
            let value = e.target.value.replace(/\D/g, '');

            // Limiter à 10 chiffres
            if (value.length > 10) {
                value = value.substring(0, 10);
            }

            e.target.value = value;

            // Validation en temps réel
            if (value.length === 10) {
                this.markFieldAsValid(phoneInput);
            } else if (value.length > 0) {
                this.markFieldAsInvalid(phoneInput);
            }
        });

        // Formatage lors de la perte de focus
        phoneInput.addEventListener('blur', (e) => {
            const value = e.target.value;
            if (value.length === 10) {
                // Formater en 01 23 45 67 89
                const formatted = value.replace(/(\d{2})(\d{2})(\d{2})(\d{2})(\d{2})/, '$1 $2 $3 $4 $5');
                e.target.value = formatted;
            }
        });

        // Retirer le formatage lors du focus pour faciliter la saisie
        phoneInput.addEventListener('focus', (e) => {
            const value = e.target.value.replace(/\s/g, '');
            e.target.value = value;
        });
    }

    /**
     * Configure le formatage automatique du matricule
     */
    setupMatriculeFormatting() {
        const matriculeInput = document.getElementById('matricule');
        if (!matriculeInput) return;

        matriculeInput.addEventListener('input', (e) => {
            // Convertir en majuscules
            let value = e.target.value.toUpperCase();

            // Supprimer les caractères non alphanumériques
            value = value.replace(/[^A-Z0-9]/g, '');

            e.target.value = value;
        });
    }

    /**
     * Configure la confirmation de suppression
     */
    setupDeleteConfirmation() {
        const deleteForm = document.getElementById('deleteForm');
        if (!deleteForm) return;

        deleteForm.addEventListener('submit', (e) => {
            const confirmMessage = this.messages.deleteConfirm ||
                'Êtes-vous sûr de vouloir supprimer définitivement ce client ?';

            if (!confirm(confirmMessage)) {
                e.preventDefault();
            }
        });
    }

    /**
     * Configure la validation en temps réel
     */
    setupRealTimeValidation() {
        const formFields = document.querySelectorAll('#clientForm input, #clientForm textarea');

        formFields.forEach(field => {
            // Validation lors de la saisie (avec délai)
            let timeout;
            field.addEventListener('input', () => {
                clearTimeout(timeout);
                timeout = setTimeout(() => {
                    if (field.value.trim()) {
                        if (this.validateSpecificField(field, field.value.trim())) {
                            this.markFieldAsValid(field);
                        } else {
                            this.markFieldAsInvalid(field);
                        }
                    } else {
                        // Retirer les classes de validation si le champ est vide
                        field.classList.remove('is-valid', 'is-invalid');
                        this.hideFieldError(field);
                    }
                }, 500);
            });

            // Validation lors de la perte de focus
            field.addEventListener('blur', () => {
                if (field.hasAttribute('required') || field.value.trim()) {
                    const isValid = field.value.trim() &&
                        this.validateSpecificField(field, field.value.trim());

                    if (isValid) {
                        this.markFieldAsValid(field);
                    } else {
                        this.markFieldAsInvalid(field);
                    }
                }
            });
        });
    }
}

// Initialisation une fois que le DOM est chargé
document.addEventListener('DOMContentLoaded', () => {
    new ClientFormManager();
});