package com.hoon.calendardiaryapp.data.api

import com.hoon.calendardiaryapp.data.api.response.HolidayResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface HolidayApiService {
    /**
     * @param year : 'yyyy' format
     */

    @GET("/api/v3/PublicHolidays/{year}/KR")
    suspend fun searchHolidays(
        @Path("year") year: String
    ): Response<HolidayResponse>
}