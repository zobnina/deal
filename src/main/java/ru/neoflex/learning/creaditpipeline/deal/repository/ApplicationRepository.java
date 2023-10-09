package ru.neoflex.learning.creaditpipeline.deal.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.neoflex.learning.creaditpipeline.deal.entity.Application;

@Repository
public interface ApplicationRepository extends JpaRepository<Application, Long> {
}
