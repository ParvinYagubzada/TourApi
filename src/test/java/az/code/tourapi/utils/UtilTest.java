package az.code.tourapi.utils;

import az.code.tourapi.exceptions.InvalidTokenFormatException;
import az.code.tourapi.models.UserData;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.time.ZoneOffset;

import static org.junit.jupiter.api.Assertions.*;

@SuppressWarnings("SpellCheckingInspection")
class UtilTest {

    @Test
    void convertTokenValid() {
        String token = "eyJhbGciOiJSUzI1NiIsInR5cCIgOiAiSldUIiwia2lkIiA6ICJXRmxGeTVuT2dNRnM5QWVadU5CTFM5czQ0d3h4Y19CZzVOVnoycXRaX1FrIn0.eyJleHAiOjE2MjY4NDc3NDksImlhdCI6MTYyNjgxMTc0OSwianRpIjoiY2M1YjU4YTQtOTlmNC00MjEwLTk3MDMtMzUxNTU1YjdlZWQ0IiwiaXNzIjoiaHR0cDovL2xvY2FsaG9zdDo4MTgwL2F1dGgvcmVhbG1zL1RvdXIiLCJhdWQiOiJhY2NvdW50Iiwic3ViIjoiYWE2NTFiYWMtNDA3ZC00NmRjLTg1OWMtZTAwMDk0Njg1YmRkIiwidHlwIjoiQmVhcmVyIiwiYXpwIjoidG91ci1hcHAiLCJzZXNzaW9uX3N0YXRlIjoiNzQ1MzNmMWEtNjRhNS00MzlhLThhM2ItNjNkMDE2MDI3ZjVlIiwiYWNyIjoiMSIsInJlYWxtX2FjY2VzcyI6eyJyb2xlcyI6WyJvZmZsaW5lX2FjY2VzcyIsInVtYV9hdXRob3JpemF0aW9uIiwiYXBwLXVzZXIiLCJkZWZhdWx0LXJvbGVzLXRvdXIiXX0sInJlc291cmNlX2FjY2VzcyI6eyJ0b3VyLWFwcCI6eyJyb2xlcyI6WyJ1c2VyIl19LCJhY2NvdW50Ijp7InJvbGVzIjpbIm1hbmFnZS1hY2NvdW50IiwibWFuYWdlLWFjY291bnQtbGlua3MiLCJ2aWV3LXByb2ZpbGUiXX19LCJzY29wZSI6ImVtYWlsIHByb2ZpbGUiLCJjcmVhdGlvbl90aW1lIjoxNjI2ODA3Mzc1MDM3LCJlbWFpbF92ZXJpZmllZCI6dHJ1ZSwiYWdlbmN5X25hbWUiOiJHbG9iYWwgVHJhdmVsIiwidm9lbiI6IjEyMzQ1Njc4OTAiLCJuYW1lIjoiUGVydmluIFlhcXViemFkZSIsInByZWZlcnJlZF91c2VybmFtZSI6InBlcnZpbnVzZXIiLCJnaXZlbl9uYW1lIjoiUGVydmluIiwiZmFtaWx5X25hbWUiOiJZYXF1YnphZGUiLCJlbWFpbCI6InBhcnZpbnl5QGNvZGUuZWR1LmF6In0.I5st39QtYNH_H4-l2GMHeRy6LPy3KsHtGssgdwgGxDpn3shyr4b0t_oPu8hWrpkAHRdAriGKIHiBUe1gi6o8RAM5Qh3gEsahGTFBWbGyX8UbtqUyLisK58fHwevChLU7esBrM24w63zb3ZyhDMFL50qztkDY5qjOO6lhp5Agv_d5vf2-G7KQxdHR310T-zOBzBIf-PLYq6QhXSO2C_92F6zcsDJquityt6hSVXZR-2EcucgN26vdrahjSHdaDz8nUoPdfH9QcRCAH8LGh2_Ap_YfZLbCaAc7ogM55kr3jFuonnYur65qWny0Tnc4GMVzK8WG4F2t6bFzAzzWCu9qLg";
        UserData expected = new UserData();
        expected.setAgencyName("Global Travel");
        expected.setUsername("pervinuser");
        expected.setFullName("Pervin Yaqubzade");
        expected.setEmail("parvinyy@code.edu.az");
        expected.setRegistrationTime(LocalDateTime.ofEpochSecond(1626807375037L, 0, ZoneOffset.ofHours(4)));
        assertEquals(expected, Util.convertToken(token));
    }

    @Test
    void convertTokenInvalidFormat() {
        String token = "test";
        assertThrows(InvalidTokenFormatException.class, () -> {
            Util.convertToken(token);
        });
    }

