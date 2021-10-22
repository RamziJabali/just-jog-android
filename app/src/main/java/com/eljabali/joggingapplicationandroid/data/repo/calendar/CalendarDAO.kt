package com.eljabali.joggingapplicationandroid.data.repo.calendar

import androidx.room.*
import io.reactivex.Completable
import io.reactivex.Maybe
import io.reactivex.Observable
import io.reactivex.Single

@Dao
interface CalendarDAO {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun addJogDate(calendarJogDate: CalendarJogDate): Completable

    @Query("SELECT * FROM user_jog_schedule")
    fun getAll(): Observable<List<CalendarJogDate>>

    @Query("SELECT * FROM calendar_jog_dates WHERE date LIKE (:stringDate)")
    fun getByDate(stringDate: String): Maybe<List<CalendarJogDate>>

    @Query("SELECT * FROM calendar_jog_dates WHERE date BETWEEN (:startDate) AND (:endDate)")
    fun getByRangeOfDates(startDate: String, endDate: String): Observable<List<CalendarJogDate>>

    @Query("DELETE FROM calendar_jog_dates")
    fun deleteAll(): Completable

    @Query("SELECT * FROM calendar_jog_dates ORDER BY id DESC LIMIT 1")
    fun getLastJogDate(): Maybe<CalendarJogDate>

    @Delete
    fun delete(calendarJogDate: CalendarJogDate): Single<Int>
}