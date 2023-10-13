package ru.neoflex.learning.creaditpipeline.deal.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.SequenceGenerator;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.FieldDefaults;
import org.hibernate.Hibernate;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;
import ru.neoflex.learning.creaditpipeline.deal.model.Gender;
import ru.neoflex.learning.creaditpipeline.deal.model.MaritalStatus;

import java.time.LocalDate;
import java.util.Objects;

@Getter
@Setter
@ToString
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Client {

    @Id
    @SequenceGenerator(name = "client_id_seq", sequenceName = "client_id_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "client_id_seq")
    Long id;

    String lastName;

    String firstName;

    String middleName;

    LocalDate birthDate;

    String email;

    @Enumerated(EnumType.STRING)
    Gender gender;

    @Enumerated(EnumType.STRING)
    MaritalStatus maritalStatus;

    Short dependentAmount;

    @JdbcTypeCode(SqlTypes.JSON)
    Passport passport;

    @JdbcTypeCode(SqlTypes.JSON)
    Employment employment;

    String account;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        Client client = (Client) o;
        return id != null && Objects.equals(id, client.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}