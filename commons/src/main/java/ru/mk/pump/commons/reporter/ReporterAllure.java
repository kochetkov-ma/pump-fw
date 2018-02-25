package ru.mk.pump.commons.reporter;


import io.qameta.allure.Allure;
import io.qameta.allure.model.Parameter;
import io.qameta.allure.model.Status;
import io.qameta.allure.model.StepResult;
import io.qameta.allure.model.TestResult;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import org.joda.time.format.DateTimeFormatterBuilder;
import ru.mk.pump.commons.exception.ExecutionException;
import ru.mk.pump.commons.exception.PumpMessage;
import ru.mk.pump.commons.utils.Strings;

import java.io.ByteArrayInputStream;
import java.util.Deque;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ConcurrentLinkedDeque;
import java.util.function.Consumer;

import static ru.mk.pump.commons.constants.StringConstants.LINE;

@SuppressWarnings({"unused", "WeakerAccess"})
@Slf4j
@ToString(exclude = {"currentTestUuid", "blockUuidQueue"})
public class ReporterAllure implements Reporter, AutoCloseable {

    /**
     * For your information.
     * This path cannot be changed after initialisation Allure Lifecycle in any places of program, not only {@link ReporterAllure} such as AllureJunitAdapter or other
     */
    public static final String SYSTEM_ALLURE_RESULT_PATH = "allure.results.directory";

    private static final String LOG_PREFIX = "[REPORTER] STEP";

    private final Type minimumLevelForDuplicateInSlf4;

    @Setter
    private Type autoScreenLevel = Type.OFF;

    @Setter
    private Type postingLevel = Type.INFO;

    private ThreadLocal<String> currentTestUuid = new InheritableThreadLocal<>();

    private ThreadLocal<Deque<String>> blockUuidQueue = InheritableThreadLocal.withInitial(ConcurrentLinkedDeque::new);
    @Getter
    private AttachmentsFactory attachmentsFactory;

    @AllArgsConstructor
    private class Info {

        private static final String I = "kochetkov-ma@yandex.ru";

        private Attachment attachment;

        private Throwable throwable;

        @Override
        public String toString() {
            return Strings
                    .space(I, attachment != null ? "Attachment exists" : null,
                            throwable != null ? "Throwable class " + throwable.getClass().getSimpleName() : null);
        }

        Attachment attachment() {
            return attachment;
        }

        Throwable throwable() {
            return throwable;
        }
    }

    public ReporterAllure() {
        this.attachmentsFactory = new AttachmentsFactory(Optional::empty);
        this.minimumLevelForDuplicateInSlf4 = Type.INFO;
    }

    public ReporterAllure(Screenshoter screenshoter) {
        this.attachmentsFactory = new AttachmentsFactory(screenshoter);
        this.minimumLevelForDuplicateInSlf4 = Type.INFO;
    }

    public ReporterAllure(Screenshoter screenshoter, Type minimumLevelForDuplicateInSlf4) {

        this.attachmentsFactory = new AttachmentsFactory(screenshoter);
        this.minimumLevelForDuplicateInSlf4 = minimumLevelForDuplicateInSlf4;
    }

    public ReporterAllure withAttachmentsFactory(AttachmentsFactory attachmentsFactory) {
        this.attachmentsFactory = attachmentsFactory;
        return this;
    }

    @Override
    public void testStart(String title, String description) {
        currentTestUuid.set(testStart(title, description, null));
    }

    @Override
    public void testStop() {
        testStop(currentTestUuid.get(), tc -> tc.setStatus(Status.PASSED));
    }

    @Override
    public void info(String title, String description, Attachment attachment) {
        step(Type.INFO, title, description, new Info(attachment, null));
    }

    @Override
    public void info(String title, String description) {
        step(Type.INFO, title, description, new Info(null, null));
    }

    @Override
    public void blockStart(String title, String description) {
        final String uuid = UUID.randomUUID().toString();
        final StepResult stepResult = new StepResult()
                .withDescription(description)
                .withName(title);
        Allure.getLifecycle().startStep(uuid, stepResult);
        blockUuidQueue.get().add(uuid);
    }

