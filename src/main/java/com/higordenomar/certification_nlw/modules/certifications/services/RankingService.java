package com.higordenomar.certification_nlw.modules.certifications.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.higordenomar.certification_nlw.modules.students.entities.CertificationStudentEntity;
import com.higordenomar.certification_nlw.modules.students.repositories.CertificationStudentRepository;

@Service
public class RankingService {
  @Autowired
  private CertificationStudentRepository certificationStudentRepository;

  public List<CertificationStudentEntity> execute() {
    return certificationStudentRepository.findTopRankingByGradeDesc();
  }
}
