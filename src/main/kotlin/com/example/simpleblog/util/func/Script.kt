package com.example.simpleblog.util.func

import jakarta.servlet.http.HttpServletResponse
import java.io.PrintWriter

fun responseData(resp: HttpServletResponse, jsonData: String?) {
    println("Response Data: $jsonData")
    resp.setHeader("Content-Type", "application/json; charset=utf-8")
    val out: PrintWriter = resp.writer
    try {
        out.use {
            out.println(jsonData)
        }
    } catch (e: Exception) {
        e.printStackTrace()
    }

}