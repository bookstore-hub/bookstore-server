package com.rdv.server.core.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


/**
 * @author davidgarcia
 */

@RestController
@CrossOrigin
@RequestMapping("/search")
@Tag(name = "SearchController", description = "Set of endpoints to handle the RDV search logic")
public class SearchController {

    protected static final Log LOGGER = LogFactory.getLog(SearchController.class);

    //search

}
