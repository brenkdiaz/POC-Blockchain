package com.example.blockchainremito;

import java.time.LocalDate;
import java.util.concurrent.CompletableFuture;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableAsync;
import com.example.blockchainremito.model.Remito;
import com.example.blockchainremito.service.RemitoService;

@SpringBootApplication
@EnableAsync
public class BlockchainRemitoPocApplication {

    public static void main(String[] args) {
        SpringApplication.run(BlockchainRemitoPocApplication.class, args);
    }

    
}
