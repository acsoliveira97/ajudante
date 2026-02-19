package pt.cinzarosa.ajudante.controller

import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping

@Controller
@RequestMapping("/ui/shifts")
class ShiftUiController {

    @GetMapping("/new")
    fun newShift(): String {
        return "shifts/new"
    }
}