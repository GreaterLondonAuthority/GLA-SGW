/**
 * Copyright (c) Greater London Authority, 2016.
 *
 * This source code is licensed under the Open Government Licence 3.0.
 *
 * http://www.nationalarchives.gov.uk/doc/open-government-licence/version/3/
 */
package uk.gov.london.ilr.web;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@Api(hidden = true)
public class HealthCheckController {

    Logger log = LoggerFactory.getLogger(getClass());

    @Autowired
    JdbcTemplate jdbcTemplate;

    boolean mocked = false;

    public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @RequestMapping(value = "/healthcheck", method = RequestMethod.GET)
    @ApiOperation(value = "", hidden = true)
    public @ResponseBody
    ResponseEntity<String> healthCheck() {
        if (mocked) {
            throw new RuntimeException("health check mocked to fail!");
        }
        jdbcTemplate.execute("select 1");
//        if (dataInitialiser.isFinished()) {
            return ResponseEntity.ok("OK");
//        }
//        else {
//            return ResponseEntity.status(503).body("KO");
//        }
    }

}
