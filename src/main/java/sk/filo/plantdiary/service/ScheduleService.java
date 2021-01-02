package sk.filo.plantdiary.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import sk.filo.plantdiary.service.so.CreateScheduleSO;
import sk.filo.plantdiary.service.so.ScheduleSO;

import java.util.List;

@Service
public class ScheduleService {

    private static final Logger LOGGER = LoggerFactory.getLogger(ScheduleService.class);

    public ScheduleSO create(CreateScheduleSO createScheduleSO) {
        throw new java.lang.UnsupportedOperationException("Not supported yet.");
    }

    public ScheduleSO update(ScheduleSO scheduleSO) {
        throw new java.lang.UnsupportedOperationException("Not supported yet.");
    }

    public ScheduleSO getOne(Long id) {
        throw new java.lang.UnsupportedOperationException("Not supported yet.");
    }

    public List<ScheduleSO> getAll() {
        throw new java.lang.UnsupportedOperationException("Not supported yet.");
    }

    public void delete(Long id) {
        throw new java.lang.UnsupportedOperationException("Not supported yet.");
    }
}
