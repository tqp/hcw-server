package com.timsanalytics.crc.main.services.types;

import com.timsanalytics.crc.main.beans.types.ServicesProvidedType;
import com.timsanalytics.crc.main.dao.types.ServicesProvidedTypeDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ServicesProvidedTypeService {
    private final ServicesProvidedTypeDao servicesProvidedTypeDao;

    @Autowired
    public ServicesProvidedTypeService(ServicesProvidedTypeDao servicesProvidedTypeDao) {
        this.servicesProvidedTypeDao = servicesProvidedTypeDao;
    }

    public List<ServicesProvidedType> getServicesProvidedTypeList() {
        return this.servicesProvidedTypeDao.getServicesProvidedTypeList();
    }
}
