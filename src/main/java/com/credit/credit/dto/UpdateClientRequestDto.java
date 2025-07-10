package com.credit.credit.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import org.hibernate.validator.constraints.Length;

public record UpdateClientRequestDto(
        @Pattern(regexp = "^[А-ЯЁ][а-яё]+$", message = "Некорректное имя")
        @Length(min = 2)
        String firstName,

        @Pattern(regexp = "^[А-ЯЁ][а-яё]*(?:-[А-ЯЁ][а-яё]*)*$", message = "Некорректная фамилия")
        @Length(min = 2)
        String lastName,

        @Pattern(regexp = "^[А-ЯЁ][а-яё]+$", message = "Некорректное отчество")
        @Length(min = 2)
        String middleName,

        @Pattern(regexp = "^\\+?\\d{11,12}$", message = "Некорректный формат телефона")
        String phone
) {}
