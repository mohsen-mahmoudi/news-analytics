package com.demo.microservice_2021.kafka.streams.api;

import com.demo.microservice_2021.kafka.streams.model.KafkaStreamsResponseModel;
import com.demo.microservice_2021.kafka.streams.runner.KafkaStreamRunner;
import com.demo.microservice_2021.kafka.streams.runner.StreamRunner;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotEmpty;

@PreAuthorize("isAuthenticated()")
@RestController
@RequestMapping("/")
public class KafkaStreamsController {

    private static final Logger LOG = LoggerFactory.getLogger(KafkaStreamsController.class);

    private final StreamRunner kafkaStreamRunner;

    public KafkaStreamsController(KafkaStreamRunner kafkaStreamRunner) {
        this.kafkaStreamRunner = kafkaStreamRunner;
    }

    @GetMapping("/get-word-count-by-word/{word}")
    @Operation(summary = "getWordCountByWord")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "success", content = {
                    @Content(schema = @Schema(implementation = KafkaStreamsResponseModel.class))
            }),
            @ApiResponse(responseCode = "400", description = "not found"),
            @ApiResponse(responseCode = "500", description = "unexpected error")
    })
    @ResponseBody
    public ResponseEntity<KafkaStreamsResponseModel> getWordCountByWord(
            @PathVariable @NotEmpty String word) {
        Long count = kafkaStreamRunner.getValueByKey(word);
        LOG.info("getWordCountByWord returned {}", count);
        return ResponseEntity.ok(KafkaStreamsResponseModel.builder()
                .word(word)
                .wordCount(count)
                .build());
    }
}
