package com.teat.teat_backend.repository;

import com.teat.teat_backend.entity.TestRun;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface TestRunRepository extends JpaRepository<TestRun, UUID> {
}
