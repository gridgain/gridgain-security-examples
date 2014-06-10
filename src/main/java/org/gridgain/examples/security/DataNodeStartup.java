/* @java.file.header */

/*  _________        _____ __________________        _____
 *  __  ____/___________(_)______  /__  ____/______ ____(_)_______
 *  _  / __  __  ___/__  / _  __  / _  / __  _  __ `/__  / __  __ \
 *  / /_/ /  _  /    _  /  / /_/ /  / /_/ /  / /_/ / _  /  _  / / /
 *  \____/   /_/     /_/   \_,__/   \____/   \__,_/  /_/   /_/ /_/
 */

package org.gridgain.examples.security;

import org.gridgain.grid.*;

/**
 *
 */
public class DataNodeStartup {
    /** Cache name. */
    public static final String CACHE_NAME = "partitioned";

    /**
     * @param args Arguments.
     * @throws Exception If startup failed.
     */
    public static void main(String[] args) throws Exception {
        try (Grid g = GridGain.start("config/security-data-node.xml")) {
            System.out.println("Press 'Enter' to exit data node.");

            // Wait until killed by user.
            System.in.read();
        }
    }
}
