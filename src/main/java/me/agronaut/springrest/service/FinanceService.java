package me.agronaut.springrest.service;

import me.agronaut.springrest.model.Finance;
import me.agronaut.springrest.model.FinanceDto;
import me.agronaut.springrest.model.User;
import me.agronaut.springrest.repository.FinanceRepository;
import me.agronaut.springrest.util.LogUtil;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class FinanceService {
    private final FinanceRepository financeRepository;
    private final LogUtil logger = new LogUtil(UserService.class);
    private final ModelMapper modelMapper;

    @Autowired
    public FinanceService(FinanceRepository financeRepository, ModelMapper modelMapper) {
        this.financeRepository = financeRepository;
        this.modelMapper = modelMapper;
    }


    public Page<FinanceDto> getAllByUser(User user, Pageable pageable) {
        logger.debug("FinanceService.getAllByUser - START");

        List<Finance> res = financeRepository.getAllByUser(user);

        return new PageImpl<>(res.stream().map(iter -> modelMapper.map(iter, FinanceDto.class)).toList(), pageable, res.size());
    }

    public FinanceDto save(FinanceDto financeDto, User user) {
        logger.debug("FinanceService.save - START");
        Finance casted = modelMapper.map(financeDto, Finance.class);
        casted.setUser(user);
        return modelMapper.map(financeRepository.save(casted), FinanceDto.class);
    }

    public FinanceDto update(FinanceDto financeToUpdate) {
        logger.debug("FinanceService.update - START");
        Finance casted = modelMapper.map(financeToUpdate, Finance.class);
        financeRepository.save(casted);

        return modelMapper.map(casted, FinanceDto.class);
    }

    public void delete(Long id) {
        logger.debug("FinanceService.delete - START");

        financeRepository.deleteById(id);
    }

    public FinanceDto  getById(Long id) {
        logger.debug("FinanceService.getById - START");
        return modelMapper.map(financeRepository.getById(id).orElseThrow(NoSuchElementException::new), FinanceDto.class);
    }

    public Page<FinanceDto> getAllActualMonth(Pageable pageable) {
        logger.debug("getAllActualMonth - START");
        // set first and last date for between
        Calendar now = Calendar.getInstance();
        now.set(Calendar.DAY_OF_MONTH, 1);
        Date firstDay = now.getTime();
        now.set(Calendar.DAY_OF_MONTH, Calendar.getInstance().getActualMaximum(Calendar.DAY_OF_MONTH));
        Date lastDay = now.getTime();

        List<FinanceDto> res = new LinkedList<>();

        // get all repeatable finance
        res.addAll(getAllRepeatable().stream().map((element) -> modelMapper.map(element, FinanceDto.class)).toList());

        // get all by actual month
        res.addAll(financeRepository.findAllByRepeatDateBetween(firstDay, lastDay).stream().map((element) -> modelMapper.map(element, FinanceDto.class)).toList());

        return new PageImpl<>(res, pageable, res.size());
    }

    public List<Finance> getAllRepeatable() {
        return financeRepository.findAllByRepeatableIsTrue();
    }
}
