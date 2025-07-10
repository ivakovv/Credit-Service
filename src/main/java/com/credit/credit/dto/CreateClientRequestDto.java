package com.credit.credit.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import org.hibernate.validator.constraints.Length;

public record CreateClientRequestDto(
        @NotNull(message = "Имя не может быть пустым")
        @Pattern(regexp = "^[А-ЯЁ][а-яё]+$", message = "Некорректное имя")
        @Length(min = 2)
        String firstName,

        @NotNull(message = "Фамилия не может быть пустой")
        @Pattern(regexp = "^[А-ЯЁ][а-яё]*(?:-[А-ЯЁ][а-яё]*)*$",
                message = "Некорректный формат фамилии")
        @Length(min = 2)
        String lastName,

        @Pattern(regexp = "^[А-ЯЁ][а-яё]+$", message = "Некорректное отчество")
        @Length(min = 2)
        String middleName,

        @NotNull(message = "Полное имя не может быть пустым")
        @Length(min = 2)
        String fullName,

        @NotNull(message = "Телефон не может быть пустым")
        @Pattern(regexp = "^\\+?\\d{11,12}$", message = "Некорректный формат телефона")
        String phoneNumber
) {}
