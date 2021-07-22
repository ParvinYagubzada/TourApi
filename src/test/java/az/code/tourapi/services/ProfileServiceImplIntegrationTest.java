package az.code.tourapi.services;

import az.code.tourapi.enums.UserRequestStatus;
import az.code.tourapi.models.dtos.OfferDTO;
import az.code.tourapi.models.entities.Request;
import az.code.tourapi.models.entities.User;
import az.code.tourapi.models.rabbit.AcceptedOffer;
import az.code.tourapi.models.rabbit.RawRequest;
import az.code.tourapi.repositories.OfferRepository;
import az.code.tourapi.repositories.RequestRepository;
import az.code.tourapi.repositories.UserRepository;
import az.code.tourapi.repositories.UserRequestRepository;
import az.code.tourapi.utils.Mappers;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@TestMethodOrder(OrderAnnotation.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class ProfileServiceImplIntegrationTest {

    public static final String AGENCY_NAME = "DataFlex";
    public static final String SORT_BY = "status";

    @Autowired
    private ProfileService service;
    @Autowired
    private Mappers mappers;
    @Autowired
    private UserRequestRepository userRequestRepo;
    @Autowired
    private UserRepository userRepo;
    @Autowired
    private RequestRepository requestRepo;
    @Autowired
    private OfferRepository offerRepo;
    @Autowired
    private QueueListenerService listenerService;

    private static OfferDTO createOffer() {
        return OfferDTO.builder()
                .description("salary").travelDates("time")
                .price(386).notes("sock")
                .build();
    }

    private static AcceptedOffer createAccepted(String uuid) {
        return AcceptedOffer.builder()
                .agencyName(ProfileServiceImplIntegrationTest.AGENCY_NAME).uuid(uuid)
                .username("test").phoneNumber(null)
                .firstName("test1").lastName("test2")
                .userId("12345678")
                .build();
    }

    @Test
    @Order(1)
    @DisplayName("ProfileService - getRequests - NEW_REQUEST")
    void getRequests_NEW_REQUEST() {
        assertThat(service.getRequests(AGENCY_NAME, null, UserRequestStatus.NEW_REQUEST,
                0, 100, SORT_BY)).hasSize(8)
                .filteredOn(userRequest -> userRequest.getStatus() == UserRequestStatus.NEW_REQUEST);
    }

    @Test
    @Order(2)
    @DisplayName("ProfileService - getRequests - OFFER_MADE")
    void getRequests_OFFER_MADE() {
        assertThat(service.getRequests(AGENCY_NAME, null, UserRequestStatus.OFFER_MADE,
                0, 100, SORT_BY)).hasSize(10)
                .filteredOn(userRequest -> userRequest.getStatus() == UserRequestStatus.OFFER_MADE &&
                        userRequest.getOffer() != null);
    }

    @Test
    @Order(3)
    @DisplayName("ProfileService - getRequests - ACCEPTED")
    void getRequests_ACCEPTED() {
        assertThat(service.getRequests(AGENCY_NAME, null, UserRequestStatus.ACCEPTED,
                0, 100, SORT_BY)).hasSize(10)
                .filteredOn(userRequest -> userRequest.getStatus() == UserRequestStatus.ACCEPTED
                        && userRequest.getCustomer() != null && userRequest.getOffer() != null);
    }

    @Test
    @Order(4)
    @DisplayName("ProfileService - getRequests - EXPIRED")
    void getRequests_EXPIRED() {
        assertThat(service.getRequests(AGENCY_NAME, null, UserRequestStatus.EXPIRED,
                0, 100, SORT_BY)).hasSize(2)
                .filteredOn(userRequest -> userRequest.getStatus() == UserRequestStatus.EXPIRED);
    }

    @Test
    @Order(5)
    @DisplayName("ProfileService - getRequests - isArchived")
    void getRequests_isArchived() {
        assertThat(service.getRequests(AGENCY_NAME, true, null,
                0, 100, SORT_BY)).hasSize(2)
                .filteredOn(userRequest -> userRequest.getStatus() == UserRequestStatus.EXPIRED &&
                        userRequest.isArchived());
    }

    @BeforeAll
    public void init() {
        userRepo.saveAndFlush(User.builder().username("shayne.pfannerstill").agencyName(AGENCY_NAME).voen("5344501174").email("serina.tremblay@yahoo.com").name("Cleveland Padberg").build());
        userRepo.saveAndFlush(User.builder().username("herb.mraz").agencyName("McDonald's").voen("5850888582").email("loris.cronin@hotmail.com").name("Nicolas Hammes").build());
        List<Request> requests = new ArrayList<>();
        requests.add(mappers.rawToRequest(new RawRequest("6a2bb2f2-5162-4242-897f-30eb145229d4", "RU", "Black Gate", "Gladden Fields", "Cirith Ungol", "28.04.1981", "24.08.1959", "206", "457"), LocalTime.parse("20:48:24.120550859")));
        requests.add(mappers.rawToRequest(new RawRequest("7444627a-f71c-4f23-ae80-d5f00c7eb958", "EN", "Warning beacons of Gondor", "Núath", "Tol Galen", "21.07.2002", "04.03.1993", "571", "730"), LocalTime.parse("18:32:18.020888525")));
        requests.add(mappers.rawToRequest(new RawRequest("cd787f5f-5269-47cf-9828-2daa49ad6ba6", "AZ", "Black Gate", "Henneth Annûn", "Barad-dûr", "19.06.1995", "08.01.2000", "132", "672"), LocalTime.parse("23:48:42.300904493")));
        requests.add(mappers.rawToRequest(new RawRequest("5257afcc-3111-467f-a22c-26dc197f65e3", "RU", "Isenmouthe", "Tol Brandir", "Máhanaxar", "05.01.1991", "17.05.1979", "280", "220"), LocalTime.parse("23:39:54.873738919")));
        requests.add(mappers.rawToRequest(new RawRequest("60a54f4b-ac4f-4925-8a25-69b5b6f43cf1", "AZ", "Greenway", "Carchost", "Taur-im-Duinath", "16.11.1957", "22.03.1986", "883", "675"), LocalTime.parse("01:13:07.528748150")));
        requests.add(mappers.rawToRequest(new RawRequest("a87ecae0-93ad-4dd2-bfe8-dcdca6c6482f", "RU", "Tol Galen", "Isengard", "Ilmen", "28.11.1976", "29.05.2002", "654", "564"), LocalTime.parse("00:23:56.852164931")));
        requests.add(mappers.rawToRequest(new RawRequest("80fa6898-e146-4d93-a5fa-3af891f6ec90", "RU", "Helm's Deep", "Andustar", "Warning beacons of Gondor", "30.08.1965", "22.03.1989", "942", "642"), LocalTime.parse("10:13:56.114806737")));
        requests.add(mappers.rawToRequest(new RawRequest("45d2d85c-24cd-4079-bb1c-f15fc65c69ae", "EN", "Tol Brandir", "Minas Tirith", "Old Ford", "24.06.1984", "29.09.1973", "770", "772"), LocalTime.parse("20:25:23.398537617")));
        requests.add(mappers.rawToRequest(new RawRequest("4996ea87-d412-440f-bea2-a1b065055b6c", "EN", "Bag End", "Eä", "Henneth Annûn", "23.10.1993", "27.01.1975", "766", "624"), LocalTime.parse("04:33:14.760119063")));
        requests.add(mappers.rawToRequest(new RawRequest("d917337a-f526-401d-b46c-c548597467c0", "EN", "Losgar", "Utumno", "East Beleriand", "02.02.1994", "15.02.1994", "343", "016"), LocalTime.parse("20:22:31.224482566")));
        requests.add(mappers.rawToRequest(new RawRequest("f8e9b3e0-b444-4136-9279-89da1a5d12b8", "RU", "Rivendell", "Utumno", "Dol Guldur", "26.01.1975", "30.06.2000", "870", "132"), LocalTime.parse("15:11:28.514367257")));
        requests.add(mappers.rawToRequest(new RawRequest("e9e142bb-5412-411a-abd3-200beef1abb3", "RU", "Dome of Stars", "Vaiya", "Tol-in-Gaurhoth", "12.11.1990", "16.05.1981", "650", "166"), LocalTime.parse("06:08:41.469425971")));
        requests.add(mappers.rawToRequest(new RawRequest("2b6fecc2-aa3c-4ac4-adeb-e291afd7c076", "AZ", "Cirith Ungol", "Bridge of Khazad-dûm", "Haven of the Eldar", "21.10.1961", "25.02.1994", "582", "536"), LocalTime.parse("16:20:28.890975872")));
        requests.add(mappers.rawToRequest(new RawRequest("214641aa-0417-4541-9189-39c4dd53bfb3", "AZ", "Land of the Sun", "Máhanaxar", "Old Forest Road", "16.04.1983", "09.11.1966", "530", "507"), LocalTime.parse("21:56:21.981149819")));
        requests.add(mappers.rawToRequest(new RawRequest("a803fba6-bc35-4684-b73f-931bc62e3610", "EN", "Rath Dínen", "Old Forest", "Rivendell", "15.10.1962", "29.04.1970", "217", "437"), LocalTime.parse("03:48:48.866761932")));
        requests.add(mappers.rawToRequest(new RawRequest("22a2fb70-242b-455c-a4e1-8e83fa24b04f", "EN", "Hyarnustar", "Haven of the Eldar", "Enchanted Isles", "04.12.1963", "09.10.1962", "710", "860"), LocalTime.parse("08:37:52.531659364")));
        requests.add(mappers.rawToRequest(new RawRequest("75dbf688-8d6f-4839-9d60-055b9b7df0a1", "AZ", "Máhanaxar", "Taur-im-Duinath", "Fens of Serech", "12.08.2002", "10.12.1961", "774", "257"), LocalTime.parse("17:07:02.022357820")));
        requests.add(mappers.rawToRequest(new RawRequest("43c63974-ecba-46bf-b1d6-189f9c01a779", "AZ", "Tol-in-Gaurhoth", "Old Ford", "Vaiya", "02.04.1958", "11.09.1983", "002", "049"), LocalTime.parse("00:04:20.683964334")));
        requests.add(mappers.rawToRequest(new RawRequest("b1c30e4d-20d4-409c-9d4f-e8376dc430d6", "AZ", "Coldfells", "Luthany", "Warning beacons of Gondor", "29.03.1975", "18.07.1991", "780", "883"), LocalTime.parse("22:06:11.664230481")));
        requests.add(mappers.rawToRequest(new RawRequest("9c2c30d7-da6d-4588-b60a-db11763708a9", "EN", "Rivendell", "Eastfarthing", "Tol Morwen", "22.01.1994", "03.06.1985", "536", "812"), LocalTime.parse("07:18:51.782641635")));
        requests.add(mappers.rawToRequest(new RawRequest("eb19a117-930e-41a1-92e1-225e7d9c8753", "RU", "Cirith Ungol", "Fens of Serech", "Black Gate", "27.05.2003", "03.11.1961", "979", "843"), LocalTime.parse("12:18:37.356542393")));
        requests.add(mappers.rawToRequest(new RawRequest("37179d19-800c-4757-a32d-8749a015d50f", "EN", "Doriath", "Inn of the Prancing Pony", "Eastfarthing", "04.02.1979", "30.01.1994", "323", "689"), LocalTime.parse("02:32:46.726821306")));
        requests.add(mappers.rawToRequest(new RawRequest("29353340-3152-4631-bd6a-b2b903428d7d", "RU", "Dark Land", "Taur-im-Duinath", "Tol Brandir", "23.01.2002", "14.06.1962", "025", "907"), LocalTime.parse("06:22:51.241456399")));
        requests.add(mappers.rawToRequest(new RawRequest("5b3759a5-69bc-476c-8c7c-7ca6f7f0f766", "AZ", "Rivendell", "Fens of Serech", "Vaiya", "20.05.1968", "30.09.1963", "787", "718"), LocalTime.parse("09:33:07.935656659")));
        requests.add(mappers.rawToRequest(new RawRequest("e3aa2e1f-2f2a-4e93-9e3b-48b55f382b7a", "RU", "Núath", "Dark Land", "Nargothrond", "12.05.1972", "12.12.1974", "156", "593"), LocalTime.parse("09:27:10.908094945")));
        requests.add(mappers.rawToRequest(new RawRequest("08163204-ad6b-4c28-85bf-9f203a2d3a87", "RU", "Hobbit-hole", "Sarn Ford", "Isle of Balar", "14.07.2001", "25.10.1969", "451", "240"), LocalTime.parse("18:51:55.352026929")));
        requests.add(mappers.rawToRequest(new RawRequest("bd33a3be-6110-4b7b-9efa-eb9b52cb6c4e", "RU", "Haudh-en-Nirnaeth", "Utumno", "Utumno", "16.01.1983", "09.11.1957", "239", "917"), LocalTime.parse("09:38:08.396513649")));
        requests.add(mappers.rawToRequest(new RawRequest("d4941ead-5e1e-4cca-b70f-1a6ff30b0aa8", "AZ", "Fens of Serech", "Old Ford", "Tol Galen", "19.06.2000", "10.01.1989", "264", "252"), LocalTime.parse("23:13:22.001307694")));
        requests.add(mappers.rawToRequest(new RawRequest("63f2f394-1ba3-492b-ba6a-9cfe205958bf", "RU", "Coldfells", "Black Gate", "Helm's Deep", "03.09.2002", "05.03.1978", "618", "671"), LocalTime.parse("03:23:36.116986435")));
        requests.add(mappers.rawToRequest(new RawRequest("f63e789b-109d-45e2-8ea9-d22b86814143", "RU", "Rivendell", "Nargothrond", "Tol Morwen", "30.12.1963", "06.11.1978", "721", "610"), LocalTime.parse("00:23:01.379504034")));
        requestRepo.saveAllAndFlush(requests);
        IntStream.range(0, 20).forEach(value -> service.makeOffer(AGENCY_NAME, requests.get(value).getUuid(), createOffer()));
        IntStream.range(10, 20).forEach(value -> listenerService.listenAcceptances(createAccepted(requests.get(value).getUuid())));
        IntStream.of(0, 1, 10, 11, 20, 21).forEach(value -> listenerService.listenDeactivations(requests.get(value).getUuid()));
    }

    @AfterAll
    public void clean() {
        offerRepo.deleteAll();
        userRequestRepo.deleteAll();
        requestRepo.deleteAll();
        userRepo.deleteAll();
    }
}