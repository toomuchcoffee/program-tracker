package de.toomuchcoffee.pt.domain.repository;

import de.toomuchcoffee.pt.domain.entity.Program;
import de.toomuchcoffee.pt.domain.entity.ProgramId;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProgramRepository extends JpaRepository<Program, ProgramId> {
}