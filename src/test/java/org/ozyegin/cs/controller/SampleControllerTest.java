package org.ozyegin.cs.controller;

import java.util.ArrayList;
import java.util.List;
import org.junit.Assert;
import org.junit.Test;
import org.ozyegin.cs.entity.Sample;
import org.testcontainers.shaded.org.apache.commons.lang.RandomStringUtils;

public class SampleControllerTest extends IntegrationTestSuite {
  private List<Sample> generateSamples(int size) {
    ArrayList<Sample> samples = new ArrayList<>();
    for (int i = 0; i < size; i++) {
      samples.add(new Sample()
          .name(RandomStringUtils.random(random(10), true, true))
          .data(RandomStringUtils.random(random(1000), true, true))
          .value(random(100)));
    }
    return samples;
  }

  @Test
  public void create() {
    List<Sample> samples = generateSamples(4);
    postMethod("/sample", String.class, samples, new ArrayList<>(), new ArrayList<>());
    assertTwoListEqual(sampleRepository.getAll(), samples);
  }

  @Test
  public void createFail() {
    List<Sample> samples = generateSamples(4);
    samples.get(3).setName(null);
    postMethod("/sample", String.class, samples, new ArrayList<>(), new ArrayList<>());
    Assert.assertTrue(sampleRepository.getAll().isEmpty());
  }
}
