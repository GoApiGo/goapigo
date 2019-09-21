package com.goapigo.core.client.callback;

import com.goapigo.core.GoApiGoProcessor;
import com.goapigo.core.client.annotations.GoClient;
import com.goapigo.core.client.annotations.GoHeader;
import com.goapigo.core.client.annotations.GoPath;
import com.goapigo.core.client.annotations.GoPathParam;
import lombok.var;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Method;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.*;
import java.util.stream.Collectors;

public class HttpClientCallback implements MethodInterceptor {
  @Override
  public Object intercept(Object object, Method method, Object[] objects, MethodProxy methodProxy)
      throws Throwable {
    var goClient = method.getDeclaringClass().getAnnotation(GoClient.class);
    var url = goClient.url();
    var goPath = method.getAnnotation(GoPath.class);
    var path = goPath != null ? goPath.value() : "";
    var httpVerb =
        Arrays.stream(HttpVerbs.values())
            .filter(verb -> method.isAnnotationPresent(verb.getAnnotationClass()))
            .findFirst()
            .orElse(HttpVerbs.GET);
    var connectionUrl = url + path;
    var processedUrl = processPathParam(connectionUrl, method, objects);
    var headers = getHeaders(method, objects);
    var htmlContent = doHttpRequest(httpVerb, processedUrl, headers);
    return new GoApiGoProcessor().go(htmlContent, method.getReturnType());
  }

  private Map<String, String> getHeaders(Method method, Object[] parametersValues) {
    var methodParameters = Arrays.asList(method.getParameters());
    return Arrays.stream(method.getParameters())
        .filter(parameter -> parameter.isAnnotationPresent(GoHeader.class))
        .map(
            parameter -> {
              var annotation = parameter.getAnnotation(GoHeader.class);
              var headerName = Optional.of(annotation.value()).orElse(parameter.getName());
              var headerValue = parametersValues[methodParameters.indexOf(parameter)];
              return new AbstractMap.SimpleEntry<>(headerName, headerValue.toString());
            })
        .collect(
            Collectors.toMap(AbstractMap.SimpleEntry::getKey, AbstractMap.SimpleEntry::getValue));
  }

  private String processPathParam(String connectionUrl, Method method, Object[] objects) {
    List<String> pathParameterNames =
        Arrays.stream(method.getParameters())
            .filter(parameter -> parameter.isAnnotationPresent(GoPathParam.class))
            .map(
                parameter -> {
                  var annotation = parameter.getAnnotation(GoPathParam.class);
                  return Optional.of(annotation.value()).orElse(parameter.getName());
                })
            .collect(Collectors.toList());
    return pathParameterNames.stream()
        .reduce(
            connectionUrl,
            (url, pathParam) -> {
              int parameterIndex = pathParameterNames.indexOf(pathParam);
              return url.replace("{" + pathParam + "}", objects[parameterIndex].toString());
            });
  }

  private String doHttpRequest(
      HttpVerbs httpVerb, String connectionUrl, Map<String, String> headers) throws IOException {
    HttpURLConnection connection = (HttpURLConnection) new URL(connectionUrl).openConnection();
    connection.setRequestMethod(httpVerb.name());
    headers.entrySet().stream()
        .forEach(entry -> connection.addRequestProperty(entry.getKey(), entry.getValue()));
    connection.connect();
    return new BufferedReader(new InputStreamReader(connection.getInputStream()))
        .lines()
        .reduce(String::concat)
        .orElse("");
  }
}
