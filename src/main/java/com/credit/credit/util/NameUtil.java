package com.credit.credit.util;

import com.credit.credit.entity.Client;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class NameUtil {

    /**
     * Проверяет, были ли обновлены имя/фамилия/отчество
     */
    public boolean isNameUpdated(String firstName, String lastName, String middleName) {
        return firstName != null || lastName != null || middleName != null;
    }

    /**
     * Генерирует полное имя из компонентов
     */
    public String generateFullName(String lastName, String firstName, String middleName) {
        return String.join(" ",
                lastName,
                firstName,
                middleName != null ? middleName.trim() : ""
        ).trim();
    }

    /**
     * Обновляет поля имени, если они не null
     */
    public void updateNameFields(Client client, String firstName, String lastName, String middleName) {
        Optional.ofNullable(firstName).ifPresent(client::setFirstName);
        Optional.ofNullable(lastName).ifPresent(client::setLastName);
        Optional.ofNullable(middleName).ifPresent(client::setMiddleName);
    }
}