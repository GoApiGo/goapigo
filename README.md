# GoApiGo

That is an API based on CSS Selector and HTTP Request to accelerate developments applied to the following things:
* Build a web scraper
* Build an integration based on an existing page

## How it works?

It's simple. If you have a HTML response and need to map it into an object, you just need to annotate.

## Example

You can see complete examples in our [examples repository](https://github.com/GoApiGo/goapigo-examples).

You just need to create a class and annotate your attributes with the appropriated annotation and its css selector.

Below a simple example:

```java
public class MyResponse {
  @TextBy("span:nth-child(1)")
  private Integer id;

  @TextBy("span:nth-child(2)")
  private String name;
}
```

To execute that:

```java
MyResponse myResponse = new GoApiGoProcessor().go(htmlContent, MyResponse.class);
```

## Maven dependency

Soon.