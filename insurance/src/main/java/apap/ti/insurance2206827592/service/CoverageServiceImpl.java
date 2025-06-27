package apap.ti.insurance2206827592.service;

import apap.ti.insurance2206827592.model.Coverage;
import apap.ti.insurance2206827592.repository.CoverageDb;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CoverageServiceImpl implements CoverageService {

    @Autowired
    CoverageDb coverageDb;

    @Override
    public List<Coverage> getAllCoverage() { return coverageDb.findAll(); }
}
