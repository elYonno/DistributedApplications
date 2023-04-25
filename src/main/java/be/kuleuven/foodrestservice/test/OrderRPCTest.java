package be.kuleuven.foodrestservice.test;

import be.kuleuven.foodrestservice.domain.*;
import com.google.gson.*;
import org.apache.http.HttpEntity;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;

public class OrderRPCTest {
    private static final String TARGET_URL = "http://localhost:8080/restrpc/addOrder";
    private static final Gson gson = new GsonBuilder().registerTypeAdapter(
            LocalDateTime.class,
            (JsonDeserializer<LocalDateTime>) (json, type, jsonDeserializationContext) -> {
                Instant instant = Instant.ofEpochMilli(json.getAsJsonPrimitive().getAsLong());
                return LocalDateTime.ofInstant(instant, ZoneId.systemDefault());
            }
    ).create();

    public static void main(String[] args) throws IOException {
        final CloseableHttpClient client = HttpClients.createDefault();
        final HttpPut httpPut = new HttpPut(TARGET_URL);

        Order order = new Order(
                "Engels Plein 14",
                List.of(
                        "5268203c-de76-4921-a3e3-439db69c462a",
                        "cfd1601f-29a0-485d-8d21-7607ec0340c8"
                )
        );

        String outJson = gson.toJson(order);

        httpPut.setHeader("Accept", "application/json");
        httpPut.setHeader("Content-type", "application/json");
        httpPut.setEntity(new StringEntity(outJson));

        String response = client.execute(httpPut, httpResponse -> {
           int status = httpResponse.getStatusLine().getStatusCode();

           if (status >= 200 && status < 300) {
               HttpEntity entity = httpResponse.getEntity();

               if (entity != null) return EntityUtils.toString(entity);
               else return null;
           } else {
               throw new ClientProtocolException("Response = " + status);
           }
        });

        System.out.println(response);

        //TODO: assert functions
//        OrderConfirmation confirmation = gson.fromJson(response, OrderConfirmation.class);
//
//        check(1, confirmation.address().equals(order.getAddress()));
//        check(2, confirmation.total() == 15.00);
    }

    private static void check(int number, boolean pass) {
        if (pass)
            System.out.println("Test " + number + ": [PASSED]");
        else
            System.out.println("Test " + number + ": [FAILED]");
    }
}
