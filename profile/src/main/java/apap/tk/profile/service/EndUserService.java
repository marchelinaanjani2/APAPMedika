package apap.tk.profile.service;

import apap.tk.profile.model.EndUser;
public interface EndUserService {
    EndUser addEndUser(EndUser endUser);
    EndUser getEndUserByEmail(String email);

}
