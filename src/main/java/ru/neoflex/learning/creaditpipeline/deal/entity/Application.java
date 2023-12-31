package ru.neoflex.learning.creaditpipeline.deal.entity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.SequenceGenerator;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;
import ru.neoflex.learning.creaditpipeline.deal.dictionary.ApplicationStatus;

import java.time.LocalDate;

@Getter
@Setter
@ToString
@Builder
@Accessors(chain = true)
@FieldDefaults(level = AccessLevel.PRIVATE)
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Application {

    @Id
    @SequenceGenerator(name = "application_id_seq", sequenceName = "application_id_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "application_id_seq")
    Long id;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @ToString.Exclude
    Client client;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @ToString.Exclude
    Credit credit;

    @Enumerated(EnumType.STRING)
    ApplicationStatus status;

    LocalDate creationDate;

    @JdbcTypeCode(SqlTypes.JSON)
    LoanOffer appliedOffer;

    LocalDate signDate;

    String sesCode;

    @Builder.Default
    @JdbcTypeCode(SqlTypes.JSON)
    ApplicationStatusHistoryList statusHistory = ApplicationStatusHistoryList.builder().build();
}