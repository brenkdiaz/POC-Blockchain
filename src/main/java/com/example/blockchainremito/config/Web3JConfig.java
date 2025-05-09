package com.example.blockchainremito.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.http.HttpService;


@Configuration
public class Web3JConfig {
    
    @Value("${google.cloud.web3.uri}")
    private String gcpRpcUrl;

    @Bean(name = "web3j")
    public Web3j web3j() {
        return Web3j.build(new HttpService(gcpRpcUrl));
    }
}
