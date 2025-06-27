package apap.tk.profile.service;


import apap.tk.profile.model.EndUser;
import apap.tk.profile.repository.EndUserDb;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class EndUserServiceImpl implements EndUserService {
    @Autowired
    private EndUserDb endUserDb;

    public List<EndUser> getAllUsers() {
        return endUserDb.findAll();
    }

    @Override
    public EndUser addEndUser(EndUser endUser) {
        return endUserDb.save(endUser);
    }

    @Override
    public EndUser getEndUserByEmail(String email){
        return endUserDb.findByEmail(email);
    }
}
