package hu.bme.aut.android.eszkozkolcsonzo.domain.use_case

import android.util.Patterns

class ValidateEmail {

    fun execute(email: String): ValidationResult {
        if(email.isBlank()) {
            return ValidationResult(
                successful = false,
                errorMessage = "Az email mező nem lehet üres"
            )
        }
        if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            return ValidationResult(
                successful = false,
                errorMessage = "Nem érvényes email cím"
            )
        }
        return ValidationResult(
            successful = true
        )
    }
}