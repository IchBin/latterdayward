package my.latterdayward.controller.user

import my.latterdayward.data.*
import my.latterdayward.repo.DataCardRepository
import my.latterdayward.service.FileService
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.*
import org.springframework.web.servlet.mvc.support.RedirectAttributes
import javax.servlet.http.HttpSession

@Controller
@RequestMapping("/user/datacard")
class UserDataCardController(
    private val repo: DataCardRepository,
    private val fileService: FileService
) {

    @GetMapping("")
    fun home(model: MutableMap<String, Any?>, session: HttpSession): String {
        val user = session.getAttribute("user") as User
        model["datacards"] = repo.findByWardPath(user.ward?.path!!)
            ?.sortedBy { it.order }
            ?.groupBy { it.type }
            ?.entries
            ?.sortedBy { m -> m.key }
        model["colors"] = Colors()
        return "user/datacard"
    }

    @GetMapping("/add")
    fun add(model: MutableMap<String, Any?>, session: HttpSession): String {
        val user = session.getAttribute("user") as User
        model["datacard"] = DataCard().toForm()
        model["files"] = fileService.fileList(user)
        return "user/datacard_add"
    }

    @PostMapping("/edit")
    fun edit(@RequestParam id: String, model: MutableMap<String, Any?>, session: HttpSession): String {
        val user = session.getAttribute("user") as User
        model["files"] = fileService.fileList(user)
        model["datacard"] = repo.findByIdOrNull(id)
        return "user/datacard_add"
    }

    @PostMapping("/save")
    fun save(dataCard: DataCard, r: RedirectAttributes, m: Messages): String {
        val cards = repo.findByWardPathAndType(dataCard.wardPath!!, dataCard.type!!)
            ?.filter { it.id != dataCard.id }
            ?.sortedBy { it.order }

        // If order number is between other cards, re-order the other cards order numbers.
        // Otherwise, set as the last card
        cards?.takeIf { it.isNotEmpty() }?.let { c ->
            dataCard.order?.let { order ->
                if (order in c.first().order!!..c.last().order!! ) {
                    c.filter { it.order!! >= dataCard.order!! }.forEach { o ->
                        o.order = o.order?.plus(1)
                    }
                    repo.saveAll(c)
                }
            } ?: run { dataCard.order = cards.size + 1 }
        }
        repo.save(dataCard)
        r.addFlashAttribute("messages", m.success("DataCard added for ${dataCard.title}"))
        return "redirect:/user/datacard"
    }

    @PostMapping("/delete")
    fun delete(@RequestParam id: String, r: RedirectAttributes, m: Messages): String {
        repo.deleteById(id)
        r.addFlashAttribute("messages", m.success("You have successfully deleted the DataCard."))
        return "redirect:/user/datacard"
    }

    @GetMapping("/order/{type}")
    fun order(@PathVariable type: String, model: MutableMap<String, Any?>, session: HttpSession): String {
        val user = session.getAttribute("user") as User
        val datacards = repo.findByWardPathAndType(user.ward?.path!!, type)
        model["datacards"] = datacards?.sortedBy { it.order }
        model["cardNumbers"] = (1..datacards?.maxBy { it.order!! }?.order!!).toList()
        model["cardType"] = type
        return "user/datacard_order"
    }

    @PostMapping("/order/{type}/save")
    fun saveOrder(datacards: DataCardOrder, @PathVariable type: String, r: RedirectAttributes, m: Messages): String {
        if (duplicateOrder(datacards.datacards!!)) {
            r.addFlashAttribute("messages", m.error("You cannot have any Datacards with the same order number."))
            return "redirect:/user/datacard/order/$type"
        }
        r.addFlashAttribute("messages", m.success("You have successfully ordered the ${type}'s."))
        repo.saveAll(datacards.datacards!!)
        return "redirect:/user/datacard"
    }


    @ResponseBody
    @GetMapping("/active")
    fun activateCard(@RequestParam active: Boolean, @RequestParam id: String): Boolean {
        val card = repo.findByIdOrNull(id)
        card?.let {
            it.active = active
            repo.save(card)
            return true
        } ?: run {
            return false
        }
    }

    private fun duplicateOrder(d: List<DataCard>): Boolean {
        return d.groupingBy { it.order }.eachCount().filter { it.value > 1 }.isNotEmpty()
    }
}