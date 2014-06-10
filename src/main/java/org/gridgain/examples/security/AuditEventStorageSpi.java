/* @java.file.header */

/*  _________        _____ __________________        _____
 *  __  ____/___________(_)______  /__  ____/______ ____(_)_______
 *  _  / __  __  ___/__  / _  __  / _  / __  _  __ `/__  / __  __ \
 *  / /_/ /  _  /    _  /  / /_/ /  / /_/ /  / /_/ / _  /  _  / / /
 *  \____/   /_/     /_/   \_,__/   \____/   \__,_/  /_/   /_/ /_/
 */

package org.gridgain.examples.security;

import org.gridgain.grid.*;
import org.gridgain.grid.events.*;
import org.gridgain.grid.lang.*;
import org.gridgain.grid.logger.*;
import org.gridgain.grid.resources.*;
import org.gridgain.grid.security.*;
import org.gridgain.grid.spi.*;
import org.gridgain.grid.spi.eventstorage.*;

import java.util.*;

import static org.gridgain.grid.events.GridEventType.*;

/**
 * Sample event storage SPI which logs cache and authentication events.
 */
@GridSpiMultipleInstancesSupport(true)
public class AuditEventStorageSpi extends GridSpiAdapter implements GridEventStorageSpi {
    /** Injected logger. */
    @GridLoggerResource
    private GridLogger log;

    /** {@inheritDoc} */
    @Override public <T extends GridEvent> Collection<T> localEvents(GridPredicate<T> tGridPredicate) {
        // We do not store any events.
        return Collections.emptyList();
    }

    /** {@inheritDoc} */
    @Override public void record(GridEvent evt) throws GridSpiException {
        if (evt.type() == EVT_CACHE_OBJECT_PUT ||
            evt.type() == EVT_CACHE_OBJECT_READ ||
            evt.type() == EVT_CACHE_OBJECT_REMOVED) {
            GridCacheEvent e = (GridCacheEvent)evt;

            UUID subjId = e.subjectId();

            try {
                GridSecuritySubject subj = getSpiContext().authenticatedSubject(subjId);

                log.error("Cache event [evt=" + e.name() +
                    ", subjId=" + subjId +
                    ", subjType=" + subj.type() +
                    ", addr=" + subj.address() +
                    ", login=" + subj.login() + ']');
            }
            catch (GridException ex) {
                log.error("Failed to get authenticated subject for grid event: " + evt, ex);
            }
        }
        else if (evt.type() == EVT_AUTHORIZATION_FAILED) {
            GridAuthorizationEvent e = (GridAuthorizationEvent)evt;

            log.error("Authorization failed [evt=" + e.name() +
                ", perm=" + e.operation() +
                ", subjId=" + e.subject().id() +
                ", subjType=" + e.subject().type() +
                ", addr=" + e.subject().address() +
                ", login=" + e.subject().login() + ']');
        }
        else if (evt.type() == EVT_AUTHENTICATION_FAILED ||
                evt.type() == EVT_AUTHENTICATION_SUCCEEDED) {
            GridAuthenticationEvent e = (GridAuthenticationEvent)evt;

            log.error("Authentication event [evt=" + e.name() + ", subjId=" + e.subjectId() + ", " +
                "subjType=" + e.subjectType() + ", login=" + e.login() + ']');
        }
    }

    /** {@inheritDoc} */
    @Override public void spiStart(String s) throws GridSpiException {
        // No-op.
    }

    /** {@inheritDoc} */
    @Override public void spiStop() throws GridSpiException {
        // No-op.
    }
}
