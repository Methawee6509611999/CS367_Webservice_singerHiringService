package dev.methawee.project;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.web.client.RestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

@RestController
@RequestMapping("/singers")
public class SingerController {

    private final SingerRepository singerRepository;
    private final ScheduleRepository scheduleRepository;
    private final RestTemplate restTemplate;

    public SingerController(SingerRepository singerRepository, ScheduleRepository scheduleRepository) {
        this.singerRepository = singerRepository;
        this.scheduleRepository = scheduleRepository;
        this.restTemplate = new RestTemplate();
    }

    // 1. Get all singer names
    @GetMapping
    public List<String> getAllSingerNames() {
        return singerRepository.findAll().stream()
                .map(Singer::getName)
                .collect(Collectors.toList());
    }

    // 2. Hire a singer on a specific date and location
    @PostMapping("/{id}/hireAt")
    @ResponseStatus(HttpStatus.CREATED)
    public Schedule hireSingerAt(@PathVariable Long id, @RequestBody HireRequest request) {
        Singer singer = singerRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Singer not found"));

        // Check for double booking
        boolean isBooked = scheduleRepository.findBySingerId(id).stream()
                .anyMatch(s -> s.getDate().equals(request.date));

        if (isBooked) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Singer is not available on this date");
        }

        // Prepare external POST request
        String reserveUrl = "http://192.168.0.11:8080/locations/" + request.location + "/reserve";
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        // Create JSON payload with date and singer name
        String jsonPayload = String.format(
                "{\"date\":\"%s\",\"singer\":\"%s\"}",
                request.date.toString(),
                singer.getName()
        );

        HttpEntity<String> httpEntity = new HttpEntity<>(jsonPayload, headers);

        // Send POST request
        try {
            ResponseEntity<String> response = restTemplate.postForEntity(reserveUrl, httpEntity, String.class);
            if (!response.getStatusCode().is2xxSuccessful()) {
                throw new ResponseStatusException(HttpStatus.BAD_GATEWAY, "Failed to reserve singer");
            }
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.BAD_GATEWAY, "Error communicating with reservation service", e);
        }

        // Save schedule locally
        Schedule schedule = new Schedule();
        schedule.setSinger(singer);
        schedule.setDate(request.date);
        schedule.setLocation(request.location);

        return scheduleRepository.save(schedule);
    }

    //3.get by genre
    @GetMapping(params = "genre")
    public List<String> getSingersByGenre(@RequestParam String genre) {
        if (genre == null || genre.trim().isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Genre parameter is required");
        }
        return singerRepository.findByGenre(genre).stream()
                .map(Singer::getName)
                .collect(Collectors.toList());
    }

    // just hire
    @PostMapping("/{id}/hire")
    @ResponseStatus(HttpStatus.CREATED)
    public Schedule hireSinger(@PathVariable Long id, @RequestBody HireRequest request) {
        Singer singer = singerRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Singer not found"));

        // Optional: check for double booking
        boolean isBooked = scheduleRepository.findBySingerId(id).stream()
                .anyMatch(s -> s.getDate().equals(request.date));

        if (isBooked) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Singer is not available on this date");
        }

        Schedule schedule = new Schedule();
        schedule.setSinger(singer);
        schedule.setDate(request.date);
        schedule.setLocation(request.location);



        return scheduleRepository.save(schedule);
    }

    // DTO for hire request
    public static class HireRequest {
        public LocalDate date;
        public String location;
    }
}