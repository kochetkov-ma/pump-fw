package ru.mk.pump.commons.exception;

@SuppressWarnings("unused")
public class ConfigurationException extends PumpException {

    public ConfigurationException(String title) {
        super(title);
    }

    public ConfigurationException(String title, Throwable cause) {
        super(title, cause);
    }

    public ConfigurationException(PumpMessage exceptionMessage) {
        super(exceptionMessage);
    }

    public ConfigurationException(PumpMessage exceptionMessage, Throwable cause) {
        super(exceptionMessage, cause);
    }
}
