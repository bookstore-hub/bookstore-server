package com.rdv.server.core.util.fuzzymatching.user;

import com.miguelfonseca.completely.data.Indexable;
import com.rdv.server.core.entity.User;
import lombok.Getter;

import java.util.Arrays;
import java.util.List;

/**
 * @author davidgarcia
 */
@Getter
public class UserRecord implements Indexable {

    private final User user;

    public UserRecord(User userObject) {
        this.user = userObject;
    }

    @Override
    public List<String> getFields() {
        return Arrays.asList(user.getUsername(), user.getFirstName(), user.getLastName());
    }

}
