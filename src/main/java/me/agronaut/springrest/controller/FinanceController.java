package me.agronaut.springrest.controller;

import me.agronaut.springrest.model.FinanceDto;
import me.agronaut.springrest.model.User;
import me.agronaut.springrest.service.FinanceService;
import me.agronaut.springrest.service.UserService;
import me.agronaut.springrest.util.LogUtil;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

@RestController
@RequestMapping("/finance")
public class FinanceController {
    @Qualifier("FinanceService")
    private final FinanceService financeSD;
    @Qualifier("userService")
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

    /**
     * Get list of all {@link me.agronaut.springrest.model.Finance} by currently logged in user
     * @param request request object from endpoint
     * @param pageable pageable object from endpoint
     * @return {@link Page} object with data
     */
    @GetMapping("/get")
    public Page<FinanceDto> getAllByUSer(HttpServletRequest request, Pageable pageable) {
        User current = userSD.getByUsername(request.getUserPrincipal().getName());

        return financeSD.getAllByUser(current, pageable);
    }

    /**
     * Get all finances by User with given ID
     * @param userId wanted User's id in database
     * @param pageable pageable object from endpoint
     * @return {@link Page} object with data
     */
    @GetMapping("/get/{userId}")
    public Page<FinanceDto> getAllByUSerWithParameter(@PathVariable Long userId, Pageable pageable) {
        User current = userSD.getById(userId);

        return financeSD.getAllByUser(current, pageable);
    }
}
