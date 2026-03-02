package com.example.beerapi.service;

import com.example.beerapi.model.Beer;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import javax.net.ssl.*;
import java.security.cert.X509Certificate;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class BeerService {

    private static final String API_URL = "https://api.sampleapis.com/beers/ale";
    private final RestTemplate restTemplate;
    private final ObjectMapper mapper = new ObjectMapper();

    public BeerService() {
        this.restTemplate = createUnsafeRestTemplate(); // ignore SSL
    }

    public List<Beer> getAllBeers() {
        try {
            // Fetch as raw list of maps
            String json = restTemplate.getForObject(API_URL, String.class);
            List<Map<String, Object>> rawList = mapper.readValue(json, new TypeReference<>() {});

            // Filter out bad or malformed entries
            return rawList.stream()
                    .filter(item -> item.get("price") != null && !item.get("price").toString().contains("{{"))
                    //.filter(item -> item.get("name") != null && !"Hi".equalsIgnoreCase(item.get("name").toString()))
                    .filter(item -> item.get("rating") instanceof Map) // ensure rating is an object, not string
                    .map(item -> mapper.convertValue(item, Beer.class))
                    .collect(Collectors.toList());

        } catch (Exception e) {
            throw new RuntimeException("Failed to fetch or parse beer data", e);
        }
    }

    public List<Beer> getBeersWithRatingAbove(double threshold) {
        return getAllBeers().stream()
                .filter(beer -> beer.getRating() != null && beer.getRating().getAverage() > threshold)
                .collect(Collectors.toList());
    }

    // ---- Helper: create RestTemplate that ignores SSL validation ----
    private RestTemplate createUnsafeRestTemplate() {
        try {
            TrustManager[] trustAllCerts = new TrustManager[]{
                    new X509TrustManager() {
                        public X509Certificate[] getAcceptedIssuers() { return new X509Certificate[0]; }
                        public void checkClientTrusted(X509Certificate[] certs, String authType) { }
                        public void checkServerTrusted(X509Certificate[] certs, String authType) { }
                    }
            };
            SSLContext sslContext = SSLContext.getInstance("TLS");
            sslContext.init(null, trustAllCerts, new java.security.SecureRandom());
            HttpsURLConnection.setDefaultSSLSocketFactory(sslContext.getSocketFactory());
            HttpsURLConnection.setDefaultHostnameVerifier((hostname, session) -> true);
            return new RestTemplate();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
