package ca.jrvs.apps.twitter.example;

import ca.jrvs.apps.twitter.example.dto.Company;
import ca.jrvs.apps.twitter.example.dto.Dividend;
import ca.jrvs.apps.twitter.example.dto.Financial;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.SerializationFeature;

import okhttp3.Headers;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

import java.io.IOException;
import java.net.URI;

public class JasonParser {

    public static final String companyStr = "{\n"
            + "   \"symbol\":\"AAPL\",\n"
            + "   \"companyName\":\"Apple Inc.\",\n"
            + "   \"exchange\":\"Nasdaq Global Select\",\n"
            + "   \"description\":\"Apple Inc is designs, manufactures and markets mobile communication and media devices and personal computers, and sells a variety of related software, services, accessories, networking solutions and third-party digital content and applications.\",\n"
            + "   \"CEO\":\"Timothy D. Cook\",\n"
            + "   \"sector\":\"Technology\",\n"
            + "   \"financials\":[\n"
            + "      {\n"
            + "         \"reportDate\":\"2018-12-31\",\n"
            + "         \"grossProfit\":32031000000,\n"
            + "         \"costOfRevenue\":52279000000,\n"
            + "         \"operatingRevenue\":84310000000,\n"
            + "         \"totalRevenue\":84310000000,\n"
            + "         \"operatingIncome\":23346000000,\n"
            + "         \"netIncome\":19965000000\n"
            + "      },\n"
            + "      {\n"
            + "         \"reportDate\":\"2018-09-30\",\n"
            + "         \"grossProfit\":24084000000,\n"
            + "         \"costOfRevenue\":38816000000,\n"
            + "         \"operatingRevenue\":62900000000,\n"
            + "         \"totalRevenue\":62900000000,\n"
            + "         \"operatingIncome\":16118000000,\n"
            + "         \"netIncome\":14125000000\n"
            + "      }\n"
            + "   ],\n"
            + "   \"dividends\":[\n"
            + "      {\n"
            + "         \"exDate\":\"2018-02-09\",\n"
            + "         \"paymentDate\":\"2018-02-15\",\n"
            + "         \"recordDate\":\"2018-02-12\",\n"
            + "         \"declaredDate\":\"2018-02-01\",\n"
            + "         \"amount\":0.63\n"
            + "      },\n"
            + "      {\n"
            + "         \"exDate\":\"2017-11-10\",\n"
            + "         \"paymentDate\":\"2017-11-16\",\n"
            + "         \"recordDate\":\"2017-11-13\",\n"
            + "         \"declaredDate\":\"2017-11-02\",\n"
            + "         \"amount\":0.63\n"
            + "      }\n"
            + "   ]\n"
            + "}";

    public static String toJson(Object object, boolean prettyJson, boolean includeNullValues)
            throws JsonProcessingException {
        ObjectMapper m = new ObjectMapper();
        if (!includeNullValues) {
            m.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        }
        if (prettyJson) {
            m.enable(SerializationFeature.INDENT_OUTPUT);
        }
        return m.writeValueAsString(object);
    }

    public static <T> T toObjectFromJson(String json, Class clazz) throws IOException {
        ObjectMapper m = new ObjectMapper();
        return (T) m.readValue(json, clazz);
    }

    public static void main(String[] args) throws IOException {
        Company company = toObjectFromJson(companyStr, Company.class);
        System.out.println(toJson(company, true, false));

        // Convert JSON string to JsonNode
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonNode = objectMapper.readTree(companyStr);
        // Get value of a specific key
        JsonNode financialsNode = jsonNode.get("financials");
        String financialJson = financialsNode.get(0).toString();
        Financial financial = toObjectFromJson(financialJson, Financial.class);
        System.out.println(toJson(financial, true, false));

        JsonNode dividendsNode = jsonNode.get("dividends");
        String dividendJson = dividendsNode.get(0).toString();
        Dividend dividend = toObjectFromJson(dividendJson, Dividend.class);
        System.out.println(toJson(dividend, true, false));

//        String symbol = "MSFT";
//        String apiKey = "54706e6343msh96825dc87a0b044p188036jsn4bab408b03a2";
        OkHttpClient client = new OkHttpClient();

        Request request = new Request.Builder()
                .url("https://alpha-vantage.p.rapidapi.com/query?interval=5min&function=TIME_SERIES_INTRADAY&symbol=MSFT&datatype=json&output_size=compact")
                .get()
                .addHeader("X-RapidAPI-Key", "54706e6343msh96825dc87a0b044p188036jsn4bab408b03a2")
                .addHeader("X-RapidAPI-Host", "alpha-vantage.p.rapidapi.com")
                .build();

        Response response = client.newCall(request).execute();
        // Get and output other headers if needed
        //String responseBody = response.body().string();
//        // Parse the JSON response
//        JsonObject jsonResponse = JsonParser.parseString(responseBody).getAsJsonObject();
//        System.out.println(response.get("Meta Data").getAsJsonObject().getAsString())
        Headers headers = response.headers();
        for (String name : headers.names()) {
            System.out.println(name + ": " + headers.get(name));
        }
        //System.out.println(response.body().string());
        
    }
}

