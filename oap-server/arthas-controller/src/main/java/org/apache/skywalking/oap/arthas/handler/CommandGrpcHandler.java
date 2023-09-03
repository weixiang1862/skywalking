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

import io.grpc.stub.StreamObserver;
import lombok.extern.slf4j.Slf4j;
import org.apache.skywalking.apm.network.arthas.ArthasCommandServiceGrpc;
import org.apache.skywalking.apm.network.arthas.Command;
import org.apache.skywalking.apm.network.arthas.CommandRequest;
import org.apache.skywalking.apm.network.arthas.CommandResponse;
import org.apache.skywalking.oap.arthas.CommandQueue;
import org.apache.skywalking.oap.server.library.server.grpc.GRPCHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Slf4j
public class CommandGrpcHandler extends ArthasCommandServiceGrpc.ArthasCommandServiceImplBase implements GRPCHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(CommandGrpcHandler.class);

    @Override
    public void get(final CommandRequest request, final StreamObserver<CommandResponse> responseObserver) {
        CommandResponse.Builder builder = CommandResponse.newBuilder().setCommand(Command.NONE);
        CommandQueue.consumeCommand(request.getServiceName(), request.getInstanceName())
                    .ifPresent(command -> {
                        LOGGER.info(
                            "consume {} command for service {}, instance {}", command, request.getServiceName(),
                            request.getInstanceName()
                        );
                        builder.setCommand(command);
                    });
        responseObserver.onNext(builder.build());
        responseObserver.onCompleted();
    }
}
