package co.edu.usbcali.finalproject.domain;

/**
 * Created by Marlon.Ramirez on 14/03/2018.
 */

public abstract class Validation {
    public void validate(String document, String password, String password2, String email) throws Exception {
        if (document == null || document.isEmpty()) {
            throw new Exception("Por favor digite el número de documento");
        }
        if (email == null || email.isEmpty()) {
            throw new Exception("Por favor digite un email valido");
        }
        if (password == null || password.isEmpty()) {
            throw new Exception("Por favor digite una contraseña");
        }
        if (password.length() < 5) {
            throw new Exception("La contraseña debe tener por lo menos 5 caracteres");
        }
        if (!password.equals(password2)) {
            throw new Exception("Las contraseñas no coinciden");
        }
    }
}
