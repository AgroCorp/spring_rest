package me.agronaut.springrest.controller;

import me.agronaut.springrest.model.FinanceDto;
import me.agronaut.springrest.model.User;
import me.agronaut.springrest.service.FinanceService;
import me.agronaut.springrest.service.UserService;
import me.agronaut.springrest.util.LogUtil;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

@RestController
@RequestMapping("/finance")
@CrossOrigin
public class FinanceController {
    private final FinanceService financeSD;
    private final UserService userSD;

    private final LogUtil logger = new LogUtil(getClass());

    public FinanceController(FinanceService financeSD, UserService userSD) {
        this.financeSD = financeSD;
        this.userSD = userSD;
    }

    @PostMapping("/add")
    public FinanceDto add(@Valid @RequestBody FinanceDto financeDto, HttpServletRequest request) {
        logger.debug("principalname:", "NAME", request.getUserPrincipal().getName());
        User current = userSD.getByUsername(request.getUserPrincipal().getName());
        logger.debug("user", "USER", current);
        return financeSD.save(financeDto, current);
    }

    @PutMapping("/update")
    public FinanceDto updateFinance(@Valid @RequestBody FinanceDto financeToUpdate) {
        return financeSD.update(financeToUpdate);
    }

    @DeleteMapping("/delete/{id}")
    public void deleteSelected(@PathVariable("id") Long id) {
        financeSD.delete(id);
    }

    @GetMapping("/get-all-by-user")
    public Page<FinanceDto> getAllByUSer(HttpServletRequest request, Pageable pageable) {
        User current = userSD.getByUsername(request.getUserPrincipal().getName());

        return financeSD.getAllByUser(current, pageable);
    }
}