    @Override
    public void blockStop() {
        if (!blockUuidQueue.get().isEmpty()) {
            blockStop(blockUuidQueue.get().pop());
        }
    }

    @Override
    public void blockStopAll() {
        while (!blockUuidQueue.get().isEmpty()) {
            blockStop(blockUuidQueue.get().pop());
        }
    }

    @Override
    public void warn(String title, String description, Attachment attachment, Throwable throwable) {
        step(Type.WARNING, title, Strings.concat(LINE, description, Strings.toString(throwable)), new Info(attachment, null));
    }

    @Override
    public void warn(String title, String description, Throwable throwable) {
        step(Type.WARNING, title, Strings.concat(LINE, description, Strings.toString(throwable)), new Info(null, null));
    }

    @Override
    public void warn(String title, String description, Attachment attachment) {
        step(Type.WARNING, title, description, new Info(attachment, null));
    }

    @Override
    public void warn(String title, String description) {
        step(Type.WARNING, title, description, new Info(null, null));
    }

    @Override
    public void error(String title, String description) {
        step(Type.ERROR, title, description, new Info(null, new AssertionError("Test error")));
    }

    @Override
    public void error(String title, String description, Attachment attachment) {
        step(Type.ERROR, title, description, new Info(attachment, new AssertionError("Test error")));
    }

    @Override
    public void error(String title, String description, Attachment attachment, Throwable throwable) {
        step(Type.ERROR, title, description, new Info(attachment, throwable));
    }

    @Override
    public void error(String title, String description, Throwable throwable) {
        step(Type.ERROR, title, description, new Info(null, throwable));
    }

    @Override
    public void attach(Attachment attachment) {
        if (attachment.getSourceByte() != null) {
            Allure.addAttachment(attachment.getName(), attachment.getType(), new ByteArrayInputStream(attachment.getSourceByte().get()),
                    attachment.getExtension());
        } else {
            Allure.addAttachment(attachment.getName(), attachment.getType(), attachment.getSource(), attachment.getExtension());
        }
    }

    @Override
    public void fail(String title, String description, Attachment attachment, AssertionError assertionError) {
        step(Type.FAIL, title, description, new Info(attachment, assertionError));
    }

    @Override
    public void pass(String title, String description, Attachment attachment) {
        step(Type.PASS, title, description, new Info(attachment, null));
    }

    @Override
    public void pass(String title, String description) {
        step(Type.PASS, title, description, new Info(null, null));
    }

    @Override
    public AttachmentsFactory attachments() {
        return attachmentsFactory;
    }

    public String testStart(String title, String description, Consumer<TestResult> testResultConsumer) {
        String uuid = UUID.randomUUID().toString();
        final TestResult testResult = new TestResult()
                .withDescription(description)
                .withName(title)
                .withUuid(uuid);
        if (testResultConsumer != null) {
            testResultConsumer.accept(testResult);
        }
        Allure.getLifecycle().scheduleTestCase(testResult);
        Allure.getLifecycle().startTestCase(uuid);
        return uuid;
    }

    public void testStop(String uuid, Consumer<TestResult> testResultConsumer) {
        blockStopAll();
        if (!Strings.isEmpty(uuid)) {
            if (testResultConsumer != null) {
                Allure.getLifecycle().updateTestCase(uuid, testResultConsumer);
            }
            Allure.getLifecycle().stopTestCase(uuid);
            Allure.getLifecycle().writeTestCase(uuid);
        }
    }

    @Override
    public void close() {
        //testStop();
    }

    private static String now() {
        return org.joda.time.Instant.now().toString(new DateTimeFormatterBuilder()
                .appendPattern("dd-MM-yy HH:mm:ss:SSSS")
                .toFormatter());
    }

    private void blockStop(String uuid) {
        Allure.getLifecycle().updateStep(uuid, block -> block.withStatus(Status.PASSED));
        Allure.getLifecycle().stopStep(uuid);
    }

