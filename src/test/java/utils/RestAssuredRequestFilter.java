package utils;

import io.restassured.filter.Filter;
import io.restassured.filter.FilterContext;
import io.restassured.response.Response;
import io.restassured.specification.FilterableRequestSpecification;
import io.restassured.specification.FilterableResponseSpecification;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class RestAssuredRequestFilter implements Filter {
    @Override
    public Response filter(FilterableRequestSpecification requestSpec,
                           FilterableResponseSpecification responseSpec,
                           FilterContext ctx) {

        log.info("Making request to: {} {}", requestSpec.getMethod(), requestSpec.getURI());

        if (requestSpec.getBody() != null) {
            log.info("Request body: {}", requestSpec.getBody().toString());
        }

        Response response = ctx.next(requestSpec, responseSpec);

        log.info("Received response with status: {}", response.getStatusCode());
        log.info("Response body: {}", response.getBody().asString());

        long startTime = System.currentTimeMillis();
        long endTime = System.currentTimeMillis();


        log.info("Response Headers:");
        response.getHeaders().forEach(header ->
                log.info("{}: {}", header.getName(), header.getValue()));

        log.info("Response Time: {}ms", endTime - startTime);

        return response;
    }
}
