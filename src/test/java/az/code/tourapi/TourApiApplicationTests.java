package az.code.tourapi;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;

import static az.code.tourapi.utils.Mappers.timeFormatter;
import static java.nio.charset.StandardCharsets.UTF_8;
import static org.springframework.http.MediaType.APPLICATION_JSON;

@SpringBootTest
@SuppressWarnings("SpellCheckingInspection")
public class TourApiApplicationTests {

    public static final String UUID = "a46d6230-c521-46ba-9957-1bb0347370e7";
    public static final String AGENCY_NAME = "Global Travel";

    public static final String AUTHORIZATION = "Authorization";
    public static final String TOKEN = ".eyJleHAiOjE2MjY5OTg2MDQsImlhdCI6MTYyNjk2MjYwNCwianRpIjoiZmJlOTdmZDYtNTNlMC00MGZkLWFkMzItZDExOTIwNjI3NjFmIiwiaXNzIjoiaHR0cDovL2xvY2FsaG9zdDo4MTgwL2F1dGgvcmVhbG1zL1RvdXIiLCJhdWQiOiJhY2NvdW50Iiwic3ViIjoiMmFkYTk2ZDAtNmExMC00ZThjLTg2YmQtMzQzOGE3Zjk2OWVmIiwidHlwIjoiQmVhcmVyIiwiYXpwIjoidG91ci1hcHAiLCJzZXNzaW9uX3N0YXRlIjoiN2I1NGFhMjktMWE3Yy00NDFkLWIzMDItMTNiZDQyZmIzZTFjIiwiYWNyIjoiMSIsInJlYWxtX2FjY2VzcyI6eyJyb2xlcyI6WyJvZmZsaW5lX2FjY2VzcyIsInVtYV9hdXRob3JpemF0aW9uIiwiYXBwLXVzZXIiLCJkZWZhdWx0LXJvbGVzLXRvdXIiXX0sInJlc291cmNlX2FjY2VzcyI6eyJ0b3VyLWFwcCI6eyJyb2xlcyI6WyJ1c2VyIl19LCJhY2NvdW50Ijp7InJvbGVzIjpbIm1hbmFnZS1hY2NvdW50IiwibWFuYWdlLWFjY291bnQtbGlua3MiLCJ2aWV3LXByb2ZpbGUiXX19LCJzY29wZSI6ImVtYWlsIHByb2ZpbGUiLCJjcmVhdGlvbl90aW1lIjoxNjI2OTYxOTQwOTI2LCJlbWFpbF92ZXJpZmllZCI6dHJ1ZSwiYWdlbmN5X25hbWUiOiJHbG9iYWwgVHJhdmVsIiwidm9lbiI6IjEyMzQ1Njc4OTAiLCJuYW1lIjoiUGVydmluIFlhcXViemFkZSIsInByZWZlcnJlZF91c2VybmFtZSI6InBlcnZpbnVzZXIiLCJnaXZlbl9uYW1lIjoiUGVydmluIiwiZmFtaWx5X25hbWUiOiJZYXF1YnphZGUiLCJlbWFpbCI6InBhcnZpbnl5QGNvZGUuZWR1LmF6In0.";
    public static final MediaType APPLICATION_JSON_UTF8 = new MediaType(APPLICATION_JSON.getType(), APPLICATION_JSON.getSubtype(), UTF_8);

    public static final ZoneId SYSTEM_DEFAULT = ZoneId.systemDefault();
    public static final LocalDateTime DATE_TIME = LocalDateTime.of(LocalDate.now(), LocalTime.parse("15:00:00", timeFormatter));
    public static final LocalDate DATE = DATE_TIME.toLocalDate();

    @Test
    @DisplayName("Application stats")
    void contextLoads() {}
}
