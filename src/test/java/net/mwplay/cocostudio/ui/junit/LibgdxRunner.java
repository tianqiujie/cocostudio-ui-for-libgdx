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
package net.mwplay.cocostudio.ui.junit;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.jglfw.JglfwApplication;
import com.badlogic.gdx.backends.jglfw.JglfwApplicationConfiguration;
import net.mwplay.cocostudio.ui.junit.exception.LibgdxInitException;
import net.mwplay.cocostudio.ui.junit.util.Condition;
import net.mwplay.cocostudio.ui.junit.util.ConditionWaiter;
import org.apache.commons.io.FileUtils;
import org.junit.runner.Description;
import org.junit.runner.notification.RunNotifier;
import org.junit.runners.BlockJUnit4ClassRunner;
import org.junit.runners.model.FrameworkMethod;
import org.junit.runners.model.InitializationError;

import java.io.File;
import java.io.IOException;
import java.util.Random;
import java.util.concurrent.atomic.AtomicBoolean;

public class LibgdxRunner extends BlockJUnit4ClassRunner {
    private Random random = new Random();
    private static File prefs;
    private static AtomicBoolean init = new AtomicBoolean(false);

    public LibgdxRunner(Class<?> klass) throws InitializationError {
        super(klass);
        if (init.compareAndSet(false, true)) {
            initApplication();
        }
    }

    private void initApplication() {
        try {
            JglfwApplicationConfiguration cfg = new JglfwApplicationConfiguration();
            cfg.preferencesLocation = String.format("tmp/%d/.prefs/", random.nextLong());
            cfg.title = "Libgdx Runner";
            cfg.width = 1;
            cfg.height = 1;
            cfg.forceExit = true;
            new JglfwApplication(new TestApplicationListener(), cfg);
            ConditionWaiter.wait(new Condition() {
                @Override
                public Boolean check() {
                    return Gdx.files != null;
                }
            }, "Jglfw init failed.", 10);
            prefs = new File(Gdx.files.getExternalStoragePath(), "tmp/");
            Runtime.getRuntime().addShutdownHook(new Thread(new Runnable() {
                @Override
                public void run() {
                    LibgdxRunner.this.safeCleanDir();
                    LibgdxRunner.this.closeGdxApplication();
                }
            }));
        } catch (Exception ex) {
            throw new LibgdxInitException(ex);
        }
    }

    private void safeCleanDir() {
        try {
            FileUtils.deleteDirectory(prefs);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void closeGdxApplication() {
        Gdx.app.exit();
    }

    @Override
    public void run(RunNotifier notifier) {
        super.run(notifier);
    }

    @Override
    protected void runChild(final FrameworkMethod method, final RunNotifier notifier) {
        final Description description = describeChild(method);
        if (description.getAnnotation(NeedGL.class) != null) {
            final AtomicBoolean running = new AtomicBoolean(true);
            Gdx.app.postRunnable(new Runnable() {
                @Override
                public void run() {
                    if (LibgdxRunner.this.isIgnored(method)) {
                        notifier.fireTestIgnored(description);
                    } else {
                        LibgdxRunner.this.runLeaf(methodBlock(method), description, notifier);
                    }
                    running.set(false);
                }
            });
            ConditionWaiter.wait(new Condition() {
                @Override
                public Boolean check() {
                    return !running.get();
                }
            }, description, 30, new Runnable() {
                @Override
                public void run() {
                    LibgdxRunner.this.closeGdxApplication();
                }
            });
        } else {
            runLeaf(methodBlock(method), description, notifier);
        }
    }

    private class TestApplicationListener extends ApplicationAdapter {
        @Override
        public void create() {

        }
    }
}
