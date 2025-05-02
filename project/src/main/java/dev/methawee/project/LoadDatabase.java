package dev.methawee.project;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.LocalDate;

@Configuration
public class LoadDatabase {

    private static final Logger log = LoggerFactory.getLogger(LoadDatabase.class);

    @Bean
    CommandLineRunner initDatabase(SingerRepository singerRepository, ScheduleRepository scheduleRepository) {
        return args -> {
            // Create and save singers
            Singer adele = new Singer("Adele");
            adele.setGenre("pop");
            Singer rihanna = new Singer("Rihanna");
            rihanna.setGenre("rap");
            Singer bruno = new Singer("Bruno Mars");
            bruno.setGenre("pop");
            Singer weeknd = new Singer("The Weeknd");
            weeknd.setGenre("pop");
            Singer chester = new Singer("Chester Bennington");
            chester.setGenre("rock");

            singerRepository.save(adele);
            singerRepository.save(rihanna);
            singerRepository.save(bruno);
            singerRepository.save(weeknd);

            log.info("Preloaded singer: " + adele);
            log.info("Preloaded singer: " + rihanna);

            // Create and save schedules using the saved singers
            Schedule s1 = new Schedule();
            s1.setSinger(adele);
            s1.setDate(LocalDate.of(2025, 6, 10));
            s1.setLocation("Bangkok");

            Schedule s2 = new Schedule();
            s2.setSinger(rihanna);
            s2.setDate(LocalDate.of(2025, 6, 12));
            s2.setLocation("Chiang Mai");

            scheduleRepository.save(s1);
            scheduleRepository.save(s2);

            log.info("Preloaded schedule: " + s1);
            log.info("Preloaded schedule: " + s2);
        };
    }
}