    /**
     * @param level       step level
     * @param title       step title
     * @param description need to be title + lineSeparator + description for full information
     */
    private void step(Type level, String title, String description, Info info) {
        if (postingLevel.getPriority() > level.getPriority()) {
            log.debug(Strings.space(LOG_PREFIX, "skip this message with level=", level.name(), "minimal level=", postingLevel.name()));
            return;
        }
        final String stepUuid = UUID.randomUUID().toString();
        final StepResult stepResult = new StepResult()
                .withName(level.getValue() + title)
                .withStatus(Status.PASSED)
                .withParameters(new Parameter().withName("status").withValue(level.name()),
                        new Parameter().withName("time").withValue(now()));
        if (Allure.getLifecycle().getCurrentTestCase().isPresent()) {
            Allure.getLifecycle().startStep(stepUuid, stepResult);
        } else {
            String blockUuid = blockUuidQueue.get().peekLast();
            if (blockUuid != null) {
                Allure.getLifecycle().startStep(blockUuid, stepUuid, stepResult);
            } else if (currentTestUuid.get() != null) {
                Allure.getLifecycle().startStep(currentTestUuid.get(), stepUuid, stepResult);
            }
        }
        if (Objects.nonNull(description)) {
            if (description.contains("\n")) {
                attach(attachments().text("Description", description));
            } else {
                Allure.getLifecycle().updateStep(stepUuid, res -> res.withParameters(new Parameter().withName("description").withValue(description)));
            }
        }

        if (level.getPriority() >= minimumLevelForDuplicateInSlf4.getPriority()) {
            if (level.getPriority() < 3) {
                log.info(Strings.concat(System.lineSeparator(), Strings.space(LOG_PREFIX, level.toString(), title), description, info.toString()));
            } else if (level.getPriority() == 3) {
                log.warn(Strings.concat(System.lineSeparator(), Strings.space(LOG_PREFIX, level.toString(), title), description, info.toString()));
            } else if (level.getPriority() > 3) {
                log.error(Strings.concat(System.lineSeparator(), Strings.space(LOG_PREFIX, level.toString(), title), description, info.toString()));
            }
        }
        if (info.attachment() != null) {
            attach(info.attachment());
        }
        if (!AttachmentsFactory.isScreen(info.attachment) && autoScreenLevel.getPriority() <= level.getPriority()) {
            log.debug(Strings.space(LOG_PREFIX, "auto attach screen with level=", level.name(), "minimal auto-screen level=", autoScreenLevel.name()));
            attach(getAttachmentsFactory().screen(String.format("auto screen on '%s'", level)));
        }
        if (info.throwable() != null) {
            if (info.throwable() instanceof AssertionError) {
                Allure.getLifecycle().updateStep(stepUuid, res -> res.setStatus(Status.FAILED));
                Allure.getLifecycle().stopStep(stepUuid);
                throw (AssertionError) info.throwable();
            } else {
                final PumpMessage exceptionMessage = new PumpMessage(title, description)
                        .addExtraInfo("Status", level.name())
                        .addExtraInfo("Attachment",
                                info.attachment() != null ? Strings.space(info.attachment().getName(), info.attachment().getType()) : null);
                Allure.getLifecycle().updateStep(stepUuid, res -> res.setStatus(Status.BROKEN));
                Allure.getLifecycle().stopStep(stepUuid);
                throw new ExecutionException(exceptionMessage, info.throwable());
            }
        }
        Allure.getLifecycle().stopStep(stepUuid);
    }

    public enum Type {
        OFF("", Integer.MAX_VALUE),
        ALL("", 0),
        INFO("", 2),
        WARNING("[WARN] ", 3),
        ERROR("[ERROR] ", 4),
        ATTACHMENT("[ATTACHMENT] ", 0),
        PASS("[PASS] ", 1),
        FAIL("[FAIL] ", 4);

        @Getter
        private final String value;

        @Getter
        private final int priority;

        Type(String value, int priority) {
            this.value = value;
            this.priority = priority;
        }

        @Override
        public String toString() {
            return name();
        }
    }
}
