/**
 * Copyright (c) Greater London Authority, 2016.
 *
 * This source code is licensed under the Open Government Licence 3.0.
 *
 * http://www.nationalarchives.gov.uk/doc/open-government-licence/version/3/
 */
package uk.gov.london.ilr.environment;

import java.time.Clock;
import java.time.OffsetDateTime;

/**
 * Represents the environment in which the application is executing.
 *
 * Created by sleach on 19/08/2016.
 */
public interface Environment {
    String shortName();

    String fullName();

    String releaseNumber();

    String buildNumber();

    String getAppVersionAndBuildNumberElement();

    String profileName();

    String hostName();

    String opsBaseUrl();

    /**
     * Returns an instance of the system clock for the active environment.
     * Note that this may be a dummy/stub/fake clock for testing.
     */
    Clock  clock();

    /**
     * Returns true if the environment should be initialised with test data.
     */
    boolean isTestEnvironment();

    String defPwHash();

    /**
     * @return the current date time.
     */
    OffsetDateTime now();
}
