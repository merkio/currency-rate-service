# currency-service

Technologies:
- Maven
- Java 11
- Spring Boot
- Feign Client
- Swagger

Swagger documentation is available at \
`http://localhost:8080/swagger-ui/index.html`

Project contains Makefile and Dockerfile to build and run application \
To build image and run app:\
` make build-run ` \
Application work with currency rates from ECB.
1. To get rate for the date use path e.g. \
   `/api/exchange-api/USD/EUR?day=2021-02-19` \
   The rates can be received only for workdays. \
   If the day is not set by default service uses the current day.
   
2. To convert an amount in a given currency to another use the path \
   `/api/exchange-api/USD/EUR?day=2021-02-19&amount=10`
3. Link to the public interactive chart contains in the response ("chart_link")

Other make commands with comments you can see in the Makefile
