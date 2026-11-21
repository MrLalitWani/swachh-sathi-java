package com.swachhsathi.config;

import com.swachhsathi.entity.*;
import com.swachhsathi.repository.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class DataSeeder implements CommandLineRunner {

    private static final Logger log = LoggerFactory.getLogger(DataSeeder.class);

    private final UserRepository userRepository;
    private final SensitiveFacilityPOIRepository poiRepository;
    private final PasswordEncoder passwordEncoder;

    public DataSeeder(UserRepository userRepository,
                      SensitiveFacilityPOIRepository poiRepository,
                      PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.poiRepository = poiRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) {
        seedUsers();
        seedPOIs();
    }

    private void seedUsers() {
        if (userRepository.count() > 0) {
            log.info("Users already seeded. Skipping.");
            return;
        }

        // Admin user
        User admin = new User();
        admin.setName("Admin Swachh");
        admin.setEmail("admin@swachhsathi.gov.in");
        admin.setPassword(passwordEncoder.encode("admin123"));
        admin.setRole(Role.ROLE_ADMIN);
        admin.setPhone("9876543210");
        admin.setAddress("PMC Office, Pune");
        userRepository.save(admin);

        // Sample Worker
        User worker = new User();
        worker.setName("Raju Kamble");
        worker.setEmail("worker@swachhsathi.gov.in");
        worker.setPassword(passwordEncoder.encode("worker123"));
        worker.setRole(Role.ROLE_WORKER);
        worker.setPhone("9876501234");
        worker.setAddress("Ward 15, Pune");
        userRepository.save(worker);

        // Sample Citizen
        User citizen = new User();
        citizen.setName("Priya Sharma");
        citizen.setEmail("citizen@swachhsathi.gov.in");
        citizen.setPassword(passwordEncoder.encode("citizen123"));
        citizen.setRole(Role.ROLE_CITIZEN);
        citizen.setPhone("9823456780");
        citizen.setAddress("Kothrud, Pune");
        userRepository.save(citizen);

        log.info("Seeded 3 default users (admin, worker, citizen).");
        log.info("Admin login    -> admin@swachhsathi.gov.in / admin123");
        log.info("Worker login   -> worker@swachhsathi.gov.in / worker123");
        log.info("Citizen login  -> citizen@swachhsathi.gov.in / citizen123");
    }

    private void seedPOIs() {
        if (poiRepository.count() > 0) {
            log.info("POI data already seeded. Skipping.");
            return;
        }

        // Pune city sample sensitive facility POIs
        List<SensitiveFacilityPOI> pois = List.of(
                createPOI("Bal Gandharva School",        FacilityType.SCHOOL,      18.5236, 73.8478, 5.0, "Sadashiv Peth, Pune"),
                createPOI("Ruby Hall Clinic",             FacilityType.HOSPITAL,    18.5362, 73.8731, 4.5, "Sassoon Road, Pune"),
                createPOI("Empress Botanical Garden",    FacilityType.PARK,        18.5309, 73.8773, 3.5, "Nagar Road, Pune"),
                createPOI("KEM Hospital",                 FacilityType.HOSPITAL,    18.5195, 73.8553, 4.5, "Rasta Peth, Pune"),
                createPOI("Fergusson College",            FacilityType.SCHOOL,      18.5209, 73.8428, 5.0, "Shivajinagar, Pune"),
                createPOI("Pataleshwar Caves Park",       FacilityType.PARK,        18.5202, 73.8390, 3.5, "Jangli Maharaj Road, Pune"),
                createPOI("Residential Zone - Kothrud",  FacilityType.RESIDENTIAL, 18.5074, 73.8077, 3.0, "Kothrud, Pune"),
                createPOI("Commercial Zone - FC Road",   FacilityType.COMMERCIAL,  18.5236, 73.8398, 2.0, "Fergusson College Road, Pune"),
                createPOI("Sassoon General Hospital",    FacilityType.HOSPITAL,    18.5190, 73.8552, 4.5, "Pune Station Area"),
                createPOI("Residential Zone - Hadapsar", FacilityType.RESIDENTIAL, 18.5024, 73.9219, 3.0, "Hadapsar, Pune")
        );

        poiRepository.saveAll(pois);
        log.info("Seeded {} sensitive facility POIs (Pune region).", pois.size());
    }

    private SensitiveFacilityPOI createPOI(String name, FacilityType type, double lat, double lng, double weight, String address) {
        SensitiveFacilityPOI poi = new SensitiveFacilityPOI();
        poi.setName(name);
        poi.setFacilityType(type);
        poi.setLatitude(lat);
        poi.setLongitude(lng);
        poi.setWeight(weight);
        poi.setAddress(address);
        return poi;
    }
}
