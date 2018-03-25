package ru.mk.pump.web.utils;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import ru.mk.pump.commons.constants.MainConstants;
import ru.mk.pump.commons.exception.PumpException;
import ru.mk.pump.commons.exception.PumpMessage;
import ru.mk.pump.commons.exception.UtilException;
import ru.mk.pump.commons.utils.HttpClients;
import ru.mk.pump.commons.utils.Strings;
import ru.mk.pump.web.browsers.Browser;

import java.io.IOException;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;
import java.util.stream.Collectors;

import static java.lang.String.format;

@SuppressWarnings("unused")
@Slf4j
public class DownloadHelper {

    private final Browser browser;
    private final Path downloadDir;

    public DownloadHelper(Browser browser) {
        this.browser = browser;
        this.downloadDir = browser.downloads().getDownloadDir();
    }

    public String download(String urlString) {
        HttpClient httpClient = HttpClients.newHttpClientNoSSL();

        final HttpGet request = new HttpGet(getURI(urlString));
        request.setHeader("Cookie", getCookies());
        Path finalPath = downloadDir.resolve(getFullFileName(urlString));
        if (Files.exists(finalPath)) {
            finalPath = downloadDir.resolve(getFullFileName(urlString) + UUID.randomUUID().toString());
        }
        try {
            final HttpResponse response = httpClient.execute(request);
            if (response.getStatusLine().getStatusCode() != HttpStatus.SC_OK) {
                throw exception(format("Request has been executed with code '%s'", response.getStatusLine().getStatusCode()), urlString, null);
            }
            FileUtils.copyInputStreamToFile(response.getEntity().getContent(), finalPath.toFile());
        } catch (IOException e) {
            throw exception("HttpClient error", urlString, e);
        }
        return finalPath.toString();
    }

    private String getCookies() {
        final String result = browser.getDriver().manage().getCookies().stream().map(cookie -> cookie.getName() + "=" + cookie.getValue())
                .collect(Collectors.joining("; "));
        log.debug("[DOWNLOAD-HELPER] Cookies from WebDriver : " + result);
        return result;
    }

    private String getFullFileName(String urlString) {
        final String[] paths = urlString.split("/");
        return paths[paths.length - 1];
    }


    private URI getURI(String urlString) {
        try {
            final URI result = new URI(urlString);
            log.debug("[DOWNLOAD-HELPER] URI : " + result);
            return result;
        } catch (Exception e) {
            throw exception("Cannot get URL", urlString, e);
        }
    }

    private UtilException exception(String message, String urlString, Throwable throwable) {
        final String finalMessage = "[DOWNLOAD-HELPER] Cannot finish download ." + message;

        throw new PumpException(new PumpMessage(finalMessage)
                .withDesc(Strings.space("URL :", urlString, "Download to :", downloadDir.toString()))
                .addExtraInfo(browser), throwable);
    }
}