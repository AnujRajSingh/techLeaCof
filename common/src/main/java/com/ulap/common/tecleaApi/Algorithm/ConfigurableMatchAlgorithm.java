package com.ulap.util.tecleaApi.Algorithm;

import java.util.Map;

public interface ConfigurableMatchAlgorithm extends MatchAlgorithm
{
  void configure(Map<String, String> options);
}
