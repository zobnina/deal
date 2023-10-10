package ru.neoflex.learning.creaditpipeline.deal.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.neoflex.learning.creaditpipeline.deal.entity.Credit;

@Repository
public interface CreditRepository extends JpaRepository<Credit, Long> {
}
