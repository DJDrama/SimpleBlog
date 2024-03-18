package com.example.simpleblog.config

import com.p6spy.engine.common.P6Util
import com.p6spy.engine.logging.Category
import com.p6spy.engine.spy.P6SpyOptions
import com.p6spy.engine.spy.appender.MessageFormattingStrategy
import jakarta.annotation.PostConstruct
import org.hibernate.engine.jdbc.internal.FormatStyle
import org.springframework.context.annotation.Configuration
import org.springframework.data.jpa.repository.config.EnableJpaAuditing
import java.util.*

@Configuration
@EnableJpaAuditing
class LoggingConfig {

    @PostConstruct
    fun setLogMessageFormat() {
        P6SpyOptions.getActiveInstance().logMessageFormat = P6spySqlFormatConfiguration::class.java.name
    }
}

class P6spySqlFormatConfiguration : MessageFormattingStrategy {
    companion object {
        private const val SQL_CREATE = "create"
        private const val SQL_ALTER = "alter"
        private const val SQL_COMMENT = "comment"
    }

    // apply sql format
    override fun formatMessage(
        connectionId: Int,
        now: String?,
        elapsed: Long,
        category: String?,
        prepared: String?,
        sql: String?,
        url: String?
    ): String =
        now + "|" + elapsed + "ms|" + category + "|connection " + connectionId + "|" + P6Util.singleLine(prepared) + formatSql(
            category,
            sql
        )

    private fun formatSql(category: String?, sql: String?): String? {
        var resultSql = "";
        if (sql == null || sql.trim() == "")
            return resultSql

        // Only format Statement, distinguish DDL And DML
        if (Category.STATEMENT.name == category) {
            val tempSql = sql.trim().toLowerCase(Locale.ROOT)
            resultSql =
                if (tempSql.startsWith(SQL_CREATE) || tempSql.startsWith(SQL_ALTER) || tempSql.startsWith(SQL_COMMENT))
                    FormatStyle.DDL.formatter.format(sql)
                else
                    FormatStyle.BASIC.formatter.format(sql)
            resultSql = "|\nHeFormatSql(P6Spy sql,Hibernate format):$resultSql"
        }
        return resultSql
    }
}