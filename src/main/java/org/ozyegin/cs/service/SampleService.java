package org.ozyegin.cs.service;

import java.util.List;
import org.ozyegin.cs.entity.Sample;
import org.ozyegin.cs.repository.SampleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SampleService {
  @Autowired
  private SampleRepository sampleRepository;

  public void create(List<Sample> samples) {
    sampleRepository.create(samples);
  }
}
