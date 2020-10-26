package com.service.statistic;

import com.model.Statistic;
import com.repository.statistic.StatisticRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class StatisticServiceImpl implements StatisticService {
    @Autowired
    private StatisticRepo staticRepo;

    @Override
    public List<Statistic> getAllByDate(Date date) {
        return staticRepo.getAllByDate(date);
    }

    @Override
    public Statistic addStatistic(Statistic statistic) {
        return staticRepo.save(statistic);
    }

    @Override
    public void delete(Statistic statistic) {
        staticRepo.delete(statistic);
    }

    @Override
    public void updateStatistic(Statistic statistic) {
        staticRepo.saveAndFlush(statistic);
    }

    @Override
    public Statistic getStatisticById(int id) {
        return staticRepo.getOne(id);
    }

    @Override
    public List<Statistic> getAllStatistic() {
        return staticRepo.findAll();
    }
}