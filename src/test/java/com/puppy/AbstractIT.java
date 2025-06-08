package com.puppy;

import com.github.database.rider.core.api.dataset.DataSet;
import com.github.database.rider.junit5.DBUnitExtension;
import java.util.function.Consumer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionTemplate;

@ActiveProfiles("it")
@ExtendWith(DBUnitExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public abstract class AbstractIT {

  @Autowired private TransactionTemplate transactionTemplate;

  private static final String CLEAN_ALL_TABLES_SQL = "datasets/clean-all-tables.sql";

  @BeforeEach
  @DataSet(executeScriptsBefore = CLEAN_ALL_TABLES_SQL)
  public void cleanDatabases() {}

  public void doInTransaction(Consumer<TransactionStatus> callback) {
    transactionTemplate.executeWithoutResult(callback);
  }
}
