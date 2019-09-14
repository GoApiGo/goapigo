package com.goapigo.core.client;

import com.goapigo.core.client.annotations.GoClient;
import com.goapigo.core.client.callback.HttpClientCallback;
import com.goapigo.core.exception.HttpClientException;
import net.sf.cglib.proxy.Enhancer;

public class GoHttpClient {

  public static <T> T go(Class<T> httpClient) {
    if (!httpClient.isAnnotationPresent(GoClient.class)) {
      throw new HttpClientException("Interface must be annotated by @GoClient");
    }
    if (!httpClient.isInterface()) {
      throw new HttpClientException("Must be an interface");
    }
    Enhancer enhancer = new Enhancer();
    enhancer.setSuperclass(httpClient);
    enhancer.setCallback(new HttpClientCallback());
    return (T) enhancer.create();
  }
}
