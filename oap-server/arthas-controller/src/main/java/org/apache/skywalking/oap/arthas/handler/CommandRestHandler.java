/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package org.apache.skywalking.oap.arthas.handler;

import com.linecorp.armeria.common.HttpResponse;
import com.linecorp.armeria.server.annotation.Blocking;
import com.linecorp.armeria.server.annotation.Post;
import lombok.extern.slf4j.Slf4j;
import org.apache.skywalking.apm.network.arthas.Command;
import org.apache.skywalking.apm.network.arthas.CommandRequest;
import org.apache.skywalking.oap.arthas.CommandQueue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Slf4j
public class CommandRestHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(CommandRestHandler.class);

    @Blocking
    @Post("/arthas/start")
    public HttpResponse start(final CommandRequest request) throws Exception {
        LOGGER.info(
            "produce start command for service {}, instance {}", request.getServiceName(), request.getInstanceName()
        );

        CommandQueue.produceCommand(request.getServiceName(), request.getInstanceName(), Command.START);
        return HttpResponse.of(200);
    }

    @Blocking
    @Post("/arthas/stop")
    public HttpResponse stop(final CommandRequest request) throws Exception {
        LOGGER.info(
            "produce stop command for service {}, instance {}", request.getServiceName(), request.getInstanceName()
        );

        CommandQueue.produceCommand(request.getServiceName(), request.getInstanceName(), Command.STOP);
        return HttpResponse.of(200);
    }
}
