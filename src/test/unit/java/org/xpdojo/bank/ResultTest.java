package org.xpdojo.bank;

import org.junit.jupiter.api.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.xpdojo.bank.Result.failure;
import static org.xpdojo.bank.Result.success;

class ResultTest {

    @Test
    void successResultShouldReportSucceededAsTrue() {
        Result result = success();
        assertThat(result.succeeded(), is(true));
    }

    @Test
    void failureResultShouldReportSucceededAsFalse() {
        Result result = failure();
        assertThat(result.succeeded(), is(false));
    }
}