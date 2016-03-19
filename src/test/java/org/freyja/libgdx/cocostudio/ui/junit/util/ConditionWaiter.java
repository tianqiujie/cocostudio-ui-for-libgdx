/*
 * Copyright 2016 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.freyja.libgdx.cocostudio.ui.junit.util;

import org.freyja.libgdx.cocostudio.ui.junit.exception.TimeoutException;
import org.junit.runner.Description;

import java.util.concurrent.TimeUnit;

public class ConditionWaiter {
    public static void wait(Condition condition, String message, final int maxCount, final Runnable runnable) {
        int current = 0;
        while (!condition.check()) {
            try {
                TimeUnit.SECONDS.sleep(1);
            } catch (InterruptedException e) {

            }
            current++;
            if (current > maxCount) {
                if (runnable != null) {
                    runnable.run();
                }
                throw new TimeoutException(message);
            }
        }
    }

    public static void wait(Condition condition, String message, final int maxCount) {
        wait(condition, message, maxCount, null);
    }

    public static void wait(Condition condition, Description description, final int maxCount, final Runnable runnable) {
        wait(condition, "timeout! " + description.toString(), maxCount, runnable);
    }
}
