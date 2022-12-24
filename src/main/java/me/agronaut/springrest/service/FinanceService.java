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

@Service
public class FinanceService {
    private final FinanceRepository financeRepository;
    private final ModelMapper modelMapper;

    private final LogUtil logger = new LogUtil(this.getClass());

    @Autowired
    public FinanceService(FinanceRepository financeRepository, ModelMapper modelMapper) {
        this.financeRepository = financeRepository;
        this.modelMapper = modelMapper;
    }


    public Page<FinanceDto> getAllByUser(User user, Pageable pageable) {
        List<Finance> res = financeRepository.getAllByUser(user);

        return new PageImpl<>(res.stream().map(iter -> modelMapper.map(iter, FinanceDto.class)).toList(), pageable, res.size());
    }

    public FinanceDto save(FinanceDto financeDto, User user) {
        Finance casted = modelMapper.map(financeDto, Finance.class);
        casted.setUser(user);
        return modelMapper.map(financeRepository.save(casted), FinanceDto.class);
    }
}
