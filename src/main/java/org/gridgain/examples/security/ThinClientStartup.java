/* @java.file.header */

/*  _________        _____ __________________        _____
 *  __  ____/___________(_)______  /__  ____/______ ____(_)_______
 *  _  / __  __  ___/__  / _  __  / _  / __  _  __ `/__  / __  __ \
 *  / /_/ /  _  /    _  /  / /_/ /  / /_/ /  / /_/ / _  /  _  / / /
 *  \____/   /_/     /_/   \_,__/   \____/   \__,_/  /_/   /_/ /_/
 */

package org.gridgain.examples.security;

import org.gridgain.client.*;
import org.gridgain.grid.security.*;

import java.util.*;

/**
 * Starts remote client and performs a number of cache operations.
 */
public class ThinClientStartup {
    /** Server address. */
    public static final String SRV_ADDR = "localhost:11211";

    /** Login. Change to client-no-remove to switch to client without remove permissions. */
    public static final String CLIENT_LOGIN = "client";
    //public static final String CLIENT_LOGIN = "client-no-remove";

    /** Password. */
    public static final String CLIENT_PASSWORD = "password";

    /**
     * @param args Arguments.
     * @throws Exception If startup failed.
     */
    public static void main(String[] args) throws Exception {
        try (GridClient client = client()) {
            GridClientData data = client.data("partitioned");

            for (int i = 0; i < 10; i++)
                data.put(i, String.valueOf(i));

            for (int i = 0; i < 10; i++)
                data.get(i);

            for (int i = 0; i < 10; i++)
                data.remove(i);
        }
    }

    /**
     * @return Client instance.
     * @throws Exception If client start failed.
     */
    private static GridClient client() throws Exception {
        GridClientConfiguration cfg = new GridClientConfiguration();

        cfg.setServers(Arrays.asList(SRV_ADDR));

        GridClientDataConfiguration data = new GridClientDataConfiguration();

        data.setName(DataNodeStartup.CACHE_NAME);

        cfg.setDataConfigurations(Arrays.asList(data));

        cfg.setSecurityCredentialsProvider(new GridSecurityCredentialsBasicProvider(
            new GridSecurityCredentials(CLIENT_LOGIN, CLIENT_PASSWORD)));

        return GridClientFactory.start(cfg);
    }
}
