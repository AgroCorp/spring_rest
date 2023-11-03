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

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.time.LocalDateTime;
import java.util.Calendar;
import java.util.LinkedList;
import java.util.List;
import java.util.NoSuchElementException;

@Service
public class FinanceService {
    private final FinanceRepository financeRepository;
    private final LogUtil logger = new LogUtil(UserService.class);
    private final ModelMapper modelMapper;

    @PersistenceContext
    private EntityManager entityManager;

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

    public Page<FinanceDto> getAllActualMonth(User currentUser, Pageable pageable) {
        logger.debug("getAllActualMonth - START");
        // set first and last date for between
        Calendar now = Calendar.getInstance();
        now.set(Calendar.DAY_OF_MONTH, 1);
        now.set(Calendar.HOUR_OF_DAY, 0);
        now.set(Calendar.MINUTE, 0);
        now.set(Calendar.SECOND, 0);
        now.set(Calendar.MILLISECOND, 0);

        LocalDateTime firstDay = LocalDateTime.ofInstant(now.toInstant(), now.getTimeZone().toZoneId());
        now.set(Calendar.DAY_OF_MONTH, Calendar.getInstance().getActualMaximum(Calendar.DAY_OF_MONTH));
        now.set(Calendar.HOUR_OF_DAY, 23);
        now.set(Calendar.MINUTE, 59);
        now.set(Calendar.SECOND, 59);
        now.set(Calendar.MILLISECOND, 999);
        LocalDateTime lastDay = LocalDateTime.ofInstant(now.toInstant(), now.getTimeZone().toZoneId());

        List<FinanceDto> res = new LinkedList<>();

        // get all repeatable finance
        res.addAll(getAllRepeatable().stream().map(element -> modelMapper.map(element, FinanceDto.class)).toList());

        // get all by actual month
        res.addAll(financeRepository.findAllByRepeatDateBetweenAndUser(firstDay, lastDay, currentUser).stream().map(element -> modelMapper.map(element, FinanceDto.class)).toList());

        return new PageImpl<>(res, pageable, res.size());
    }

    public List<Finance> getAllRepeatable() {
        return financeRepository.findAllByRepeatableIsTrue();
    }

    public Page<FinanceDto> getAllByForm(FinanceDto searchForm, Pageable pageable) {
        CriteriaBuilder builder = entityManager.getCriteriaBuilder();

        CriteriaQuery<Finance> query = builder.createQuery(Finance.class);

        Root<Finance> root = query.from(Finance.class);

        LinkedList<Predicate> where = new LinkedList<>();
        if (searchForm.getName() != null && !searchForm.getName().isEmpty()) {
            where.add(builder.like(root.get("name"), "%" + searchForm.getName() + "%"));
        }
        if (searchForm.getAmount() != null) {
            where.add(builder.equal(root.get("amount"), searchForm.getAmount()));
        }
        if (searchForm.getCategory() != null) {
            where.add(builder.equal(root.get("category"), searchForm.getCategory()));
        }
        if (searchForm.getUser() != null) {
            where.add(builder.equal(root.get("user"), searchForm.getUser()));
        }
        if (searchForm.getIncome()!= null) {
            where.add(builder.equal(root.get("income"), searchForm.getIncome()));
        }
        if (searchForm.getRepeatable() != null) {
            where.add(builder.equal(root.get("repeatable"), searchForm.getRepeatable()));
        }
        if (searchForm.getRepeatDate() != null) {
            LocalDateTime from = searchForm.getRepeatDate().toLocalDate().atTime(0,0);
            LocalDateTime to = searchForm.getRepeatDate().toLocalDate().atTime(23,59);
            where.add(builder.between(root.get("repeatDate"),from, to));
        }

        query.select(root).where(builder.and(where.toArray(new Predicate[0])));

        int count = entityManager.createQuery(query).getResultList().size();
        List<Finance> res = entityManager.createQuery(query).setFirstResult((int) pageable.getOffset())
                .setMaxResults(pageable.getPageSize()).getResultList();

        return new PageImpl<>(res.stream().map(element -> modelMapper.map(element, FinanceDto.class)).toList(), pageable, count);
    }
}
