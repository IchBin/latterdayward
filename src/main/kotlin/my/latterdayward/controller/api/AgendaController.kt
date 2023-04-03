package my.latterdayward.controller.api

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import io.swagger.v3.oas.annotations.tags.Tag
import my.latterdayward.data.*
import my.latterdayward.repo.AgendaRepository
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.time.LocalDate

@RestController
@RequestMapping("/api/agenda")
@Tag(name = "Agenda", description = "Sacrament Agenda API")
class AgendaController(
    private val repo: AgendaRepository
) {

    @Operation(summary = "Get the Sacrament Agenda.", tags = ["Agenda"])
    @ApiResponses(value = [
        ApiResponse(responseCode = "200", description = "Returns Sacrament Agenda for today or next Sunday.",
            content = [Content(mediaType = "application/json", schema = Schema(implementation = Agenda::class))]),
        ApiResponse(responseCode = "404", description = "No Sacrament Agenda was found for current or next Sunday.",
            content = [Content(mediaType = "application/json", schema = Schema(implementation = ErrorResponse::class))])
    ])
    @GetMapping("/{path}")
    fun findSacramentAgenda(@PathVariable path: String) : ResponseEntity<Any> {
        repo.findByWardPathAndDate(path, LocalDate.now().nextSunday())?.let {
            return ResponseEntity.status(HttpStatus.OK).body(it)
        } ?: run {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ErrorResponse("No ward schedule was found."))
        }
    }
}