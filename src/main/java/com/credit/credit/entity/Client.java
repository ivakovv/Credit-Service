package com.credit.credit.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "clients")
public class Client {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Pattern(message = "Некорректное имя",
            regexp = "^[А-ЯЁ][а-яё]+$")
    @Length(min = 2)
    @NotNull
    @Column(name = "firstname", nullable = false)
    private String firstName;

    @Pattern(message = "Некорректный формат фамилии:",
            regexp = "^[А-ЯЁ][а-яё]*(?:-[А-ЯЁ][а-яё]*)*$")
    @Length(min = 2)
    @NotNull
    @Column(name = "lastname", nullable = false)
    private String lastName;

    @Pattern(message = "Некорректное отчество",
            regexp = "^[А-ЯЁ][а-яё]+$")
    @Length(min = 2)
    @Column(name = "middle_name", nullable = true)
    private String middleName;

    @Column(name = "full_name", nullable = false)
    @NotNull
    private String fullName;

    @Pattern(message = "Некорректный формат телефона:",
            regexp = "^\\+?\\d+$")
    @Length(min = 11, max = 12)
    @NotNull
    @Column(name = "phone_number", nullable = false)
    private String phoneNumber;

    @OneToMany
    private List<Credit> credits;
}
