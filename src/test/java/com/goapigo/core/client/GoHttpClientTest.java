package com.goapigo.core.client;

import com.github.tomakehurst.wiremock.junit.WireMockRule;
import com.goapigo.core.annotations.GoApiGo;
import com.goapigo.core.annotations.TextBy;
import com.goapigo.core.client.annotations.*;
import com.goapigo.core.exception.HttpClientException;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static com.goapigo.core.client.GoHttpClient.go;
import static org.assertj.core.api.Assertions.assertThat;

@RunWith(MockitoJUnitRunner.class)
public class GoHttpClientTest {

  @Rule public WireMockRule wireMockRule = new WireMockRule(8089);

  @Test
  public void goWithInterface() {
    stubFor(
        get(urlEqualTo("/mock"))
            .willReturn(
                aResponse()
                    .withStatus(200)
                    .withHeader("Content-Type", "text/html")
                    .withBody("<span id=\"id\">1</span><span id=\"name\">Aroldo</span>")));

    MockedHttpClient httpClient = go(MockedHttpClient.class);
    assertThat(httpClient).isNotNull();
    MockedResponse response = httpClient.get();
    assertThat(response.id).isNotNull().isEqualTo(1);
    assertThat(response.name).isNotNull().isEqualTo("Aroldo");
  }

  @Test
  public void goWithPathParameter() {
    stubFor(
        get(urlEqualTo("/mock/someName"))
            .willReturn(
                aResponse()
                    .withStatus(200)
                    .withHeader("Content-Type", "text/html")
                    .withBody("<span id=\"id\">1</span><span id=\"name\">someName</span>")));

    MockedHttpClient httpClient = go(MockedHttpClient.class);
    assertThat(httpClient).isNotNull();
    MockedResponse response = httpClient.get("someName");
    assertThat(response.id).isNotNull().isEqualTo(1);
    assertThat(response.name).isNotNull().isEqualTo("someName");
  }

  @Test
  public void goWithoutExplicitVerb() {
    stubFor(
        get(urlEqualTo("/mock"))
            .willReturn(
                aResponse()
                    .withStatus(200)
                    .withHeader("Content-Type", "text/html")
                    .withBody("<span id=\"id\">1</span><span id=\"name\">Aroldo</span>")));

    MockedHttpClient httpClient = go(MockedHttpClient.class);
    assertThat(httpClient).isNotNull();
    MockedResponse response = httpClient.getWithoutExplicitVerb();
    assertThat(response.id).isNotNull().isEqualTo(1);
    assertThat(response.name).isNotNull().isEqualTo("Aroldo");
  }

  @Test
  public void goWithHeader() {
    stubFor(
        get(urlEqualTo("/mock/header"))
            .withHeader("authorization", equalTo("ABC"))
            .willReturn(
                aResponse()
                    .withStatus(200)
                    .withHeader("Content-Type", "text/html")
                    .withBody("<span id=\"id\">1</span><span id=\"name\">Aroldo</span>")));

    MockedHttpClient httpClient = go(MockedHttpClient.class);
    assertThat(httpClient).isNotNull();
    MockedResponse response = httpClient.getWithHeader("ABC");
    assertThat(response.id).isNotNull().isEqualTo(1);
    assertThat(response.name).isNotNull().isEqualTo("Aroldo");
  }

  @Test(expected = HttpClientException.class)
  public void goWithInterfaceNotAnnotated() {
    go(HttpClientNotAnnotated.class);
  }

  @Test(expected = HttpClientException.class)
  public void goWhenNotIsInterface() {
    go(HttpClientNotIsInterface.class);
  }

  @GoClient(url = "http://localhost:8089")
  interface MockedHttpClient {
    @GoGet
    @GoPath("/mock")
    MockedResponse get();

    @GoPath("/mock")
    MockedResponse getWithoutExplicitVerb();

    @GoGet
    @GoPath("/mock/{name}")
    MockedResponse get(@GoPathParam("name") String name);

    @GoPath("/mock/header")
    MockedResponse getWithHeader(@GoHeader("authorization") String authorization);
  }

  interface HttpClientNotAnnotated {}

  @GoApiGo
  public static class MockedResponse {
    @TextBy("#id")
    private Integer id;

    @TextBy("#name")
    private String name;
  }

  class HttpClientNotIsInterface {}
}
