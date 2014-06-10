/* @java.file.header */

/*  _________        _____ __________________        _____
 *  __  ____/___________(_)______  /__  ____/______ ____(_)_______
 *  _  / __  __  ___/__  / _  __  / _  / __  _  __ `/__  / __  __ \
 *  / /_/ /  _  /    _  /  / /_/ /  / /_/ /  / /_/ / _  /  _  / / /
 *  \____/   /_/     /_/   \_,__/   \____/   \__,_/  /_/   /_/ /_/
 */

package org.gridgain.examples.task;

import org.gridgain.grid.GridException;
import org.gridgain.grid.compute.GridComputeJob;
import org.gridgain.grid.compute.GridComputeJobAdapter;
import org.gridgain.grid.compute.GridComputeJobResult;
import org.gridgain.grid.compute.GridComputeTaskSplitAdapter;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 *
 */
public class CharCountTask extends GridComputeTaskSplitAdapter<String, Integer> {
    /** {@inheritDoc} */
    @Override protected Collection<? extends GridComputeJob> split(int gridSize, String arg) throws GridException {
        String[] splits = arg.split(" ");

        Collection<GridComputeJob> res = new ArrayList<>(splits.length);

        for (String split : splits)
            res.add(new CharCountJob(split));

        return res;
    }

    /** {@inheritDoc} */
    @Nullable @Override public Integer reduce(List<GridComputeJobResult> res) throws GridException {
        int sum = 0;

        for (GridComputeJobResult r : res) {
            if (r.getException() != null)
                throw r.getException();

            sum += r.<Integer>getData();
        }

        return sum;
    }

    private static class CharCountJob extends GridComputeJobAdapter {
        /** Message split */
        private String split;

        /**
         * @param split Split.
         */
        public CharCountJob(String split) {
            this.split = split;
        }

        /** {@inheritDoc} */
        @Nullable @Override public Object execute() throws GridException {
            return split.length();
        }
    }
}
