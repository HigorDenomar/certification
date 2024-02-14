package com.higordenomar.certification_nlw.modules.certifications.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.higordenomar.certification_nlw.modules.certifications.services.RankingService;
import com.higordenomar.certification_nlw.modules.students.entities.CertificationStudentEntity;

@RestController
@RequestMapping("/ranking")
public class RankingController {
  @Autowired
  private RankingService rankingService;

  @GetMapping
  public List<CertificationStudentEntity> top() {
    return rankingService.execute();
  }
}
