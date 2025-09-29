package br.com.fiap.smartmottu.exception;

import java.util.function.Supplier;

public class NotFoundException extends RuntimeException{

    public NotFoundException(String message) {
        super(message);
    }

    public static Supplier<NotFoundException> forUser(Long id) {
        return () -> new NotFoundException("Usuario não achado para id" + id);
    }

    public static Supplier<NotFoundException> forEmail(String email) {
        return () -> new NotFoundException("Usuário não encontrado com e-mail" + email);
    }

    public static Supplier<NotFoundException> forMoto(Long id) {
        return () -> new NotFoundException("Moto não achado para id" + id);
    }
    public static Supplier<NotFoundException> forStatusMoto(Long id) {
        return () -> new NotFoundException("StatusMoto não achado para id" + id);
    }
    public static Supplier<NotFoundException> forTipoMoto(Long id) {
        return () -> new NotFoundException("TipoMoto não achado para id" + id);
    }

    public static Supplier<NotFoundException> forLogin() {
        return () -> new NotFoundException("Usuário não encontrado na base dados");
    }

    public static Supplier<NotFoundException> forUsuarioAluguel() {
        return () -> new NotFoundException("Usuário já possui uma moto alugada");
    }

    public static Supplier<NotFoundException> forAluguel() {
        return () -> new NotFoundException("Status ALUGADA não encontrado");
    }



}