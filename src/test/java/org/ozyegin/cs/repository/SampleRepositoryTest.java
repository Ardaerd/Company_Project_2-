package org.ozyegin.cs.repository;

import java.util.ArrayList;
import java.util.List;
import org.junit.Assert;
import org.junit.Test;
import org.ozyegin.cs.entity.Sample;
import org.testcontainers.shaded.org.apache.commons.lang.RandomStringUtils;

public class SampleRepositoryTest extends IntegrationTestSuite {
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
  public void createAndGetListSample() {
    List<Sample> samples = generateSamples(40);
    sampleRepository.create(samples);
    List<Sample> dbSamples = sampleRepository.getAll();
    int i = 0;
    assertTwoListEqual(samples, dbSamples);
  }

  @Test
  public void updateSample() throws Exception {
    List<Sample> samples = generateSamples(1);
    sampleRepository.create(samples);
    samples.get(0).setData("data");
    sampleRepository.update(samples.get(0).id(1));
    List<Sample> dbSample = sampleRepository.getAll();

    Assert.assertEquals(samples.get(0).data("data"), dbSample.get(0));
  }

  @Test(expected = Exception.class)
  public void deleteSample() throws Exception {
    List<Sample> samples = generateSamples(1);
    sampleRepository.create(samples);
    samples.get(0).setData("data");
    sampleRepository.delete(1);
    sampleRepository.getAll();
  }
}
