package cl.duoc.rinconsur.domain.validation

import android.util.Patterns

object Validators {
    /*
        funcion para validar que en el nombre
        haya escrito algo
     */
    fun validarNombre(nombre: String): String?{
        if (nombre.isBlank()){
            return "Ingresa tu nombre"
        }
        return null
    }
    /*
        validamos que escriba un email y que
        sea en formato correcto @
     */
    fun validarEmail(email: String): String?{
        val emailLimpio = email.trim()
        if (emailLimpio.isEmpty()){
            return "Ingresa tu correo"
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(emailLimpio).matches()){
            return "Ingresa un correo válido"
        }
        return null
    }
    /*
        validamos que la contraseña no este vacia
     */
    fun validarContrasena(contrasena: String): String?{
        if (contrasena.isBlank()){
            return "Ingresa tu contraseña"
        }
        return  null
    }
}