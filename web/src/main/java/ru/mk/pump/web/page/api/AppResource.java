package ru.mk.pump.web.page.api;

interface AppResource {

    /**
     * @return Only concrete resource part. Not full URL
     */
    String getResourcePath();

    /**
     * @return Base application URL. End with '/'
     */
    String getBaseUrl();

    /**
     * @return Full url to resource (base + resource)
     */
    String getUrl();
}