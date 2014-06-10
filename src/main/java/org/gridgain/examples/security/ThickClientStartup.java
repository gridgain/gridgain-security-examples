/* @java.file.header */

/*  _________        _____ __________________        _____
 *  __  ____/___________(_)______  /__  ____/______ ____(_)_______
 *  _  / __  __  ___/__  / _  __  / _  / __  _  __ `/__  / __  __ \
 *  / /_/ /  _  /    _  /  / /_/ /  / /_/ /  / /_/ / _  /  _  / / /
 *  \____/   /_/     /_/   \_,__/   \____/   \__,_/  /_/   /_/ /_/
 */

package org.gridgain.examples.security;

import org.gridgain.grid.*;
import org.gridgain.grid.cache.*;

/**
 * Starts client node and performs a number of cache operations.
 */
public class ThickClientStartup {
    /**
     * @param args Arguments.
     * @throws Exception If startup failed.
     */
    public static void main(String[] args) throws Exception {
        // Uncomment to disable quiet logging mode (INFO logging is disabled in quiet mode).
        //System.setProperty("GRIDGAIN_QUIET", "false");

        try (Grid g = GridGain.start("config/security-client-node.xml")) {
            System.out.println("Client node started: " + g.localNode().id());

            GridCache<Integer, String> c = g.cache(DataNodeStartup.CACHE_NAME);

            for (int i = 0; i < 10; i++)
                c.putx(i, String.valueOf(i));

            for (int i = 0; i < 10; i++)
                c.get(i);

            for (int i = 0; i < 10; i++)
                c.removex(i);
        }
    }
}
