package com.ravuri.calibration.repository;

import com.ravuri.calibration.entity.TestDocument;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TestDocumentRepository extends JpaRepository<TestDocument,Long> {
}
