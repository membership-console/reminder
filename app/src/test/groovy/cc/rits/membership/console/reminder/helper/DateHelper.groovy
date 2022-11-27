package cc.rits.membership.console.reminder.helper


import java.time.LocalDateTime
import java.time.ZoneOffset
import java.time.temporal.ChronoUnit

/**
 * 日付ヘルパー
 */
class DateHelper {

    /**
     * 日付を作成
     *
     * @param year 年
     * @param month 月
     * @param day 日
     * @return 日付
     */
    static LocalDateTime build(final Integer year, final Integer month, final Integer day) {
        return LocalDateTime.of(year, month, day, 0, 0)
    }

    /**
     * 明日の日付を取得
     *
     * @return 明日の日付
     */
    static LocalDateTime tomorrow() {
        return LocalDateTime.now().plusDays(1)
    }

    /**
     * 今日の日付を取得
     *
     * @return 今日の日付
     */
    static LocalDateTime today() {
        return LocalDateTime.now()
    }

    /**
     * 昨日の日付を取得
     *
     * @return 昨日の日付
     */
    static LocalDateTime yesterday() {
        return LocalDateTime.now().minusDays(1)
    }

    /**
     * 2つの日付が分まで一致するかチェック
     *
     * @param date1 date
     * @param date2 date
     * @return チェック結果
     */
    static boolean isSameMinutes(final LocalDateTime date1, final LocalDateTime date2) {
        final instant1 = date1.toInstant(ZoneOffset.UTC).truncatedTo(ChronoUnit.MINUTES)
        final instant2 = date2.toInstant(ZoneOffset.UTC).truncatedTo(ChronoUnit.MINUTES)
        return instant1 == instant2
    }

}
