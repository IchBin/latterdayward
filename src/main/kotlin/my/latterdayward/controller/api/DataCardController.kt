package my.latterdayward.controller.api

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import io.swagger.v3.oas.annotations.tags.Tag
import my.latterdayward.data.DataCard
import my.latterdayward.data.ErrorResponse
import my.latterdayward.repo.DataCardRepository
import org.bson.types.ObjectId
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/datacard")
@Tag(name = "DataCard", description = "DataCard API")
class DataCardController(
    private val dataCardRepo: DataCardRepository
) {

    @Operation(summary = "Get all datacard details.")
    @ApiResponses(value = [
        ApiResponse(responseCode = "200", description = "List of datacard details was found.",
            content = [Content(mediaType = "application/json", schema = Schema(implementation = DataCard::class))]),
        ApiResponse(responseCode = "404", description = "No datacard details were found.",
            content = [Content(mediaType = "application/json", schema = Schema(implementation = ErrorResponse::class))])
    ])
    @GetMapping("/{path}")
    fun findAllDataCards(@PathVariable path: String) : ResponseEntity<Any> {
        dataCardRepo.findByWardPath(path)?.let {
            return ResponseEntity.status(HttpStatus.OK).body(it)
        } ?: run {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ErrorResponse("No datacard details were found."))
        }
    }

    @Operation(summary = "Get datacard details by type.")
    @ApiResponses(value = [
        ApiResponse(responseCode = "200", description = "List of datacard details was found.",
            content = [Content(mediaType = "application/json", schema = Schema(implementation = DataCard::class))]),
        ApiResponse(responseCode = "404", description = "No datacard details were found.",
            content = [Content(mediaType = "application/json", schema = Schema(implementation = ErrorResponse::class))])
    ])
    @GetMapping("/{path}/find/by-type/{type}")
    fun findAllDataCardsByType(@Schema(implementation = String::class, type = "String")
                               @PathVariable type: String,
                               @PathVariable path: String) : ResponseEntity<Any> {
        dataCardRepo.findByWardPathAndType(path, type)?.let {
            return ResponseEntity.status(HttpStatus.OK).body(it)
        } ?: run {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ErrorResponse("No datacard details were found."))
        }
    }

    @Operation(summary = "Get datacard by ID.")
    @ApiResponses(value = [
        ApiResponse(responseCode = "200", description = "Datacard details was found.",
            content = [Content(mediaType = "application/json", schema = Schema(implementation = DataCard::class))]),
        ApiResponse(responseCode = "404", description = "No datacard details were found.",
            content = [Content(mediaType = "application/json", schema = Schema(implementation = ErrorResponse::class))])
    ])
    @GetMapping("/{path}/find/{id}")
    fun findDataCardById(@Schema(implementation = String::class, type = "String")
                         @PathVariable id: ObjectId,
                         @PathVariable path: String) : ResponseEntity<Any> {
        dataCardRepo.findByIdAndWardPath(id, path)?.let {
            return ResponseEntity.status(HttpStatus.OK).body(it)
        } ?: run {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ErrorResponse("No datacard details were found."))
        }
    }
}