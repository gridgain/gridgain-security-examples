﻿/* @csharp.file.header */

/*  _________        _____ __________________        _____
 *  __  ____/___________(_)______  /__  ____/______ ____(_)_______
 *  _  / __  __  ___/__  / _  __  / _  / __  _  __ `/__  / __  __ \
 *  / /_/ /  _  /    _  /  / /_/ /  / /_/ /  / /_/ / _  /  _  / / /
 *  \____/   /_/     /_/   \_,__/   \____/   \__,_/  /_/   /_/ /_/
 */

namespace GridGain.Security.Example
{
    using System;
    using System.Collections.Generic;
    using System.Diagnostics;
    using System.Linq;
    using System.Text;
    using GridGain.Client;

    using GridGain.Client.Balancer;

    using X = System.Console;

    /**
     * <summary>
     * This example demonstrates use of C# remote Compute Client API. To execute
     * this example you should start an instance of <c>ClientExampleNodeStartup</c>
     * Java class which will start up a GridGain node with proper configuration.
     * <para/>
     * Note that this example requires <c>org.gridgain.examples.misc.client.api.ClientExampleTask</c>
     * class to be present in remote nodes' classpath. If remote nodes are run by <c>ggstart.{sh|bat}</c> script
     * then <c>gridgain-examples.jar</c> file should be placed to <c>GRIDGAIN_HOME/libs/ext</c> folder.
     * You can build <c>gridgain-examples.jar</c> by running <c>mvn package</c> in <c>GRIDGAIN_HOME/examples</c>
     * folder. After that <c>gridgain-examples.jar</c> will be generated by Maven in
     * <c>GRIDGAIN_HOME/examples/target</c> folder.
     * <para/>
     * After node has been started this example creates a client and executes few test tasks.
     * <para/>
     * Note that different nodes cannot share the same port for rest services. If you want
     * to start more than one node on the same physical machine you must provide different
     * configurations for each node. Otherwise, this example would not work.</summary>
     */
    public class GridClientApiExample
    {
        /** <summary>Grid node address to connect to.</summary> */
        private static readonly String ServerAddress = "127.0.0.1";

        /**
         * <summary>
         * Starts up an empty node with specified configuration, then runs client compute example.</summary>
         *
         * <exception cref="GridClientException">If failed.</exception>
         */
        [STAThread]
        public static void Main()
        {
            /* Enable debug messages. */
            //Debug.Listeners.Add(new TextWriterTraceListener(System.Console.Out));

            String taskName = "org.gridgain.examples.task.CharCountTask";
            String taskArg = "Hello Dot Net World";

            IGridClient client = CreateClient();

            try
            {
                // Show grid topology.
                X.WriteLine(">>> Client created, current grid topology: " + ToString(client.Compute().Nodes()));

                IGridClientCompute prj = client.Compute();

                // Execute test task that will count total number of nodes in grid.
                int wordCnt = prj.Execute<int>(taskName, taskArg);

                X.WriteLine(">>> Task result [args='" + taskArg + "', wordCnt=" + wordCnt + ']');

            }
            catch (GridClientException e)
            {
                Console.WriteLine("Unexpected grid client exception happens: {0}", e);
            }
            finally
            {
                GridClientFactory.StopAll();
            }
        }

        /**
         * <summary>
         * This method will create a client with default configuration. Note that this method expects that
         * first node will bind rest binary protocol on default port.</summary>
         *
         * <returns>Client instance.</returns>
         * <exception cref="GridClientException">If client could not be created.</exception>
         */
        private static IGridClient CreateClient()
        {
            var cacheCfg = new GridClientDataConfiguration();

            // Set remote cache name.
            cacheCfg.Name = "partitioned";

            // Set client partitioned affinity for this cache.
            cacheCfg.Affinity = new GridClientPartitionAffinity();

            var cfg = new GridClientConfiguration();

            cfg.DataConfigurations.Add(cacheCfg);

            cfg.Credentials = "good.user:password";

            // Point client to a local node. Note that this server is only used
            // for initial connection. After having established initial connection
            // client will make decisions which grid node to use based on collocation
            // with key affinity or load balancing.
            cfg.Servers.Add(ServerAddress + ':' + GridClientConfiguration.DefaultTcpPort);

            return GridClientFactory.Start(cfg);
        }

        /**
         * <summary>
         * Concatenates the members of a collection, using the specified separator between each member.</summary>
         *
         * <param name="list">A collection that contains the objects to concatenate.</param>
         * <param name="separator">The string to use as a separator.</param>
         * <returns>A string that consists of the members of values delimited by the separator string.</return>
         */
        public static String ToString<T>(IEnumerable<T> list, String separator = ",")
        {
            return list == null ? "null" : String.Join(separator, list);
        }
    }
}