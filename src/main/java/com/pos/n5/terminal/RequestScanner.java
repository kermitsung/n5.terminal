package com.pos.n5.terminal;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.List;
import java.util.Map;
import java.util.regex.PatternSyntaxException;
import java.util.stream.Collectors;

@Component
public class RequestScanner {

    @Value("${n5.request.folder}")
    private String folderPath;

    @Value("${n5.socket.address}")
    private String socketAddress;

    @Scheduled(fixedDelay = 1000)
    public void parseRequestFiles() throws IOException {
         List<Path> paths = Files.walk(Paths.get(folderPath))
                 .filter(path -> !path.toString().contains("response") && Files.isRegularFile(path))
                 .collect(Collectors.toList());

        for (Path path : paths) {
            try {
                List<String> lines = Files.readAllLines(path);
                Map<String, String> request = lines.stream().collect(Collectors.toMap(line -> line.split(":")[0].trim(), line -> line.split(":")[1].trim()));
                request.forEach((key, value) -> System.out.println(key + ": " + value));
                if (requestValidation(request)) {
                    Files.write(path, System.lineSeparator().getBytes(), StandardOpenOption.APPEND);
                    Files.write(path, "======Invalid Payment Request======".getBytes(), StandardOpenOption.APPEND);
                    continue;
                }

                Map<?, ?> response = N5SaleRequest.saleSocket(socketAddress, new BigDecimal(request.get("amount")));
                if (response != null) {
                    response.forEach((key, value) -> System.out.println(key + ": " + value));
                    Files.write(path, System.lineSeparator().getBytes(), StandardOpenOption.APPEND);
                    Files.write(path, "======Payment Response======".getBytes(), StandardOpenOption.APPEND);
                    for (Map.Entry<?, ?> entry : response.entrySet()) {
                        Object key = entry.getKey();
                        Object value = entry.getValue();
                        String responseField = key + ": " + value;
                        Files.write(path, System.lineSeparator().getBytes(), StandardOpenOption.APPEND);
                        Files.write(path, responseField.getBytes(), StandardOpenOption.APPEND);
                    }
                }
            } catch (IOException e) {
                Files.write(path, System.lineSeparator().getBytes(), StandardOpenOption.APPEND);
                Files.write(path, "======File Reading Exception======".getBytes(), StandardOpenOption.APPEND);
            } catch (PatternSyntaxException e) {
                Files.write(path, System.lineSeparator().getBytes(), StandardOpenOption.APPEND);
                Files.write(path, "======Invalid Payment Request Format======".getBytes(), StandardOpenOption.APPEND);
            }
            renaming(path, getFileNameWithoutExtension(path.getFileName().toString()) + "-response.txt");
        }
    }

    private boolean requestValidation(Map<String, String> request) {
        return request.containsKey("orderId") &&request.containsKey("amount");
    }

    private String getFileNameWithoutExtension(String fileName) {
        return fileName.replaceFirst("[.][^.]+$", "");
    }

    private void renaming(Path oldName, String newNameString) throws IOException {
        Files.move(oldName, oldName.resolveSibling(newNameString));
    }
}
