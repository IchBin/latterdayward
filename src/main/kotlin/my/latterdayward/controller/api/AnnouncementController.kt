package my.latterdayward.controller.api

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import io.swagger.v3.oas.annotations.tags.Tag
import my.latterdayward.data.Announcement
import my.latterdayward.data.ErrorResponse
import my.latterdayward.repo.AnnouncementRepository
import org.bson.types.ObjectId
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping(value = ["/api/announcement"])
@Tag(name = "Announcement", description = "Ward Announcement API")
class AnnouncementController(
    private val announcementRepo: AnnouncementRepository
) {

    @Operation(summary = "Get a list of ALL Announcements for a ward.")
    @ApiResponses(value = [
        ApiResponse(responseCode = "200", description = "Announcements were found.",
            content = [Content(mediaType = "application/json", schema = Schema(implementation = Announcement::class))]),
        ApiResponse(responseCode = "404", description = "No Announcements were found.",
            content = [Content(mediaType = "application/json", schema = Schema(implementation = ErrorResponse::class))])
    ])
    @GetMapping("/{path}")
    fun fetchAnnouncements(@PathVariable path: String) : ResponseEntity<Any> {
        announcementRepo.findAllByWardPath(path)?.let {
            return ResponseEntity.status(HttpStatus.OK).body(it)
        } ?: run {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ErrorResponse("No Announcements found"))
        }
    }

    @Operation(summary = "Get an Announcement by ID.")
    @ApiResponses(value = [
        ApiResponse(responseCode = "200", description = "An Announcement was found.",
            content = [Content(mediaType = "application/json", schema = Schema(implementation = Announcement::class))]),
        ApiResponse(responseCode = "404", description = "No Announcement was found.",
            content = [Content(mediaType = "application/json", schema = Schema(implementation = ErrorResponse::class))])
    ])
    @GetMapping("/{path}/find/{id}")
    fun findAnnouncement(@Schema(implementation = String::class, type = "String") @PathVariable id: ObjectId,
                         @PathVariable path: String) : ResponseEntity<Any> {
        announcementRepo.findByIdAndWardPath(id, path)?.let {
            return ResponseEntity.status(HttpStatus.OK).body(it)
        } ?: run {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ErrorResponse("No Announcement was found."))
        }
    }

    @Operation(summary = "Get Announcements by type.")
    @ApiResponses(value = [
        ApiResponse(responseCode = "200", description = "Announcements were found.",
            content = [Content(mediaType = "application/json", schema = Schema(implementation = Announcement::class))]),
        ApiResponse(responseCode = "404", description = "No Announcements were found.",
            content = [Content(mediaType = "application/json", schema = Schema(implementation = ErrorResponse::class))])
    ])
    @GetMapping("/{path}/find/by-type/{type}")
    fun fetchAnnouncementsByType(@PathVariable path: String, @PathVariable type: String) : ResponseEntity<Any> {
        announcementRepo.findByWardPathAndType(path, type)?.let {
            return ResponseEntity.status(HttpStatus.OK).body(it)
        } ?: run {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ErrorResponse("No Announcements were found."))
        }
    }
}