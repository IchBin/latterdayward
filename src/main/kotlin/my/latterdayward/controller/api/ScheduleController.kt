package my.latterdayward.controller.api

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import io.swagger.v3.oas.annotations.tags.Tag
import my.latterdayward.data.ErrorResponse
import my.latterdayward.data.Schedule
import my.latterdayward.data.nextSunday
import my.latterdayward.repo.AgendaRepository
import my.latterdayward.repo.ScheduleRepository
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.temporal.TemporalAdjusters.next

@RestController
@RequestMapping("/api/schedule")
@Tag(name = "Schedule", description = "Ward Schedule API")
class ScheduleController(
    private val scheduleRepo: ScheduleRepository,
    private val agendaRepository: AgendaRepository
) {

    @Operation(summary = "Get the ward schedule.", tags = ["Schedule"])
    @ApiResponses(value = [
        ApiResponse(responseCode = "200", description = "Ward schedule was found.",
            content = [Content(mediaType = "application/json", schema = Schema(implementation = Schedule::class))]),
        ApiResponse(responseCode = "404", description = "No ward schedule was found.",
            content = [Content(mediaType = "application/json", schema = Schema(implementation = ErrorResponse::class))])
    ])
    @GetMapping("/{path}")
    fun findWardSchedule(@PathVariable path: String) : ResponseEntity<Any> {
        scheduleRepo.findByWardPath(path)?.let {
            val schedules = scheduleOrOverride(it)
            schedules.forEach { s ->
                s.isAgenda = agendaRepository.findByWardPathAndDate(path, LocalDate.now().nextSunday()) != null
            }
            return ResponseEntity.status(HttpStatus.OK).body(schedules)
        } ?: run {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ErrorResponse("No ward schedule was found."))
        }
    }

    private fun scheduleOrOverride(schedules: List<Schedule>): List<Schedule> {
        val override = schedules.filter { s ->
            s.dateOverride == LocalDate.now().nextSunday()
        }
        return override.ifEmpty { schedules.filter { it.dateOverride == null } }
    }
}