package hu.bme.aut.android.eszkozkolcsonzo.domain.use_case

class ValidatePassword {

    fun execute(password: String): ValidationResult {
        if (password.length < 8) {
            return ValidationResult(
                successful = false,
                errorMessage = "A jelszónak legalább 8 karakter hosszúnak kell lennie"
            )
        }
        val containsLettersAndDigits = password.any { it.isDigit() } &&
                password.any { it.isLetter() }
        if (!containsLettersAndDigits) {
            return ValidationResult(
                successful = false,
                errorMessage = "A jeszónak tartalmaznia kell legalább egy betűt és legalább egy számot"
            )
        }
        return ValidationResult(
            successful = true
        )
    }
}