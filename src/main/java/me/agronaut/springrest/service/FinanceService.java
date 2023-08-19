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

import java.util.List;
import java.util.NoSuchElementException;

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

    public Long getSumOfMonthCurrentUser(User user) {
        return financeRepository.getSumOfCurrentMonth(user);
    }

    public void delete(Long id) {
        logger.debug("FinanceService.delete - START");

        financeRepository.deleteById(id);
    }

    public FinanceDto  getById(Long id) {
        logger.debug("FinanceService.getById - START");
        return modelMapper.map(financeRepository.getById(id).orElseThrow(NoSuchElementException::new), FinanceDto.class);
    }
}
