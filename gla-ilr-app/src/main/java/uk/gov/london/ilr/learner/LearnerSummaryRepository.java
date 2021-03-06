/**
 * Copyright (c) Greater London Authority, 2016.
 *
 * This source code is licensed under the Open Government Licence 3.0.
 *
 * http://www.nationalarchives.gov.uk/doc/open-government-licence/version/3/
 */
package uk.gov.london.ilr.learner;

import com.querydsl.core.types.Predicate;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

import java.util.Set;

public interface LearnerSummaryRepository extends JpaRepository<LearnerSummary, LearnerPK>,
    QuerydslPredicateExecutor<LearnerSummary> {

    default Page<LearnerSummary> findAll(String learner, Set<Integer> ukprns, Integer academicYear,
        Boolean filterBySupplementaryData, Pageable pageable) {
        Predicate predicate = new LearnerSummaryQueryBuilder()
                .withLearner(learner)
                .withUkprns(ukprns)
                .withAcademicYear(academicYear)
                .withFilterBySupplementaryData(filterBySupplementaryData)
                .getPredicate();
        if (predicate == null) {
            return findAll(pageable);
        } else {
            return findAll(predicate, pageable);
        }
    }

}
