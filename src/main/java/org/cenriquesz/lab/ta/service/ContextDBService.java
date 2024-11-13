package org.cenriquesz.lab.ta.service;

import org.springframework.stereotype.Service;

import org.cenriquesz.lab.ta.model.entity.Context;
import org.cenriquesz.lab.ta.repository.ContextRepository;

@Service
public class ContextDBService {

  private final ContextRepository repository;

  public ContextDBService(ContextRepository repository) {
    this.repository = repository;
  }

  public void save(Context context) {
    repository.save(context);
  }
}
