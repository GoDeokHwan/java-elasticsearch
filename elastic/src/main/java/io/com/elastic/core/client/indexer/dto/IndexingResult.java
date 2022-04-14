package io.com.elastic.core.client.indexer.dto;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.action.DocWriteResponse;
import org.elasticsearch.action.delete.DeleteResponse;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static java.util.Optional.ofNullable;

@Slf4j
@Getter
public class IndexingResult {
    private String failureMessage;
    private long succedCount = -1;
    private int requestSize = 0;
    private List<String> failedIdList;

    private static final List<DocWriteResponse.Result> UPSERT_SUCCESS_RESULT =
            Collections.unmodifiableList(Arrays.asList(DocWriteResponse.Result.CREATED, DocWriteResponse.Result.UPDATED));
    private static final List<DocWriteResponse.Result> DELETE_SUCCESS_RESULT =
            Collections.unmodifiableList(Arrays.asList(DocWriteResponse.Result.DELETED, DocWriteResponse.Result.NOT_FOUND));

    public static IndexingResult of(String failureMessage, List<String> failedIdList, int requestSize) {
        IndexingResult bulkResult = new IndexingResult();
        bulkResult.failureMessage = failureMessage;
        bulkResult.failedIdList = failedIdList;
        bulkResult.succedCount = requestSize - failedIdList.size();
        bulkResult.requestSize = requestSize;
        return bulkResult;
    }

    public static IndexingResult of(String failureMessage, String failedId) {
        return of(failureMessage, Collections.singletonList(failedId), 1);
    }

    public static IndexingResult of(String failureMessage, List<String> failedIds) {
        return of(failureMessage, failedIds, 1);
    }

    public static IndexingResult ofFailure() {
        return ofFailure("Failure!");
    }

    public static IndexingResult ofFailure(String errorMessage) {
        IndexingResult bulkResult = new IndexingResult();
        bulkResult.failureMessage = errorMessage;
        return bulkResult;
    }

    public static IndexingResult getUpsertIndexingResult(DocWriteResponse writeResponse, String id, String errorMessage) {
        return ofNullable(writeResponse)
                .map(upsertResult -> {
                            if (UPSERT_SUCCESS_RESULT.contains(upsertResult.getResult())) {
                                return ofSucced(1);
                            }
                            return of(errorMessage, id);
                        }
                ).orElse(ofFailure(errorMessage));
    }

    public static IndexingResult getDeleteResult(DeleteResponse deleteResponse, String productId, String errorMessage) {
        return ofNullable(deleteResponse).map(result -> {
            if (DELETE_SUCCESS_RESULT.contains(deleteResponse.getResult())) {
                return ofSucced(1);
            }
            return of(errorMessage, productId);
        }).orElse(ofFailure());
    }

    public static IndexingResult ofSucced(int succedCount) {
        IndexingResult bulkResult = new IndexingResult();
        bulkResult.requestSize = succedCount;
        bulkResult.succedCount = succedCount;
        return bulkResult;
    }

    public boolean failure() {
        return !succed();
    }

    public boolean succed() {
        return succedCount == requestSize;
    }
}
