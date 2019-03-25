/*
 *
 * Copyright (c) 2019 xp-dojo organisation and committers
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

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