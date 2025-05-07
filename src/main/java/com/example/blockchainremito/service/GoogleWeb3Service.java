package com.example.blockchainremito.service;

import java.math.BigInteger;
import java.time.Instant;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.web3j.crypto.Credentials;
import org.web3j.crypto.RawTransaction;
import org.web3j.crypto.TransactionEncoder;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.DefaultBlockParameterName;
import org.web3j.protocol.core.methods.response.EthSendTransaction;
import org.web3j.protocol.core.methods.response.Transaction;
import org.web3j.protocol.core.methods.response.EthBlock.Block;
import org.web3j.utils.Convert;
import org.web3j.utils.Numeric;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import lombok.experimental.var;

@Service
@FieldDefaults(level = AccessLevel.PRIVATE)
public class GoogleWeb3Service {

    Web3j web3j;

    public GoogleWeb3Service(@Qualifier("web3j") Web3j web3j) {
        this.web3j = web3j;
    }

    public String publishHashToBlockchain(String fromPrivateKey, String hashHex) throws Exception {
        Credentials credentials = Credentials.create(fromPrivateKey);
    
        BigInteger nonce = web3j.ethGetTransactionCount(
                credentials.getAddress(),
                DefaultBlockParameterName.LATEST
        ).send().getTransactionCount();
    
        BigInteger gasPrice = Convert.toWei("20", Convert.Unit.GWEI).toBigInteger();
        BigInteger gasLimit = BigInteger.valueOf(50000);
    
        RawTransaction tx = RawTransaction.createTransaction(
                nonce,
                gasPrice,
                gasLimit,
                credentials.getAddress(), // enviártelo a vos mismo
                BigInteger.ZERO,
                hashHex // <--- AQUÍ VA EL HASH
        );
    

        Long chainId = 11155111L; // este es el id de la blockchain de sepolia testnet de ethereum
        byte[] signedMessage = TransactionEncoder.signMessage(tx, chainId, credentials);
        String hexValue = Numeric.toHexString(signedMessage);
    
        EthSendTransaction sendTx = web3j.ethSendRawTransaction(hexValue).send();
    
        if (sendTx.hasError()) {
            throw new RuntimeException("Error al enviar tx: " + sendTx.getError().getMessage());
        }
    
        return sendTx.getTransactionHash();
    }


    /**
     * Funcion asyncrona que obtiene el metadata de la transaccion
     * esto es debido a que el bloque puede tardar en ser generado
     * por lo que se debe esperar a que se genere el bloque
     * y luego obtener el metadata de la transaccion
     */
    @Async
    public CompletableFuture<Map<String, Object>> getTxMetadata(String txHash) throws Exception {
        Transaction tx = getTransaction(txHash).get();
        Block block = generateBlock(tx).get();


        Map<String, Object> result = new HashMap<>();
        result.put("txHash", txHash);
        result.put("blockNumber", block.getNumber());
        result.put("timestamp", Instant.ofEpochSecond(block.getTimestamp().longValue()));
        return CompletableFuture.completedFuture(result);

    }
    

        
    private Optional<Transaction> getTransaction(String txHash) throws Exception {
        
        Optional<Transaction> tx = Optional.empty();

        while (tx.isEmpty() || tx.get().getBlockHash() == null || tx.get().getBlockHash().isEmpty()) {
            try{
                tx = web3j.ethGetTransactionByHash(txHash).sendAsync().get().getTransaction();
                if (tx.isEmpty() || tx.get().getBlockHash() == null || tx.get().getBlockHash().isEmpty()) {
                    Thread.sleep(1000); // Espera 2 segundos antes de volver a intentar
                }
            } catch (Exception e) {
                System.out.println("Error al obtener la transaccion: " + e.getMessage());
            }
        }
        return tx;
    }

    private Optional<Block> generateBlock(Transaction tx) throws Exception {
        Optional<Block> block = Optional.empty();
        long backoffTime = 1000;  // 1 segundos
        while (block.isEmpty()) {

            try{
                block = Optional.ofNullable(web3j.ethGetBlockByHash(tx.getBlockHash(), false).sendAsync().get().getBlock());
            
                if (block.isEmpty()) {
                    Thread.sleep(backoffTime); // Espera 2 segundos antes de volver a intentar
                }
            } catch (Exception e) {
                throw new Exception("Error al obtener el bloque: " + e.getMessage());
            }
        }

        return block;
    }    
    
    
}
