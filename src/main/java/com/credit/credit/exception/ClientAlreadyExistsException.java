package com.credit.credit.exception;

public class ClientAlreadyExistsException extends RuntimeException {

    public ClientAlreadyExistsException(String phoneNumber) {
        super("Клиент с номером " + phoneNumber + " уже существует");
    }
}
