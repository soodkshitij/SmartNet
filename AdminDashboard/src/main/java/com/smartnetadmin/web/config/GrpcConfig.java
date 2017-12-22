package com.smartnetadmin.web.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import com.smartnet.grpc.SmartnetGrpc;
import com.smartnet.grpc.SmartnetGrpc.SmartnetBlockingStub;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;


@Configuration
@ComponentScan("com.smartnetadmin.*")
public class GrpcConfig {
	
	@Autowired
	@Bean(name = "grpcClient")
	public SmartnetBlockingStub getGrpcClient() {
		ManagedChannel channel = ManagedChannelBuilder.forAddress("localhost", 3000).usePlaintext(true).build();
		SmartnetGrpc.SmartnetBlockingStub blockingStub = SmartnetGrpc.newBlockingStub(channel);
		return blockingStub;
	}
	
}
