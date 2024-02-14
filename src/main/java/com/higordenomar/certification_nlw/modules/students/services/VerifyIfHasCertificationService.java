package com.higordenomar.certification_nlw.modules.students.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.higordenomar.certification_nlw.modules.students.dto.VerifyHasCertificationDTO;
import com.higordenomar.certification_nlw.modules.students.repositories.CertificationStudentRepository;

@Service
public class VerifyIfHasCertificationService {
  @Autowired
  private CertificationStudentRepository certificationStudentRepository;

  public boolean execute(VerifyHasCertificationDTO dto) {
    var result = this.certificationStudentRepository.findByStudentEmailAndTechnology(dto.getEmail(),
        dto.getTechnology());

    if (!result.isEmpty()) {
      return true;
    }

    return false;
  }
}