    @Test
    void convertTokenInvalidValue() {
        String token = "eyJhbGciOiJSUzI1NiIsInR5cCIgOiAiSldUIiwia2lkIiA6ICJXRmxGeTVuT2dNRnM5QWVadU5CTFM5czQ0d3h4Y19CZzVOVnoycXRaX1FrIn0.eyJleHAiOjE2MjY4NDg2OTAsImlhdCI6MTYyNjgxMjY5MCwianRpIjoiYmRiOGQzNzktNjc4YS00NmUxLThjMTMtY2E0YTcwNWI3MTZmIiwiaXNzIjoiaHR0cDovL2xvY2FsaG9zdDo4MTgwL2F1dGgvcmVhbG1zL1RvdXIiLCJhdWQiOiJhY2NvdW50Iiwic3ViIjoiZjliNzczZGEtZTI5Yy00ODNiLTlhM2ItNTcyZjkxM2RhN2UyIiwidHlwIjoiQmVhcmVyIiwiYXpwIjoidG91ci1hcHAiLCJzZXNzaW9uX3N0YXRlIjoiN2Q4YmQ4ZDktMGNiNy00ZWE3LWFhYmEtMDcxNWE5NzU2MzQxIiwiYWNyIjoiMSIsInJlYWxtX2FjY2VzcyI6eyJyb2xlcyI6WyJvZmZsaW5lX2FjY2VzcyIsInVtYV9hdXRob3JpemF0aW9uIiwiZGVmYXVsdC1yb2xlcy10b3VyIl19LCJyZXNvdXJjZV9hY2Nlc3MiOnsiYWNjb3VudCI6eyJyb2xlcyI6WyJtYW5hZ2UtYWNjb3VudCIsIm1hbmFnZS1hY2NvdW50LWxpbmtzIiwidmlldy1wcm9maWxlIl19fSwic2NvcGUiOiJlbWFpbCBwcm9maWxlIiwiY3JlYXRpb25fdGltZSI6MTYyNjQ0NjQyNDkzNiwiZW1haWxfdmVyaWZpZWQiOnRydWUsIm5hbWUiOiJBZG1pbiBBZG1pbiIsInByZWZlcnJlZF91c2VybmFtZSI6ImFkbWludXNlciIsImdpdmVuX25hbWUiOiJBZG1pbiIsImZhbWlseV9uYW1lIjoiQWRtaW4iLCJlbWFpbCI6ImFkbWluQGFkbWluLmNvbSJ9.jGQlOtzC9o1I2gwKFaewSy3IYfOWpYSG5nAyRMi5FDFoqzdJ1lx9O7p4iLv3b1UWqumGcy9ALpGL-w3jgqpENCiPFTNWMyVbxt06HIBQx6D8yUok4EjzzitIuALW7nxR9YnJ8tZ01AfSXpZhm4kgPfZr1oQdIdU0Y_fu_UMdJ0fAb3awtjJIWoS35_sCPOtOzJyDfc9ZDfCzVJA-DNbsSlAFfqg1j3YG0MtjlSXyEZZNXNmCf7e5hfXRxv_CZMbkWGVJMehi7oFu9kbABXyKa6URyAWfpr8qtgsArr2H6AhN3AOsC4lm9g5xLIvwog1AdVhDKHLxEZFq4hSnlZ6ZuQ.eyJleHAiOjE2MjY4NDg1MzgsImlhdCI6MTYyNjgxMjUzOCwianRpIjoiMDZlNDNmYzItODEzNC00NGNkLThlN2YtM2M3Y2UwNzNjMjhhIiwiaXNzIjoiaHR0cDovL2xvY2FsaG9zdDo4MTgwL2F1dGgvcmVhbG1zL1RvdXIiLCJhdWQiOiJhY2NvdW50Iiwic3ViIjoiZjliNzczZGEtZTI5Yy00ODNiLTlhM2ItNTcyZjkxM2RhN2UyIiwidHlwIjoiQmVhcmVyIiwiYXpwIjoidG91ci1hcHAiLCJzZXNzaW9uX3N0YXRlIjoiYWVmZTk0YTItNzRhZi00Y2U3LWE3ODAtZWZkZjZkMDY5NzlkIiwiYWNyIjoiMSIsInJlYWxtX2FjY2VzcyI6eyJyb2xlcyI6WyJvZmZsaW5lX2FjY2VzcyIsInVtYV9hdXRob3JpemF0aW9uIiwiZGVmYXVsdC1yb2xlcy10b3VyIl19LCJyZXNvdXJjZV9hY2Nlc3MiOnsiYWNjb3VudCI6eyJyb2xlcyI6WyJtYW5hZ2UtYWNjb3VudCIsIm1hbmFnZS1hY2NvdW50LWxpbmtzIiwidmlldy1wcm9maWxlIl19fSwic2NvcGUiOiJlbWFpbCBwcm9maWxlIiwiY3JlYXRpb25fdGltZSI6MTYyNjQ0NjQyNDkzNiwiZW1haWxfdmVyaWZpZWQiOnRydWUsImFnZW5jeV9uYW1lIjoiUGFzaGEgQmFuayIsIm5hbWUiOiJBZG1pbiBBZG1pbiIsInByZWZlcnJlZF91c2VybmFtZSI6ImFkbWludXNlciIsImdpdmVuX25hbWUiOiJBZG1pbiIsImZhbWlseV9uYW1lIjoiQWRtaW4iLCJlbWFpbCI6ImFkbWluQGFkbWluLmNvbSJ9.BUTzmMdcJ-v3VLGHclO9t3Iulzdk-2o-vSWHUvtLBr9g5TdPnQ1ZVZLr39Havjp-x4SNJ8Zq3gdhFw2jnh0jWQ5wkhuQ9tyQufBuI8TfxpaGwWziA_dPnLO9QF9V_Gso-GKvS_07nM4kgBnxDA7ZxsRbvXwhrRGeUNl66uxxRw_bpyPziv37zNm-uA1wJNvOUZYkV8ZeSqGjQerd0-KNo0S1fVKYi3M5WWCzqT3xDT_ylMOqGMUNPvJWJpvuYBEX8qkMfYcUze4T7ylvS3SqtZuR6XwzucmCkZd4Sqkdo8xOM-dnXdkh9Bv2slSEUZIdcYCESj8icKU_uBdbcYAHGQ";
        assertThrows(InvalidTokenFormatException.class, () -> {
            Util.convertToken(token);
        });
    }
}