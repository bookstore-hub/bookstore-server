package com.rdv.server.core.util.fuzzymatching.user;


import com.miguelfonseca.completely.AutocompleteEngine;
import com.miguelfonseca.completely.text.analyze.tokenize.WordTokenizer;
import com.miguelfonseca.completely.text.analyze.transform.LowerCaseTransformer;
import com.rdv.server.core.entity.User;
import com.rdv.server.core.repository.UserRepository;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.BeanInitializationException;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;


/**
 * @author davidgarcia
 */
@Component
public class UserMatcher implements ApplicationListener<ApplicationReadyEvent> {

    private static final Log LOGGER = LogFactory.getLog(User.class);

    private AutocompleteEngine<UserRecord> engine = null;
    private boolean initialized = false;

    private final UserRepository userRepository;

    public UserMatcher(UserRepository userRepository) {
        this.userRepository = userRepository;
    }


    @Override
    public void onApplicationEvent(final ApplicationReadyEvent event) {
        if (LOGGER.isInfoEnabled()) {
            LOGGER.info("UserMatcher initialized start: " + new Date());
        }
        engine = new AutocompleteEngine.Builder<UserRecord>()
                .setIndex(new UserAdapter())
                .setAnalyzers(new LowerCaseTransformer(), new WordTokenizer())
                .build();

        engine.addAll(userRepository.findAllUsers().stream().map(UserRecord::new).toList());

        initialized = true;

        if (LOGGER.isInfoEnabled()) {
            LOGGER.info("UserMatcher initialized end: " + new Date());
        }
    }

    public List<User> collectPossibleMatches(String query) {
        if (!initialized) {
            throw new BeanInitializationException("Users not initialized yet");
        }

        List<UserRecord> matches = engine.search(query);
        List<User> users = matches.stream().map(UserRecord::getUser).toList();

        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("Results for " + query + ": " + users.stream().map(Object::toString).toList());
        }

        return users;
    }

}
