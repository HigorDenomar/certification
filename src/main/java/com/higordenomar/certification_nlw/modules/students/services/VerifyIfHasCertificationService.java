package com.higordenomar.certification_nlw.modules.students.services;

import org.springframework.stereotype.Service;

import com.higordenomar.certification_nlw.modules.students.dto.VerifyHasCertificationDTO;

@Service
public class VerifyIfHasCertificationService {
  public boolean execute(VerifyHasCertificationDTO dto) {
    if (dto.getEmail().equals("higordenomar@gmail.com") &&
        dto.getTechnology().equals("Java")) {
      return true;
    }

    return false;
  }
}
