package dev.methawee.project;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/singers")
public class SingerController {

    private final SingerRepository singerRepository;
    private final ScheduleRepository scheduleRepository;

    public SingerController(SingerRepository singerRepository, ScheduleRepository scheduleRepository) {
        this.singerRepository = singerRepository;
        this.scheduleRepository = scheduleRepository;
    }

    // 1. ✅ Get all singer names
    @GetMapping
    public List<String> getAllSingerNames() {
        return singerRepository.findAll().stream()
                .map(Singer::getName)
                .collect(Collectors.toList());
    }

    // 2. ✅ Hire a singer on a specific date and location
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

    // 3. ✅ Get dates when singer is unavailable (optional)
    @GetMapping("/{id}/unavailable")
    public List<LocalDate> getUnavailableDates(@PathVariable Long id) {
        Singer singer = singerRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Singer not found"));

            return scheduleRepository.findBySingerId(id).stream()
                .map(Schedule::getDate)
                .collect(Collectors.toList());
    }

    // DTO for hire request
    public static class HireRequest {
        public LocalDate date;
        public String location;
    }
}
