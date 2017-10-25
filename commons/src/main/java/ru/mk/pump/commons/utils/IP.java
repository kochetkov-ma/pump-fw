package ru.mk.pump.commons.utils;

import java.net.InetAddress;
import java.net.UnknownHostException;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;
import ru.mk.pump.commons.exception.UtilException;

@UtilityClass
@Slf4j
public final class IP {

    public InetAddress getAddress() {
        try {
            return InetAddress.getLocalHost();
        } catch (UnknownHostException e) {
            throw new UtilException("Error address", e);
        }
    }

    public String getIP() {
        return getAddress().getHostAddress();
    }

    public String getHost() {
        return getAddress().getCanonicalHostName();
    }
}
